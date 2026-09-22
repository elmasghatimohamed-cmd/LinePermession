package ma.youcode.lineperm.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void init() {
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                          "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                          "login TEXT NOT NULL UNIQUE, " +
                          "password_hash TEXT NOT NULL);";

        String sqlFichiers = "CREATE TABLE IF NOT EXISTS fichiers (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             "nom TEXT NOT NULL UNIQUE, " +
                             "proprietaire TEXT NOT NULL, " +
                             "prop_r BOOLEAN NOT NULL DEFAULT 1, " +
                             "prop_w BOOLEAN NOT NULL DEFAULT 1, " +
                             "prop_d BOOLEAN NOT NULL DEFAULT 1, " +
                             "aut_r BOOLEAN NOT NULL DEFAULT 0, " +
                             "aut_w BOOLEAN NOT NULL DEFAULT 0, " +
                             "aut_d BOOLEAN NOT NULL DEFAULT 0, " +
                             "FOREIGN KEY (proprietaire) REFERENCES users(login) ON DELETE CASCADE);";

        String sqlLogs = "CREATE TABLE IF NOT EXISTS logs (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "date TEXT NOT NULL, " +
                         "heure TEXT NOT NULL, " +
                         "utilisateur TEXT NOT NULL, " +
                         "action TEXT NOT NULL, " +
                         "fichier TEXT NOT NULL, " +
                         "resultat TEXT NOT NULL);";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsers);
            stmt.execute(sqlFichiers);
            stmt.execute(sqlLogs);

            System.out.println("Initialisation des tables SQLite reussie.");

        } catch (SQLException e) {
            System.err.println("Erreur d initialisation de la base SQLite : " + e.getMessage());
        }
    }
}