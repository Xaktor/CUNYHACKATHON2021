
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Decoration {
    int x,y,w,h;
    String name;
    
    public Decoration() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
    }
    
    public Decoration(String argName, int argW, int argH, int mapW, int mapH, int mapT) {
        name = argName;
        w = argW;
        h = argH;
        x = (int)(Math.random()*(mapW-w));
        y = (int)(Math.random()*(mapH-h))+mapT;
    }
    
    public void drawSelf(Graphics2D g2d, int bX, int bY) {
        Image dec = new ImageIcon(getClass().getResource("Decorations/"+name+".png")).getImage();
        g2d.drawImage(dec,x+bX,y+bY,w,h,null);
    }
    
    
}