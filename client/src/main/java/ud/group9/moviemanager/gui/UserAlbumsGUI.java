package ud.group9.moviemanager.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.LineBorder;

import ud.group9.moviemanager.api.MovieManagerClient;
import ud.group9.moviemanager.data.Album;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;

import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserAlbumsGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
		
        DefaultListModel<String> l1 = new DefaultListModel<>();  
        for (Album album: MovieManagerClient.getAlbums()){
        	l1.addElement(album.getTitle());  
        }
        JList<String> list = new JList<>(l1); 
        list.addMouseListener(new MouseAdapter() {
        	@SuppressWarnings("unchecked")
			@Override
        	public void mouseClicked(MouseEvent e) {
        		  if (e.getClickCount() == 2) {
        		    System.out.println((((JList<String>)e.getComponent()).getSelectedValue()));
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
	}

}
