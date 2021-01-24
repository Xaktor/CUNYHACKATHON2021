import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.io.Serializable;

public class Text implements Serializable
{
    private int writeX, writeY; //current coordinates
    private int startX, startY; //starting coordinates
    private int furthestX = 1150; //WIDTH-50 //cannot write
    //private int furthestY = 625; //HEIGHT-50 //past this point
    private long writeTime; //keep track of when you started writing
    private long finishedTime; //keep track of when you finished writing
    private int[] indexes;
    
    public Text()
    {
        writeX = 0;
        writeY = 0;
        startX = 0;
        startY = 0;
        writeTime = 0;
        finishedTime = 0;
        indexes = new int[0];
    }
    
    public Text(int x1, int y1)
    {
        writeX = x1;
        writeY = y1;
        startX = x1;
        startY = y1;
        writeTime = 0;
        finishedTime = 0;
        indexes = new int[0];
    }
    
    public void write(String str, Graphics2D g2d) {
        for(int i=0; i<str.length(); i++)
        {
            char letter = str.charAt(i);
            boolean upper = Character.isUpperCase(letter);
            boolean symbol = !Character.isLetter(letter);
            if(letter!=' ') {
                Image image;
                
                if(upper || symbol) {
                    if(letter=='.')
                        image = new ImageIcon(this.getClass().getResource("Font/period.png")).getImage();
                    else if(letter==':')
                        image = new ImageIcon(this.getClass().getResource("Font/colon.png")).getImage();
                    else if(letter=='?')
                        image = new ImageIcon(this.getClass().getResource("Font/question.png")).getImage();
                    else if(letter=='/')
                        image = new ImageIcon(this.getClass().getResource("Font/slash.png")).getImage();
                    else if(letter=='"')
                        image = new ImageIcon(this.getClass().getResource("Font/quote.png")).getImage();
                    else
                        image = new ImageIcon(this.getClass().getResource("Font/"+letter+".png")).getImage();
                }
                else {
                    if(letter=='q'||letter=='g'||letter=='p'||letter=='j'||letter=='y')
                        writeY+=10;
                    image = new ImageIcon(this.getClass().getResource("Font/"+letter+letter+".png")).getImage();
                }
                g2d.drawImage(image,writeX,writeY,25,35,null); //5,7
            }
            writeX+=25;
            if(letter=='W' || letter=='M' || letter=='T' || letter=='Y'||letter=='w'||letter=='m'||letter=='n'||letter=='/')
                writeX+=5;
            else if(letter=='f' || letter=='c' || letter=='k'||letter=='x'||letter=='1'||letter=='"')
                writeX-=5;
            else if(letter=='l' || letter=='(' || letter==')')
                writeX-=10;
            else if(letter=='i'||letter=='I'||letter=='!'||letter=='\''||letter=='.'||letter==':')
                writeX-=15;
            else if(letter=='q'||letter=='g'||letter=='p'||letter=='j'||letter=='y')
                writeY-=10;
            
            //check to see if the next word will go past furthestX
            if(letter==' ') {
                String parsedStr = str.substring(i+1);
                int nextWordLength;
                if(parsedStr.indexOf(" ")==-1) //no more spaces (so last word)
                    nextWordLength = parsedStr.length();
                else
                    nextWordLength = parsedStr.indexOf(" ")+1;
                
                if(writeX+nextWordLength*25>furthestX) {
                    writeX = startX;
                    writeY+=startY+55;
                }
            }
            else if(writeX>furthestX) {
                writeX = startX;
                writeY+=+55;
            }
        }
        writeX = startX;
        writeY = startY;
    }
    
    /**
     * Prints out a message of your choosing one letter at a time.
     * @param str the message you want to be typed
     * @param g2d 
     * @param duration how long it takes for one letter to print out
     * @param currentTime
     * @return true if the message has finished typing, false if the message
     * is still being typed out
     */
    public boolean type(String str, Graphics2D g2d, int duration, long currentTime)
    {
        int extent = (int)((currentTime-writeTime)/duration);
        if(extent>str.length())
            extent = str.length();
        for(int i=0; i<extent; i++)
        {
            char letter = str.charAt(i);
            boolean upper = Character.isUpperCase(letter);
            boolean symbol = !Character.isLetter(letter);
            if(letter!=' ') {
                Image image;
                
                if(upper || symbol) {
                    /*if(letter==',')
                        writeY+=10;*/
                    
                    if(letter=='.')
                        image = new ImageIcon(this.getClass().getResource("Font/period.png")).getImage();
                    else if(letter==':')
                        image = new ImageIcon(this.getClass().getResource("Font/colon.png")).getImage();
                    else if(letter=='?')
                        image = new ImageIcon(this.getClass().getResource("Font/question.png")).getImage();
                    else if(letter=='/')
                        image = new ImageIcon(this.getClass().getResource("Font/slash.png")).getImage();
                    else if(letter=='%')
                        image = new ImageIcon(this.getClass().getResource("Font/empty.png")).getImage();
                    else
                        image = new ImageIcon(this.getClass().getResource("Font/"+letter+".png")).getImage();
                }
                else {
                    if(letter=='q'||letter=='g'||letter=='p'||letter=='j'||letter=='y')
                        writeY+=10;
                    image = new ImageIcon(this.getClass().getResource("Font/"+letter+letter+".png")).getImage();
                }
                
                g2d.drawImage(image,writeX,writeY,25,35,null); //5,7
            }
            writeX+=25;
            if(letter=='W' || letter=='M' || letter=='T' || letter=='Y'||letter=='w'||letter=='m'||letter=='n'||letter=='/')
                writeX+=5;
            else if(letter=='f' || letter=='c' || letter=='k'||letter=='x'||letter=='1')
                writeX-=5;
            else if(letter=='l' || letter=='(' || letter==')')
                writeX-=10;
            else if(letter=='i'||letter=='I'||letter=='!'||letter=='\''||letter=='.'||letter==':')
                writeX-=15;
            else if(letter=='q'||letter=='g'||letter=='p'||letter=='j'||letter=='y')
                writeY-=10;
            else if(letter=='%')
                writeX-=25;
            
            //check to see if the next word will go past furthestX
            if(letter==' ') {
                String parsedStr = str.substring(i+1);
                int nextWordLength;
                if(parsedStr.indexOf(" ")==-1) //no more spaces (so last word)
                    nextWordLength = parsedStr.length();
                else
                    nextWordLength = parsedStr.indexOf(" ")+1;
                
                if(writeX+nextWordLength*25>furthestX) {
                    writeX = startX;
                    writeY+=55;
                }
            }
            else if(writeX>furthestX) {
                writeX = startX;
                writeY+=55;
            }
        }
        writeX = startX;
        writeY = startY;
        
        if(extent==str.length())
            return true;
        return false;
    }
    
    public void changeX(int newX) {
        writeX = newX;
        startX = newX;
    }
    
    public void changeY(int newY) {
        writeY = newY;
        startY = newY;
    }
    
    public void reassignCoordinates(int newX, int newY) {
        writeX = newX;
        writeY = newY;
        startX = newX;
        startY = newY;
    }
    
    public long getWriteTime() {
        return writeTime;
    }
    
    public void updateWriteTime(long w) {
        writeTime = w;
    }
}