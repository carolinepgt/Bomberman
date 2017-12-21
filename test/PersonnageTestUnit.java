import org.junit.Assert;
import org.junit.Test;

public class PersonnageTestUnit {

    @Test
    public void testPoseBombe(){
        Element[][] e=new Plateau().getTabElement();
        Personnage p= new Personnage(0,0,"",0);
        // Poser une bombe sur case pleine
        Assert.assertTrue(p.poseBombe(e,0)== null);

        // Poser une bombe sur case vide
        p.setPosX("30");
        p.setPosY("30");
        int nbBombe=p.getNbBombeRestantes();
        Bombe b=p.poseBombe(e,0);
        Assert.assertEquals(b, e[1][1]);
        Assert.assertEquals(nbBombe-1, p.getNbBombeRestantes());

        // Poser une bombe sans en avoir en stock
        p.setPosX("60");
        Assert.assertTrue(p.poseBombe(e,0)== null);
        p.setNbBombeRestantes(5);


        // poser une spikeBombe
        b=p.poseBombe(e,1);
        Assert.assertEquals(b, e[2][1]);
        Assert.assertTrue(b instanceof SpikeBombe);

    }

    @Test
    public void testDeplacement(){
        Plateau pl=new Plateau();
        Personnage p= new Personnage(30,30,"",0);

        int x=p.getPosX();
        int y=p.getPosY();
        int v=p.getVitesse();

        // tentative de deplacement vers case pleine (ici au nord ou à l'ouest de la position de base du j1)
        p.actualisePosition(pl, true,false,false,true);

        Assert.assertEquals(x,p.getPosX());
        Assert.assertEquals(y,p.getPosY());

        // tentative de déplacement vers l'est (case vide)

        p.actualisePosition(pl, false,true,false,false);

        Assert.assertEquals(x+v,p.getPosX());
        Assert.assertEquals(y,p.getPosY());

        p.actualisePosition(pl,false,false,false,true); // retour pos de base

        // deplacement avec plus de vitesse
        p.setVitesse(10);
        p.actualisePosition(pl, false,false,true,false);

        Assert.assertEquals(x,p.getPosX());
        Assert.assertEquals(y+10,p.getPosY());

        // test avec plus de vitesse que la distance vers le mur
        p.setVitesse(15);
        p.actualisePosition(pl, true,false,false,false);

        Assert.assertEquals(x,p.getPosX());
        Assert.assertEquals(y,p.getPosY());
        // retour sur l'emplacement de base alors qu'il était seulement a 10 de distances et non 15

    }
}
