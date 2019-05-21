package ud.group9.moviemanager.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.border.LineBorder;

import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.api.exceptions.SearchMovieException;
import ud.group9.moviemanager.api.exceptions.SignupException;
import ud.group9.moviemanager.data.Album;
import ud.group9.moviemanager.data.Movie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.GridBagLayout;
import java.awt.Image;

public class UserAlbumsGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = LogManager.getRootLogger();
	private JPanel contentPane;
	private DefaultListModel<String> l1;
	private boolean movies = false;
	private HashMap<String, String> movieIDs = new HashMap<>(); 
	private JButton btnBorrarAlbum;
	private JButton btnAddToAlbum;
	private String shownAlbum;
	private JButton btnSearchForMovie;
	private JButton btnMyAlbums;
	private JPanel panelForMainOptions;
	private JScrollPane albumsScrollPanel;
	private JPanel panelMainOptions;
	private JList<String> list;
	private JLabel lblMyAlbums;
	private boolean searching = false;
	private JPanel panel;
	private JLabel lblMovie;
	private boolean moviedetails = false;
	private JPanel panelMovieDetails;
	private JLabel lblTitleField;
	private JLabel lblYearField;
	private ImageIcon image;
	private JPanel panel_1;
	private JLabel lblPoster;
	private boolean signin = false;
	private JButton btnSignUp;
	private JTextField username;
	private JPasswordField password;
	private JLabel lblConfirm;
	private JPasswordField passwordConf;
	private JButton btnLogIn;
	private JPanel panel_3;
	private JLabel lblNewLabel;
	private JPanel panelMainButtonSignUp;
	private JPanel panelUserData;
	private JPanel panel_2;
	private JButton btnBack;
	private JButton btnBackFromSignUp;
	private HashMap<JComponent, String> texts = new HashMap<>();
	private Color mainPanelBackgroundColor = Color.decode("#ffffff");
	private Color titleBackgroundColor = Color.decode("#a5ffd6");
	private Color buttonBackgroundColor = Color.decode("#ffffff");
	private Color listSeparatorColor = Color.decode("#ffa69e");
	private Color textFieldBackgroundColor = Color.decode("#ffffff");
	private JTextArea areaPlot;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//					MovieManagerClient.LogIn("user", "test_password");
					UserAlbumsGUI frame = new UserAlbumsGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @brief Frame creation
	 * 
	 * Create the frame.
	 * @throws IOException A exception if something goes wrong with the input/output
	 * @throws MalformedURLException A exception if something goes wrong with the url
	 */
	public UserAlbumsGUI() throws MalformedURLException, IOException {
		UIManager.put("OptionPane.cancelButtonText", MovieManagerClient.getBundle().getString("cancel"));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panelForMainOptions = new JPanel();
		panelForMainOptions.setBackground(mainPanelBackgroundColor);
		contentPane.add(panelForMainOptions, BorderLayout.CENTER);
		panelForMainOptions.setLayout(new GridBagLayout());

		albumsScrollPanel = new JScrollPane();
		albumsScrollPanel.setVisible(true);
		albumsScrollPanel.setMinimumSize(new Dimension(434, 199));
		albumsScrollPanel.setBackground(mainPanelBackgroundColor);
		albumsScrollPanel.setVisible(false);
		panelForMainOptions.add(albumsScrollPanel);

		JPanel panelUpperLabel = new JPanel();
		panelUpperLabel.setLayout(new BorderLayout());
		panelUpperLabel.setBackground(titleBackgroundColor);
		contentPane.add(panelUpperLabel, BorderLayout.NORTH);

		lblMyAlbums = new JLabel(MovieManagerClient.getBundle().getString("moviemanager"), SwingConstants.CENTER);
		texts.put(lblMyAlbums, "moviemanager");
		panelUpperLabel.add(lblMyAlbums, BorderLayout.CENTER);

		JPanel panelLanguages = new JPanel();
		panelLanguages.setBackground(titleBackgroundColor);
		JLabel lblLanguage = new JLabel("en ");
		lblLanguage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MovieManagerClient.setBundle("en");
				for (JComponent c: texts.keySet()){
					if ((c instanceof JLabel) && (!moviedetails)) ((JLabel)c).setText(MovieManagerClient.getBundle().getString(texts.get(c)));
					else if (c instanceof JButton) ((JButton)c).setText(MovieManagerClient.getBundle().getString(texts.get(c)));
				}
				if (lblMyAlbums.getText().equals(MovieManagerClient.getBundle().getString("myalbums"))){
					l1.set(0, (MovieManagerClient.getBundle().getString("watched")));
					l1.set(l1.getSize()-1, (MovieManagerClient.getBundle().getString("newalbum")));
				}
				revalidate();
				repaint();
			}
		});
		panelLanguages.add(lblLanguage);
		JLabel lblLanguage2 = new JLabel("es");
		lblLanguage2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MovieManagerClient.setBundle("es");
				for (JComponent c: texts.keySet()){
					if ((c instanceof JLabel) && (!moviedetails)) ((JLabel)c).setText(MovieManagerClient.getBundle().getString(texts.get(c)));
					else if (c instanceof JButton) ((JButton)c).setText(MovieManagerClient.getBundle().getString(texts.get(c)));
				}
				if (lblMyAlbums.getText().equals(MovieManagerClient.getBundle().getString("myalbums"))){
					l1.set(0, (MovieManagerClient.getBundle().getString("watched")));
					l1.set(l1.getSize()-1, (MovieManagerClient.getBundle().getString("newalbum")));
				}
				revalidate();
				repaint();
			}
		});
		panelLanguages.add(lblLanguage2);
		panelUpperLabel.add(panelLanguages, BorderLayout.EAST);
		
		JLabel lblLanguageCompensator = new JLabel("");
		Rectangle2D text1 = lblLanguage.getFont().getStringBounds(lblLanguage.getText(), new FontRenderContext(new AffineTransform(),true,true));
		Rectangle2D text2 = lblLanguage2.getFont().getStringBounds(lblLanguage2.getText(), new FontRenderContext(new AffineTransform(),true,true));
		lblLanguageCompensator.setPreferredSize(new Dimension((int) (text1.getWidth() + text2.getWidth()), (int) (text1.getHeight() + text2.getHeight())));
		panelUpperLabel.add(lblLanguageCompensator, BorderLayout.WEST);

		l1 = new DefaultListModel<>();  

		list = new JList<>(l1); 
		list.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!movies){
						if (((JList<String>)e.getComponent()).getSelectedIndex() == ((JList<String>)e.getComponent()).getModel().getSize()-1){
							UIManager.put("OptionPane.okButtonText", MovieManagerClient.getBundle().getString("createalbum"));
							String m = JOptionPane.showInputDialog(MovieManagerClient.getBundle().getString("albumtitle"));
							if (m != null){
								MovieManagerClient.createAlbum(m);
								LOGGER.info("Album successfully created.");
								showAlbums();
							}
						}else if(((JList<String>)e.getComponent()).getSelectedIndex() == 0){
							try {
								showMovies(MovieManagerClient.getWatched(), true);
							} catch (SearchMovieException e1) {
								LOGGER.warn(e1.toString());
							}
						}else{
							shownAlbum = (((JList<String>)e.getComponent()).getSelectedValue());
							showMovies(MovieManagerClient.getAlbumByTitle(((JList<String>)e.getComponent()).getSelectedValue()).getMovies(), false);
						}
					}else{
						showMovieDetails(MovieManagerClient.getMovie(movieIDs.get((((JList<String>)e.getComponent()).getSelectedValue().substring(1)))));
						moviedetails = true;
					}
				}else{
					if (movies){
						showMovieInAlbumOptions(MovieManagerClient.isWatched(movieIDs.get((((JList<String>)e.getComponent()).getSelectedValue().substring(1)))));
						if (searching){
							btnAddToAlbum.setText(MovieManagerClient.getBundle().getString("addtoalbum"));
							texts.put(btnAddToAlbum, "addtoalbum");
							btnAddToAlbum.setVisible(true);
						}else if (movies){
							btnAddToAlbum.setText(MovieManagerClient.getBundle().getString("removefromalbum"));
							texts.put(btnAddToAlbum, "removefromalbum");
							btnAddToAlbum.setVisible(true);
						}
					}else{
						if (((JList<String>)e.getComponent()).getSelectedIndex() == ((JList<String>)e.getComponent()).getModel().getSize()-1
								|| ((JList<String>)e.getComponent()).getSelectedIndex() == 0){
							btnBorrarAlbum.setVisible(false);
						}else{
							btnBorrarAlbum.setVisible(true);
							btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("deletealbum"));
							texts.put(btnBorrarAlbum, "deletealbum");
						}
					}
				}
			}
		});
		list.setBackground(mainPanelBackgroundColor);
		list.setFixedCellHeight(50);
		list.setCellRenderer(new DefaultListCellRenderer(){
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
				listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, listSeparatorColor));
				return listCellRendererComponent;
			};
		}
				);

		albumsScrollPanel.setViewportView(list);
		panel_2 = new JPanel();
		panel_2.setBackground(mainPanelBackgroundColor);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 2;
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		panel_2.setVisible(false);
		JPanel pBotonera = new JPanel();
		pBotonera.add(panel_2);
		contentPane.add(pBotonera, BorderLayout.SOUTH);

		btnBack = new JButton(MovieManagerClient.getBundle().getString("back"));
		texts.put(btnBack, "back");
		btnBack.setForeground(Color.BLACK);
		btnBack.setBackground(buttonBackgroundColor);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnBack.getText().equals(MovieManagerClient.getBundle().getString("back"))){
					if (lblConfirm.isVisible()){
						signin(false);
					}
					else if (moviedetails){
						hideMovieDetails();
						moviedetails = false;
					}
					else if (movies && !searching){
						showAlbums();
					}else{
						searching = false;
						hideMainOptions(false);
						btnBorrarAlbum.setVisible(false);
						btnAddToAlbum.setVisible(false);
						lblMyAlbums.setText(MovieManagerClient.getBundle().getString("moviemanager"));
						texts.put(lblMyAlbums, "moviemanager");
					}
				}else if (btnBack.getText().equals(MovieManagerClient.getBundle().getString("logout"))){
					login(false);
				}

			}
		});
		panel_2.add(btnBack);

		btnBorrarAlbum = new JButton(MovieManagerClient.getBundle().getString("deletealbum"));
		texts.put(btnBorrarAlbum, "deletealbum");
		btnBorrarAlbum.setVisible(false);
		btnBorrarAlbum.setForeground(Color.BLACK);
		btnBorrarAlbum.setBackground(buttonBackgroundColor);
		btnBorrarAlbum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnBorrarAlbum.getText().equals(MovieManagerClient.getBundle().getString("deletealbum"))){
					MovieManagerClient.deleteAlbumByTitle(shownAlbum);
					showAlbums(); 
				}else if(btnBorrarAlbum.getText().equals(MovieManagerClient.getBundle().getString("addwatched"))){
					try {
						MovieManagerClient.addToWatched(movieIDs.get((list.getSelectedValue().substring(1))));
						l1.setElementAt("★" + l1.getElementAt(list.getSelectedIndex()).substring(1), list.getSelectedIndex());
						showMovieInAlbumOptions(true);
					} catch (SearchMovieException e1) {
						LOGGER.warn(e1.toString());
					}
				}else if(btnBorrarAlbum.getText().equals(MovieManagerClient.getBundle().getString("removewatched"))){
					try {
						MovieManagerClient.deleteFromWatched(movieIDs.get((list.getSelectedValue().substring(1))));
						l1.setElementAt("☆" + l1.getElementAt(list.getSelectedIndex()).substring(1), list.getSelectedIndex());
						showMovieInAlbumOptions(false);
					} catch (SearchMovieException e1) {
						LOGGER.warn(e1.toString());
					}
				}else if(btnBorrarAlbum.getText().equals(MovieManagerClient.getBundle().getString("rate"))){
					MovieManagerClient.createRating(movieIDs.get((list.getSelectedValue().substring(1))), 80);
					btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("updaterating"));
					texts.put(btnBorrarAlbum, "updaterating");
					btnAddToAlbum.setText(MovieManagerClient.getBundle().getString("deleterating"));
					texts.put(btnAddToAlbum, "deleterating");
					btnAddToAlbum.setVisible(true);
				}else if(btnBorrarAlbum.getText().equals(MovieManagerClient.getBundle().getString("updaterating"))){
					MovieManagerClient.updateRating(movieIDs.get((list.getSelectedValue().substring(1))), 50);
				}
			}
		});
		panel_2.add(btnBorrarAlbum);

		btnAddToAlbum = new JButton(MovieManagerClient.getBundle().getString("addtoalbum"));
		texts.put(btnAddToAlbum, "addtoalbum");
		btnAddToAlbum.setVisible(false);
		btnAddToAlbum.setForeground(Color.BLACK);
		btnAddToAlbum.setBackground(buttonBackgroundColor);
		btnAddToAlbum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnAddToAlbum.getText().equals(MovieManagerClient.getBundle().getString("addtoalbum"))){
					ArrayList<JCheckBox> cbg = new ArrayList<>();
					UIManager.put("OptionPane.okButtonText", MovieManagerClient.getBundle().getString("store"));
					for (Album a : MovieManagerClient.getAlbums()){
						cbg.add(new JCheckBox(a.getTitle()));
					}
					String message = MovieManagerClient.getBundle().getString("selectalbum");
					Object[] params = {message, cbg.toArray()};
					if(JOptionPane.showConfirmDialog(null, params, MovieManagerClient.getBundle().getString("addtoalbum"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
						for (JCheckBox cb : cbg)
							if (cb.isSelected()) MovieManagerClient.addMovieToAlbumByTitle(cb.getText(), movieIDs.get((list.getSelectedValue().substring(1))));
					}
				}else if (btnAddToAlbum.getText().equals(MovieManagerClient.getBundle().getString("removefromalbum"))){
					MovieManagerClient.deleteMovieFromAlbum(MovieManagerClient.getAlbumByTitle(shownAlbum).getAlbumID(), movieIDs.get((list.getSelectedValue().substring(1))));
					showMovies(MovieManagerClient.getAlbumByTitle(shownAlbum).getMovies(), false);
					if (list.getModel().getSize() == 0) btnAddToAlbum.setVisible(false);
				}else if (btnAddToAlbum.getText().equals(MovieManagerClient.getBundle().getString("deleterating"))){
					MovieManagerClient.deleteRating(movieIDs.get((list.getSelectedValue().substring(1))));
					btnAddToAlbum.setVisible(false);
					btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("rate"));
					texts.put(btnBorrarAlbum, "rate");
				}
			}
		});
		panel_2.add(btnAddToAlbum);

		panelMainOptions = new JPanel();
		panelMainOptions.setBackground(mainPanelBackgroundColor);
		panelForMainOptions.add(panelMainOptions);
		panelMainOptions.setVisible(false);

		panelUserData = new JPanel();
		panelForMainOptions.add(panelUserData);
		panelUserData.setBackground(mainPanelBackgroundColor);

		GridBagLayout gbl_panelUserData = new GridBagLayout();
		gbl_panelUserData.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelUserData.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panelUserData.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelUserData.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelUserData.setLayout(gbl_panelUserData);

		JLabel lblUsername = new JLabel(MovieManagerClient.getBundle().getString("username"));
		texts.put(lblUsername, "username");
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
		username.setBackground(textFieldBackgroundColor);
		username.setColumns(10);

		JLabel lblPassword = new JLabel(MovieManagerClient.getBundle().getString("password"));
		texts.put(lblPassword, "password");
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
		password.setBackground(textFieldBackgroundColor);
		password.setColumns(10);

		lblConfirm = new JLabel(MovieManagerClient.getBundle().getString("confirmpassword"));
		texts.put(lblConfirm, "confirmpassword");
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
		passwordConf.setBackground(textFieldBackgroundColor);
		passwordConf.setColumns(10);

		panel_3 = new JPanel();
		panel_3.setBackground(mainPanelBackgroundColor);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.gridwidth = 2;
		gbc_panel_3.insets = new Insets(0, 0, 0, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 3;
		panelUserData.add(panel_3, gbc_panel_3);

		btnLogIn = new JButton(MovieManagerClient.getBundle().getString("login"));
		texts.put(btnLogIn, "login");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String usname = username.getText();
				String pswrd = String.valueOf(password.getPassword());

				try {
					if (MovieManagerClient.LogIn(usname, pswrd)){
						JOptionPane.showMessageDialog(btnLogIn, MovieManagerClient.getBundle().getString("loginsuccessful"));
						LOGGER.info("Login successful.");
						login(true);
					}else{
						JOptionPane.showMessageDialog(btnLogIn, MovieManagerClient.getBundle().getString("loginunsuccessful"));
						LOGGER.warn("Login unseccessful");
					}
				} catch (SignupException e1) {
					JOptionPane.showMessageDialog(btnLogIn, e1);
					LOGGER.warn(e1.toString());
				}
			}
		});
		panel_3.add(btnLogIn);
		btnLogIn.setForeground(Color.BLACK);
		btnLogIn.setBackground(buttonBackgroundColor);

		JPanel panel1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel1.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		panel1.setBackground(mainPanelBackgroundColor);
		pBotonera.add(panel1);

		panelMainButtonSignUp = new JPanel();
		panelMainButtonSignUp.setBackground(mainPanelBackgroundColor);
		getContentPane().add(panelMainButtonSignUp, BorderLayout.SOUTH);
		panelMainButtonSignUp.setLayout(new BoxLayout(panelMainButtonSignUp, BoxLayout.Y_AXIS));

		lblNewLabel = new JLabel(MovieManagerClient.getBundle().getString("newuserquestion"));
		texts.put(lblNewLabel, "newuserquestion");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panelMainButtonSignUp.add(lblNewLabel);
		JPanel panelButtonSignUp = new JPanel();
		panelMainButtonSignUp.add(panelButtonSignUp);
		FlowLayout flowLayout_1 = (FlowLayout) panelButtonSignUp.getLayout();
		flowLayout_1.setVgap(10);
		flowLayout_1.setHgap(10);
		panelButtonSignUp.setBackground(mainPanelBackgroundColor);

		btnSignUp = new JButton(MovieManagerClient.getBundle().getString("signup"));
		texts.put(btnSignUp, "signup");
		panelButtonSignUp.add(btnSignUp);
		btnBackFromSignUp = new JButton(MovieManagerClient.getBundle().getString("back"));
		texts.put(btnBackFromSignUp, "back");
		panelButtonSignUp.add(btnBackFromSignUp);
		btnSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(signin){
					String usname = username.getText();
					String pswrd = "";

					if (password.getPassword().length == 0) {
						JOptionPane.showMessageDialog(btnSignUp, MovieManagerClient.getBundle().getString("emptypassword"));
						LOGGER.warn("Empty password");
						return;
					}
					if(!(String.valueOf(password.getPassword()).equals(String.valueOf(passwordConf.getPassword())))) {
						JOptionPane.showMessageDialog(btnSignUp, MovieManagerClient.getBundle().getString("differentpasswords"));
						LOGGER.warn("Different passwords");
						return;
					}
					pswrd = String.valueOf(password.getPassword());
					try {
						MovieManagerClient.SignUp(usname, pswrd);
						JOptionPane.showMessageDialog(btnSignUp, MovieManagerClient.getBundle().getString("signupsuccessful"));
						LOGGER.info("SignUp successful.");
						MovieManagerClient.LogIn(usname, pswrd);
						passwordConf.setText("");
						signin = false;
						login(true);
					} catch (SignupException e1) {
						JOptionPane.showMessageDialog(btnSignUp, e1);
						LOGGER.warn(e1.toString());
					}
				}
				else{
					signin = true;
					signin(true);
				}
			}
		});
		btnSignUp.setBackground(buttonBackgroundColor);
		btnSignUp.setForeground(Color.BLACK);
		btnBackFromSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				signin = false;
				signin(false);
			}
		});
		btnBackFromSignUp.setBackground(buttonBackgroundColor);
		btnBackFromSignUp.setForeground(Color.BLACK);
		btnBackFromSignUp.setVisible(false);
		btnMyAlbums = new JButton(MovieManagerClient.getBundle().getString("myalbums"));
		texts.put(btnMyAlbums, "myalbums");
		btnMyAlbums.setForeground(Color.BLACK);
		btnMyAlbums.setBackground(buttonBackgroundColor);
		btnMyAlbums.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideMainOptions(true);
				showAlbums();
			}
		});

		btnSearchForMovie = new JButton(MovieManagerClient.getBundle().getString("searchmovie"));
		texts.put(btnSearchForMovie, "searchmovie");
		btnSearchForMovie.setForeground(Color.BLACK);
		btnSearchForMovie.setBackground(buttonBackgroundColor);
		btnSearchForMovie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UIManager.put("OptionPane.okButtonText", MovieManagerClient.getBundle().getString("search"));
				JTextField moviename = new JTextField();
				JTextField movieyear = new JTextField();
				Object[] message = {
						MovieManagerClient.getBundle().getString("moviename"), moviename,
						MovieManagerClient.getBundle().getString("movieyear"), movieyear
				};
				if (JOptionPane.showConfirmDialog(null, message, "Search for movie", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
					if (!moviename.getText().isEmpty()) {
						try {
							searching = true;
							hideMainOptions(true);
							showMovies(MovieManagerClient.searchForMovie(moviename.getText(), movieyear.getText()), true);
							lblMyAlbums.setText(MovieManagerClient.getBundle().getString("searchresults"));
							texts.put(lblMyAlbums, "searchresults");
						} catch (SearchMovieException e1) {
							LOGGER.warn(e1.toString());
						}
					}
				}
			}
		});
		panelMainOptions.setLayout(new BoxLayout(panelMainOptions, BoxLayout.Y_AXIS));
		btnSearchForMovie.setAlignmentX(CENTER_ALIGNMENT);
		panelMainOptions.add(btnSearchForMovie);
		panelMainOptions.add(Box.createRigidArea(new Dimension(0, 10)));
		btnMyAlbums.setAlignmentX(CENTER_ALIGNMENT);
		panelMainOptions.add(btnMyAlbums);

		panelMovieDetails = new JPanel();
		panelMovieDetails.setBackground(mainPanelBackgroundColor);
		panelMovieDetails.setLayout(new BoxLayout(panelMovieDetails, BoxLayout.Y_AXIS));
		panelMovieDetails.setVisible(false);
		panelForMainOptions.add(panelMovieDetails);

		lblMovie = new JLabel();
		lblMovie.setVisible(false);
		panelMovieDetails.add(lblMovie);

		panel = new JPanel();
		panel.setBackground(mainPanelBackgroundColor);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelMovieDetails.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel_1 = new JPanel();
		panel_1.setBackground(mainPanelBackgroundColor);
		panel_1.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		panel_1.setMinimumSize(new Dimension(400, 400));
		panel_1.setPreferredSize(new Dimension(400, 400));
		panel_1.setMaximumSize(new Dimension(400, 400));
		panel.add(panel_1);


		GridBagLayout panelData = new GridBagLayout();
		panelData.columnWidths = new int[]{57, 0, 0, 0, 0, 0, 0, 0, 0};
		panelData.rowHeights = new int[]{0, 0, 0, 0, 0};
		panelData.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelData.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(panelData);

		JLabel lblTitle = new JLabel("Title:");
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 1;
		panel_1.add(lblTitle, gbc_lblTitle);
		lblTitle.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);

		lblTitleField = new JLabel();
		GridBagConstraints gbc_lblTitleField = new GridBagConstraints();
		gbc_lblTitleField.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitleField.gridx = 1;
		gbc_lblTitleField.gridy = 1;
		panel_1.add(lblTitleField, gbc_lblTitleField);

		JLabel lblYear = new JLabel("Year:");
		GridBagConstraints gbc_lblYear = new GridBagConstraints();
		gbc_lblYear.insets = new Insets(0, 0, 5, 5);
		gbc_lblYear.gridx = 0;
		gbc_lblYear.gridy = 2;
		panel_1.add(lblYear, gbc_lblYear);
		lblYear.setHorizontalAlignment(SwingConstants.LEFT);
		lblYear.setAlignmentY(1.0f);

		lblYearField = new JLabel();
		lblYearField.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblYearField = new GridBagConstraints();
		gbc_lblYearField.anchor = GridBagConstraints.WEST;
		gbc_lblYearField.insets = new Insets(0, 0, 5, 5);
		gbc_lblYearField.gridx = 1;
		gbc_lblYearField.gridy = 2;
		panel_1.add(lblYearField, gbc_lblYearField);

		JLabel lblPlot = new JLabel("Plot:");
		GridBagConstraints gbc_lblPlot = new GridBagConstraints();
		gbc_lblPlot.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlot.gridx = 0;
		gbc_lblPlot.gridy = 3;
		gbc_lblPlot.anchor = GridBagConstraints.NORTH;
		panel_1.add(lblPlot, gbc_lblPlot);
		lblPlot.setHorizontalAlignment(SwingConstants.LEFT);
		lblPlot.setAlignmentY(1.0f);

		areaPlot = new JTextArea();

		areaPlot.setColumns(100);
		areaPlot.setLineWrap(true);
		areaPlot.setWrapStyleWord(true);
		areaPlot.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		GridBagConstraints gbc_lblPlotField = new GridBagConstraints();
		gbc_lblPlotField.anchor = GridBagConstraints.WEST;
		gbc_lblPlotField.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPlotField.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlotField.gridx = 1;
		gbc_lblPlotField.gridwidth = 5;
		gbc_lblPlotField.gridy = 3;
		panel_1.add(areaPlot, gbc_lblPlotField);


		//		JLabel lblAlbum = new JLabel("Album:");
		//		GridBagConstraints gbc_lblAlbum = new GridBagConstraints();
		//		gbc_lblAlbum.insets = new Insets(0, 0, 5, 5);
		//		gbc_lblAlbum.gridx = 0;
		//		gbc_lblAlbum.gridy = 3;
		//		panel_1.add(lblAlbum, gbc_lblAlbum);
		//		lblAlbum.setHorizontalAlignment(SwingConstants.LEFT);
		//		lblAlbum.setAlignmentY(1.0f);
		//		int cont = 0;
		//		for(final Album album: retrieveAlbumsLinked(movie)) {
		//			JLabel lblAlbumField = new JLabel(album.getTitle());
		//			lblAlbumField.addMouseListener(new MouseListener() {
		//				@Override
		//				public void mouseClicked(MouseEvent arg0) {
		//					viewAlbum(album);
		//					
		//				}
		//
		//				@Override
		//				public void mouseEntered(MouseEvent e) {
		//					// TODO Auto-generated method stub
		//					
		//				}
		//
		//				@Override
		//				public void mouseExited(MouseEvent e) {
		//					// TODO Auto-generated method stub
		//					
		//				}
		//
		//				@Override
		//				public void mousePressed(MouseEvent e) {
		//					// TODO Auto-generated method stub
		//					
		//				}
		//
		//				@Override
		//				public void mouseReleased(MouseEvent e) {
		//					// TODO Auto-generated method stub
		//					
		//				}
		//			});
		//			lblAlbumField.setHorizontalAlignment(SwingConstants.RIGHT);
		//			GridBagConstraints gbc_lblAlbumField = new GridBagConstraints();
		//			gbc_lblAlbumField.anchor = GridBagConstraints.WEST;
		//			gbc_lblAlbumField.insets = new Insets(0, 0, 5, 5);
		//			gbc_lblAlbumField.gridx = 1 + cont;
		//			gbc_lblAlbumField.gridy = 3;
		//			panel_1.add(lblAlbumField, gbc_lblAlbumField);
		//			cont++;


		JPanel panelPoster = new JPanel();
		panelPoster.setBackground(mainPanelBackgroundColor);
		panelPoster.setPreferredSize(new Dimension(300, 444));
		panel.add(panelPoster);
		lblPoster = new JLabel("", SwingConstants.CENTER);
		//			image = new ImageIcon((ImageIO.read(new URL("https://m.media-amazon.com/images/M/MV5BMTkwNTczNTMyOF5BMl5BanBnXkFtZTcwNzUxOTUyMw@@._V1_SX300.jpg"))).getScaledInstance(84, 100, Image.SCALE_DEFAULT));
		//			lblPoster = new JLabel("", image, SwingConstants.CENTER);
		panelPoster.add(lblPoster);
		this.setVisible(true);
	}

	/**
	 * @brief Shows User albums in the main GUI
	 * 
	 * @param moviesToShow List of movies to show in the GUI
	 * @param watched Specifies if a movie is watched or not
	 */
	private void showMovies(ArrayList<Movie> moviesToShow, boolean watched){
		l1.clear();
		movieIDs.clear();
		btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("deletealbum"));
		texts.put(btnBorrarAlbum, "deletealbum");
		for (Movie movie: moviesToShow){
			if(MovieManagerClient.isWatched(movie.getMovieID())) l1.addElement("★" + movie.getTitle());  
			else l1.addElement("☆" + movie.getTitle());
			movieIDs.put(movie.getTitle(), movie.getMovieID());
		}
		btnBorrarAlbum.setVisible(!watched);
		if (!watched){
			lblMyAlbums.setText(shownAlbum + ":");
		}
		movies = true;
		LOGGER.info("Show Movies successfully loaded.");
	}

	/**
	 * @brief Hide main options from GUI
	 * 
	 * @param hide Specifies if options are hidden or not
	 */
	private void hideMainOptions(boolean hide){
		if(hide){
			btnBack.setText(MovieManagerClient.getBundle().getString("back"));
			texts.put(btnBack, "back");
		}
		else{
			btnBack.setText(MovieManagerClient.getBundle().getString("logout"));
			texts.put(btnBack, "logout");
		}
		panelMainOptions.setVisible(!hide);
		albumsScrollPanel.setVisible(hide);
	}

	/**
	 * @brief Show Albums in the main GUI
	 * 
	 */
	private void showAlbums(){
		lblMyAlbums.setText(MovieManagerClient.getBundle().getString("myalbums"));
		texts.put(lblMyAlbums, "myalbums");
		l1.clear();
		shownAlbum = null;
		l1.addElement(MovieManagerClient.getBundle().getString("watched"));
		for (Album album: MovieManagerClient.getAlbums()){
			l1.addElement(album.getTitle());  
		}
		l1.addElement(MovieManagerClient.getBundle().getString("newalbum"));
		btnBorrarAlbum.setVisible(false);
		movies = false;
		LOGGER.info("ShowAlbums successfully loaded.");
	}

	/**
	 * @brief Show Movie options in GUI
	 * 
	 * @param isWatched Specifies if the movie is watched or not
	 */
	private void showMovieInAlbumOptions(boolean isWatched){
		btnBorrarAlbum.setVisible(true);
		if (isWatched){
			btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("removewatched"));
			texts.put(btnBorrarAlbum, "removewatched");
		}
		else{
			btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("addwatched"));
			texts.put(btnBorrarAlbum, "addwatched");
		}
	}

	/**
	 * @brief Shows movie details in GUI
	 * 
	 * Shows all the details of the passed film in the GUI
	 * 
	 * @param m Movie object
	 */
	private void showMovieDetails(Movie m){
		albumsScrollPanel.setVisible(false);
		if (MovieManagerClient.getRating(movieIDs.get((list.getSelectedValue().substring(1)))) == -1){
			btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("rate"));
			texts.put(btnBorrarAlbum, "rate");
			btnAddToAlbum.setVisible(false);
		}
		else{
			btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("updaterating"));
			texts.put(btnBorrarAlbum, "updaterating");
			btnAddToAlbum.setText(MovieManagerClient.getBundle().getString("deleterating"));
			texts.put(btnBorrarAlbum, "deleterating");
		}
		lblMyAlbums.setText(list.getSelectedValue() + ":");
		panelMovieDetails.setPreferredSize(panelForMainOptions.getSize());
		lblTitleField.setText(m.getTitle());
		lblYearField.setText(String.valueOf(m.getYear()));
		areaPlot.setText(m.getPlot());
		try {
			image = new ImageIcon((ImageIO.read(new URL(m.getPoster()))).getScaledInstance(300, 444, Image.SCALE_DEFAULT));
			lblPoster.setIcon(image);
		} catch (MalformedURLException e) {
			LOGGER.warn(e.toString());
		} catch (IOException e) {
			LOGGER.warn(e.toString());
		}
		panelMovieDetails.setVisible(true);
		lblMovie.setText(m.getTitle());
		lblMovie.setVisible(true);
		LOGGER.info("Show Movie details successfully loaded.");
	}
	
	/**
	 * @brief Hide all the Movie details
	 * 
	 * Removes all the movie details from the main GUI
	 */
	private void hideMovieDetails(){
		if (!searching){
			lblMyAlbums.setText(shownAlbum + ":");
		}else{
			lblMyAlbums.setText(MovieManagerClient.getBundle().getString("searchresults"));
			texts.put(lblMyAlbums, "searchresults");
		}
		panelMovieDetails.setVisible(false);
		//		btnBorrarAlbum.setVisible(true);
		if (MovieManagerClient.isWatched(movieIDs.get((list.getSelectedValue().substring(1))))){
			btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("removewatched"));
			texts.put(btnBorrarAlbum, "removewatched");
		}
		else{
			btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("addwatched"));
			texts.put(btnBorrarAlbum, "addwatched");
		}
		if (searching){
			btnAddToAlbum.setText(MovieManagerClient.getBundle().getString("addtoalbum"));
			texts.put(btnAddToAlbum, "addtoalbum");
		}else if (movies){
			btnAddToAlbum.setText(MovieManagerClient.getBundle().getString("removefromalbum"));
			texts.put(btnAddToAlbum, "removefromalbum");
		}
		btnAddToAlbum.setVisible(true);
		albumsScrollPanel.setVisible(true);
	}
	
	/**
	 * @brief Load SignIn objects in GUI
	 * 
	 * @param signin Specifies if we want to SignIn or not
	 */
	private void signin(boolean signin){
		btnLogIn.setVisible(!signin);
		lblMyAlbums.setText(MovieManagerClient.getBundle().getString("welcome"));
		texts.put(lblMyAlbums, "welcome");
		lblConfirm.setVisible(signin);
		passwordConf.setVisible(signin);
		lblNewLabel.setVisible(!signin);
		btnBackFromSignUp.setVisible(signin);
	}

	/**
	 * @brief Load login objects in GUI
	 * 
	 * @param login Specifies if we want to Login or not
	 */
	private void login(boolean login){
		hideMainOptions(!login);
		panelMainButtonSignUp.setVisible(!login);
		if (!login){
			for (Component c: panelForMainOptions.getComponents()) c.setVisible(false);
			getContentPane().add(panelMainButtonSignUp, BorderLayout.SOUTH);
			signin(false);
			username.setText("");
			password.setText("");
		}
		panelUserData.setVisible(!login);
		panelMainButtonSignUp.setVisible(!login);
		if (login) contentPane.add(panel_2, BorderLayout.SOUTH);
		panel_2.setVisible(login);
	}
}
