package smartcatch;

import java.net.*;
import java.io.*;

public class Arduino1 {
	SmartCatch app = smartcatch.SmartCatch.app;
	String ipAddress;
	int port;
			
	public void send (String msg) {
		try {
			AboutPanel aboutPanel = app.aboutPanel;
			ipAddress = aboutPanel.getPreference(Cn.SubmarineIPAddress);
			String portStr = aboutPanel.getPreference(Cn.SubmarineIPPort);
			port = Integer.parseInt(portStr);
	
			URL ard = new URL(ipAddress + ":" + port + "/" + msg);
			URLConnection yc = ard.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
				System.out.println(inputLine);
			in.close();
		}
		catch (Exception ex) {
			
		}
	}
}
