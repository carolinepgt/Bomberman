import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;


public class View {

    private Scene scene;
    private Image imagePerso;
    private ImageView[] nodePerso;
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

        nodePerso = new ImageView[2];
        for (int i=0; i<nodePerso.length; i++){

            nodePerso[i] = new ImageView(new Image("img2/SKF_"+model.getTabPerso()[i].getCouleur()+"1.PNG"));
            terrain.getChildren().add(nodePerso[i]);
            actualisePositionImage(1, i);
        }
        scene = new Scene(terrain, 630, 630);

    }

    public Scene getScene() {
        return scene;
    }

    /*South=1 East=2 North=3 West=4*/
   public void actualisePositionImage(int direction, int indexPerso){
       Personnage perso=model.getTabPerso()[indexPerso];
       imagePerso=new Image("img2/SKF_"+perso.getCouleur()+direction+".PNG");
       nodePerso[indexPerso].setImage(imagePerso);

       nodePerso[indexPerso].relocate(perso.getPosX(),perso.getPosY());
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

    /*
    * explosion[4][rayonExplo]
    *  0 : North
    *  1 : East
    *  2 : South
    *  3 : West
    * */
    public void afficheRange(int[] explosion, Bombe bombe, String couleurPerso) {
        ParallelTransition animeRayon = new ParallelTransition();
        ArrayList listEventImg = new ArrayList();
        ArrayList listImg = new ArrayList<ImageView>();
        ImageView image = null;
        int rayon;

        rayon = explosion[0];
        for (int i = 0; i<rayon;i++) {
            if (i == 0) {
                image = new ImageView(new Image("img2/Explo"+couleurPerso+"0.jpg"));
            }else if(i<rayon-1){
                image = new ImageView(new Image("img2/Explosion"+couleurPerso+"1V.png"));
            }else {
                image = new ImageView(new Image("img2/Explosion"+couleurPerso+"2Vh.png"));
            }

            terrain.getChildren().add(image);
            System.out.println("x : "+ bombe.getPosX() * sizeElem+ " y : "+(bombe.getPosY()-i) * sizeElem);

            image.relocate(bombe.getPosX() * sizeElem, (bombe.getPosY()-i) * sizeElem);


            FadeTransition apparition = new FadeTransition(Duration.seconds(0.5), image);
            apparition.setFromValue(1.0);
            apparition.setToValue(0.1);

            listEventImg.add(apparition);
            listImg.add(image);

        }

        rayon = explosion[1];
        for (int i = 1; i<rayon;i++) {
            if(i<rayon-1){
                image = new ImageView(new Image("img2/Explosion"+couleurPerso+"1H.png"));
            } else {
                image = new ImageView(new Image("img2/Explosion"+couleurPerso+"2Hd.png"));
            }

            terrain.getChildren().add(image);
            image.relocate((bombe.getPosX()+i) * sizeElem, bombe.getPosY() * sizeElem);

            FadeTransition apparition = new FadeTransition(Duration.seconds(0.5), image);
            apparition.setFromValue(1.0);
            apparition.setToValue(0.1);
            listEventImg.add(apparition);

            listImg.add(image);
        }

        rayon = explosion[2];
        for (int i = 1; i<rayon;i++) {
            if(i<rayon-1){
                image = new ImageView(new Image("img2/Explosion"+couleurPerso+"1V.png"));
            } else {
                image = new ImageView(new Image("img2/Explosion"+couleurPerso+"2Vb.png"));
            }

            terrain.getChildren().add(image);
            image.relocate(bombe.getPosX() * sizeElem, (bombe.getPosY()+i) * sizeElem);

            FadeTransition apparition = new FadeTransition(Duration.seconds(0.5), image);
            apparition.setFromValue(1.0);
            apparition.setToValue(0.1);
            listEventImg.add(apparition);

            listImg.add(image);
        }

        rayon = explosion[3];
        for (int i = 1; i<rayon;i++) {
            if(i<rayon-1){
                image = new ImageView(new Image("img2/Explosion"+couleurPerso+"1H.png"));
            } else {
                image = new ImageView(new Image("img2/Explosion"+couleurPerso+"2Hg.png"));
            }
            terrain.getChildren().add(image);
            image.relocate((bombe.getPosX()-i) * sizeElem, bombe.getPosY() * sizeElem);

            FadeTransition apparition = new FadeTransition(Duration.seconds(0.5), image);
            apparition.setFromValue(1.0);
            apparition.setToValue(0.1);
            listEventImg.add(apparition);

            listImg.add(image);
        }


        animeRayon.getChildren().addAll(listEventImg);
        animeRayon.play();

        animeRayon.getChildren().removeAll();

        animeRayon.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                terrain.getChildren().removeAll(listImg);
            }
        });


        System.out.println("----------------------------");
    }

    public void supprimeImagePersonnage(int indexPerso) {
        terrain.getChildren().remove(nodePerso[indexPerso]);
    }

    public void afficheFenetreFin(){

        Stage stage = new Stage();
        Label modalityLabel = new Label("Partie terminé!");
        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(e -> stage.close());
        VBox root = new VBox();
        root.getChildren().addAll(modalityLabel, closeButton);
        Scene scene = new Scene(root, 200, 100);
        stage.setScene(scene);
        stage.show();
    }
}
