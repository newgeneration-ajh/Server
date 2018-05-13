package kr.co.coders.ajh.server.runnable;

import java.io.OutputStream;
import java.io.IOException;

public class SendRunnable implements Runnable {
	
	private OutputStream mOutputStream = null;
	private byte[] mDatas = null;
	private int mDataSize = 0;
	
	public SendRunnable ( OutputStream outputStream ) {
		mOutputStream = outputStream;
	}
	
	public void setData ( byte[] datas , int size ) {
		mDatas = datas;
		mDataSize = size;
	}
	
	@Override
	public void run() {
		try { 
			byte[] sendDataSize = new byte[2];
            sendDataSize[1] = (byte)( ( mDataSize & 0x0000ff00 ) >> 8 ) ;
            sendDataSize[0] = (byte)( ( mDataSize & 0x000000ff ) );
			mOutputStream.write(sendDataSize , 0 , 2);
			mOutputStream.write(mDatas , 0, mDataSize );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}