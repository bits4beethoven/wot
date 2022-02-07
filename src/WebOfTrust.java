import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Web of Trust (WoT) model. The WoT consists of nodes. The linkProbability map
 * is a function in Haskell style: for a node A it returns a function, which
 * consumes another node B and returns the trust of the link between A and B.
 * nodeHashMap is used for hashing.
 * 
 * @author bits4beethoven
 */
public class WebOfTrust {
	private List<Node> nodes;
	private Map<Node, Map<Node, Double>> linkProbabilityMap;
	private Map<String, Node> nodeHashMap;
	private Algorithm algorithm;
	private NodeComparator nodeComparator;

	/**
	 * Creates a WoT from the file. The file must be in a certain format, see
	 * <a href="https://github.com/bits4beethoven/wot">the repository</a> for
	 * description.
	 * 
	 * @param fileName Path to the file
	 */
	public WebOfTrust(String fileName) {
		nodes = new ArrayList<Node>();
		nodeHashMap = new HashMap<String, Node>();
		nodeComparator = new NodeComparator();
		linkProbabilityMap = new TreeMap<Node, Map<Node, Double>>(nodeComparator);

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			int lineCounter = 0;
			/*
			 * p can take values 0,1 or 2, indicating three different types of the input
			 * line
			 */
			byte p = 0;
			try {
				/*
				 * Read the header and input node names
				 */
				if ((line = br.readLine()) != null) {
					lineCounter++;
					Arrays.asList(line.split(",")).forEach(x -> {
						Node node = new Node(x);
						nodes.add(node);
						nodeHashMap.put(x, node);
						Map<Node, Double> map = new TreeMap<Node, Double>(nodeComparator);
						linkProbabilityMap.put(node, map);
					});
					p = 2;
				}
				/*
				 * For each node, read its' associations to other nodes
				 */
				Node parent = null;
				while ((line = br.readLine()) != null) {
					lineCounter++;
					try {
						switch (p++) {
						case 0: // node name
							parent = nodeHashMap.get(line);
							break;
						case 1: // node associations
							for (String link : line.split(",")) {
								Node child = nodeHashMap.get(link.split(":")[0]);
								child.addParent(parent);
								linkProbabilityMap.get(parent).put(child, Double.parseDouble(link.split(":")[1]));
							}
							break;
						}
						p %= 3;
					} catch (Exception e) {
						System.err.println(
								"Wrong input format in line " + lineCounter + " of \"" + fileName + "\": " + line);
						System.exit(1);
					}
				}
			} catch (IOException e) {
				System.err.print("Wrong file format!\n");
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					System.err.print("Could not close the file!\n");
				}
			}
		} catch (FileNotFoundException e) {
			System.err.print("File not found!\n");
		}
		
		// put a trust algorithm here
		algorithm = new Caronni();
	}

	/**
	 * @param source A
	 * @param target B
	 * @return Trust of A in B
	 */
	public double calculateTrustOfTo(String source, String target) {
		View temporalView = new View(source);
		nodes.stream().map(Node::getName).forEach(x -> {
			temporalView.setTrustTo(x, (x.equals(source) ? 1.0 : 0.0));
		});
		return algorithm.calculateTrustOfTo(this, temporalView, nodeHashMap.get(source), nodeHashMap.get(target));
	}

	/**
	 * Creates a view of a node on the network
	 * @param sourceName Owner of the view
	 * @return View of sourceName
	 */
	public View createViewOf(String sourceName) {
		View view = new View(sourceName);
		nodes.forEach(x -> {
			String targetName = x.getName();
			Double trustValue = calculateTrustOfTo(sourceName, targetName);
			view.setTrustTo(targetName, trustValue);
		});
		return view;
	}

	/**
	 * @param source A
	 * @param target B
	 * @return Probability of the link between A and B
	 */
	public double getLinkProbabilityBetween(Node source, Node target) {
		return linkProbabilityMap.get(source).get(target);
	}

	public String toString() {
		StringBuilder head = new StringBuilder();
		head.append("Web of Trust: " + linkProbabilityMap.keySet());
		StringBuilder links = new StringBuilder();
		linkProbabilityMap.keySet().stream().filter(n -> !linkProbabilityMap.get(n).isEmpty()).forEach(x -> {
			links.append(x + "...\n");
			linkProbabilityMap.get(x).keySet().stream().forEach(y -> {
				links.append("... -> " + y + ": " + linkProbabilityMap.get(x).get(y) + ", ");
			});
			links.append("\n");
		});
		head.append("\n");

		return head.toString() + links.toString();
	}
	/**
	 * A comparator for Nodes that is used in a TreeMap
	 * @author bits4beethoven
	 *
	 */
	class NodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node a, Node b) {
			return a.getName().compareTo(b.getName());
		}
	}

	public NodeComparator getNodeComparator() {
		return nodeComparator;
	}

}
