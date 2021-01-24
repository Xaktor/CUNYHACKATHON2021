import java.awt.Color;
import java.awt.Graphics2D;
public class Ceiling extends Platform{
    public Ceiling() {
        super();
    }
    public Ceiling(int i1, int i2, int i3, int i4) {
        super(i1,i2,i3,i4);
    }
    
    @Override
    public void drawSelf(Graphics2D g2d,int bX, int bY) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect(getX()+bX,getY()+bY,getW(),getH());
    }
}