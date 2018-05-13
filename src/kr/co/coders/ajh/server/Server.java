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
import kr.co.coders.ajh.server.memory_pool.MemoryPool;

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
			mThreadPoolExcutor.submit(mAcceptRunnable);
		} catch ( IOException e ) {
			e.printStackTrace();	
		}
	}

	@Override
	public void onRecvComplete ( byte[] data , int size , int from ) {
		System.out.println("Hello Message From : " + from );
		System.out.println(new String(data,0,size) + " " + data );
		MemoryPool.getInstance().returnMemory(data);
		mThreadPoolExcutor.submit( mClientHash.get(from).getRecvRunnable());
	}
	
	@Override
	public void onClosedSocket ( int hashCode ) {
		mClientHash.remove(hashCode);
	}
	
	@Override
	public void onAccept ( Socket socket ) {
		Client client = new Client(socket , this , this );
		mClientHash.put(client.hashCode() , client );
		System.out.println("Hello : " + client.hashCode());
		mThreadPoolExcutor.submit(mAcceptRunnable);
		mThreadPoolExcutor.submit(client.getRecvRunnable());
	}
}
