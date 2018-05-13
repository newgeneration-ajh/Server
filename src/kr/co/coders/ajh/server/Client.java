package kr.co.coders.ajh.server;

import java.io.IOException;
import java.net.Socket;
import kr.co.coders.ajh.server.runnable.RecvRunnable;
import kr.co.coders.ajh.server.runnable.SendRunnable;

import kr.co.coders.ajh.server.listener.onRecvCompleteListener;
import kr.co.coders.ajh.server.listener.onClosedSocketListener;
import kr.co.coders.ajh.server.listener.onSendCompleteListener;

public class Client {
	private Socket mSocket = null;
	private RecvRunnable mRecvRunnable = null;
	private SendRunnable mSendRunnable = null;
	
	public Client ( Socket socket , onRecvCompleteListener recvCompleteListener ,
				    onSendCompleteListener sendCompleteListener ,
				    onClosedSocketListener closedSocketListener ) {
		mSocket = socket;
		try {
			mRecvRunnable = new RecvRunnable( socket.getInputStream() , recvCompleteListener , 
											 closedSocketListener , this.hashCode() );
			mSendRunnable = new SendRunnable ( socket.getOutputStream() );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int hashCode() {
		return mSocket.hashCode();
	}
	
	public RecvRunnable getRecvRunnable() {
		return mRecvRunnable;
	}
	
	public SendRunnable getSendRunnable() {
		return mSendRunnable;
	}
}
