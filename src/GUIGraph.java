
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rahulrdhanendran
 */



public class GUIGraph extends JFrame
{
    public GUIGraph()
    {
        setSize(800,800);
        setTitle("Graph");
        MyPanel panel = new MyPanel();
        add(panel);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        GUIGraph gui = new GUIGraph();
        
    }
}


class MyPanel extends Component {
    ArrayList<Point> vertices;
    ArrayList<EdgeDS> edges;
    public MyPanel()
    {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        ReadFromFiles();
    }
    
    public void ReadFromFiles()
    {
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("Vertices.txt"));
            String line = br.readLine();
            
            while(line!=null)
            {
                String[] bits = line.split("\t");
                Point p = new Point(Integer.parseInt(bits[0]),Integer.parseInt(bits[1]));
                vertices.add(p);
                line = br.readLine();
            }
        }
        catch(Exception e)
        {
            System.out.println("Blah");
        }
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader("Edges.txt"));
            String line = br.readLine();
            
            while(line!=null)
            {
                String[] bits = line.split("\t");
                EdgeDS e = new EdgeDS();
                e.source = new Point(Integer.parseInt(bits[0]),Integer.parseInt(bits[1]));
                e.destination = new Point(Integer.parseInt(bits[2]),Integer.parseInt(bits[3]));
                edges.add(e);
                line = br.readLine();
            }
        }
        catch(Exception e)
        {
            System.out.println("Blah");
        }
        
    }
    
@Override
public void paint(Graphics g) {
    super.paint(g);
    for(EdgeDS e : edges)
    {
        g.drawLine(e.source.x*6+50, e.source.y*6+50, e.destination.x*6+50, e.destination.y*6+50);
    }
    g.setColor(Color.red);
    for(Point p : vertices)
    {
        g.fillRect(p.x*6+50, p.y*6+50, 10, 10);
    }
}

}

class EdgeDS
{
    Point source,destination;
}
