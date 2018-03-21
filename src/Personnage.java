
public class Personnage {
    static int width;
    static int height;
    private int nj; //
    int posX;
    int posY;
    int vitesse;
    int nbBombeRestantes;
    private int portee;
    private int vie;
    private boolean haveMine;
    private int nbSpikeBombe;
    private String couleur;
    private int changement;

    public Personnage(int posX, int posY,String couleur) {
        this.posX = posX;
        this.posY = posY;
        width=28;
        height=28;
        vitesse = 2;
        portee=0;
        vie=1;
        nbBombeRestantes=0;
        haveMine=false;
        nbSpikeBombe=0;
        changement=1;
        this.couleur = couleur;
    }

    public Personnage(int posX, int posY, String couleur, int nj) {
        this.posX = posX;
        this.posY = posY;
        this.nj=nj; //-
        width=28;
        height=28;
        vitesse = 2;
        portee=1;
        vie=1;
        nbBombeRestantes=1;
        haveMine=false;
        nbSpikeBombe=1;
        changement=1;
        this.couleur = couleur;

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
    public Bombe poseBombe(Element[][] elements, int typeBombe) {
        int sizeElem=Element.size;
        if (!estEnVie()) return null;
        int x = (posX + width / 2) / sizeElem;
        int y = (posY + height / 2) / sizeElem;
        Element element = elements[x][y];


        if (getNbBombeRestantes() > 0 && element == null) {
            Bombe bombe=null;
            if (typeBombe==0){
                bombe = new Bombe(this, x, y);
            }
            if (typeBombe==1 && nbSpikeBombe>0){
                bombe = new SpikeBombe(this, x, y);
                nbSpikeBombe--;
            }
            elements[x][y]=bombe;
            return bombe;
        }
        return null;
    }


    public String getCouleur() {
        return couleur;
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

    public int getNbSpikeBombe() {
        return nbSpikeBombe;
    }

    public void setNbSpikeBombe(int nbSpikeBombe) {
        this.nbSpikeBombe = nbSpikeBombe;
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

    public boolean getHaveMine() {
        return haveMine;
    }

    public void setHaveMine(boolean haveMine) {
        this.haveMine = haveMine;
    }

    public boolean estEnVie() {
        if (vie<=0)return false;
        return true;
    }

    public void setPosX(String posX) { //-
        this.posX = Integer.parseInt(posX);
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(String posY) { //-
        this.posY = Integer.parseInt(posY);
    }
    public void setPosY(int  posY) {
        this.posY = posY;
    }

    public int getNj() { //-
        return nj;
    }

    public void setChangement(String changement) {
        if (!changement.equals("0")){
            this.changement = Integer.parseInt(changement);
        }
    }

    public int getChangement() {
        return changement;
    }


}
