package ud.group9.moviemanager.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginGUI {

	private JFrame frmLogin;
	private JTextField username;
	private JTextField password;
	private JButton btnLogin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI window = new LoginGUI();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setAlwaysOnTop(true);
		frmLogin.setTitle("LoginGUI");
		frmLogin.setBackground(Color.RED);
		frmLogin.getContentPane().setBackground(Color.ORANGE);
		frmLogin.getContentPane().setLayout(null);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPassword.setLabelFor(frmLogin);
		lblPassword.setBounds(62, 90, 61, 14);
		frmLogin.getContentPane().add(lblPassword);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBackground(new Color(240, 240, 240));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel.setLabelFor(frmLogin);
		lblNewLabel.setBounds(62, 59, 60, 14);
		frmLogin.getContentPane().add(lblNewLabel);
		
		username = new JTextField();
		username.setBackground(new Color(255, 228, 181));
		username.setBounds(132, 56, 112, 20);
		frmLogin.getContentPane().add(username);
		username.setColumns(10);

		btnLogin = new JButton("LoginGUI");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String usname = username.getText();
				String pswrd = password.getText();
				
				if(usname.equals("username") && pswrd.equals("password")) {
					
					JOptionPane.showMessageDialog(btnLogin, "LoginGUI succesfull!");
					
				}
				
				else {
					
					JOptionPane.showMessageDialog(btnLogin, "LoginGUI error!");
					
				}
			}
		});
		
		password = new JTextField();
		password.setBackground(new Color(255, 222, 173));
		password.setBounds(132, 87, 112, 20);
		frmLogin.getContentPane().add(password);
		password.setColumns(10);
		btnLogin.setBackground(new Color(255, 140, 0));
		btnLogin.setBounds(97, 127, 89, 23);
		frmLogin.getContentPane().add(btnLogin);
		
		JButton btnSingup = new JButton("SingUp");
		btnSingup.setBackground(new Color(255, 140, 0));
		btnSingup.setBounds(97, 200, 89, 23);
		frmLogin.getContentPane().add(btnSingup);
		
		btnSingup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new SingupGUI();
				SingupGUI.main(null);;
			    
				
			}
		});
		
		
		JLabel lblAreYouNew = new JLabel("Are you new?");
		lblAreYouNew.setBounds(97, 175, 88, 14);
		frmLogin.getContentPane().add(lblAreYouNew);
		frmLogin.setBounds(100, 100, 300, 300);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
