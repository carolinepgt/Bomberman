
public class Mur extends Element {

    private final boolean Destructible;

    public Mur(boolean Destructible, int x, int y) {
        super(Destructible,x,y);
        this.Destructible=Destructible;
    }

    public boolean isDestructible() {
        return Destructible;
    }
}
