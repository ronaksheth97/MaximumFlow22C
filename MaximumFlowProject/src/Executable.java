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
	private JPanel panel_North, panel_Center, panel_West, panel_East, panel_South;
	private JButton btnReadFromFile = new JButton("Read From File");
	private JButton btnAddremoveEdge = new JButton("Add/Remove Edge");
	private JButton btnDrawGraph = new JButton("Draw Graph");
	private JButton btnGetMaximumFlow = new JButton("Get Maximum Flow");
	private JButton btnUndoRemoval = new JButton("Undo Removal");
	private JButton btnClear = new JButton("Clear Graph");
	private JButton btnExit = new JButton("Exit");
	private JLabel menuTitle = new JLabel("Team 2: Maximum Flow Problem");
	private JLabel section1_CreateGraph = new JLabel("Step 1: Create your Graph");
	private JLabel section1_ReadStatus = new JLabel("  Status: ");
	private JLabel section1_AddOrRemoveStatus = new JLabel("  Status: ");
	private JLabel section1_UndoRemoveStatus = new JLabel("  Status: ");
	private JLabel section2_ShowGraph = new JLabel("Step 2: Show your Graph");
	private JLabel section3_GetMax = new JLabel("Step 3: Get Maximum Flow!");
	private JLabel section3_MaxFlowStatus = new JLabel("  Status: ");
	private JLabel section4_Next = new JLabel("Next : ");
	private JLabel section4_ClearStatus = new JLabel("  Status: ");
	private Font font_Content = new Font("Arial", 0, 15);
	private Font font_Subtitle = new Font("Arial", 1, 20);

	// type E is specified to String when class is instantiated
	FordFulkerson<String> graph; // Graph
	public static Scanner userScanner = new Scanner(System.in);

	public Executable() {
		init();
	}

	private void init() {
		graph = new FordFulkerson<String>(); // E is specified to String
		
/*Update main menu display of this part by following block as commented
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
*/
		//------------update starts: display of main menu------------
		//set 5 panels of frame_mainMenu
		panel_North = new JPanel();
		panel_North.setBackground(new Color(255, 255, 255));
		menuTitle.setFont(new Font("Arial",Font.BOLD, 30));
		panel_North.add(menuTitle);
		
		panel_Center = new JPanel(new GridLayout(11,1));
		panel_Center.setBackground(new Color(255, 255, 255));
		section1_CreateGraph.setFont(font_Subtitle);
		//section 1 - title
		panel_Center.add(section1_CreateGraph);
		panel_Center.add(new JLabel(""));
		//section 1 - read file
		btnReadFromFile.setFont(font_Content);
		panel_Center.add("ReadFile", btnReadFromFile);
		section1_ReadStatus.setFont(new Font("Arial", 0, 15));
		section1_ReadStatus.setForeground(Color.GRAY);
		panel_Center.add(section1_ReadStatus);
		//section 1 - add or remove
		btnAddremoveEdge.setFont(font_Content);
		panel_Center.add("AddRemove", btnAddremoveEdge);
		section1_AddOrRemoveStatus.setFont(new Font("Arial", 0, 15));
		section1_AddOrRemoveStatus.setForeground(Color.GRAY);
		panel_Center.add(section1_AddOrRemoveStatus);
		//section 1 - undo remove
		btnUndoRemoval.setFont(font_Content);
		panel_Center.add("Undo", btnUndoRemoval);
		section1_UndoRemoveStatus.setFont(new Font("Arial", 0, 15));
		section1_UndoRemoveStatus.setForeground(Color.GRAY);
		panel_Center.add(section1_UndoRemoveStatus);
		
		//section 2
		section2_ShowGraph.setFont(font_Subtitle);
		panel_Center.add(section2_ShowGraph);
		panel_Center.add(new JLabel(""));
		btnDrawGraph.setFont(font_Content);
		panel_Center.add("DrawGraph", btnDrawGraph);
		panel_Center.add(new JLabel(""));
		//section 3
		section3_GetMax.setFont(font_Subtitle);
		panel_Center.add(section3_GetMax);
		panel_Center.add(new JLabel(""));	
		btnGetMaximumFlow.setFont(font_Content);
		panel_Center.add("GetMax", btnGetMaximumFlow);
		section3_MaxFlowStatus.setFont(new Font("Arial", 0, 15));
		section3_MaxFlowStatus.setForeground(Color.GRAY);
		panel_Center.add(section3_MaxFlowStatus);	
		//section 4
		section4_Next.setFont(font_Subtitle);
		panel_Center.add(section4_Next);
		panel_Center.add(new JLabel(""));
		btnClear.setFont(font_Content);
		panel_Center.add("Clear", btnClear);
		section4_ClearStatus.setFont(font_Content);
		section4_ClearStatus.setForeground(Color.GRAY);
		panel_Center.add(section4_ClearStatus);
		btnExit.setFont(font_Content);
		panel_Center.add("Exit", btnExit);

		panel_West = new JPanel();
		panel_West.setBackground(new Color(255, 255, 255));
		panel_West.add(new Label("  "));
		panel_East = new JPanel();
		panel_East.setBackground(new Color(255, 255, 255));
		panel_East.add(new Label("  "));
		panel_South = new JPanel();
		panel_South.setBackground(new Color(255, 255, 255));
		panel_South.add(new Label("  "));

		
		//initialize frame
		frame = new JFrame();
		frame.setTitle("Team 2 : Maximum Flow Problem"); // Window Title
		frame.setSize(600, 500); // Window Dimensions
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center Window
		//add panels
		frame.add(panel_North, BorderLayout.NORTH);
		frame.add(panel_Center, BorderLayout.CENTER);
		frame.add(panel_West, BorderLayout.WEST);
		frame.add(panel_East, BorderLayout.EAST);
		frame.add(panel_South, BorderLayout.SOUTH);
		//------------update ends: display of main menu------------

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
				graph.undoRemove();
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
					//update UI to initialization
					section1_ReadStatus.setText("  Status: ");
					section1_ReadStatus.setForeground(Color.GRAY);
					section1_ReadStatus.updateUI();
					
					section1_AddOrRemoveStatus.setText("  Status: ");
					section1_AddOrRemoveStatus.setForeground(Color.GRAY);
					section1_AddOrRemoveStatus.updateUI();
					
					section1_UndoRemoveStatus.setText("  Status: ");
					section1_UndoRemoveStatus.setForeground(Color.GRAY);
					section1_UndoRemoveStatus.updateUI();
					
					section3_MaxFlowStatus.setText("  Status: ");
					section3_MaxFlowStatus.setForeground(Color.GRAY);
					section3_MaxFlowStatus.updateUI();
					
					section4_ClearStatus.setText("  Status: Graph is all cleared!");
					section4_ClearStatus.setForeground(Color.BLUE);
					section4_ClearStatus.updateUI();
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
				String cityF = vertex1.getText(); // E is specified to String
				String cityT = vertex2.getText(); // E is specified to String
				String maxF = cap.getText();
				int maxFlow;
				try {
					maxFlow = Integer.parseInt(maxF);
				} catch (Exception inv) { // If unable to parse input as int,
											// close window and return
					printErrMsg(err);
					// update msg on main menu
					section1_AddOrRemoveStatus.setText("  Status: Unable to add! Not integer.");
					section1_AddOrRemoveStatus.setForeground(Color.RED);
					section1_AddOrRemoveStatus.updateUI();
					
					section3_MaxFlowStatus.setText("  Status: ");
					section3_MaxFlowStatus.setForeground(Color.GRAY);
					section3_MaxFlowStatus.updateUI();

					return;
				}
				if(maxFlow < 1){
					printErrMsg(err);
					// update msg on main menu
					section1_AddOrRemoveStatus.setText("  Status: Unable to add! Max flow < 1.");
					section1_AddOrRemoveStatus.setForeground(Color.RED);
					section1_AddOrRemoveStatus.updateUI();
					
					section3_MaxFlowStatus.setText("  Status: ");
					section3_MaxFlowStatus.setForeground(Color.GRAY);
					section3_MaxFlowStatus.updateUI();

					return;
				}
				
				graph.addEdge(cityF, cityT, maxFlow);
				

				System.out.println(cityF + " to " + cityT + " with max flow of " + maxFlow + " added"); // Temp to test values returned
				// update msg on main menu
				section1_AddOrRemoveStatus.setText("  Status: [" + cityF + "] to [" + cityT + "] , max flow (" + maxFlow + ") added");
				section1_AddOrRemoveStatus.setForeground(Color.BLUE);
				section1_AddOrRemoveStatus.updateUI();
				
				section3_MaxFlowStatus.setText("  Status: ");
				section3_MaxFlowStatus.setForeground(Color.GRAY);
				section3_MaxFlowStatus.updateUI();

				subMenu.dispose();
			}
		});
		
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String cityF = vertex1.getText(); // E is specified to String
				String cityT = vertex2.getText(); // E is specified to String
				graph.remove(cityF, cityT);
				System.out.println(cityF + " to " + cityT + " removed"); // Temp to test values returned
				//update menu notice
				section1_AddOrRemoveStatus.setText("  Status: " + cityF + " to " + cityT + " removed");
				section1_AddOrRemoveStatus.setForeground(Color.BLUE);
				section1_AddOrRemoveStatus.updateUI();
				
				section3_MaxFlowStatus.setText("  Status: ");
				section3_MaxFlowStatus.setForeground(Color.GRAY);
				section3_MaxFlowStatus.updateUI();

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
	 * Format: "CityFrom, CityTo, Capacity" ***SPACE AFTER COMMA***
	 */
	private void readFromFile(String filename){
		boolean failed = false;
		Scanner file = openInputFile(filename);
		if(file == null){
			printErrMsg("Unable to open " + filename);
			//show file read successfully msg
			section1_ReadStatus.setText("  Status: Unable to open!");
			section1_ReadStatus.setForeground(Color.RED);
			section1_ReadStatus.updateUI();
			
			section3_MaxFlowStatus.setText("  Status: ");
			section3_MaxFlowStatus.setForeground(Color.GRAY);
			section3_MaxFlowStatus.updateUI();
			return;
		}
		while(file.hasNextLine()){
			String line = file.nextLine();
			line = line.trim();
		  //line = line.replaceAll("\\s","");
			String[] aline = line.split(", ");
			if(aline.length != 3){
				printErrMsg("Unable to add: " + line);
			}
			else{
				String from = aline[0];
				String to = aline[1];
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
					graph.addEdge(from, to, cap);
				}
			}
			failed = false; // Reset for next line
			//show file read successfully msg
			section1_ReadStatus.setText("  Status: Read successfully!");
			section1_ReadStatus.setForeground(Color.BLUE);
			section1_ReadStatus.updateUI();
		
			section3_MaxFlowStatus.setText("  Status: ");
			section3_MaxFlowStatus.setForeground(Color.GRAY);
			section3_MaxFlowStatus.updateUI();
		}
		//show file read successfully msg
		section1_ReadStatus.setText("  Read status: Successfully!");
		section1_ReadStatus.setForeground(Color.BLUE);
		section1_ReadStatus.updateUI();
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
				Vertex<String> sourceOfGraph = graph.getVertex(sourceS); // local variable "from" is changed to "sourceOfGraph"
				Vertex<String> sinkOfGraph = graph.getVertex(sinkS); // local variable "to" is changed to "sinkOfGraph"
				
				subMenu.dispose();
				
				openDrawGraph();
				
				System.out.println("Path from [ " + sourceOfGraph.data.toString() + " ] to [ " + sinkOfGraph.data.toString() + " ]: " + graph.hasAugmentingPath(sourceOfGraph, sinkOfGraph)); // add [] to show a better display
				System.out.println();
		        	graph.applyFordFulkerson(sourceOfGraph, sinkOfGraph);
		        	System.out.println("Max flow from source to sink: " + graph.getMaxFlow()); // "from s to t" is changed to "from source to sink"
				//update menu notice
				section3_MaxFlowStatus.setText("  Max flow from [" + sourceOfGraph.data + "] to [" + sinkOfGraph.data + "] = " + graph.getMaxFlow());
			        section3_MaxFlowStatus.setForeground(Color.BLUE);
			        section3_MaxFlowStatus.updateUI();

				//printMaxFlow(sourceV, sinkV); Print Maxmimum Flow
			}
		});
		subMenu.setVisible(true);
	}
	
	private void printMaxFlow(Vertex<String> source, Vertex<String> sink){ // E is specified to String
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
