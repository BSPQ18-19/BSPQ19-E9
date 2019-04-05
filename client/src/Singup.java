package src; 
 
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Singup {

	private JFrame frmSingup;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Singup window = new Singup();
					window.frmSingup.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Singup() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSingup = new JFrame();
		frmSingup.setAlwaysOnTop(true);
		frmSingup.setResizable(false);
		frmSingup.getContentPane().setBackground(Color.ORANGE);
		frmSingup.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 60, 79, 14);
		frmSingup.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setBounds(10, 85, 79, 14);
		frmSingup.getContentPane().add(lblPassword);
		
		JLabel lblConfirm = new JLabel("Confirm \r\n\r\npassword:");
		lblConfirm.setBounds(10, 110, 122, 14);
		frmSingup.getContentPane().add(lblConfirm);
		
		JLabel lblAreYouNew = new JLabel("Are you new? ");
		lblAreYouNew.setBounds(83, 22, 81, 14);
		frmSingup.getContentPane().add(lblAreYouNew);
		
		textField = new JTextField();
		textField.setBackground(new Color(255, 222, 173));
		textField.setBounds(139, 57, 95, 20);
		frmSingup.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBackground(new Color(255, 222, 173));
		textField_1.setBounds(139, 82, 95, 20);
		frmSingup.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBackground(new Color(255, 222, 173));
		textField_2.setBounds(139, 107, 95, 20);
		frmSingup.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton = new JButton("Singup");
		btnNewButton.setBackground(new Color(255, 140, 0));
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setBounds(75, 161, 89, 23);
		frmSingup.getContentPane().add(btnNewButton);
		frmSingup.setTitle("Singup");
		frmSingup.setBounds(100, 100, 250, 250);
		frmSingup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
