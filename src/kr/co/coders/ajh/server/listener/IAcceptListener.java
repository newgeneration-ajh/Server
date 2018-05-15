package kr.co.coders.ajh.server.listener;

import java.net.Socket;

public interface IAcceptListener {
	public void onAccept(Socket socket);
}
