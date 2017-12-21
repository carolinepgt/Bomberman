import org.junit.Assert;
import org.junit.Test;


public class EffetTestUnit {

    @Test
    public void testEffetPortee(){
        Personnage perso=new Personnage(0,0,"",0);
        Effet e = new Effet(1,0,0);
        int portee=perso.getPortee();
        e.appliqueEffet(perso);

        Assert.assertEquals(portee+1, perso.getPortee());

        perso.setPortee(6);
        e.appliqueEffet(perso);
        Assert.assertEquals(6, perso.getPortee());

    }

    @Test
    public void testEffetBombe(){
        Personnage perso=new Personnage(0,0,"",0);
        Effet e = new Effet(2,0,0);
        int bombe=perso.getNbBombeRestantes();
        e.appliqueEffet(perso);

        Assert.assertEquals(bombe+1, perso.getNbBombeRestantes());


    }


    @Test
    public void testEffetVie(){
        Personnage perso=new Personnage(0,0,"",0);
        Effet e = new Effet(3,0,0);
        int vie=perso.getVie();
        e.appliqueEffet(perso);

        Assert.assertEquals(vie+1, perso.getVie());

        perso.setVie(3);
        e.appliqueEffet(perso);
        Assert.assertEquals(3, perso.getVie());

    }

    @Test
    public void testEffetVitesse(){
        Personnage perso=new Personnage(0,0,"",0);
        Effet e = new Effet(4,0,0);
        int vitesse=perso.getVitesse();
        e.appliqueEffet(perso);

        Assert.assertEquals(vitesse+1, perso.getVitesse());

        perso.setVie(4);
        e.appliqueEffet(perso);
        Assert.assertEquals(4, perso.getVitesse());

    }
}