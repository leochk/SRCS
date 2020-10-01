package srcs.workflow.graph;

/**
 * Class representing a unidirectional edge between two Node containing data of
 * type T.
 * 
 * @author check-leo
 *
 * @param <T> the data type
 */
public class Edge<T> {
	private static long	cpt	= 0;
	private final long	id;
	private Node<T>		from;
	private Node<T>		to;

	public Edge() {

		this.id = cpt++;
	}

	public Node<T> getFrom() {

		return from;
	}

	public void setFrom(Node<T> from) {

		this.from = from;
	}

	public Node<T> getTo() {

		return to;
	}

	public void setTo(Node<T> to) {

		this.to = to;
	}

	public long getId() {

		return id;
	}

}
