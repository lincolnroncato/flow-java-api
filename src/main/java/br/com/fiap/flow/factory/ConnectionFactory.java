package br.com.fiap.flow.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public static Connection getConnection() throws SQLException {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Se não veio nada do ambiente, decide pelo profile do Quarkus
        if (isBlank(url) || isBlank(user) || isBlank(password)) {
            String profile = System.getProperty("quarkus.profile", "prod"); 
            // em jar, geralmente é "prod"

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
        return DriverManager.getConnection(url, user, password);
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

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
