import java.util.ArrayList;
import java.util.Collections;

/**
 * <h3>The Algorithm class</h3>
 * <p>
 * Given a CSP, use an algorithm in this class to solve it. It will output a result set.
 * </p>
 * <p>
 * The algorithm is the backtracking algorithm with MRV heuristic. There are 2 backtracking methods. All the other methods are static methods
 * for dealing with specific backtracking tasks.
 * </p>
 * @author adnan
 *
 */
public class CSPAlgorithm {
	
	private CSP csp;
	
	/**
	 * Create the Algorithm object to be used to solve the CSP that is given to it
	 * @param csp
	 */
	public CSPAlgorithm(CSP csp) {
		this.csp = csp;
	}

	private String getVariable(ArrayList<String> configOrder) {
		
		int minVal, tmpVal;
		String minVar = null;
		
		//reverse the configOrder, thereby favouring the variables at the front of the list
		Collections.reverse(configOrder);
		minVal = Integer.MAX_VALUE; // some very large number
		//Go through all the remaining variables in the order specified (that don't have a value in config and figure out which one has the MRV)		
		//retrieve a variable based on a heuristic or the next 'unfilled' one if there is no heuristic
        for (String s : configOrder) {
        	if(csp.config.get(s).equalsIgnoreCase("")) {
            	tmpVal = 0;
        		//need to check ALL values for each variable
        		for(String adj: csp.constraintsTable.get(s)) {
        			for(String val: csp.constraintsTable.get(s).get(adj)) {
        				if(consistent(val, s)) {
        					tmpVal++;
        				}
        			}
        		}
				if(tmpVal <= minVal) {
					minVal = tmpVal;
					minVar = s;
				}
        	}
        }
		
        //Return the variable that has the least number of legal values
		return minVar;
	}
	private SET<String> orderDomainValue(String variable) {
		
		//return the SET of domain values for the variable
		return csp.domainTable.get(variable);
	}
	
	private boolean complete() {
		
		for(String s: csp.config) {
			//if we find a variable in the config with no value, then csp means that the config is NOT complete
			if(csp.config.get(s).equalsIgnoreCase(""))
				return false;
		}
		
		//ALL variables in config have a value, so the configuration is complete
		return true;
	}
		
	private boolean consistent(String value, String variable) {
		
		//we need to get the constraint list for the variable
		for(String constraints: csp.constraintsTable.get(variable)) {
			//if the adjacency list member's value is equal to the variable's selected value, then consistency fails
			System.out.print("csp.config: ");
			for(String conf: csp.config) {
				System.out.print(conf + " = " + csp.config.get(conf) + ", ");
			}
			System.out.println();
			System.out.format("consistent: %s, %s, %s\n", constraints, csp.config.get(constraints), csp.constraintsTable.get(variable).get(constraints).get(value));
			if(csp.constraintsTable.get(variable).get(constraints).get(value) != null && 
					!csp.config.get(constraints).equals("") && 
					!(csp.constraintsTable.get(variable).get(constraints).get(value).contains(csp.config.get(constraints)))) {
				return false;
			}
		}
			
		//consistency check passed according to the variable's adjacancy list
		return true;
	}
	
	/**
	 * Backtracking with default ordering (alphabetical)
	 * @return
	 */
	ST<String, String> backtracking() {
		ArrayList<String> configOrder = new ArrayList<String>();
		for(String node: csp.G.vertices()) {
				configOrder.add(node);
		}
		return this.backtracking(configOrder);
	}
	
	/**
	 * Backtracking with user-defined ordering of variables
	 * 
	 * @param configOrder - the user defined ordering of variables
	 * @return
	 */
	ST<String, String> backtracking(ArrayList<String> configOrder) {
		
		//recursion base case - check configuration completeness
		if(this.complete())
			return csp.config;
		
		ST<String, String> result = null;
		
		//get a variable
		String v = this.getVariable(configOrder);
		
		//get a SET of all the variable's values
		SET<String> vu = this.orderDomainValue(v);
		
		//loop through all the variable's values
		for(String u: vu) {
			//if(consistent(u, v, config, g)) {
			if(this.consistent(u, v)) {	
				csp.config.put(v, u);
				result = this.backtracking(configOrder);
				if(result != null)
					return result;
				csp.config.put(v, "");
			}
		}
		return null;
		
	}
	
	
}
