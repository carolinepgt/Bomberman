/**
 * Created by Yoann on 23/11/2017.
 */
public class Personnage_IA extends Personnage {
    private boolean bombePoser=false;
    private int direction;
    boolean passer=false;
    private int positionTabPerso;
    String blop;

    public Personnage_IA(int posX, int posY){
        super(posX,posY);
    }

    public Personnage_IA(int posX, int posY, String couleur, int nbsJ, int i){
        super(posX,posY,couleur,nbsJ);
        positionTabPerso = i;
        this.blop = "passer";
    }

    /*
    Si c'est possible pose une bombe à la position du joueur
     */
    public Bombe poseBombe(Element[][] elements, int typeBombe) {
        int sizeElem= Element.size;
        if (!estEnVie()) return null;
        int x = (getPosX()) / sizeElem;
        int y = (getPosY()) / sizeElem;
        Element element = elements[x][y];


        if (getNbBombeRestantes() > 0 && element == null) {
            Bombe bombe=null;
            if (typeBombe==0){
                bombe = new Bombe(this, x, y);
            }
            elements[x][y]=bombe;
            return bombe;
        }
        return null;
    }

    public int actualisePosition(Plateau plateau, boolean north, boolean east, boolean south, boolean west) {

        Element[][] elements=plateau.getTabElement();
        int sizeElem= Element.size;
        int modifPosition=0;

        if (north) {
            for (int i = 0; i < vitesse; i++) {
                Element element1 = elements [posX/sizeElem] [(posY/sizeElem)-1];
                if (posX%30!=0 || posY%30!=0){
                    posY-=1;
                    modifPosition=3;
                } else if((element1 == null || element1.getClass() == Effet.class)){
                    posY-=1;
                    modifPosition=3;
                }
            }
        }
        if (east) {
            for (int i = 0; i < vitesse; i++) {
                Element element1 = elements[(posX/sizeElem)+1] [posY/sizeElem];
                if (posX%30!=0 || posY%30!=0){
                    posX+=1;
                    modifPosition = 2;
                } else if ((element1 == null || element1.getClass() == Effet.class )) {
                    posX+=1;
                    modifPosition = 2;
                }
            }
        }

        if (south) {
            for (int i = 0; i < vitesse; i++) {
                Element element1 = elements [posX/sizeElem] [(posY/sizeElem)+1];
                if (posX%30!=0 || posY%30!=0){
                    posY+=1;
                    modifPosition=1;
                } else if ((element1 == null || element1.getClass() == Effet.class)) {
                    posY+=1;
                    modifPosition=1;
                }
            }
        }
        if (west) {
            for (int i = 0; i < vitesse; i++) {
                Element element1 = elements [(posX/sizeElem)-1] [posY/sizeElem];
                if (posX%30!=0 || posY%30!=0){
                    posX-=1;
                    modifPosition = 4;
                } else if ((element1 == null || element1.getClass() == Effet.class)) {
                    posX-=1;
                    modifPosition = 4;
                }
            }
        }
        return modifPosition;
    }

    public String getBlop() {
        return blop;
    }

    public void setBlop(String blop) {
        this.blop = blop;
    }

    public boolean isPasser() {
        return passer;
    }

    public void setPasser(boolean passer) {
        this.passer = passer;
    }

    public boolean isBombePoser() {
        return bombePoser;
    }

    public int getPositionTabPerso() {
        return positionTabPerso;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setBombePoser(boolean bombePoser) {
        this.bombePoser = bombePoser;
    }

    public void setNbBombeRestantes(int nbBombeRestantes) {
        if(nbBombeRestantes>=1){
            this.nbBombeRestantes = 1;
        } else {
            this.nbBombeRestantes = 0;
        }
    }

    public void setVitesse(int vitesse) {
        this.vitesse = 2;
    }
}
