package serverAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import utils.Settings;

//Basic interface that connect to a server.
//Uses port and address loaded from settings
public class ClientSide extends Connection{
//	final int PORT = 44394;
	Socket socket;
	PrintWriter out;
	BufferedReader in;

	@Override
	public void close(){
		try{socket.close();}
		catch(IOException e){e.printStackTrace();}
		socket = null;
	}

	@Override
	public boolean isClosed(){
		return socket == null || socket.isClosed();
	}

	public ClientSide(MessageReceiver rec, Settings settings){
		super(rec);
		try{
			//Look for a server at host:port
			socket = new Socket(settings.getString("host", "127.0.0.1"), settings.getInt("port", 44394));
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Connected to server");
		}
		catch(IOException e){
			System.out.println("Unable to connect to server!");
			return;
		}

		//ioThread
		new Thread(){
			@Override public void run(){
				while(!isClosed()){
					try{
						while(in.ready()){
							String line = in.readLine();
//							System.out.println("Received: "+line);
							receiver.receiveMessage(line);
						}
					}
					catch(IOException e){e.printStackTrace();}
				}
			}
		}.start();
	}
	
	@Override
	public void println(String message){
		if(isClosed()) return;
		out.println(message);
		out.flush();
//		System.out.println("Sent: "+message);
	}
}