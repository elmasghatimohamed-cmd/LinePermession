package ma.youcode.lineperm;

import java.sql.SQLException;
import ma.youcode.lineperm.dao.FichierDao;
import ma.youcode.lineperm.model.FichierProtege;

public class Main {

    public static void main(String[] args) throws SQLException {
        // ConsoleApp app = new ConsoleApp();
        // app.demarrer();

        FichierDao fichierDao = new FichierDao();

        // Creation
        FichierProtege fichier = new FichierProtege("test.txt", "Ali");
        fichierDao.save(fichier);

        System.out.println("ID genere : " + fichier.getId());

        // Recherche par ID
        FichierProtege trouve = fichierDao.findById(fichier.getId());

        if (trouve != null) {
            System.out.println("Nom : " + trouve.getNom());
            System.out.println("Proprietaire : " + trouve.getProprietaire());
            System.out.println("Droits : " + trouve.getDroitsFormates());
        }

        // Recherche par nom
        System.out.println(fichierDao.findByNom("test.txt"));

        // Recherche par proprietaire
        System.out.println("Fichiers de Ali : "
                + fichierDao.findByProprietaire("Ali").size());

        // Mise a jour des droits
        fichierDao.updateDroits(fichier.getId(), "rwd|---");

        FichierProtege modifie = fichierDao.findById(fichier.getId());

        if (modifie != null) {
            System.out.println("Nouveaux droits : "
                    + modifie.getDroitsFormates());
        }

        // Suppression
        // fichierDao.delete(fichier.getId());

        // System.out.println("Apres suppression : "
        // + fichierDao.findById(fichier.getId()));
    }
}