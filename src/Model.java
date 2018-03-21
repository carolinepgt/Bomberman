
public class Model {

    private Personnage[] tabPerso;
    private Fantome[] tabFantome;
    private Fantopac fantopac;
    private Effet[] tabBonusPacman;
    private Plateau plateau;
    int nbsJoueurs;
    private boolean partiePacman;



    public Model(int nbJoueurs) {
        this.nbsJoueurs = nbJoueurs;
        if(nbJoueurs==1){
            tabPerso=new Personnage[nbJoueurs+1];
            tabPerso[1]= new Personnage_IA(570,570,"Orange",0,1);
//            tabPerso[1]=new Personnage(60,570, "Orange", 3);
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
        fantopac = new Fantopac(270,270, "trump");
        partiePacman = false;
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

    public Effet[] getTabBonusPacman() {
        return tabBonusPacman;
    }

    public void setTabBonusPacman(Effet[] tabBonusPacman) {
        this.tabBonusPacman = tabBonusPacman;
    }

    public Fantopac getFantopac() {
        return fantopac;
    }

    public void setPartiePacman(boolean partiePacman) {
        this.partiePacman = partiePacman;
    }

    public boolean isPartiePacman() {
        return partiePacman;
    }

    public Fantome[] getTabFantome() {
        return tabFantome;
    }

    public void setTabFantome(Fantome[] tabFantom) {
        this.tabFantome = tabFantom;
    }
}
