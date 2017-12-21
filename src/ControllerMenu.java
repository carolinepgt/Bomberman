import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static javafx.scene.media.MediaPlayer.INDEFINITE;


public class ControllerMenu {

    private VueMenu vm;

    public ControllerMenu(VueMenu vm) {
        this.vm = vm;
        controlLocalReseauButton();
    }

    private void controlLocalReseauButton() {
        List<Node> l=vm.bMenu.getChildren();
        ((Button)l.get(0)).setOnAction(actionEvent -> {
            vm.creerChoixNbJoueursLocal();
            controlNbJoueursButtonLocal();
        });
        ((Button)l.get(1)).setOnAction(actionEvent -> {
            /*vm.creerChoixNbJoueursReseau();
            controlNbJoueursButtonReseau();*/

            vm.creerChoixClientServeur();
            controlClientServeurButton(2);
        });
    }

    private void controlNbJoueursButtonLocal() {
        List<Node> l=vm.bMenu.getChildren();
        for (int i=0; i<2; i++){
            final int nj=i;
            ((Button)l.get(i)).setOnAction(actionEvent -> lancerPartieLocal(nj+1));
        }
    }

    private void lancerPartieLocal(int nj) {
        Stage stage=((Stage)vm.bMenu.getScene().getWindow());
        stage.close();
        Model model = new Model(nj);
        View view = new View(model);
        Controller controller=new Controller(view,model);

        stage.setScene(view.getScene());
        stage.show();

/*
        File file = new File(System.getProperty("user.dir")+"/src/son/megalovania.mp3");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(INDEFINITE);
        mediaPlayer.setVolume(0.02);
        mediaPlayer.play();
*/




        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1_000_000_000) {
                    lastUpdate = now ;
                }
                if (model.partieFini()) {
                    //mediaPlayer.stop();
                    this.stop();
                }
                controller.actualisePostion();
                // if (mediaPlayer.getStatus()== MediaPlayer.Status.PAUSED) mediaPlayer.play();

            }
        };
        timer.start();
    }

/*
    private void controlNbJoueursButtonReseau() {
        List<Node> l=vm.bMenu.getChildren();
        for (int i=0; i<3; i++){
             final int nj=i;
            ((Button)l.get(i)).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    vm.creerChoixClientServeur();
                    controlClientServeurButton(nj+2);
                }
            });
        }
    }
*/
    private void controlClientServeurButton(int nj) {
        List<Node> l=vm.bMenu.getChildren();
        ((Button)l.get(0)).setOnAction(actionEvent -> {
            vm.creerSelectionIP();
            controlSelectionIP(nj);
    });
        ((Button)l.get(1)).setOnAction(actionEvent -> {
            String ip="";
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            vm.creerAttenteServeur(ip);
            final String host=ip;
            ((Button)vm.bMenu.getChildren().get(1)).setOnAction(actionEvent1 -> lancerPartieReseau(true, host,  nj));
        });
    }

    private void controlSelectionIP(int nj) {
        List<Node> l=vm.bMenu.getChildren();
        ((Button)l.get(2)).setOnAction(actionEvent -> {
            String ip=((TextField)l.get(1)).getText();


            lancerPartieReseau(false, ip, nj);
        });
    }

    private void lancerPartieReseau(boolean estServeur, String host, int nj) {
        Stage stage=((Stage)vm.bMenu.getScene().getWindow());
        Model model = new Model(nj);
        View view = new View(model);
        int port = 3456;
        ControllerReseau controller;

        if (estServeur){
            System.out.println(nj);
            controller=new ControllerReseau(view,model,0);
            BombermanServer bs = new BombermanServer(host, port, controller);
            bs.open();
            System.out.println("Serveur initialisé.");
            bs.attendClientConnecte();
        }


        else {
            controller = new ControllerReseau(view, model, 1);
            Thread t = new Thread(new ClientConnexion(host, port, controller));
            t.start();

        }

        stage.close();
        stage.setScene(view.getScene());
        stage.show();

        exitApplication(stage);

/*
        File file = new File(System.getProperty("user.dir")+"/src/son/megalovania.mp3");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(INDEFINITE);
        mediaPlayer.setVolume(0.02);
        mediaPlayer.play();

*/


        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1_000_000_000) {
                    lastUpdate = now ;
                }
                if (model.partieFini()) {
                    //mediaPlayer.stop();
                    this.stop();
                }
                controller.actualisePostion();
                // if (mediaPlayer.getStatus()== MediaPlayer.Status.PAUSED) mediaPlayer.play();

            }
        };
        timer.start();
    }


    private void exitApplication(Stage primaryStage) {

        primaryStage.setOnCloseRequest(we -> System.exit(0));
    }


}
