package smartcatch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;

public class LightRadioPanel extends RadioPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	DecimalFormat df = new DecimalFormat("000");
	int last = 0;
	private String analogPort; // arduino analog port i.e. "A3", "A4".
	private String digitalPort;
	private SmartCatch app = smartcatch.SmartCatch.app;
	private LightPanel parent;

	public LightRadioPanel (LightPanel parent, String name, String analogPort, String digitalPort) {
		super(name, "off", "medium", "high", null);
		this.parent = parent;
		this.analogPort = analogPort;
		this.digitalPort = digitalPort;
	  }
	
	public void init() {
		String name = getName();
		// mg911 
	}
	
	public void setValue(int value) {
		int medium = Integer.valueOf(app.aboutPanel.getPreference(Cn.SubmarineLightMedium));
		int high = Integer.valueOf(app.aboutPanel.getPreference(Cn.SubmarineLightHigh));
		String name = getName();
		if (value <= 0) {
			this.update(name + "=" + this.firstString);
		}
		else if (value < medium) {
			this.update(name + "=" + this.secondString);
		}
		else {
			this.update(name + "=" + this.thirdString);
		}
		String on = "0";
		if (value > 0)
			on = "1";
		if (app.arduino != null)
			app.arduino.send("dw(" + digitalPort + ", " + on + ")");
	}
	
	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();
		if (command.equals(this.firstString)) {
			if (app.arduino != null)
				app.arduino.send("dw(" + digitalPort + ",000)");
			parent.updateSlider(command);
		}
		else {
			if (app.arduino != null)
				app.arduino.send("dw(" + digitalPort + ",001)");
			String valStr = "";
			if (command.equals(this.secondString)) {
				valStr = app.aboutPanel.getPreference(Cn.SubmarineLightMedium);
				parent.updateSlider(command);
			}
			else if (command.equals(this.thirdString)) {
				valStr = app.aboutPanel.getPreference(Cn.SubmarineLightHigh);
				parent.updateSlider(command);
			}
			if (app.arduino != null)
				app.arduino.send("aw(" + analogPort + ", " + valStr + ")");
		}
		update(this.getName() + " = " + command);
	}

}