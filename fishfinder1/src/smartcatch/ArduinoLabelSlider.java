package smartcatch;

import java.io.IOException;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ArduinoLabelSlider extends LabelSlider implements ChangeListener {
	private static final long serialVersionUID = 6715551239241789463L;
	private SmartCatch app = SmartCatch.app;
	private String command;
	private String port;

	public ArduinoLabelSlider(String name, String command, String port, int min, int max, int tickspacing) {
		super(name, min, max, tickspacing);
		this.command = command;
		this.port = port;
	}

	public void stateChanged(ChangeEvent ie) {
		JSlider slider = (JSlider) ie.getSource();
		int val = slider.getValue();
		String name = label.getName();
		String value = df.format(val);
		label.setText(name + "   " + value);
		repaint();
		// if (!slider.getValueIsAdjusting()) 
		{
			String msg;
			if (command != null) {
				msg = command + "(" + port + "," + value + ")";
		    }
			else {
				msg = port + "=" + value;
			}
			if (app.arduino != null)
				app.arduino.send(msg);
		}
	}
	
	public void init () {
		// String name = label.getName(); 
		// retrieve the value?
	}		
}
