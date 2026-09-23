package ma.youcode.lineperm.service;

import java.util.Collection;
import ma.youcode.lineperm.access.ControleAcces;
import ma.youcode.lineperm.dao.FichierDao;
import ma.youcode.lineperm.log.LogService;
import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;

public class FileService {

    private final FichierDao fichierDao;
    private final LogService logService;

    public FileService(FichierDao fichierDao, LogService logService) {
        this.fichierDao = fichierDao;
        this.logService = logService;
    }

    public boolean creerFichier(User user, String nom) {
        if (user == null || nom == null || nom.isBlank()|| nom.contains("/") || nom.contains("\\") 
            || fichierDao.findByNom(nom) != null) {
            return false;
        }
        FichierProtege nouveau = new FichierProtege(nom, user.getLogin());
        fichierDao.save(nouveau);
        logService.enregistrer(user.getLogin(), "CREATION", nom, "OK");
        return true;
    }

    public Collection<FichierProtege> listerTous() {
        return fichierDao.findAll();
    }

    public FichierProtege getFichier(String nom) {
        return fichierDao.findByNom(nom);
    }

    public String lireContenu(User user, String nom) {
        FichierProtege fichier = fichierDao.findByNom(nom);

        if (user == null || fichier == null
                || !ControleAcces.estAutorise(user, fichier, 'r')) {

            if (user != null) {
                logService.enregistrer(
                        user.getLogin(), "LECTURE", nom, "REFUSE");
            }
            return null;
        }

        logService.enregistrer(
                user.getLogin(), "LECTURE", nom, "OK");

        return fichier.getContenu();
    }

    public boolean ecrireContenu(User user, String nom, String contenu) {
        FichierProtege fichier = fichierDao.findByNom(nom);

        if (user == null || fichier == null
                || !ControleAcces.estAutorise(user, fichier, 'w')) {

            if (user != null) {
                logService.enregistrer(
                        user.getLogin(), "ECRITURE", nom, "REFUSE");
            }
            return false;
        }

        fichierDao.updateContenu(fichier.getId(), contenu);
        fichier.setContenu(contenu);

        logService.enregistrer(
                user.getLogin(), "ECRITURE", nom, "OK");

        return true;
    }

    public boolean modifierDroits(User user, String nom, String argDroit) {
        FichierProtege fichier = fichierDao.findByNom(nom);

        if (user == null || fichier == null
                || !user.getLogin().equals(fichier.getProprietaire())) {

            if (user != null) {
                logService.enregistrer(
                        user.getLogin(), "CHMOD", nom, "REFUSE");
            }
            return false;
        }

        boolean ajouter = !argDroit.startsWith("-");
        String droits = argDroit.replace("-", "");

        for (char c : droits.toCharArray()) {
            if (c == 'r') {
                fichier.setAutR(ajouter);
            }
            if (c == 'w') {
                fichier.setAutW(ajouter);
            }
            if (c == 'd') {
                fichier.setAutD(ajouter);
            }
        }

        fichierDao.updateDroits(
                fichier.getId(),
                fichier.getDroitsFormates());

        logService.enregistrer(
                user.getLogin(), "CHMOD", nom, "OK");

        return true;
    }

    public boolean supprimerFichier(User user, String nom) {
        FichierProtege fichier = fichierDao.findByNom(nom);

        if (user == null || fichier == null
                || !ControleAcces.estAutorise(user, fichier, 'd')) {

            if (user != null) {
                logService.enregistrer(
                        user.getLogin(), "SUPPRESSION", nom, "REFUSE");
            }
            return false;
        }

        fichierDao.delete(fichier.getId());

        logService.enregistrer(
                user.getLogin(), "SUPPRESSION", nom, "OK");

        return true;
    }
}