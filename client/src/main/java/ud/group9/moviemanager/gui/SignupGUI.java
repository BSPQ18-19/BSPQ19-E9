package ud.group9.moviemanager.gui;

import ud.group9.moviemanager.api.LoggerMaster;
import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.api.exceptions.SignupException;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class SignupGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private boolean signin = false;
	private JButton btnSignUp;
	private JTextField username;
	private JPasswordField password;
	private JLabel lblConfirm;
	private JPasswordField passwordConf;
	private JLabel lblAreYouNew;
	private JButton btnLogIn;
	private JPanel panel_2;
	private JLabel lblNewLabel;
	private JPanel panelMainButtonSignUp;
	
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
		try {
            LoggerMaster.setup();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.ORANGE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridBagLayout());

		JPanel panelUserData = new JPanel();
		panel.add(panelUserData);
		panelUserData.setBackground(Color.ORANGE);
		GridBagLayout gbl_panelUserData = new GridBagLayout();
		gbl_panelUserData.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelUserData.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panelUserData.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelUserData.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelUserData.setLayout(gbl_panelUserData);

		JLabel lblUsername = new JLabel(MovieManagerClient.getBundle().getString("username") + ":");
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		panelUserData.add(lblUsername, gbc_lblUsername);

		username = new JTextField();
		GridBagConstraints gbc_username = new GridBagConstraints();
		gbc_username.insets = new Insets(0, 0, 5, 5);
		gbc_username.gridx = 1;
		gbc_username.gridy = 0;
		panelUserData.add(username, gbc_username);
		username.setBackground(new Color(255, 222, 173));
		username.setColumns(10);

		JLabel lblPassword = new JLabel(MovieManagerClient.getBundle().getString("password") + ":");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		panelUserData.add(lblPassword, gbc_lblPassword);

		password = new JPasswordField();
		GridBagConstraints gbc_password = new GridBagConstraints();
		gbc_password.insets = new Insets(0, 0, 5, 5);
		gbc_password.gridx = 1;
		gbc_password.gridy = 1;
		panelUserData.add(password, gbc_password);
		password.setBackground(new Color(255, 222, 173));
		password.setColumns(10);

		lblConfirm = new JLabel(MovieManagerClient.getBundle().getString("confirmpassword") + ":");
		lblConfirm.setVisible(false);
		GridBagConstraints gbc_lblConfirm = new GridBagConstraints();
		gbc_lblConfirm.insets = new Insets(0, 0, 5, 5);
		gbc_lblConfirm.gridx = 0;
		gbc_lblConfirm.gridy = 2;
		panelUserData.add(lblConfirm, gbc_lblConfirm);

		passwordConf = new JPasswordField();
		passwordConf.setVisible(false);
		GridBagConstraints gbc_passwordConf = new GridBagConstraints();
		gbc_passwordConf.insets = new Insets(0, 0, 5, 5);
		gbc_passwordConf.gridx = 1;
		gbc_passwordConf.gridy = 2;
		panelUserData.add(passwordConf, gbc_passwordConf);
		passwordConf.setBackground(new Color(255, 222, 173));
		passwordConf.setColumns(10);
		
		panel_2 = new JPanel();
		panel_2.setBackground(Color.ORANGE);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 2;
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		panelUserData.add(panel_2, gbc_panel_2);
		
		btnLogIn = new JButton(MovieManagerClient.getBundle().getString("login"));
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String usname = username.getText();
				String pswrd = String.valueOf(password.getPassword());

				try {
					if (MovieManagerClient.LogIn(usname, pswrd)){
						JOptionPane.showMessageDialog(btnLogIn, MovieManagerClient.getBundle().getString("loginsuccessful"));
						LOGGER.log(Level.INFO, "Login successful.");
						setVisible(false);
						new MovieManagerGUI();
					}else{
						JOptionPane.showMessageDialog(btnLogIn, MovieManagerClient.getBundle().getString("loginunsuccessful"));
						LOGGER.log(Level.WARNING, "Login unseccessful");
					}
				} catch (SignupException e1) {
					JOptionPane.showMessageDialog(btnLogIn, e1);
					LOGGER.log(Level.SEVERE, LoggerMaster.getStackTrace(e1));
				}
			}
		});
		panel_2.add(btnLogIn);
		btnLogIn.setForeground(Color.BLACK);
		btnLogIn.setBackground(new Color(255, 140, 0));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		panel_1.setBackground(Color.ORANGE);
		getContentPane().add(panel_1, BorderLayout.NORTH);
		
				lblAreYouNew = new JLabel(MovieManagerClient.getBundle().getString("welcome"));
				lblAreYouNew.setVisible(false);
				panel_1.add(lblAreYouNew);
				lblAreYouNew.setHorizontalAlignment(SwingConstants.CENTER);
				
				panelMainButtonSignUp = new JPanel();
				panelMainButtonSignUp.setBackground(Color.ORANGE);
				getContentPane().add(panelMainButtonSignUp, BorderLayout.SOUTH);
				panelMainButtonSignUp.setLayout(new BoxLayout(panelMainButtonSignUp, BoxLayout.Y_AXIS));
				
				lblNewLabel = new JLabel(MovieManagerClient.getBundle().getString("newuserquestion"));
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				panelMainButtonSignUp.add(lblNewLabel);
				JPanel panelButtonSignUp = new JPanel();
				panelMainButtonSignUp.add(panelButtonSignUp);
				FlowLayout flowLayout_1 = (FlowLayout) panelButtonSignUp.getLayout();
				flowLayout_1.setVgap(10);
				flowLayout_1.setHgap(10);
				panelButtonSignUp.setBackground(Color.ORANGE);
				
						btnSignUp = new JButton(MovieManagerClient.getBundle().getString("signup"));
						panelButtonSignUp.add(btnSignUp);
						btnSignUp.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								
								if(signin){
								String usname = username.getText();
								String pswrd = "";

								if (password.getPassword().length == 0) {
									JOptionPane.showMessageDialog(btnSignUp, MovieManagerClient.getBundle().getString("emptypassword"));
									LOGGER.log(Level.WARNING, "Empty password");
									return;
								}
								if(!(String.valueOf(password.getPassword()).equals(String.valueOf(passwordConf.getPassword())))) {
									JOptionPane.showMessageDialog(btnSignUp, MovieManagerClient.getBundle().getString("differentpasswords"));
									LOGGER.log(Level.WARNING, "Different passwords");
									return;
								}
								pswrd = String.valueOf(password.getPassword());
								try {
									MovieManagerClient.SignUp(usname, pswrd);
									JOptionPane.showMessageDialog(btnSignUp, MovieManagerClient.getBundle().getString("signupsuccessful"));
									LOGGER.log(Level.INFO, "SignUp successful.");
									MovieManagerClient.LogIn(usname, pswrd);
									setVisible(false);
									dispose();
									new MovieManagerGUI();
								} catch (SignupException e1) {
									JOptionPane.showMessageDialog(btnSignUp, e1);
									LOGGER.log(Level.SEVERE, LoggerMaster.getStackTrace(e1));
								}
								}
								else{
									signin = true;
									signin();
								}
							}
						});
						btnSignUp.setBackground(new Color(255, 140, 0));
						btnSignUp.setForeground(Color.BLACK);
		this.setTitle("SignupGUI");
		this.setBounds(100, 100, 300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private void signin(){
		btnLogIn.setVisible(false);
		lblAreYouNew.setVisible(true);
		lblConfirm.setVisible(true);
		passwordConf.setVisible(true);
		lblNewLabel.setVisible(false);
	}
}
