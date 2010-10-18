import java.util.ArrayList;
import java.util.Vector;

/**
 * <p>
 * This class represents a particular Constraint Satisfaction Problem.
 * </p>
 * <p>
 * It contains a Symbol Table representation of the a Graph with vertices as Keys in the table and the Set of adjacencies as Edges. See
 * Graph.java for more information. 
 * </p>
 * <p>
 * The constraintsTable is another Symbol table that represents the constraints for the WHOLE CSP (the whole graph). It is defined as:
 * (Variable, (Neighbour, (Value, Set of legal neighbour values))) for EVERY value of every constraint-bearing variable.
 * </p>
 * <p>
 * The domainTable is a Symbol table (Variable, Set of domain values) where each key is a variable and val is the set of domain values
 * </p>
 * <p>
 * The config is the internal representation of the variable=value assignments. At the beginning it gets initialized to variable="" for
 * every variable by makeConfig().
 * </p>
 * <p>
 * The dIndex and vIndex are internal representations of the vertices and domain so that each one of these entities has an int value association.
 * </p>
 * <p>
 * Coordinates are for GUI primal graph purposes.
 * </p>
 * @author adnan
 *
 */
public class CSP {
	
	Graph G;
	ST<String, ST<String, ST<String, SET<String>>>> constraintsTable;
	ST<String, SET<String>> domainTable;
	ST<String, String> config;
	ST<String, ArrayList<Integer>> coordinatesTable;
	ST<String, Integer> vIndex;
	ST<String, ST<String, Integer>> dIndex;
	
	public CSP(Graph G, 
				ST<String, ST<String, ST<String, SET<String>>>> constraintsTable, 
				ST<String, SET<String>> domainTable, 
				ST<String, String> config,
				ST<String, ArrayList<Integer>> coordinatesTable,
				ST<String, Integer> vIndex,
				ST<String, ST<String, Integer>> dIndex) {
			
		this.G = G;
		this.constraintsTable = constraintsTable;
		this.domainTable = domainTable;
		this.config = config;
		this.coordinatesTable = coordinatesTable;
		this.vIndex = vIndex;
		this.dIndex = dIndex; 
	}
	
	public CSP() {
		this(null, null, null, null, null, null, null);
	}
	
	//output the CSP
	public String toString() {
		
		String cspString = "";
		
		//Print out the constraints Table
		int m = 0;
		for(String x: this.constraintsTable) {
			for(String y: this.constraintsTable.get(x)) {
				for(String z: (this.constraintsTable.get(x)).get(y)) {
					m++;
					cspString = cspString + String.format("(%s, (%s, (%s, (%s))))\n", x, y, z, ((this.constraintsTable.get(x)).get(y)).get(z).toString());
				}
				System.out.println("--------------------------------------------");
				//break;
			}
			System.out.println("--------------------------------------------");
			//break;
		}

		
		return null;
		
	}
	
	/**
	 * Makes default configuration based on Graph
	 * TODO: I don't like doing it this way
	 */
	void makeConfig() {
		this.config = new ST<String, String>();
		
		for(String node: G.vertices()) {
			this.config.put(node, "");
			System.out.print(node + ", ");
		}
		System.out.println();
	}

}
