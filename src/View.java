import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class View {


    private Scene scene;
    private Image imagePerso;
    private ImageView nodePerso;
    private Group terrain;
    private Model model;
    private ImageView[][] tabImageView;

    public View(Model model) {
        this.model = model;
        creeScene();
    }

    public void creeScene(){
        terrain = new Group();

        tabImageView=new ImageView[10][10];
        for (int x=0; x<10; x++){
            for (int y=0; y<10; y++){
                Element element=model.getPlateau().getTabElement()[x][y];
                if (element!=null){
                    Image imageElement=new Image(element.getImageURL());
                    tabImageView[x][y]= new ImageView(imageElement);
                    tabImageView[x][y].relocate(x*50,y*50);
                    terrain.getChildren().add(tabImageView[x][y]);
                }
            }
        }

        imagePerso = new Image("img/perso.jpg");
        nodePerso = new ImageView(imagePerso);
        terrain.getChildren().add(nodePerso);
        actualisePositionPerso();
        scene = new Scene(terrain, 500, 500);

    }

    public Scene getScene() {
        return scene;
    }

   public void actualisePositionPerso(){
       Personnage perso=model.getTabPerso()[0];
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

    public void supprimeElementImageView(int posX, int posY) {
       terrain.getChildren().remove(tabImageView[posX][posY]);
       tabImageView[posX][posY]=null;
    }
}
