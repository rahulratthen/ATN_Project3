
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

//This algorithm implements genetic algorithm
public class Algorithm2 {
    
    int nodes;
    ArrayList<Point> points;
    int maxDegree, minCardinality;
    
    public Algorithm2(int n)
    {
        nodes = n;
        InputGenerator input = new InputGenerator();
        points = input.GetRandomPoints(n);
        maxDegree = 5;
        minCardinality = 3;
    }
    
    public int[][] getZeroAdjacencyMatrix()
    {
        int [][]matrix = new int[nodes][nodes];
        for(int i=0;i<nodes;i++)
        {
            for(int j=0;j<nodes;j++)
            {
                matrix[i][j] = 0;
            }
        }
        return matrix;
    }
    
    public boolean checkCardinality(Graph g)
    {
        //check cardinality
        for(Vertex v : g.vertices)
        {
            if(v.getDegree()<3)
                return false;
        }
        return true;
    }
    
    public boolean checkDiameter(Graph g)
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
    
    public void execute()
    {
        //Create an arraylist of 100 flipped adjacency matrices...level 1
        ArrayList<int[][]> initialMatrices = new ArrayList<>();
        for(int i=0;i<100;i++)
        {
            int [][]matrix = getZeroAdjacencyMatrix();
            int count = getRandomFlipCount();
            for(int j=0;j<count;j++)
            {
                int iIndex = getRandomMatixIndex();
                int jIndex = getRandomMatixIndex();
                matrix[iIndex][jIndex] = 1;
            }
            initialMatrices.add(matrix);
        }
        
        int size = initialMatrices.size();
        double minCost = Double.POSITIVE_INFINITY;
        Graph minGraph = null;
        //Generate and validate graphs according to the conditions
        while(size>0)
        {
            Iterator it = initialMatrices.iterator();
            while(it.hasNext())
            {
                int [][]matrix = (int[][])it.next();
                //convert to graph
                Graph g = generateGraph(matrix);
                if(!(checkCardinality(g) && checkDiameter(g)))
                {
                    it.remove();
                    if(minCost == Double.POSITIVE_INFINITY)
                        minCost = getCost(g);
                }
                else
                {
                    double cost = getCost(g);
                    
                    if(minCost > cost)
                    {
                        minCost = cost;
                        minGraph = g;
                        //System.out.println("Min cost : "+minCost);
                    }
                    else
                    {
                        it.remove();
                    }
                }
            }
            
            size = initialMatrices.size();
            
            //flip k random indices of adjacency matrix
            if(size>0)
            {
                for(int i=0;i<size;i++)
                {
                    for(int j=0;j<getRandomFlipCount();j++)
                    {
                        int iIndex = getRandomMatixIndex();
                        int jIndex = getRandomMatixIndex();
                        if(initialMatrices.get(i)[iIndex][jIndex] == 1)
                        {
                            initialMatrices.get(i)[iIndex][jIndex] = 0;
                        }
                        else
                            initialMatrices.get(i)[iIndex][jIndex] = 1;
                    }
                }
            }
        }
        
        System.out.println("Algorithm 2 Minimum Cost : "+minCost);
        WriteToFiles(minGraph);
        
    }
    
    public void WriteToFiles(Graph g)
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
    
    public double getCost(Graph g)
    {
        double cost = 0;
        for(Edge e : g.edges)
        {
            cost += e.distance;
        }
        return cost;
    }
    
    public Graph generateGraph(int [][]adj)
    {
        Graph g = new Graph();
        //add vertices
        for(int i=0;i<nodes;i++)
        {
            Vertex v = new Vertex();
            v.location = new Point(points.get(i).x,points.get(i).y);
            
            g.vertices.add(v);
        }
        
        //add edges and adjacency lists
        for(int i=0;i<nodes;i++)
        {
            for(int j=0;j<nodes;j++)
            {
                if(adj[i][j]==1)
                {
                    Edge e = new Edge();
                    e.vertexA = g.vertices.get(i);
                    e.vertexB = g.vertices.get(j);
                    e.weight = 1;
                    e.distance = GetDistance(points.get(i),points.get(j));
                    if(!g.edges.contains(e))
                    {
                        g.edges.add(e);
                        g.vertices.get(i).IncidentEdges.add(e);
                        g.vertices.get(j).IncidentEdges.add(e);
                    }
                }
            }
        }
        
        return g;
    }
    
    public int getRandomFlipCount()
    {
        Random rand = new Random();
        int index = rand.nextInt(190);
        
        return index;
    }
    
    public int getRandomMatixIndex()
    {
        Random rand = new Random();
        int index = rand.nextInt(nodes);
        return index;
    }
    
    public Double GetDistance(Point p1, Point p2)
    {
        double distance = Double.POSITIVE_INFINITY;
        distance = Math.sqrt(((p2.x - p1.x)*(p2.x - p1.x))+((p2.y - p1.y)*((p2.y - p1.y))));
        return distance;
    }
    
    public static void main(String[] args) {
        Algorithm2 a2 = new Algorithm2(20);
        a2.execute();
    }
}
