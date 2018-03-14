import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Yoann on 07/02/2018.
 */
public class Fantopac extends Personnage_IA{
    ImageView imgFantopac = new ImageView(new Image("img2/fantopac2.png"));


    public Fantopac(int posX, int posY) {
        super(posX,posY);
    }

    public int actualisePosition(Plateau plateau, boolean north, boolean east, boolean south, boolean west) {

        Element[][] elements=plateau.getTabElement();
        int sizeElem= Element.size;
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

    public int[] getPosFantopac() {
        int[] tabPos = {posX,posY};
        return tabPos;
    }
}
