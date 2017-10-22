import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

public class Graph<T> {
  public static final int VISIT_COLOR_WHITE = 1;
  public static final int VISIT_COLOR_GREY = 2;
  public static final int VISIT_COLOR_BLACK = 3;

  public List<Vertex<T>> verticies;
  public List<Edge<T>> edges;
  private Vertex<T> rootVertex;

  public Graph() {
    verticies = new ArrayList<Vertex<T>>();
    edges = new ArrayList<Edge<T>>();
  }
  
  public ArrayList<String> bridgeword(Vertex<T> from,Vertex<T> to){
    ArrayList<String> store = new ArrayList<String>();
    for(int i = 0 ; i<edges.size() ; i++) {
      if(edges.get(i).getFrom() == from){
        for(int x = 0;x<edges.get(i).getTo().outgoingEdges.size();x++){
          Edge<T> bridge = edges.get(i).getTo().outgoingEdges.get(x);
          if(bridge.getTo() == to){
            store.add(bridge.getFrom().getName());
          }
        }
      }
    }
    return store;
  }


  public  final String rand_go(Vertex<T> start) {
  //zhushi
    String out = "";
    Vertex<T> now = start;
    final ArrayList<String> path = new ArrayList<String>();
    while(!now.outgoingEdges.isEmpty()){
       Random rand = new Random();
       Vertex<T> next  = now.outgoingEdges.get(rand.nextInt(now.outgoingEdges.size())).getTo();
       String tpath = now.getName()+" "+next.getName();
       path.add(tpath);
       now = next;
       out = out+tpath+"\n";
       System.out.println(tpath+"\n");
       if(Collections.frequency(path,tpath)>1){
         break;
       }
    }
    return out;
  }

  public boolean isEmpty() {
    return verticies.size() == 0;
  }
  public boolean addVertex(Vertex<T> v) {
    boolean added = false;
    if (verticies.contains(v) == false) {
      added = verticies.add(v);
    }
    return added;
  }
  public int size() {
    return verticies.size();
  }

  public Vertex<T> getRootVertex() {
    return rootVertex;
  }
  public void setRootVertex(Vertex<T> root) {
    this.rootVertex = root;
    if (verticies.contains(root) == false)
      this.addVertex(root);
  }
  public Vertex<T> getVertex(int n) {
    return verticies.get(n);
  }
  public List<Vertex<T>> getVerticies() {
    return this.verticies;
  }
  /*寮曞叆cost鏉冮噸,鍏佽杈圭殑鏉冮噸鍙犲姞,鍏堟壘鍒癴rom鐨勭偣鍜�*/
  public boolean addEdge(Vertex<T> from, Vertex<T> to, int cost) throws IllegalArgumentException {
    if (verticies.contains(from) == false)
      throw new IllegalArgumentException("from is not in graph");
    if (verticies.contains(to) == false)
      throw new IllegalArgumentException("to is not in graph");

    Edge<T> e = new Edge<T>(from, to, cost);
    if (from.findEdge(to) != null)
      return false;
    else {
      from.addEdge(e);
      to.addEdge(e);
      edges.add(e);
      return true;
    }
  }

  public boolean insertBiEdge(Vertex<T> from, Vertex<T> to, int cost)
      throws IllegalArgumentException {
    return addEdge(from, to, cost) && addEdge(to, from, cost);
  }
  public List<Edge<T>> getEdges() {
    return this.edges;
  }
  public boolean removeVertex(Vertex<T> v) {
    if (!verticies.contains(v))
      return false;

    verticies.remove(v);
    if (v == rootVertex)
      rootVertex = null;
    for (int n = 0; n < v.getOutgoingEdgeCount(); n++) {
      Edge<T> e = v.getOutgoingEdge(n);
      v.remove(e);
      Vertex<T> to = e.getTo();
      to.remove(e);
      edges.remove(e);
    }
    for (int n = 0; n < v.getIncomingEdgeCount(); n++) {
      Edge<T> e = v.getIncomingEdge(n);
      v.remove(e);
      Vertex<T> predecessor = e.getFrom();
      predecessor.remove(e);
    }
    return true;
  }
  public boolean removeEdge(Vertex<T> from, Vertex<T> to) {
    Edge<T> e = from.findEdge(to);
    if (e == null)
      return false;
    else {
      from.remove(e);
      to.remove(e);
      edges.remove(e);
      return true;
    }
  }

  public void clearMark() {
    for (Vertex<T> w : verticies)
      w.clearMark();
  }

  public void clearEdges() {
    for (Edge<T> e : edges)
      e.clearMark();
  }
  public void depthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
    VisitorEX<T, RuntimeException> wrapper = new VisitorEX<T, RuntimeException>() {
      public void visit(Graph<T> g, Vertex<T> v) throws RuntimeException {
        if (visitor != null)
          visitor.visit(g, v);
      }
    };
    this.depthFirstSearch(v, wrapper);
  }

  public <E extends Exception> void depthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor) throws E {
    if (visitor != null)
      visitor.visit(this, v);
    v.visit();
    for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
      Edge<T> e = v.getOutgoingEdge(i);
      if (!e.getTo().visited()) {
        depthFirstSearch(e.getTo(), visitor);
      }
    }
  }

  public void breadthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
    VisitorEX<T, RuntimeException> wrapper = new VisitorEX<T, RuntimeException>() {
      public void visit(Graph<T> g, Vertex<T> v) throws RuntimeException {
        if (visitor != null)
          visitor.visit(g, v);
      }
    };
    this.breadthFirstSearch(v, wrapper);
  }

  public <E extends Exception> void breadthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor)
      throws E {
    LinkedList<Vertex<T>> q = new LinkedList<Vertex<T>>();

    q.add(v);
    if (visitor != null)
      visitor.visit(this, v);
    v.visit();
    while (q.isEmpty() == false) {
      v = q.removeFirst();
      for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
        Edge<T> e = v.getOutgoingEdge(i);
        Vertex<T> to = e.getTo();
        if (!to.visited()) {
          q.add(to);
          if (visitor != null)
            visitor.visit(this, to);
          to.visit();
        }
      }
    }
  }

  /**
   * Find the spanning tree using a DFS starting from v.
   *
   * @param v -
   *          the vertex to start the search from
   * @param visitor -
   *          visitor invoked after each vertex is visited and an edge is added
   *          to the tree.
   */
  public void dfsSpanningTree(Vertex<T> v, DFSVisitor<T> visitor) {
    v.visit();
    if (visitor != null)
      visitor.visit(this, v);

    for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
      Edge<T> e = v.getOutgoingEdge(i);
      if (!e.getTo().visited()) {
        if (visitor != null)
          visitor.visit(this, v, e);
        e.mark();
        dfsSpanningTree(e.getTo(), visitor);
      }
    }
  }

  /**
   * Search the verticies for one with name.
   *
   * @param name -
   *          the vertex name
   * @return the first vertex with a matching name, null if no matches are found
   */
  public Vertex<T> findVertexByName(String name) {
    Vertex<T> match = null;
    for (Vertex<T> v : verticies) {
      if (name.equals(v.getName())) {
        match = v;
        break;
      }
    }
    return match;
  }
  public Vertex<T> findVertexByData(T data, Comparator<T> compare) {
    Vertex<T> match = null;
    for (Vertex<T> v : verticies) {
      if (compare.compare(data, v.getData()) == 0) {
        match = v;
        break;
      }
    }
    return match;
  }


  public Edge<T>[] findCycles() {
    ArrayList<Edge<T>> cycleEdges = new ArrayList<Edge<T>>();
    // Mark all verticies as white
    for (int n = 0; n < verticies.size(); n++) {
      Vertex<T> v = getVertex(n);
      v.setMarkState(VISIT_COLOR_WHITE);
    }
    for (int n = 0; n < verticies.size(); n++) {
      Vertex<T> v = getVertex(n);
      visit(v, cycleEdges);
    }

    Edge<T>[] cycles = new Edge[cycleEdges.size()];
    cycleEdges.toArray(cycles);
    return cycles;
  }

  private void visit(Vertex<T> v, ArrayList<Edge<T>> cycleEdges) {
    v.setMarkState(VISIT_COLOR_GREY);
    int count = v.getOutgoingEdgeCount();
    for (int n = 0; n < count; n++) {
      Edge<T> e = v.getOutgoingEdge(n);
      Vertex<T> u = e.getTo();
      if (u.getMarkState() == VISIT_COLOR_GREY) {
        // A cycle Edge<T>
        cycleEdges.add(e);
      } else if (u.getMarkState() == VISIT_COLOR_WHITE) {
        visit(u, cycleEdges);
      }
    }
    v.setMarkState(VISIT_COLOR_BLACK);
  }

  public String toString() {
    StringBuffer tmp = new StringBuffer("Graph[");
    for (Vertex<T> v : verticies)
      tmp.append(v);
    tmp.append(']');
    return tmp.toString();
  }

}
class Edge<T> {
  private Vertex<T> from;
  private Vertex<T> to;
  private int cost;
  private boolean mark;
  public Edge(Vertex<T> from, Vertex<T> to) {
    this(from, to, 0);
  }
  public Edge(Vertex<T> from, Vertex<T> to, int cost) {
    this.from = from;
    this.to = to;
    this.cost = cost;
    mark = false;
  }
  public Vertex<T> getTo() {
    return to;
  }
  public Vertex<T> getFrom() {
    return from;
  }
  public int getCost() {
    return cost;
  }
  public void mark() {
    mark = true;
  }
  public void clearMark() {
    mark = false;
  }

  /**
   * Get the edge mark flag
   *
   * @return edge mark flag
   */
  public boolean isMarked() {
    return mark;
  }

  /**
   * String rep of edge
   *
   * @return string rep with from/to vertex names and cost
   */
  public String toString() {
    StringBuffer tmp = new StringBuffer("Edge[from: ");
    tmp.append(from.getName());
    tmp.append(",to: ");
    tmp.append(to.getName());
    tmp.append(", cost: ");
    tmp.append(cost);
    tmp.append("]");
    return tmp.toString();
  }
}

class Vertex<T> {
  public List<Edge<T>> incomingEdges;

  public List<Edge<T>> outgoingEdges;

  private String name;

  private boolean mark;

  private int markState;

  private T data;

  /**
   * Calls this(null, null).
   */
  public Vertex() {
    this(null, null);
  }

  /**
   * Create a vertex with the given name and no data.
   *
   * @param n
   */
  public Vertex(String n) {
    this(n, null);
  }
  
  public Vertex(String n , T data) {
    incomingEdges = new ArrayList<Edge<T>>();
    outgoingEdges = new ArrayList<Edge<T>>();
    name = n;
    mark = false;
    this.data = data;
  }

  public String getName() {
    return name;
  }

  public T getData() {
    return this.data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public boolean addEdge(Edge<T> e) {
    if (e.getFrom() == this)
      outgoingEdges.add(e);
    else if (e.getTo() == this)
      incomingEdges.add(e);
    else
      return false;
    return true;
  }

  public void addOutgoingEdge(Vertex<T> to, int cost) {
    Edge<T> out = new Edge<T>(this, to, cost);
    outgoingEdges.add(out);
  }

  public void addIncomingEdge(Vertex<T> from, int cost) {
    Edge<T> out = new Edge<T>(this, from, cost);
    incomingEdges.add(out);
  }
  public boolean hasEdge(Edge<T> e) {
    if (e.getFrom() == this)
      return incomingEdges.contains(e);
    else if (e.getTo() == this)
      return outgoingEdges.contains(e);
    else
      return false;
  }

  public boolean remove(Edge<T> e) {
    if (e.getFrom() == this)
      incomingEdges.remove(e);
    else if (e.getTo() == this)
      outgoingEdges.remove(e);
    else
      return false;
    return true;
  }

  public int getIncomingEdgeCount() {
    return incomingEdges.size();
  }

  public Edge<T> getIncomingEdge(int i) {
    return incomingEdges.get(i);
  }

  public List getIncomingEdges() {
    return this.incomingEdges;
  }

  /**
   *
   * @return the count of incoming edges
   */
  public int getOutgoingEdgeCount() {
    return outgoingEdges.size();
  }

  /**
   * Get the ith outgoing edge
   *
   * @param i
   *          the index into outgoing edges
   * @return ith outgoing edge
   */
  public Edge<T> getOutgoingEdge(int i) {
    return outgoingEdges.get(i);
  }

  /**
   * Get the outgoing edges
   *
   * @return outgoing edge list
   */
  public List getOutgoingEdges() {
    return this.outgoingEdges;
  }
  public Edge<T> findEdge(Vertex<T> dest) {
    for (Edge<T> e : outgoingEdges) {
      if (e.getTo() == dest)
        return e;
    }
    return null;
  }
  public Edge<T> findEdge(Edge<T> e) {
    if (outgoingEdges.contains(e))
      return e;
    else
      return null;
  }
  public int cost(Vertex<T> dest) {
    if (dest == this)
      return 0;

    Edge<T> e = findEdge(dest);
    int cost = Integer.MAX_VALUE;
    if (e != null)
      cost = e.getCost();
    return cost;
  }
  public boolean hasEdge(Vertex<T> dest) {
    return (findEdge(dest) != null);
  }
  public boolean visited() {
    return mark;
  }
  public void mark() {
    mark = true;
  }
  public void setMarkState(int state) {
    markState = state;
  }
  public int getMarkState() {
    return markState;
  }
  public void visit() {
    mark();
  }
  public void clearMark() {
    mark = false;
  }
  public String toString() {
    StringBuffer tmp = new StringBuffer("Vertex(");
    tmp.append(name);
    tmp.append(", data=");
    tmp.append(data);
    tmp.append("), in:[");
    for (int i = 0; i < incomingEdges.size(); i++) {
      Edge<T> e = incomingEdges.get(i);
      if (i > 0)
        tmp.append(',');
      tmp.append('{');
      tmp.append(e.getFrom().name);
      tmp.append(',');
      tmp.append(e.getCost());
      tmp.append('}');
    }
    tmp.append("], out:[");
    for (int i = 0; i < outgoingEdges.size(); i++) {
      Edge<T> e = outgoingEdges.get(i);
      if (i > 0)
        tmp.append(',');
      tmp.append('{');
      tmp.append(e.getTo().name);
      tmp.append(',');
      tmp.append(e.getCost());
      tmp.append('}');
    }
    tmp.append(']');
    return tmp.toString();
  }
}
interface Visitor<T> {
  public void visit(Graph<T> g, Vertex<T> v);
}
interface VisitorEX<T, E extends Exception> {
  public void visit(Graph<T> g, Vertex<T> v) throws E;
}
interface DFSVisitor<T> {
  public void visit(Graph<T> g, Vertex<T> v);
  public void visit(Graph<T> g, Vertex<T> v, Edge<T> e);
}