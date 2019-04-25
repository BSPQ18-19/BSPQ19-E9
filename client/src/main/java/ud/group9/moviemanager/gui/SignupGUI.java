package ud.group9.moviemanager.gui;
 
import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.api.exceptions.SignupException;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JButton btnSignUp;
	private JTextField username;
	private JPasswordField password;
	private JPasswordField passwordConf;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					new SignupGUI(new MovieManagerClient("127.0.0.1", 8000));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public SignupGUI() {
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.ORANGE);
		this.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel(MovieManagerClient.getBundle().getString("username") + ":");
		lblUsername.setBounds(10, 60, 79, 14);
		this.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel(MovieManagerClient.getBundle().getString("password") + ":");
		lblPassword.setBounds(10, 85, 79, 14);
		this.getContentPane().add(lblPassword);
		
		JLabel lblConfirm = new JLabel(MovieManagerClient.getBundle().getString("confirmpassword") + ":");
		lblConfirm.setBounds(10, 110, 122, 14);
		this.getContentPane().add(lblConfirm);
		
		JLabel lblAreYouNew = new JLabel(MovieManagerClient.getBundle().getString("welcome"));
		lblAreYouNew.setBounds(30, 22, 250, 14);
		this.getContentPane().add(lblAreYouNew);
		
		username = new JTextField();
		username.setBackground(new Color(255, 222, 173));
		username.setBounds(139, 57, 95, 20);
		this.getContentPane().add(username);
		username.setColumns(10);

		password = new JPasswordField();
		password.setBackground(new Color(255, 222, 173));
		password.setBounds(139, 82, 95, 20);
		this.getContentPane().add(password);
		password.setColumns(10);

		passwordConf = new JPasswordField();
		passwordConf.setBackground(new Color(255, 222, 173));
		passwordConf.setBounds(139, 107, 95, 20);
		this.getContentPane().add(passwordConf);
		passwordConf.setColumns(10);
		
		btnSignUp = new JButton(MovieManagerClient.getBundle().getString("signup"));
		btnSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String usname = username.getText();
				String pswrd = "";

				if (password.getPassword().length == 0) {
					JOptionPane.showMessageDialog(btnSignUp, MovieManagerClient.getBundle().getString("emptypassword"));
					return;
				}
				if(!(String.valueOf(password.getPassword()).equals(String.valueOf(passwordConf.getPassword())))) {
					JOptionPane.showMessageDialog(btnSignUp, MovieManagerClient.getBundle().getString("differentpasswords"));
					return;
				}
				pswrd = String.valueOf(password.getPassword());
				try {
					MovieManagerClient.SignUp(usname, pswrd);
					JOptionPane.showMessageDialog(btnSignUp, MovieManagerClient.getBundle().getString("signupsuccessful"));
					MovieManagerClient.LogIn(usname, pswrd);
					setVisible(false);
					new MovieManagerGUI();
				} catch (SignupException e1) {
					JOptionPane.showMessageDialog(btnSignUp, e1);
				}
			}
		});
		btnSignUp.setBackground(new Color(255, 140, 0));
		btnSignUp.setForeground(Color.BLACK);
		btnSignUp.setBounds(75, 161, 89, 23);
		this.getContentPane().add(btnSignUp);
		this.setTitle("SignupGUI");
		this.setBounds(100, 100, 300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
