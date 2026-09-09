package ma.youcode.lineperm;

import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.UserService;

import ma.youcode.lineperm.ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        // UserService userService = new UserService();
        // userService.charger();
        // String login = "mohamed";
        // String motDePasse = "123456";

        // if (userService.creerCompte(login, motDePasse)) {
        // System.out.println("Compte cree avec succes !");
        // } else {
        // System.out.println("Le compte existe deja.");
        // }

        // User user = userService.connecter(login, motDePasse);
        // if (user != null) {
        // System.out.println("Connexion reussie");

        // System.out.println("Bienvenue " + user.getLogin());
        // } else {
        // System.out.println("Login ou mot de pass incorrectes.");
        // }
        ConsoleApp console = new ConsoleApp();
        console.demarrer();
        console.login();
        // console.signup();
    }
}