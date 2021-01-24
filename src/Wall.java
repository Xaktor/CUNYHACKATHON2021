import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Wall extends Platform {
    public Wall() {
        super();
    }
    public Wall(int i1, int i2, int i3, int i4) {
        super(i1,i2,i3,i4);
    }
    
    @Override
    public void drawSelf(Graphics2D g2d, int bX, int bY) {
        Image elevator = new ImageIcon(getClass().getResource("Solids/long-block-vertical-large.png")).getImage();
        g2d.drawImage(elevator,getX()+bX,getY()+bY,getW(),getH(),null);
    }
    public void drawImage(Graphics2D g2d, String file) {
        Image image = new ImageIcon(getClass().getResource(file+".png")).getImage();
        g2d.drawImage(image,getX(),getY(),getW(),getH(),null);
    }
}