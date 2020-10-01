package srcs.workflow.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphImpl<T> implements Graph<T> {
	Map<T, Node<T>>	mapNode	= new HashMap<>();
	Set<Edge<T>>	mapEdge	= new HashSet<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {

		return mapNode.keySet().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNode(T n) throws IllegalArgumentException {

		if (mapNode.containsKey(n))
			throw new IllegalArgumentException("Noeud déjà existant");

		Node<T> node = new Node<>(n);
		mapNode.put(n, node);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addEdge(T from, T to) throws IllegalArgumentException {

		Node<T>	f	= mapNode.get(from);
		Node<T>	t	= mapNode.get(to);

		if (f == null || t == null)
			throw new IllegalArgumentException("Un noeud n'existe pas");

		if (existEdge(from, to))
			throw new IllegalArgumentException("Edge déjà existant");

		Edge<T> edge = new Edge<T>();
		edge.setFrom(f);
		edge.setTo(t);
		f.addEdge(edge);
		t.addEdge(edge);

		mapEdge.add(edge);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existEdge(T from, T to) {

		Node<T> node;

		if ((node = mapNode.get(from)) == null)
			return false;

		for (Edge<T> e : node.getEdges()) {
			T dataTo = e.getTo().getData();

			if (to.equals(dataTo))
				return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existNode(T n) {

		return mapNode.containsKey(n);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {

		return mapNode.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {

		return mapNode.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> getNeighborsOut(T from) throws IllegalArgumentException {

		Node<T>	node;
		List<T>	res	= new ArrayList<>();

		if ((node = mapNode.get(from)) == null)
			throw new IllegalArgumentException("Noeud inexistant");

		for (Edge<T> e : node.getEdges()) {
			T dataTmp = e.getFrom().getData();
			if (dataTmp.equals(from))
				res.add(e.getTo().getData());
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> getNeighborsIn(T to) throws IllegalArgumentException {

		Node<T>	node;
		List<T>	res	= new ArrayList<>();

		if ((node = mapNode.get(to)) == null)
			throw new IllegalArgumentException("Noeud inexistant");

		for (Edge<T> e : node.getEdges()) {
			T dataTmp = e.getTo().getData();
			if (dataTmp.equals(to))
				res.add(e.getFrom().getData());
		}

		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<T> accessible(T from) throws IllegalArgumentException {

		return accessible_(from, new HashSet<T>());
	}

	private Set<T> accessible_(T from, Set<T> res) {

		if (mapNode.get(from) == null)
			throw new IllegalArgumentException("Noeud inexistant");

		List<T> outNodes = getNeighborsOut(from);

		for (T out : outNodes) {
			if (res.contains(out))
				continue;
			res.add(out);
			res.addAll(accessible_(out, res));
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDAG() {

		for (T node : mapNode.keySet()) {
			Set<T> a = accessible(node);
			if (a.contains(node))
				return false;
		}
		return true;
	}

}
