import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Floor extends Platform {
    private Ceiling ceiling;
    private Wall wall1, wall2;
    
    public Floor() {
        super();
        ceiling = new Ceiling();
        wall1 = new Wall();
        wall2 = new Wall();
    }
    public Floor(int i1, int i2, int i3, int i4) {
        super(i1,i2,i3,i4);
        ceiling = new Ceiling(i1+1,i2+i4-2,i3-2,2);
        wall1 = new Wall(i1-4,i2+10,5,i4-10);
        wall2 = new Wall(i1+i3-1,i2+10,5,i4-10);
    }
    

    public Ceiling getCeiling() {
        return ceiling;
    }
    public Wall getWall1() {
        return wall1;
    }
    public Wall getWall2() {
        return wall2;
    }
    
    public void drawImage(Graphics2D g2d, String file) {
        Image image = new ImageIcon(getClass().getResource(file+".png")).getImage();
        g2d.drawImage(image,getX(),getY(),getW(),getH(),null);
    }
    
    
}