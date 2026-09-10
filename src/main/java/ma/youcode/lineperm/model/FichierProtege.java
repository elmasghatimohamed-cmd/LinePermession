package ma.youcode.lineperm.model;

public class FichierProtege {

    private final String nom;
    private final String proprietaire;

    private boolean ownerR;
    private boolean ownerW;
    private boolean ownerD;
    private boolean otherR;
    private boolean otherW;
    private boolean otherD;

    public FichierProtege(String nom, String proprietaire,
            boolean ownerR, boolean ownerW, boolean ownerD,
            boolean otherR, boolean otherW, boolean otherD) {
        this.nom = nom;
        this.proprietaire = proprietaire;
        this.ownerR = ownerR;
        this.ownerW = ownerW;
        this.ownerD = ownerD;
        this.otherR = otherR;
        this.otherW = otherW;
        this.otherD = otherD;
    }

    public FichierProtege(String nom, String proprietaire) {
        this(nom, proprietaire, true, true, true, false, false, false);
    }

    public boolean isOwnerR() {
        return ownerR;
    }

    public boolean isOwnerW() {
        return ownerW;
    }

    public boolean isOwnerD() {
        return ownerD;
    }

    public boolean isOtherR() {
        return otherR;
    }

    public boolean isOtherW() {
        return otherW;
    }

    public boolean isOtherD() {
        return otherD;
    }

    public void setOtherR(boolean value) {
        this.otherR = value;
    }

    public void setOtherW(boolean value) {
        this.otherW = value;
    }

    public void setOtherD(boolean value) {
        this.otherD = value;
    }

    public String getBlocProprietaire() {
        return (ownerR ? "r" : "-") + (ownerW ? "w" : "-") + (ownerD ? "d" : "-");
    }

    public String getBlocAutres() {
        return (otherR ? "r" : "-") + (otherW ? "w" : "-") + (otherD ? "d" : "-");
    }

    public String versLigne() {
        return nom + ";" + proprietaire + ";" + getBlocProprietaire() + ";" + getBlocAutres();
    }
}