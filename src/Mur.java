
public class Mur extends Element {

    private final boolean Destructible;

    public Mur(boolean Destructible, int x, int y) {
        super("img/mur.png",x,y);
        this.Destructible=Destructible;
    }

    public boolean isDestructible() {
        return Destructible;
    }
}
