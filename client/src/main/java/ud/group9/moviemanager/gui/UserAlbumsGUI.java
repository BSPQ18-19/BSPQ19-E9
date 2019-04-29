package ud.group9.moviemanager.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.border.LineBorder;

import ud.group9.moviemanager.api.LoggerMaster;
import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.api.exceptions.SearchMovieException;
import ud.group9.moviemanager.data.Album;
import ud.group9.moviemanager.data.Movie;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.Image;

public class UserAlbumsGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
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
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MovieManagerClient.LogIn("user", "test_password");
					UserAlbumsGUI frame = new UserAlbumsGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public UserAlbumsGUI() throws MalformedURLException, IOException {
		try {
            LoggerMaster.setup();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log files");
        }
		UIManager.put("OptionPane.cancelButtonText", MovieManagerClient.getBundle().getString("cancel"));
		UIManager.put("OptionPane.okButtonText", MovieManagerClient.getBundle().getString("createalbum"));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panelForMainOptions = new JPanel();
		panelForMainOptions.setBackground(Color.ORANGE);
		contentPane.add(panelForMainOptions, BorderLayout.CENTER);
		panelForMainOptions.setLayout(new GridBagLayout());

		albumsScrollPanel = new JScrollPane();
		albumsScrollPanel.setVisible(true);
		albumsScrollPanel.setMinimumSize(new Dimension(434, 199));
		albumsScrollPanel.setBackground(Color.ORANGE);
		albumsScrollPanel.setVisible(false);
		panelForMainOptions.add(albumsScrollPanel);

		JPanel panelUpperLabel = new JPanel();
		panelUpperLabel.setBackground(Color.ORANGE);
		contentPane.add(panelUpperLabel, BorderLayout.NORTH);

		lblMyAlbums = new JLabel(MovieManagerClient.getBundle().getString("moviemanager"));
		panelUpperLabel.add(lblMyAlbums);

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
								LOGGER.log(Level.INFO, "Album successfully created.");
								showAlbums();
							}
						}else if(((JList<String>)e.getComponent()).getSelectedIndex() == 0){
							try {
								showMovies(MovieManagerClient.getWatched(), true);
							} catch (SearchMovieException e1) {
								LOGGER.log(Level.SEVERE, LoggerMaster.getStackTrace(e1));
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
							btnAddToAlbum.setVisible(true);
						}else if (movies){
							btnAddToAlbum.setText(MovieManagerClient.getBundle().getString("removefromalbum"));
							btnAddToAlbum.setVisible(true);
						}
					}else{
						if (((JList<String>)e.getComponent()).getSelectedIndex() == ((JList<String>)e.getComponent()).getModel().getSize()-1
								|| ((JList<String>)e.getComponent()).getSelectedIndex() == 0){
							btnBorrarAlbum.setVisible(false);
						}else{
							btnBorrarAlbum.setVisible(true);
							btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("deletealbum"));

						}
					}
				}
			}
		});
		list.setBackground(Color.ORANGE);
		list.setFixedCellHeight(50);
		list.setCellRenderer(new DefaultListCellRenderer(){
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
				listCellRendererComponent.setBorder(new LineBorder(new Color(240, 230, 140), 4));
				return listCellRendererComponent;
			};
		}
				);

		albumsScrollPanel.setViewportView(list);
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.ORANGE);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 2;
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		contentPane.add(panel_2, BorderLayout.SOUTH);

		JButton btnBack = new JButton(MovieManagerClient.getBundle().getString("back"));
		btnBack.setForeground(Color.BLACK);
		btnBack.setBackground(new Color(255, 140, 0));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (moviedetails){
					hideMovieDetails();
					moviedetails = false;
				}
				else if (movies && !searching){
					showAlbums();
				}else{
					searching = false;
					hideMainOptions(false);
					btnBorrarAlbum.setVisible(false);
					lblMyAlbums.setText(MovieManagerClient.getBundle().getString("moviemanager"));
				}
				btnAddToAlbum.setVisible(false);
			}
		});
		panel_2.add(btnBack);

		btnBorrarAlbum = new JButton(MovieManagerClient.getBundle().getString("deletealbum"));
		btnBorrarAlbum.setVisible(false);
		btnBorrarAlbum.setForeground(Color.BLACK);
		btnBorrarAlbum.setBackground(new Color(255, 140, 0));
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
						LOGGER.log(Level.SEVERE, LoggerMaster.getStackTrace(e1));
					}
				}else if(btnBorrarAlbum.getText().equals(MovieManagerClient.getBundle().getString("removewatched"))){
					try {
						MovieManagerClient.deleteFromWatched(movieIDs.get((list.getSelectedValue().substring(1))));
						l1.setElementAt("☆" + l1.getElementAt(list.getSelectedIndex()).substring(1), list.getSelectedIndex());
						showMovieInAlbumOptions(false);
					} catch (SearchMovieException e1) {
						LOGGER.log(Level.SEVERE, LoggerMaster.getStackTrace(e1));
					}
				}
			}
		});
		panel_2.add(btnBorrarAlbum);

		btnAddToAlbum = new JButton(MovieManagerClient.getBundle().getString("addtoalbum"));
		btnAddToAlbum.setVisible(false);
		btnAddToAlbum.setForeground(Color.BLACK);
		btnAddToAlbum.setBackground(new Color(255, 140, 0));
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
				}
			}
		});
		panel_2.add(btnAddToAlbum);

		panelMainOptions = new JPanel();
		panelMainOptions.setBackground(Color.ORANGE);
		panelForMainOptions.add(panelMainOptions);

		btnMyAlbums = new JButton("My albums");
		btnMyAlbums.setForeground(Color.BLACK);
		btnMyAlbums.setBackground(new Color(255, 140, 0));
		btnMyAlbums.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideMainOptions(true);
				showAlbums();
			}
		});

		btnSearchForMovie = new JButton("Search for movie");
		btnSearchForMovie.setForeground(Color.BLACK);
		btnSearchForMovie.setBackground(new Color(255, 140, 0));
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
						} catch (SearchMovieException e1) {
							LOGGER.log(Level.SEVERE, LoggerMaster.getStackTrace(e1));
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
		panelMovieDetails.setBackground(Color.ORANGE);
		panelMovieDetails.setLayout(new BoxLayout(panelMovieDetails, BoxLayout.Y_AXIS));
		panelMovieDetails.setVisible(false);
		panelForMainOptions.add(panelMovieDetails);
		
		lblMovie = new JLabel();
		lblMovie.setVisible(false);
		panelMovieDetails.add(lblMovie);
		
		panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelMovieDetails.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.ORANGE);
		panel_1.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		panel_1.setMinimumSize(new Dimension(330, 199));
		panel_1.setPreferredSize(new Dimension(330, 199));
		panel_1.setMaximumSize(new Dimension(330, 199));
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
		
		JLabel lblAlbum = new JLabel("Album:");
		GridBagConstraints gbc_lblAlbum = new GridBagConstraints();
		gbc_lblAlbum.insets = new Insets(0, 0, 5, 5);
		gbc_lblAlbum.gridx = 0;
		gbc_lblAlbum.gridy = 3;
		panel_1.add(lblAlbum, gbc_lblAlbum);
		lblAlbum.setHorizontalAlignment(SwingConstants.LEFT);
		lblAlbum.setAlignmentY(1.0f);
		int cont = 0;
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
			panelPoster.setBackground(Color.ORANGE);
			panelPoster.setPreferredSize(new Dimension(84, 199));
			panel.add(panelPoster);
			lblPoster = new JLabel("", SwingConstants.CENTER);
//			image = new ImageIcon((ImageIO.read(new URL("https://m.media-amazon.com/images/M/MV5BMTkwNTczNTMyOF5BMl5BanBnXkFtZTcwNzUxOTUyMw@@._V1_SX300.jpg"))).getScaledInstance(84, 100, Image.SCALE_DEFAULT));
//			lblPoster = new JLabel("", image, SwingConstants.CENTER);
			panelPoster.add(lblPoster);
	}

	private void showMovies(ArrayList<Movie> moviesToShow, boolean watched){
		l1.clear();
		movieIDs.clear();
		btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("deletealbum"));
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
		LOGGER.log(Level.INFO, "Show Movies successfully loaded.");
	}

	private void hideMainOptions(boolean hide){
		panelMainOptions.setVisible(!hide);
		albumsScrollPanel.setVisible(hide);
	}

	private void showAlbums(){
		lblMyAlbums.setText(MovieManagerClient.getBundle().getString("myalbums"));
		l1.clear();
		shownAlbum = null;
		l1.addElement(MovieManagerClient.getBundle().getString("watched"));
		for (Album album: MovieManagerClient.getAlbums()){
			l1.addElement(album.getTitle());  
		}
		l1.addElement(MovieManagerClient.getBundle().getString("newalbum"));
		btnBorrarAlbum.setVisible(false);
		movies = false;
		LOGGER.log(Level.INFO, "ShowAlbums successfully loaded.");
	}

	private void showMovieInAlbumOptions(boolean isWatched){
		btnBorrarAlbum.setVisible(true);
		if (isWatched) btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("removewatched"));
		else btnBorrarAlbum.setText(MovieManagerClient.getBundle().getString("addwatched"));
	}
	
	private void showMovieDetails(Movie m){
		albumsScrollPanel.setVisible(false);
		btnBorrarAlbum.setVisible(false);
		btnAddToAlbum.setVisible(false);
		lblMyAlbums.setText(list.getSelectedValue() + ":");
		panelMovieDetails.setPreferredSize(panelForMainOptions.getSize());
		lblTitleField.setText(m.getTitle());
		lblYearField.setText(String.valueOf(m.getYear()));
		try {
			image = new ImageIcon((ImageIO.read(new URL(m.getPoster()))).getScaledInstance(84, 100, Image.SCALE_DEFAULT));
			lblPoster.setIcon(image);
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, LoggerMaster.getStackTrace(e));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, LoggerMaster.getStackTrace(e));
		}
		panelMovieDetails.setVisible(true);
		lblMovie.setText(m.getTitle());
		lblMovie.setVisible(true);
		LOGGER.log(Level.INFO, "Show Movie details successfully loaded.");
	}
	private void hideMovieDetails(){
		if (!searching){
			lblMyAlbums.setText(shownAlbum + ":");
		}else{
			lblMyAlbums.setText(MovieManagerClient.getBundle().getString("searchresults"));
		}
		panelMovieDetails.setVisible(false);
		albumsScrollPanel.setVisible(true);
		btnBorrarAlbum.setVisible(true);
		btnAddToAlbum.setVisible(true);
	}
}
