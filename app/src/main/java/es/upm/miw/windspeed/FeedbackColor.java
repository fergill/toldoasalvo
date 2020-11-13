package es.upm.miw.windspeed;

public class FeedbackColor {

    private int R;
    private int G;
    private int B;


    public FeedbackColor(int r, int g, int b) {
        super();
        R = r;
        G = g;
        B = b;
    }

    /**
     * @return the r
     */
    public int getR() {
        return R;
    }
    /**
     * @param r the r to set
     */
    public void setR(int r) {
        R = r;
    }
    /**
     * @return the g
     */
    public int getG() {
        return G;
    }
    /**
     * @param g the g to set
     */
    public void setG(int g) {
        G = g;
    }
    /**
     * @return the b
     */
    public int getB() {
        return B;
    }
    /**
     * @param b the b to set
     */
    public void setB(int b) {
        B = b;
    }


    public String getHexR(){
        if (Integer.toHexString(R).length()==1){
            return "0"+Integer.toHexString(R);
        }
        return Integer.toHexString(R);
    }
    public String getHexG(){
        if (Integer.toHexString(G).length()==1){
            return "0"+Integer.toHexString(G);
        }
        return Integer.toHexString(G);
    }
    public String getHexB(){
        if (Integer.toHexString(B).length()==1){
            return "0"+Integer.toHexString(B);
        }
        return Integer.toHexString(B);
    }

    public String getHexColor(){
        return "#"+getHexR()+getHexG()+getHexB();
    }


}