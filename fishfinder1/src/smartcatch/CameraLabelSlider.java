package smartcatch;

import javax.swing.JSlider;
import java.awt.Color;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class CameraLabelSlider extends LabelSlider implements ChangeListener {
	private static final long serialVersionUID = 6715551239241789463L;
	private SmartCatch app = smartcatch.SmartCatch.app;

	public CameraLabelSlider(String name, int min, int max, int tickSpacing) {
		super(name, min, max, tickSpacing);
		// this.setBackground(Color.pink);
	}

	public void stateChanged(ChangeEvent ie) {
		JSlider slider = (JSlider) ie.getSource();
		int val = slider.getValue();
		String name = label.getName();
		String str = df.format(val);
		label.setText(name + "   " + str);
		repaint();
		if (!slider.getValueIsAdjusting())
			app.camera.setValue(label.getName(), str);
	}
	
	public void init () {
		String name = label.getName();
		app.camera.getValue(name);
	}		
}

