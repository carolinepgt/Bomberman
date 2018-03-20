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
                if(f.posX%30==0 && f.posY%30==0)comportementFantome(f);

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


    private void comportementFantome(Fantome fantome) {
        fantome.initDirectionFantome();
        int posX = fantome.posX;
        int posY = fantome.posY;
        int cptTour = fantome.getCptTour();

        int indexEnnemiProche = getEnnemiProche(fantome);

        takeDecision(fantome, indexEnnemiProche);

        //Gestion de l'IA des fantomes avec un focntion récursive --> Trop LOURD  ++ gestion des "tours"
//        if(cptTour==0){
//            fantome.getChemin().clear();
//            ArrayList<Integer> directionTrouve = chercheUnCheminVersAdversaire(posX/30,posY/30,0, fantome.getChemin());
//            System.out.println("Direction trouver "+ directionTrouve+ " --> f :"+fantome);
//        }
//
//        if(!fantome.getChemin().isEmpty()){
//            switch (fantome.getChemin().get(cptTour)){
//                case 1 :
//                    fantome.setGoSouth(true);
//                    break;
//                case 2 :
//                    fantome.setGoEast(true);
//                    break;
//                case 3 :
//                    fantome.setGoNorth(true);
//                    break;
//                case 4 :
//                    fantome.setGoWest(true);
//                    break;
//            }
//        }
//        cptTour++;
//        if(cptTour>=2){
//            fantome.setCptTour(0);
//        } else {
//            fantome.setCptTour(cptTour);
//        }
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

        //go South
        if(plateau[xF][yF+1]==null && distance_SOUTH_p<distanceF_P)fantome.setGoSouth(true);

            //go East
        else if(plateau[xF+1][yF]==null && distance_EAST_p<distanceF_P)fantome.setGoEast(true);

            // go north
        else if(plateau[xF][yF-1]==null && distance_NORTH_p<distanceF_P)fantome.setGoNorth(true);

            // go West
        else if(plateau[xF-1][yF]==null && distance_WEST_p<distanceF_P)fantome.setGoWest(true);

            // go autre direction libre
        else testDirection(fantome);
    }

    private int getDistance(int xF, int yF, int xP, int yP) {
        return (int) Math.sqrt(Math.pow(xP-xF,2)+Math.pow(yP-yF,2));
    }


    private void testDirection(Fantome fantome) {
        int x_Fantome = fantome.getPosX()/30; int y_fantome = fantome.getPosY()/30;

        Element[][] plateau = model.getPlateau().getTabElement();

        if(plateau[x_Fantome][y_fantome-1]==null) fantome.setGoNorth(true);
        else if(plateau[x_Fantome-1][y_fantome]==null) fantome.setGoWest(true);
        else if(plateau[x_Fantome][y_fantome+1]==null) fantome.setGoSouth(true);
        else if(plateau[x_Fantome+1][y_fantome]==null) fantome.setGoEast(true);
        else fantome.initDirectionFantome();
    }

    /**
     * !!! N'EST FINALMENT PAS UTILIS2 MAIS PEUT ETRE UTILE PLUS TARD
     * Méthode qui calcul les distances entre le "fantome" et les perso et renvoie l'id du plus proche
     *
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

    //Fonction récursive gloutonne...
    private ArrayList<Integer> chercheUnCheminVersAdversaire(int posX, int posY, int depart, ArrayList<Integer> chemin) {
//        System.out.println();
        for (Personnage p: model.getTabPerso()) {
            System.out.println("posX:"+posX+" posY:"+posY+" posPerso : "+p.getPosX()/30+" "+p.getPosY()/30);
            if (posX==p.getPosX()/30 && posY==p.getPosY()/30) return chemin;
        }

        Element[][] plateau = model.getPlateau().getTabElement();
//        ArrayList<Integer> retour;

        if(plateau[posX-1][posY]==null && depart!= 2){
            System.out.println("goWest !");
            chemin.add(4);
            chercheUnCheminVersAdversaire(posX-1,posY,4,chemin);

        }else if(plateau[posX][posY-1]==null && depart!= 1){
            System.out.println("goNorth !");
            chemin.add(3);
            chercheUnCheminVersAdversaire(posX,posY-1,3,chemin);

        }else if(plateau[posX+1][posY]==null && depart!= 4){
            System.out.println("goEast !");
            chemin.add(2);
            chercheUnCheminVersAdversaire(posX+1,posY,2,chemin);

        }else if(plateau[posX][posY+1]==null && depart!= 3){
            System.out.println("goSouth !");
            chemin.add(1);
            chercheUnCheminVersAdversaire(posX,posY+1,1,chemin);

        } else {
            chemin.add(0);
        }

        if(chemin.get(chemin.size()-1)==0){
            chemin.remove(chemin.size()-1);
            if(depart==4){
                System.out.println("goEast !");
                chemin.remove(chemin.size()-1);
                return chercheUnCheminVersAdversaire(posX+1,posY,2,chemin);

            }else if(depart==1){
                System.out.println("goNorth !");
                chemin.remove(chemin.size()-1);
                return chercheUnCheminVersAdversaire(posX,posY-1,3,chemin);

            }else if(depart==3){
                System.out.println("goSouth !");
                chemin.remove(chemin.size()-1);
                return chercheUnCheminVersAdversaire(posX,posY+1,1,chemin);

            }else if(depart==2){
                System.out.println("goWest !");
                chemin.remove(chemin.size()-1);
                return chercheUnCheminVersAdversaire(posX-1,posY,4,chemin);
            } else {
                return chemin;
            }
        } else {
            return chemin;
        }
    }
}


