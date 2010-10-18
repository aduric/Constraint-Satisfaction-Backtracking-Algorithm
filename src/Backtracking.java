
public class Backtracking {

	public static String getVariable(ST<String, String> config) {
		
		//retrieve a variable based on a heuristic or the next 'unfilled' one if there is no heuristic
        for (String s : config) {
        	if(config.get(s).equalsIgnoreCase(""))
        		return s;
        }
		
        //get variable failed (all variables have been coloured)
		return null;
	}
	
	public static SET<String> orderDomainValue(String variable, ST<String, SET<String>> domain) {
		
		//return the SET of domain values for the variable
		return domain.get(variable);
	}
	
	public static boolean complete(ST<String, String> config) {
		
		for(String s: config) {
			//if we find a variable in the config with no value, then this means that the config is NOT complete
			if(config.get(s).equalsIgnoreCase(""))
				return false;
		}
		
		//ALL variables in config have a value, so the configuration is complete
		return true;
	}
	
	
	public static boolean consistent(String value, String variable, ST<String, String> config, Graph g) {
		
		//we need to get the adjacency list for the variable
		for(String adj: g.adjacentTo(variable)) {
			//if the adjacency list member's value is equal to the variable's selected value, then consistency fails
			if(config.get(adj).equalsIgnoreCase(value)) {
				//consistency check fail
				return false;
			}
		}
			
		//consistency check passed according to the variable's adjacancy list
		return true;
	}
	
	public static boolean consistent(String value, String variable, ST<String, String> config,
										ST<String, ST<String, ST<String, SET<String>>>> constraintsTable) {
		
		//we need to get the constraint list for the variable
		for(String constraints: constraintsTable.get(variable)) {
			//if the adjacency list member's value is equal to the variable's selected value, then consistency fails
			if(!config.get(constraints).equals("") && !(constraintsTable.get(constraints).get(value).contains(config.get(constraints)))) {
				return false;
			}
		}
			
		//consistency check passed according to the variable's adjacancy list
		return true;
	}
	
	public static ST<String, String> backtracking(ST<String, String> config, ST<String, SET<String>> domain, Graph g) {
		
		//recursion base case - check configuration completeness
		if(complete(config))
			return config;
		
		ST<String, String> result = null;
		
		//get a variable
		String v = getVariable(config);
		
		//get a SET of all the variable's values
		SET<String> vu = orderDomainValue(v, domain);
		
		//loop through all the variable's values
		for(String u: vu) {
			//if(consistent(u, v, config, g)) {
			if(consistent(u, v, config, g)) {	
				config.put(v, u);
				result = backtracking(config, domain, g);
				if(result != null)
					return result;
				config.remove(v);
			}
		}
		return null;
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        Graph G = new Graph();
        G.addEdge("x1", "x2");
        G.addEdge("x1", "x3");
        G.addEdge("x1", "x4");
        G.addEdge("x1", "x7");
        G.addEdge("x2", "x6");
        G.addEdge("x3", "x7");
        G.addEdge("x4", "x5");
        G.addEdge("x5", "x6");
        G.addEdge("x5", "x7");

        // print out graph
        System.out.println(G);

        // print out graph again by iterating over vertices and edges
        /*for (String v : G.vertices()) {
        	System.out.println(v + ": ");
            for (String w : G.adjacentTo(v)) {
            	System.out.println(w + " ");
            }
            System.out.println();
        }*/
        
        //build the domain table
        ST<String, SET<String>> domainTable = new ST<String, SET<String>>();
        
        //build the domains
        SET<String> domainX1 = new SET<String>();
        SET<String> domainX2 = new SET<String>();
        SET<String> domainX3 = new SET<String>();
        SET<String> domainX4 = new SET<String>();
        SET<String> domainX5 = new SET<String>();
        SET<String> domainX6 = new SET<String>();
        SET<String> domainX7 = new SET<String>();

        // insert some keys
        domainX1.add("red");
        domainX1.add("blue");
        domainX1.add("green");
        
        domainX2.add("blue");
        domainX2.add("green");

        domainX3.add("red");
        domainX3.add("blue");
        domainX3.add("green");

        domainX4.add("red");
        domainX4.add("blue");

        domainX5.add("blue");
        domainX5.add("green");

        domainX6.add("red");
        domainX6.add("green");
        domainX6.add("yellow");

        domainX7.add("red");
        domainX7.add("blue");

        domainTable.put("x1", domainX1);
        domainTable.put("x2", domainX2);
        domainTable.put("x3", domainX3);
        domainTable.put("x4", domainX4);
        domainTable.put("x5", domainX5);
        domainTable.put("x6", domainX6);
        domainTable.put("x7", domainX7);
        
        
       //create empty configuration
       ST<String, String> config = new ST<String, String>();
       
       config.put("x1","");
       config.put("x2","");
       config.put("x3","");
       config.put("x4","");
       config.put("x5","");
       config.put("x6","");
       config.put("x7","");
       
       ST<String, String> result = backtracking(config, domainTable, G);
       
       // print out the solution
       for (String s : result)
           System.out.println(s + " " + result.get(s));

	}

}
