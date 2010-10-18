import java.util.ArrayList;

/**
 * A helper class to deal with the problem of parsing a .bcn file that is indexed. Is used as an intermediary between an indexed .bcn file and
 * a symbolic CSP.
 * 
 * @author adnan
 *
 */
public class Node {
	
	String label;
	int varNum;
	
	String[] domain;
	int[] adj;
	//int[][][] constraints;
	
	ArrayList<ArrayList<Integer>> constraints;
	
	int[] coordinates;

	public Node(int varnum, String label, String[] domain, int[] adj, ArrayList<ArrayList<Integer>> constraints, int[] coordinates) {
		
		this.varNum = varnum;
		this.label = label;
		this.domain = domain;
		this.adj = adj;
		this.constraints = constraints;
		this.coordinates = coordinates;
		
	}
	
	public Node() {
		this(0, null, null, null, null, null);
	}
	
}
	
	
