package com.boliche.proyecto.Bolichongo;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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

import interfaces.ListadoSeguidores;
import interfaces.MainFrame;

public class Plataforma {

	private static Plataforma INSTANCE;
	private static int counter;
	private static int longbreak;



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

	private List<String> getFollowersUsername(long pk) {

		List<String> users =new ArrayList<>();
		List<InstagramUserSummary> followers = getFollowers(pk);
		for(InstagramUserSummary user:followers)
		{

			users.add(user.getUsername());
		}


		return users;
	}

	public void comentarFoto(long pk,String mensaje)
	{
		InstagramFeedResult result = null;
		try {
			result = instagram.sendRequest(new InstagramUserFeedRequest(pk));
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



	public void mandarMensaje(long userPk,String mensaje)
	{

		ArrayList<String> recipients = new ArrayList<String>();

		String id= Long.toString(userPk);
		recipients.add(id);
		//System.out.println(user.getUser().getPk());
		

		try {
			instagram.sendRequest(InstagramDirectShareRequest.builder(ShareType.MESSAGE, recipients).message(mensaje).build());

		} catch (IOException e) {
			//System.out.println("No se pudo enviar el mensaje a "+ userResult(usuario);
			System.out.println("algo paso");
			e.printStackTrace();
		}
	}


	public void enviar(final List<InstagramUserSummary> usuariosRobados, final List<String> mensajes,int index,final boolean DM) {

		counter=index+1;

		final int cantidad= usuariosRobados.size();

		for(int i=index;i<usuariosRobados.size();i++)
		{
			
			final int j=generarRandom(mensajes.size());
			final InstagramUserSummary user=usuariosRobados.get(i);

			SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
				protected Boolean doInBackground() throws Exception {
					
					//o es un Direct Message o hay que comentar las fotos
				
					if(DM)mandarMensaje(user.getPk(), mensajes.get(j));
					else comentarFoto(user.getPk(), mensajes.get(j));
					return true;

				}

				protected void done()
				{
					if(DM)MainFrame.status.setText("Enviados "+ counter +"/"+ cantidad+". Mensaje enviado a: "+ user.getUsername());
					else MainFrame.status.setText("Comentados "+ counter +"/"+ cantidad+". Comentado a: "+ user.getUsername());

				}

			};
			worker.execute();
			
	
			//espera entre 80 y 120 segundos.
			espera(80000,40000);
			
		
			if(longbreak==50){
				//cuando mande 50 espera 10 minutos
				espera(500000,100000);
				longbreak=0;
			}
			counter++;
			longbreak++;
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





}
