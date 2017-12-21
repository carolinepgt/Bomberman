import javafx.application.Application;
import javafx.stage.Stage;


public class RunPartie extends Application{

    public static void main(String [ ] args){
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        VueMenu vm=new VueMenu();
        ControllerMenu cm = new ControllerMenu(vm);

        primaryStage.setScene(vm.getScene());
        primaryStage.show();

    }


}