package srcs.workflow.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing a graph node containing a data of type T
 * 
 * @author check-leo
 *
 * @param <T> the type of data
 */
public class Node<T> {
	private static long		cpt		= 0;
	private final long		id;
	private Set<Edge<T>>	edges	= new HashSet<>();
	private T				data;

	public Node(T data) {

		this.id = cpt++;
		this.data = data;
	}

	public T getData() {

		return data;
	}

	public void setData(T data) {

		this.data = data;
	}

	public long getId() {

		return id;
	}

	public Set<Edge<T>> getEdges() {

		return edges;
	}

	public boolean addEdge(Edge<T> e) {

		return edges.add(e);
	}

}
