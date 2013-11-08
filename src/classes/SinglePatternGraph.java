package classes;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.JFrame;
import org.jgraph.JGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
public class SinglePatternGraph
{
    // Contains all pattern candidates of a meta pattern
    private LinkedList<Pattern> patternCandidates;
    
    // Our class contains a real graph, which is not visible
    private SimpleDirectedGraph<String, DefaultEdge> internGraph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
    private SimpleDirectedGraph<String, DefaultEdge> symGraph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
    
    // This value will be set to all Pattern in this Graph
    private double M1Value;
    private double M2Value;
    private double M3Value;
    
    // Constructor
    public SinglePatternGraph(LinkedList<Pattern> patternCandidates)
    {
        this.patternCandidates = patternCandidates;
        buildGraph();
    }
    
    //fills the graph with nodes and edges, using the patternCandidates
    public void buildGraph()
    {
        // we need to traverse through all Pattern Candidates and add the Strings
        for (Pattern currentPattern: patternCandidates)
        {
            for(int i = 0; i < currentPattern.pattern.size()-1; i++)
            {
                // TODO Make it dynamic, not static
                String left = currentPattern.pattern.get(i);
                String right = currentPattern.pattern.get(i+1);
            
                if(left.equals(right))
                {
                    internGraph.addVertex(left);
                }
                else
                {
                    internGraph.addVertex(left);
                    internGraph.addVertex(right);
                    internGraph.addEdge(left, right);
                }
            }
        }
        buildSymGraph();
        calculateM1Value();
        calculateM2Value();
        calculateM3Value();
        sendMeasurementsToPattern();
        
        // watch the graph beeing build ^^
        createWindow(internGraph);
    }
    
    // use this methode to activate a visualization of the graph
    public void createWindow(SimpleDirectedGraph<String, DefaultEdge> graph)
    {
        // JFrame can use JGraphs only, not SimpleDirectedGraphs. So we need to create a JGraph, using the internGraph as source
        JGraph jgraph = new JGraph(new JGraphModelAdapter<String, DefaultEdge>(graph));
        
        JFrame window = new JFrame();
        
        window.getContentPane().add(jgraph);
        
        // Modify this options as you want to
        window.setSize(800, 800);
        window.setLocation(200,200);
        window.setVisible(true);
        
        return;
    }
    
    
    public void buildSymGraph()
    {
        // exactly the same Class --> Safe
        symGraph = (SimpleDirectedGraph<String, DefaultEdge>)internGraph.clone();
        
        // this will delete all edges, that are not bidirectional
        for (Pattern currentPattern: patternCandidates)
        {
            for(int i = 0; i < currentPattern.pattern.size()-1; i++)
            {
                // TODO Make it dynamic, not static
                String left = currentPattern.pattern.get(i);
                String right = currentPattern.pattern.get(i+1);
            
                if(!left.equals(right))
                {
                    if(!symGraph.containsEdge(right, left))
                    {
                        symGraph.removeEdge(left, right);
                    }
                }
            }
        }
    }
    // Each meta pattern gains a value M1, M2 and M3
    // M1 = |{x|Ex(y) A(x,y) ^ Ex(z) A(z,x)}|/|Nodes(G(P))|
    public void calculateM1Value()
    {
        int symmetrie_counter = 0;
        int vertex_counter = 0;
        
        // an iterator class, useable for directed graphs
        BreadthFirstIterator<String, DefaultEdge> iterator = new BreadthFirstIterator<String, DefaultEdge>(internGraph);
        
        // will visit every node of the Graph and check if its symmetric
        do
        {
            String vertex = iterator.next();
            vertex_counter++;
            int incomming = internGraph.inDegreeOf(vertex);
            int outgoing = internGraph.outDegreeOf(vertex);
            
            if(outgoing > 0 && incomming > 0) symmetrie_counter++;
        }while(iterator.hasNext());
        M1Value = ((double)(symmetrie_counter))/(double)vertex_counter;        
    }
    
    // can be used, after the function buildSymGraph was called
    // M2 = |Nodes(SymG(P))|/|Nodes(G(P))|
    public void calculateM2Value()
    {
        BreadthFirstIterator<String, DefaultEdge> iterator = new BreadthFirstIterator<String, DefaultEdge>(symGraph);
        
        // counts all nodes that have a degree != 0 (it means that they have a bidirectional edge)
        int symmetric_node_counter = 0;
        int node_counter = 0;
        
        while(iterator.hasNext())
        {
            String vertex = iterator.next();
            node_counter++;
            
            // because it has to be bidirectional it's enought if we check for one direction
            if(symGraph.inDegreeOf(vertex) != 0) symmetric_node_counter++;
        }
        
        M2Value = (double)symmetric_node_counter/(double)node_counter;        
    }
    
    // can be used after buildSymGraph() was called
    // M3 = |Arcs(SymG(P))| / |Arcs(G(P))|
    public void calculateM3Value()
    {
        Set<DefaultEdge> symGraphEdges = symGraph.edgeSet();
        int edgesCountSymGraph = symGraphEdges.size();
        
        Set<DefaultEdge> graphEdges = internGraph.edgeSet();
        int edgesCountGraph = graphEdges.size();
        
        M3Value = (double)edgesCountSymGraph/(double)edgesCountGraph;
    }
    
    // can be used after setM1Values() calculated M1. This methode will now set M1 for all Pattern.
    public void sendMeasurementsToPattern()
    {
        for (Pattern currentPattern : patternCandidates)
        {
            currentPattern.setM1_Value(M1Value);
            currentPattern.setM2_Value(M2Value);
            currentPattern.setM3_Value(M3Value);
        }
    }
}