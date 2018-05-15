package kr.co.coders.ajh.server.listener;

import java.net.Socket;

public interface IClosedSocketListener {
	public void onClosedSocket ( int mHashCode );
}
