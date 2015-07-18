package smartcatch;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class LightPanel extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	private LabelSlider lslider;
	private DecimalFormat df = new DecimalFormat("000");
	int last = 0;
	private SmartCatch app = smartcatch.SmartCatch.app;
	private Arduino arduino = app.arduino;
	private String analogPort; // arduino analog port i.e. "022", "023".
	private String digitalPort; // arduino digital port i.e. "020", "021".
	private LightRadioPanel rp;
	
	public LightPanel(String str, String analogPort, String digitalPort) {
		this.analogPort = analogPort;
		this.digitalPort = digitalPort;
		this.setBackground(Cn.bg);
		VerticalFlowLayout layout = new VerticalFlowLayout(FlowLayout.LEADING);
		layout.setVgap(0);
		this.setLayout(new VerticalFlowLayout());

		lslider = new ArduinoLabelSlider(str, "aw", analogPort, 0, 255, 20);
		// lslider.setBackground(new Color(0xdbdbdb));
		lslider.slider.addChangeListener(this);
		lslider.setValue(last);
		this.add(lslider);
		rp = new LightRadioPanel(this, str, analogPort, digitalPort);
		this.add(rp);
		
		if (arduino != null) {
			arduino.send("pinmode(" + digitalPort + ", 1)");
		}
	}
	
	public void updateSlider(String str) {
		String tmp = "0";
		int val = 0;
		if (str.equals("off")) {
			tmp = df.format(val);
			lslider.setValue(val);
		}
		else if (str.equals("medium")) {
			val = Integer.valueOf(app.aboutPanel.getPreference(Cn.SubmarineLightMedium)).intValue();
			tmp = df.format(val);
			lslider.setValue(val);
		}
		else if (str.equals("high")) {
			val = Integer.valueOf(app.aboutPanel.getPreference(Cn.SubmarineLightHigh)).intValue();
			tmp = df.format(val);
			lslider.setValue(val);
		}
		if (arduino != null) {
			String dstr = (val > 0) ? "1" : "0";
			arduino.send("dw(" + digitalPort + ", " + dstr  +")");
			arduino.send("aw(" + analogPort + ", " + tmp + ")");
		}
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		int val = (int) source.getValue();
		lslider.setValue(val);
		if (arduino != null) {
			String str = df.format(val);
			arduino.send("aw(" + analogPort + ", " + str + ")");
		}
		// turn on the radio buttons if necessary
		if (rp != null)
			rp.setValue(val);
	}
	
	public void shutdown() {
		updateSlider("off");
	}
	

}