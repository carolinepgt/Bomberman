/**
 * Created by Guillaume on 11/11/2017.
 */
public class SpikeBombe extends  Bombe{
    public SpikeBombe(Personnage perso, int posX, int posY) {
        super(perso, posX, posY);
        //setImageURL("spikeBombe.jpg");
    }

    @Override
    public int[] explosion(Plateau plateau) {
        Element[][] elements = plateau.getTabElement();
        int[] explosion=new int[4];
        int x =getPosX();
        int y =getPosY();

        for (int i=0; i<4; i++){
            boolean mur = false;
            Element e=null;
            int porteeReel;
            for (porteeReel = 1; porteeReel <= portee && !mur; porteeReel++) {
                switch (i){
                    case 0: e = elements[x][y - porteeReel]; break;
                    case 1: e = elements[x + porteeReel][y]; break;
                    case 2: e = elements[x][y + porteeReel]; break;
                    case 3: e = elements[x - porteeReel][y]; break;
                }
                mur = e!=null && e instanceof Mur && !((Mur) e).isDestructible();
            }

            if (mur && !((Mur)e).isDestructible()) porteeReel--;
            explosion[i]=porteeReel;
        }
        return explosion;
    }
}
