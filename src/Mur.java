import java.util.Random;

public class Mur extends Element {

    private final boolean Destructible;
    private Effet effet;

    public Mur(boolean Destructible, int x, int y) {
        super(Destructible,x,y);
        Random r =new Random();
        if (r.nextInt(4)==0) {
            effet = new Effet(getPosX(), getPosY());
        }
        this.Destructible=Destructible;
    }

    public Mur(boolean destructible, int posX, int posY, int effet) {
        super(destructible, posX, posY);
        if (effet!=0) this.effet = new Effet(effet, getPosX(), getPosY());

        Destructible = destructible;
    }

    public Mur(boolean destructible, int posX, int posY, String url) {
        super(url, posX, posY);
        Destructible = destructible;
    }

    public boolean isDestructible() {
        return Destructible;
    }

    @Override //-
    public String toString() {
        if (!Destructible) return "MI";
        if (effet==null) return "M0";
        return "M"+effet.toString();
    }

    public Effet getEffet() {
        return effet;
    }
}
