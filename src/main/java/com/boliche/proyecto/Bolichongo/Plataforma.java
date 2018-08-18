package com.boliche.proyecto.Bolichongo;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest.ShareType;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramPostCommentRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

public class Plataforma {
	
	 private static Plataforma INSTANCE;
	 private JLabel status;

Instagram4j instagram;

		private Plataforma(JLabel status) {
			
			this.instagram=null;
			this.status=status;

		}
		
		

	    public static Plataforma getInstance(JLabel status) {
	    	
	    	if (INSTANCE==null){
	    		INSTANCE = new Plataforma(status);
	    		
	    	}
	    	
	    		return INSTANCE;
	    }
	    
	    
 public void iniciarSesion(String usuario,String clave)
 {
		instagram = Instagram4j.builder().username(usuario).password(clave).build();
		instagram.setup();
		
		while(true){
		try {
			instagram.login();
			break;
		} catch (IOException e) {
			System.out.println("No se pudo iniciar sesion");
			e.printStackTrace();
		}
		}
 }
 
 
 
 public Instagram4j getInstagram() {
	return instagram;
}



public void setInstagram(Instagram4j instagram) {
	this.instagram = instagram;
}



public InstagramSearchUsernameResult userResult(String usuario)
 {
	 InstagramSearchUsernameResult userResult = null;
		try {
			userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(usuario));
			return userResult;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
 }
 
 public List<InstagramUserSummary> getSeguidores(String usuario)
 {
	 
	  InstagramSearchUsernameResult userResult= userResult(usuario);
	 InstagramGetUserFollowersResult followers = null;
	 List<InstagramUserSummary> users = new ArrayList<InstagramUserSummary>();
	 String nextMaxId = null;
	 while (true) {
		try {
			followers = instagram.sendRequest(new InstagramGetUserFollowersRequest(userResult.getUser().getPk(), nextMaxId));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	users.addAll(followers.getUsers());
	 	nextMaxId = followers.getNext_max_id();
	 	
	 	if (nextMaxId == null) {
	 		break;
	 	}
	 	
		try {
			Thread.sleep((long)(500));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }

	 return users;
 }
 
 
 
 
 public void comentarFoto(String usuario,String mensaje)
 {
	 InstagramFeedResult result = null;
	 try {
		result = instagram.sendRequest(new InstagramUserFeedRequest(userResult(usuario).getUser().getPk()));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 for (InstagramFeedItem item : result.getItems()) 
	 {
		 try {
				instagram.sendRequest(new InstagramPostCommentRequest(item.getPk(), mensaje));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 break;
	 }
	 
	 

 }
 
 private void comentarFoto(InstagramUserSummary user, String mensaje) {
	 InstagramFeedResult result = null;
	 try {
		result = instagram.sendRequest(new InstagramUserFeedRequest(user.getPk()));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 for (InstagramFeedItem item : result.getItems()) 
	 {
		 try {
				instagram.sendRequest(new InstagramPostCommentRequest(item.getPk(), mensaje));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 break;
	 }
		
	}


@SuppressWarnings({ "rawtypes", "unchecked" })
public void mandarMensaje(String usuario,String mensaje)
 {
	
	 InstagramSearchUsernameResult user= userResult(usuario);
	 
	 System.out.println(user.getUser().getUsername());
	 ArrayList recipients = new ArrayList();
	 
	 String id= Long.toString(user.getUser().getPk());
	 recipients.add(id);
	 System.out.println(user.getUser().getPk());

	 
	 try {
		instagram.sendRequest(InstagramDirectShareRequest.builder(ShareType.MESSAGE, recipients).message(mensaje).build());
		
	} catch (IOException e) {
		System.out.println("No se pudo enviar el mensaje a "+ user.getUser().getUsername());
		e.printStackTrace();
	}
 }

@SuppressWarnings({ "unchecked", "rawtypes" })
public void mandarMensaje(InstagramUserSummary usuario,String mensaje)
{
	
	 ArrayList recipients = new ArrayList();
	 
	 recipients.add(usuario.getPk());

	 
	 try {
		instagram.sendRequest(InstagramDirectShareRequest.builder(ShareType.MESSAGE, recipients).message(mensaje).build());
		
	} catch (IOException e) {
		System.out.println("No se pudo enviar el mensaje a "+ usuario.getUsername());
		e.printStackTrace();
	}
}



public void enviarMensajeUsuarios(List<InstagramUserSummary> usuariosRobados, String mensaje) {
	
	int countNumber=0;
	int cantidad= usuariosRobados.size();
	
	for(InstagramUserSummary user: usuariosRobados)
	{
		
		mandarMensaje(user, mensaje);
		status.setText("Enviados "+ countNumber +" de "+ cantidad+". Mensaje enviado a: "+ user.getUsername());
		
		//espera entre 0 y 110 segundos.
		espera();
	}
	
}


public void espera(){
	
	try {
		Thread.sleep((long)(generarRandom()+60000));
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


public int generarRandom()
{
	
	SecureRandom sr = new SecureRandom();
	sr.nextBytes(new byte[1]);
	
	return sr.nextInt(30000);
}



public void comentarFotos(List<InstagramUserSummary> usuariosRobados, String mensaje) {
	int countNumber=0;
	int cantidad= usuariosRobados.size();
	
	for(InstagramUserSummary user: usuariosRobados)
	{
		
		comentarFoto(user, mensaje);
		status.setText("Comentados "+ countNumber +" de "+ cantidad+". Comentado a: "+ user.getUsername());
		
		//espera entre 0 y 110 segundos.
		espera();
	}
	
	
}





}
