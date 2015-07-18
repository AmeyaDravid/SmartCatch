package smartcatch;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.JPanel;

class RadioPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	String firstString;
	String secondString;
	String thirdString;
	String fourthString;
	ImageButton firstButton;
	ImageButton secondButton;
	ImageButton thirdButton;
	ImageButton fourthButton;
	private SmartCatch app = smartcatch.SmartCatch.app;
	
	public RadioPanel (String name, String first, String second, String third, String fourth) {
		this.setName(name);
		this.setBackground(Cn.bg);
		firstString = first;
		secondString = second;
		thirdString = third;
		fourthString = fourth;
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0); 
	    this.setLayout(layout);
	    layout.setHgap(0);
	    layout.setVgap(0);
		
		if (first != null) {
		    firstButton = new ImageButton(first, this);
		    firstButton.setActionCommand(first);
		    firstButton.setSelected(true);
		    this.add(firstButton);
		}

		if (second != null) {
		    secondButton = new ImageButton(second, this);
		    secondButton.setActionCommand(second);
		    this.add(secondButton);
		}

		if (third != null) {
		    thirdButton = new ImageButton(third, this);
		    thirdButton.setActionCommand(third);
		    this.add(thirdButton);
		}
		if (fourth != null) {
		    fourthButton = new ImageButton(fourth, this);
		    fourthButton.setActionCommand(fourth);
		    this.add(fourthButton);
		}
	}
		
	public void update(String message) {
		String ret = message;
		if (ret != null) {
			StringTokenizer st = new StringTokenizer(ret, " =");
			String nm = st.nextToken();
			String val = st.nextToken();
			if (firstButton != null)
				firstButton.setSelected(false);
			if (secondButton != null)
				secondButton.setSelected(false);
			if (thirdButton != null)
				thirdButton.setSelected(false);
			if (fourthButton != null)
				fourthButton.setSelected(false);

			if (val.equalsIgnoreCase (firstString))
				firstButton.setSelected(true);
			else if (val.equalsIgnoreCase(secondString))
				secondButton.setSelected(true);
			else if (val.equalsIgnoreCase(thirdString))
				thirdButton.setSelected(true);
			else if (val.equalsIgnoreCase(fourthString))
				fourthButton.setSelected(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {		

	}
	
	
}