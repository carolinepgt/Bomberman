
public class Personnage {
    private int posX;
    private int posY;
    private int vitesse;
    private int nbBombeRestantes;
    private int portee;
    private int vie;

    public Personnage(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.vitesse = 5;
        this.portee=1;
        this.vie=1;
        this.nbBombeRestantes=1;

    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getVitesse() {
        return vitesse;
    }

    public int getNbBombeRestantes() {
        return nbBombeRestantes;
    }

    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }

    public void setNbBombeRestantes(int nbBombeRestantes) {
        this.nbBombeRestantes = nbBombeRestantes;
    }

    public int getPortee() {
        return portee;
    }

    public void setPortee(int portee) {
        this.portee = portee;
    }

    public int getVie() {
        return vie;
    }

    public void setVie(int vie) {
        this.vie = vie;
    }
}
