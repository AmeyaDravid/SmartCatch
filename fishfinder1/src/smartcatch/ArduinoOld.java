package smartcatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ArduinoOld {
	PrintWriter out;
	BufferedReader in;
	SmartCatch app = smartcatch.SmartCatch.app;
	String ipAddress;
	int port;
	int timeout;
	Socket socket = new Socket();
	public int DISCONNECTED = 0;
	public int CONNECTED = 1;
	public int CONNECTING = 2;
	public int connectStatus = DISCONNECTED;
	public boolean connecting;
	ConnectThread connectThread;

	public ArduinoOld() {
		connect();
	}
	
	class ConnectThread extends Thread {
		public void run() {
			try {
				connectStatus = CONNECTING;
				AboutPanel aboutPanel = app.aboutPanel;
				ipAddress = aboutPanel.getPreference(Cn.SubmarineIPAddress);
				String portStr = aboutPanel.getPreference(Cn.SubmarineIPPort);
				port = Integer.parseInt(portStr);
				String timeoutStr = aboutPanel.getPreference(Cn.SubmarineTimeoutSeconds);
				timeout = Integer.parseInt(timeoutStr);
				app.debug.logln ("Arduino connecting to " + ipAddress + " " + port);
				
				socket.connect(new InetSocketAddress(ipAddress, port), timeout * 1000);
				if (socket != null) {
					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					connectStatus = CONNECTED;
					app.debug.logln ("Arduino connected to " + ipAddress + " " + port);
				}
			} catch (IOException ex) {
				app.debug.logln ("Arduino: " + ex.getMessage());
				connectStatus = DISCONNECTED;
			}
		}
	}
	
	public void connect() {
		if (connectThread == null) {
			connectThread = new ConnectThread();
			try {
				connectThread.start();
			}
			catch (Exception ex) {
				app.debug.report(ex);
			}
		}
	}
	
	public void disconnect() throws IOException {
		close();
	}

	public void send(String msg) {
		try {
			if (out != null) {
				if (connectStatus == CONNECTED) {
					boolean connected = socket.isConnected();
					if (connected) {
						out.println(msg + "\n");
						String response = in.readLine();  // mg911 put the response reader in a separate thread
						app.debug.logln("arduino> " + response);
					}
					else {
						app.debug.logln("arduino is not connected.");
						// mg911 reconnect
					}
				}
				else if (connectStatus == DISCONNECTED) {
					app.debug.logln("Arduino disconnected, but attempted to send: " + msg);
				}
				else if (connectStatus == CONNECTING) {
					app.debug.logln("Arduino connecting, but attempted to send: " + msg);
				}
			}
		} catch (IOException io) {
			app.debug.report(io);
		}
		catch (Exception ex) {
			app.debug.report(ex);
		}
	}

	public void close() throws IOException {
		connectStatus = DISCONNECTED;
		if (out != null)
			out.close();
		if (in != null)
			in.close();
		if (socket != null)
			socket.close();
	}
}
