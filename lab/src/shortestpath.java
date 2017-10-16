import java.util.*;

public class shortestpath<T>{
private Set<Vertex<T>> settled;
private Set<Vertex<T>> unsettled;
private Map<Vertex<T>, Vertex<T>> pre;
private Map<Vertex<T>, Integer> distance;
private List<Edge<T>> edges;
  public shortestpath(Vertex<T> begin,Graph<T> gg){
    execute(begin);
    edges = gg.edges;
  }
  public LinkedList<Vertex<T>> getpath(Vertex<T> start){
  LinkedList<Vertex<T>> path = new LinkedList<Vertex<T>>();
  Vertex<T> step = start;
  // check if a path exists
  if (pre.get(step) == null) {
      return null;
  }
  path.add(step);
  while (pre.get(step) != null) {
      step = pre.get(step);
      path.add(step);
  }
  // Put it into the correct order
  Collections.reverse(path);
  return path;
  }
  public int getsd(Vertex<T> to){
    Integer d = distance.get(to);
    if(d == null){
      return Integer.MAX_VALUE;
    }else{
      return d;
    }
  }
  public Vertex<T> getMIn(Set<Vertex<T>> s){
    Vertex<T> min = null;
    for (Vertex<T> vv:s){
      if(min == null){
        min = vv;
      }
      else{
        if(getsd(vv)<getsd(min)){
          min = vv;
        }
      }
    }
  return min;
  }
  public boolean isSettled(Vertex<T> node){
    return settled.contains(node);
  }
  public List<Vertex<T>> getNber(Vertex<T> node){
    List<Vertex<T>> Nber = new ArrayList<Vertex<T>>();
    for (Edge<T> temp:node.outgoingEdges){
      if(temp.getFrom() == node && !isSettled(temp.getTo())){
        Nber.add(temp.getTo());
      }
    }
    return Nber;
  }
  public int getd(Vertex<T> from, Vertex<T> to){
    for(Edge<T> temp:from.outgoingEdges){
      if(temp.getTo() == to){
          return temp.getCost();
      }
    }
    throw new RuntimeException("gg");
  }
  public void findmin(Vertex<T> node){
    List<Vertex<T>> nn = getNber(node);
    for(Vertex<T> target:nn){
      if(getsd(target)>getsd(node)+getd(node,target)){
        distance.put(target,getsd(node)+getd(node,target));
        pre.put(target,node);
        unsettled.add(target);
      }
    }
  }
  public void execute(Vertex<T> s){
    settled = new HashSet<Vertex<T>>();
    unsettled = new HashSet<Vertex<T>>();
    distance = new HashMap<Vertex<T>,Integer>();
    pre = new HashMap<Vertex<T>,Vertex<T>>();
    distance.put(s,0);
    unsettled.add(s);
    while(unsettled.size()>0){
      Vertex<T> node = getMIn(unsettled);
      settled.add(node);
      unsettled.remove(node);
      findmin(node);
    }
  }
}