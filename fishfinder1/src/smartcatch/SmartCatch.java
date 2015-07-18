package smartcatch;

import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT;
import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT;
import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;







import boofcv.io.image.UtilImageIO;

import com.github.sarxos.webcam.Webcam;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class SmartCatch extends JFrame {
	//flag for the buttons
	public static int recursionNumber=0;
	public static boolean playPauseFlag=true;
	public static boolean changeTabFlag=true;
	public static int changeTabIndex=0;
	public static boolean startConversionCue=true;
	private static final long serialVersionUID = -5908767652173440643L;
	static public SmartCatch app;
	public String version = "SmartCatch 0.7s";
	public Debug debug;
	public static JPanel vidPane = new JPanel();
	public static JFrame frame=new JFrame();
	public static VideoPanel videoPanel;
	public RightPanel rightPanel;
	public AboutPanel aboutPanel;
	public Arduino arduino;
	public ArecontCamera camera;
	public static ComPanel comPanel;
	public boolean translucencySupported;
    boolean isUniformTranslucencySupported;
    boolean isPerPixelTranslucencySupported;
    boolean isShapedWindowSupported;
    static int flag=0;
    VideoCap videoCap=new VideoCap();
    
    JButton play=new JButton("start recording");
    JButton pause=new JButton("stop recording");
    JPanel buttonPanel=new JPanel();
	
	public SmartCatch() throws InterruptedException, IOException, SQLException {
		
		app = this;
		
		configSlider ();
	    setTitle(version);
		debug = new Debug();
		debug.logln("starting: " + version);
		debug.logln(this.getJavaProperties());
		this.addWindowListener(new MyWindowAdapter());
		aboutPanel = new AboutPanel();
		rightPanel = new RightPanel();
		arduino = new Arduino();
		comPanel = new ComPanel();
		
	    JTabbedPane tabbedPane = new JTabbedPane();
	    
	    
	    
	    tabbedPane.addTab("Control", vidPane);
	    tabbedPane.addTab("Playback", comPanel);
	   // tabbedPane.addTab("Preferences", aboutPanel);
	    tabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				
					changeTabIndex++;
				
				
			}
		});
	    JRootPane root = getRootPane();
	    root.setLayout(new BorderLayout());
	    root.add(tabbedPane, BorderLayout.CENTER);
		System.setProperty ("apple.laf.useScreenMenuBar","true");
        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vidPane.setLayout(new BorderLayout());
        videoPanel = new VideoPanel();
        vidPane.add(videoPanel, BorderLayout.CENTER);
        vidPane.add(rightPanel, BorderLayout.EAST);     
        this.addCloseListener();
        this.addShutdownHook();
        vidPane.validate();
       
       vidPane.add(buttonPanel);
       
      
		pause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0){
				System.out.println("you pressed pause");
				playPauseFlag=true;
				startConversionCue=false;
				try {
					try {
						Recorder.deleteFiles();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				int result = JOptionPane.showConfirmDialog(frame,
						"play video?",
						"play video", JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.YES_OPTION){
					
					
				}
			}
		});
		vidPane.repaint();

        this.repaint();
        int i=0;
        flag++;
        
        
        //code for getting the stream-repaint the frame
        Webcam webcam=Webcam.getWebcams().get(0);
        if(flag==0){
        webcam.open();}
        int j=0;
        
        for(;;i++){
        	RescaleOp rescaleOp = new RescaleOp(RightPanel.showSliderDemo2(), RightPanel.showSliderDemo1(), null);
        	
        	if (changeTabIndex%2==0){
        	//System.out.println(RightPanel.recordOnOffFlag);
        		BufferedImage image=webcam.getImage();
        		rescaleOp.filter(image, image);
        	vidPane.getGraphics().drawImage(image, 100, 100,400,400, this);
        	//vidPane.getGraphics().drawImage(videoCap.getOneFrame(RightPanel.recordOnOffFlag), 100, 100,400,400, this);
        	}
        	if(RightPanel.recordOnOffFlag==false){
        		UtilImageIO.saveImage(webcam.getImage(), System.getProperty("user.dir")+"/camera/"+j+".jpg");
            	j++;
        	}
        	else{j=0;}
        	if (changeTabIndex%2!=0){
        		repaint();
        		//System.out.println(changeTabIndex);
        		
        		mySmartTable.createTable();
        	}
        	
        	//Thread.sleep(50);
        }
        }

	
	
	
	
	
	
	
	

	private void configSlider () {
		Icon icon = createImageIcon("img/slider-nob.png", null);
		
	}
	
	private void checkTranslucency() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		isUniformTranslucencySupported = gd.isWindowTranslucencySupported(TRANSLUCENT);
		isPerPixelTranslucencySupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
		isShapedWindowSupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT);
        
        if (isUniformTranslucencySupported) {
            JFrame.setDefaultLookAndFeelDecorated(true);
        }
	}
	
	class MyWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			try {
				debug.logln("closing");
			} catch (Exception ex) {
				debug.report(ex);
			}
		}
	}
	
	public String getJavaProperties() {
		String str = "Java Properties\n";
		Properties props = System.getProperties();
		Enumeration<?> names = props.propertyNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = (String) props.get(name);
			str += name + " = " + value + "\n";
		}
		return (str);
	}
	
	public String getVersion() {
		return this.version;
	}
	
	private void shutdown() {
		if (this.videoPanel != null) {
		 	videoPanel.stop();
		}
		rightPanel.shutdown();
	}
	
	private void addShutdownHook() {
		// Register a hook to close the application when quit via the app menu.
		Runnable runner = new Runnable() {
			public void run() {
				shutdown();
			}
		};
		Runtime.getRuntime().addShutdownHook(
				new Thread(runner, "Shutdown hook"));
	}
	
	private void addCloseListener() {
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame) e.getSource();
				int result = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to exit SmartCatch?",
						"Exit SmartCatch", JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION) {
					shutdown();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}
		});
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			JOptionPane.showMessageDialog (app, "Couldn't find file: " + path);
			return null;
		}
	}
	
	public void showOnCenter (JFrame frame2) {
		Rectangle bounds = app.getBounds();
		Rectangle fb = frame2.getBounds();
		int left = bounds.x + (bounds.width / 2) - fb.width / 2;
		int top = bounds.y + (bounds.height / 2) - fb.height / 2;
	
		frame2.setLocation(left, top);
		frame2.setVisible(true);
	}
	static float salinity[]=new float[10];
	static float temperature[]=new float[10];
	static String string;
	public static void main (String [] args) throws SQLException, InterruptedException, IOException {
		String string =System.getProperty("user.dir");
		try{
			Runtime.getRuntime().exec("mkdir "+string+"/camera");
			Runtime.getRuntime().exec("mkdir "+string+"/video");
		}finally{
			
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		
        Connection conn=(Connection) DriverManager.getConnection("jdbc:mysql://localhost/SmartCatch","root","versace123");
    	Statement stmt=(Statement) conn.createStatement();
    	System.out.println("Success");
    	
    	ResultSet result=stmt.executeQuery("select * from smarty");
    	int i=0;
    	while(result.next()){
    		salinity[i]=result.getFloat("salinity");
    		temperature[i]=result.getFloat("temperature");
    		i++;
    	}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	/*if(recursionNumber==0){   
       Login.main(null);
       
       recursionNumber++;
       }
    	Thread.sleep(25000);
       System.out.println(Login.toSmartCatch);
		if(Login.toSmartCatch==true)*/{
			try {
				new SmartCatch();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
       }
       }
				
		
	

