package ma.youcode.lineperm.model;

public class FichierProtege {
    private final String nom;
    private final String proprietaire;

    private boolean propR;
    private boolean propW;
    private boolean propD;
    private boolean autR;
    private boolean autW;
    private boolean autD;

    public FichierProtege(String nom, String proprietaire, boolean propR, boolean propW, boolean propD, boolean autR,
            boolean autW, boolean autD) {
        this.nom = nom;
        this.proprietaire = proprietaire;
        this.propR = propR;
        this.propW = propW;
        this.propD = propD;
        this.autR = autR;
        this.autW = autW;
        this.autD = autD;
    }

    public FichierProtege(String nom, String proprietaire) {
        this(nom, proprietaire, true, true, true, false, false, false);
    }

    public String getNom() {
        return nom;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public boolean isPropR() {
        return propR;
    }

    public void setPropR(boolean propR) {
        this.propR = propR;
    }

    public boolean isPropW() {
        return propW;
    }

    public void setPropW(boolean propW) {
        this.propW = propW;
    }

    public boolean isPropD() {
        return propD;
    }

    public void setPropD(boolean propD) {
        this.propD = propD;
    }

    public boolean isAutR() {
        return autR;
    }

    public void setAutR(boolean autR) {
        this.autR = autR;
    }

    public boolean isAutW() {
        return autW;
    }

    public void setAutW(boolean autW) {
        this.autW = autW;
    }

    public boolean isAutD() {
        return autD;
    }

    public void setAutD(boolean autD) {
        this.autD = autD;
    }

    public String getBlocProprietaire() {
        return (propR ? "r" : "-") + (propW ? "w" : "-") + (propD ? "d" : "-");
    }

    public String getBlocAutres() {
        return (autR ? "r" : "-") + (autW ? "w" : "-") + (autD ? "d" : "-");
    }

    public String getDroitsFormates() {
        return getBlocProprietaire() + "|" + getBlocAutres();
    }
}