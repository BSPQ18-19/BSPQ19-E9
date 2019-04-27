package ud.group9.moviemanager.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.LineBorder;

import ud.group9.moviemanager.api.MovieManagerClient;
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
import java.util.HashMap;

public class UserAlbumsGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultListModel<String> l1;
	private boolean movies = false;
	private HashMap<String, String> movieIDs = new HashMap<>(); 
	
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

		JScrollPane albumsScrollPanel = new JScrollPane();
		albumsScrollPanel.setBackground(Color.ORANGE);
		contentPane.add(albumsScrollPanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		contentPane.add(panel, BorderLayout.NORTH);

		JLabel lblMyAlbums = new JLabel("My albums:");
		panel.add(lblMyAlbums);

		l1 = new DefaultListModel<>();  
		showAlbums();
		
		JList<String> list = new JList<>(l1); 
		list.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!movies){
						if (((JList<String>)e.getComponent()).getSelectedIndex() != ((JList<String>)e.getComponent()).getLastVisibleIndex())
							showAlbumMovies((((JList<String>)e.getComponent()).getSelectedValue()));
						else{
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
		
		JButton btnLogIn = new JButton(MovieManagerClient.getBundle().getString("back"));
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (movies){
					showAlbums();
				}
			}
		});
		panel_2.add(btnLogIn);
		btnLogIn.setForeground(Color.BLACK);
		btnLogIn.setBackground(new Color(255, 140, 0));
		
	}

	private void showAlbumMovies(String albumTitle){
		l1.clear();
		movieIDs.clear();
		for (Movie movie: MovieManagerClient.getAlbumByTitle(albumTitle).getMovies()){
			l1.addElement(movie.getTitle());  
			movieIDs.put(movie.getTitle(), movie.getMovieID());
		}
		movies = true;
	}
	
	private void showAlbums(){
		l1.clear();
		for (Album album: MovieManagerClient.getAlbums()){
			l1.addElement(album.getTitle());  
		}
		l1.addElement(MovieManagerClient.getBundle().getString("newalbum"));
		movies = false;
	}
}
