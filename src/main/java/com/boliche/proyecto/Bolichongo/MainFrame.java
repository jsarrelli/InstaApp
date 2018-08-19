package com.boliche.proyecto.Bolichongo;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField usuarioRobado;
	private static Plataforma plataforma;
	private List<InstagramUserSummary> usuariosRobados;
	private JTextPane mensaje;
	private static JLabel status;
	private JButton btnVerLista;
	private JProgressBar progressBar;
	private JButton robarbtn;
	private static Instagram4j user;
	private static LoginFrame loginFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					loginFrame= new LoginFrame();
					loginFrame.setVisible(true);
					
					SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
						protected Boolean doInBackground() throws Exception {

							plataforma=Plataforma.getInstance();
							plataforma.iniciarSesion("ninabelgrano", "estrella1");
							user=plataforma.getInstagram();
							return true;
						}
						
						protected void done()
						{
							loginFrame.dispose();
							MainFrame frame = new MainFrame();
							frame.setVisible(true);
						}

					};
					worker.execute();
				
					
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		status = new JLabel("Estado");
		status.setBounds(35, 256, 61, 16);
		status.setVisible(false);
		contentPane.add(status);

		progressBar = new JProgressBar();
		progressBar.setBounds(299, 98, 103, 20);
		progressBar.setIndeterminate(true); //we'll use an indeterminate progress bar
		progressBar.setVisible(false);
		contentPane.add(progressBar);


		

		

		//plataforma.comentarFoto("luucasrial", "hola luqilla soy Mr. Java");

		//plataforma.mandarMensaje("arimancuso", "soy el numero 1");





		JLabel lblBienvenido = new JLabel("Bienvenido " + user.getUsername());
		lblBienvenido.setFont(new Font("Courier", Font.ITALIC, 15));
		lblBienvenido.setBounds(149, 6, 278, 16);
		contentPane.add(lblBienvenido);


		JLabel lblImagen = new JLabel("Imagen");
		lblImagen.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/Instagram_icon-icons.com_66804.png")));
		lblImagen.setBounds(6, 6, 131, 131);
		contentPane.add(lblImagen);

		robarbtn = new JButton("Obtener Seguidores");
		robarbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				progressBar.setVisible(true);
				robarbtn.setVisible(false);
				btnVerLista.setVisible(false);

				SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {


					protected Boolean doInBackground() throws Exception {


						usuariosRobados=plataforma.getFollowers(plataforma.userResult(usuarioRobado.getText()).getUser().getPk());
						return true;

					}
					
					protected void done()
					{
						robarbtn.setVisible(true);
						progressBar.setVisible(false);
						btnVerLista.setVisible(true);
						btnVerLista.setText(Integer.toString(usuariosRobados.size())+ " seguidores");
					}

				};

				worker.execute();


				



				


			}
		});
		robarbtn.setBounds(274, 98, 153, 29);
		contentPane.add(robarbtn);

		usuarioRobado = new JTextField();
		usuarioRobado.setText("Ingrese el robado");
		usuarioRobado.setBounds(274, 57, 153, 29);
		contentPane.add(usuarioRobado);
		usuarioRobado.setColumns(10);

		mensaje = new JTextPane();
		mensaje.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		mensaje.setForeground(Color.GRAY);
		mensaje.setText("Escriba aqui el mensaje a enviar");
		mensaje.setBounds(25, 168, 276, 82);
		contentPane.add(mensaje);

		JButton btnEnviar = new JButton("Enviar Mensaje");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {



				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Esta seguro que desea enviar este mensaje?","Confirme", dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					status.setVisible(true);
					plataforma.enviarMensajeUsuarios(usuariosRobados,mensaje.getText(),status);
				}
			}
		});
		btnEnviar.setBounds(313, 224, 117, 29);
		contentPane.add(btnEnviar);

		status = new JLabel("");
		status.setBounds(35, 256, 61, 16);
		status.setVisible(false);
		contentPane.add(status);

		JButton btnComentarFoto = new JButton("Comentar Foto");
		btnComentarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Esta seguro que desea enviar este comentario?","Confirme", dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					status.setVisible(true);
					plataforma.comentarFotos(usuariosRobados,mensaje.getText(),status);
				}
				
			}
		});
		btnComentarFoto.setBounds(313, 167, 117, 29);
		contentPane.add(btnComentarFoto);

		btnVerLista = new JButton("Ver lista");
		btnVerLista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListadoSeguidores listado=new ListadoSeguidores(usuariosRobados,usuarioRobado.getText());
				listado.setVisible(true);
				

			}
		});
		btnVerLista.setBounds(274, 124, 156, 29);
		btnVerLista.setVisible(false);
		contentPane.add(btnVerLista);


	}
	
	

}
