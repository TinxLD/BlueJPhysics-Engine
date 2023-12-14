import sum.kern.*;
import java.util.*;

public class DebugText
{
     private Buntstift pen;

     private double px;
     private double py;
     private String text;
     
    public DebugText(double px, double py, String text)
    {
        pen = new Buntstift();
        
        this.px = px;
        this.py = py;
        setText(text);
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public void Draw(){
        pen.bewegeBis(px, py);
        pen.setzeFarbe(Farbe.ROT);
        pen.schreibeText(text + "");
    }
}
