import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;


public class View {

    private Scene scene;
    private ImageView[] nodePerso;
    private ImageView[][] tabImageView;
    private ImageView imgFantopac;
    private ImageView[] imagesFantome;
    private ImageView[][] imagesSol;

    private Group terrain;
    private Model model;
    private int sizeElem = 30;
    private String skin = "SKF_";


    public View(Model model) {
        this.model = model;
        creeScene();
    }

    public void creeScene(){
        terrain = new Group();

        tabImageView=new ImageView[21][21];
        imagesSol = new ImageView[21][21];
        for (int x=0; x<21; x++){
            for (int y=0; y<21; y++){
                Element element=model.getPlateau().getTabElement()[x][y];
                if (element!=null){
                    Image imageElement=new Image(element.getImageURL());
                    tabImageView[x][y]= new ImageView(imageElement);
                    tabImageView[x][y].relocate(x*sizeElem,y*sizeElem);
                    terrain.getChildren().add(tabImageView[x][y]);
                }
                inserSol(x,y);
            }
        }

        nodePerso = new ImageView[model.getTabPerso().length];
        for (int i=0; i<nodePerso.length; i++){

            nodePerso[i] = new ImageView(new Image("img2/SKF_"+model.getTabPerso()[i].getCouleur()+"1.PNG"));
            terrain.getChildren().add(nodePerso[i]);
            actualisePositionImage(1, i);
        }
        scene = new Scene(terrain, 630, 630);


        initFantopac();
    }


    public Scene getScene() {
        return scene;
    }

    /*South=1 East=2 North=3 West=4*/
    public void actualisePositionImage(int direction, int indexPerso){
        Personnage perso=model.getTabPerso()[indexPerso];

        String numAnime;
        if(!model.isPartiePacman()){
            skin = "SKF_";
            numAnime = "";
        } else {
            terrain.getChildren().remove(imgFantopac);
            skin = "pacman_";
            numAnime = "_"+perso.getNumAnime();

            nodePerso[indexPerso].setFitWidth(sizeElem);
            nodePerso[indexPerso].setFitHeight(sizeElem);
        }

        int persoX = perso.getPosX();
        int persoY = perso.getPosY();

        if(persoX<1){
            persoX =599;
        } else if(persoX>599){
            persoX = 1;
        }
        perso.setPosX(persoX);
        Image imagePerso = new Image("img2/"+skin + perso.getCouleur() + direction + numAnime+".png");


        nodePerso[indexPerso].setImage(imagePerso);
        nodePerso[indexPerso].relocate(persoX,persoY);
    }

    public void insereElementToBack(ImageView image, int x, int y) {
        tabImageView[x][y]=image;
        terrain.getChildren().add(image);
        image.toBack();
    }

    public void insereElementToFront(ImageView image, int x, int y) {
        tabImageView[x][y]=image;
        terrain.getChildren().add(image);
        image.toFront();
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

        animeRayon.setOnFinished(actionEvent -> terrain.getChildren().removeAll(listImg));


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


    public void initialisePlateauReseau() {
        Platform.runLater(() -> {
            for (int i=0; i<model.getTabPerso().length; i++){
                actualisePositionImage(model.getTabPerso()[i].getChangement(),i);
            }
            for (int i=0; i<tabImageView.length; i++){
                for (int j=0; j<tabImageView[i].length; j++){
                    terrain.getChildren().remove(tabImageView[i][j]);
                    Element e=model.getPlateau().getTabElement()[i][j];
                    if (e==null)tabImageView[i][j]=null;
                    else {
                        terrain.getChildren().add(tabImageView[i][j]);
                        tabImageView[i][j].relocate(i*sizeElem,j*sizeElem);
                    }

                }
            }
        });
    }

    /**Méthode pour la gestion de l'affichage du Fantopac**/
    private void initFantopac() {
        imgFantopac = new ImageView(new Image("img2/fantopac"+1+".png"));
        imgFantopac.setFitHeight(sizeElem);
        imgFantopac.setFitWidth(sizeElem);
        terrain.getChildren().add(imgFantopac);
    }

    /*South=1 East=2 North=3 West=4*/
    public void actualisePositionImageFantopac(int direction){
        Fantopac fantopac = model.getFantopac();

        /*Autre skin : trump  gostBlack  ghost_   pacfant_Vert_  pacfant_Rouge_  fantopac*/
        imgFantopac.setImage( new Image("img2/"+fantopac.getCouleur()+direction+".png"));

        imgFantopac.relocate(fantopac.getPosX(),fantopac.getPosY());
    }


    /** Méthode pour la gestion de l'affichage des fantomes lors d'une partie "2" --> pacman**/

    public void initFantome(Fantome[] tabFamtome){
        int nbsFantome = tabFamtome.length;
        imagesFantome = new ImageView[nbsFantome];

        for (int i = 0; i < nbsFantome; i++) {
            imagesFantome[i] =  new ImageView(new Image("img2/pacfant_"+tabFamtome[i].getCouleur()+"_1.png"));
            imagesFantome[i].setFitHeight(sizeElem);
            imagesFantome[i].setFitWidth(sizeElem);
            terrain.getChildren().add(imagesFantome[i]);
        }
    }

    /*South=1 East=2 North=3 West=4*/
    public void actualisePositionImageFantome(int idFantom, int direction){
        Fantome fantome = model.getTabFantome()[idFantom];


        if(fantome.posX<1){
            fantome.posX=570;
        } else if(fantome.posX>599){
            fantome.posX=30;
        }

        /*Autre chaine : pacfant_Couleur_direction_.png   couleurs dispo : Rouge Vert*/
        imagesFantome[idFantom].setImage( new Image("img2/pacfant_"+fantome.getCouleur()+"_"+direction+".png"));
        imagesFantome[idFantom].relocate(fantome.getPosX(),fantome.getPosY());
    }

    public void actualiseAllImage(){
        for (int x=0; x<21; x++){
            for (int y=0; y<21; y++){
                Element element = model.getPlateau().getTabElement()[x][y];
                if (element!=null){
                    Image img = new Image(element.getImageURL());
                    tabImageView[x][y].setImage(img);
                    tabImageView[x][y].setFitHeight(sizeElem);
                    tabImageView[x][y].setFitWidth(sizeElem);
                    tabImageView[x][y].relocate(x*sizeElem,y*sizeElem);
                }
            }
        }
    }

    public void inserSol(int x, int y) {
        Image img = new Image("img2/solPacman.png");
        ImageView imageView = new ImageView(img);

        imageView.setFitHeight(sizeElem);
        imageView.setFitWidth(sizeElem);

        imageView.relocate(x*sizeElem,y*sizeElem);
        terrain.getChildren().add(imageView);
        imageView.setVisible(false);

        imagesSol[x][y] = imageView;
        imagesSol[x][y].toBack();
    }

    public void reveleSol(int x, int y){
        imagesSol[x][y].setVisible(true);
    }
}
