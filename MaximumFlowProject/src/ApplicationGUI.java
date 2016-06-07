import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ApplicationGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationGUI window = new ApplicationGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ApplicationGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnReadFromFile = new JButton("Read From File");
		
		JButton btnAddremoveEdge = new JButton("Add/Remove Edge");
		btnAddremoveEdge.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Add_Remove newWindow = new Add_Remove();
				newWindow.setVisible(true);
			}
		});
		
		JButton btnDrawGraph = new JButton("Draw Graph");
		
		JButton btnGetMaximumFlow = new JButton("Get Maximum Flow");
		
		JButton btnUndoRemoval = new JButton("Undo Removal");
		
		JButton btnExit = new JButton("Exit");
		
		JLabel lblMaximumFlow = new JLabel("Maximum Flow ");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnReadFromFile)
								.addComponent(btnAddremoveEdge)
								.addComponent(btnDrawGraph)
								.addComponent(btnGetMaximumFlow)
								.addComponent(btnUndoRemoval)
								.addComponent(btnExit)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(171)
							.addComponent(lblMaximumFlow)))
					.addContainerGap(180, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(8)
					.addComponent(lblMaximumFlow)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnReadFromFile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddremoveEdge)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDrawGraph)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnGetMaximumFlow)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnUndoRemoval)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnExit)
					.addContainerGap(44, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
