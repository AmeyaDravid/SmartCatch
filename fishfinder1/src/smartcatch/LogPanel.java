package smartcatch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogPanel extends JPanel {
	static long sleepTime = 500;
	JTextArea ta = new JTextArea();
	JScrollPane scrollPane;
	File logFile;
	Thread connectThread;
	SmartCatch app = SmartCatch.app;
	boolean done = false;

	public LogPanel(File logFile) {
		this.setPreferredSize(new Dimension (400, 800));
		this.setBackground(Color.green);
		this.logFile = logFile;
		scrollPane = new JScrollPane(ta);
		ta.setLineWrap(true);
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		connect();
	}

	public void connect() {
		if (connectThread == null) {
			connectThread = new ConnectThread();
			try {
				connectThread.start();
			} catch (Exception ex) {
				app.debug.report(ex);
			}
		}
	}
	
	public void disconnect() {
		done = true;
		connectThread = null;
	}

	class ConnectThread extends Thread {
		public void run() {
			try {
				BufferedReader input = new BufferedReader(new FileReader(
						logFile));
				String currentLine = null;

				while (!done) {
					if ((currentLine = input.readLine()) != null) {
						ta.append(currentLine + "\n");
						ta.setCaretPosition(ta.getDocument().getLength());
						continue;
					}

					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}

				}
				input.close();
			} catch (Exception ex) {
			}
		}
	}
}
