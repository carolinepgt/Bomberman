import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

public class Controller {

    private View view;
    private Model model;
    private int nbJoueur;
    private FantopacThread fantopacThread;


    private boolean[] goNorth;
    private boolean[] goEast;
    private boolean[] goSouth;
    private boolean[] goWest;

    public int sizeElem = 30;

    public Controller(View view, Model model) {

        this.view = view;
        this.model = model;
        this.nbJoueur =model.getTabPerso().length;
        goNorth=new boolean[nbJoueur+1];
        goEast=new boolean[nbJoueur+1];
        goSouth=new boolean[nbJoueur+1];
        goWest=new boolean[nbJoueur+1];

        this.fantopacThread = new FantopacThread(model,nbJoueur,this);
        start();

    }

    public void start() {
        Scene scene = view.getScene();
        scene.setOnKeyPressed(this::keyEventPressed);
        scene.setOnKeyReleased(this::keyEventReleased);
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
        if(model.nbsJoueurs==1){
            comportementIA();
        }

//        System.out.println("fantopacThread.getState() : "+fantopacThread.getState());
        if(fantopacThread.getState()== Thread.State.NEW)fantopacThread.start();

        for (int i=0; i<model.getTabPerso().length; i++){
            Personnage perso= model.getTabPerso()[i];
            int changement=perso.actualisePosition(model.getPlateau(),goNorth[i], goEast[i], goSouth[i], goWest[i]);
            if (changement!=0)view.actualisePositionImage(changement, i);
            verifieEffet(perso);
        }


        Fantopac fantopac = model.getFantopac();
        int changementFantopac = fantopac.actualisePosition(model.getPlateau(),goNorth[nbJoueur], goEast[nbJoueur], goSouth[nbJoueur], goWest[nbJoueur]);
        if (changementFantopac!=0)view.actualisePositionImageFantopac(changementFantopac);
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



    /******************** Methode Fantopac******************************************************/



    /******************** Methode IA ******************************************************/
    private void initDirectionIA(int i) {
        goNorth[i]=false;
        goEast[i]=false;
        goSouth[i]=false;
        goWest[i]=false;
    }

    private void comportementIA() {
        for (int i=0; i<model.getTabPerso().length; i++){
            if(model.getTabPerso()[i] instanceof Personnage_IA){
                Personnage_IA perso = (Personnage_IA) model.getTabPerso()[i];
                int xPerso = perso.getPosX();
                int yPerso = perso.getPosY();

                //if (xPerso%30==0 && yPerso%30==0)initDirectionIA(i);
                chercheGo(perso);
/*
                Node root = perso.getTree().createRoot(xPerso,yPerso, 0);
                Element[][] tabElement = model.getPlateau().getTabElement();
                Personnage[] tabPerso = model.getTabPerso();
                boolean[][] tabVisited = new boolean[21][21];

                tree.buildTree(root,tabVisited,tabElement, tabPerso);*/
            }
        }
    }

    private void chercheGo(Personnage_IA perso){
        int xIA = (perso.getPosX())/30;
        int yIA = (perso.getPosY())/30;

        int xCible = 0;
        int yCible = 0;

        Element[][] tabElement = model.getPlateau().getTabElement();


        for (int i=0; i<model.getTabPerso().length; i++) {
            if (!(model.getTabPerso()[i] instanceof Personnage_IA)) {
                xCible = model.getTabPerso()[i].getPosX()/30;
                yCible = model.getTabPerso()[i].getPosY()/30;
            }
        }

        //System.out.println("xCible : "+xCible+" yCible :"+yCible);


        int[][] caseSafe = rechercheCaseSafe();
        int iTabPerso = perso.getPositionTabPerso();
        int xDiff = xCible-xIA;
        int yDiff = yCible-yIA;
        int diff = Math.abs(xDiff)-Math.abs(yDiff);
        if ((perso.getPosX() % 30 == 0 && perso.getPosY() % 30 == 0)) {

            if(caseSafe[xIA][yIA]==0 && perso.getNbBombeRestantes()!=0) {
//                System.out.println("Mon perso a des bombe.je me tire pas");

                if (diff >= 0) {
                    if (xDiff >= 0) {
//                        System.out.println("xDiff="+xDiff+" yDiff="+yDiff);

//                        System.out.println("----------------->EAST");

                        if (xCible == xIA + 1) {
                            toucheBombe(iTabPerso, 0);
                        } else {
                            eastPossible(tabElement, xIA, yIA, perso, iTabPerso, caseSafe);
                        }
                /*goEast*/
                    } else {
//                        System.out.println("----------------->WEST");
                        if (xCible == xIA - 1) {
                            toucheBombe(iTabPerso, 0);
                        } else {
                            westPossible(tabElement, xIA, yIA, perso, iTabPerso, caseSafe);
                        }
                /*goWest*/
                    }
                } else {
                    if (yDiff >= -2 && yDiff < 2) {
//                        System.out.println("----------------->NORTH");
                        if (yCible == yIA - 1) {
                            toucheBombe(iTabPerso, 0);
                        } else {
                            northPossible(tabElement, xIA, yIA, perso, iTabPerso, caseSafe);
                        }
                /*goNorth*/
                    } else if (yDiff <= 0){
                        if (yCible == yIA - 1) {
                            toucheBombe(iTabPerso, 0);
                        } else {
                            northPossible(tabElement, xIA, yIA, perso, iTabPerso, caseSafe);
                        }
                    } else {
//                        System.out.println("----------------->SOUTH");
                        if (yCible == yIA + 1) {
                            toucheBombe(iTabPerso, 0);
                        } else {
                            southPossible(tabElement, xIA, yIA, perso, iTabPerso, caseSafe);
                        }
                /*goSouth*/
                    }
                }
                if (perso.getNbBombeRestantes() > 0) verifPosFail(perso.getBlop(), iTabPerso, xDiff, yDiff);

            } else {
//                System.out.println("Jme Tiiiiiiiirrrrreeeeeeee");
                if(xIA%30==0 && yIA%30==0)initDirectionIA(iTabPerso);
                jmeTirrreeeeee(iTabPerso, perso, xCible, yCible);
            }
        }

    }

    /**
    * i : position de l'IA dans le tableau des personnages
    * direction :
    * N --> north | E --> east | S --> sud | W --> west
    */
    private void modifDirectionIA(int i,String direction){
        switch(direction){
            case "N" :
                goNorth[i]=true;
                goEast[i]=false;
                goSouth[i]=false;
                goWest[i]=false;
                break;
            case "E" :
                goNorth[i]=false;
                goEast[i]=true;
                goSouth[i]=false;
                goWest[i]=false;
                break;
            case "S" :
                goNorth[i]=false;
                goEast[i]=false;
                goSouth[i]=true;
                goWest[i]=false;
                break;
            case "W" :
                goNorth[i]=false;
                goEast[i]=false;
                goSouth[i]=false;
                goWest[i]=true;
                break;
        }

    }

    /**
     * 4 Méthodes qui vérifient si il est possible d'aller dans une direction (une méthode par direction)
     * */
    private boolean southPossible(Element[][] tabElement, int xIA, int yIA, Personnage_IA perso, int iTabPerso, int[][] caseSafe) {
        Element e = tabElement[xIA][yIA+1];
        if((e instanceof Mur) && ((Mur) e).isDestructible()) {
//            System.out.println("Je pose une bombe au SOUTH");
            toucheBombe(iTabPerso,0);
            return true;
        } else if (!(e instanceof Mur) && caseSafe[xIA][yIA+1]<100){
            modifDirectionIA(iTabPerso,"S");
            //System.out.println("goSOUTH");
            return true;
        } else {
            perso.setBlop("South");
            return false;
        }
    }


    private boolean northPossible(Element[][] tabElement, int xIA, int yIA, Personnage_IA perso, int iTabPerso, int[][] caseSafe) {
        Element e = tabElement[xIA][yIA-1];
        if((e instanceof Mur) && ((Mur) e).isDestructible()) {
            toucheBombe(iTabPerso,0);
//            System.out.println("Je pose une bombe au nord");
            return true;
        } else if (!(e instanceof Mur) && caseSafe[xIA][yIA-1]<100){
            modifDirectionIA(iTabPerso,"N");
            //System.out.println("goNORTH");
            return true;
        } else {
            perso.setBlop("North");
            return false;
        }
    }

    private boolean westPossible(Element[][] tabElement, int xIA, int yIA, Personnage_IA perso, int iTabPerso, int[][] caseSafe) {
        Element e = tabElement[xIA-1][yIA];
        if((e instanceof Mur) && ((Mur) e).isDestructible()) {
//            System.out.println("Je pose une bombe a WEST");
            toucheBombe(iTabPerso,0);
            return true;
        } else if (!(e instanceof Mur) && caseSafe[xIA-1][yIA]<100){
            modifDirectionIA(iTabPerso,"W");
            //System.out.println("goWEST");
            return true;
        } else {
            perso.setBlop("West");
            return false;
        }
    }

    private boolean eastPossible(Element[][] tabElement, int xIA,int yIA, Personnage_IA perso, int iTabPerso, int[][] caseSafe) {
        Element e = tabElement[xIA+1][yIA];
        if((e instanceof Mur) && ((Mur) e).isDestructible()) {
//            System.out.println("Je pose une bombe a EAST");
            toucheBombe(iTabPerso,0);
            return true;
        } else if (!(e instanceof Mur) && caseSafe[xIA+1][yIA]<100){
            //System.out.println("goEAST");
            modifDirectionIA(iTabPerso, "E");
            return true;
        } else {
            perso.setBlop("East");
            return false;
        }
    }


    /**
     * Méthode qui vérifie si l'IA peut aller là où elle veut aller
     * xDiff et yDiff : différence entre (respectivement) les x et y de l'enemi et de l'IA
     * iTabPerso : indice de position de l'IA dans le tableau des personnages (tabPerso)
     * blop : paramètre qui indique le besoin de vérification ou non.
     */
    private void verifPosFail(String blop, int iTabPerso, int xDiff, int yDiff) {
        Element[][] tabElement = model.getPlateau().getTabElement();
        Personnage_IA perso = (Personnage_IA) model.getTabPerso()[iTabPerso];
        int xIA = (perso.getPosX())/30;
        int yIA = (perso.getPosY())/30;
        int[][] caseSafe = rechercheCaseSafe();


        blop = perso.getBlop();


        switch (blop){

            case "passer":
                break;

            default :
                Element e = tabElement[xIA][yIA-1];
                if (perso.getNbBombeRestantes()!=0){

                    if(!(e instanceof Mur)){
                        modifDirectionIA(iTabPerso,"N");
                        //System.out.println("##############solution -->NORTH");
                        break;
                    } else {
                        e = tabElement[xIA+1][yIA];
                        if(!(e instanceof Mur)){
                            modifDirectionIA(iTabPerso,"E");
                            //System.out.println("##############solution -->EAST");
                            break;
                        } else {
                            e = tabElement[xIA][yIA+1];
                            if(!(e instanceof Mur)){
                                modifDirectionIA(iTabPerso,"S");
//                                System.out.println("##############solution -->SOUTH");
                                break;
                            } else {
                                e = tabElement[xIA-1][yIA];
                                if(!(e instanceof Mur)){
                                    modifDirectionIA(iTabPerso,"W");
//                                    System.out.println("##############solution -->WEST");
                                    break;
                                }
                            }
                        }
                    }
                }

                perso.setBlop("passer");
        }
    }

    /**
     * Méthode qui retourne le tableau de jeu suivant le danger present sur la case.
     * Une "case peut prendre plusieur valeur plus elle est élevé plus elle est dangereuse.
     * valeur case = 100 --> mur cassable/incassable
     * valeur case < 100 --> rayon d'explosion d'une bombe
     */
    private int[][] rechercheCaseSafe() {
        int[][] posSafe = new int[21][21];
        int[] rayonExplo;
        int posXbombe;
        int posYbombe;
        int xExplo=0;
        int yExplo=0;

        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 21; y++) {
                Element e = model.getPlateau().getTabElement()[x][y];

                if(e == null || e instanceof Effet){
                    posSafe[x][y] = 0;
                } else {
                    posSafe[x][y] = 100;
                }
            }
        }

        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 21; y++) {
                Element e = model.getPlateau().getTabElement()[x][y];

                if (e instanceof Bombe) {
                    posXbombe = e.getPosX();
                    posYbombe = e.getPosY();
                    rayonExplo = ((Bombe) e).explosion(model.getPlateau());

                    for (int i = 0; i < rayonExplo.length; i++) {
                        //System.out.println("rayon explo : "+rayonExplo[i]);
                        xExplo = posXbombe;
                        yExplo = posYbombe;
                        for (int j = 1; j < rayonExplo[i]; j++) {
                            switch (i) {
                                case 0:
                                    yExplo--;
                                    break;
                                case 1:
                                    xExplo++;
                                    break;
                                case 2:
                                    yExplo++;
                                    break;
                                case 3:
                                    xExplo--;
                                    break;
                            }
                            posSafe[xExplo][yExplo] += rayonExplo[i] - j;
                        }
                    }
                }
            }
        }
        return posSafe;
    }

    /**
    * Méthode qui fait fuir l'IA vers une position "safe"
    * xCible et yCible :position de l'enemie
    * iTabPerso : indice de position de l'IA dans le tableau des personnages (tabPerso)
    */
    private void jmeTirrreeeeee(int iTabPerso, Personnage_IA perso, int xCible, int yCible) {
        int[][] caseSafe = rechercheCaseSafe();
        int xIA = (perso.getPosX())/30;
        int yIA = (perso.getPosY())/30;

        if(caseSafe[xIA][yIA] != 0){

            int[] caseAutourPerso = {caseSafe[xIA+1][yIA], caseSafe[xIA-1][yIA],
                    caseSafe[xIA][yIA-1], caseSafe[xIA][yIA+1]};
            int casePlusSafe = 1000;
            int choix = 0;

            //printTabCaseSafe();

            for (int i =0; i<4; i++){
                if(caseAutourPerso[i] < casePlusSafe){
                    switch (i) {
                        case 0:
                            if (xCible == xIA + 1) {
                                break;
                            } else {
                                casePlusSafe = caseAutourPerso[i];
                                choix = i;
                                break;
                            }
                        case 1:
                            if (xCible == xIA - 1) {
                                break;
                            } else {
                                casePlusSafe = caseAutourPerso[i];
                                choix = i;
                                break;
                            }
                        case 2:
                            if (yCible == yIA - 1) {
                                break;
                            } else {
                                casePlusSafe = caseAutourPerso[i];
                                choix = i;
                                break;
                            }
                        case 3:
                            if (yCible == yIA + 1) {
                                break;
                            } else {
                                casePlusSafe = caseAutourPerso[i];
                                choix = i;
                                break;
                            }
                    }
                }
            }


            switch (choix){
                case 0 :
//                    System.out.println("jme tire : goEAST");
                    modifDirectionIA(iTabPerso,"E");
                    break;
                case 1 :
//                    System.out.println("jme tire : goWEST");
                    modifDirectionIA(iTabPerso,"W");
                    break;
                case 2 :
//                    System.out.println("jme tire : goNORTH");
                    modifDirectionIA(iTabPerso,"N");
                    break;
                case 3 :
//                    System.out.println("jme tire : goSOUTH");
                    modifDirectionIA(iTabPerso,"S");
                    break;
            }
        }
    }
    /**
     * Tous est dans son nom
     */
    private void printTabCaseSafe() {
        int[][] tabCase =  rechercheCaseSafe();
        for (int x = 0; x < 21; x++) {
            for (int y = 0; y < 21; y++) {
                System.out.print(tabCase[y][x]+" ");
            }
            System.out.println();
        }
        System.out.println("\n--------------------------------");

    }

    /**
     * Métode qui fait faire demi-tour à l'IA
     */
    private void moveInverse(Personnage_IA perso, int i) {
        int lastDirection = perso.getDirection();
        //System.out.println("Dernière direction : "+lastDirection);

        switch (lastDirection) {
            case 0 :
                goSouth[i]=true;
                perso.setDirection(2);
                break;
            case 1 :
                goWest[i]=true;
                perso.setDirection(3);
                break;
            case 2 :
                goNorth[i]=true;
                perso.setDirection(0);
                break;
            case 3 :
                goEast[i]=true;
                perso.setDirection(1);
                break;
        }
    }


    private void afficheTabDirectionIA() {
        for (int i = 1; i <goNorth.length ; i++) {
            System.out.println(" ---------------------------------------");
            System.out.println("| i = "+i+"                                |");
            System.out.println("| N:"+goNorth[i]+" . E:"+goEast[i]+" . S:"+goSouth[i]+" . W:"+goWest[i]+" |");
            System.out.println(" ---------------------------------------\n\n");
        }
    }


    /******************** Getters/Setters ******************************************************/
    public View getView() {
        return view;
    }

    public boolean[] getGoNorth() {
        return goNorth;
    }

    public boolean[] getGoEast() {
        return goEast;
    }

    public boolean[] getGoSouth() {
        return goSouth;
    }

    public boolean[] getGoWest() {
        return goWest;
    }

    public void setGoNorthI(int indice, boolean var) {
        goNorth[indice] = var;
    }
    public void setGoEastI(int indice, boolean var) {
        goEast[indice] = var;
    }
    public void setGoSouthI(int indice, boolean var) {
        goSouth[indice] = var;
    }
    public void setGoWestI(int indice, boolean var) {
        goWest[indice] = var;
    }
}