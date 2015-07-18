package smartcatch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

import eu.hansolo.steelseries.gauges.Radial;

public class RadialGauge extends JPanel {
	private static final long serialVersionUID = 1L;
	public final Radial gauge = new Radial();

	public RadialGauge(String title, String units) {
		gauge.setTitle(title);
		gauge.setUnitString(units);
		this.setLayout(new BorderLayout());
		this.add(gauge, BorderLayout.CENTER);
	}
	
	public void setValue (double value) {
		gauge.setValueAnimated (value);
	}
	
	public void setValue(String str) {
		double value = 0.0;
		try {
			value = Double.valueOf(str);
		} catch (NumberFormatException ex) {
			// TODO - handle invalid input
			System.err.println("invalid input");
		}
		gauge.setValueAnimated(value);
	}

	public Dimension getPreferredSize() {
		return new Dimension(250, 250);
	}
}
