package kr.co.coders.ajh.server.runnable;

import java.io.InputStream;
import java.io.IOException;

import kr.co.coders.ajh.server.listener.onRecvCompleteListener;
import kr.co.coders.ajh.server.listener.onClosedSocketListener;
import kr.co.coders.ajh.server.memory_pool.MemoryPool;

public class RecvRunnable implements Runnable {
	private InputStream mInputStream = null;
	private onRecvCompleteListener mRecvCompleteListener = null;
	private onClosedSocketListener mClosedSocketListener = null;
	private int mHashCode = 0;
	
	public RecvRunnable ( InputStream inputStream , 
						 onRecvCompleteListener recvCompleteListener , 
						 onClosedSocketListener closedSocketListener ,
						 int hashCode ) {
		mInputStream = inputStream;
		mRecvCompleteListener = recvCompleteListener;
		mClosedSocketListener = closedSocketListener;
		mHashCode = hashCode;
	}
	
	@Override
	public void run() {
		try { 
			byte[] dataSizeArray = new byte[2];
			if ( mInputStream.read(dataSizeArray , 0  , 2) == 2 ) {
				byte[] datas = MemoryPool.getInstance().allocate();
				int recvDataSize = 0;
				int dataSize = calcSizeByteArray(dataSizeArray);
				
				while ( dataSize != recvDataSize ) {
					int tmpSize = mInputStream.read(datas, recvDataSize, dataSize - recvDataSize);
					if ( tmpSize == -1 ) {
						mClosedSocketListener.onClosedSocket(mHashCode);
						return;
					}
					recvDataSize += tmpSize;
				}
				if ( mRecvCompleteListener != null ) {
					mRecvCompleteListener.onRecvComplete(datas, recvDataSize , mHashCode );
				}
				else {
					System.out.println("Recv Complete Listener null!!");
				}
			}
			else {
				mClosedSocketListener.onClosedSocket(this.hashCode());
			}
		} catch ( IOException e ) { 
			e.printStackTrace();
		}
	}
	
	private int calcSizeByteArray( byte[] sizeByte ) {
		int dataSize = sizeByte[1] << 8;
		dataSize |= sizeByte[0];
		
		return dataSize;
	}
}
