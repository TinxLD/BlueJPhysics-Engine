
import sum.kern.*;

public class Circle
{
    private Buntstift pen;
    private Fenster w;

    public double px;
    public double py;

    public double vx;
    public double vy;

    public double ax = 0;
    public double ay = 2;

    private int cr;
    private int cg;
    private int cb;

    public int rad;

    public boolean isout;

    public static boolean debugText = true;
    public static boolean debugLine = true;
    public static boolean staticColor = true;
    
    public static double colfriction = 0.2;

    public Circle(Fenster w, double px, double py, double vx, double vy, int rad)
    {
        this.w = w;
        pen = new Buntstift();

        this.px = px;
        this.py = py;

        this.vx = vx;
        this.vy = vy;

        cr = (int)(Math.random() * 255);
        cg = (int)(Math.random() * 255);
        cb = (int)(Math.random() * 255);

        if(staticColor)
        {
            cr = 156; 
            cg = 158;
            cb = 181;
        }

        this.rad = rad;
    }

    public void pUpdate(){
        vx += ax;
        vy += ay;

        WallCollision();

        px += vx * 0.05;
        py += vy * 0.05;   

        /*
        if(py + rad >= w.hoehe() - 40 || py - rad <= 10 || px - rad <= 10 || px + rad >= w.breite() - 10)
            isout = true;
        else
            isout = false;
        */
    }

    private void WallCollision()
    {
        //Bottom
        if(py + rad >= w.hoehe() - 40)
        {
            vy = -vy;
            vy = vy * colfriction;

            //vy -= 1;

            if ((py + rad) - (w.hoehe() - 40) > 0)
            {
                py -= (((w.hoehe() - 40) - (py + rad)) * -1) * 0.7;
            }     
        }
        //Top (turned off)
        if(false || py - rad <= 10)
        {
            /*
            vy = -vy;
            vy = vy * colfriction;
            vx = vx * colfriction;

            vy += 1;

            /*
            if ((py - rad) - (10) < 0)
            {
            py += (py - rad) - (10) * -1;
            }
             */
        }

        //Left
        if(px - rad <= 0)
        {
            vx = -vx;
            vy = vy * colfriction;
            vx = vx * colfriction;

            vx += 1;
            px += 2;
        }

        if(px + rad >= w.breite())
        {
            vx = -vx;
            vy = vy * colfriction;
            vx = vx * colfriction;

            vx -= 1;       
            px -= 3;
        }
    }

    public void dUpdate(){
        pen.bewegeBis(px, py);
        pen.setzeFarbe(Farbe.rgb(cr, cg, cb));
        pen.setzeLinienbreite(3);
        pen.zeichneKreis(rad);

        if(debugLine)
        {
            pen.setzeFarbe(Farbe.GRUEN);
            pen.bewegeBis(px, py);
            pen.runter();
            pen.bewegeBis(px + vx, py + vy);
            pen.hoch();
        }
        if(debugText)
        {
            pen.setzeFarbe(Farbe.rgb(76, 87, 207));
            pen.bewegeBis(px, py); 
            pen.schreibeText("X:" + String.format(java.util.Locale.US,"%.2f", vx) + " Y:" + String.format(java.util.Locale.US,"%.2f", vy));
        }
    }

    public void applyForce(double explosionX, double explosionY, double force) {
        // Calculate the direction of the force
        double angle = Math.atan2(py - explosionY, px - explosionX);

        // Apply the force in that direction
        vx += force * Math.cos(angle);
        vy += force * Math.sin(angle);
    }
    
    public int getMass()
    {
        return (int)rad * (int)rad; 
    }

    double fmin(double a, double b){
        if(a > b)
            return b;
        else
            return a;
    }

    double fmax(double a, double b){
        if(a > b)
            return a;
        else
            return b;
    }
    
    public static double squaredDistanceBetweenCircles(Circle circle1, Circle circle2) {
        double dx = circle2.px - circle1.px;
        double dy = circle2.py - circle1.py;

        return Math.sqrt(dx * dx + dy * dy);
    }
}
