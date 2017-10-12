import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;


public class View {

    private Scene scene;
    private Image imagePerso;
    private ImageView nodePerso;
    private Group terrain;
    private Model model;
    private ImageView[][] tabImageView;
    public int sizeElem = 30;

    public View(Model model) {
        this.model = model;
        creeScene();
    }

    public void creeScene(){
        terrain = new Group();

        tabImageView=new ImageView[21][21];
        for (int x=0; x<21; x++){
            for (int y=0; y<21; y++){
                Element element=model.getPlateau().getTabElement()[x][y];
                if (element!=null){
                    Image imageElement=new Image(element.getImageURL());
                    tabImageView[x][y]= new ImageView(imageElement);
                    tabImageView[x][y].relocate(x*sizeElem,y*sizeElem);
                    terrain.getChildren().add(tabImageView[x][y]);
                }
            }
        }

        URL imageURL = getClass().getResource("img2/SKF_B1.PNG");
        imagePerso = new Image(imageURL.toExternalForm());
        nodePerso = new ImageView(imagePerso);
        terrain.getChildren().add(nodePerso);
        actualisePositionImage(1);
        scene = new Scene(terrain, 630, 630);

    }

    public Scene getScene() {
        return scene;
    }

    /*South=1 East=2 North=3 West=4*/
   public void actualisePositionImage(int direction){
       Personnage perso=model.getTabPerso()[0];

       imagePerso=new Image("img2/SKF_B"+direction+".PNG");
       nodePerso.setImage(imagePerso);

       nodePerso.relocate(perso.getPosX(),perso.getPosY());
   }


    public Image getImagePerso() {
        return imagePerso;
    }


    public void insereElement(ImageView image, int x, int y) {
       tabImageView[x][y]=image;
       terrain.getChildren().add(image);
       image.toBack();
    }

    public void supprimeImageView(int posX, int posY) {
       terrain.getChildren().remove(tabImageView[posX][posY]);
       tabImageView[posX][posY]=null;
    }
}
