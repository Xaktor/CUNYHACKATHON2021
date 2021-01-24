//Vadim Allayev, Radmir Sataev, Veronica Koval, Addhyaya Sharma
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Font;
//adding images
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
//adding music
import java.applet.*;
import java.awt.Color;
import java.util.ArrayList;

public class RunningFile extends JComponent implements KeyListener, MouseListener
{
    private int WIDTH,HEIGHT;
    private int backgroundX, backgroundY,backgroundW,backgroundH,backgroundTop,level;
    private long startTime,currentTime;
    private boolean textFinished;
    private Player user;
    private ArrayList<Floor> floors;
    private ArrayList<Wall> walls;
    private ArrayList<Ceiling> ceilings;
    private ArrayList<Laser> lasers;
    private ArrayList<Decoration> decorations;
    private ArrayList<Block> blocks;
    private FinishLine finish;
    private Text text;
    private enum STATE{
        MENU,GAME,TUTORIAL,VICTORY
    };
    private STATE State=STATE.MENU; //fix before submit
    
    //Default Constructor
    public RunningFile()
    {
        //initializing instance variables
        WIDTH = 1200;
        HEIGHT = 675;
        
        backgroundX = 0;
        backgroundY = 0;
        backgroundW = 0;
        backgroundH = 0;
        backgroundTop = 0;
        level = 1; //fix before submit
        createLevel();
        
        startTime = System.currentTimeMillis();
        currentTime = 0;
        
        textFinished = false;
        
        user = new Player();
        text = new Text(50,50);
        
        //Setting up the GUI
        JFrame gui = new JFrame(); //This makes the gui box
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Makes sure program can close
        gui.setTitle("Robot Puzzle Game"); //This is the title of the game, you can change it
        gui.setPreferredSize(new Dimension(WIDTH + 5,HEIGHT + 30)); //Setting the size for gui
        gui.setResizable(false); //Makes it so the gui can't be resized
        gui.getContentPane().add(this); //Adding this class to the gui

        /*If after you finish everything, you can declare your buttons or other things
         *at this spot. AFTER gui.getContentPane().add(this) and BEFORE gui.pack();
         */

        gui.pack(); //Packs everything together
        gui.setLocationRelativeTo(null); //Makes so the gui opens in the center of screen
        gui.setVisible(true); //Makes the gui visible
        gui.addKeyListener(this);
        gui.addMouseListener(this);
    }
    
    
    //This method will acknowledge user input
    public void keyPressed(KeyEvent e) 
    {
        int key = e.getKeyCode();
        //System.out.println(key);
        
        if(State == STATE.GAME) {
            if(key==87) //w
                user.setUp(true);
            if(key==83) //s
                user.setDown(true);
            if(key==65) { //a
                user.setLeft(true);
            }
            if(key==68) { //d
                user.setRight(true);
            }
        }
    }
    
    
    //All your UI drawing goes in here
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        
        Color gray = new Color(38,38,38);
        g.setColor(gray);
        g.fillRect(0,0,WIDTH+5,HEIGHT+30);
        if(State==STATE.MENU){
            text.reassignCoordinates(100, 100);
            textFinished = text.type("Hi, this game engine was made for CUNY Hackathon 2021. We have created a couple of levels to showcase the engine. Press any key to start or press T to see the tutorial.", g2d,50,currentTime);
        }
        else if(State==STATE.TUTORIAL){
            text.reassignCoordinates(100, 100);
            textFinished = text.type("W to jump, A to move LEFT, D to move RIGHT, SPACE BAR to cycle robot modes. Objective: avoid obstacles such as laser beams and complete the level by reaching the elevator. Press any key to start the game.", g2d, 30, currentTime);
        }
        else if(State==STATE.GAME){
            for(int i=0; i<decorations.size(); i++) {
                decorations.get(i).drawSelf(g2d, backgroundX, backgroundY);
            }

            for(int i=0; i<blocks.size(); i++) {
                blocks.get(i).drawSelf(g2d, backgroundX, backgroundY);
            }
            
            user.drawSelf(g2d,currentTime);
            /*for(int i=0; i<floors.size(); i++) {
                floors.get(i).drawSelf(g2d, backgroundX, backgroundY);
            }*/
            /*for(int i=0; i<walls.size(); i++) {
                walls.get(i).drawSelf(g2d, backgroundX, backgroundY);
            }
            for(int i=0; i<ceilings.size(); i++) {
                ceilings.get(i).drawSelf(g2d, backgroundX, backgroundY);
            }*/
            for(int i=0; i<lasers.size();i++){
                lasers.get(i).drawSelf(g2d,currentTime, backgroundX, backgroundY);
            }
            finish.drawSelf(g2d, backgroundX, backgroundY);
        }
        else if(State == STATE.VICTORY) {
            text.reassignCoordinates(500,300);
            text.type("You Win!", g2d, 200, currentTime);
        }
    }
    
    public void loop()
    {
        currentTime = System.currentTimeMillis() - startTime;
        
        if(State==STATE.GAME) {
            int mob = user.getMobility();
            if(user.isLeft()) {
                if(user.getX()<=WIDTH/6 && backgroundX<-mob)
                    backgroundX+=mob;
                else
                    user.moveLeft();
            }
            if(user.isRight()) {
                int rightSide = user.getX() + user.getW();
                if(rightSide>=WIDTH/6*5 && rightSide-backgroundX<backgroundW-WIDTH/6-mob)
                    backgroundX-=mob;
                else
                    user.moveRight();
            }


            if(user.isAirborne()) {
                //rising + falling
                if(user.getY()<=HEIGHT/5 && user.getVelocityY()<0 && backgroundY<-backgroundTop+user.getVelocityY())
                    backgroundY-=user.getVelocityY();
                else if(user.getY()+user.getH()>=HEIGHT/5*4 && user.getVelocityY()>0 && backgroundY>user.getVelocityY())
                    backgroundY-=user.getVelocityY();
                else
                    user.moveVertical();
                user.updateVelocityY(currentTime);

            }

            user.act(floors,walls,ceilings,currentTime,backgroundX,backgroundY);

            if(user.isDead(lasers, currentTime, backgroundX, backgroundY, HEIGHT)) { //tp to checkpoint?
                user.reset(currentTime);
                backgroundX = 0;
                backgroundY = 0;
            }
            
            if(user.overlaps(finish,backgroundX,backgroundY)) {
                if(level==1) {
                    level++;
                    user.reset(currentTime);
                    backgroundX = 0;
                    backgroundY = 0;
                    createLevel();
                }
                else {
                    State = STATE.VICTORY;
                    text.updateWriteTime(currentTime);
                }
            }
        }
        //Do not write below this
        repaint();
    }
    
    public void createLevel() {
        floors = new ArrayList<>();
        walls = new ArrayList<>();
        ceilings = new ArrayList<>();
        lasers = new ArrayList<>();
        decorations = new ArrayList<>();
        blocks = new ArrayList<>();
        finish = new FinishLine();
        
        backgroundW = 2000;
        backgroundH = 800;
        backgroundTop = HEIGHT-backgroundH;
        
        decorations.add(new Decoration("small-square-background2",112,112,backgroundW,backgroundH,backgroundTop));
        decorations.add(new Decoration("dots-background",128,128,backgroundW,backgroundH,backgroundTop));
        decorations.add(new Decoration("x-background",80,120,backgroundW,backgroundH,backgroundTop));
        decorations.add(new Decoration("teeth-background",48,128,backgroundW,backgroundH,backgroundTop));
        decorations.add(new Decoration("small-square-with-inside-background2",112,112,backgroundW,backgroundH,backgroundTop));
        decorations.add(new Decoration("semi-x-background",112,128,backgroundW,backgroundH,backgroundTop));
        decorations.add(new Decoration("short-wires-background",80,56,backgroundW,backgroundH,backgroundTop));
        decorations.add(new Decoration("wires-background",112,48,backgroundW,backgroundH,backgroundTop));
        decorations.add(new Decoration("octagon-background",224,240,backgroundW,backgroundH,backgroundTop));
        decorations.add(new Decoration("small-square-background",112,104,backgroundW,backgroundH,backgroundTop));
        
        
        if(level==1) {
            backgroundW = 3000;
            backgroundH = 2000;
            backgroundTop = HEIGHT-backgroundH;
            
            //boundary walls
            floors.add(new Floor(20,backgroundTop,32,backgroundH));
            floors.add(new Floor(backgroundW-52,backgroundTop,32,backgroundH));
            for(int i=0; i<backgroundH; i+=64) {
                blocks.add(new Block("basic-block-vertical",20,backgroundTop + i,32,64));
                blocks.add(new Block("basic-block-vertical",backgroundW-52,backgroundTop + i,32,64));
            }
            
            
            floors.add(new Floor(0,HEIGHT-100,WIDTH,40));
            floors.add(new Floor(WIDTH/5*3+100,HEIGHT-250,128,32)); //2 small blocks - plat1
            floors.add(new Floor(WIDTH/3+100,HEIGHT-400,128,32)); //2 small blocks - plat2
            floors.add(new Floor(WIDTH/5*3+100,HEIGHT-550,128,32)); //2 small blocks - plat3
            floors.add(new Floor(WIDTH-100,HEIGHT-700,1152,32)); //20 small blocks - floor after plats
            floors.add(new Floor(20,HEIGHT-924,1080+1408,32)); //middle barrier
            floors.add(new Floor(WIDTH+156,HEIGHT-476,1152,32)); //middle maze floor
            floors.add(new Floor(WIDTH-100,HEIGHT-252,1440,32)); //bottom maze floor
            
            blocks.add(new Block("basic-block",WIDTH/5*3+100,HEIGHT-250,64,32));
            blocks.add(new Block("basic-block",WIDTH/5*3+164,HEIGHT-250,64,32));
            
            blocks.add(new Block("basic-block",WIDTH/3+100,HEIGHT-400,64,32));
            blocks.add(new Block("basic-block",WIDTH/3+164,HEIGHT-400,64,32));
            
            blocks.add(new Block("basic-block",WIDTH/5*3+100,HEIGHT-550,64,32));
            blocks.add(new Block("basic-block",WIDTH/5*3+164,HEIGHT-550,64,32));
            
            blocks.add(new Block("basic-block",WIDTH-100,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*1,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*2,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*3,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*4,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*5,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*6,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*7,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*8,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*9,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*10,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*11,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*12,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*13,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*14,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*15,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*16,HEIGHT-700,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*17,HEIGHT-700,64,32));
            
            blocks.add(new Block("basic-block",20,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*1,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*2,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*3,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*4,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*5,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*6,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*7,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*8,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*9,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*10,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*11,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*12,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*13,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*14,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*15,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*16,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*17,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*18,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*19,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*20,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*21,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*22,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*23,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*24,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*25,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*26,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*27,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*28,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*29,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*30,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*31,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*32,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*33,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*34,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*35,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*36,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*37,HEIGHT-924,64,32));
            blocks.add(new Block("basic-block",20+64*38,HEIGHT-924,64,32));
            
            blocks.add(new Block("basic-block",WIDTH+156,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*1,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*2,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*3,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*4,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*5,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*6,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*7,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*8,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*9,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*10,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*11,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*12,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*13,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*14,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*15,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*16,HEIGHT-476,64,32));
            blocks.add(new Block("basic-block",WIDTH+156+64*17,HEIGHT-476,64,32));
            
            blocks.add(new Block("basic-block",WIDTH-100,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*1,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*2,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*3,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*4,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*5,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*6,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*7,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*8,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*9,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*10,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*11,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*12,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*13,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*14,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*15,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*16,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*17,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*18,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*19,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*20,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*21,HEIGHT-252,64,32));
            blocks.add(new Block("basic-block",WIDTH-100+64*22,HEIGHT-252,64,32));
            
            
            blocks.add(new Block("basic-block",0,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",0,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*2,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*3,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*4,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*5,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*6,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*7,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*8,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*9,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*10,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*11,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*12,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*13,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*14,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*15,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*16,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*17,HEIGHT-100,64,32));
            blocks.add(new Block("basic-block",64*18,HEIGHT-100,64,32));
            
            for(int i=0; i<backgroundH; i+=64) {
                blocks.add(new Block("basic-block-vertical",20,backgroundTop + i,32,64));
                blocks.add(new Block("basic-block-vertical",2508,backgroundTop + i,32,64));
                blocks.add(new Block("basic-block-vertical",WIDTH-105,0 + i,32,64));
            }
            
            finish = new FinishLine(2300, 300);
            
            for(int i=0; i<floors.size(); i++) {
                Floor curr = floors.get(i);
                walls.add(curr.getWall1());
                walls.add(curr.getWall2());
                ceilings.add(curr.getCeiling());
            }
            
            lasers.add(new Laser(WIDTH+412,HEIGHT-748,false,false));
            lasers.add(new Laser(WIDTH+412,HEIGHT-796,false,false));
            lasers.add(new Laser(WIDTH+412,HEIGHT-844,false,false));
            lasers.add(new Laser(WIDTH+412,HEIGHT-892,false,false));
            
            lasers.add(new Laser(WIDTH+668,HEIGHT-748,false,false));
            lasers.add(new Laser(WIDTH+668,HEIGHT-796,false,false));
            lasers.add(new Laser(WIDTH+668,HEIGHT-844,false,false));
            lasers.add(new Laser(WIDTH+668,HEIGHT-892,false,false));
            
            blocks.add(new Block("zapper-on-down-red",WIDTH+404,HEIGHT-924,32,32));
            blocks.add(new Block("zapper-on-up-red",WIDTH+404,HEIGHT-700,32,32));
            blocks.add(new Block("zapper-on-down-red",WIDTH+660,HEIGHT-924,32,32));
            blocks.add(new Block("zapper-on-up-red",WIDTH+660,HEIGHT-700,32,32));
            
            walls.add(new Wall(WIDTH-105,0,5,HEIGHT)); //left maze wall
            walls.add(new Wall(2508,HEIGHT-924,5,224+32+224)); //right maze wall
        }
        else if(level==2) {
            backgroundW = 3000;
            backgroundH = 2000;
            backgroundTop = HEIGHT-backgroundH;
            
            //boundary walls
            floors.add(new Floor(20,backgroundTop,32,backgroundH));
            floors.add(new Floor(backgroundW-52,backgroundTop,32,backgroundH));
            for(int i=0; i<backgroundH; i+=64) {
                blocks.add(new Block("basic-block-vertical",20,backgroundTop + i,32,64));
                blocks.add(new Block("basic-block-vertical",backgroundW-52,backgroundTop + i,32,64));
            }
            
            floors.add(new Floor(0,HEIGHT-100,1500,32)); //first floor
            floors.add(new Floor(0,HEIGHT-372,1372,32)); //first ceiling
            floors.add(new Floor(1500,HEIGHT-228,160,160)); //first block
            floors.add(new Floor(1340,HEIGHT-532,32,160)); 
            floors.add(new Floor(1340,HEIGHT-532,480,32)); 
            floors.add(new Floor(1815,HEIGHT-532,32,160)); 
            floors.add(new Floor(1660,HEIGHT-100,480,32)); //second floor
            floors.add(new Floor(1815,HEIGHT-372,192,32)); //second ceiling
            floors.add(new Floor(1980,HEIGHT-532,32,160)); 
            floors.add(new Floor(2476,HEIGHT-100,524,32)); //third floor
            floors.add(new Floor(2800,HEIGHT-244,128,32)); //2 small blocks - bot plat1
            floors.add(new Floor(2500,HEIGHT-388,128,32)); //2 small blocks - bot plat2
            floors.add(new Floor(2200,HEIGHT-532,128,32)); //2 small blocks - bot plat3
            floors.add(new Floor(1020,HEIGHT-1302,32,930)); //leftmost vertical wall second level
            floors.add(new Floor(1420,HEIGHT-1142,32,450)); //right vertical wall second level
            floors.add(new Floor(1052,HEIGHT-692,128,32)); //2nd level plat1
            floors.add(new Floor(1292,HEIGHT-842,128,32)); //2nd level plat2
            floors.add(new Floor(1052,HEIGHT-992,128,32)); //2nd level plat3
            floors.add(new Floor(1020,HEIGHT-1302,800,32)); //top ceiling
            floors.add(new Floor(1420,HEIGHT-1142,640,32)); //top floor
            
            //floor + ceiling 1
            blocks.add(new Block("basic-block",1308,HEIGHT-372,64,32));
            for(int i=0; i<24; i++) {
                blocks.add(new Block("basic-block",i*64,HEIGHT-100,64,32));
                if(i<21)
                    blocks.add(new Block("basic-block",i*64,HEIGHT-372,64,32));
            }
            //floor 2
            blocks.add(new Block("basic-block",2076,HEIGHT-100,64,32));
            for(int i=0; i<7; i++) {
                blocks.add(new Block("basic-block",1660+i*64,HEIGHT-100,64,32));
            }
            //ceiling 2
            for(int i=0; i<3; i++) {
                blocks.add(new Block("basic-block",1815+i*64,HEIGHT-372,64,32));
            }
            //floor 3
            for(int i=0; i<9; i++) {
                blocks.add(new Block("basic-block",2476+i*64,HEIGHT-100,64,32));
            }
            
            blocks.add(new Block("square-block-bolts",1500,HEIGHT-228,160,160));
            
            blocks.add(new Block("basic-block",1756,HEIGHT-532,64,32));
            for(int i=0; i<7; i++) {
                blocks.add(new Block("basic-block",1340+i*64,HEIGHT-532,64,32));
            }
            
            blocks.add(new Block("basic-block-vertical",1340,HEIGHT-436,32,64));
            blocks.add(new Block("basic-block-vertical",1340,HEIGHT-532,32,64));
            blocks.add(new Block("basic-block-vertical",1340,HEIGHT-468,32,64));
            blocks.add(new Block("basic-block-vertical",1815,HEIGHT-436,32,64));
            blocks.add(new Block("basic-block-vertical",1815,HEIGHT-532,32,64));
            blocks.add(new Block("basic-block-vertical",1815,HEIGHT-468,32,64));
            blocks.add(new Block("basic-block-vertical",1975,HEIGHT-436,32,64));
            blocks.add(new Block("basic-block-vertical",1975,HEIGHT-532,32,64));
            blocks.add(new Block("basic-block-vertical",1975,HEIGHT-468,32,64));
             
            //top floor, top ceiling
            blocks.add(new Block("basic-block",1756,HEIGHT-1302,64,32));
            for(int i=0; i<12; i++) {
                blocks.add(new Block("basic-block",1020+i*64,HEIGHT-1302,64,32));
            }
            for(int i=0; i<10; i++) {
                blocks.add(new Block("basic-block",1420+i*64,HEIGHT-1142,64,32));
            }
            
            //long walls second level
            blocks.add(new Block("basic-block-vertical",1020,HEIGHT-436,32,64));
            for(int i=0; i<14; i++) {
                blocks.add(new Block("basic-block-vertical",1020,HEIGHT-1302+i*64,32,64));
            }
            for(int i=0; i<7; i++) {
                blocks.add(new Block("basic-block-vertical",1420,HEIGHT-1142+i*64,32,64));
            }
            
            
            //small plats right side
            blocks.add(new Block("basic-block",2800,HEIGHT-244,64,32));
            blocks.add(new Block("basic-block",2864,HEIGHT-244,64,32));
            blocks.add(new Block("basic-block",2500,HEIGHT-388,64,32));
            blocks.add(new Block("basic-block",2564,HEIGHT-388,64,32));
            blocks.add(new Block("basic-block",2200,HEIGHT-532,64,32));
            blocks.add(new Block("basic-block",2264,HEIGHT-532,64,32));
            
            //small plats left side
            blocks.add(new Block("basic-block",1052,HEIGHT-692,64,32));
            blocks.add(new Block("basic-block",1116,HEIGHT-692,64,32));
            blocks.add(new Block("basic-block",1292,HEIGHT-842,64,32));
            blocks.add(new Block("basic-block",1356,HEIGHT-842,64,32));
            blocks.add(new Block("basic-block",1052,HEIGHT-992,64,32));
            blocks.add(new Block("basic-block",1116,HEIGHT-992,64,32));
            
            for(int i=0; i<floors.size(); i++) {
               Floor curr = floors.get(i);
               walls.add(curr.getWall1());
               walls.add(curr.getWall2());
               ceilings.add(curr.getCeiling());
            }
            
            finish = new FinishLine(200,180);
            
            //first 3 red lasers
            lasers.add(new Laser(600,HEIGHT-148,false,false));
            lasers.add(new Laser(600,HEIGHT-196,false,false));
            lasers.add(new Laser(600,HEIGHT-244,false,false));
            lasers.add(new Laser(600,HEIGHT-292,false,false));
            lasers.add(new Laser(600,HEIGHT-340,false,false));
            lasers.add(new Laser(856,HEIGHT-148,false,false));
            lasers.add(new Laser(856,HEIGHT-196,false,false));
            lasers.add(new Laser(856,HEIGHT-244,false,false));
            lasers.add(new Laser(856,HEIGHT-292,false,false));
            lasers.add(new Laser(856,HEIGHT-340,false,false));
            lasers.add(new Laser(1112,HEIGHT-148,false,false));
            lasers.add(new Laser(1112,HEIGHT-196,false,false));
            lasers.add(new Laser(1112,HEIGHT-244,false,false));
            lasers.add(new Laser(1112,HEIGHT-292,false,false));
            lasers.add(new Laser(1112,HEIGHT-340,false,false));
            
            blocks.add(new Block("zapper-on-down-red",592,HEIGHT-372,32,32));
            blocks.add(new Block("zapper-on-up-red",592,HEIGHT-100,32,32));
            blocks.add(new Block("zapper-on-down-red",848,HEIGHT-372,32,32));
            blocks.add(new Block("zapper-on-up-red",848,HEIGHT-100,32,32));
            blocks.add(new Block("zapper-on-down-red",1104,HEIGHT-372,32,32));
            blocks.add(new Block("zapper-on-up-red",1104,HEIGHT-100,32,32));
            
            //next single red laser
            lasers.add(new Laser(1903,HEIGHT-148,false,false));
            lasers.add(new Laser(1903,HEIGHT-196,false,false));
            lasers.add(new Laser(1903,HEIGHT-244,false,false));
            lasers.add(new Laser(1903,HEIGHT-292,false,false));
            lasers.add(new Laser(1903,HEIGHT-340,false,false));
            
            blocks.add(new Block("zapper-on-down-red",1895,HEIGHT-372,32,32));
            blocks.add(new Block("zapper-on-up-red",1895,HEIGHT-100,32,32));
            
            //plat red lasers
            lasers.add(new Laser(1052,HEIGHT-834,true,false));
            lasers.add(new Laser(1100,HEIGHT-834,true,false));
            lasers.add(new Laser(1148,HEIGHT-834,true,false));
            lasers.add(new Laser(1196,HEIGHT-834,true,false));
            lasers.add(new Laser(1244,HEIGHT-834,true,false));
            
            blocks.add(new Block("zapper-on-right-red",1020,HEIGHT-842,32,32));
            blocks.add(new Block("zapper-on-left-red",1292,HEIGHT-842,32,32));
            
            lasers.add(new Laser(1180,HEIGHT-984,true,false));
            lasers.add(new Laser(1228,HEIGHT-984,true,false));
            lasers.add(new Laser(1276,HEIGHT-984,true,false));
            lasers.add(new Laser(1324,HEIGHT-984,true,false));
            lasers.add(new Laser(1372,HEIGHT-984,true,false));
            
            blocks.add(new Block("zapper-on-right-red",1148,HEIGHT-992,32,32));
            blocks.add(new Block("zapper-on-left-red",1420,HEIGHT-992,32,32));
            
            //annoying top blue laser
            lasers.add(new Laser(1377,HEIGHT-482,true,true));
            lasers.add(new Laser(1425,HEIGHT-482,true,true));
            lasers.add(new Laser(1473,HEIGHT-482,true,true));
            lasers.add(new Laser(1521,HEIGHT-482,true,true));
            lasers.add(new Laser(1569,HEIGHT-482,true,true));
            lasers.add(new Laser(1617,HEIGHT-482,true,true));
            lasers.add(new Laser(1665,HEIGHT-482,true,true));
            lasers.add(new Laser(1713,HEIGHT-482,true,true));
            lasers.add(new Laser(1761,HEIGHT-482,true,true));
            
            blocks.add(new Block("zapper-on-right-blue",1340,HEIGHT-490,32,32));
            
            lasers.add(new Laser(1847,HEIGHT-482,true,true));
            lasers.add(new Laser(1895,HEIGHT-482,true,true));
            lasers.add(new Laser(1927,HEIGHT-482,true,true));
            
            blocks.add(new Block("zapper-on-middle-blue",1815,HEIGHT-490,32,32));
            blocks.add(new Block("zapper-on-left-blue",1975,HEIGHT-490,32,32));
            
            //bottom level floor laser
            lasers.add(new Laser(2140,HEIGHT-92,true,true));
            lasers.add(new Laser(2188,HEIGHT-92,true,true));
            lasers.add(new Laser(2236,HEIGHT-92,true,true));
            lasers.add(new Laser(2284,HEIGHT-92,true,true));
            lasers.add(new Laser(2332,HEIGHT-92,true,true));
            lasers.add(new Laser(2380,HEIGHT-92,true,true));
            lasers.add(new Laser(2428,HEIGHT-92,true,true));
            
            blocks.add(new Block("zapper-on-right-blue",2108,HEIGHT-100,32,32));
            blocks.add(new Block("zapper-on-left-blue",2476,HEIGHT-100,32,32));
            
            //middle level floor laser
            lasers.add(new Laser(1292,HEIGHT-524,true,true));
            lasers.add(new Laser(1244,HEIGHT-524,true,true));
            lasers.add(new Laser(1196,HEIGHT-524,true,true));
            lasers.add(new Laser(1148,HEIGHT-524,true,true));
            lasers.add(new Laser(1100,HEIGHT-524,true,true));
            lasers.add(new Laser(1052,HEIGHT-524,true,true));
            
            blocks.add(new Block("zapper-on-right-blue",1020,HEIGHT-532,32,32));
            blocks.add(new Block("zapper-on-left-blue",1340,HEIGHT-532,32,32));
            
        }
    }
    
    
    
    //These methods are required by the compiler.  
    //You might write code in these methods depending on your goal.
    public void keyTyped(KeyEvent e) 
    {
    }
    public void keyReleased(KeyEvent e) 
    {
        int key = e.getKeyCode();
        
        if(State == STATE.MENU) {
            if(textFinished) {
                if(key==84) {
                    text.updateWriteTime(currentTime);
                    State = STATE.TUTORIAL;
                }
                else {
                    State = STATE.GAME;
                }
            }
        }
        else if(State == STATE.TUTORIAL) {
            if(textFinished)
                State = STATE.GAME;
        }
        else if(State == STATE.GAME) {
            if(key==87) //w
                user.setUp(false);
            if(key==83) //s
                user.setDown(false);
            if(key==65) //a
                user.setLeft(false);
            if(key==68) //d
                user.setRight(false);
            if(key==32) //space
                user.cycleMode();
        }
    }
    public void mousePressed(MouseEvent e)
    {
    }
    public void mouseReleased(MouseEvent e)
    {
    }
    public void mouseClicked(MouseEvent e)
    {
    }
    public void mouseEntered(MouseEvent e)
    {
    }
    public void mouseExited(MouseEvent e)
    {
    }
    public void start(final int ticks){
        Thread gameThread = new Thread(){
            public void run(){
                while(true){
                        loop();
                        try{
                        Thread.sleep(1000 / ticks);
                        }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };	
    gameThread.start();
    }

    public static void main(String[] args)
    {
        
        RunningFile r = new RunningFile();
        r.start(60);
    }
}
