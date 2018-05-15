package kr.co.coders.ajh.server.runnable;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

import kr.co.coders.ajh.server.listener.IAcceptListener;

public class AcceptRunnable implements Runnable {
	private ServerSocket mServerSocket = null;
	private IAcceptListener mAcceptListener = null;
	
	public AcceptRunnable ( ServerSocket serverSocket , 
						    IAcceptListener acceptListener ) {
		mServerSocket = serverSocket;
		mAcceptListener = acceptListener;
	}
	
	@Override
	public void run() {
		try {
			Socket tmpSocket = mServerSocket.accept();
			mAcceptListener.onAccept(tmpSocket);
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
