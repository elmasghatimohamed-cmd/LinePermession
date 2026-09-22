package ma.youcode.lineperm;

import java.sql.Connection;
import java.sql.SQLException;

import ma.youcode.lineperm.db.DBConnection;
import ma.youcode.lineperm.db.DatabaseInitializer;
import ma.youcode.lineperm.ui.ConsoleApp;

import ma.youcode.lineperm.dao.UserDao;
import ma.youcode.lineperm.model.User;

public class Main {
    public static void main(String[] args) throws SQLException {
        // ConsoleApp app = new ConsoleApp();
        // app.demarrer();

        UserDao userDao = new UserDao();

        User user = new User(
                "architect_test",
                "hash_test");

        userDao.save(user);

        System.out.println("ID : " + user.getId());

        User found = userDao.findByLogin("architect_test");

        if (found != null) {
            System.out.println("Login : " + found.getLogin());
            System.out.println("Hash : " + found.getPasswordHash());
        }

        System.out.println("\n=== TOUS LES USERS ===");

        for (User u : userDao.findAll()) {
            System.out.println(
                    u.getId() + " - " + u.getLogin());
        }
    }

}