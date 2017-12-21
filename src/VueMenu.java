import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Created by Guillaume on 14/12/2017.
 */
public class VueMenu {

    private Scene scene;
    private Group menu;
    Group bMenu;

    public VueMenu() {
        creerMenu();
    }

    private void creerMenu() {
        menu=new Group();

        bMenu=new Group();
        Button local=new Button("Jouer en local");
        local.relocate(0, 0);
        bMenu.getChildren().add(local);
        Button reseau=new Button("Jouer en réseau");
        reseau.relocate(0, 50);
        bMenu.getChildren().add(reseau);
        Button quitter=new Button("Quitter");
        quitter.relocate(0, 100);
        bMenu.getChildren().add(quitter);
        menu.getChildren().add(bMenu);
        bMenu.relocate(50,100);

        scene=new Scene(menu, 200, 250);
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
        Text text = new Text("Votre adresse ip : "+ ip);
        bMenu.getChildren().add(text);
        Button b=new Button("Démarrer la recherche de client");
        b.relocate(0,10);
        bMenu.getChildren().add(b);
    }
}
