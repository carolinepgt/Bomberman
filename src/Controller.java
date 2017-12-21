import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

public class Controller {

    private View view;
    private Model model;


    private boolean[] goNorth;
    private boolean[] goEast;
    private boolean[] goSouth;
    private boolean[] goWest;

    public int sizeElem = 30;

    public Controller(View view, Model model) {

        this.view = view;
        this.model = model;
        int nbJoueur=model.getTabPerso().length;
        goNorth=new boolean[nbJoueur];
        goEast=new boolean[nbJoueur];
        goSouth=new boolean[nbJoueur];
        goWest=new boolean[nbJoueur];

        start();

    }

    public void start() {
        Scene scene = view.getScene();
        scene.setOnKeyPressed(this::keyEventPressed);
        scene.setOnKeyReleased(this::keyEventReleased);
        actionQuitter();
    }

    /*
    Action à la pression d'une touche
     */
    public void keyEventPressed(javafx.scene.input.KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                goNorth[1] = true;
                break;
            case RIGHT:
                goEast[1] = true;
                break;
            case DOWN:
                goSouth[1] = true;
                break;
            case LEFT:
                goWest[1] = true;
                break;
            case NUMPAD0:
                toucheBombe(1, 0);
                break;
            case NUMPAD1:
                toucheBombe(1, 1);
                break;
            case Z:
                goNorth[0]=true;
                break;
            case D:
                goEast[0] = true;
                break;
            case S:
                goSouth[0] = true;
                break;
            case Q:
                goWest[0] = true;
                break;
            case SPACE:
                toucheBombe(0, 0);
                break;
            case C:
                toucheBombe(0, 1);
                break;

        }
    }


    /*
    Action lorsque l'on relache la touche
     */
    public void keyEventReleased(javafx.scene.input.KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                goNorth[1] = false;
                break;
            case RIGHT:
                goEast[1] = false;
                break;
            case DOWN:
                goSouth[1] = false;
                break;
            case LEFT:
                goWest[1] = false;
                break;
            case Z:
                goNorth[0]=false;
                break;
            case D:
                goEast[0] = false;
                break;
            case S:
                goSouth[0] = false;
                break;
            case Q:
                goWest[0] = false;
                break;
        }
    }


    /*
    Gère les déplacements du personnages en fonction des attributs activé par la pression des touches
     */
    public void actualisePostion() {
        for (int i=0; i<model.getTabPerso().length; i++){
            Personnage perso= model.getTabPerso()[i];
            int changement=perso.actualisePosition(model.getPlateau(),goNorth[i], goEast[i], goSouth[i], goWest[i]);
            if (changement!=0) view.actualisePositionImage(changement, i);
            verifieEffet(perso);
        }

    }

    private void toucheBombe(int i, int typeBombe) {
        Bombe bombe = model.getTabPerso()[i].poseBombe(model.getPlateau().getTabElement(), typeBombe);
        if (bombe!=null ) {
            ImageView iVBombe = new ImageView(new Image(bombe.getImageURL()));
            iVBombe.relocate(bombe.getPosX() * sizeElem, bombe.getPosY() * sizeElem);
            view.insereElement(iVBombe, bombe.getPosX(), bombe.getPosY());


            ScaleTransition scaleAnimation = new ScaleTransition(Duration.seconds(2), iVBombe);
            scaleAnimation.setFromX(0.7);
            scaleAnimation.setToX(1);
            scaleAnimation.setFromY(0.7);
            scaleAnimation.setToY(1);
            scaleAnimation.play();


            scaleAnimation.setOnFinished(actionEvent -> suppressionElement(bombe.getPosX(), bombe.getPosY()));
        }
    }

    /*
    Supprime l'élement envoyé en paramètre
     */
    private void suppressionElement(int x, int y) {
        Element[][] elements = model.getPlateau().getTabElement();
        Element element=elements[x][y];
        if (element==null || !(element.getClass()==Effet.class)) {
            for (int i = 0; i < model.getTabPerso().length; i++) {
                Personnage perso = model.getTabPerso()[i];

                if (perso.getPosX()/30 == x && perso.getPosY()/30 == y) degatJoueur(i);
            }
        }

        if (element != null) {

            if (!(element.getClass() == Mur.class && !((Mur) element).isDestructible())) {
                elements[x][y] = null;
                view.supprimeImageView(x, y);

                if (element.getClass() == Mur.class) {
                    supprimeMur((Mur) element);
                }

                if (element.isBombe()) {
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
        bombe.getPersonnage().setNbBombeRestantes(bombe.getPersonnage().getNbBombeRestantes() + 1);
        int[] explosion = bombe.explosion(model.getPlateau());
        String couleurPerso = bombe.getPersonnage().getCouleur();

        for (int i=1; i<explosion[0]; i++) suppressionElement(bombe.getPosX(), bombe.getPosY()-i);
        for (int i=1; i<explosion[1]; i++) suppressionElement(bombe.getPosX()+i, bombe.getPosY());
        for (int i=1; i<explosion[2]; i++) suppressionElement(bombe.getPosX(), bombe.getPosY()+i);
        for (int i=1; i<explosion[3]; i++) suppressionElement(bombe.getPosX()-i, bombe.getPosY());

        view.afficheRange(explosion,bombe,couleurPerso);

    }

    /*
    Inflige un point de degat au joueur
     */
    private void degatJoueur(int indexPerso) {
        model.getTabPerso()[indexPerso].setVie(model.getTabPerso()[indexPerso].getVie() - 1);
        if (!model.getTabPerso()[indexPerso].estEnVie()) {
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
    private void verifieEffet(Personnage perso) {
        Element[][] elements =model.getPlateau().getTabElement();
        Element[] elementsAVerifier =new Element[4];
        elementsAVerifier[0]= elements[perso.getPosX()/Element.size][perso.getPosY()/Element.size];
        elementsAVerifier[1]= elements[(perso.getPosX()+Personnage.width)/Element.size][perso.getPosY()/Element.size];
        elementsAVerifier[2]= elements[(perso.getPosX()+Personnage.width)/Element.size][(perso.getPosY()+Personnage.height)/Element.size];
        elementsAVerifier[3]= elements[perso.getPosX()/Element.size][(perso.getPosY()+Personnage.height)/Element.size];
        for (int i=0; i<4; i++){
            if (elementsAVerifier[i]!= null && elementsAVerifier[i] instanceof Effet){
                boolean elementIdentique=false;
                for (int j=0; j<i; j++){
                    if (elementsAVerifier[i]==elementsAVerifier[j]) elementIdentique=true;
                }
                if (!elementIdentique) {
                    ((Effet) elementsAVerifier[i]).appliqueEffet(perso);
                    suppressionElement(elementsAVerifier[i].getPosX(), elementsAVerifier[i].getPosY());
                }
            }
        }
    }

    private void actionQuitter(){
        view.quitter.setOnAction(event -> Platform.exit());
    }

    private void actionChangerSkin(){
        view.changementSkin.setOnAction(event -> {
            if (view.skinNoel) view.mettreSkinBase();
            else view.mettreSkinNoel();
        });
    }
}