
public class Model {

    private Personnage[] tabPerso;
    private Plateau plateau;

    public Model() {
        tabPerso=new Personnage[2];
        tabPerso[0]=new Personnage(30,30, "Bleu");
        tabPerso[1]=new Personnage(570,570, "Orange");
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
        if (nbJoueurEnVie<=1) return true;
        return false;
    }
}
