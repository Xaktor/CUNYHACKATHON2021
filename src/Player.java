import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Player {
    private int x,y,w,h,mobility,idleSpeed,moveSpeed,moveFrames;
    private int jumpStrength,velocityY,initialVelocityY;
    private char mode; //b = basic (yellow), s = small, c = crawler (spider), f = flying
    private boolean up,down,left,right,facingRight,airborne;
    private long animTime,jumpTime;
    private String name;
    
    public Player() {
        x = 100;
        y = 300;
        mode = 'b';
        createRobot();
        up = false;
        down = false;
        left = false;
        right = false;
        facingRight = true;
        airborne = false;
        velocityY = 0;
        initialVelocityY = 0;
        animTime = 0;
        jumpTime = 0;
    }
    
    public Player(char modeArg) {
        x = 100;
        y = 300;
        w = 64;
        h = 128;
        mode = modeArg;
        createRobot();
        up = false;
        down = false;
        left = false;
        right = false;
        facingRight = true;
        animTime = 0;
    }
    
    public void drawSelf(Graphics2D g2d, long currentTime) {
        String file = "Robots/robot-"+name+"-";
        
        if(left || right) {
            file+="move-";
            int cycle = (int)((currentTime-animTime-1)/moveSpeed)+1;
            if(animTime + moveFrames*moveSpeed < currentTime) {
                animTime = currentTime;
                cycle = 1;
            }
            file+=cycle;
        }
        else {
            file+="idle-";
            int cycle = (int)((currentTime-animTime-1)/idleSpeed)+1;
            if(animTime + 4*idleSpeed < currentTime) {
                animTime = currentTime;
                cycle = 1;
            }
            file+=cycle;
        }
        
        if(!facingRight)
            file+="-left";
        
        drawImage(g2d,file);
    }
    
    public void drawImage(Graphics2D g2d, String file) {
        Image image = new ImageIcon(getClass().getResource(file+".png")).getImage();
        g2d.drawImage(image,x,y,w,h,null);
    }
    
    public boolean overlaps(Platform p, int bX, int bY) {
        if(w>0 && x<=p.getX()+p.getW()+bX && x+w>=p.getX()+bX) { //x
            if(y<=p.getY()+p.getH()+bY && y+h>=p.getY()+bY) //y
                return true;
            }
        return false;
    }
    
    public boolean overlaps(Laser p, int bX, int bY) {
        if(w>0 && x<=p.getX()+p.getW()+bX && x+w>=p.getX()+bX) { //x
            if(y<=p.getY()+p.getH()+bY && y+h>=p.getY()+bY) //y
                return true;
            }
        return false;
    }
    
    public boolean overlapsX(Platform p, int bX, int bY) {
        return x<=p.getX()+p.getW()+bX && x+w>=p.getX()+bX;
    }
    public boolean overlapsY(Platform p, int bX, int bY) {
        return y<=p.getY()+p.getH()+bY && y+h>=p.getY()+bY;
    }
    public void fall(long currentTime) {
        airborne = true;
        initialVelocityY = 0;
        velocityY = 0;
        jumpTime = currentTime;
    }
    
    public void jump(long currentTime) {
        airborne = true;
        initialVelocityY = jumpStrength;
        velocityY = initialVelocityY;
        y+=velocityY;
        jumpTime = currentTime;
    }
    
    public void land(Platform p, int bY) {
        airborne = false;
        initialVelocityY = 0;
        velocityY = 0;
        y = p.getY()-h+bY;
    }
    public void act(ArrayList<Floor> floors, ArrayList<Wall> walls, ArrayList<Ceiling> ceilings, long currentTime, int bX, int bY) {
        for(int i=0; i<ceilings.size();i++){
            Ceiling curr = ceilings.get(i);
            if(overlaps(curr,bX,bY)){
                y = curr.getY()+curr.getH()+bY+1;
                fall(currentTime);
            }
        }
        
        for(int i=0; i<walls.size(); i++) {
            Wall curr = walls.get(i);
            if(overlaps(curr,bX,bY))
            {
                if(right)
                    x = curr.getX()-w-1+bX;
                else
                    x = curr.getX()+curr.getW()+1+bX;
                
            }
        }
        
        for(int i=0; i<floors.size(); i++) {
        Floor curr = floors.get(i);
            if(overlaps(curr,bX,bY)) {
                land(curr,bY);
            }
        }
        
        if(!airborne) {
            if(up)
                jump(currentTime);
            else if(!onTheFloor(floors,bX,bY)) {
                fall(currentTime);
            }
        }
    }
    
    public void moveVertical() {
        y += velocityY;
    }
    
    public void updateVelocityY(long currentTime) {
        double time = (currentTime - jumpTime) / 100;
        velocityY = (int) (time + initialVelocityY);
    }
    
    public boolean isDead(ArrayList<Laser> lasers, long currentTime,int bX, int bY, int screenHeight){
        /*for(int i=0; i<lasers.size(); i++) {
            Laser curr = lasers.get(i);
            if(curr.isActive() && overlaps(curr,bX,bY))
                return true;
        }*/
        
        if(y>screenHeight)
            return true;
        
        return false;
    }
    
    public void reset(long currentTime) {
        x = 100;
        y = 300;
        fall(currentTime);
    }

    public void createRobot() {
        if(mode=='b') {
            y-=28;
            name = "yellow";
            moveFrames = 8;
            mobility = 3;
            w = 56; //14
            h = 100; //25
            jumpStrength = -7;
        }
        else if(mode=='s') {
            y+=28;
            name = "small";
            moveFrames = 8;
            mobility = 5;
            //13x18
            w = 52;
            h = 72;
            jumpStrength = -5;
        }
        /*else if(mode=='c') {
            y-=56;
            name = "spider";
            moveFrames = 4;
            mobility = 2;
            w = 64;
            h = 128;
        }
        else if(mode=='f') {
            name = "flying";
            moveFrames = 8;
            mobility = 2;
            w = 64;
            h = 128;
        }*/
        
        idleSpeed = 150;
        moveSpeed = 130;
    }
    
    public boolean onTheFloor(ArrayList<Floor> floors, int bX, int bY) {
        for(int i=0; i<floors.size(); i++) {
            Floor curr = floors.get(i);
            if(curr.getY()==y+h && overlapsX(curr,bX,bY))
                return true;
        }
        return false;
    }
    
    public void cycleMode() {
        if(mode=='b')
            mode = 's';
        else if(mode=='s')
            mode = 'b';
        /*else if(mode=='c')
            mode = 'f';
        else
            mode = 'b';*/
        
        createRobot();
    }
    
    public void moveLeft() {
        x-=mobility;
        facingRight = false;
    }
    public void moveRight() {
        x+=mobility;
        facingRight = true;
    }
    public void setUp(boolean u) {
        up = u;
    }
    public void setDown(boolean d) {
        down = d;
    }
    public void setLeft(boolean l) {
        left = l;
    }
    public void setRight(boolean r) {
        right = r;
    }
    public void setFacingRight(boolean fr) {
        facingRight = fr;
    }
    public boolean isLeft() {
        return left;
    }
    public boolean isRight() {
        return right;
    }
    public boolean isAirborne() {
        return airborne;
    }
    public boolean isFacingRight() {
        return facingRight;
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

    public int getMobility() {
        return mobility;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }
    
    
}