
public class Mine extends Element {

    private int vieMine;

    public Mine(int posX, int posY){
        super("img2/mine.jpg",posX,posY);
        this.vieMine=1;
    }

    public void setVieMine(int vieMine){this.vieMine=vieMine;}

    public int getVieMine(){return vieMine;}

}
