
import java.io.PrintWriter;
import java.util.*;
import javax.xml.ws.EndpointContext;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rahulrdhanendran
 */

//This is a greedy algorithm that tries to satisfy the conditions
public class Algorithm1 {
    
    ArrayList<Point> points;
    int maxDegree, minCardinality;
    Graph g;
    
    public Algorithm1(int n)
    {
        g = new Graph();
        InputGenerator input = new InputGenerator();
        points = input.GetRandomPoints(n);
        maxDegree = 5;
        minCardinality = 3;
    }
    
    public void ConstructCompleteGraph()
    {
        for(int i=0; i<points.size() ; i++)
        {
            Vertex v = new Vertex();
            v.location = points.get(i);
            g.vertices.add(v);
        }
        
        for(int i=0; i<points.size() ; i++)
        {
            for(int j=0;j<points.size();j++)
            {
                if(i==j)
                    continue;
                
                Edge e = new Edge();
                e.distance = GetDistance(points.get(i), points.get(j));
                e.vertexA = g.vertices.get(i);
                e.vertexB = g.vertices.get(j);
                e.weight = 1;
                   
                
                if(!g.edges.contains(e))
                {
                    g.edges.add(e);
                    g.vertices.get(i).IncidentEdges.add(e);
                    g.vertices.get(j).IncidentEdges.add(e);
                }
                
                
                    
            }
        }
        //System.out.println("Complete graph constructed");
    }
    
    public int GetVertexIndex(Vertex a)
    {
        for(int i=0;i<g.vertices.size();i++)
        {
            if(g.vertices.get(i).equals(a))
                return i;
        }
        return 0;
    }
    
    public boolean checkCardinality()
    {
        //check cardinality
        for(Vertex v : g.vertices)
        {
            if(v.IncidentEdges.size()<3)
                return false;
        }
        return true;
    }
    
    public boolean checkDiameter()
    {
        
        for(int i=0;i<g.vertices.size();i++)
        {
            Graph tempGraph = new Graph();
                tempGraph.edges = new ArrayList<>(g.edges);
                tempGraph.vertices = new ArrayList<>(g.vertices);
                Iterator it = tempGraph.vertices.iterator();
                while(it.hasNext())
                {
                    Vertex v = (Vertex)it.next();
                    v.minDistance = Double.POSITIVE_INFINITY;
                    v.previous = null;
                }
                tempGraph.computePaths(tempGraph.vertices.get(i));
                
            for(int j=0;j<g.vertices.size();j++)
            {
                if(i==j)
                    continue;
                List<Vertex> path = tempGraph.getShortestPathTo(tempGraph.vertices.get(j));
                if(path.get(path.size()-1).minDistance>4 || path.get(path.size()-1).minDistance<1)
                {
                    /*for(Vertex x : path)
                        System.out.print(x+"=>");
                    System.out.println("");
                    */
                    return false;
                }
                
            }
            
            
        }
        return true;
    }
    
    //constructs a complete graph
    public void ConstructGraph()
    {
        ConstructCompleteGraph();
        //Prune the graph
        
        Collections.sort(g.edges, new EdgeComparator());
        Iterator it = g.edges.iterator();
        while(it.hasNext())
        {
            Edge e = (Edge)it.next();
            
            int SourceIndex = GetVertexIndex(e.vertexA);
            int DestIndex = GetVertexIndex(e.vertexB);
            g.vertices.get(SourceIndex).IncidentEdges.remove(e);
            g.vertices.get(DestIndex).IncidentEdges.remove(e);
            
            if(!checkCardinality())
            {
                //add edge back to graph
                g.vertices.get(SourceIndex).IncidentEdges.add(e);
                g.vertices.get(DestIndex).IncidentEdges.add(e);
                continue;
            }
            
            if(!checkDiameter())
            {
                g.vertices.get(SourceIndex).IncidentEdges.add(e);
                g.vertices.get(DestIndex).IncidentEdges.add(e);
                continue;
            }
            it.remove();
        }
        
        
        WriteToFiles();
    }
    
    /*
    
    public void ConstructGraph()
    {
        //Find "maxDegree" nearest points from first point
        ArrayList<Point> remaining = new ArrayList<>(points);
        Queue<Point> unExplored = new LinkedList<>();
        Vertex v = new Vertex();
        v.location = points.get(0);
        g.vertices.add(v);
        unExplored.add(remaining.get(0));
        remaining.remove(0);
        while(remaining.size()>0)
        {
            Point source = unExplored.remove();
            ArrayList<Point> neighbours = GetNearestPoints(source, remaining.size()==points.size()-1?maxDegree:maxDegree-1, remaining);
            for(Point temp : neighbours)
            {
                unExplored.add(temp);
                
                Vertex v1 = new Vertex();
                v1.location = temp;
                g.vertices.add(v1);
                
                Edge e = new Edge();
                e.distance = GetDistance(source, temp);
                e.vertexA = g.getVertex(source);
                e.vertexB = v1;
                
                g.edges.add(e);
                
                Vertex SourceVertex = g.getVertex(source);
                SourceVertex.IncidentEdges.add(e);
                
                Vertex DestVertex = g.getVertex(temp);
                DestVertex.IncidentEdges.add(e);
            }
            
            for(Point temp : neighbours)
                remaining.remove(temp);
        }
        System.out.println("Graph generated");
        WriteToFiles();
    }
    */
    
    public void WriteToFiles()
    {
        try
        {
            PrintWriter writer = new PrintWriter("Vertices.txt", "UTF-8");
            Iterator it = g.vertices.iterator();
            while(it.hasNext())
            {
                Vertex v = (Vertex)it.next();
                writer.println(v.location.x+"\t"+v.location.y);
            }
            writer.close();
            
            writer = new PrintWriter("Edges.txt","UTF-8");
            it = g.edges.iterator();
            while(it.hasNext())
            {
                Edge e = (Edge)it.next();
                writer.println(e.vertexA.location.x+"\t"+e.vertexA.location.y+"\t"+e.vertexB.location.x+"\t"+e.vertexB.location.y);
            }
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("Blah");
        }
    }
    
    public ArrayList<Point> GetNearestPoints(Point p, int number, ArrayList<Point> remPoints)
    {
        ArrayList<Point> neighbours = new ArrayList<>();
        ArrayList<PointDistance> allNeighbours = new ArrayList<>();
        for(Point temp : remPoints)
        {
            double dist = GetDistance(p,temp);
            PointDistance pd = new PointDistance(temp,dist);
            allNeighbours.add(pd);
        }
        Collections.sort(allNeighbours, new CustomComparator());
        int index = 0;
        if(allNeighbours.size()>=number)
            index = number;
        else
            index = allNeighbours.size();
        
        for(int i=0;i<index;i++)
            neighbours.add(allNeighbours.get(i).p);
        
        return neighbours;
    }
    
    public double getCost()
    {
        double cost = 0;
        for(Edge e : g.edges)
        {
            cost += e.distance;
        }
        return cost;
    }
    
    
    public Double GetDistance(Point p1, Point p2)
    {
        double distance = Double.POSITIVE_INFINITY;
        distance = Math.sqrt(((p2.x - p1.x)*(p2.x - p1.x))+((p2.y - p1.y)*((p2.y - p1.y))));
        return distance;
    }
    
    /*
    public static void main(String[] args) {
        Algorithm1 a1 = new Algorithm1(30);
        a1.ConstructGraph();
        
        System.out.println("Algorithm 1 Minimum Cost : "+a1.getCost());
        GUIGraph gui = new GUIGraph();
        
    }*/
    
}







class PointDistance
{
    Point p;
    double distance;
    public PointDistance(Point p, double d)
    {
        this.p = p;
        distance = d;
    }
}

class CustomComparator implements Comparator<PointDistance>
{

    @Override
    public int compare(PointDistance o1, PointDistance o2) {
        return (int)(o1.distance-o2.distance);
    }
    
}

class EdgeComparator implements Comparator<Edge>
{
    
    @Override
    public int compare(Edge e1, Edge e2)
    {
        return (int)(e2.distance-e1.distance);
    }
}