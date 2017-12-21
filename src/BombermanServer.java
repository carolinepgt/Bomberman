import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class BombermanServer {

    //On initialise des valeurs par défaut
    private int port;
    private String host;
    private ServerSocket server = null;
    private boolean isRunning = true;
    private ControllerReseau controllerReseau;
    private boolean clientConnecte=false;

    public BombermanServer(String pHost, int pPort, ControllerReseau controllerReseau){
        host = pHost;
        port = pPort;
        this.controllerReseau = controllerReseau;
        try {
            server = new ServerSocket(port, 100, InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //On lance notre serveur
    public void open(){
        final int nbJoueur= controllerReseau.getModel().getTabPerso().length;

                int n=1;
                while(isRunning && n<nbJoueur){

                    try {
                        //On attend une connexion d'un client
                        Socket client = server.accept();
                        n++;
                        //Une fois reçue, on la traite dans un thread séparé
                        System.out.println("Connexion cliente reçue.");
                        Thread t = new Thread(new ClientProcessor(client, controllerReseau));
                        t.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    server = null;
                }
                clientConnecte=true;


    }

    public void attendClientConnecte() {
        while(!clientConnecte){
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        isRunning = false;
    }
}