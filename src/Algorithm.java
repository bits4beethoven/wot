
public interface Algorithm {
	/**
	 * Calculates trust of a source node into a target node
	 * @param wot Web of Trust
	 * @param view Current view on the Web of Trust. Initial trust to source is set to 1.0, initial trust to all other nodes is set to 0.0
	 * @param source Source node in the web
	 * @param target Target node in the web
	 * @return Trust of source into target
	 */
	public double calculateTrustOfTo(WebOfTrust wot, View view, Node source, Node target);
}
