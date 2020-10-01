package srcs.workflow.graph;

import java.util.List;
import java.util.Set;

public interface Graph<T> extends Iterable<T> {
	/**
	 * Create a node containing the object T, and insert it in the graph
	 * 
	 * @param n the data to store
	 * @throws IllegalArgumentException if n exists in graph
	 */
	void addNode(T n) throws IllegalArgumentException;

	/**
	 * Create an unidirectional link between inputed data, and insert it in the
	 * graph
	 * 
	 * @param from
	 * @param to
	 * @throws IllegalArgumentException if from or to not in graph or edge
	 *                                  already exists
	 */
	void addEdge(T from, T to) throws IllegalArgumentException;

	/**
	 * Compute if an unidirectional link between inputed data exists
	 * 
	 * @param from
	 * @param to
	 * @return True if it exists
	 */
	boolean existEdge(T from, T to);

	/**
	 * Compute if a node containing the inputed data exists
	 * 
	 * @param n
	 * @return True if it exists
	 */
	boolean existNode(T n);

	/**
	 * Compute if graph is empty (i.e, there is no node)
	 * 
	 * @return True if empty
	 */
	boolean isEmpty();

	/**
	 * Compute number of graph node
	 * 
	 * @return the number of node
	 */
	int size();

	/**
	 * Get the list of neighboring nodes of node from to via the link outcoming.
	 * 
	 * @param from the node
	 * @return List of neighbor
	 * @throws IllegalArgumentException if from does not exist
	 */
	List<T> getNeighborsOut(T from) throws IllegalArgumentException;

	/**
	 * Get the list of neighboring nodes of node to to via the link incoming.
	 * 
	 * @param from the node
	 * @return List of neighbor
	 * @throws IllegalArgumentException if to does not exist
	 */
	List<T> getNeighborsIn(T to) throws IllegalArgumentException;

	/**
	 * Get the list of nodes accessible from inputed node
	 * 
	 * @param from the node
	 * @return
	 * @throws IllegalArgumentException if from do not exist
	 */
	Set<T> accessible(T from) throws IllegalArgumentException;

	/**
	 * Test if the graph is acyclic
	 * 
	 * @return True if acyclic
	 */
	boolean isDAG();
}
