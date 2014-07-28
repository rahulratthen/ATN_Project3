
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rahulrdhanendran
 */

//this class just executes both the algorithms and displays results
public class MainClass {
    
    public void PointGenerator(int n)
    {
        ArrayList<Point> points = new ArrayList<>();
        Random rand = new Random();
        
        while(points.size()<n)
        {
            Point p = new Point(rand.nextInt(101), rand.nextInt(101));
            if(!points.contains(p))
            {
                points.add(p);
            }
        }
         
        //Write to the common file
        try
        {
            PrintWriter writer = new PrintWriter("Points.txt", "UTF-8");
            Iterator it = points.iterator();
            while(it.hasNext())
            {
                Point p = (Point)it.next();
                writer.println(p.x+"\t"+p.y);
            }
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("Blah");
        }
        
    }
    
    public static void main(String[] args) {
        MainClass mc = new MainClass();
        int nodes = 20;
        mc.PointGenerator(nodes);
        Algorithm1 a1 = new Algorithm1(nodes);
        a1.ConstructGraph();
        System.out.println("Algorithm 1 Minimum Cost : "+a1.getCost());
        GUIGraph gui1 = new GUIGraph("Complete Graph pruning Algorithm");
        
        Algorithm2 a2 = new Algorithm2(nodes);
        a2.execute();
        GUIGraph gui2 = new GUIGraph("Genetic Algorithm");
    }
}
