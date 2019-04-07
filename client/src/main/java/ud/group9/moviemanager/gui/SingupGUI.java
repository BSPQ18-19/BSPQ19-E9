package ud.group9.moviemanager.gui;
 
import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.api.exceptions.SignupException;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SingupGUI {

	private MovieManagerClient mmc;
	private JFrame frmSingup;
	private JButton btnSignUp;
	private JTextField username;
	private JPasswordField password;
	private JPasswordField passwordConf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SingupGUI window = new SingupGUI(new MovieManagerClient("127.0.0.1", 8000));
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
	public SingupGUI(MovieManagerClient mmc) {
		this.mmc = mmc;
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
		
		username = new JTextField();
		username.setBackground(new Color(255, 222, 173));
		username.setBounds(139, 57, 95, 20);
		frmSingup.getContentPane().add(username);
		username.setColumns(10);

		password = new JPasswordField();
		password.setBackground(new Color(255, 222, 173));
		password.setBounds(139, 82, 95, 20);
		frmSingup.getContentPane().add(password);
		password.setColumns(10);

		passwordConf = new JPasswordField();
		passwordConf.setBackground(new Color(255, 222, 173));
		passwordConf.setBounds(139, 107, 95, 20);
		frmSingup.getContentPane().add(passwordConf);
		passwordConf.setColumns(10);
		
		btnSignUp = new JButton("SingupGUI");
		btnSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String usname = username.getText();
				String pswrd = "";

				if (password.getPassword().length == 0) {
					JOptionPane.showMessageDialog(btnSignUp, "passwords can't be empty");
					return;
				}

				if(!password.getPassword().equals(passwordConf.getPassword())) {
					JOptionPane.showMessageDialog(btnSignUp, "passwords do not match");
					return;
				}
				pswrd = String.valueOf(password.getPassword());
				try {
					mmc.SignUp(usname, pswrd);
					JOptionPane.showMessageDialog(btnSignUp, "signup successful");
					mmc.LogIn(usname, pswrd);

					new MovieManagerGUI();
					MovieManagerGUI.main(null);
				} catch (SignupException e1) {
					JOptionPane.showMessageDialog(btnSignUp, e1);
				}
			}
		});
		btnSignUp.setBackground(new Color(255, 140, 0));
		btnSignUp.setForeground(Color.BLACK);
		btnSignUp.setBounds(75, 161, 89, 23);
		frmSingup.getContentPane().add(btnSignUp);
		frmSingup.setTitle("SingupGUI");
		frmSingup.setBounds(100, 100, 250, 250);
		frmSingup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
