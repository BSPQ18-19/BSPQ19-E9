package ud.group9.moviemanager.gui;

import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.api.exceptions.SignupException;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.Color;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private MovieManagerClient mmc;
	private JTextField username;
	private JPasswordField password;
	private JButton btnLogin;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					new LoginGUI(new MovieManagerClient("127.0.0.1", 8000));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public LoginGUI(MovieManagerClient mmc) {
		this.mmc = mmc;
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setAlwaysOnTop(true);
		this.setTitle("LoginGUI");
		this.setBackground(Color.RED);
		this.getContentPane().setBackground(Color.ORANGE);
		this.getContentPane().setLayout(null);
		
		JLabel lblPassword = new JLabel(mmc.getBundle().getString("password") + ":");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPassword.setLabelFor(this);
		lblPassword.setBounds(62, 90, 61, 14);
		this.getContentPane().add(lblPassword);
		
		JLabel lblNewLabel = new JLabel(mmc.getBundle().getString("username") + ":");
		lblNewLabel.setBackground(new Color(240, 240, 240));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel.setLabelFor(this);
		lblNewLabel.setBounds(62, 59, 60, 14);
		this.getContentPane().add(lblNewLabel);
		
		username = new JTextField();
		username.setBackground(new Color(255, 228, 181));
		username.setBounds(132, 56, 112, 20);
		this.getContentPane().add(username);
		username.setColumns(10);

		btnLogin = new JButton(mmc.getBundle().getString("login"));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String usname = username.getText();
				String pswrd = String.valueOf(password.getPassword());

				try {
					if (mmc.LogIn(usname, pswrd)){
						JOptionPane.showMessageDialog(btnLogin, mmc.getBundle().getString("loginsuccessful"));
						setVisible(false);
						new MovieManagerGUI();
					}else{
						JOptionPane.showMessageDialog(btnLogin, mmc.getBundle().getString("loginunsuccessful"));
					}
				} catch (SignupException e1) {
					JOptionPane.showMessageDialog(btnLogin, e1);
				}
			}
		});
		
		password = new JPasswordField();
		password.setBackground(new Color(255, 222, 173));
		password.setBounds(132, 87, 112, 20);
		this.getContentPane().add(password);
		password.setColumns(10);
		btnLogin.setBackground(new Color(255, 140, 0));
		btnLogin.setBounds(97, 127, 89, 23);
		this.getContentPane().add(btnLogin);
		
		JButton btnSingup = new JButton(mmc.getBundle().getString("signup"));
		btnSingup.setBackground(new Color(255, 140, 0));
		btnSingup.setBounds(97, 200, 89, 23);
		this.getContentPane().add(btnSingup);
		
		btnSingup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
				new SignupGUI(mmc);
			}
		});
		
		
		JLabel lblAreYouNew = new JLabel(mmc.getBundle().getString("newuserquestion"));
		//TODO Change the location of the text depending on the length of the message in different languages
		lblAreYouNew.setBounds(60, 175, 250, 14);
		this.getContentPane().add(lblAreYouNew);
		this.setBounds(100, 100, 300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
