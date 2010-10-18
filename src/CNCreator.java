import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * Creates a .bcn file given a CSP.
 * </p>
 * <p>
 * This class converts a sybmolic CSP into an indexed text file format.
 * </p>
 * @author adnan
 *
 */
public class CNCreator {
	
	
	public static void write(String writeFile, CSP csp) {
	
	//open file for writing
	File fileOut = new File(writeFile);
	
	//create one if necessary
	if(!fileOut.canWrite()) {
		System.out.println("File " + writeFile + " not found. Creating file.");
		try {
			if(writeFile.contains("/")) {
				//Need to create the directory - Unix style
				//System.out.println("Unix file system - creating directory: " + writeFile.substring(0, writeFile.lastIndexOf("/")));
				File dirs = new File(writeFile.substring(0, writeFile.lastIndexOf("/")));
				dirs.mkdirs();
			}
			else if(writeFile.contains("\\")) {
				//Need to create the directory - Windows style
				File dirs = new File(writeFile.substring(writeFile.lastIndexOf("\\")));
				dirs.mkdirs();
			}
			fileOut.createNewFile();
		} catch (IOException e) {
			System.out.println("Could not create file: " + writeFile);
			e.printStackTrace();
		}
	}
	try {
		
		PrintWriter pOut = new PrintWriter(fileOut);
		
		//write first line
		pOut.format("%d  #_of_nodes\n\n", csp.G.V());
		
		pOut.flush();
		
		
	//for each node
	int n = 0;
	for(String node: csp.G.vertices()) {
		
		pOut.format("%d  #_of_states_var_%d\n", csp.domainTable.get(node).size(), csp.vIndex.get(node));
		pOut.format("%s  node_label\n", node);
		for(String domainValue: csp.domainTable.get(node)) {
			pOut.format("%s\n", domainValue);
		}
		pOut.format("%d  ", csp.G.degree(node));
		for(String adj: csp.G.adjacentTo(node)) {
			pOut.format("%d ", csp.vIndex.get(adj));
		}
		pOut.println("neighbor");
		pOut.format("%d  #constraints_stored\n", csp.constraintsTable.get(node).size());
		for(String cons: csp.constraintsTable.get(node)) {
			String legal_config = "";
			//pOut.format("%d  %d  nb/#legal_config\n", csp.vIndex.get(cons), csp.constraintsTable.get(node).get(cons).size());
			int dc = 0;
			for(String ncons: csp.constraintsTable.get(node).get(cons)) {
				for(String domSet: csp.constraintsTable.get(node).get(cons).get(ncons)) {        					
					dc++;
					legal_config += String.format("%d %s ; ", csp.dIndex.get(node).get(ncons), csp.dIndex.get(node).get(domSet));
					if((dc % 5) == 0) {
						legal_config += "\n";
						//pOut.println();
						//dc = 0;
					}
				}
			}
			pOut.format("%d  %d  nb/#legal_config\n", csp.vIndex.get(cons), dc);
			pOut.print(legal_config);
			if((dc % 5) != 0) {
				pOut.println();
			}
		}
		pOut.format("%d %d  Coordinate\n", csp.coordinatesTable.get(node).get(0), csp.coordinatesTable.get(node).get(1));
		
		pOut.println();
		pOut.flush();
		
	}
	
	
	} catch (Exception e) {
		System.out.println("Cannot output to file");
		e.printStackTrace();
	}
	
	}

}
