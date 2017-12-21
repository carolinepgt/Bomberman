
public class Bombe extends Element {

    protected Personnage personnage;
    protected int portee;

    public Bombe(Personnage perso, int posX, int posY){
        super("img2/BombeNoel.png",posX,posY);
        this.personnage=perso;
        portee=perso.getPortee();
        perso.setNbBombeRestantes(perso.getNbBombeRestantes()-1);
    }

    public Personnage getPersonnage() {
        return personnage;
    }


    public int getPortee() {
        return portee;
    }

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
                mur = e!=null && e instanceof Mur;
            }

            if (mur && !((Mur)e).isDestructible()) porteeReel--;
            explosion[i]=porteeReel;
        }
        return explosion;
    }
}
