import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;

import javax.swing.JApplet;
import javax.swing.JFrame;


public class PrimalGraph extends JApplet {

	CSP csp;
	ST<String, String> result;
	
	public PrimalGraph(CSP csp, ST<String, String> result) {
		// TODO Auto-generated constructor stub
		this.csp = csp;
		this.result = result;
	}

	public static void drawPrimalGraph(CSP csp, ST<String, String> result) {
		JFrame f = new JFrame("Primal Graph");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		JApplet applet = new PrimalGraph(csp, result);
		f.getContentPane().add("Center", applet);
		applet.init();
		f.setSize(new Dimension(800, 800));
		f.show();
	}
	
	public void init() {
		setBackground(Color.white);
		setForeground(Color.white);
	}
	
	public void paint(Graphics gix) {
		Graphics2D gix2 = (Graphics2D)gix;
		gix2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		gix2.setPaint(Color.gray);
		
		//gix2.draw(new Line2D.Double(1,1,20,20));
		
		for(String s: csp.G.vertices()) {
			int x1 = csp.coordinatesTable.get(s).get(0);
			int y1 = csp.coordinatesTable.get(s).get(1);
			for(String adj: csp.G.adjacentTo(s)) {
				int x2 = csp.coordinatesTable.get(adj).get(0);
				int y2 = csp.coordinatesTable.get(adj).get(1);
				//System.out.format("Drawing: %d, %d, %d, %d", x1,y1,x2,y2);
				gix2.drawString(s + " = " + result.get(s), x1, y1);
				gix2.draw(new Line2D.Double(x1,y1,x2,y2));
			}
		}

	}
	
}
