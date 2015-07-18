package smartcatch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class MySliderUI extends BasicSliderUI {

	public MySliderUI(JSlider slider) throws IOException {
		super(slider);
	}

	@Override
	public void paintTrack(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Rectangle t = trackRect;
		Rectangle w = thumbRect;
		g2d.setColor(Cn.trackColor);
		g2d.fillRect(t.x, t.y, w.x - t.x, t.height);
		g2d.setColor(Cn.bg);
		g2d.fillRect(w.x + w.width, t.y, t.width - (w.x + w.width), t.height);
	}
	
	@Override
	public void paintThumb(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Rectangle w = thumbRect;
		g2d.setColor(Color.blue);
		g2d.fillRect(w.x, w.y, w.width, w.height);
	}
}
