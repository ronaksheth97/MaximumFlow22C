import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import javax.swing.*;

public class Executable {

	private JFrame frame;
	private JButton btnReadFromFile = new JButton("Read From File");
	private JButton btnAddremoveEdge = new JButton("Add/Remove Edge");
	private JButton btnDrawGraph = new JButton("Draw Graph");
	private JButton btnGetMaximumFlow = new JButton("Get Maximum Flow");
	private JButton btnUndoRemoval = new JButton("Undo Removal");
	private JButton btnExit = new JButton("Exit");
	private JLabel menuTitle = new JLabel("Maximum Flow Problem");

	public Executable() {
		init();
	}

	private void init() {
		frame = new JFrame();
		frame.setSize(200, 230);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		Container c = frame.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
		c.add("Title", menuTitle);
		c.add("ReadFile", btnReadFromFile);
		c.add("AddRemove", btnAddremoveEdge);
		c.add("DrawGraph", btnDrawGraph);
		c.add("GetMax", btnGetMaximumFlow);
		c.add("Undo", btnUndoRemoval);
		c.add("Exit", btnExit);

		// Click Add/Remove Button
		btnAddremoveEdge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openAddRemoveMenu();
			}
		});
		// Click Exit Button
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});

		frame.setVisible(true);
	}

	// Sub-Menu for Add/Remove Button
	private void openAddRemoveMenu() {
		final JFrame subMenu = new JFrame();
		subMenu.setSize(200, 230);
		subMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		subMenu.setLocationRelativeTo(null);
		Container c = subMenu.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

		final JTextField vertex1 = new JTextField("City From");
		final JTextField vertex2 = new JTextField("City To");
		final JTextField cap = new JTextField("Capacity");
		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");

		c.add(vertex1);
		c.add(vertex2);
		c.add(cap);
		c.add(add);
		c.add(remove);

		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String cityF = vertex1.getText();
				String cityT = vertex2.getText();
				String maxF = cap.getText();
				int maxFlow;
				try {
					maxFlow = Integer.parseInt(maxF);
				} catch (Exception inv) { // If unable to parse input as int,
											// close window and return
					JFrame err = new JFrame();

					JLabel errMsg = new JLabel("     Invalid input, not added", 2);
					err.setSize(200, 100);
					err.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					err.setLocationRelativeTo(null);
					err.add(errMsg);
					err.setVisible(true);
					subMenu.dispose();
					return;
				}

				System.out.println(cityF + " to " + cityT + " with max flow of " + maxFlow);
				subMenu.dispose();
			}
		});
		// frame.setVisible(true);
		subMenu.setVisible(true);

	}
}
