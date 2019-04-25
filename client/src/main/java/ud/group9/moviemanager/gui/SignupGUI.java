package ud.group9.moviemanager.gui;
 
import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.api.exceptions.SignupException;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

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
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JLabel lblAreYouNew = new JLabel(MovieManagerClient.getBundle().getString("welcome"));
		lblAreYouNew.setHorizontalAlignment(SwingConstants.CENTER);
		this.getContentPane().add(lblAreYouNew, BorderLayout.NORTH);
		
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
		this.getContentPane().add(btnSignUp, BorderLayout.SOUTH);
		
		JPanel panelUserData = new JPanel();
		panelUserData.setBackground(Color.ORANGE);
		getContentPane().add(panelUserData);
		GridBagLayout gbl_panelUserData = new GridBagLayout();
		gbl_panelUserData.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelUserData.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panelUserData.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelUserData.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelUserData.setLayout(gbl_panelUserData);
								
								JLabel lblUsername = new JLabel(MovieManagerClient.getBundle().getString("username") + ":");
								lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
								GridBagConstraints gbc_lblUsername = new GridBagConstraints();
								gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
								gbc_lblUsername.gridx = 1;
								gbc_lblUsername.gridy = 1;
								panelUserData.add(lblUsername, gbc_lblUsername);
								
								username = new JTextField();
								GridBagConstraints gbc_username = new GridBagConstraints();
								gbc_username.insets = new Insets(0, 0, 5, 0);
								gbc_username.gridx = 2;
								gbc_username.gridy = 1;
								panelUserData.add(username, gbc_username);
								username.setBackground(new Color(255, 222, 173));
								username.setColumns(10);
								
								JLabel lblPassword = new JLabel(MovieManagerClient.getBundle().getString("password") + ":");
								GridBagConstraints gbc_lblPassword = new GridBagConstraints();
								gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
								gbc_lblPassword.gridx = 1;
								gbc_lblPassword.gridy = 2;
								panelUserData.add(lblPassword, gbc_lblPassword);
								
										password = new JPasswordField();
										GridBagConstraints gbc_password = new GridBagConstraints();
										gbc_password.insets = new Insets(0, 0, 5, 0);
										gbc_password.gridx = 2;
										gbc_password.gridy = 2;
										panelUserData.add(password, gbc_password);
										password.setBackground(new Color(255, 222, 173));
										password.setColumns(10);
								
								JLabel lblConfirm = new JLabel(MovieManagerClient.getBundle().getString("confirmpassword") + ":");
								GridBagConstraints gbc_lblConfirm = new GridBagConstraints();
								gbc_lblConfirm.insets = new Insets(0, 0, 0, 5);
								gbc_lblConfirm.gridx = 1;
								gbc_lblConfirm.gridy = 3;
								panelUserData.add(lblConfirm, gbc_lblConfirm);
								
										passwordConf = new JPasswordField();
										GridBagConstraints gbc_passwordConf = new GridBagConstraints();
										gbc_passwordConf.gridx = 2;
										gbc_passwordConf.gridy = 3;
										panelUserData.add(passwordConf, gbc_passwordConf);
										passwordConf.setBackground(new Color(255, 222, 173));
										passwordConf.setColumns(10);
		this.setTitle("SignupGUI");
		this.setBounds(100, 100, 300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
