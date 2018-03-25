import javax.naming.ldap.PagedResultsControl;
import java.util.ArrayList;
import java.util.Random;


public class FantomeThread extends Thread {
    private Model model;
    private Controller controller;

    FantomeThread(Model model, Controller controller){
        this.model = model;
        this.controller = controller;
    }

    public void run(){
        while(true){
            for (Fantome f : model.getTabFantome()) {
                if(f!=null){

                    if(f.posX%30==0 && f.posY%30==0)comportementFantome(f);

                }
                try {
                    sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            if(!controller.getView().getScene().getWindow().isShowing())break;
        }
    }

    private synchronized void comportementFantome(Fantome fantome) {
        fantome.initDirectionFantome();

        int indexEnnemiProche = getEnnemiProche(fantome);

        takeDecision(fantome, indexEnnemiProche);
    }

    private void takeDecision(Fantome fantome, int indexEnnemi) {
        int xF = fantome.getPosX()/30;
        int yF = fantome.getPosY()/30;

        int xP = model.getTabPerso()[indexEnnemi].getPosX()/30;
        int yP = model.getTabPerso()[indexEnnemi].getPosY()/30;

        int distanceF_P =  getDistance(xF,yF,xP,yP);

        Element[][] plateau = model.getPlateau().getTabElement();

        fantome.initDirectionFantome();


        int distance_SOUTH_p = getDistance(xF,yF+1,xP,yP);
        int distance_EAST_p = getDistance(xF+1,yF,xP,yP);
        int distance_NORTH_p = getDistance(xF,yF-1,xP,yP);
        int distance_WEST_p = getDistance(xF-1,yF,xP,yP);


        if(fantome.isAttaque()){
            //go South
            if(plateau[xF][yF+1]==null && distance_SOUTH_p<distanceF_P && isNotFantome(xF,yF+1))fantome.setGoSouth(true);

                //go East
            else if(plateau[xF+1][yF]==null && distance_EAST_p<distanceF_P && isNotFantome(xF+1,yF))fantome.setGoEast(true);

                // go north
            else if(plateau[xF][yF-1]==null && distance_NORTH_p<distanceF_P && isNotFantome(xF,yF-1))fantome.setGoNorth(true);

                // go West
            else if(plateau[xF-1][yF]==null && distance_WEST_p<distanceF_P && isNotFantome(xF-1,yF))fantome.setGoWest(true);

                // go autre direction libre
            else testDirection(fantome);
        } else {

            //go South
            if((plateau[xF][yF+1]==null || plateau[xF][yF+1].getClass()==Effet.class) && distance_SOUTH_p>distanceF_P)fantome.setGoSouth(true);

                //go East
            else if((plateau[xF+1][yF]==null || plateau[xF+1][yF].getClass()==Effet.class) && distance_EAST_p>distanceF_P)fantome.setGoEast(true);

                // go north
            else if((plateau[xF][yF-1]==null || plateau[xF][yF-1].getClass()==Effet.class) && distance_NORTH_p>distanceF_P)fantome.setGoNorth(true);

                // go West
            else if((plateau[xF-1][yF]==null || plateau[xF-1][yF].getClass()==Effet.class) && distance_WEST_p>distanceF_P)fantome.setGoWest(true);

                // go autre direction libre
            else testDirection(fantome);
        }
    }

    private boolean isNotFantome(int xF, int yF) {
        for (Fantome f : model.getTabFantome()) {
            if(f.getPosX()/30==xF && f.getPosY()/30==yF)return false;
        }
        return true;
    }

    private int getDistance(int xF, int yF, int xP, int yP) {
        return (int) Math.sqrt(Math.pow(xP-xF,2)+Math.pow(yP-yF,2));
    }


    private void testDirection(Fantome fantome) {
        int x_Fantome = fantome.getPosX()/30; int y_fantome = fantome.getPosY()/30;

        Element[][] plateau = model.getPlateau().getTabElement();

        Random random = new Random();
        boolean ok=false;
        int choix;

        while (!ok){
            choix = random.nextInt(4);
            switch (choix){
                case 0 :
                    if(plateau[x_Fantome][y_fantome-1]==null || plateau[x_Fantome][y_fantome-1].getClass()==Effet.class){
                        fantome.setGoNorth(true);
                        ok=true;
                        break;
                    }
                case 1 :
                    if(plateau[x_Fantome-1][y_fantome]==null || plateau[x_Fantome-1][y_fantome].getClass()==Effet.class) {
                        fantome.setGoWest(true);
                        ok=true;
                        break;
                    }
                case 2 :
                    if(plateau[x_Fantome][y_fantome+1]==null || plateau[x_Fantome][y_fantome+1].getClass()==Effet.class) {
                        fantome.setGoSouth(true);
                        ok=true;
                        break;
                    }
                case 3 :
                    if(plateau[x_Fantome+1][y_fantome]==null || plateau[x_Fantome+1][y_fantome].getClass()==Effet.class) {
                        fantome.setGoEast(true);
                        ok = true;
                        break;
                    }
            }
        }
    }

    /**
     * Méthode qui calcul les distances entre le "fantome" et les perso et renvoie l'id du plus proche
     * **/
    private int getEnnemiProche(Fantome fantome) {
        Personnage[] tabPerso = model.getTabPerso();
        int indexEnnemiLePlusProche=0;
        int distanceMin = 1000000;

        for (int i=0; i<tabPerso.length;i++) {
            Personnage p = tabPerso[i];
            int xP = p.getPosX()/30;  int yP = p.getPosY()/30;
            int xF = fantome.posX/30; int yF = fantome.posY/30;

            int distanceF_P = (int) Math.sqrt(Math.pow(xP-xF,2)+Math.pow(yP-yF,2));
            if(distanceF_P<distanceMin){
                distanceMin=distanceF_P;
                indexEnnemiLePlusProche=i;
            }
        }

        return indexEnnemiLePlusProche;
    }
}


