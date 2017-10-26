
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
                }else if(!(x<=2 && y<=2) && !(x>=18 && y>=18) && (x%2!=0 || y%2!=0) ){
                    tabElement[x][y]=new Mur(true,x,y);
                }
            }
        }

    }

    public Element[][] getTabElement() {
        return tabElement;
    }

    public void setElement(Element element, int x, int y){
        tabElement[x][y]=element;
    }

    public void afficherMap1(){
        tabElement = new Element[21][21];
        for (int i = 0;i<13;i++){
            for (int j = 0;j<13;j++){
                if (i==0 || j==0 || i==12 || j==12){
                    tabElement[i][j] = new Mur(false,i,j);
                }else if (i == 6 && j == 6){
                    tabElement[i][j] = new Mur(false,i,j);
                }else if((i >= 2 && i<=4 && j == 2) || (i >= 2 && i<=4 && j == 10) || (i >= 8 && i<=10 && j == 2) || (i >= 8&& i<=10 && j == 10)){
                    tabElement[i][j] = new Mur(false,i,j);
                }else if (((i == 2 || i == 10) && (j == 3 || j == 4)) || ((i == 2 || i == 10) && (j == 8 || j == 9))){
                    tabElement[i][j] = new Mur(false,i,j);
                }else if (((i == 4 || i == 8) && (j == 4 || j == 5 ||j == 7 || j == 8))){
                    tabElement[i][j] = new Mur(false,i,j);
                }else if ((i == 6 && (j == 2 || j == 10)) || (j == 6 && (i == 2 || i == 10))){
                    tabElement[i][j] = new Mur(false,i,j);
                }else if ((i == 5 && (j == 4 || j == 8)) || (i == 7 && (j == 4 || j == 8))){
                    tabElement[i][j] = new Mur(false,i,j);
                }else if (((i == 1 || i == 11 || i == 3 || i == 9) && (j >=3 && j<=9)) || ((j == 1 || j == 11) && (i >=3 && i<=9))){
                    tabElement[i][j] = new Mur(true,i,j);
                }else if((j == 3 || j == 9) && (i >= 4 && i <= 8)){
                    tabElement[i][j] = new Mur(true,i,j);
                }else if((j == 5 || j == 7) && (i >= 5 && i <= 7)){
                    tabElement[i][j] = new Mur(true,i,j);
                }else if((i == 5 || i == 7) && (j == 6)){
                    tabElement[i][j] = new Mur(true,i,j);
                }
            }
        }
    }

    public void afficherMap2() {
        tabElement = new Element[21][21];
        for (int i = 0;i<13;i++) {
            for (int j = 0; j < 13; j++) {
                if (i==0 || j==0 || i==12 || j==12){
                    tabElement[i][j] = new Mur(false,i,j);
                }
                if ((i >= 2 && i <= 5 && j >= 2 && j <= 5) || (i >= 7 && i <= 10 && j >= 7 && j <= 10)){
                    if (i == j){
                        tabElement[i][j] = new Mur(false,i,j);
                    }
                }
                if ((i == 2 || i == 10) && (j % 2 == 0)){
                    tabElement[i][j] = new Mur(false,i,j);
                }
                if ((j == 2 || j == 10) && (i % 2 == 0)){
                    tabElement[i][j] = new Mur(false,i,j);
                }
                if ((i == 4 && j == 2)){
                    tabElement[i][j] = new Mur(false,i,j);
                }
                if ((i == 6 && (j == 3 || j == 9)) || (j == 6 && (i == 3 || i == 9))){
                    tabElement[i][j] = new Mur(false,i,j);
                }
                if ((i == 9 && j == 3) || (i == 8 && j == 4) ||(i == 7 && j == 5) || (i == 5 && j == 7) || (i == 4 && j == 8) || (i == 3 && j == 9)){
                    tabElement[i][j] = new Mur(false,i,j);
                }
                if(i == 6 && j == 6){
                    tabElement[i][j] = new Mur(true,i,j);
                }
                if (((i == 1 || i == 11) && (j >=3 && j<=9)) || ((j == 1 || j == 11) && (i >=3 && i<=9))){
                    tabElement[i][j] = new Mur(true,i,j);
                }
                if((i >= 2 && i <= 4) && (j ==5 || j == 7) || (i >= 8 && i <= 10) && (j ==5 || j == 7)){
                    tabElement[i][j] = new Mur(true,i,j);
                }
                if ((j >= 2 && j <= 4) && (i ==5 || i == 7) || (j >= 8 && j <= 10) && (i ==5 || i == 7)){
                    tabElement[i][j] = new Mur(true,i,j);
                }
                if ((i == 6 && (j == 4 || j== 8)) || (j == 6 && (i == 4 || i == 8))){
                    tabElement[i][j] = new Mur(true,i,j);
                }
            }
        }
    }
    public void afficherMap3(){
        tabElement = new Element[21][21];
        for (int i = 0;i<13;i++) {
            for (int j = 0; j < 13; j++) {
                if (i == 0 || j == 0 || i == 12 || j == 12) {
                    tabElement[i][j] = new Mur(false, i, j);
                }
                if (((i == 2 || i == 10) && j%2 ==0) || ((j == 2 || j == 10) && i % 2 == 0)){
                    tabElement[i][j] = new Mur(false, i, j);
                }
                if ((i == 2 && j == 7) || (i == 7 && j == 2) ||(i == 6 && j == 3) || (i == 3 && j == 8) || (i == 4 && j == 3) || (i == 8 && j == 9) || (i == 4 && j == 6)){
                    tabElement[i][j] = new Mur(false, i, j);
                }
                if ((i == 6 && (j>=5 && j<= 10)) || (j == 8 && (i>=5 && i<= 10))){
                    tabElement[i][j] = new Mur(false, i, j);
                }
                if ((j == 5 || j == 10) && (i>=4 && i<= 6)){
                    tabElement[i][j] = new Mur(false, i, j);
                }
                if ((i == 8 && (j>=4 && j<=6)) || (j==6 && (i>=9 && i<=10))){
                    tabElement[i][j] = new Mur(false, i, j);
                }
                if (((i == 1 || i == 11) && (j >=3 && j<=9)) || ((j == 1 || j == 11) && (i >=3 && i<=9))){
                    tabElement[i][j] = new Mur(true,i,j);
                }
                if ((i>=7 && i<=10) && (j == 3 || j == 7)){
                    tabElement[i][j] = new Mur(true,i,j);
                }
                if ((i == 3 && (j>=2 && j<= 7)) || (j == 4 && (i>=3 && i<= 7))){
                    tabElement[i][j] = new Mur(true,i,j);
                }
                if ((j == 9 && (i>=2 && i<=4)) || (i ==9 && ((j>=4 && j<= 5) || (j == 10)))){
                    tabElement[i][j] = new Mur(true,i,j);
                }
                if ((i == 4 && (j==7 || j==8)) || (i==5 && (j==2 || j==3 || j== 7 )) || (i == 7 && (j ==5 || j == 6 || j==9 || j==10))){
                    tabElement[i][j] = new Mur(true,i,j);
                }
                if ((i == 2 && (j == 3 || j==5)) || (i==3&&j==10) || (i ==9 && j == 2) || (i==10 && (j==9 || j== 5))){
                    tabElement[i][j] = new Mur(true,i,j);
                }
            }
        }
    }

}
