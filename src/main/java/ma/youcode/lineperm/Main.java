package ma.youcode.lineperm;

// import ma.youcode.lineperm.ui.ConsoleApp;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.model.FichierProtege;

public class Main {
    public static void main(String[] args) {
        // ConsoleApp app = new ConsoleApp();
        // app.demarrer();

        FileService fileService = new FileService();
        User user = new User("Badr", "123456");
        boolean resultat = fileService.creerFichier(user, "test.txt");
        if (resultat) {
            System.out.println("Fichier cree avec succes.");
        } else {
            System.out.println("Echec de creation du fichier.");
        }

        for (FichierProtege fichier : fileService.listerTous()) {
            System.out.println(fichier.getNom() + " | Proprietaire: " + fichier.getProprietaire() + " | Droits: "
                    + fichier.getBlocProprietaire() + "|" + fichier.getBlocAutres());
        }
    }

}