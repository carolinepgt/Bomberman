import org.junit.Assert;
import org.junit.Test;

public class SpikeBombeTestUnit {

    @Test
    public void testExplosion(){
        Plateau p=new Plateau();
        Personnage perso=new Personnage(0,0,"",0);
        SpikeBombe b = new SpikeBombe(perso,2,1);

        int[] rayonExplosionATest=b.explosion(p);
        int[] oracleRayonExplosion=new int[] {1,2,1,2};

        Assert.assertArrayEquals(rayonExplosionATest, oracleRayonExplosion);

        // on ne supprime pas les murs par rapport aux tests de la bombe normale

        perso.setPortee(3); // test avec une portée différente
        b = new SpikeBombe(perso,1,3);

        rayonExplosionATest=b.explosion(p);
        oracleRayonExplosion=new int[] {3,4,4,1};

        Assert.assertArrayEquals(rayonExplosionATest, oracleRayonExplosion);
    }
}
