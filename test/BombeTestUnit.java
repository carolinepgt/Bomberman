import org.junit.Assert;
import org.junit.Test;

public class BombeTestUnit {

    @Test
    public void testExplosion(){
        Plateau p=new Plateau();
        Personnage perso=new Personnage(0,0,"",0);
        Bombe b = new Bombe(perso,2,1);

        int[] rayonExplosionATest=b.explosion(p);
        int[] oracleRayonExplosion=new int[] {1,2,1,2};

        Assert.assertArrayEquals(rayonExplosionATest, oracleRayonExplosion);


        p.getTabElement()[1][3]=null;
        p.getTabElement()[1][4]=null;
        p.getTabElement()[1][5]=null;
        perso.setPortee(3); // test avec une portée différente
        b = new Bombe(perso,1,3);

        rayonExplosionATest=b.explosion(p);
        oracleRayonExplosion=new int[] {3,2,4,1};

        Assert.assertArrayEquals(rayonExplosionATest, oracleRayonExplosion);
    }
}
