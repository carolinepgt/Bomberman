
public class Element {
    private String imageURL;
    private int posX;
    private int posY;

    public Element(String imageURL, int posX, int posY) {
        this.imageURL = imageURL;
        this.posX=posX;
        this.posY=posY;
    }
    public Element(Boolean murDestructible, int posX, int posY) {
        if(murDestructible){
            this.imageURL = "img2/MurCassable.jpg";
        } else {
            this.imageURL = "img2/Mur2.png";
        }
        this.posX=posX;
        this.posY=posY;
    }



    public String getImageURL() {
        return imageURL;
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

}
