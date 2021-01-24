import java.awt.Color;
import java.awt.Graphics2D;

public class Platform {
    private int x,y,w,h;
    
    public Platform() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
    }
    
    public Platform(int argX, int argY, int argW, int argH) {
        x = argX;
        y = argY;
        w = argW;
        h = argH;
    }
    
    public void drawSelf(Graphics2D g2d, int bX, int bY) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect(x+bX, y+bY, w, h);
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