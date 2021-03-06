package kr.co.coders.ajh.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import kr.co.coders.ajh.server.runnable.AcceptRunnable;
import kr.co.coders.ajh.server.listener.IAcceptListener;
import kr.co.coders.ajh.server.listener.IClosedSocketListener;
import kr.co.coders.ajh.server.listener.IRecvCompleteListener;
import kr.co.coders.ajh.server.listener.ISendCompleteListener;
import kr.co.coders.ajh.server.memory_pool.MemoryPool;

public class Server implements IAcceptListener , IClosedSocketListener , 
IRecvCompleteListener , ISendCompleteListener {
	
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
	
	public void sendAllClients( byte[] datas , int size  ) {
		mClientHash.forEach( (k,v) -> {
			v.getSendRunnable().setData(datas,size);
			mThreadPoolExcutor.submit(v.getSendRunnable());
		});
	}
	
	public void sendAllClientsWithoutMe( byte[] datas , int size , int from ) {
		mClientHash.forEach( (k,v) -> {
			if ( k != from ) {
				v.getSendRunnable().setData(datas,size);
				mThreadPoolExcutor.submit(v.getSendRunnable());
			}
		});
	}
	
	public void sendEcho( byte[] datas , int size , int from ) {
		Client client = mClientHash.get(from);
		client.getSendRunnable().setData(datas,size);
		mThreadPoolExcutor.submit(client.getRecvRunnable());
	}
	
	@Override
	public void onSendComplete ( byte[] datas ) {
		MemoryPool.getInstance().returnMemory(datas);
	}
	
	@Override
	public void onRecvComplete ( byte[] datas , int size , int from ) {
		MemoryPool.getInstance().returnMemory(datas);
		mThreadPoolExcutor.submit( mClientHash.get(from).getRecvRunnable());
	}
	
	@Override
	public void onClosedSocket ( int hashCode ) {
		System.out.println("bye : " + hashCode);
		mClientHash.remove(hashCode);
	}
	
	@Override
	public void onAccept ( Socket socket ) {
		Client client = new Client(socket , this , this , this );
		mClientHash.put(client.hashCode() , client );
		System.out.println("Hello : " + client.hashCode());
		mThreadPoolExcutor.submit(mAcceptRunnable);
		mThreadPoolExcutor.submit(client.getRecvRunnable());
	}
}
