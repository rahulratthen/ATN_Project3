
import java.util.ArrayList;

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
    
    public int getDiameter()
    {
        return 0;
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
}

class Vertex
{
    Point location;
    ArrayList<Edge> IncidentEdges;
    
    public Vertex()
    {
        IncidentEdges = new ArrayList<>();
    }
    
    public int getDegree()
    {
        return IncidentEdges.size();
    }
}

class Edge
{
    Vertex vertexA,vertexB;
    double weight;
    
    @Override
    public boolean equals(Object o) {
        Edge o1 = (Edge) o;
        if((o1.vertexA==this.vertexA && o1.vertexB==this.vertexB) || (o1.vertexA==this.vertexB && o1.vertexB==this.vertexA) )
            return true;
            
    return false;
    }
    
    @Override
    public int hashCode(){
        return 1;
    }
}