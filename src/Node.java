import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Represent a node of a Web of Trust
 * @author bits4beethoven
 *
 */
public class Node {
	private String name;
	private List<Node> parents;

	/**
	 * Creates a new node with the given name
	 * @param name Name of the node
	 */
	public Node(String name) {
		this.name = name;
		parents = new ArrayList<Node>();
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns parents that are considered in the current recursion step of the algorithm
	 * @param view View on the network in the current recursion step
	 * @param nodeComparator Comparator that sorts the returned list
	 * @return A sorted list of parents considered in the current recursion step
	 */
	public List<Node> getFilteredParents(View view, WebOfTrust.NodeComparator nodeComparator) {
		return parents.stream().sorted(nodeComparator).filter(x -> view.getKeySet().contains(x.getName()))
				.collect(Collectors.toList());
	}

	public void addParent(Node parent) {
		parents.add(parent);
	}

	public void deleteParent(Node parent) {
		parents.remove(parent);
	}

	public String toString() {
		return "Node " + name;
	}

}
