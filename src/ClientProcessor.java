
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientProcessor implements Runnable{

    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private ControllerReseau controllerReseau;

    public ClientProcessor(Socket pSock, ControllerReseau controllerReseau){
        sock = pSock;
        this.controllerReseau = controllerReseau;
    }

    public void run(){
        //tant que la connexion est active, on traite les demandes

        try {
            writer = new PrintWriter(sock.getOutputStream());
            reader = new BufferedInputStream(sock.getInputStream());
            String plateau= controllerReseau.creeMessagePlateau();
            writer.write(plateau);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!sock.isClosed()){

            try {
                String response = read();

                controllerReseau.setMessageClient(response);
                writer.write(controllerReseau.getMessageServeur());
                writer.flush();

            }catch(SocketException e){
                System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
                break;
            } catch (IOException e) {
                e.printStackTrace();
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