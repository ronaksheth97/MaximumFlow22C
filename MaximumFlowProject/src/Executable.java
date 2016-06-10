import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Scanner;

import javax.swing.*;

public class Executable<E> {

	private JFrame frame;
	private JButton btnReadFromFile = new JButton("Read From File");
	private JButton btnAddremoveEdge = new JButton("Add/Remove Edge");
	private JButton btnDrawGraph = new JButton("Draw Graph");
	private JButton btnGetMaximumFlow = new JButton("Get Maximum Flow");
	private JButton btnUndoRemoval = new JButton("Undo Removal");
	private JButton btnClear = new JButton("Clear Graph");
	private JButton btnExit = new JButton("Exit");
	private JLabel menuTitle = new JLabel("       Maximum Flow Problem");
	FordFulkerson<E> graph;
	public static Scanner userScanner = new Scanner(System.in);

	public Executable() {
		init();
	}

	private void init() {
		graph = new FordFulkerson<E>();
		
		frame = new JFrame();
		frame.setSize(200, 240); // Window Dimensions
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center Window
		Container c = frame.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS)); // Layout of Panel
		c.add("Title", menuTitle);
		c.add("ReadFile", btnReadFromFile); // Add all Buttons + Title For Main screen
		c.add("AddRemove", btnAddremoveEdge);
		c.add("DrawGraph", btnDrawGraph);
		c.add("GetMax", btnGetMaximumFlow);
		c.add("Undo", btnUndoRemoval);
		c.add("Clear", btnClear);
		c.add("Exit", btnExit);

		// Click Read From File Button
		btnReadFromFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				JOptionPane getInputFile = new JOptionPane();
				String fileName = getInputFile.showInputDialog("Please enter filename");
				if(fileName == null) return;
				readFromFile(fileName);
			}
		});
		
		// Click Add/Remove Button
		btnAddremoveEdge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openAddRemoveMenu(); // Open sub-menu
			}
		});
		
		// Click Draw Graph Button
		btnDrawGraph.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				 openDrawGraph();
			}
		});
		
		// Click Get Maximum Flow Button
		btnGetMaximumFlow.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				openGetMFlowMenu();
			}
		});
		
		btnUndoRemoval.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//graph.undo(); Undo Removal
			}
		});
		
		// Click Clear Graph Button
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to empty the graph?","Warning", dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					graph.clear();
				}
				
			}
		});
		
		// Click Exit Button
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close window
			}
		});

		frame.setVisible(true);
	}

	// Sub-Menu for Add/Remove Button
	private void openAddRemoveMenu() {
		final JFrame subMenu = new JFrame();
		subMenu.setSize(200, 230); // Dimensions
		subMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		subMenu.setLocationRelativeTo(null); // Center
		Container c = subMenu.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS)); // Panel Layout

		final JTextField vertex1 = new JTextField("City From");
		final JTextField vertex2 = new JTextField("City To");
		final JTextField cap = new JTextField("Capacity");
		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		JButton cancel = new JButton("Cancel");
		c.add(vertex1);
		c.add(vertex2);
		c.add(cap);
		c.add(add);
		c.add(remove);
		c.add(cancel);

		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String err = "Invalid Capacity";
				E cityF = (E) vertex1.getText();
				E cityT = (E) vertex2.getText();
				String maxF = cap.getText();
				int maxFlow;
				try {
					maxFlow = Integer.parseInt(maxF);
				} catch (Exception inv) { // If unable to parse input as int,
											// close window and return
					printErrMsg(err);
					return;
				}
				if(maxFlow < 1){
					printErrMsg(err);
					return;
				}
				
				graph.addEdge(cityF, cityT, maxFlow);
				

				System.out.println(cityF + " to " + cityT + " with max flow of " + maxFlow + " added"); // Temp to test values returned
				subMenu.dispose();
			}
		});
		
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				E cityF = (E) vertex1.getText();
				E cityT = (E) vertex2.getText();
				graph.remove(cityF, cityT);
				System.out.println(cityF + " to " + cityT + " removed"); // Temp to test values returned
				subMenu.dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				subMenu.dispose();
			}
		});
		// frame.setVisible(true);
		subMenu.setVisible(true);

	}
	
	private void printErrMsg(String msg){
		Object[] options = {"OK"};
		JOptionPane.showOptionDialog(null, msg, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	}
	// Method to open file
	public static Scanner openInputFile(String filename) {
		Scanner scanner = null;
		File file = new File(filename);

		try {
			scanner = new Scanner(file);
		} // end try
		catch (FileNotFoundException fe) {
			System.out.println("Can't open input file\n");
			return null; // array of 0 elements
		} // end catch
		return scanner;
	}
	/* Method to read file (adds edges directly to graph)
	 * Format: "CityFrom,CityTo,Capacity" ***NO SPACE AFTER COMMA***
	 */
	private void readFromFile(String filename){
		boolean failed = false;
		Scanner file = openInputFile(filename);
		if(file == null){
			printErrMsg("Unable to open " + filename);
			return;
		}
		while(file.hasNextLine()){
			String line = file.nextLine();
			line = line.trim();
		  //line = line.replaceAll("\\s","");
			String[] aline = line.split(",");
			if(aline.length != 3){
				printErrMsg("Unable to add: " + line);
			}
			else{
				String from = aline[0];
				E fromE = (E) from;
				String to = aline[1];
				E toE = (E) to;
				String caps = aline[2];
				int cap = -1;
				try {
					cap = Integer.parseInt(caps);
				} catch (Exception inv) { // If unable to parse input as int,
											// close window and return
					printErrMsg("Unable to add: " + line);
					failed = true;
				}
				if(cap < 1 && !failed){ // If capacity is 0 or negative
					printErrMsg("Unable to add: " + line);
					failed = true;
				}
				if(!failed){ // Do not add if failed
					graph.addEdge(fromE, toE, cap);
				}
			}
			failed = false; // Reset for next line
		}
	}
	
	// Sub-Menu for Draw Graph Button
	private void openDrawGraph(){
		JFrame subMenu = new JFrame();
		JButton btnExport = new JButton("Export");
		final JTextArea textArea = new JTextArea(50, 10);
		textArea.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		// keeps reference of standard output stream;
		PrintStream standardOut = System.out;
		// re-assigns standard output stream and error output stream
		System.setOut(printStream);
		System.setErr(printStream);
		
		// Creates the GUI
		subMenu.setLayout(new GridBagLayout());
		subMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		subMenu.setSize(480, 320);
		subMenu.setLocationRelativeTo(null); // center
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.anchor = GridBagConstraints.WEST;
		
		subMenu.add(btnExport, constraints);
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		
		
		
		subMenu.add(new JScrollPane(textArea), constraints);
		
		//displayGraph();
		graph.showAdjTable();
		// Test output to Export
		//System.out.println(textArea.getText());
		
		btnExport.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JOptionPane getOutputFile = new JOptionPane();
				String fileName = getOutputFile.showInputDialog("Please enter filename");
				PrintWriter out;
				try{
					out = new PrintWriter(fileName);
				}
				catch(FileNotFoundException ex){
					System.err.print("\nInvalid File");
					return;
				}
				catch(NullPointerException nx){
					System.err.print("\nOutput Not Exported");
					return;
				}
				out.println(textArea.getText());
				System.out.println("File Sucessfully Exported to " + fileName);
				out.close();
				//System.out.println("\nExporting to: " + fileName) Temp to ensure it works (prints to screen)
			}
		});
		
		subMenu.setVisible(true);
		
	}
	
	// Sub-Menu for Get Maximum Flow Button
	private void openGetMFlowMenu(){
		// Window for inputting source and sink names
		final JFrame subMenu = new JFrame();
		final JTextField source = new JTextField("Source");
		final JTextField sink = new JTextField("Sink");
		final JButton btncancel = new JButton("Cancel");
		final JButton btncontinue = new JButton("Continue");
		
		subMenu.setSize(200, 230); // Dimensions
		subMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		subMenu.setLocationRelativeTo(null); // Center
		Container c = subMenu.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS)); // Panel Layout
		
		c.add("Source", source);
		c.add("Sink", sink);
		
		c.add("Cancel", btncancel);
		c.add("Continue", btncontinue);
		
		btncancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				subMenu.dispose();
			}
		});
		
		btncontinue.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String sourceS = source.getText();
				String sinkS = sink.getText();
				E sourceE = (E) sourceS;
				E sinkE = (E) sinkS;
				Vertex<E> sourceV = graph.getVertex(sourceE);
				Vertex<E> sinkV = graph.getVertex(sinkE);
				
				
				subMenu.dispose();
				System.out.println(sourceS + " to " + sinkS + " shown"); // Temp to test values returned
				
				openDrawGraph();
				//printMaxFlow(sourceV, sinkV); Print Maxmimum Flow
			}
		});
		subMenu.setVisible(true);
	}
	
	private void printMaxFlow(Vertex<E> source, Vertex<E> sink){
		graph.applyFordFulkerson(source, sink);
		int maxFlow = graph.getMaxFlow();
		System.out.println("Maximum Flow: " + maxFlow);
	}
	
	private void displayGraph(){
		System.out.println("Testing displayGraph Method");
		System.out.println("San Francisco --- 4 --- San Jose --- 3 --- Los Angeles");
		System.out.println("San Francisco --- 6 --- Sacramento --- 10 --- Los Angeles");
	}
	
}
