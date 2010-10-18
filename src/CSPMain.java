import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * <h2>CSP Main</h2>
 * <p>
 * The main class for the CSP solver. This class calls all the other classes and instantiates objects necessary to perform the computation.
 * So, the steps needed to be followed to read in, write out and simulate a CSP are described below.
 * </p>
 * <p>
 * The CSP project is divided into 4 parts, and each part has a class that is associated with an operation.
 * </p>
 * <p>
 * To read in a file, see CNParser.java. This class takes a text file as input and parses it to create a CSP.
 * </p>
 * <p>
 * The CNSimulator generates a RANDOM CSP depending on the parameters given. See CNSimulator.java. It will return a CSP that is ready to be solved
 * or written to a file.
 * </p>
 * <p>
 * To output a CSP (created by the CNSimulator.java for instance), use the CNCreator.java class. As input, it expects the output file name
 * and the CSP. 
 * </p>
 * </p>
 * The CSPAlgorithm SOLVES the CSP. Thus is expects a CSP as input. Please see CSPAlgorithm.java for more information. 
 * The only algorithm that is available there is the Bactracking algorithm. The Backtracking algorithm expects a variable ordering. This ordering
 * must be given as an ArrayList of ALL the variables in the ordering desired. This is to break ties when selecting variables in the algorithm. 
 * </p>
 * <p>
 * A CSP is a class (CSP.java) that contains all the necessary data structures to provide information about the CSP.
 * </p>
 * 
 * 
 * 
 * @author adnan
 *
 */
public class CSPMain {

	/**
	 * <p>
	 * Application usage:
	 * </p>
	 * <p>
	 * Please run the program with the following input usage:
	 * </p>
	 * <p>
	 * If you want to run and solve a CSP from a file:<br>
	 * <strong>java CSPMain -f filenameIn</strong>
	 * </p>
	 * <p>
	 * If you want to simulate a CSP and write it to a file. Note that the program will NOT solve the CSP:<br>
	 * <strong>java CSPMain -s N filenameOut &lt;num_of_nodes&gt; &lt;num_of_values&gt; &lt;num_of_constraints&gt; &lt;assignment_ratio&gt;</strong>
	 * </p>
	 * <p>
	 * - where N is the number of simulations (number of CSPs generated)<br>
	 * - filenameOut is the file path (without the the extension)<br>
	 * - num_of_nodes is the number of nodes per CSP<br>
	 * - num_of_values is the number of domain values per node<br>
	 * - num_of_constraints is the number of edges in a CSP (this must be a valid number)<br>
	 * - assignment_ratio is the number of valid assignments / total number of assignments possible between two nodes that are constrained (ie, are linked)<br>
	 * </p>
	 * <p>
	 * This will generate N text files in the form of filenameOutx.bcn where x = integer</p>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		int READ = 1;
		int SIM = 2;
		int MAXCSP = 10;
		int MAXNODES = 30;
		int MAXVALUES = 60;
		
		//For simulation purposes
		int numCSP = 0;
		int numNodes = 0;
		int numValues = 0;
		int numConstraints = 0;
		double assignmentRatio = 0.0;
/*		
for(String a: args) {		
System.out.println(a + " ");
}
*/
		
		//code for desired operation
		int op = 0;
		//Check user input
		if(args.length < 2 || args.length > 7) {
			System.out.println("Invalid arguments. Check JDoc for CSPMain for correct usage.");
			System.exit(0);
		}
		
		if(args[0].equals("-f")) { //Read in CSP from file
			//Does the file exist?
			if(args.length != 2) {
				System.out.println("Invalid arguments. See usage.");
				System.exit(0);
			}
			if(args[1].length() > 0) {
				File f = new File(args[1]);
				if(!f.exists()) {
					System.out.println("No file found: " + args[1]);
					System.exit(0);
				}
				else {
					//Everything is OK
					System.out.println("Starting parsing...");
					op = READ;
				}
			}
		}
		else if(args[0].equals("-s")) { //Simlulate a CSP / create a file and write to it			
			//CSPMain -s N filenameOut <num_of_nodes> <num_of_values> <num_of_constraints> <assignment_ratio>
			System.out.println(args.length);
			if(args.length != 7) {
				System.out.println("Invalid arguments for a simulation. See usage.");
				System.exit(0);
			}
			
			try { //put all the user input into int variables
				numCSP = Integer.parseInt(args[1]);
				numNodes = Integer.parseInt(args[3]);
				numValues = Integer.parseInt(args[4]);
				numConstraints = Integer.parseInt(args[5]);
				assignmentRatio = Double.parseDouble(args[6]);
				
			} catch(NumberFormatException e) {
				System.out.println("One of the arguments supplied is not a valid integer.");
				System.exit(0);
			}
			
			//now check the size of each value, make sure that they are resonable
			if(numCSP > MAXCSP) {
				System.out.println("Maximum number of CSPs is " + MAXCSP);
				System.exit(0);
			}
			if(numNodes > MAXNODES) {
				System.out.println("Maximum number of variables is " + MAXNODES);
				System.exit(0);
			}
			if(numValues > MAXVALUES) {
				System.out.println("Maximum number of values per node is " + MAXVALUES);
				System.exit(0);
			}
			if(numConstraints < (numNodes - 1) || numConstraints > (numNodes * (numNodes - 1)/2)) {
				System.out.println("Invalid number of constraints given.");
				System.exit(0);
			}
			if(assignmentRatio < 0.0 || assignmentRatio > 1.0) {
				System.out.println("Invalid assignment ratio. Needs to be between 0.0 and 1.0.");
				System.exit(0);
			}
			
			//everything is OK
			System.out.println("Starting Simulation...");
			op = SIM;

		} 
		else {
			System.out.println("Invalid arguments. See usage.");
			System.exit(0);
		}
	
			
		if(op == READ) {

			//Create the CSP and solve it
			CSP csp;

			//parse CSP from a text file
			csp = CNParser.parse(args[1]);

			//create a default configuration (variable ordering [default = alpha])
			csp.makeConfig();

			//Create the Algorithm and pass onto it the CSP
			CSPAlgorithm CNSolver = new CSPAlgorithm(csp);

			//solve the CSP
			//add a timer so that we know how long the algorithm takes
			Date timer1 = new Date();
			long currTime = timer1.getTime();
			ST<String, String> result = CNSolver.backtracking();
			Date timer2 = new Date();
			long afterTime = timer2.getTime();
			
			if(result == null) {
				System.out.println("No Solution!");
				System.out.format("Algorithm took %d ms\n", afterTime - currTime);
			}
			else {
				// print out the solution
				System.out.println("Backtracking Solution");
				System.out.println("---------------------");
				for (String s : result)
					System.out.println(s + " " + result.get(s));
				System.out.println("---------------------");
				System.out.format("Algorithm took %d ms\n", afterTime - currTime);
				System.out.println("---------------------");
			}
			
			//Draw the Primal Graph
			PrimalGraph.drawPrimalGraph(csp, result);
			
			System.out.println("Thank you for using the program.");

		}
		else if(op == SIM) {
			//Simulate a CN

			for(int n = 0; n < numCSP; n++) {

				//Generate a CN using the CNSimulator 
				System.out.format("Generating CSP: (%d, %d, %d, %f)\n", numNodes, numValues, numConstraints, assignmentRatio);
				CSP csp1 = CNSimulator.generateCN(numNodes, numValues, numConstraints, assignmentRatio);
				//The default variable order is the same as node order (this often produces trivial solutions)
				//To change the order of the variables, put the nodes into the configOrder list as desired
				//All nodes must be in this list
				
				/*System.out.println("Number of nodes: " + csp1.G.V());
			
			ArrayList<String> configOrder = new ArrayList<String>();
			int corder[] = {3,0,4,5,2,1,6};

			System.out.println("New Order");
			for(int m = 0; m < csp1.G.V(); m++) {
				int mo = 0;
				for(String node: csp1.G.vertices()) {
					if(mo == corder[m]) {
						System.out.print(node + ", ");
						configOrder.add(node);
					}
					mo++;
				}
			}*/
				System.out.println();
				csp1.makeConfig();

				//Output the new CN
				CNCreator.write(args[2] + (n + 1) + ".bcn", csp1);

				System.out.println("CSP file generated: " + args[2] + (n + 1) + ".bcn");
/*				
				//Solve it!
				csp1.makeConfig();
				CSPAlgorithm solution = new CSPAlgorithm(csp1);
				ST<String, String> result1 = solution.backtracking();

				System.out.println("Solution");

				// print out the solution
				for (String s : result1)
					System.out.println(s + " = " + result1.get(s));
*/				
				
			}

		}
	}

}
