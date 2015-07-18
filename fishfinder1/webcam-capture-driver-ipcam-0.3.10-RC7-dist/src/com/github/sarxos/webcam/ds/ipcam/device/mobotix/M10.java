package com.github.sarxos.webcam.ds.ipcam.device.mobotix;

import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.ipcam.IpCamDevice;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;


/**
 * Speed Dome X104S IP Camera by XVision.
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class M10 extends IpCamDevice {

	private static final Dimension[] SIZES = new Dimension[] {
		WebcamResolution.VGA.getSize(),
	};

	private URL base = null;

	public M10(String name, String urlBase) {
		this(name, toURL(urlBase));
	}

	public M10(String name, URL base) {
		super(name, (URL) null, IpCamMode.PUSH);
		this.base = base;
	}

	@Override
	public Dimension[] getResolutions() {
		return SIZES;
	}

	@Override
	public Dimension getResolution() {
		return SIZES[0];
	}

	@Override
	public URL getURL() {

		// http://80.122.26.250:7000/cgi-bin/faststream.jpg?stream=full&fps=1&rand=142755

		String url = String.format("%s/cgi-bin/faststream.jpg?stream=full&fps=1&rand=%d", base, System.currentTimeMillis());

		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new WebcamException(String.format("Incorrect URL %s", url), e);
		}
	}

	public static void main(String[] args) throws MalformedURLException {

		// System.setProperty(IpCamHttpClient.PROXY_HOST_KEY,
		// "global.proxy.lucent.com");
		// System.setProperty(IpCamHttpClient.PROXY_PORT_KEY, "8000");

		IpCamDeviceRegistry.register(new M10("MOBOTIX AG M10", "http://80.122.26.250:7000"));
		Webcam.setDriver(new IpCamDriver());

		WebcamPanel panel = new WebcamPanel(Webcam.getDefault(), false);

		JFrame f = new JFrame("Mobotix Demo");
		f.add(panel);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel.start();

	}
}
