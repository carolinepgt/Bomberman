
public class Model {

    private Personnage[] tabPerso;
    private Plateau plateau;

    public Model(int nbJoueurs) {
        if(nbJoueurs==1){
            tabPerso=new Personnage[nbJoueurs+1];
            tabPerso[1]= new Personnage_IA(570,570,"Orange",0,1);
        } else {
            tabPerso=new Personnage[nbJoueurs];
        }
        switch (nbJoueurs){
            case 4:
                tabPerso[3]=new Personnage(30,570, "Orange", 3);
            case 3:
                tabPerso[2]=new Personnage(570,30, "Bleu", 2);
            case 2:
                tabPerso[1]=new Personnage(570,570, "Orange", 1);
            case 1:
                tabPerso[0]=new Personnage(30,30, "Bleu", 0);
        }


        plateau=new Plateau();
    }

    public Personnage[] getTabPerso() {
        return tabPerso;
    }

    public Plateau getPlateau() {
        return plateau;
    }



    public boolean partieFini(){
        int nbJoueurEnVie=0;
        for (Personnage perso : tabPerso){
            if (perso.estEnVie())nbJoueurEnVie++;
        }
        return nbJoueurEnVie <= 1;
    }
}
