import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * View on the Web of Trust. A view holds the name of the owner, the map
 * represents the trust of the owner in other nodes of the web. The trust is
 * expressed as a value between 0.0 and 1.0.
 * @author bits4beethoven
 */
public class View {
	private String owner;
	private Map<String, Double> trust;

	/**
	 * Creates a view for a certain node
	 * @param name Name of the node
	 */
	public View(String name) {
		owner = name;
		trust = new TreeMap<String, Double>();
	}
	/**
	 * @return Nodes to which the owner has some kind of relationship
	 */
	public Set<String> getKeySet() {
		return trust.keySet();
	}

	/**
	 * @param other Other node in the network
	 * @return Trust of source into the other node in this network
	 */
	public Double getTrustFor(String other) {
		return trust.get(other);
	}

	/**
	 * Sets the trust of the owner into some other node
	 * @param Other node in the network
	 * @param Trust value between 0.0 and 1.0
	 */
	public void setTrustTo(String other, double value) {
		trust.put(other, value);
	}

	/**
	 * Removes the trust association from the view and returns its trust value
	 * @param Other node in the network
	 * @return Trust value associated with the other node
	 */
	public Double removeTrustTo(String other) {
		Double temp = trust.get(other);
		trust.remove(other);
		return temp;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		trust.keySet().forEach(x -> sb.append("\tTrust in " + x + ": " + trust.get(x) + "\n"));
		return "View of " + owner + ":\n" + sb.toString();
	}

	/**
	 * Creates a copy of this view
	 * @return Copy of the view
	 */
	public View copy() {
		View view = new View(owner);
		trust.keySet().stream().forEach(x -> view.setTrustTo(x, trust.get(x)));
		return view;
	}
}
