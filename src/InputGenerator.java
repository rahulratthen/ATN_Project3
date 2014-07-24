
import java.util.ArrayList;
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

//This class generates 'n' points on the plane (0,0) to(100,100)
public class InputGenerator {
    
    public ArrayList<Point> GetRandomPoints(int n)
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
        return points;
    }
    /*
    public static void main(String[] args) {
        InputGenerator in = new InputGenerator();
        ArrayList<Point> p = in.GetRandomPoints(20);
        System.out.println("hi");
    }*/
    
}

class Point
{
    int x,y;
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object o) 
    {
        Point o1 = (Point) o;
        if(o1.x == this.x && o1.y == this.y)
            return true;
        return false;
    }
    
    @Override
    public int hashCode(){
        return 1;
    }
}
