package smartcatch;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Random;

import eu.hansolo.steelseries.gauges.DisplayRectangular;
import eu.hansolo.steelseries.gauges.Linear;
import eu.hansolo.steelseries.gauges.Radial;

public class MultiGauge extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	final Linear linear = new Linear();
	final Radial radial = new Radial();
	final DisplayRectangular rectangular = new DisplayRectangular();
	private SmartCatch app = smartcatch.SmartCatch.app;
	
	private final int LINEAR = 0;
	private final int RADIAL = 1;
	private final int RECTANGULAR = 2;
	private int gaugeType = -1;
	Thread anim;
	boolean done = false;
	String style = app.aboutPanel.getPreference(Cn.TempHumidityStyle);
	
	public MultiGauge(String title, String units) {
		this.setLayout(new BorderLayout());

		setType(style);
		switch (gaugeType) {
		case LINEAR:
			linear.setTitle(title);
			linear.setUnitString(units);
			break;
		case RADIAL:
			radial.setTitle(title);
			radial.setUnitString(units);
			break;
		case RECTANGULAR:
			rectangular.setTitle(title);
			rectangular.setUnitString(units);
			rectangular.setFrameVisible(false);
			rectangular.setInitialized(true);
			rectangular.reInitialize();
			break;
		}
		anim = new Thread(this, title + "-animation");
		anim.start();
	}
	
	public void shutdown() {
		done = true;
	}

	public void setValue(double value) {
		switch (gaugeType) {
			case LINEAR:linear.setValueAnimated(value);break;
			case RADIAL:radial.setValueAnimated(value);break;
			case RECTANGULAR:rectangular.setValueAnimated(value);break;
		}
	}

	public void setValue(String str) {
		double value = 0.0;
		try {
			value = Double.valueOf(str);
		} catch (NumberFormatException ex) {
			// TODO - handle invalid input
			System.err.println("invalid input");
		}
		switch (gaugeType) {
			case LINEAR: linear.setValueAnimated(value); break;
			case RADIAL: radial.setValueAnimated(value); break;
			case RECTANGULAR: rectangular.setValueAnimated(value); break;
		}
	}
	
	public void setType(String type) {
		if (type.equals("linear")) {
			this.removeAll();
			this.add(linear, BorderLayout.CENTER);
			this.gaugeType = this.LINEAR; 
		}
		else if (type.equals("radial")) {
			this.removeAll();
			this.add(radial, BorderLayout.CENTER);
			this.gaugeType = this.RADIAL;
		}
		else if (type.equals("rectangular")) {
			this.removeAll();
			this.add(rectangular, BorderLayout.CENTER);
			this.gaugeType = this.RECTANGULAR;
		}
	}

	public Dimension getPreferredSize() {
		if (style.equals("linear"))
			return new Dimension(255, 40);
		else if (style.equals("rectangular"))
			return (new Dimension (255, 25));
		return new Dimension(250, 250);
	}
	
	protected static Random random = new Random();
	public static double randomInRange(double min, double max) {
	  double range = max - min;
	  double scaled = random.nextDouble() * range;
	  double shifted = scaled + min;
	  return shifted; // == (rand.nextDouble() * (max-min)) + min;
	}
	
	@Override
	public void run() {
		while (!done) {
			double min;
			double max;
			double value = 0;
			double inc = 0;
			switch (gaugeType) {
			case LINEAR:
				 min = linear.getMinValue();
				 max = linear.getMaxValue();
				 inc = randomInRange(-2, 2);
				 value = linear.getValue();
				 inc = randomInRange(-2, 2);
			     if ((value + inc > max) || (value + inc < min))
			    	 inc = -inc;
				linear.setValueAnimated(value + inc);
				break;
			case RADIAL:
				 min = radial.getMinValue();
				 max = radial.getMaxValue();
				 value = radial.getValue();
				 inc = randomInRange(-2, 2);
			     if ((value + inc > max) || (value + inc < min))
			    	 inc = -inc;
				radial.setValueAnimated(value + inc);
				break;
			case RECTANGULAR:
				 min = rectangular.getMinValue();
				 max = rectangular.getMaxValue();
				 value = rectangular.getValue();
				 inc = randomInRange(-2, 2);
				 inc = randomInRange(-2, 2);
			     if ((value + inc > max) || (value + inc < min))
			    	 inc = -inc;
				rectangular.setValueAnimated(value + inc);
				break;
			}
			try {
				Thread.sleep((int)randomInRange (1000, 3000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
