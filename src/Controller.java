
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import javax.swing.*;


public class Controller {

    private View view;
    private Model model;


    private boolean goNorth;
    private boolean goSouth;
    private boolean goWest;
    private boolean goEast;
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
                goNorth = true;
                break;
            case DOWN:
                goSouth = true;
                break;
            case LEFT:
                goWest = true;
                break;
            case RIGHT:
                goEast = true;
                break;
            case SPACE:
                poseBombe();
                break;
        }
    }

    /*
    Action lorsque l'on relache la touche
     */
    public void keyEventReleased(javafx.scene.input.KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                goNorth = false;
                break;
            case DOWN:
                goSouth = false;
                break;
            case LEFT:
                goWest = false;
                break;
            case RIGHT:
                goEast = false;
                break;
        }
    }


    /*
    Gère les déplacements du personnages en fonction des attributs activé par la pression des touches
     */
    public void actualisePostion() {
        Element[][] elements = model.getPlateau().getTabElement();
        Personnage perso = model.getTabPerso()[0];
        int width = (int) view.getImagePerso().getWidth();
        int height = (int) view.getImagePerso().getHeight();

        Element bombeProbableHG = elements[perso.getPosX() / sizeElem][perso.getPosY() / sizeElem];
        Element bombeProbableBG = elements[perso.getPosX() / sizeElem][(perso.getPosY() + height) / sizeElem];
        Element bombeProbableHD = elements[(perso.getPosX() + width) / sizeElem][perso.getPosY() / sizeElem];
        Element bombeProbableBD = elements[(perso.getPosX() + width) / sizeElem][(perso.getPosY() + height) / sizeElem];

        if (goNorth) {
            for (int i = 0; i < perso.getVitesse(); i++) {
                Element element1 = elements[perso.getPosX() / sizeElem][(perso.getPosY() - 1) / sizeElem];
                Element element2 = elements[(perso.getPosX() + width) / sizeElem][(perso.getPosY() - 1) / sizeElem];
                if ((element1 == null || element1.getClass() == Effet.class || element1 == bombeProbableHG) && (element2 == null || element2.getClass() == Effet.class || element2 == bombeProbableHD)) {
                    perso.setPosY(perso.getPosY() - 1);

                    view.actualisePositionImage(3);
                    appliqueEffet(element1, element2);
                }
            }
        }
        if (goEast) {
            for (int i = 0; i < perso.getVitesse(); i++) {
                Element element1 = elements[(perso.getPosX() + width + 1) / sizeElem][perso.getPosY() / sizeElem];
                Element element2 = elements[(perso.getPosX() + width + 1) / sizeElem][(perso.getPosY() + height) / sizeElem];
                if ((element1 == null || element1.getClass() == Effet.class || element1 == bombeProbableHD) && (element2 == null || element2.getClass() == Effet.class || element2 == bombeProbableBD)) {
                    perso.setPosX(perso.getPosX() + 1);
                    view.actualisePositionImage(2);
                    appliqueEffet(element1, element2);
                }
            }

        }
        if (goSouth) {
            for (int i = 0; i < perso.getVitesse(); i++) {
                Element element1 = elements[perso.getPosX() / sizeElem][(perso.getPosY() + height + 1) / sizeElem];
                Element element2 = elements[(perso.getPosX() + width) / sizeElem][(perso.getPosY() + height + 1) / sizeElem];
                if ((element1 == null || element1.getClass() == Effet.class || element1 == bombeProbableBG) && (element2 == null || element2.getClass() == Effet.class || element2 == bombeProbableBD)) {
                    perso.setPosY(perso.getPosY() + 1);
                    view.actualisePositionImage(1);
                    appliqueEffet(element1, element2);
                }
            }
        }
        if (goWest) {
            for (int i = 0; i < perso.getVitesse(); i++) {
                Element element1 = elements[(perso.getPosX() - 1) / sizeElem][perso.getPosY() / sizeElem];
                Element element2 = elements[(perso.getPosX() - 1) / sizeElem][(perso.getPosY() + height) / sizeElem];
                if ((element1 == null || element1.getClass() == Effet.class || element1 == bombeProbableHG) && (element2 == null || element2.getClass() == Effet.class || element2 == bombeProbableBG)) {
                    perso.setPosX(perso.getPosX() - 1);
                    view.actualisePositionImage(4);
                    appliqueEffet(element1, element2);
                }
            }
        }
    }

    /*
    Si c'est possible pose une bombe à la position du joueur
     */
    private void poseBombe() {

        Personnage perso = model.getTabPerso()[0];
        int posX = (perso.getPosX() + (int) view.getImagePerso().getWidth() / 2) / sizeElem;
        int posY = (perso.getPosY() + (int) view.getImagePerso().getHeight() / 2) / sizeElem;
        Element element = model.getPlateau().getTabElement()[posX][posY];

        if (perso.getNbBombeRestantes() > 0 && element == null) {
            element = new Bombe(perso, posX, posY);
            model.getPlateau().setElement(element, posX, posY);
            animationBombe((Bombe) element);
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
                suppressionElement(element);
            }
        });
    }

    /*
    Supprime l'élement envoyé en paramètre
     */
    private void suppressionElement(Element element) {

        Element[][] elements = model.getPlateau().getTabElement();
        if (element != null && element == elements[element.getPosX()][element.getPosY()]) {

            if (!(element.getClass() == Mur.class && !((Mur) element).isDestructible())) {
                elements[element.getPosX()][element.getPosY()] = null;
                view.supprimeImageView(element.getPosX(), element.getPosY());

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
        Effet effet = new Effet(element.getPosX(), element.getPosY());
        model.getPlateau().getTabElement()[element.getPosX()][element.getPosY()] = effet;
        ImageView imageEffet = new ImageView(new Image(effet.getImageURL()));
        imageEffet.relocate(element.getPosX() * sizeElem, element.getPosY() * sizeElem);
        view.insereElement(imageEffet, element.getPosX(), element.getPosY());
    }

    /*
    Gère l'explosion de la bombe: les elements en ligne et à portée de la bombe sont détruit, les joueurs dans cette zone subissent des degats
     */
    private void explosionBombe(Bombe bombe) {
        int x = model.getTabPerso()[0].getPosX() / sizeElem;
        int y = model.getTabPerso()[0].getPosY() / sizeElem;
        if (x == bombe.getPosX() && y == bombe.getPosY()) degatJoueur();

        Element[][] elements = model.getPlateau().getTabElement();

        bombe.getPersonnage().setNbBombeRestantes(bombe.getPersonnage().getNbBombeRestantes() + 1);

        boolean mur = false;
        Element e;
        for (int i = 1; i <= bombe.getPortee() && !mur; i++) {
            if (x == bombe.getPosX() - i && y == bombe.getPosY()) degatJoueur();
            e = elements[bombe.getPosX() - i][bombe.getPosY()];
            mur = e != null && e.getClass() == Mur.class;
            suppressionElement(e);
        }

        mur = false;
        for (int i = 1; i <= bombe.getPortee() && !mur; i++) {
            if (x == bombe.getPosX() + i && y == bombe.getPosY()) degatJoueur();
            e = elements[bombe.getPosX() + i][bombe.getPosY()];
            mur = e != null && e.getClass() == Mur.class;
            suppressionElement(e);
        }

        mur = false;
        for (int i = 1; i <= bombe.getPortee() && !mur; i++) {
            if (x == bombe.getPosX() && y == bombe.getPosY() - i) degatJoueur();
            e = elements[bombe.getPosX()][bombe.getPosY() - i];
            mur = e != null && e.getClass() == Mur.class;
            suppressionElement(e);
        }

        mur = false;
        for (int i = 1; i <= bombe.getPortee() && !mur; i++) {
            if (x == bombe.getPosX() && y == bombe.getPosY() + i) degatJoueur();
            e = elements[bombe.getPosX()][bombe.getPosY() + i];
            mur = e != null && e.getClass() == Mur.class;
            suppressionElement(e);
        }
    }

    /*
    Inflige un point de degat au joueur
     */
    private void degatJoueur() {
        model.getTabPerso()[0].setVie(model.getTabPerso()[0].getVie() - 1);
        if (model.getTabPerso()[0].getVie() <= 0) System.out.println("le joueur est mort, fin de la partie");
    }


    /*
    Applique un ou les effets présents a l'emplacement du déplacement du personnages,
     */
    private void appliqueEffet(Element element1, Element element2) {
        Personnage perso = model.getTabPerso()[0];
        if (element1 != null && element1.getClass() == Effet.class) {
            ((Effet) element1).appliqueEffet(perso);
            suppressionElement(element1);
        }
        if (element2 != null && element2.getClass() == Effet.class && element1 != element2) {
            ((Effet) element2).appliqueEffet(perso);
            suppressionElement(element2);
        }
    }

}


