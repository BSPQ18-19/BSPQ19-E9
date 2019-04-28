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
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.border.LineBorder;

import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.api.exceptions.SearchMovieException;
import ud.group9.moviemanager.data.Album;
import ud.group9.moviemanager.data.Movie;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;

public class UserAlbumsGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultListModel<String> l1;
	private boolean movies = false;
	private HashMap<String, String> movieIDs = new HashMap<>(); 
	private JButton btnBorrarAlbum;
	private String shownAlbum;
	private JButton btnSearchForMovie;
	private JButton btnMyAlbums;
	private JPanel panelForMainOptions;
	private JScrollPane albumsScrollPanel;
	private JPanel panelMainOptions;

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
	 */
	public UserAlbumsGUI() {
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

		JLabel lblMyAlbums = new JLabel("My albums:");
		panelUpperLabel.add(lblMyAlbums);

		l1 = new DefaultListModel<>();  

		JList<String> list = new JList<>(l1); 
		list.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!movies){
						if (((JList<String>)e.getComponent()).getSelectedIndex() != ((JList<String>)e.getComponent()).getLastVisibleIndex()){
							showMovies(MovieManagerClient.getAlbumByTitle(((JList<String>)e.getComponent()).getSelectedValue()).getMovies());
							shownAlbum = (((JList<String>)e.getComponent()).getSelectedValue());
						}else{
							UIManager.put("OptionPane.okButtonText", MovieManagerClient.getBundle().getString("createalbum"));
							String m = JOptionPane.showInputDialog(MovieManagerClient.getBundle().getString("albumtitle"));
							if (m != null){
								MovieManagerClient.createAlbum(m);
								showAlbums();
							}
						}
					}else{
						MovieGUI movieGUI = new MovieGUI(movieIDs.get((((JList<String>)e.getComponent()).getSelectedValue())));
						movieGUI.setVisible(true);
						setVisible(false);
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
				if (movies){
					showAlbums();
				}
			}
		});
		panel_2.add(btnBack);

		btnBorrarAlbum = new JButton(MovieManagerClient.getBundle().getString("deletealbum"));
		btnBorrarAlbum.setVisible(false);
		btnBorrarAlbum.setForeground(Color.BLACK);
		btnBorrarAlbum.setBackground(new Color(255, 140, 0));
		btnBorrarAlbum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MovieManagerClient.deleteAlbumByTitle(shownAlbum);
				showAlbums(); 
			}
		});
		panel_2.add(btnBorrarAlbum);

		panelMainOptions = new JPanel();
		panelMainOptions.setBackground(Color.ORANGE);
		panelForMainOptions.add(panelMainOptions);

		btnMyAlbums = new JButton("My albums");
		btnMyAlbums.setForeground(Color.BLACK);
		btnMyAlbums.setBackground(new Color(255, 140, 0));
		btnMyAlbums.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideMainOptions();
				System.out.println(panelForMainOptions.getSize());
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
							hideMainOptions();
							showMovies(MovieManagerClient.searchForMovie(moviename.getText(), movieyear.getText()));
						} catch (SearchMovieException e1) {
							e1.printStackTrace();
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

		showAlbums();
	}

	private void showMovies(ArrayList<Movie> moviesToShow){
		l1.clear();
		movieIDs.clear();
		for (Movie movie: moviesToShow){
			l1.addElement(movie.getTitle());  
			movieIDs.put(movie.getTitle(), movie.getMovieID());
		}
		btnBorrarAlbum.setVisible(true);
		movies = true;
	}

	private void hideMainOptions(){
		panelMainOptions.setVisible(false);
		albumsScrollPanel.setVisible(true);
	}

	private void showAlbums(){
		l1.clear();
		shownAlbum = null;
		for (Album album: MovieManagerClient.getAlbums()){
			l1.addElement(album.getTitle());  
		}
		l1.addElement(MovieManagerClient.getBundle().getString("newalbum"));
		btnBorrarAlbum.setVisible(false);
		movies = false;
	}
}
