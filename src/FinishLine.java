import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class FinishLine extends Platform {
    
    public FinishLine() {
        super();
    }
    
    public FinishLine(int argX, int argY) {
        super(argX, argY, 192, 128); //48x32
    }
    
    @Override
    public void drawSelf(Graphics2D g2d, int bX, int bY) {
        Image elevator = new ImageIcon(getClass().getResource("Solids/elevator.png")).getImage();
        g2d.drawImage(elevator,getX()+bX,getY()+bY,getW(),getH(),null);
    }
}