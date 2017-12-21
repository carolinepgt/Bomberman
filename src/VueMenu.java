import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class VueMenu {

    private Scene scene;
    private Group menu;
    Group bMenu;
    private ImageView fond;


    public VueMenu() {
        creerMenu();
    }

    private void creerMenu() {
        menu=new Group();
        fond=new ImageView("img2/fond_menu.jpg");
        fond.setFitHeight(200);
        fond.setFitWidth(300);
        menu.getChildren().add(fond);
        bMenu=new Group();
        Button b=new Button("Jeu en local");
        b.relocate(0,0);
        bMenu.getChildren().add(b);
        b=new Button("Jeu en réseau");
        b.relocate(0,50);
        bMenu.getChildren().add(b);
        b=new Button("Quitter");
        b.relocate(0, 100);
        bMenu.getChildren().add(b);
        menu.getChildren().add(bMenu);
        bMenu.relocate(50,50);

        scene=new Scene(menu, 300, 200);
    }
    public Scene getScene() {
        return scene;
    }

    public void creerChoixNbJoueursReseau() {
        bMenu.getChildren().clear();

        for (int i=2; i<=4; i++){
            Button b= new Button("Partie "+i+" joueurs");
            b.relocate(0, (i-2)*50);
            bMenu.getChildren().add(b);
        }
    }

    public void creerChoixNbJoueursLocal() {
        bMenu.getChildren().clear();

        Button b= new Button("Partie "+1+" joueur");
        bMenu.getChildren().add(b);

        b= new Button("Partie "+2+" joueurs");
        b.relocate(0, 50);
        bMenu.getChildren().add(b);
    }

    public void creerChoixClientServeur() {

        bMenu.getChildren().clear();

        Button b= new Button("Client");
        bMenu.getChildren().add(b);

        b= new Button("Serveur");
        b.relocate(0, 50);
        bMenu.getChildren().add(b);
    }

    public void creerSelectionIP() {
        bMenu.getChildren().clear();
        menu.getChildren().remove(fond);
        /*ImageView bomb=new ImageView("img2/bomb.gif");
        bomb.setPreserveRatio(true);
        bomb.setFitHeight(100);
        bomb.relocate(200,100);
        menu.getChildren().add(bomb);*/
        Text text = new Text("Adresse IP du serveur:");
        bMenu.getChildren().add(text);
        TextField tf=new TextField();
        bMenu.getChildren().add(tf);
        tf.relocate(0,5);
        Button b=new Button("Valider");
        bMenu.getChildren().add(b);
        b.relocate(0,35);

    }

    public void creerAttenteServeur(String ip) {
        bMenu.getChildren().clear();
        menu.getChildren().remove(fond);
        /*ImageView bomb=new ImageView("img2/bomb.gif");
        bomb.setPreserveRatio(true);
        bomb.setFitHeight(100);
        bomb.relocate(200,100);
        menu.getChildren().add(bomb);*/
        Text text = new Text("Votre adresse ip : "+ ip);
        bMenu.getChildren().add(text);
        Button b=new Button("Démarrer la recherche de client");
        b.relocate(0,10);
        bMenu.getChildren().add(b);
    }
}