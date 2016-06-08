import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Executable {
	
	private JFrame frame;
	private JButton btnReadFromFile = new JButton("Read From File");
	private JButton btnAddremoveEdge = new JButton("Add/Remove Edge");
	private JButton btnDrawGraph = new JButton("Draw Graph");
	private JButton btnGetMaximumFlow = new JButton("Get Maximum Flow");
	private JButton btnUndoRemoval = new JButton("Undo Removal");
	private JButton btnExit = new JButton("Exit");
	private JLabel  menuTitle = new JLabel("Maximum Flow Problem");
	
	public Executable(){
		init();
	}
	
	private void init(){
		frame = new JFrame();
		frame.setSize(200, 230);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frame.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
		c.add("Title", menuTitle);
		c.add("ReadFile", btnReadFromFile);
		c.add("AddRemove", btnAddremoveEdge);
		c.add("DrawGraph", btnDrawGraph);
		c.add("GetMax", btnGetMaximumFlow);
		c.add("Undo", btnUndoRemoval);
		c.add("Exit", btnExit);
		
		btnAddremoveEdge.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				openAddRemoveMenu();
			}
		});
		frame.setVisible(true);
	}
	
	private void openAddRemoveMenu(){
		JFrame subMenu = new JFrame();
		subMenu.setSize(200, 230);
		subMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container c = subMenu.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
		
		JTextField vertex1 = new JTextField("City From");
		JTextField vertex2 = new JTextField("City To");
		JTextField cap = new JTextField("Capacity");
		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		
		c.add(vertex1);
		c.add(vertex2);
		c.add(cap);
		c.add(add);
		c.add(remove);
		
		subMenu.setVisible(true);
		
		
	}
}
