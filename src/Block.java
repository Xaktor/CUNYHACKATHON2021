
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Block {
    private String name;
    private int x,y,w,h;
    
    public Block() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
    }
    
    public Block(String argName, int argX, int argY, int argW, int argH) {
        name = argName;
        x = argX;
        y = argY;
        w = argW;
        h = argH;
    }
    
    public void drawSelf(Graphics2D g2d, int bX, int bY) {
        Image dec = new ImageIcon(getClass().getResource("Solids/"+name+".png")).getImage();
        g2d.drawImage(dec,x+bX,y+bY,w,h,null);
    }
}