import org.junit.Assert;
import org.junit.Test;

public class ModelTestUnit {

    @Test
    public void testFinPartie(){
        Model m=new Model(3);
        Assert.assertFalse(m.partieFini());

        m.getTabPerso()[1].setVie(0);
        Assert.assertFalse(m.partieFini());

        m.getTabPerso()[2].setVie(0);
        Assert.assertTrue(m.partieFini());
    }
}
