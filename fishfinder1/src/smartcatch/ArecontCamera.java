package smartcatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class ArecontCamera implements Runnable {
	private SmartCatch app = smartcatch.SmartCatch.app;
	public boolean isConnected = false;
	private ArecontListener listener;
	Thread comThread;
	boolean done = false;
	CommandList cl = new CommandList();
	
	private class CommandList {
		private Vector<Command> commands = new Vector<Command>();
		
		public synchronized void addCommand (String getset, String name, String value) {
			boolean found = false;
			
			for (int i = 0; i < commands.size(); i++) {
				Command command = (Command)commands.elementAt(i);
				if (command.getset.equals(getset) && command.name.equals(name)) {
					command.value = value;
					found = true;
				}
			}
			if (!found) {
				commands.add(new Command(getset, name, value));
			}
		}
		
		public synchronized Command getCommand() {
			Command com = null;
			if (commands.size() > 0)
				com = commands.remove(0);
			return com;
		}
		
	}
	
	public ArecontCamera (ArecontListener listener) {
		this.listener = listener;
		comThread = new Thread(this);
	}
	
	public void startListening() {
		comThread.start();
	}
	
	public void send(String str) {
		String ret = "";
		try {
			String videoURL = app.aboutPanel.getPreference(Cn.VideoURL);
	        URL camera = new URL(videoURL + str);
	        URLConnection yc = camera.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) 
	           ret += inputLine;
	        in.close();
	        setConnected(true);
		}
		catch (ConnectException ce) {
			setConnected(false);
			ret = "connected=false";
		}
		catch (Exception ex) {
			System.out.println ("Exception: " + ex.getMessage());
			ret = "exception=" + ex.getMessage();
		}
		listener.receiveCameraMessage(ret);
	}
	
	public void setConnected (boolean on) {
		this.isConnected = on;
	}
	
	public void setValue(String name, String value) {
		this.cl.addCommand("set", name, value );	
	}
	
	public void getValue(String name) {
		this.cl.addCommand("get", name, null);
	}
	
	class Command {
		public String getset;
		public String name;
		public String value;
		
		public Command (String getset, String name, String value) {
			this.getset = getset;
			this.name = name;
			this.value = value;
		}
		
		public String getStr () {
			String ret = getset + "?" + name;
			if (getset.equals("set"))
				ret += "=" + value;
			return ret;
		}
	}

	@Override
	public void run() {
		while (!done) {
			Command com = cl.getCommand();
			if (com != null) {
				String str = com.getStr();
				send(str);
			} else {
				try {
					Thread.currentThread().sleep(500);
				} catch (InterruptedException ie) {
					
				}
			}
			
		}
		
	}


}
