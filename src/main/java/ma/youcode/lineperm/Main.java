package ma.youcode.lineperm;

import java.sql.Connection;
import java.sql.SQLException;

import ma.youcode.lineperm.db.DBConnection;
import ma.youcode.lineperm.db.DatabaseInitializer;
import ma.youcode.lineperm.ui.ConsoleApp;

import ma.youcode.lineperm.dao.UserDao;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.UserService;

public class Main {
    public static void main(String[] args) throws SQLException {
        // ConsoleApp app = new ConsoleApp();
        // app.demarrer();

        UserDao userDao = new UserDao();
        UserService userService = new UserService(userDao);

        boolean cree = userService.creerCompte(
                "test_user",
                "123456");

        System.out.println("=== CREATION ===");
        System.out.println("Compte cree : " + cree);

        System.out.println("\n=== EXISTENCE ===");

        boolean existe = userService.existe("test_user");

        System.out.println("Utilisateur existe : " + existe);

        System.out.println("\n=== CONNEXION CORRECTE ===");

        User user = userService.connecter(
                "test_user",
                "123456");

        if (user != null) {
            System.out.println("Connexion reussie !");
            System.out.println("ID : " + user.getId());
            System.out.println("Login : " + user.getLogin());
        } else {
            System.out.println("Connexion refusee.");
        }

    }

}