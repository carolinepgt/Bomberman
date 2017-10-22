import java.util.Random;

public class Effet extends Element {
    int typeEffet;

    /*

    1 : Bonus portee;
    2 : Bonus nbBombe;
    3 : Bonus vie;
    4 : Bonus vitesse;
    5 : Bonus mine;

     */

    public Effet(int posX, int posY) {
        super( "img/effet.gif",posX, posY);
        typeEffetCreation();
    }

    public void typeEffetCreation(){
        Random random=new Random();
        this.typeEffet=random.nextInt(5)+1;
        String url="";
        switch (typeEffet) {
            case 1: url="img2/effet.gif"; break;
            case 2: url="img2/bonusNbBombeRestantes.jpg"; break;
            case 3: url="img2/bonusVie.png"; break;
            case 4: url="img2/bonusVitesse.jpg"; break;
            case 5: url="img2/bonusMine.png"; break;
        }
        super.setImageURL(url);
    }

    public void appliqueEffet(Personnage perso) {
        switch (typeEffet) {
            case 1:  if (perso.getPortee()<6) perso.setPortee(perso.getPortee()+1); break;
            case 2: perso.setNbBombeRestantes(perso.getNbBombeRestantes()+1); break;
            case 3: if (perso.getVie()<3) perso.setVie(perso.getVie()+1); break;
            case 4: if (perso.getVitesse()<4) perso.setVitesse(perso.getVitesse()+1); break;
            case 5: perso.setHaveMine(true); break;
            case 6:
        }
    }
}
