package smartcatch;

import javax.swing.JPanel;

public class VideoOptionsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private SmartCatch app = smartcatch.SmartCatch.app;
	public CameraLabelSlider blueSlider = new CameraLabelSlider("Blue", -10, 10, 2);
	public CameraLabelSlider redSlider = new CameraLabelSlider("Red", -10, 10, 2);


	public VideoOptionsPanel () {
		setLayout(new VerticalFlowLayout());
		this.setBackground(Cn.bg);
		this.add(blueSlider);
		blueSlider.init();
		this.add(redSlider);
		redSlider.init();
	}
}
