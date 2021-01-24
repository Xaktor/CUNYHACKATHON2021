import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Laser {
    private int x,y,w,h;
    private long animTime;
    private String name;
    private int skip;
    private boolean isBlue;
    
    public Laser() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
        skip = 0;
    }
    
    public Laser(int argX, int argY, boolean isHorizontal, boolean isBlueArg) {
        x = argX;
        y = argY;
        isBlue = isBlueArg;
        skip = 0;
        
        name = "Solids/electricity-";
        if(isBlue) name+="blue-";
        else name+="red-";
        if(isHorizontal) name+="horizontal-";
        
        if(isHorizontal) {
            w = 48; //16
            h = 18; //6
        }
        else {
            w = 18;
            h = 48;
        }
    }
    
    public boolean isActive() {
        return isBlue || (!isBlue && skip<=2);
    }
    
    public void drawSelf(Graphics2D g2d, long currentTime, int bX, int bY) {
        if(isActive()) {
            int cycle = (int)((currentTime-animTime-1)/80)+1;
            if(animTime + 80*3 < currentTime) {
                animTime = currentTime;
                cycle = 1;
                skip++;
            }
            drawImage(g2d,name+cycle,bX,bY);
        }
        else {
            if(animTime + 80*3 < currentTime) {
                animTime = currentTime;
                skip++;
                if(skip==7)
                    skip = 0;
            }
        }
        
    }
    
    public void drawImage(Graphics2D g2d, String file, int bX, int bY) {
        Image image = new ImageIcon(getClass().getResource(file+".png")).getImage();
        g2d.drawImage(image,x+bX,y+bY,w,h,null);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getW() {
        return w;
    }
    public int getH() {
        return h;
    }
    
    
    
}