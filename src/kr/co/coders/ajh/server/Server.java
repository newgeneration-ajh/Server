package kr.co.coders.ajh.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import kr.co.coders.ajh.server.runnable.AcceptRunnable;
import kr.co.coders.ajh.server.listener.onAcceptListener;
import kr.co.coders.ajh.server.listener.onClosedSocketListener;
import kr.co.coders.ajh.server.listener.onRecvCompleteListener;

public class Server implements onAcceptListener , onClosedSocketListener , 
onRecvCompleteListener {
	
	private ThreadPoolExecutor mThreadPoolExcutor = null;
	private ServerSocket mServerSocket = null;
	private AcceptRunnable mAcceptRunnable = null;
	private HashMap<Integer , Client> mClientHash = null;
	
	public Server ( int port ) {
		try {
			mServerSocket = new ServerSocket(port);
			mThreadPoolExcutor =(ThreadPoolExecutor)Executors.newFixedThreadPool(10);
			mAcceptRunnable = new AcceptRunnable(mServerSocket , this);
			mClientHash = new HashMap<>();
		} catch ( IOException e ) {
			e.printStackTrace();	
		}
	}
	@Override
	public void onRecvComplete ( byte[] data , int size , int from ) {
		
	}
	
	@Override
	public void onClosedSocket ( int hashCode ) {
	
	}
	
	@Override
	public void onAccept ( Socket socket ) {
		
	}
}
