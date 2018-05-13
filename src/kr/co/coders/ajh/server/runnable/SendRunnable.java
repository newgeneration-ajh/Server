package kr.co.coders.ajh.server.runnable;

import java.io.OutputStream;
import java.io.IOException;

public class SendRunnable implements Runnable {
	
	private OutputStream mOutputStream = null;
	
	public SendRunnable ( OutputStream outputStream ) {
		mOutputStream = outputStream;
	}
	
	@Override
	public void run() {
		
	}
}
