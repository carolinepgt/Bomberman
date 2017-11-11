import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;

import static javafx.scene.media.AudioClip.INDEFINITE;


public class RunPartie extends Application{

    public static void main(String [ ] args){
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        Model model = new Model();
        View view = new View(model);
        Controller controller=new Controller(view,model);

        primaryStage.setScene(view.getScene());
        primaryStage.show();


        File file = new File(System.getProperty("user.dir")+"/src/son/megalovania.mp3");
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(INDEFINITE);
        mediaPlayer.setVolume(0.02);
        mediaPlayer.play();



        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (model.partieFini()) {
                    mediaPlayer.stop();
                    this.stop();
                }
                controller.actualisePostion();
                if (mediaPlayer.getStatus()== MediaPlayer.Status.PAUSED) mediaPlayer.play();

            }
        };
        timer.start();


    }

    private void exitApplication(Stage primaryStage) {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });
    }

}