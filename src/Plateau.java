
public class Plateau {
    private Element[][] tabElement;

    public Plateau() {
        tabElement = new Element[10][10];
        for (int x=0; x<10; x++){
            for (int y=0; y<10; y++){
                if (x==0 || y==0 || x==9 || y==9){
                    tabElement[x][y]=new Mur(false,x,y);
                }
            }
        }
        tabElement[3][4]=new Mur(true,3,4);

        tabElement[2][3]=new Mur(true,2,3);
        tabElement[7][8]=new Mur(true,7,8);
        tabElement[3][7]=new Mur(true,3,7);

    }

    public Element[][] getTabElement() {
        return tabElement;
    }

    public void setElement(Element element, int x, int y){
        tabElement[x][y]=element;
    }


}
