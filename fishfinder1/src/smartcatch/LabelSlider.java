package smartcatch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;  
import java.io.IOException;

import javax.swing.*;  
import javax.swing.plaf.*;  
import javax.swing.plaf.metal.MetalSliderUI;  
  

public class LabelSlider extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 4287348712078901555L;
	public JSlider slider;
	public JLabel label;
	DecimalFormat df = new DecimalFormat("000");

	public LabelSlider(String name, int min, int max, int tickSpacing) {
		this.setBackground(Cn.bg);
	    Dimension labelDim = new Dimension (115, 25);
	    Dimension sliderDim = new Dimension (140, 25);

		slider = new JSlider(min, max);
		slider.setBackground(Cn.bg);
		UIManager.put("Slider.tickColor", Color.cyan);
	    UIManager.put("Slider.isFilled", Boolean.TRUE);
	    slider.setPaintTicks(true);
	    slider.setMajorTickSpacing(tickSpacing);
	    slider.setPreferredSize(sliderDim);
		slider.addChangeListener(this);
		try {
		slider.setUI(new MySliderUI(slider));
		} catch (IOException io) { }

		
		label = new JLabel(name);
		label.setName(name);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setBackground(Cn.bg);
		label.setPreferredSize(labelDim);
		label.setForeground(Cn.fg);
		
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
		this.setLayout(layout);
		
		this.add(label);
		this.add(slider);
	}
	
	public void addChangeListener(ChangeListener listener) {
		slider.addChangeListener(listener);
	}
	
	public void setValue(int value) {
		slider.setValue(value);
		label.setText(label.getName() + "   " + value);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
}