package kr.co.coders.ajh.server.listener;

public interface IRecvCompleteListener {
		public void onRecvComplete(byte[] datas , int size , int from  );
}
