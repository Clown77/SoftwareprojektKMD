package classes;
import java.util.LinkedList;
import javax.swing.JFrame;
import org.jgraph.JGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
public class SinglePatternGraph
{
    // Contains all pattern candidates of a meta pattern
    private LinkedList<Pattern> patternCandidates;
    
    // Our class contains a real graph, which is not visible
    private SimpleDirectedGraph<String, DefaultEdge> internGraph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
    
    // this JFrame is used to visualize the graph
    private JFrame window = new JFrame();
    
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
        // watch the graph beeing build ^^
        JFrame window = createWindow();
    }
    
    // use this methode to activate a visualization of the graph
    public JFrame createWindow()
    {
        // JFrame can use JGraphs only, not SimpleDirectedGraphs. So we need to create a JGraph, using the internGraph as source
        JGraph jgraph = new JGraph(new JGraphModelAdapter<String, DefaultEdge>(internGraph));
        
        window.getContentPane().add(jgraph);
        
        // Modify this options as you want to
        window.setSize(800, 800);
        window.setLocation(200,200);
        window.setVisible(true);
        
        return window;
    }
}