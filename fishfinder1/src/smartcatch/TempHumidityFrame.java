package smartcatch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class TempHumidityFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private SmartCatch app = smartcatch.SmartCatch.app;
	private TempHumidityPanel thp = new TempHumidityPanel();
	
	public TempHumidityFrame () {
		this.setUndecorated(true);
		 this.setOpacity(0.35f);
		 this.setBackground(Color.gray);
		setTitle("Temp & Humidity");
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(thp, BorderLayout.CENTER);
		this.pack();
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				thp.shutdown();
				app.rightPanel.tempHumidityFrame = null;
				dispose();
			}
		});
		app.showOnCenter(this);
	}
}
