package com.boliche.proyecto.Bolichongo;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingWorker;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest.ShareType;
import org.brunocvcunha.instagram4j.requests.InstagramFollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramPostCommentRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

public class Plataforma {

	private static Plataforma INSTANCE;
	private static int counter;


	Instagram4j instagram;

	private Plataforma() {

		this.instagram=null;


	}



	public static Plataforma getInstance() {

		if (INSTANCE==null){
			INSTANCE = new Plataforma();

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



	public static int getCounter() {
		return counter;
	}



	public static void setCounter(int counter) {
		Plataforma.counter = counter;
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

	public List<InstagramUserSummary> getFollowers(long pk)
	{

		InstagramGetUserFollowersResult followers = null;
		List<InstagramUserSummary> users = new ArrayList<InstagramUserSummary>();
		String nextMaxId = null;
		while (true) {
			try {
				followers = instagram.sendRequest(new InstagramGetUserFollowersRequest(pk, nextMaxId));
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

	public List<InstagramUserSummary> getFollowing(long pk)
	{

		InstagramGetUserFollowersResult following = null;
		List<InstagramUserSummary> users = new ArrayList<InstagramUserSummary>();
		String nextMaxId = null;
		while (true) {
			try {
				following = instagram.sendRequest(new InstagramGetUserFollowingRequest(pk, nextMaxId));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			users.addAll(following.getUsers());
			nextMaxId = following.getNext_max_id();

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


	public List<String> getFollowingUsername(long pk)
	{


		List<String> users =new ArrayList<>();
		List<InstagramUserSummary> following = getFollowing(pk);
		for(InstagramUserSummary user:following)
		{

			users.add(user.getUsername());
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



	public void enviarMensajeUsuarios(List<InstagramUserSummary> usuariosRobados, String mensaje,JLabel status) {

		int countNumber=0;
		int cantidad= usuariosRobados.size();

		for(InstagramUserSummary user: usuariosRobados)
		{

			mandarMensaje(user, mensaje);
			status.setText("Enviados "+ countNumber +" de "+ cantidad+". Mensaje enviado a: "+ user.getUsername());

			//espera entre 60 y 90 segundos.
			espera(60000,30000);
		}

	}


	public void espera(int minimo, int margen){

		try {
			Thread.sleep((long)(generarRandom(margen)+minimo));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public int generarRandom(int margen)
	{

		SecureRandom sr = new SecureRandom();
		sr.nextBytes(new byte[1]);

		return sr.nextInt(margen);
	}



	public void comentarFotos(List<InstagramUserSummary> usuariosRobados, String mensaje,JLabel status) {
		int countNumber=0;
		int cantidad= usuariosRobados.size();

		for(InstagramUserSummary user: usuariosRobados)
		{

			comentarFoto(user, mensaje);
			status.setText("Comentados "+ countNumber +" de "+ cantidad+". Comentado a: "+ user.getUsername());

			//espera entre 60 y 90 segundos.

		}


	}



	public void SeguirUsuarios(List<InstagramUserSummary> usuariosRobados) {

		ListadoSeguidores.estado.setVisible(true);

		List<String> following=getFollowingUsername(instagram.getUserId());
		//System.out.println(following.size());
		//System.out.println(following.contains(userResult("julisarrelli").getUser().getUsername()));
	   
		final int size=usuariosRobados.size();
		counter=0;
		for(final InstagramUserSummary user:usuariosRobados)
		{

			if(!following.contains(user.getUsername())){
				SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {


					protected Boolean doInBackground() throws Exception {
						instagram.sendRequest(new InstagramFollowRequest(user.getPk()));
						return true;

					}

					protected void done()
					{
						ListadoSeguidores.estado.setText(user.getUsername()+" ha sido seguido. "+counter+"/"+size);
						ListadoSeguidores.list.setSelectedValue(user.getUsername()+" / "+ user.getFull_name(), true);
					}

				};
				worker.execute();
				espera(60000,20000);
			}
			counter++;

		}

		// TODO Auto-generated method stub

	}





	public void DejarSeguirUsuarios(List<InstagramUserSummary> usuariosRobados) {

		ListadoSeguidores.estado.setVisible(true);
		List<String> following=getFollowingUsername(instagram.getUserId());

		final int size=usuariosRobados.size();
		counter=0;
		
		for(final InstagramUserSummary user:usuariosRobados)
		{

			if(following.contains(user.getUsername())){
				SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {


					protected Boolean doInBackground() throws Exception {
						instagram.sendRequest(new InstagramUnfollowRequest(user.getPk()));
						return true;

					}

					protected void done()
					{
						ListadoSeguidores.estado.setText(user.getUsername()+" ha sido dejado de seguir. "+counter+"/"+size);
						ListadoSeguidores.list.setSelectedValue(user.getUsername()+" / "+ user.getFull_name(), true);
					}

				};
				worker.execute();
				espera(60000,20000);
			}
			counter++;

		}

		// TODO Auto-generated method stub


	}


	public void DejarSeguirNoTeSiguen(List<InstagramUserSummary> usuariosRobados) {
		ListadoSeguidores.estado.setVisible(true);
		List<String> following=getFollowingUsername(instagram.getUserId());
		List<String> followers=getFollowersUsername(instagram.getUserId());

		final int size=usuariosRobados.size();
		counter=0;
		
		for(final InstagramUserSummary user:usuariosRobados)
		{

			if(following.contains(user.getUsername())&&!followers.contains(user.getUsername())){
				SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {


					protected Boolean doInBackground() throws Exception {
						instagram.sendRequest(new InstagramUnfollowRequest(user.getPk()));
						return true;

					}

					protected void done()
					{
						ListadoSeguidores.estado.setText(user.getUsername()+" ha sido dejado de seguir. "+counter+"/"+size);
						ListadoSeguidores.list.setSelectedValue(user.getUsername()+" / "+ user.getFull_name(), true);
					}

				};
				worker.execute();
				espera(60000,20000);
			}
			counter++;

		}
	}



	private List<String> getFollowersUsername(long pk) {

		List<String> users =new ArrayList<>();
		List<InstagramUserSummary> followers = getFollowers(pk);
		for(InstagramUserSummary user:followers)
		{

			users.add(user.getUsername());
		}


		return users;
	}




}
