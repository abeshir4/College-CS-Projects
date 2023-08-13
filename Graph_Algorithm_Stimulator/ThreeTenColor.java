//TODO: Implement the required methods and add JavaDoc for them.
//Remember: Do NOT add any additional instance or class variables (local variables are ok)
//and do NOT alter any provided methods or change any method signatures!

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.awt.Color;

import javax.swing.JPanel;

import java.util.Collection;
import java.util.NoSuchElementException;

import java.util.LinkedList;

/**
 *  Simulation of our coloring algorithm.
 *
 */
class ThreeTenColor implements ThreeTenAlg {
    /**
     *  The graph the algorithm will run on.
     */
    Graph<GraphNode, GraphEdge> graph;

    /**
     *  The priority queue of nodes for the algorithm.
     */
    WeissPriorityQueue<GraphNode> queue;

    /**
     *  The stack of nodes for the algorithm.
     */
    LinkedList<GraphNode> stack;

    /**
     *  Whether or not the algorithm has been started.
     */
    private boolean started = false;

    /**
     *  Whether or not the algorithm is in the coloring stage or not.
     */
    private boolean coloring = false;

    /**
     *  The color when a node has "no color".
     */
    public static final Color COLOR_NONE_NODE = Color.WHITE;

    /**
     *  The color when an edge has "no color".
     */
    public static final Color COLOR_NONE_EDGE = Color.BLACK;

    /**
     *  The color when a node is inactive.
     */
    public static final Color COLOR_INACTIVE_NODE = Color.LIGHT_GRAY;

    /**
     *  The color when an edge is inactive.
     */
    public static final Color COLOR_INACTIVE_EDGE = Color.LIGHT_GRAY;

    /**
     *  The color when a node is highlighted.
     */
    public static final Color COLOR_HIGHLIGHT = new Color(255,204,51);

    /**
     *  The color when a node is in warning.
     */
    public static final Color COLOR_WARNING = new Color(255,51,51);


    /**
     *  The colors used to assign to nodes.
     */
    public static final Color[] COLORS = {Color.PINK, Color.GREEN, Color.CYAN, Color.ORANGE, Color.MAGENTA, Color.YELLOW, Color.DARK_GRAY, Color.BLUE};

    /**
     *  {@inheritDoc}
     */
    public EdgeType graphEdgeType() {
        return EdgeType.UNDIRECTED;
    }

    /**
     *  {@inheritDoc}
     */
    public void reset(Graph<GraphNode, GraphEdge> graph) {
        this.graph = graph;
        started = false;
        coloring = false;
    }

    /**
     *  {@inheritDoc}
     */
    public boolean isStarted() {
        return started;
    }

    /**
     *  {@inheritDoc}
     */
    public void start() {
        this.started = true;

        //create an empty stack
        stack = new LinkedList<>();

        //create an empty priority queue
        queue = new WeissPriorityQueue<>();

        for(GraphNode v : graph.getVertices()) {

            //Set the cost of each node to be its degree
            v.setCost(graph.degree(v));

            //Set each node to be active
            //This enables the display of cost for the node
            v.setActive();

            //add node into queue
            queue.add(v);
        }

        //highlight the current node with max priority
        highlightNextMax();

    }

    /**
     *  {@inheritDoc}
     */
    public void finish() {

        // Coloring completed. Set all edges back to "no color".
        for (GraphEdge e: graph.getEdges()){
            e.setColor(COLOR_NONE_EDGE);
        }

    }

    /**
     *  {@inheritDoc}
     */
    public void cleanUpLastStep() {
        // Unused. Required by the interface.
    }

    /**
     *  {@inheritDoc}
     */
    public boolean setupNextStep() {

        // Whole algorithm done.
        if (coloring && stack.size() == 0)
            return false;

        // First stage done when all nodes are pushed into stack.
        // Change the flag to start the coloring stage.
        if (!coloring && graph.getVertexCount() == stack.size()){
            coloring = true;
        }

        //Return true to indicate more steps to continue.
        return true;
    }

    /**
     *  {@inheritDoc}
     */
    public void doNextStep() {

        if (!coloring){
            //Stage 1: pushing nodes into stack one by one & update record

            // maxNode is the active node with the highest priority
            // Remove the maxNode from priority queue and push it into stack
            GraphNode maxNode = findMax();

            //Update the cost of all nodes that is a neighbor of the maxNode
            updateNeighborCost(maxNode);

            //Identify and highlight the next max node in the updated priority queue
            highlightNextMax();


        }
        else{
            //Stage 2: pop nodes from stack one by one and choose a color for each

            //Pop off stack top
            GraphNode node = stack.pop();

            //For the node popped off, pick a color that is different from all
            //neighbors who has got assigned a color so far
            Color newColor = chooseColor(node);

            //Inform all neighbors of this node the selected color
            updateColor(node, newColor);

        }

    }

    //----------------------------------------------------
    // TODO: Implement the methods below to complete the coloring algorithm.
    // - DO NOT change the signature of any required public method;
    // - Feel free to define additional method but they must be private.
    //
    // YOUR CODE HERE
    //----------------------------------------------------

    /**
     * Sets the current max node in the priority queue to the higlight color.
     */
    public void highlightNextMax(){

        // Find the current max node in the priority queue
        // and change the color of the node to be COLOR_HIGHLIGHT.
        // Note: do not dequeue the node.
        queue.element().setColor(COLOR_HIGHLIGHT);

    }

    /**
     * Looks for the max node.
     * @return the max node
     */
    public GraphNode findMax(){

        // 1. Remove the node with the max priority from the priority queue;
        //
        // Note: the max node should be the one with the highest cost
        // (i.e max number of active neighbors).  If there is a tie in cost,
        // the one with the lowest ID should be selected.
        // Hint: if your priority queue has been implemented correctly, this
        // should be straightforward.
        //
        // 2. Push the max node into stack
        // Note: Take a look at the JavaDoc of LinkedList to determine which method
        // to use, especially the interface Deque.
        //
        // 3. Set max node to be inactive and change its color to be COLOR_INACTIVE_NODE;
        //
        // 4. Set the color of all incident edges of max node to be COLOR_INACTIVE_EDGE.
        //
        // Return the max node.
        // - If queue is empty, return null.
        if(queue==null){
            return null;
        }
        LinkedList<Integer> neighcount = new LinkedList<>();
        LinkedList<GraphNode> verts = new LinkedList<>();
        GraphNode maxpri = null;
        for (GraphNode vert: graph.getVertices()){
            if(vert.isActive()){
                neighcount.add(graph.getNeighborCount(vert));
                verts.add(vert);
            }
        }
        int nodeloc = 0;
        int maxi = neighcount.get(0);
        for(int i = 1; i < neighcount.size(); i++){
            if(neighcount.get(i)>maxi){
                maxi = neighcount.get(i);
                nodeloc = i;
            }
        }
        //int nodeloc = 0;
        maxpri = verts.get(nodeloc);
        for(int i = 0; i < neighcount.size(); i++){
            if(i==nodeloc){
                continue;
            }
            else if(neighcount.get(i).equals(maxi)){
                if(verts.get(i).id < verts.get(nodeloc).id){
                    maxpri = verts.get(i);
                }
            }
        }
        queue.remove(maxpri);
        stack.add(maxpri);
        maxpri.setColor(COLOR_INACTIVE_NODE);
        for (GraphEdge ed: graph.getIncidentEdges(maxpri)){
            ed.setColor(COLOR_INACTIVE_EDGE);
        }
        return maxpri;
    }

    /**
     * Update the cost for all active neighbors of maxNode.
     * @param maxNode The node we'll be looking at
     */

    public void updateNeighborCost(GraphNode maxNode){
        // Update the cost for all active neighbors of maxNode.
        // Note that the cost of a node is equal to the number of its *active* neighbors.
        for (GraphNode vert: graph.getNeighbors(maxNode)) {
            if (vert.isActive()) {
                int nodecost = 0;
                for(GraphNode ver: graph.getNeighbors(vert)) {
                    if (ver.isActive()) {
                        nodecost++;
                    }
                }
                vert.setCost(nodecost);
            }
        }

    }

    /**
     * Picks a color for a node based on certain criteria.
     * @param node The node we're working with
     * @return The color chosen
     */
    public Color chooseColor(GraphNode node){

        // Pick a color for node based on the following criteria:
        // 1. The color is one of the listed colors in ThreeTenColor.COLORS;
        // 2. The color has not been assigned to any of its neighbors, and
        // 3. The color has the lowest index in array ThreeTenColor.COLORS in all
        //    colors that satisfy condition 2.
        //
        // After a color is selected, inform all neighbors of node that this color
        // is in use.  Hint: You will need to update nbrColors flag for the neighbors.
        //
        // Return the selected color.
        // - If we fail to find a color that satisfy all three conditions above,
        //  return COLOR_WARNING and no need to update the neighbors.
        // - If node is null, return null.
        //
        if(node==null){
            return null;
        }
        LinkedList<Integer> invalid = new LinkedList<>();
        LinkedList<Integer> valid = new LinkedList<>();
        for(GraphNode nd: graph.getNeighbors(node)){
            for(int i = 0; i < ThreeTenColor.COLORS.length; i++){
                if(nd.getColor().equals(COLORS[i])){
                    if(!invalid.contains(i)){
                        invalid.add(i);
                    }
                    else{
                        ;
                    }
                }
            }
        }
        for(int i = 0; i < ThreeTenColor.COLORS.length; i++){
            if(!invalid.contains(i)){
                valid.add(i);
            }
        }
        if(valid.size()!=0){
            for(GraphNode nd: graph.getNeighbors(node)){
                nd.setNbrColor(valid.get(0));
            }
            //node.setColor(COLORS[valid.get(0)]);
            return COLORS[valid.get(0)];
        }
        else{
            return COLOR_WARNING;
        }
    }

    /**
     * Set the color of a node to be newColor.
     * @param node The node we're working with
     * @param newColor The color we'll be setting the node to
     */
    public void updateColor(GraphNode node, Color newColor){

        // As part of the 2nd stage, set the color of node to be newColor.
        // Also, for any edge incident to node, if the edge has not assigned a color
        // in Stage 2, color it with newColor.
        //
        // No change should be made if either node or newColor is null.
        if(node==null||newColor==null){
            ;
        }
        else{
            node.setColor(newColor);
            for(GraphEdge ed: graph.getIncidentEdges(node)){
                int allcolors = 0;
                for(int i = 0; i < ThreeTenColor.COLORS.length; i++){
                    if(ed.getColor().equals(COLORS[i])){
                        allcolors++;
                        break;
                    }
                }
                if(allcolors==0){
                    ed.setColor(newColor);
                }
                else if(allcolors==1){
                    ;
                }
            }
        }

    }

}

