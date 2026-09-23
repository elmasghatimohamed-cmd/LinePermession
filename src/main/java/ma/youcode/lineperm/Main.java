package ma.youcode.lineperm;

import java.sql.SQLException;
import ma.youcode.lineperm.dao.FichierDao;
import ma.youcode.lineperm.dao.UserDao;
import ma.youcode.lineperm.log.LogService;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.FileService;

public class Main {

    public static void main(String[] args) throws SQLException {
        // ConsoleApp app = new ConsoleApp();
        // app.demarrer();

        UserDao userDao = new UserDao();
        FichierDao fichierDao = new FichierDao();
        LogService logService = new LogService();

        FileService fileService = new FileService(fichierDao, logService);

        // Creer un utilisateur de test
        User user = new User("Ali", "12356789");
        userDao.save(user);

        String nom = "test.txt";

        // 1. Creation
        System.out.println("--- TEST CREATION ---");

        boolean cree = fileService.creerFichier(user, nom);
        System.out.println("Creation : " + cree);

        // 2. Recuperation
        System.out.println("--- TEST GET FICHIER ---");

        var fichier = fileService.getFichier(nom);

        System.out.println(
                fichier != null ? fichier.getNom() : "Fichier introuvable");

        // 3. Ecriture
        System.out.println("--- TEST ECRITURE ---");

        boolean ecrit = fileService.ecrireContenu(
                user, nom, "Hello SQLite avec FileService !");

        System.out.println("Ecriture : " + ecrit);

        // 4. Lecture
        System.out.println("--- TEST LECTURE ---");

        String contenu = fileService.lireContenu(user, nom);
        System.out.println("Contenu : " + contenu);

        // 5. Modification des droits
        System.out.println("--- TEST CHMOD ---");

        boolean droits = fileService.modifierDroits(
                user, nom, "+rw");

        System.out.println("Modification droits : " + droits);

        // 6. Lister tous les fichiers
        System.out.println("--- TEST LISTER ---");

        fileService.listerTous().forEach(f -> System.out.println(f.getNom() + " | " + f.getProprietaire()));

    }
}