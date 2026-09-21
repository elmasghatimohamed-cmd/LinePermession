package ma.youcode.lineperm;

import java.sql.Connection;
import java.sql.SQLException;

import ma.youcode.lineperm.db.DBConnection;
import ma.youcode.lineperm.db.DatabaseInitializer;
import ma.youcode.lineperm.ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        // ConsoleApp app = new ConsoleApp();
        // app.demarrer();

        try {
            Connection connection = DBConnection.getConnection();

            if (connection != null && !connection.isClosed()) {
                System.out.println("Connexion SQLite reussie");
                System.out.println("Base de donnees : " + connection.getCatalog());
            }

        } catch (SQLException e) {
            System.out.println("Erreur de connexion a SQLite : " + e.getMessage());
        }
        DatabaseInitializer.init();
    }

}