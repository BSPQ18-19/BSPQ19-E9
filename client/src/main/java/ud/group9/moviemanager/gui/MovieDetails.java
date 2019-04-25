package ud.group9.moviemanager.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MovieDetails extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MovieDetails frame = new MovieDetails();
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
	public MovieDetails() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JLabel lblMovie = new JLabel("Movie: ");
		contentPane.add(lblMovie);
		
		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{14, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 1;
		panel.add(lblTitle, gbc_lblTitle);
		
		JLabel lblTitleField = new JLabel("");
		GridBagConstraints gbc_lblTitleField = new GridBagConstraints();
		gbc_lblTitleField.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitleField.gridx = 1;
		gbc_lblTitleField.gridy = 1;
		panel.add(lblTitleField, gbc_lblTitleField);
		
		ImageIcon image = new ImageIcon((ImageIO.read(input)).getScaledInstance(300, 300, Image.SCALE_DEFAULT););
		JLabel lblPoster = new JLabel("");
		GridBagConstraints gbc_lblPoster = new GridBagConstraints();
		gbc_lblPoster.insets = new Insets(0, 0, 5, 0);
		gbc_lblPoster.gridx = 10;
		gbc_lblPoster.gridy = 1;
		panel.add(lblPoster, gbc_lblPoster);
		
		JLabel lblYear = new JLabel("Year:");
		lblYear.setHorizontalAlignment(SwingConstants.LEFT);
		lblYear.setAlignmentY(1.0f);
		GridBagConstraints gbc_lblYear = new GridBagConstraints();
		gbc_lblYear.insets = new Insets(0, 0, 0, 5);
		gbc_lblYear.gridx = 0;
		gbc_lblYear.gridy = 2;
		panel.add(lblYear, gbc_lblYear);
		
		JLabel lblYearField = new JLabel("");
		GridBagConstraints gbc_lblYearField = new GridBagConstraints();
		gbc_lblYearField.insets = new Insets(0, 0, 0, 5);
		gbc_lblYearField.gridx = 2;
		gbc_lblYearField.gridy = 2;
		panel.add(lblYearField, gbc_lblYearField);
	}

}
