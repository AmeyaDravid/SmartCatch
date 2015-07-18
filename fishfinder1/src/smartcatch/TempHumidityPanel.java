package smartcatch;

import java.awt.GridLayout;
import java.awt.Color;

import javax.swing.JPanel;

public class TempHumidityPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private SmartCatch app = smartcatch.SmartCatch.app;
	public MultiGauge leftTemp = new MultiGauge("Left Temp", "F");
	public MultiGauge rightTemp = new MultiGauge("Right Temp", "F");
	public MultiGauge leftHumid = new MultiGauge("Left Humidity", "%");
	public MultiGauge rightHumid = new MultiGauge("Right Humidity", "%");

	public TempHumidityPanel () {
		setLayout(new GridLayout(2,2));
		// this.setBackground(Cn.bg);
		this.setBackground(Color.black);
		this.add(leftTemp);
		this.add(rightTemp);
		this.add(leftHumid);
		this.add(rightHumid);
	}
	
	public void shutdown() {
		leftTemp.shutdown();
		rightTemp.shutdown();
		leftHumid.shutdown();
		rightHumid.shutdown();
	}
}

