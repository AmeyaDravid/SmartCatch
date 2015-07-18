package smartcatch;

import javax.swing.*;

/**
 * <b>Programm:</b> WaveGradient<br>
 * <b>Copyright:</b> 2002 Andreas Gohr, Frank Schubert<br>
 * <b>License:</b> GPL2 or higher<br>
 * <br>
 * <b>Info:</b> This JSlider uses doubles for its values
 */
public class DoubleJSlider extends JSlider{
    private final double step;

  /**
   * Constructor - initializes with 0.0,100.0,50.0
   */
  public DoubleJSlider(){
    super();
    setDoubleMinimum(0.0);
    setDoubleMaximum(1.0);
    setDoubleValue(0.0);
    step=0.01;
  }
  
	public DoubleJSlider(double min, double max) {
		setDoubleMinimum(min);
		setDoubleMaximum(max);
		setDoubleValue(0.0);
		step = 0.01;
	}

  public DoubleJSlider(double min, double max, double val,double step){
    super();
    this.step=step;
    setDoubleMinimum(min);
    setDoubleMaximum(max);
    setDoubleValue(val);
  }

  public double getDoubleMaximum() {
    return( getMaximum()*step );
  }

  public double getDoubleMinimum() {
    return( getMinimum()*step );
  }

  public double getDoubleValue() {
	  int val = getValue();
	  return( val*step );
  }

  public void setDoubleMaximum(double max) {
    setMaximum((int)(max/step));
  }

  public void setDoubleMinimum(double min) {
    setMinimum((int)(min/step));
  }

  public void setDoubleValue(double val) {
    setValue((int)(val/step));
    setToolTipText(Double.toString(val));
  }

}