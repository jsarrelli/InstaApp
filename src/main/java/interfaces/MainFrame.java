package interfaces;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
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

import com.boliche.proyecto.Bolichongo.Plataforma;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField usuarioRobado;
	private static Plataforma plataforma;
	private List<InstagramUserSummary> usuariosRobados;
	private JTextPane mensaje1,mensaje2,mensaje3;
	public static JLabel status;
	private JButton btnVerLista;
	private JProgressBar progressBar;
	private JButton robarbtn,btnEnviar;
	private static Instagram4j user;
	private static LoginFrame loginFrame;
	private JProgressBar progressBar_1;
	private JLabel image;
	private String urlProfilePic;
	private static MainFrame frame;
	private static int index=0;
	private JTextField indexText;
	private JTextField txtDelimiter;
	private JRadioButton rdbtnListaUsuarios,rdbtnFollowers,rdbtnFollowing;
	private JRadioButton rdbtnComentarFotos;
	private JRadioButton rdbtnEnviarMensaje;
	private JLabel lblNewLabel;

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
							frame = new MainFrame();
							
							
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
		setBounds(100, 100, 700, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		indexText = new JTextField();
		indexText.setHorizontalAlignment(SwingConstants.CENTER);
		indexText.setToolTipText("Indice");
		indexText.setBounds(320, 392, 69, 26);
		contentPane.add(indexText);
		indexText.setColumns(10);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(456, 71, 103, 20);
		progressBar.setIndeterminate(true); //we'll use an indeterminate progress bar
		progressBar.setVisible(false);
		contentPane.add(progressBar);

		image= new JLabel("");
		
		image.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/Instagram_icon-icons.com_66804.png")));
		image.setBounds(6, 6, 153, 147);
		contentPane.add(image);

		
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {


			protected Boolean doInBackground() throws Exception {
				urlProfilePic=plataforma.userResult(plataforma.getInstagram().getUsername()).getUser().getProfile_pic_url();
				return true;
				

			}

			protected void done()
			{
				cargaImagen(urlProfilePic);
			}

		};
		worker.execute();
		

		


	//plataforma.mandarMensaje("matiisfer", "soy el numero 1");
	//plataforma.comentarFoto("matiisfer", "hola machi soy Mr. Java");





		JLabel lblBienvenido = new JLabel("Bienvenido " + user.getUsername());
		lblBienvenido.setFont(new Font("Euphemia UCAS", lblBienvenido.getFont().getStyle() & ~Font.ITALIC | Font.BOLD, 15));
		lblBienvenido.setBounds(311, 6, 278, 29);
		contentPane.add(lblBienvenido);



		robarbtn = new JButton("Obtener Usuarios");
		robarbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
				robarbtn.setVisible(false);
				btnVerLista.setVisible(false);
				indexText.setVisible(true);
				progressBar.setVisible(true);

				SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {


					protected Boolean doInBackground() throws Exception {
						


						if(rdbtnFollowers.isSelected())usuariosRobados=plataforma.getFollowers(plataforma.userResult(usuarioRobado.getText()).getUser().getPk());
						else if(rdbtnFollowing.isSelected())usuariosRobados=plataforma.getFollowing(plataforma.userResult(usuarioRobado.getText()).getUser().getPk());
						else if (rdbtnListaUsuarios.isSelected())usuariosRobados=plataforma.usuariosLista(usuarioRobado.getText(),txtDelimiter.getText());
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
		robarbtn.setBounds(279, 179, 153, 29);
		contentPane.add(robarbtn);

		usuarioRobado = new JTextField();
		usuarioRobado.setHorizontalAlignment(SwingConstants.CENTER);
		usuarioRobado.setText("Ingrese ");
		usuarioRobado.setBounds(279, 62, 153, 29);
		contentPane.add(usuarioRobado);
		usuarioRobado.setColumns(10);



		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Esta seguro que desea realizar tal accion?","Confirme", dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					
					status.setVisible(true);
					btnEnviar.setVisible(false);
					progressBar_1.setVisible(true);
					indexText.setVisible(false);
					rdbtnComentarFotos.setEnabled(false);
					rdbtnEnviarMensaje.setEnabled(false);
					
					//cargo los mensajes
					final List<String> mensajes=new ArrayList<>();
					if(!mensaje1.getText().isEmpty()||!mensaje1.getText().equals("Escriba aqui el mensaje N1")) mensajes.add(mensaje1.getText());
					if(!mensaje2.getText().isEmpty()||!mensaje2.getText().equals("Escriba aqui el mensaje N2")) mensajes.add(mensaje2.getText());
					if(!mensaje3.getText().isEmpty()||!mensaje3.getText().equals("Escriba aqui el mensaje N3")) mensajes.add(mensaje3.getText());

					//el indice por default es 0
					if(indexText.getText().isEmpty()) index=0;
					else index= Integer.valueOf( indexText.getText());
					
					SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {


						protected Boolean doInBackground() throws Exception {
						
							
							if(rdbtnEnviarMensaje.isSelected())plataforma.enviar(usuariosRobados, mensajes,index,true);
							else if(rdbtnComentarFotos.isSelected())plataforma.enviar(usuariosRobados, mensajes,index,false);
							
							
						
							return true;

						}

						protected void done()
						{
							rdbtnComentarFotos.setEnabled(true);
							rdbtnEnviarMensaje.setEnabled(true);
							indexText.setVisible(true);
							progressBar_1.setVisible(false);
							status.setVisible(false);
							btnEnviar.setVisible(true);
						}

					};
					worker.execute();
				}
			}
		});
		btnEnviar.setBounds(299, 439, 117, 29);
		contentPane.add(btnEnviar);

		status = new JLabel("");
		status.setBounds(6, 529, 444, 16);
		status.setVisible(false);
		contentPane.add(status);


		btnVerLista = new JButton("Ver lista");
		btnVerLista.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				ListadoSeguidores listado=new ListadoSeguidores(usuariosRobados,usuarioRobado.getText());
				listado.setVisible(true);
				

			}
		});
		btnVerLista.setBounds(279, 220, 156, 29);
		btnVerLista.setVisible(false);
		contentPane.add(btnVerLista);
		
		
		progressBar_1 = new JProgressBar();
		progressBar_1.setBounds(299, 448, 122, 20);
		progressBar_1.setVisible(false);
		progressBar_1.setIndeterminate(true);
		contentPane.add(progressBar_1);
		
		rdbtnFollowers = new JRadioButton("Followers");
		rdbtnFollowers.setBounds(171, 103, 141, 23);
		rdbtnFollowers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnFollowing.setSelected(false);
				rdbtnListaUsuarios.setSelected(false);
				txtDelimiter.setVisible(false);
				

			}
		});
		contentPane.add(rdbtnFollowers);
		
		rdbtnFollowing = new JRadioButton("Following");
		rdbtnFollowing.setBounds(326, 103, 141, 23);
		rdbtnFollowing.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnFollowers.setSelected(false);
				rdbtnListaUsuarios.setSelected(false);
				txtDelimiter.setVisible(false);
				

			}
		});
		
		contentPane.add(rdbtnFollowing);
		
		rdbtnListaUsuarios = new JRadioButton("Lista");
		rdbtnListaUsuarios.setBounds(511, 103, 141, 23);
		rdbtnListaUsuarios.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnFollowing.setSelected(false);
				rdbtnFollowers.setSelected(false);
				txtDelimiter.setVisible(true);
				

			}
		});
		contentPane.add(rdbtnListaUsuarios);
		
		txtDelimiter = new JTextField();
		txtDelimiter.setHorizontalAlignment(SwingConstants.CENTER);
		txtDelimiter.setText("Separador");
		txtDelimiter.setBounds(502, 138, 130, 26);
		txtDelimiter.setVisible(false);
		contentPane.add(txtDelimiter);
		txtDelimiter.setColumns(10);
		
		rdbtnComentarFotos = new JRadioButton("Comentar Fotos");
		rdbtnComentarFotos.setBounds(207, 264, 153, 23);
		rdbtnComentarFotos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnEnviarMensaje.setSelected(false);
				

			}
		});
		contentPane.add(rdbtnComentarFotos);
		
		rdbtnEnviarMensaje = new JRadioButton("Enviar Mensajes");
		rdbtnEnviarMensaje.setBounds(448, 264, 141, 23);
		rdbtnEnviarMensaje.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnComentarFotos.setSelected(false);
			}
		});
		contentPane.add(rdbtnEnviarMensaje);
		
		mensaje1 = new JTextPane();
		mensaje1.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		mensaje1.setForeground(Color.GRAY);
		mensaje1.setText("Escriba aqui el mensaje N1");
		mensaje1.setBounds(16, 299, 181, 82);
		contentPane.add(mensaje1);
		
		mensaje2 = new JTextPane();
		mensaje2.setText("Escriba aqui el mensaje N2");
		mensaje2.setForeground(Color.GRAY);
		mensaje2.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		mensaje2.setBounds(251, 298, 181, 82);
		contentPane.add(mensaje2);
		
		mensaje3 = new JTextPane();
		mensaje3.setText("Escriba aqui el mensaje N3");
		mensaje3.setForeground(Color.GRAY);
		mensaje3.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		mensaje3.setBounds(502, 298, 181, 82);
		contentPane.add(mensaje3);
		

		
		JLabel background = new JLabel("");
		background.setIcon(new ImageIcon(MainFrame.class.getResource("/resources/background.jpg")));
		background.setBounds(0, 0, 700, 572);
		contentPane.add(background);
		
		
		

		
		
		
	


	}
	
	
	public void cargaImagen(String dir){

	    URL url;
	    System.out.println(dir);
	    try {
	        url = new URL(dir);

	        Image imagen = ImageIO.read(url);
	        ImageIcon icon=new ImageIcon(imagen);
	    	image.setIcon(icon);

	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	 image.updateUI();

	}
}
