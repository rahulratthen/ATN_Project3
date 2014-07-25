
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rahulrdhanendran
 */

//This class will hold information about the generated graph
public class Graph {
    ArrayList<Vertex> vertices;
    ArrayList<Edge> edges;
    
    public Graph()
    {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        
    }
        
    public Vertex getVertex(Point p)
    {
        for(Vertex temp : vertices)
        {
            if(temp.location.x == p.x && temp.location.y == p.y)
                return temp;
        }
        return null;
    }
    
    public void computePaths(Vertex source)
    {
        source.minDistance = 0;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
      	vertexQueue.add(source);

	while (!vertexQueue.isEmpty()) {
	    Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u.IncidentEdges)
            {
                Vertex v;
                if(e.vertexA.equals(u))
                    v = e.vertexB;
                else
                    v = e.vertexA;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
		if (distanceThroughU < v.minDistance) {
		    vertexQueue.remove(v);
		    v.minDistance = distanceThroughU ;
		    v.previous = u;
		    vertexQueue.add(v);
		}
            }
        }
        
       int c=9; 
    }

    public List<Vertex> getShortestPathTo(Vertex target)
    {
        List<Vertex> path = new ArrayList<>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
}

class Vertex implements Comparable<Vertex>
{
    Point location;
    ArrayList<Edge> IncidentEdges;
    double minDistance = Double.POSITIVE_INFINITY;
    Vertex previous;
    
    public Vertex()
    {
        IncidentEdges = new ArrayList<>();
    }
    
    public int getDegree()
    {
        return IncidentEdges.size();
    }
    
    @Override
    public boolean equals(Object o)
    {
        Vertex o1 = (Vertex)o;
        if(o1.location.x == this.location.x && o1.location.y == this.location.y)
            return true;
        return false;
    }
    
    @Override
    public int hashCode(){
        return 1;
    }
    
    @Override
    public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}

class Edge
{
    Vertex vertexA,vertexB;
    double distance;
    int weight=1;
    
    @Override
    public boolean equals(Object o) {
        Edge o1 = (Edge) o;
        
        if((((o1.vertexA.location.x == this.vertexA.location.x)&&(o1.vertexA.location.y == this.vertexA.location.y))&&
                ((this.vertexB.location.x==o1.vertexB.location.x)&&(this.vertexB.location.y == o1.vertexB.location.y)))
                ||
                ((this.vertexA.location.x==o1.vertexB.location.x)&&(this.vertexA.location.y == o1.vertexB.location.y))&&
                ((this.vertexB.location.x == o1.vertexA.location.x)&&(this.vertexB.location.y == o1.vertexA.location.y)))
            return true;
                
            
    return false;
    }
    
    @Override
    public int hashCode(){
        return 1;
    }
}