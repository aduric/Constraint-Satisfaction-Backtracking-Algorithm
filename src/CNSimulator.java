import java.util.ArrayList;

/**
 * <p>
 * Generates a random CN according to some values and returns a CSP describing it
 * </p>
 * @author adnan
 *
 */
public class CNSimulator {

	/**
	 * <p>
	 * Generates n random strings of length depending on n. The strings will consist of all 25 possible alphabet characters
	 * </p>
	 * <p>
	 * The lengths will increment by 1 once all the previous strings of length-1 have been used up.<br>
	 * So, if n = 500, 25 strings of length = 1<br>
	 * 				+ 25*25 strings of length = 2<br>
	 * 				= 650 possible strings and therefore only strings of lengths 1 and 2 will be generated<br>
	 * </p>
	 * @param n
	 * @return
	 */
	private static ArrayList<String> genNStrings(int n) {
		
		ArrayList<String> myStrings = new ArrayList<String>();
		
		//generate a char array with minimum acceptable length
		String acceptableChars = "abcdefghijklmnopqrstuvwxyz";
		if(n == 1) {
			char rChar = acceptableChars.charAt((int)(Math.random() * acceptableChars.length()));
			String newString = Character.toString(rChar);
			myStrings.add(newString);
			return myStrings;
		}
		char[] randomString = new char[(int)Math.ceil(Math.log(n)/Math.log(acceptableChars.length()))];
		
		for(int i = 0; i < n; i++) {
			for(int c = 0; c < randomString.length; c++) {
				randomString[c] = acceptableChars.charAt((int)(Math.random() * acceptableChars.length()));
			}
			String newString = new String(randomString);
			if(!myStrings.contains(newString))
				myStrings.add(newString);
			else
				i--;
			
		}
		return myStrings;
	}
	
	
	public static CSP generateCN(int numNodes, int numValues, int numConstraints, double assignmentRatio) {
		
		int SCREEN_RES_X = 600;
		int SCREEN_RES_Y = 600;
		
		
		//Create a CSP
		CSP csp = new CSP();
		
		//Create a random graph
        Graph G = new Graph();
        
        //Generate vertices
        ArrayList<String> nodes = genNStrings(numNodes);
        
        //put vertices into the graph
        int vindex = 0;
        csp.vIndex = new ST<String, Integer>();
        ArrayList<Integer> currVIndex = new ArrayList<Integer>();
        for(String node: nodes) {
        	G.addVertex(node);
        	csp.vIndex.put(node, vindex);
        	currVIndex.add(vindex);
        	vindex++;
        }
        
        //Generate edges (constraints for CSP)
        for(int c = 0; c < numConstraints; c++) {
        	String v1 = "";
        	if(c < G.V()) //make sure all nodes are connected to at least 1 other node first
        		v1 = nodes.get(c);
        	else
        		v1 = nodes.get((int)(Math.random() * nodes.size()));
        	String v2 = nodes.get((int)(Math.random() * nodes.size()));
        	if(!G.hasEdge(v1, v2) && !v1.equals(v2)) 
        		G.addEdge(v1, v2);
        	else
        		c--;
        }
        
        //Generate a domain table
    	//TODO: Each node might have a different number of domain values
        csp.domainTable = new ST<String, SET<String>>();
        csp.dIndex = new ST<String, ST<String, Integer>>();
        ArrayList<String> domainStrings = genNStrings(numValues);
        
    	SET<String> domain = new SET<String>();
    	ST<String, Integer> domainIndex = new ST<String, Integer>();
    	int dcount = 0;
    	for(String ds: domainStrings) {
    		domain.add(ds);
    		domainIndex.put(ds, dcount);
    		dcount++;
    		System.out.println("Domain: " + ds + ", " + dcount);
    	}
        for(String node: G.vertices()) {
        	//ArrayList<String> domainStrings = genNStrings(numValues);
    		csp.domainTable.put(node, domain);
    		csp.dIndex.put(node, domainIndex);
    	}

        //Generate a constraints table
    	//TODO: Make sure that the vIndex matches up with the constraint dependency for output (constraints stored with HIGHEST index)
    	
    	ST<String, ST<String, ST<String, SET<String>>>> constraintsTable = new ST<String, ST<String, ST<String, SET<String>>>>();
    	for(String node: G.vertices()) {
    		ST<String, ST<String, SET<String>>> adjConstraint = new ST<String, ST<String, SET<String>>>();
    		for(String adj: G.adjacentTo(node)) {
    			//Need to make sure that node and adj are only constrained ONCE
    			//System.out.println(constraintsTable.get(adj));
    			if(constraintsTable.get(adj) != null && constraintsTable.get(adj).contains(node)) {
    				System.out.println("tried 2-way constraint");
    				continue;
    			}
    			int totalConstraints = (int)(csp.domainTable.get(node).size() * csp.domainTable.get(adj).size() * assignmentRatio);
    			ST<String, SET<String>> singleConstraint = new ST<String, SET<String>>();
    			for(int c = 0; c < totalConstraints; c++) {
    				String rNodeVal = "", rAdjVal = "";
    				int rNode = (int)(Math.random() * csp.domainTable.get(node).size());
    				int rAdj = (int)(Math.random() * csp.domainTable.get(adj).size());
    				for(String rn: csp.domainTable.get(node)) {
    					if(csp.dIndex.get(node).get(rn) == rNode)
    						rNodeVal = rn;
    				}
       				for(String rn: csp.domainTable.get(adj)) {
    					if(csp.dIndex.get(adj).get(rn) == rAdj)
    						rAdjVal = rn;
    				}
    				System.out.println("---------------------------------------------");
    				System.out.format("rNodeVal=%s, rAdjVal=%s\n", rNodeVal, rAdjVal);
    				
       				//create valid assignment
       				if(singleConstraint.get(rNodeVal) == null) {
       					SET<String> firstSet = new SET<String>();
       					firstSet.add(rAdjVal);
       					singleConstraint.put(rNodeVal, firstSet);
       					System.out.format("add new set: rNodeVal=%s, rAdjVal=%s\n", rNodeVal, rAdjVal);
       				}
       				else if(!singleConstraint.get(rNodeVal).contains(rAdjVal)) {
    					SET<String> incSet = singleConstraint.get(rNodeVal);
    					incSet.add(rAdjVal);
    					singleConstraint.put(rNodeVal, incSet);
    					System.out.format("!contains: rNodeVal=%s, rAdjVal=%s\n", rNodeVal, rAdjVal);
    				}
    				else {
    					System.out.format("contains: rNodeVal=%s, rAdjVal=%s\n", rNodeVal, rAdjVal);
    					c--;
    				}
    			}
    			System.out.println("Add adj constriant");
    			adjConstraint.put(adj, singleConstraint);
    			//update the vIndex, making sure that the variable doing the constraining receives a higher index
    			/*if(csp.vIndex.get(node) < csp.vIndex.get(adj)) {
    				currVIndex.remove((Object)csp.vIndex.get(adj));
    				currVIndex.add(csp.vIndex.get(node), csp.vIndex.get(adj));
    			}*/
    		}
    		constraintsTable.put(node, adjConstraint);
    	}
    	
    	//Add constraints table to the CSP
    	csp.constraintsTable = constraintsTable;
        
        //Generate a coordiantes table
        ST<String, ArrayList<Integer>> coordinatesTable = new ST<String, ArrayList<Integer>>();
        for(String node: G.vertices()) {
        	ArrayList<Integer> coords = new ArrayList<Integer>(2);
        	int x = (int)(Math.random() * SCREEN_RES_X);
        	int y = (int)(Math.random() * SCREEN_RES_Y);
        	coords.add(x);
        	coords.add(y);
        	coordinatesTable.put(node, coords);
        }
        csp.coordinatesTable = coordinatesTable;
        
        //The vertex index might have changed when computing the constraints, so we have to re-order them
        /*int ci = 0;
        for(String v: csp.vIndex) {
        	csp.vIndex.put(v, currVIndex.get(ci));
        	ci++;
        }*/
        
        //put the graph into the CSP
        csp.G = G;
        
        //return the CSP
        return csp;
	}
	
}
