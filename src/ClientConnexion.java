
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnexion implements Runnable{

    private Socket connexion = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private ControllerReseau controllerReseau;

    public ClientConnexion(String host, int port, ControllerReseau controllerReseau){
        this.controllerReseau = controllerReseau;
        try {
            connexion = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run(){
        try {
            writer = new PrintWriter(connexion.getOutputStream(), true);
            reader = new BufferedInputStream(connexion.getInputStream());
            String plateau = read();
            controllerReseau.analysePlateau(plateau);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String message;
        String response;
        while(true){
            try {
                Thread.currentThread().sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {

                message = controllerReseau.getMessageClient();
                writer.write(message);
                writer.flush();

                response = read();
                controllerReseau.setMessageServeur(response);


            } catch (IOException e1) {
                e1.printStackTrace();
            }


        }

    }

    private String read() throws IOException{
        String response;
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }


}