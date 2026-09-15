package ma.youcode.lineperm.model;

public class AccessLog {
    private final String date;
    private final String heure;
    private final String utilisateur;
    private final String action;
    private final String fichier;
    private final String resultat;

    public AccessLog(String date, String heure, String utilisateur, String action, String fichier, String resultat) {
        this.date = date;
        this.heure = heure;
        this.utilisateur = utilisateur;
        this.action = action;
        this.fichier = fichier;
        this.resultat = resultat;
    }

    public static AccessLog depuisLog(String ligne) {
        String[] parts = ligne.split(";");
        if (parts.length >= 6) {
            return new AccessLog(parts[0].trim(), 
            parts[1].trim(), 
            parts[2].trim(), 
            parts[3].trim(), 
            parts[4].trim(), 
            parts[5].trim());
        }
        return null;
    }

    public String getDate() { 
        return date; 
    }
    public String getHeure() { 
        return heure; 
    }
    public String getUtilisateur() { 
        return utilisateur; 
    }
    public String getAction() { 
        return action; 
    }
    public String getFichier() { 
        return fichier; 
    }
    public String getResultat() { 
        return resultat; 
    }

    @Override
    public String toString() {
        return date + " | " + heure + " | " + utilisateur + " | " + action + " | " + fichier + " | " + resultat;
}
}