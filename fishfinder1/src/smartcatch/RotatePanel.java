package smartcatch;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.hansolo.steelseries.tools.GaugeType;

public class RotatePanel extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	private SmartCatch app = smartcatch.SmartCatch.app;
	public ArduinoLabelSlider rotateSlider;
	public RadialGauge rg = new RadialGauge("angle", "deg");
	boolean notyet = false;
	
	public RotatePanel () {
		String submarineTiltPort = app.aboutPanel.getPreference(Cn.SubmarineTiltPort);
	    String submarineTiltMin = app.aboutPanel.getPreference(Cn.SubmarineTiltMin);
		String submarineTiltMax = app.aboutPanel.getPreference(Cn.SubmarineTiltMax);
		int minTilt = Integer.valueOf(submarineTiltMin).intValue();
		int maxTilt = Integer.valueOf(submarineTiltMax).intValue();
		this.setBackground (Cn.bg);
		VerticalFlowLayout layout = new VerticalFlowLayout(FlowLayout.LEADING);
		layout.setVgap(0);
		this.setLayout(layout);
		rotateSlider = new ArduinoLabelSlider("Rotate", "sh", submarineTiltPort, minTilt, maxTilt, 20); 

		rotateSlider.slider.addChangeListener(this);
		this.add(rotateSlider);
		
		if (notyet) {
			this.add(rg);
			rg.gauge.setMinValue(0);
			rg.gauge.setMaxValue(180.0);
			rg.gauge.setGaugeType(GaugeType.TYPE2);
			rg.gauge.setFrameVisible(false);
			rg.gauge.setLedVisible(false);
			rg.setBackground(Color.black);
		}
	}

	@Override
	public void stateChanged(ChangeEvent ie) {
		JSlider slider = (JSlider) ie.getSource();
		int val = slider.getValue();
		rg.gauge.setValue(val);
	}
	
	public void shutdown() {
		rotateSlider.slider.setValue(90);
	}

}
