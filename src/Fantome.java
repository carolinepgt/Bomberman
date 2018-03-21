import java.util.ArrayList;

/**
 * Created by Yoann on 15/03/2018.
 */
public class Fantome extends Personnage_IA {
    int iTabFantom;

    private boolean goNorth;
    private boolean goSouth;
    private boolean goEast;
    private boolean goWest;
    private int cptTour;

    private ArrayList<Integer> chemin;

    public Fantome(int posX, int posY, String couleur, int iTabFantom) {
        super(posX, posY, couleur);
        this.iTabFantom=iTabFantom;
        chemin = new ArrayList<>();
        cptTour=0;
        vitesse = 2;
    }

    public int actualisePosition(Plateau plateau, boolean north, boolean east, boolean south, boolean west) {
        int modifPosition=0;

        if (north) {
            for (int i = 0; i < vitesse; i++) {
                posY-=1;
                modifPosition=3;
            }
        }
        if (east) {
            for (int i = 0; i < vitesse; i++) {
                posX+=1;
                modifPosition = 2;
            }

        }
        if (south) {
            for (int i = 0; i < vitesse; i++) {
                posY+=1;
                modifPosition=1;
            }
        }
        if (west) {
            for (int i = 0; i < vitesse; i++) {
                posX-=1;
                modifPosition = 4;
            }
        }
        return modifPosition;
    }

    protected void initDirectionFantome() {
        goNorth = false;
        goSouth = false;
        goEast = false;
        goWest = false;
    }

    public int getCptTour() {
        return cptTour;
    }

    public void setCptTour(int cptTour) {
        this.cptTour = cptTour;
    }

    public ArrayList<Integer> getChemin() {
        return chemin;
    }

    public void setChemin(ArrayList<Integer> chemin) {
        this.chemin = chemin;
    }

    public boolean isGoNorth() {
        return goNorth;
    }

    public void setGoNorth(boolean goNorth) {
        this.goNorth = goNorth;
    }

    public boolean isGoSouth() {
        return goSouth;
    }

    public void setGoSouth(boolean goSouth) {
        this.goSouth = goSouth;
    }

    public boolean isGoEast() {
        return goEast;
    }

    public void setGoEast(boolean goEast) {
        this.goEast = goEast;
    }

    public boolean isGoWest() {
        return goWest;
    }

    public void setGoWest(boolean goWest) {
        this.goWest = goWest;
    }

    public int getiTabFantom() {
        return iTabFantom;
    }

    public void setiTabFantom(int iTabFantom) {
        this.iTabFantom = iTabFantom;
    }
}
