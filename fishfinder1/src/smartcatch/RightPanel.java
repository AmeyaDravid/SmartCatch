package smartcatch;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.hansolo.steelseries.gauges.DisplayRectangular;


public class RightPanel extends JPanel implements ActionListener, ArecontListener {
	private static final long serialVersionUID = 1L;
	private SmartCatch app = smartcatch.SmartCatch.app;
	// private JButton videoOptionsButton = new JButton("Video Options");
	private JButton tempHumidityButton = new JButton("Temp & Humidity");
	private ResetButton resetButton;
	private JButton browserButton = new JButton ("Browser");
	private VideoOptionsPanel vop;
	private JFrame videoOptionsFrame;
	public JFrame tempHumidityFrame;
	public RotatePanel rotatePanel = new RotatePanel();
	public LabelSlider sharpnessSlider = new LabelSlider("Sharpness", 0, 4, 1);
	public CameraRadioPanel daynightRadioPanel = new CameraRadioPanel("Daynight", "day", "night", "auto", null);
	// public CameraRadioPanel rotateRadioPanel = new CameraRadioPanel("Rotate", "0", "180", null, null);
	public LabelSlider moveCameraSlider = new LabelSlider("Move Camera", -90, 90, 10);
	public JButton tagVideo = new JButton("Tag Video");
	public StatusPanel statusPanel;
	public VideoControlPanel videoControlPanel;
	public VideoFilterPanel videoFilterPanel;
	public static JSlider brightnessSlider = new JSlider(JSlider.HORIZONTAL,-100,100, 15);
	public static JSlider saturationSlider = new JSlider(JSlider.HORIZONTAL, 0, 4, 1);
	public LightPanel leftLightPanel; 
	public LightPanel rightLightPanel; 
	public MultiGauge tempGauge = new MultiGauge("Temp", "F");
	public MultiGauge salinityGauge = new MultiGauge("Salinity", "%");
	public MultiGauge depthGauge = new MultiGauge("Depth", "ft");
	ImageButton recordButton = new ImageButton ("record", this);
	ImageButton tagButton = new ImageButton ("tag", this);
	ImageButton advButton = new ImageButton ("adv", this);
	
	
	//flags for the recording button
	public static boolean recordOnOffFlag=true;
	public static boolean startConversionCue=false;
	public static int decideStartOrStop=1;
	
	
	
	//data for the slider depending on the position
	public static int getBrightnessdata;
	public static int getSaturationdata;
	
// code for the brightness slider data
	
	public static int showSliderDemo1(){
	      
	      brightnessSlider.addChangeListener(new ChangeListener() {
	         public void stateChanged(ChangeEvent e) {
	            getBrightnessdata=((JSlider)e.getSource()).getValue();
	         }
	      });
	      return getBrightnessdata;
	      /*controlPanel.add(slider);      
	      mainFrame.setVisible(true);     */
	   } 
	
	//code for the saturation slider data
	
	public static int showSliderDemo2(){
	      
	      saturationSlider.addChangeListener(new ChangeListener() {
	         public void stateChanged(ChangeEvent e) {
	            getSaturationdata=((JSlider)e.getSource()).getValue();
	         }
	      });
	      return getSaturationdata;
	     
	   } 

	private BufferedImage getImage(String filename) {
		try {
			InputStream in = getClass().getResourceAsStream(filename);
			return ImageIO.read(in);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(app, "The image was not loaded: " + filename);
		}
		return null;
	}
	
	public class StatusPanel extends ImagePanel implements ActionListener {
		private static final long serialVersionUID = 1L;
		JLabel titleLabel = new JLabel(" STATUS ");
		ImageButton cameraButton = new ImageButton("camera", this);
		ImageButton powerButton = new ImageButton("power", this);
		ImageButton leakButton = new ImageButton("leak", this);
		ImageButton wifiButton = new ImageButton("wifi", this);
		ImageButton tempButton = new ImageButton("temp", this);
		ImageButton logButton = new ImageButton("logfile", this);
		//JButton button=new JButton("play recent");
		

		public StatusPanel(String filename) {
			super(filename);
			this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			this.setPreferredSize(new Dimension(255,60));
			titleLabel.setForeground(Cn.trackColor);
			this.add(titleLabel);
			this.add(cameraButton);
			this.add(powerButton);
			this.add(leakButton);
			this.add(wifiButton);
			this.add(tempButton);
			this.add(logButton);
			//this.add(button);
		}
		
		public void setCameraConnected (boolean on) {
			cameraButton.setSelected(on);
		}
		
		public void handleCameraButton() {
			if (app.videoPanel.isPlaying()) {
				app.videoPanel.stop();
			}
			else {
				app.videoPanel.start();
				app.camera.startListening();  // what about stopListening??? 911
			}
		}
		

		@Override
		
		
		//code for pressing the record button
		
		
		public void actionPerformed(ActionEvent ae) {
			ImageButton but = (ImageButton) ae.getSource();
			JButton buttonOn=(JButton)ae.getSource();
			if (but.equals (cameraButton)) {
				if(decideStartOrStop%2==0)
				{
					
					recordOnOffFlag=true;
					startConversionCue=true;
					try {
						Recorder.deleteFiles();
						int dialogButton = JOptionPane.YES_NO_OPTION;
						int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like To Play The Video","Play",dialogButton);
						if(dialogResult==JOptionPane.YES_OPTION){
						File file=new File(System.getProperty("user.dir")+"/video/"+convertImagesVideo.date+".avi");
						
						Desktop.getDesktop().open(file);
						Thread.sleep(1000);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(decideStartOrStop%2!=0)
				{
					recordOnOffFlag=false;
					
				}
				decideStartOrStop++;
			}	//handleCameraButton();
				
			 //code end here
			
			else if (but.equals (powerButton)) {}
			else if (but.equals (leakButton)) {}
			else if (but.equals (wifiButton)) {}
			else if (but.equals (tempButton)) {}
			else if (but.equals (logButton)) { 
				handleLogButton(); 
			}
			else if (but.equals (recordButton)) {
				recordOnOffFlag=false;
			}
			
		
		
	}
		
		
		
		private LogPanel logPanel;
		
		private void handleLogButton () {
			if (logPanel == null) {
				logPanel = new LogPanel(app.debug.logFile);
				app.videoPanel.add(logPanel, BorderLayout.WEST);
			}
			else {
				logPanel.disconnect();
				app.videoPanel.remove(logPanel);
				logPanel = null;
			}
			app.videoPanel.validate();
		}
		
		private void handleLogButtonOld() {
			JFrame frame = new JFrame();
			JTextArea ta = new JTextArea();
	        ta.setEditable(false); // set textArea non-editable
			frame.getContentPane().setLayout(new BorderLayout());
			JScrollPane scroll = new JScrollPane(ta);
	        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

			scroll.setPreferredSize(new Dimension(500, 500));
			frame.getContentPane().add(scroll, BorderLayout.CENTER);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			app.showOnCenter(frame);

			try {
				String textLine;
				FileReader reader = new FileReader(app.debug.logFile);
				BufferedReader br = new BufferedReader(reader);
			    while((textLine=br.readLine())!=null){
			      textLine = br.readLine();
			      ta.append(textLine + "\n");
			     } 
				br.close();
				ta.requestFocus();
			} catch (Exception e2) {
				app.debug.report(e2);
			}
		}

	}
	
	public class VideoControlPanel extends ImagePanel {
		private static final long serialVersionUID = 1L;
		
		public VideoControlPanel(String filename) {
			super(filename);
			this.setPreferredSize(new Dimension(255,156));

		}
	}
	
	public class VideoFilterPanel extends JPanel {
		private static final long serialVersionUID = 1L;
				
		public VideoFilterPanel(String filename) {
			this.setLayout(new VerticalFlowLayout());
			this.setBackground(Cn.bg);
			this.add(brightnessSlider);
			//brightnessSlider.initIDs();
			this.add(saturationSlider);
			//saturationSlider.init();
			this.add(daynightRadioPanel);
			daynightRadioPanel.init();
		}
	}
	
	
	private class ResetButton extends JButton {
		private static final long serialVersionUID = -1L;
		private ImageIcon icon;

		public ResetButton(String title) {
			super(title);
			this.setBorder(null);
			icon = app.createImageIcon("img/reset-button.gif", "reset");
			this.setIcon(icon);
			this.setBackground(Cn.bg);
		}
	}
	
	
	
	public void receiveCameraMessage(String message) {
		String ret = message;
		if (ret != null) {
			StringTokenizer st = new StringTokenizer(ret, "=");
			String id = null;
			String val = null;
			int intvalue = 0;
			if (st.hasMoreTokens())
				id = st.nextToken();
			if (st.hasMoreTokens()) {
				val = st.nextToken();
			}
			if (id.equalsIgnoreCase("brightness")) {
				intvalue = Integer.valueOf(val).intValue();
				brightnessSlider.setValue(intvalue);
			}
			else if (id.equalsIgnoreCase("saturation")) {
				intvalue = Integer.valueOf(val).intValue();
				saturationSlider.setValue(intvalue);
			}
			else if (id.equalsIgnoreCase("blue")) {
				intvalue = Integer.valueOf(val).intValue();
				vop.blueSlider.setValue(intvalue);
			}
			else if (id.equalsIgnoreCase("red")) {
				intvalue = Integer.valueOf(val).intValue();
				vop.redSlider.setValue(intvalue);
			}
			else if (id.equalsIgnoreCase("daynight"))
				daynightRadioPanel.update(ret);
			/*
			else if (id.equalsIgnoreCase("rotate"))
				rotateRadioPanel.update(ret);
			*/
			else if (id.equalsIgnoreCase("exception")) {
				JOptionPane.showMessageDialog(app, "camera exception: " + val);
			}
			else if (id.equalsIgnoreCase("connected")) {
				if (val.equals("false")) {
					String videoURL = app.aboutPanel.getPreference(Cn.VideoURL);
					JOptionPane.showMessageDialog(app, "camera is not connected at: " + videoURL );
				}
			}
			else {
				JOptionPane.showMessageDialog (app, "unknown camera message: " + id);
			}
		}
		this.updateCameraConnected();
	}

	class ImagePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private String imageFileName;
		private ImageIcon icon;

		public ImagePanel(String fileName) {
			this.setBackground(Cn.bg);
			this.imageFileName = fileName;
			this.setLayout(null);
			rebuild();
		}
		
		private void rebuild() {
			removeAll();
			if (imageFileName != null) {
				icon = app.createImageIcon(imageFileName, "");
			}
		}
		
		public void setImageFile(String fileName) {
			this.imageFileName = fileName;
			icon = null;
			rebuild();
		}
		
		public void paint (Graphics g) {
			super.paint(g);
			if (icon != null)
				g.drawImage(icon.getImage(), 0, 0, null);
		}
	}

	class CameraRadioPanel extends RadioPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		public CameraRadioPanel(String name, String first, String second,
				String third, String fourth) {
			super(name, first, second, third, fourth);
		}

		public void init() {
			String name = getName();
			app.camera.getValue(name);
			updateCameraConnected();
		}
	
		public void actionPerformed(ActionEvent ae) {
			String command = ae.getActionCommand();
			String name = getName();
			app.camera.setValue (name, command);
		}
	}
	
	public void updateCameraConnected () {
		statusPanel.setCameraConnected(app.camera.isConnected);
	}
		
	class CameraLabelDoubleSlider extends DoubleLabelSlider implements ChangeListener {
		private static final long serialVersionUID = 6715551239241789463L;

		public CameraLabelDoubleSlider(String name, double min, double max) {
			super(name, min, max);
		}

		public void stateChanged(ChangeEvent ie) {
			DoubleJSlider slider = (DoubleJSlider) ie.getSource();
			double val = slider.getDoubleValue();
			String str = "" + val;
			String name = label.getName();
			label.setText(name + "   " + val);
			repaint();
			if (!slider.getValueIsAdjusting())
				app.camera.setValue(label.getName(), str);
		}
		
		public void init () {
			String name = label.getName();
			app.camera.getValue(name);
		}		
	}
	
	public RightPanel() {
		setLayout(new VerticalFlowLayout(FlowLayout.LEADING, 0, 0, false, false));
		this.setBackground(Cn.bg);
		this.setPreferredSize(new Dimension (255, 768));
		app.camera = new ArecontCamera(this);
		vop = new VideoOptionsPanel();		
		
		statusPanel = new StatusPanel(null);
		this.add(statusPanel);
		
		JPanel videoControlPanel = new JPanel();
		videoControlPanel.setBackground(Cn.bg);
		videoControlPanel.add(recordButton);
		videoControlPanel.add(tagButton);
		videoControlPanel.add(advButton);
		this.add(videoControlPanel);

		videoFilterPanel = new VideoFilterPanel("img/vid-filter-bkgnd.jpg");
		this.add(videoFilterPanel);
		
		
		this.add(rotatePanel);
		
		AboutPanel aboutPanel = app.aboutPanel;
		
		String leftLightPort = aboutPanel.getPreference(Cn.SubmarineLeftLightPort);
		String leftLightRelayPort = aboutPanel.getPreference(Cn.SubmarineLeftLightRelayPort);
		leftLightPanel = new LightPanel (Cn.SubmarineLeftLightName, leftLightPort, leftLightRelayPort);
		this.add(leftLightPanel);

		String rightLightPort = aboutPanel.getPreference(Cn.SubmarineRightLightPort);
		String rightLightRelayPort = aboutPanel.getPreference(Cn.SubmarineRightLightRelayPort);
		rightLightPanel = new LightPanel (Cn.SubmarineRightLightName, rightLightPort, rightLightRelayPort);
		this.add(rightLightPanel);
		
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
		JPanel tsd = new JPanel(layout);
		MyRectGauge tg = new MyRectGauge("temp");
		tsd.add(tg);
		MyRectGauge sg = new MyRectGauge("salinity");
		tsd.add(sg);
		MyRectGauge dg = new MyRectGauge("depth");
		tsd.add(dg);
		this.add(tsd);
		
		
	}
	
	class MyRectGauge extends JPanel {
		private static final long serialVersionUID = 1L;
		private JLabel label;
		private final DisplayRectangular dr = new DisplayRectangular();

		public MyRectGauge (String title) {
			Dimension fullSize = new Dimension (86, 52);
			Dimension drSize = new Dimension (42, 24);
			Dimension labelSize = new Dimension (42, 25);
			this.setBackground(Cn.bg);
		
			label = new JLabel(title);
			label.setForeground(Cn.fg);
			label.setPreferredSize(labelSize);
			VerticalFlowLayout layout = new VerticalFlowLayout(FlowLayout.LEADING);
			layout.setVerticalFill(false);
			layout.setVgap(0);
			this.setLayout(layout);
			this.setPreferredSize(fullSize);
			dr.setBorder(null);
			dr.setFrameVisible(false);
			dr.setPreferredSize(drSize);
			this.add(label);
			this.add(dr);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(this.advButton)) {
			if (videoOptionsFrame == null) {
				videoOptionsFrame = new JFrame();
				videoOptionsFrame.setTitle("Video Options");
				videoOptionsFrame.getContentPane().setLayout(new BorderLayout());
				videoOptionsFrame.getContentPane().add(vop, BorderLayout.CENTER);
				videoOptionsFrame.pack();
				videoOptionsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				app.showOnCenter(videoOptionsFrame);
			}
			videoOptionsFrame.setVisible(true);
		}
		else if (ae.getSource().equals(tempHumidityButton)) {
			if (tempHumidityFrame == null) {
				tempHumidityFrame = new TempHumidityFrame();
			}
			tempHumidityFrame.setVisible(true);
		}
		else if (ae.getSource().equals(browserButton)) {
			try {
				String videoURL = app.aboutPanel.getPreference(Cn.VideoURL);
				URI uri = new URI(videoURL);
				Desktop.getDesktop().browse(uri);
			} catch (Exception ex) {}
		}
	}
	
	public void shutdown () {
		this.rotatePanel.shutdown();
		this.leftLightPanel.shutdown();
		this.rightLightPanel.shutdown();
	}
}
