package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.Timer;
import serverAPI.*;
import utils.Blot;
import utils.BodyList;
import utils.Line;
import utils.Settings;
import utils.Utils;

public class ServerMain implements MessageReceiver, ActionListener{
	public static void main(String[] args){ new ServerMain(); }

	BodyList bodyList;
	Settings settings;
	HashMap<Integer, Player> players;
	Connection serverHook;
	Blot playerShape;
	
	ServerMain(){
		settings = new Settings();
		players = new HashMap<Integer, Player>();
		bodyList = new BodyList();
		serverHook = new ServerSide(this, settings);
		//TODO: read in body shape
		new Timer(1, this).start(); 
	}

	@Override
	public void receiveMessage(String message) {
		int i = message.indexOf(' '), id = Integer.parseInt(message.substring(0, i));
		message = message.substring(i+1);
		try{
			// Assume all messages from a client are just their orientation
			players.get(id).orientation.input(Utils.toInputStream(message));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override public void actionPerformed(ActionEvent e){
		// Timer has been triggered! Send data to clients
//		bodyList.clear();//TODO: sd
		for(Player player : players.values()) {
			Blot shape = new Blot();
			shape.fill = playerShape.fill;
			shape.bound = new Line();
			bodyList.blots.add(e)
		}
		serverHook.println(Utils.toString(bodyList));
	}
}