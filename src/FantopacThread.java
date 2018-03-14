import javafx.application.Platform;

import java.util.Random;

/**
 * Created by Yoann on 04/03/2018.
 */
public class FantopacThread extends Thread {
    private Model model;
    private int nbJoueur;
    private Controller controller;
    private Runnable blop;

    FantopacThread(Model model, int nbJoueur, Controller controller){
        this.model = model;
        this.nbJoueur = nbJoueur;
        this.controller = controller;
    }

    public void run(){
        while(true){
            if(model.getFantopac().posX%30==0 && model.getFantopac().posY%30==0)comportementFantopac();

            try {
                sleep(15);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            if(!controller.getView().getScene().getWindow().isShowing() || model.getFantopac().getVie()<1)break;
            if(model.isPartiePacman())break;
        }
    }


    private void initDirectionFantopac() {
        controller.setGoNorthI(nbJoueur,false);
        controller.setGoEastI(nbJoueur,false);
        controller.setGoSouthI(nbJoueur,false);
        controller.setGoWestI(nbJoueur,false);
    }

    private void comportementFantopac() {
        initDirectionFantopac();
        Fantopac fantopac = model.getFantopac();
        int posX = fantopac.posX;
        int posY = fantopac.posY;

//        System.out.println("pos X :"+posX+" \npos Y : "+posY+"\n");

        if(posX>=570){
//            System.out.println("Bord -->");
            controller.setGoWestI(nbJoueur,true);
        } else if(posY>=570) {
//            System.out.println("Bord Bas");
            controller.setGoNorthI(nbJoueur,true);
        } else if(posX<=30) {
//            System.out.println("Bord <--");
            controller.setGoEastI(nbJoueur,true);
        } else if(posY<=30) {
//            System.out.println("Bord Haut");
            controller.setGoSouthI(nbJoueur,true);
        } else {
            Random loto = new Random();
            int valeur = loto.nextInt(4);
            valeur = 1;
            if(valeur==1){
                controller.setGoNorthI(nbJoueur,true);
            } else if(valeur==2){
                controller.setGoEastI(nbJoueur,true);
            } else if(valeur==3) {
                controller.setGoSouthI(nbJoueur,true);
            } else if(valeur==0){
                controller.setGoWestI(nbJoueur,true);
            }
        }

    }
}
