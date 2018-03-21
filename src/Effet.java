import java.util.Random;

public class Effet extends Element {
    private int typeEffet;

    /*

    1 : Bonus portee;
    2 : Bonus nbBombe;
    3 : Bonus vie;
    4 : Bonus vitesse;
    5 : Bonus penetrator;

    Bonus partie Pacman
    6 : Bonus killFant --> pour tuer les fantomes

     */

    public Effet(int posX, int posY) {
        super( "",posX, posY);
        typeEffetCreation();
    }


    public Effet(int type, int posX, int posY){ //-
        super( "",posX, posY);
        typeEffet=type;
        modifieUrl();
    }

    public void typeEffetCreation(){
        Random random=new Random();
        this.typeEffet=random.nextInt(5)+1;
        modifieUrl();
    }

    public void modifieUrl(){ //-
        String url="";
        switch (typeEffet) {
            case 1: url="img2/portee30_30.png"; break;
            case 2: url="img2/bonusBombe30_30.png"; break;
            case 3: url="img2/vie30_30.png"; break;
            case 4: url="img2/sonic30_30.png"; break;
            case 5: url="img2/penetrator30_30.png"; break;
        }
        super.setImageURL(url);
    }

    public void appliqueEffet(Personnage perso) {
        switch (typeEffet) {
            case 1:  if (perso.getPortee()<6) perso.setPortee(perso.getPortee()+1); break;
            case 2: perso.setNbBombeRestantes(perso.getNbBombeRestantes()+1); break;
            case 3: if (perso.getVie()<3) perso.setVie(perso.getVie()+1); break;
            case 4: if (perso.getVitesse()<4) perso.setVitesse(perso.getVitesse()+1); break;
            case 5: perso.setNbSpikeBombe(perso.getNbSpikeBombe()+1); break;
        }
    }

    @Override //-
    public String toString() {
        return ""+typeEffet;
    }
}
