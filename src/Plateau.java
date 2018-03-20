
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
                }else if(!(x<=8 && y<=2) && !(x>=18 && y>=18) && (x%2!=0 || y%2!=0) ){
                    tabElement[x][y]=new Mur(true,x,y);
                }
            }
        }

        tabElement[1][19]=null;
        tabElement[1][18]=null;
        tabElement[2][19]=null;
        tabElement[19][1]=null;
        tabElement[19][2]=null;
        tabElement[18][1]=null;

    }

    public Element[][] getTabElement() {
        return tabElement;
    }




}
