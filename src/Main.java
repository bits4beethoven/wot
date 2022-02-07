
public class Main {
	public static void main(String[] args) {
		String filename = "input_simple1";
		WebOfTrust wot = new WebOfTrust(filename);

		// measure the algorithm time
		long start = System.currentTimeMillis();
		View view = wot.createViewOf("A");
		// or for a single node:
		// System.out.println(wot.calculateTrustOfTo("A", "E"));
		long end = System.currentTimeMillis();

		System.out.println(view);
		System.out.println("Algorithm done in: " + (end - start) + "ms");
	}
}