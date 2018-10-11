import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JComboBox;

public class GUI {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
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
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setSize(frame.getWidth(), 20);
		panel.setLayout(new GridLayout(1, 2, 0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new FlowLayout(FlowLayout.LEADING));
		panel.add(panel_2);
		
		JCheckBox chckbxFacebook = new JCheckBox();
		panel_2.add(chckbxFacebook);
		JLabel labelFacebook = new JLabel(new ImageIcon("src/resources/facebook.png"));
		panel_2.add(labelFacebook);
		
		JCheckBox chckbxTwitter = new JCheckBox();
		panel_2.add(chckbxTwitter);
		JLabel labelTwitter = new JLabel(new ImageIcon("src/resources/twitter.png"));
		panel_2.add(labelTwitter);
		
		JCheckBox chckbxEmail = new JCheckBox();
		panel_2.add(chckbxEmail);
		JLabel labelEmail = new JLabel(new ImageIcon("src/resources/email.png"));
		panel_2.add(labelEmail);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new FlowLayout(FlowLayout.TRAILING));
		panel.add(panel_3);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("Defina um período");
		comboBox.addItem("Últimas 24h");
		comboBox.addItem("Última semana");
		comboBox.addItem("Último mês");
		panel_3.add(comboBox);
		
		textField = new JTextField();
		panel_3.add(textField);
		textField.setColumns(10);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JButton btnNewButton = new JButton();
		panel_3.add(btnNewButton);
		btnNewButton.setIcon(new ImageIcon("src/resources/magnifier-tool.png"));
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(1, 2, 0, 0));
		
		JList list = new JList();
		panel_1.add(list);
		
		JTextArea textArea = new JTextArea();
		panel_1.add(textArea);
	}

}
