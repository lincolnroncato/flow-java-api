package br.com.fiap.flow.factory;

import java.sql.*;

public class ConnectionFactory {

    private static final String DEFAULT_ORACLE_URL =
            "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String DEFAULT_ORACLE_USER = "system";
    private static final String DEFAULT_ORACLE_PASSWORD = "12345";

    private static final String DEFAULT_H2_URL =
            "jdbc:h2:mem:flow;DB_CLOSE_DELAY=-1;MODE=Oracle";
    private static final String DEFAULT_H2_USER = "sa";
    private static final String DEFAULT_H2_PASSWORD = "sa";

    private static volatile boolean h2Iniciado = false;

    public static Connection getConnection() throws SQLException {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (isBlank(url) || isBlank(user) || isBlank(password)) {
            String profile = System.getProperty("quarkus.profile", "prod");
            if ("dev".equalsIgnoreCase(profile)) {
                url = DEFAULT_ORACLE_URL;
                user = DEFAULT_ORACLE_USER;
                password = DEFAULT_ORACLE_PASSWORD;
            } else {
                url = DEFAULT_H2_URL;
                user = DEFAULT_H2_USER;
                password = DEFAULT_H2_PASSWORD;
            }
        }

        loadDriverFor(url);
        Connection conn = DriverManager.getConnection(url, user, password);

        // Se for H2 em prod, garante schema criado 1x
        if (url.startsWith("jdbc:h2") && !h2Iniciado) {
            synchronized (ConnectionFactory.class) {
                if (!h2Iniciado) {
                    inicializarH2(conn);
                    h2Iniciado = true;
                }
            }
        }

        return conn;
    }

    private static void inicializarH2(Connection conn) {
        // TROQUE este bloco pelo seu DDL real (vou te devolver certinho quando você mandar)
        String[] ddl = new String[] {
            "CREATE TABLE IF NOT EXISTS DDD_USUARIOS (" +
            " codigo NUMBER PRIMARY KEY, " +
            " nome VARCHAR2(100), " +
            " email VARCHAR2(100), " +
            " cargo VARCHAR2(50))",

            "CREATE TABLE IF NOT EXISTS DDD_PROCESSOS (" +
            " codigo NUMBER PRIMARY KEY, " +
            " titulo VARCHAR2(100), " +
            " descricao VARCHAR2(255), " +
            " data_criacao DATE)",

            "CREATE TABLE IF NOT EXISTS DDD_ETAPAS (" +
            " codigo NUMBER PRIMARY KEY, " +
            " cod_processo NUMBER, " +
            " titulo VARCHAR2(100), " +
            " descricao VARCHAR2(255), " +
            " ordem NUMBER, " +
            " tempo_estimado_min NUMBER, " +
            " CONSTRAINT fk_etapa_proc FOREIGN KEY (cod_processo) REFERENCES DDD_PROCESSOS(codigo))",

            "CREATE TABLE IF NOT EXISTS DDD_EXECUCOES (" +
            " codigo NUMBER PRIMARY KEY, " +
            " cod_usuario NUMBER, " +
            " cod_processo NUMBER, " +
            " cod_etapa NUMBER, " +
            " data_execucao DATE, " +
            " status VARCHAR2(30), " +
            " observacao VARCHAR2(255), " +
            " CONSTRAINT fk_exec_usuario FOREIGN KEY (cod_usuario) REFERENCES DDD_USUARIOS(codigo), " +
            " CONSTRAINT fk_exec_proc FOREIGN KEY (cod_processo) REFERENCES DDD_PROCESSOS(codigo))"
        };

        try (Statement st = conn.createStatement()) {
            for (String sql : ddl) st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao inicializar H2", e);
        }
    }

    private static void loadDriverFor(String url) {
        try {
            if (url.startsWith("jdbc:oracle")) Class.forName("oracle.jdbc.OracleDriver");
            else if (url.startsWith("jdbc:h2")) Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC não encontrado para URL: " + url, e);
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static void close(Connection conn) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
}
