
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;



public class ControllerReseau {

    private View view;
    private Model model;

    private int nbJoueur;
    private boolean[] goNorth;
    private boolean[] goEast;
    private boolean[] goSouth;
    private boolean[] goWest;
    private boolean[] bombe;
    private boolean[] messageBombe;
    private String messageServeur;
    private int nj;

    public int sizeElem = 30;
    private int[] changement;

    public ControllerReseau(View view, Model model, int numJoueur) {

        this.view = view;
        this.model = model;
        nbJoueur=model.getTabPerso().length;
        goNorth=new boolean[nbJoueur];
        goEast=new boolean[nbJoueur];
        goSouth=new boolean[nbJoueur];
        goWest=new boolean[nbJoueur];
        bombe=new boolean[nbJoueur];
        changement=new int[nbJoueur];
        messageBombe=new boolean[nbJoueur];
        messageServeur="";
        nj=numJoueur;


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
            case Z:
                goNorth[nj]=true;
                break;
            case D:
                goEast[nj] = true;
                break;
            case S:
                goSouth[nj] = true;
                break;
            case Q:
                goWest[nj] = true;
                break;
            case SPACE:
                bombe[nj]=true;
                break;
        }
    }


    /*
    Action lorsque l'on relache la touche
     */
    public void keyEventReleased(javafx.scene.input.KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case Z:
                goNorth[nj]=false;
                break;
            case D:
                goEast[nj] = false;
                break;
            case S:
                goSouth[nj] = false;
                break;
            case Q:
                goWest[nj] = false;
                break;
            case SPACE:
                bombe[nj]=false;
                break;
        }
    }


    public void setMessageClient(String messageClient) {
        char[] m=messageClient.toCharArray();
        int n=Integer.parseInt(""+m[0]);

        goNorth[n]=m[1]=='1';
        goEast[n]=m[2]=='1';
        goSouth[n]=m[3]=='1';
        goWest[n]=m[4]=='1';
        bombe[n]=m[5]=='1';
    }

    private void creeMessageServeur() {
        String message="";
        int n=0;
        for (Personnage p : model.getTabPerso()){
            message+=p.getPosX()+"-"+p.getPosY()+"-"+changement[n]+"-";
            if (messageBombe[n]) {
                message+=1;
                messageBombe[n]=false;
            }
            else message+=0;
            message+="_";
            n++;
        }
        messageServeur=message+"_"+model.partieFini();
    }

    public void setMessageServeur(String messageServeur) {
        this.messageServeur = messageServeur;
    }


    private void analyseMessageServeur(){
        String[] m=messageServeur.split("_");
        String[] mp;
        for (int i = 0 ; i<nbJoueur; i++){
            Personnage p = model.getTabPerso()[i];
            mp = m[i].split("-");
            p.setPosX(mp[0]);
            p.setPosY(mp[1]);
            verifieEffet(p);
            p.setChangement(mp[2]);
            if (mp[3].equals("1")) toucheBombe(i,0);
        }

        Platform.runLater(() -> {
            for (int i = 0; i < model.getTabPerso().length; i++) {
                view.actualisePositionImage(model.getTabPerso()[i].getChangement(), i);
            }
        });
    }


    public String getMessageClient() {

        String message=""+nj;
        if(goNorth[nj])message+=1;
        else message+=0;
        if(goEast[nj])message+=1;
        else message+=0;
        if(goSouth[nj])message+=1;
        else message+=0;
        if(goWest[nj])message+=1;
        else message+=0;
        if(bombe[nj])message+=1;
        else message+=0;
        return message;
    }

    public String getMessageServeur() {
        creeMessageServeur();
        return messageServeur;
    }





    public String creeMessagePlateau() {
        String message="";
        for (int i =0; i<model.getPlateau().getTabElement().length; i++){
            for (Element e : model.getPlateau().getTabElement()[i]){
                if (e==null)message+="N-";
                else message+=e.toString()+"-";
            }
        }
        return message;
    }


    private void toObject(int x, int y, String s) {
        Element[][] tabE=model.getPlateau().getTabElement();
        if (s.equals("N")){
            tabE[x][y]=null;
            return;
        }
        if (s.equals("MI")){
            tabE[x][y]=new Mur( false, x, y);
            return;
        }
        for (int i=0; i<6; i++){
            if (s.equals("M"+i)){
                tabE[x][y]=new Mur( true, x, y, i);
                return;
            }
        }

    }

    public void analysePlateau(String plateau){
        String[] m;
        m=plateau.split("-");
        int taille = model.getPlateau().getTabElement().length;
        for (int i=0; i<taille; i++){
            for (int j=0; j<taille; j++){
                toObject(i, j, m[i*taille+j]);
            }
        }

        view.initialisePlateauReseau();
    }




    /****/



    /*
            Gère les déplacements du personnages en fonction des attributs activé par la pression des touches
             */
    public void actualisePostion() {
        if (nj==0) {
            for (int i = 0; i < model.getTabPerso().length; i++) {
                if (bombe[i]) toucheBombe(i, 0);
                Personnage perso = model.getTabPerso()[i];
                changement[i] = perso.actualisePosition(model.getPlateau(), goNorth[i], goEast[i], goSouth[i], goWest[i]);
                if (changement[i] != 0) view.actualisePositionImage(changement[i], i);

                verifieEffet(perso);

            }
        }
        else if (!messageServeur.equals(""))analyseMessageServeur();

    }

    private void toucheBombe(int i, int typeBombe) {
        bombe[i]=false;
        messageBombe[i]=true;

        Bombe bombe = model.getTabPerso()[i].poseBombe(model.getPlateau().getTabElement(), typeBombe);
        if (bombe!=null ) {
            ImageView iVBombe = new ImageView(new Image(bombe.getImageURL()));
            iVBombe.relocate(bombe.getPosX() * sizeElem, bombe.getPosY() * sizeElem);
            view.insereElementToBack(iVBombe, bombe.getPosX(), bombe.getPosY());

            ScaleTransition scaleAnimation = new ScaleTransition(Duration.seconds(2), iVBombe);
            scaleAnimation.setFromX(0.7);
            scaleAnimation.setToX(1);
            scaleAnimation.setFromY(0.7);
            scaleAnimation.setToY(1);
            scaleAnimation.play();


            scaleAnimation.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    suppressionElement(bombe.getPosX(), bombe.getPosY());
                }
            });
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
        if (element.getEffet()!=null){
            model.getPlateau().getTabElement()[element.getPosX()][element.getPosY()] = element.getEffet();
            ImageView imageEffet = new ImageView(new Image(element.getEffet().getImageURL()));
            imageEffet.relocate(element.getPosX() * sizeElem, element.getPosY() * sizeElem);
            view.insereElementToBack(imageEffet, element.getPosX(), element.getPosY());
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


    public Model getModel() {
        return model;
    }
}


