package smartcatch;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Debug {
	
	String appDir;
	String appName = "SmartCatch";
	String logFileName = appName + "Log.txt";
	static PrintStream ps = null;
	SmartCatch app = smartcatch.SmartCatch.app;
	File logFile = null;
	
	public Debug() {
		init();
	}
	
	public File getLogFile() {
		return (logFile);
	}
	
	public void init() {
		try {			
			System.out.flush();
			appDir = System.getProperty("user.home") + "/" + appName + "/";
			Date date = new Date();
			String str = date.toString();
			StringTokenizer st = new StringTokenizer(str);
			String dayOfWeek = st.nextToken();
			String month = st.nextToken() + "/";
			String day = st.nextToken() + "/";
			String time = st.nextToken();
			String zone = st.nextToken();
			String year = st.nextToken() + "/";

			String logsDir = appDir + "logs/";
			String yearDir = logsDir + year;
			String monthDir = yearDir + month;
			String dayDir = monthDir + day;

			logFile = new File(appDir + logFileName);
			
			boolean success = false;
			File file = new File(appDir);
			if (!file.exists()) {
				success = file.mkdir();
			}
			file = new File(logsDir);
			if (!file.exists()) {
				success = file.mkdir();
			}
			file = new File(yearDir);
			if (!file.exists())
				success = file.mkdir();
			file = new File(monthDir);
			if (!file.exists())
				success = file.mkdir();
			file = new File(dayDir);
			if (!file.exists())
				success = file.mkdir();
			logFile = new File(dayDir + logFileName);
			FileOutputStream fdOut = new FileOutputStream(logFile, true);
			ps = new PrintStream(new BufferedOutputStream(fdOut, 128), true);
			
			if (ps != null) {
				System.setOut(ps);
				System.setErr(ps);
			}
			logln("");
			logln("-------------------------------------------------------------------------------------------");
			logln(appName + " started ");
		}
		catch (java.io.IOException io) {
			report(io, "Can not create file: " + logFileName);
		}
	}

	public void logln(String str) {
		String dstr = new Date().toString() + " - " + str;
		if (ps != null) {
			ps.println(dstr);
			ps.flush();
		}
	}

	public void log(String str) {
		if (str == null) {
			System.out.println ("null string 1");
		}
		if (str.equals("null")) {
			System.out.println ("null string 2");
		}
		else if (ps != null) {
			String dstr = new Date().toString() + " - " + str;
			ps.print(dstr);
			ps.flush();
		}
	}

	public static void dumpArray(double[] a, String comment, boolean newlines) {
		int n = a.length;
		System.out.println("dumpArray(size == " + n + "):" + comment + " >>");
		for (int i = 0; i < n; i++) {
			System.out.println(a[i]);
		}
		System.out.println("<<");
	}

	public static void dumpArray(double[] a, String comment) {
		int n = a.length;
		System.out.println("dumpArray(size==" + n + "):" + comment + ">>");
		for (int i = 0; i < n; i++) {
			System.out.print(a[i] + ", ");
		}
		System.out.println("<<");
	}

	public static void dumpArray(String[] a, String comment) {
		int n = a.length;
		System.out.println("dumpArray(size==" + n + "):" + comment + ">>");
		for (int i = 0; i < n; i++) {
			System.out.print(a[i] + ", ");
		}
		System.out.println("<<");
	}

	public static void dumpArraySpecial(double[] a, int dim, String comment) {
		int n = a.length;
		System.out.println("dumpArraySpecial(size==" + n + "):" + comment + ">>");
		int r = 0;
		for (int i = 0; i < n; i++) {
			System.out.print(a[i] + ", ");
			r = i % dim;
			if(r > 0) {
				System.out.println("");
			}
		}

		System.out.println("<<");
	}

	public static void dumpArray(double[][] a, String comment) {
		int alen = a.length;
		int a0len = a[0].length;
		System.out.println("dumpArray(size==" + alen + " x " + a0len + "):" +
						   comment + ">>");
		for (int i = 0; i < alen; i++) {
			for (int j = 0; j < a0len; j++) {
				System.out.print(a[i][j] + ", ");
			}
		}
		System.out.println("<<");
	}

	public static void dumpArray(int[] a, String comment) {
		int n = a.length;
		System.out.println("dumpArray(size==" + n + "):" + comment + ">>");
		for (int i = 0; i < n; i++) {
			System.out.print(a[i] + ", ");
		}
		System.out.println("<<");
	}

	public static String getExceptionLine() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		(new Throwable()).printStackTrace(pw);
		pw.flush();
		String stackTrace = baos.toString();
		pw.close();

		StringTokenizer tok = new StringTokenizer(stackTrace, "\n");
		String str = tok.nextToken(); // 'java.lang.Throwable'
		str = tok.nextToken(); // 'at ...getCurrentMethodName'
		str = tok.nextToken(); // 'at ...handlereportableException'
		str = tok.nextToken(); // 'at ...<caller to getCurrentRoutine>'
		// Parse line 3
		// tok = new StringTokenizer(str.trim(), " <(");
		// String t = tok.nextToken(); // 'at'
		// t = tok.nextToken(); // '...<caller to getCurrentRoutine>'
		return str;
	}

	public void report (Exception ex) {
		report (ex, null);
	}

	public void report (OutOfMemoryError oom) {
		Exception ex = new Exception ("OutOfMemory");
		report (ex);
	}

	class ErrorReport {
		String userName;
		String message;
		String details;
		String stackTrace;

		public ErrorReport (String userName, String message, String details, String stackTrace) {
			this.userName = userName;
			this.message = message;
			this.details = details;
			this.stackTrace = stackTrace;
		}
	}

	Vector<ErrorReport> errors = new Vector<ErrorReport>();

	public void postError(String userName, String message, String details, String stackTrace) {
		ErrorReport errorReport = new ErrorReport(userName, message, details, stackTrace);
		errors.addElement(errorReport);
		sendError (userName, message, details, stackTrace);
	}

	public void sendError(String userName, String message, String details, String stackTrace) {
		try {
			URL url;
			URLConnection urlConn;
			DataOutputStream printout;
			DataInputStream input;
			// URL of CGI-Bin script.
			url = new URL("http://twocats.com/smartcatch/reportError.php");
			// URL connection channel.
			urlConn = url.openConnection();
			// Let the run-time system (RTS) know that we want input.
			urlConn.setDoInput(true);
			// Let the RTS know that we want to do output.
			urlConn.setDoOutput(true);
			// No caching, we want the real thing.
			urlConn.setUseCaches(false);
			// Specify the content type.
			urlConn.setRequestProperty
				("Content-Type", "application/x-www-form-urlencoded");
			// Send POST output.
			printout = new DataOutputStream(urlConn.getOutputStream());
			String content =
				"userName=" + URLEncoder.encode(userName, "UTF-8") +
				"&message=" + URLEncoder.encode(message, "UTF-8") +
				"&details=" + URLEncoder.encode(details, "UTF-8") +
				"&stackTrace=" + URLEncoder.encode(stackTrace, "UTF-8");
			printout.writeBytes(content);
			printout.flush();
			printout.close();
			// Get response data.
			input = new DataInputStream(urlConn.getInputStream());
			String str;
			while (null != ( (str = input.readUTF()))) {
				System.out.println(str);
			}
			input.close();
		} catch (IOException io) {
		}
	}

	public void report (Exception ex, String errorMessage) {
		if(ex != null) {
			String command = getExceptionLine();
			String simpleMessage = appName + " encountered the following error: " + ex.getMessage() + "\n";
			String detailedMessage = "";
			if (errorMessage != null) {
				simpleMessage += errorMessage + "\n";
			}
			simpleMessage += appName +
				" was trying to complete the following command:\n" + command +
				"\n";
			String stackTrace;
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			StringBuffer sb = sw.getBuffer();
			stackTrace = sb.toString();

			String dateStr = new Date().toString();
			String detailedStr = "Date: " + dateStr + "\n";
			detailedStr += app.getVersion();
			logln(simpleMessage);
			logln(detailedStr);
			logln(stackTrace);
			String javaProperties = app.getJavaProperties();
			
			detailedMessage = simpleMessage + "\n" + detailedStr + "\n" +
					stackTrace + "\n" + javaProperties;
			ExceptionMessageBox emb = new ExceptionMessageBox(app, ex, appName + " Error",
															  simpleMessage, detailedMessage);
			emb.setVisible(true);
			ex.printStackTrace();
			boolean doPost = false;
			if (doPost) {
				String userName = "Rob";
				postError(userName, simpleMessage, detailedStr, stackTrace);
			}
		}
		else {
			ExceptionMessageBox emb2 = new ExceptionMessageBox(app, null,
											   "Bug Report",
											   "Open the Show Details panel to compose your bug report. \n" +
											   "Use your email program to include the model file or equations if desired.",
											   "Delete this text and describe the bug here.");
			emb2.setVisible(true);
		}
	}

	public static String getCurrentMethodName() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		(new Throwable()).printStackTrace(pw);
		pw.flush();
		String stackTrace = baos.toString();
		System.out.println (stackTrace);
		pw.close();

		StringTokenizer tok = new StringTokenizer(stackTrace, "\n");
		String l = tok.nextToken(); // 'java.lang.Throwable'
		l = tok.nextToken(); // 'at ...getCurrentMethodName'
		l = tok.nextToken(); // 'at ...<caller to getCurrentRoutine>'
		// Parse line 3
		tok = new StringTokenizer(l.trim(), " <(");
		String t = tok.nextToken(); // 'at'
		t = tok.nextToken(); // '...<caller to getCurrentRoutine>'
		return t;
	}
}

class ExceptionMessageBox extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	String simpleMessage;
	String title;
	String detailedMessage;
	JPanel mainPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JMenuBar menuBar = new JMenuBar();
	JMenu editMenu = new JMenu("Edit");

	JScrollPane scrollPane1;
	JTextArea taSimple = new JTextArea("");
	JScrollPane scrollPane2;
	JTextArea taDetailed = new JTextArea ("", 50, 50);
	JButton ok = new JButton("Ok");
	JButton showDetails = new JButton("Show Details");
	JFrame parentFrame;
	JMenuItem copyItem;
	JMenuItem selectAllItem;

	public ExceptionMessageBox(JFrame frame, Exception exception, String title,
							   String simpleMessage, String detailedMessage) {
		super(frame, true);
		this.setTitle(title);
		this.simpleMessage = simpleMessage;
		this.detailedMessage = detailedMessage;
		parentFrame = frame;
		setSize(600, 400);
		scrollPane1 = new JScrollPane(taSimple,
									  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
									  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane2 = new JScrollPane(taDetailed,
									  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
									  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		taSimple.setText(simpleMessage);
		taSimple.setLineWrap(true);
		taSimple.setEditable(false);
		taDetailed.setLineWrap(true);
		taDetailed.setEditable(true);
		mainPanel.setLayout(new GridLayout(2, 1));
		mainPanel.add(scrollPane1);
		mainPanel.add(scrollPane2);
		scrollPane2.setVisible(false);
		selectAllItem = new JMenuItem("Select All");
		selectAllItem.addActionListener(this);
		editMenu.add(selectAllItem);
		copyItem = new JMenuItem("Copy");
		copyItem.addActionListener(this);
		editMenu.add(copyItem);
		menuBar.add(editMenu);
		this.setJMenuBar(menuBar);
		editMenu.addActionListener(this);
		getContentPane().add("Center", mainPanel);
		getContentPane().add("South", buttonPanel);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(showDetails);
		buttonPanel.add(ok);
		ok.addActionListener(this);
		showDetails.addActionListener(this);
		taDetailed.setText(detailedMessage);
		addWindowListener(new MyWindowAdapter());
		center();
	}
	
	public class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			we.getWindow().dispose();
		}
	}


	private void center() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = this.getSize();
		setLocation(screenSize.width / 2 - (dlgSize.width / 2),
					screenSize.height / 2 - (dlgSize.height / 2));
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == ok) {
			dispose();
		}
		if (evt.getSource() == showDetails) {
			if (showDetails.getText() == "Show Details") {
				taDetailed.setCaretPosition(0);
				scrollPane2.setVisible(true);
				showDetails.setText("Hide Details");
			}
			else {
				scrollPane2.setVisible(false);
				showDetails.setText("Show Details");
			}
		}
		else if (evt.getSource() == this.copyItem) {
			String astr = taSimple.getSelectedText();
			if (astr != null)
				taSimple.copy();
			String bstr = taDetailed.getSelectedText();
			if (bstr != null)
				taDetailed.copy();
		}
		else if (evt.getSource() == this.selectAllItem) {
				taDetailed.selectAll();
		}
	}
}
