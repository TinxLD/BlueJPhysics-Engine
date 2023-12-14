
import sum.kern.*;

import java.util.*;

public class Main
{
    public Fenster window;
    private Buntstift pen;
    private Maus m;

    private ArrayList<Circle> obj = new ArrayList();
    private DebugText bc;
    private DebugText ms;
    private DebugText mms;

    private long maxexecutionTime;
    private static int maxMS = 5;

    public Main()
    {
        window = new Fenster("Physics", true);
        pen = new Buntstift();
        m = new Maus();

        bc = new DebugText(60, 40, " ");
        ms = new DebugText(60, 50, " ");
        mms = new DebugText(60, 60, " ");

        for(int i = 0; i < 0; i++)
            obj.add(new Circle(window, 200 + Math.random() * 1000, 200 + Math.random() * 1000, Math.random() * 400 - 200, Math.random() * 200 - 100, (int)(Math.random() * 2) + 10));

        run();
    }

    boolean skipframe;
    public void run()
    {
        while (true) { 

            long startTime = System.currentTimeMillis();

            if(m.istGedrueckt())
            {
                maxexecutionTime = 0;
                
                for(Circle o : obj)
                {
                    o.applyForce(m.hPosition(), m.vPosition(), 30);
                }
            }
            
            for(Circle o : obj)
            {
                o.pUpdate();
                skipframe = false;
                if(skipframe)
                    skipframe = !skipframe;
                else
                    col();

                o.dUpdate();

                ms.Draw();
                mms.Draw();
                bc.Draw();

                pen.bewegeBis(0, window.hoehe() - 40);
                pen.setzeFarbe(Farbe.GRAU);
                pen.setzeFuellMuster(Muster.GEFUELLT);
                pen.zeichneRechteck(1920, 1920);       
            }    

            try
            {
                window.zeichneDich();
                pen.bewegeBis(0, 0);
                pen.setzeFarbe(Farbe.rgb(31, 32, 41));
                pen.setzeFuellMuster(Muster.GEFUELLT);
                pen.zeichneRechteck(1920, 1920);
            }
            catch(Exception e)
            {

            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            if(executionTime > maxexecutionTime)
                maxexecutionTime = executionTime;
            
            ms.setText(executionTime + " ms per Step");
            mms.setText(maxexecutionTime + " ms MAX per Step");
            bc.setText(obj.size() + " Circles"); 

            if(executionTime < maxMS && obj.size() < 5)
                obj.add(new Circle(window, 200 + Math.random() * 1000, 200 + Math.random() * 1000, Math.random() * 400 - 200, Math.random() * 200 - 100, (int)(Math.random() * 30) + 10));

        }
    }

    public void col()
    {
        for(Circle p1 : obj){
            for(Circle p2 : obj){
                if(p1 == p2){
                    continue;
                }

                if(ParticleOverlap(p1, p2))
                {
                    double distance = distanceSquared(p1.px, p1.py, p2.px, p2.py);
                    distance = Math.sqrt(distance);
                    double overlap = (distance - p1.rad - p2.rad)/ 2.f;

                    double moveX = (overlap * (p1.px - p2.px) / distance);
                    double moveY = (overlap * (p1.py - p2.py) / distance);

                    /*
                    if(p1.isout)
                    {
                    p2.px = p2.px + moveX * 2;
                    p2.py = p2.py + moveY * 2;
                    }
                    else if(p2.isout)
                    {
                    p1.px = p1.px - moveX * 2;
                    p1.py = p1.py - moveY * 2;
                    }
                    else
                    {
                    p1.px = p1.px - moveX;
                    p1.py = p1.py - moveY;

                    p2.px = p2.px + moveX;
                    p2.py = p2.py + moveY;
                    }
                     */

                    p1.px = p1.px - moveX;
                    p1.py = p1.py - moveY;

                    p2.px = p2.px + moveX;
                    p2.py = p2.py + moveY;

                    double normalx = (p2.px - p1.px) / distance;
                    double normaly = (p2.py - p1.py) / distance;

                    double tangentx = -normaly;
                    double tangenty = normalx;

                    double dotProductTan1 = p1.vx * tangentx + p1.vy * tangenty;
                    double dotProductTan2 = p2.vx * tangentx + p2.vy * tangenty;

                    double dotProductNorm1 = p1.vx * normalx + p1.vy * normaly;
                    double dotProductNorm2 = p2.vx * normalx + p2.vy * normaly;

                    double m1 = (dotProductNorm1 * (p1.getMass() - p2.getMass()) + 2.0f * p1.getMass() * dotProductNorm2) / (p1.getMass() + p2.getMass());
                    double m2 = (dotProductNorm2 * (p2.getMass() - p1.getMass()) + 2.0f * p1.getMass() * dotProductNorm1) / (p1.getMass() + p2.getMass());

                    double p1XV = fmin(fmax(tangentx * dotProductTan1 + normalx * m1 *0.8, -30), 30);
                    double p1YV = fmin(fmax(tangenty * dotProductTan1 + normaly * m1 *0.8, -30), 30);
                    double p2XV = fmin(fmax(tangentx * dotProductTan2 + normalx * m2 *0.8, -30), 30);
                    double p2YV = fmin(fmax(tangenty * dotProductTan2 + normaly * m2 *0.8, -30), 30);
                    p1.vx = p1XV;
                    p1.vy = p1YV;
                    p2.vx = p2XV;
                    p2.vy = p2YV;
                }
            }
        }
    }

    double distanceSquared(double ax, double ay, double bx, double by){
        double distX = ax - bx;
        double distY = ay - by;
        return distX * distX + distY * distY;
    }

    boolean ParticleOverlap(Circle p1, Circle p2) {
        double distSquare = distanceSquared(p1.px, p1.py, p2.px, p2.py);
        double radiusSum = p1.rad + p2.rad;
        return distSquare < (radiusSum * radiusSum);
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
}

