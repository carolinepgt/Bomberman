

public class Bombe extends Element {

    private Personnage personnage;
    private int portee;

    public Bombe(Personnage perso, int posX, int posY){
        super("img/bomb.png",posX,posY);
        this.personnage=perso;
        portee=perso.getPortee();
        perso.setNbBombeRestantes(perso.getNbBombeRestantes()-1);
    }

    public Personnage getPersonnage() {
        return personnage;
    }


    public int getPortee() {
        return portee;
    }
}
