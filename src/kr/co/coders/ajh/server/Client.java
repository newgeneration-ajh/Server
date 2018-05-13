package kr.co.coders.ajh.server;

import java.net.Socket;
import kr.co.coders.ajh.server.runnable.RecvRunnable;
import kr.co.coders.ajh.server.runnable.SendRunnable;

public class Client {
	private Socket mSocket = null;
	private RecvRunnable mRecvRunnable = null;
	private SendRunnable mSendRunnable = null;
	
	
}
