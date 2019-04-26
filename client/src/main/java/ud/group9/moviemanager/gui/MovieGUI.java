package ud.group9.moviemanager.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.data.Movie;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MovieGUI frame = new MovieGUI("tt0446029");
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
	public MovieGUI(String movieID) {
		Movie movie = MovieManagerClient.getMovie(movieID);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JLabel lblMovie = new JLabel("Movie: " + movie.getTitle());
		contentPane.add(lblMovie);
		
		JPanel panel = new JPanel();
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentY(Component.BOTTOM_ALIGNMENT);
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
		
		JLabel lblTitleField = new JLabel(movie.getTitle());
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
		
		JLabel lblYearField = new JLabel(String.valueOf(movie.getYear()));
		lblYearField.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblYearField = new GridBagConstraints();
		gbc_lblYearField.anchor = GridBagConstraints.WEST;
		gbc_lblYearField.insets = new Insets(0, 0, 5, 5);
		gbc_lblYearField.gridx = 1;
		gbc_lblYearField.gridy = 2;
		panel_1.add(lblYearField, gbc_lblYearField);
		
		try {
			ImageIcon image = new ImageIcon((ImageIO.read(new URL(movie.getPoster()))).getScaledInstance(300, 300, Image.SCALE_DEFAULT));
			
			JPanel panelPoster = new JPanel();
			panel.add(panelPoster);
			JLabel lblPoster = new JLabel("", image, SwingConstants.CENTER);
			panelPoster.add(lblPoster);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
