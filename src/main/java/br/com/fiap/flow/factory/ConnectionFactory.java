package br.com.fiap.flow.factory;

import java.sql.*;

public class ConnectionFactory {

    // Defaults DEV (Oracle local)
    private static final String DEFAULT_ORACLE_URL =
            "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String DEFAULT_ORACLE_USER = "system";
    private static final String DEFAULT_ORACLE_PASSWORD = "12345";

    // Defaults PROD (H2 em memória)
    private static final String DEFAULT_H2_URL =
            "jdbc:h2:mem:flow;DB_CLOSE_DELAY=-1;MODE=Oracle";
    private static final String DEFAULT_H2_USER = "sa";
    private static final String DEFAULT_H2_PASSWORD = "sa";

    private static boolean h2Initialized = false;

    public static Connection getConnection() throws SQLException {

        // Descobre profile primeiro
        String profile = System.getProperty("quarkus.profile");
        if (profile == null || profile.isBlank()) {
            profile = System.getenv("QUARKUS_PROFILE");
        }
        if (profile == null || profile.isBlank()) {
            profile = "prod"; // jar no Railway sobe como prod
        }

        String url;
        String user;
        String password;

        if ("dev".equalsIgnoreCase(profile)) {
            // DEV usa Oracle (pode vir de env ou default)
            url = getEnvOrDefault("DB_URL", DEFAULT_ORACLE_URL);
            user = getEnvOrDefault("DB_USER", DEFAULT_ORACLE_USER);
            password = getEnvOrDefault("DB_PASSWORD", DEFAULT_ORACLE_PASSWORD);
        } else {
            // PROD FORÇA H2, mesmo que alguém tenha setado DB_URL sem querer
            url = DEFAULT_H2_URL;
            user = DEFAULT_H2_USER;
            password = DEFAULT_H2_PASSWORD;
        }

        loadDriverFor(url);
        Connection conn = DriverManager.getConnection(url, user, password);

        // Inicializa schema H2 só uma vez, se estiver em prod
        if (!"dev".equalsIgnoreCase(profile) && !h2Initialized) {
            initH2Schema(conn);
            h2Initialized = true;
        }

        return conn;
    }

    private static String getEnvOrDefault(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }

    private static void loadDriverFor(String url) {
        try {
            if (url.startsWith("jdbc:oracle")) {
                Class.forName("oracle.jdbc.OracleDriver");
            } else if (url.startsWith("jdbc:h2")) {
                Class.forName("org.h2.Driver");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC não encontrado para URL: " + url, e);
        }
    }

    // Cria tabela + sequence compatível com seu DAO Oracle-style
    private static void initH2Schema(Connection conn) {
        String ddlTable =
                "CREATE TABLE IF NOT EXISTS DDD_PROCESSOS (" +
                " codigo NUMBER PRIMARY KEY," +
                " titulo VARCHAR(100) NOT NULL," +
                " descricao VARCHAR(255) NOT NULL," +
                " data_criacao DATE" +
                ")";

        String ddlSeq =
                "CREATE SEQUENCE IF NOT EXISTS DDD_PROCESSOS_SEQ START WITH 1 INCREMENT BY 1";

        try (Statement st = conn.createStatement()) {
            st.execute(ddlTable);
            st.execute(ddlSeq);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar schema H2", e);
        }
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
