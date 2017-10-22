
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import javax.swing.*;
import java.util.Random;


public class Controller {

    private View view;
    private Model model;


    private boolean goNorth;
    private boolean goSouth;
    private boolean goWest;
    private boolean goEast;

    private boolean goNorth2;
    private boolean goSouth2;
    private boolean goWest2;
    private boolean goEast2;
    public int sizeElem = 30;

    public Controller(View view, Model model) {

        this.view = view;
        this.model = model;
        start();

    }

    public void start() {
        Scene scene = view.getScene();
        scene.setOnKeyPressed(keyEvent -> keyEventPressed(keyEvent));
        scene.setOnKeyReleased(keyEvent -> keyEventReleased(keyEvent));
    }

    /*
    Action à la pression d'une touche
     */
    public void keyEventPressed(javafx.scene.input.KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                goNorth2 = true;
                break;
            case DOWN:
                goSouth2 = true;
                break;
            case LEFT:
                goWest2 = true;
                break;
            case RIGHT:
                goEast2 = true;
                break;
            case NUMPAD0:
                Personnage perso = model.getTabPerso()[1];
                if(perso.getHaveMine()){
                    poseMine(1);
                }
                else {
                    poseBombe(1);
                }
                break;
            case Z:
                goNorth=true;
                break;
            case S:
                goSouth = true;
                break;
            case Q:
                goWest = true;
                break;
            case D:
                goEast = true;
                break;
            case SPACE:
                Personnage perso2 = model.getTabPerso()[0];
                if(perso2.getHaveMine()){
                    poseMine(0);
                }
                else {
                    poseBombe(0);
                }
                break;

        }
    }

    /*
    Action lorsque l'on relache la touche
     */
    public void keyEventReleased(javafx.scene.input.KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                goNorth2 = false;
                break;
            case DOWN:
                goSouth2 = false;
                break;
            case LEFT:
                goWest2 = false;
                break;
            case RIGHT:
                goEast2 = false;
                break;
            case Z:
                goNorth=false;
                break;
            case S:
                goSouth = false;
                break;
            case Q:
                goWest = false;
                break;
            case D:
                goEast = false;
                break;
        }
    }


    /*
    Gère les déplacements du personnages en fonction des attributs activé par la pression des touches
     */
    public void actualisePosition() {
        Element[][] elements = model.getPlateau().getTabElement();
        int indexPerso=0;
        for (Personnage perso:model.getTabPerso()){
            int width = (int) view.getImagePerso().getWidth();
            int height = (int) view.getImagePerso().getHeight();

            Element bombeProbableHG = elements[perso.getPosX() / sizeElem][perso.getPosY() / sizeElem];
            Element bombeProbableBG = elements[perso.getPosX() / sizeElem][(perso.getPosY() + height) / sizeElem];
            Element bombeProbableHD = elements[(perso.getPosX() + width) / sizeElem][perso.getPosY() / sizeElem];
            Element bombeProbableBD = elements[(perso.getPosX() + width) / sizeElem][(perso.getPosY() + height) / sizeElem];

            if ((goNorth && perso==model.getTabPerso()[0])||(goNorth2 && perso==model.getTabPerso()[1])) {
                for (int i = 0; i < perso.getVitesse(); i++) {
                    Element element1 = elements[perso.getPosX() / sizeElem][(perso.getPosY() - 1) / sizeElem];
                    Element element2 = elements[(perso.getPosX() + width) / sizeElem][(perso.getPosY() - 1) / sizeElem];
                    if ((element1 == null || element1.getClass() == Effet.class || element1.getClass() == Mine.class || element1 == bombeProbableHG) && (element2 == null || element2.getClass() == Effet.class || element2.getClass() == Mine.class || element2 == bombeProbableHD)) {
                        perso.setPosY(perso.getPosY() - 1);
                        view.actualisePositionImage(3, indexPerso);
                        appliqueEffet(element1, element2, perso);
                        if(element1 != null && element1.getClass()== Mine.class) {
                            declencheMine((Mine)element1,indexPerso);
                        }
                        else if ( element2 != null && element2.getClass()== Mine.class){
                            declencheMine((Mine)element2,indexPerso);
                        }
                    }
                }
            }
            if ((goEast && perso==model.getTabPerso()[0])||(goEast2 && perso==model.getTabPerso()[1])) {
                for (int i = 0; i < perso.getVitesse(); i++) {
                    Element element1 = elements[(perso.getPosX() + width + 1) / sizeElem][perso.getPosY() / sizeElem];
                    Element element2 = elements[(perso.getPosX() + width + 1) / sizeElem][(perso.getPosY() + height) / sizeElem];
                    if ((element1 == null || element1.getClass() == Effet.class || element1.getClass() == Mine.class || element1 == bombeProbableHD) && (element2 == null || element2.getClass() == Effet.class || element2.getClass() == Mine.class || element2 == bombeProbableBD)) {
                        perso.setPosX(perso.getPosX() + 1);
                        view.actualisePositionImage(2, indexPerso);
                        appliqueEffet(element1, element2, perso);
                        if(element1 != null && element1.getClass()== Mine.class) {
                            declencheMine((Mine)element1,indexPerso);
                        }
                        else if ( element2 != null && element2.getClass()== Mine.class){
                            declencheMine((Mine)element2,indexPerso);
                        }
                    }
                }

            }
            if ((goSouth && perso==model.getTabPerso()[0])||(goSouth2 && perso==model.getTabPerso()[1])) {
                for (int i = 0; i < perso.getVitesse(); i++) {
                    Element element1 = elements[perso.getPosX() / sizeElem][(perso.getPosY() + height + 1) / sizeElem];
                    Element element2 = elements[(perso.getPosX() + width) / sizeElem][(perso.getPosY() + height + 1) / sizeElem];
                    if ((element1 == null || element1.getClass() == Effet.class || element1.getClass() == Mine.class || element1 == bombeProbableBG) && (element2 == null || element2.getClass() == Effet.class || element2.getClass() == Mine.class || element2 == bombeProbableBD)) {
                        perso.setPosY(perso.getPosY() + 1);
                        view.actualisePositionImage(1, indexPerso);
                        appliqueEffet(element1, element2, perso);
                        if(element1 != null && element1.getClass()== Mine.class) {
                            declencheMine((Mine)element1,indexPerso);
                        }
                        else if ( element2 != null && element2.getClass()== Mine.class){
                            declencheMine((Mine)element2,indexPerso);
                        }
                    }
                }
            }
            if ((goWest && perso==model.getTabPerso()[0])||(goWest2 && perso==model.getTabPerso()[1])) {
                for (int i = 0; i < perso.getVitesse(); i++) {
                    Element element1 = elements[(perso.getPosX() - 1) / sizeElem][perso.getPosY() / sizeElem];
                    Element element2 = elements[(perso.getPosX() - 1) / sizeElem][(perso.getPosY() + height) / sizeElem];
                    if ((element1 == null || element1.getClass() == Effet.class || element1.getClass() == Mine.class || element1 == bombeProbableHG) && (element2 == null || element2.getClass() == Effet.class || element2.getClass() == Mine.class || element2 == bombeProbableBG)) {
                        perso.setPosX(perso.getPosX() - 1);
                        view.actualisePositionImage(4, indexPerso);
                        appliqueEffet(element1, element2, perso);
                        if(element1 != null && element1.getClass()== Mine.class) {
                            declencheMine((Mine)element1,indexPerso);
                        }
                        else if ( element2 != null && element2.getClass()== Mine.class){
                            declencheMine((Mine)element2,indexPerso);
                        }
                    }
                }
            }
            indexPerso++;
        }
    }

    public void declencheMine(Mine m,int indexPerso){
        if(m.getVieMine()==0){
            degatJoueur(indexPerso);
            suppressionElement(m,m.getPosX(),m.getPosY());
        }
        else {
            m.setVieMine(m.getVieMine()-1);
        }
    }

    /*
    Si c'est possible pose une bombe à la position du joueur
     */
    private void poseBombe(int indexPerso) {

        Personnage perso = model.getTabPerso()[indexPerso];
        if (!perso.estEnVie()) return;
        int posX = (perso.getPosX() + (int) view.getImagePerso().getWidth() / 2) / sizeElem;
        int posY = (perso.getPosY() + (int) view.getImagePerso().getHeight() / 2) / sizeElem;
        Element element = model.getPlateau().getTabElement()[posX][posY];

        if (perso.getNbBombeRestantes() > 0 && element == null) {
            element = new Bombe(perso, posX, posY);
            model.getPlateau().setElement(element, posX, posY);
            animationBombe((Bombe) element);
        }
    }

    private void poseMine(int indexPerso) {

        Personnage perso = model.getTabPerso()[indexPerso];
        if (!perso.estEnVie()) return;
        int posX = (perso.getPosX() + (int) view.getImagePerso().getWidth() / 2) / sizeElem;
        int posY = (perso.getPosY() + (int) view.getImagePerso().getHeight() / 2) / sizeElem;
        Element element = model.getPlateau().getTabElement()[posX][posY];

        if (perso.getHaveMine() && element == null) {
            element = new Mine(posX, posY);
            model.getPlateau().setElement(element, posX, posY);
            ImageView mine = new ImageView(new Image(element.getImageURL()));
            mine.relocate(element.getPosX() * sizeElem, element.getPosY() * sizeElem);
            view.insereElement(mine, element.getPosX(), element.getPosY());
            perso.setHaveMine(false);
        }
    }

    /*
    Gère l'animation de la bombe
     */
    public void animationBombe(Bombe element) {

        ImageView bombe = new ImageView(new Image(element.getImageURL()));
        bombe.relocate(element.getPosX() * sizeElem, element.getPosY() * sizeElem);
        view.insereElement(bombe, element.getPosX(), element.getPosY());


        ScaleTransition scaleAnimation = new ScaleTransition(Duration.seconds(2), bombe);
        scaleAnimation.setFromX(0.7);
        scaleAnimation.setToX(1);
        scaleAnimation.setFromY(0.7);
        scaleAnimation.setToY(1);
        scaleAnimation.play();


        scaleAnimation.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                suppressionElement(element, element.getPosX(), element.getPosY());
            }
        });
    }

    /*
    Supprime l'élement envoyé en paramètre
     */
    private void suppressionElement(Element element, int x, int y) {
        if (element==null || !(element.getClass()==Effet.class)) {
            for (int i = 0; i < model.getTabPerso().length; i++) {
                Personnage perso = model.getTabPerso()[i];

                if (perso.getPosX()/30 == x && perso.getPosY()/30 == y) degatJoueur(i);
            }
        }

        Element[][] elements = model.getPlateau().getTabElement();
        if (element != null && element == elements[x][y]) {

            if (!(element.getClass() == Mur.class && !((Mur) element).isDestructible())) {
                elements[x][y] = null;
                view.supprimeImageView(x, y);

                if (element.getClass() == Mur.class) {
                    supprimeMur((Mur) element);
                }

                if (element.getClass() == Bombe.class) {
                    explosionBombe((Bombe) element);
                }

            }
        }
    }

    /*
    Crée un effet à la place du mur détruit
     */
    private void supprimeMur(Mur element) {
        Random random=new Random();
        if (random.nextInt(3)==0){
            Effet effet = new Effet(element.getPosX(), element.getPosY());
            model.getPlateau().getTabElement()[element.getPosX()][element.getPosY()] = effet;
            ImageView imageEffet = new ImageView(new Image(effet.getImageURL()));
            imageEffet.relocate(element.getPosX() * sizeElem, element.getPosY() * sizeElem);
            view.insereElement(imageEffet, element.getPosX(), element.getPosY());
        }
    }

    /*
    Gère l'explosion de la bombe: les elements en ligne et à portée de la bombe sont détruit, les joueurs dans cette zone subissent des degats
     */

    private void explosionBombe(Bombe bombe) {

        Element[][] elements = model.getPlateau().getTabElement();

        bombe.getPersonnage().setNbBombeRestantes(bombe.getPersonnage().getNbBombeRestantes() + 1);
        int x =bombe.getPosX();
        int y =bombe.getPosY();

        boolean mur = false;
        Element e;
        for (int i = 1; i <= bombe.getPortee() && !mur; i++) {
            e = elements[x - i][y];
            mur = e != null && e.getClass() == Mur.class;
            suppressionElement(e, x-i,y);
        }

        mur = false;
        for (int i = 1; i <= bombe.getPortee() && !mur; i++) {
            e = elements[x + i][y];
            mur = e != null && e.getClass() == Mur.class;
            suppressionElement(e,x+i,y);
        }

        mur = false;
        for (int i = 1; i <= bombe.getPortee() && !mur; i++) {
            e = elements[x][y - i];
            mur = e != null && e.getClass() == Mur.class;
            suppressionElement(e, x, y-i);
        }

        mur = false;
        for (int i = 1; i <= bombe.getPortee() && !mur; i++) {
            e = elements[x][y + i];
            mur = e != null && e.getClass() == Mur.class;
            suppressionElement(e, x, y+i);
        }
    }

    /*
    Inflige un point de degat au joueur
     */
    private void degatJoueur(int indexPerso) {
        model.getTabPerso()[indexPerso].setVie(model.getTabPerso()[indexPerso].getVie() - 1);
        if (model.getTabPerso()[indexPerso].getVie() <= 0) {
            view.supprimeImagePersonnage(indexPerso);
            if (model.partieFini()){
                view.afficheFenetreFin();
                view.getScene().setOnKeyPressed(null);
            }
        }
    }


    /*
    Applique un ou les effets présents a l'emplacement du déplacement du personnages,
     */
    private void appliqueEffet(Element element1, Element element2, Personnage perso) {
        if (element1 != null && element1.getClass()==Effet.class) {
            ((Effet) element1).appliqueEffet(perso);
            suppressionElement(element1, element1.getPosX(), element1.getPosY());
        }
        if (element2 != null && element2.getClass()==Effet.class && element1 != element2) {
            ((Effet) element2).appliqueEffet(perso);
            suppressionElement(element2, element2.getPosX(), element2.getPosY());
        }
    }

}


