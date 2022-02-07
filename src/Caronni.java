
public class Caronni implements Algorithm {

	@Override
	public double calculateTrustOfTo(WebOfTrust wot, View view, Node source, Node target) {
		String sourceName = source.getName();
		String targetName = target.getName();
		target.getFilteredParents(view, wot.getNodeComparator()).forEach(parent -> {
			String parentName = parent.getName();
			Double newTrust = 0.0;
			if (parentName.equals(sourceName)) { // parent is the source node
				newTrust = 1 - (1 - view.getTrustFor(parentName) * wot.getLinkProbabilityBetween(parent, target))
						* (1 - view.getTrustFor(targetName));
			} else { // parent is not a source node, new recursion necessary
				Double buf = view.removeTrustTo(targetName); // remove the target node temporary from the network
				View viewCopy = view.copy();

				// start a new recursion step without the current target node
				Double trustInParent = calculateTrustOfTo(wot, viewCopy, source, parent);
				newTrust = 1 - (1 - trustInParent * wot.getLinkProbabilityBetween(parent, target)) * (1 - buf);
			}
			view.setTrustTo(targetName, newTrust); // add the temporary removed target node in the network again
		});

		return view.getTrustFor(targetName);
	}
}
