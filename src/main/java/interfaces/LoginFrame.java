package interfaces;


import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 400, 300, 100);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCargando = new JLabel("Cargando..");
		lblCargando.setFont(new Font("Avenir Next", Font.PLAIN, 16));
		lblCargando.setBounds(103, 17, 103, 22);
		contentPane.add(lblCargando);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(74, 41, 146, 20);
		progressBar.setIndeterminate(true);
		contentPane.add(progressBar);
	}

}
