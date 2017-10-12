
public class Plateau {
    private Element[][] tabElement;

    public Plateau() {
        tabElement = new Element[21][21];
        for (int x=0; x<21; x++){
            for (int y=0; y<21; y++){
                if (x==0 || y==0 || x==20 || y==20){
                    tabElement[x][y]=new Mur(false,x,y);
                }else if(x%2==0 && y%2==0){
                    tabElement[x][y]=new Mur(false,x,y);
                }else if(!(x<=2 && y<=2) && (x%2!=0 || y%2!=0) ){
                    tabElement[x][y]=new Mur(true,x,y);
                }
            }
        }
        //tabElement[3][4]=new Mur(true,3,4);

    }

    public Element[][] getTabElement() {
        return tabElement;
    }

    public void setElement(Element element, int x, int y){
        tabElement[x][y]=element;
    }


}
