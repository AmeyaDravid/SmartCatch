package smartcatch;

import java.awt.Color;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DoubleLabelSlider extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	public DoubleJSlider slider;
	public JLabel label;
	Color bg = new Color(0x262626);
	DecimalFormat df = new DecimalFormat("0.0");

	public DoubleLabelSlider(String name, double min, double max) {
		this.setBackground(bg);
		slider = new DoubleJSlider(min, max);
		slider.setBackground(Color.gray);
		label = new JLabel(name);
		label.setName(name);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBackground(Color.gray);
		this.setLayout(new VerticalFlowLayout());
		this.add(slider);
		slider.addChangeListener(this);
		this.add(label);
		label.setForeground(Color.white);
	}

	public void setDoubleValue(double value) {
		slider.setDoubleValue(value);
		String str = df.format(value);
		label.setText(label.getName() + "   " + str);
	}

	public void stateChanged(ChangeEvent e) {
	}
}
