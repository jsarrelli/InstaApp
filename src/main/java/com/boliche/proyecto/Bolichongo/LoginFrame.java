package com.boliche.proyecto.Bolichongo;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {

	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 100);
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
