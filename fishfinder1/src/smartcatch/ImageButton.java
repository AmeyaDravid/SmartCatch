package smartcatch;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

public class ImageButton extends JButton {
	private SmartCatch app = smartcatch.SmartCatch.app;
	private static final long serialVersionUID = 1L;
	private ImageIcon selIcon;
	private ImageIcon offIcon;
	// private ImageIcon pressedIcon;

	public ImageButton(String name, ActionListener al) {
		this.setBorderPainted(false);
		this.setBackground(Color.black);
		selIcon = app.createImageIcon("img/" + name + "-sel.png", name);
		offIcon = app.createImageIcon("img/" + name + "-off.png", name);
		// pressedIcon = app.createImageIcon("img/" + name + "-press.png", name);
		this.setIcon(offIcon);
		this.setSelectedIcon(selIcon);
		Dimension dim = new Dimension(offIcon.getIconWidth() + 6, offIcon.getIconHeight());
		this.setPreferredSize(dim);
		// this.setPressedIcon(pressedIcon);
		this.addActionListener(al);
	}
}
