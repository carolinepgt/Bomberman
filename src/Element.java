
public class Element {
    static int size =30;
    private String imageURL;
    private String imageURLNoel;
    private int posX;
    private int posY;

    public Element(String imageURL, int posX, int posY) {
        this.imageURL = imageURL;
        this.posX=posX;
        this.posY=posY;
    }
    public Element(Boolean murDestructible, int posX, int posY) {
        if(murDestructible){
            this.imageURL = "img2/GlaceCassable.png";
        } else {
            this.imageURL = "img2/GlaceIncassable.png";
        }
        this.posX=posX;
        this.posY=posY;
    }



    public String getImageURL() {
        return imageURL;
    }
    public String getImageURLNoel() {
        return imageURLNoel;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isBombe(){
        return false;
    }

}
