
public class Personnage {
    static int width;
    static int height;
    private int posX;
    private int posY;
    private int vitesse;
    private int nbBombeRestantes;
    private int portee;
    private int vie;

    public Personnage(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.width=28;
        this.height=28;
        this.vitesse = 2;
        this.portee=1;
        this.vie=1;
        this.nbBombeRestantes=1;

    }


    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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

    public boolean estEnVie() {
        if (vie<=0)return false;
        return true;
    }


    public int actualisePosition(Plateau plateau, boolean north, boolean east, boolean south, boolean west) {

        Element[][] elements=plateau.getTabElement();
        int sizeElem=Element.size;
        int modifPosition=0;
        Element bombeProbableHG = elements[posX / sizeElem][posY / sizeElem];
        Element bombeProbableBG = elements[posX / sizeElem][(posY + height) / sizeElem];
        Element bombeProbableHD = elements[(posX + width) / sizeElem][posY / sizeElem];
        Element bombeProbableBD = elements[(posX + width) / sizeElem][(posY + height) / sizeElem];


        if (north) {
            for (int i = 0; i < vitesse; i++) {
                Element element1 = elements[posX / sizeElem][(posY - 1) / sizeElem];
                Element element2 = elements[(posX + width) / sizeElem][(posY - 1) / sizeElem];
                if ((element1 == null || element1 instanceof Effet || element1 == bombeProbableHG) && (element2 == null || element2 instanceof Effet || element2 == bombeProbableHD)) {
                    posY-=1;
                    modifPosition=3;
                }
            }
        }
        if (east) {
            for (int i = 0; i < vitesse; i++) {
                Element element1 = elements[(posX + width + 1) / sizeElem][posY / sizeElem];
                Element element2 = elements[(posX + width + 1) / sizeElem][(posY + height) / sizeElem];
                if ((element1 == null || element1.getClass() == Effet.class || element1 == bombeProbableHD) && (element2 == null || element2.getClass() == Effet.class || element2 == bombeProbableBD)) {
                    posX += 1;
                    modifPosition = 2;
                }
            }
        }

        if (south) {
            for (int i = 0; i < vitesse; i++) {
                Element element1 = elements[posX / sizeElem][(posY + height + 1) / sizeElem];
                Element element2 = elements[(posX + width) / sizeElem][(posY + height + 1) / sizeElem];
                if ((element1 == null || element1.getClass() == Effet.class || element1 == bombeProbableBG) && (element2 == null || element2.getClass() == Effet.class || element2 == bombeProbableBD)) {
                    posY+= 1;
                    modifPosition=1;
                }
            }
        }
        if (west) {
            for (int i = 0; i < vitesse; i++) {
                Element element1 = elements[(posX - 1) / sizeElem][posY / sizeElem];
                Element element2 = elements[(posX - 1) / sizeElem][(posY + height) / sizeElem];
                if ((element1 == null || element1.getClass() == Effet.class || element1 == bombeProbableHG) && (element2 == null || element2.getClass() == Effet.class || element2 == bombeProbableBG)) {
                    posX -= 1;
                    modifPosition = 4;
                }
            }
        }
        return modifPosition;
    }

    /*
    Si c'est possible pose une bombe à la position du joueur
     */
    public Bombe poseBombe(Element[][] elements) {
        int sizeElem=Element.size;
        if (!estEnVie()) return null;
        int x = (posX + width / 2) / sizeElem;
        int y = (posY + height / 2) / sizeElem;
        Element element = elements[x][y];

        if (getNbBombeRestantes() > 0 && element == null) {
            Bombe bombe = new Bombe(this, x, y);
            elements[x][y]=bombe;
            return bombe;
        }
        return null;
    }

}
