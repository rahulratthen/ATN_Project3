
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
    int maxDegree;
    Graph g;
    
    public Algorithm1(int n)
    {
        g = new Graph();
        g.vertices = new ArrayList<>();
        g.edges = new ArrayList<>();
        InputGenerator input = new InputGenerator();
        points = input.GetRandomPoints(n);
        if(n%5==0)
            maxDegree = n/5;
        else
            maxDegree = (n/5)+1;
    }
    
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
                e.weight = GetDistance(source, temp);
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
            cost += e.weight;
        }
        return cost;
    }
    
    
    public Double GetDistance(Point p1, Point p2)
    {
        double distance = Double.POSITIVE_INFINITY;
        distance = Math.sqrt(((p2.x - p1.x)*(p2.x - p1.x))+((p2.y - p1.y)*((p2.y - p1.y))));
        return distance;
    }
    
    public static void main(String[] args) {
        Algorithm1 a1 = new Algorithm1(20);
        a1.ConstructGraph();
        
        System.out.println("Cost of the network topology : "+a1.getCost());
    }
    
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
