package com.boliche.proyecto.Bolichongo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Scrollbar;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;

import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class ListadoSeguidores extends JFrame {

	private JPanel contentPane;



	/**
	 * Create the frame.
	 * @param usuarioRobado 
	 * @param usuariosRobados 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ListadoSeguidores(List<InstagramUserSummary> usuariosRobados, String usuarioRobado) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSeguidoresDe = new JLabel("Seguidores de: "+ usuarioRobado);
		lblSeguidoresDe.setBounds(6, 6, 174, 16);
		contentPane.add(lblSeguidoresDe);
		
		JLabel lblCantidad = new JLabel(usuariosRobados.size()+" seguidores");
		lblCantidad.setBounds(323, 6, 121, 16);
		contentPane.add(lblCantidad);
		

		JScrollPane menuScrollPane;
		
		DefaultListModel<String> listado= new DefaultListModel<String>();
		
		for(InstagramUserSummary usuario:usuariosRobados){
			listado.addElement(usuario.getUsername()+" / "+ usuario.getFull_name());
		}
		JList<String> list = new JList<>(listado);
		list.setVisibleRowCount(10);
		list.setBounds(67, 54, 335, 150);
		list.setSize(300,200);
		//contentPane.add(list);
		
		menuScrollPane = new JScrollPane(list);
		menuScrollPane.setBounds(67, 54, 335, 150);
		menuScrollPane.setSize(300,200);
		menuScrollPane.setVisible(true);
		contentPane.add(menuScrollPane, BorderLayout.CENTER);

	}
}
