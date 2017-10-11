import java.util.Random;

public class Effet extends Element {
    int typeEffet;

    /*
    0 : sans effet;

    1 : Bonus portee;
    2 : Bonus nbBombe;
    3 : Bonus vie;
    4 : Bonus vitesse;

    5 : Malus portee;
    6 : Malus nbBombe;
    7 : Malus vie;
    8 : Malus vitesse;
     */

    public Effet(int posX, int posY) {
        super( "",posX, posY);
        typeEffetCreation();
    }

    public void typeEffetCreation(){
        Random random=new Random();
        this.typeEffet=random.nextInt(4)+1;
        String url="";
        System.out.println(typeEffet);
        switch (typeEffet) {
            case 1: url="img/bonusPortee.jpg"; break;
            case 2: url="img/bonusNbBombeRestantes.jpg"; break;
            case 3: url="img/bonusVie.png"; break;
            case 4: url="img/bonusVitesse.jpg"; break;
        }
        super.setImageURL(url);
    }

    public void appliqueEffet(Personnage perso) {
        switch (typeEffet) {
            case 1: perso.setPortee(perso.getPortee()+1); break;
            case 2: perso.setNbBombeRestantes(perso.getNbBombeRestantes()+1); break;
            case 3: perso.setVie(perso.getVie()+1); break;
            case 4: perso.setVitesse(perso.getVitesse()+2); break;
        }
    }
}
