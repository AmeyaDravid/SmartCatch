package smartcatch;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
//import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_OFF;
//import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
//import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;
import static java.awt.RenderingHints.VALUE_RENDER_SPEED;
import java.awt.AlphaComposite;
//import java.awt.BasicStroke;
import java.awt.Color;
//import java.awt.Dimension;
import java.awt.FontMetrics;
//import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
//import java.awt.RenderingHints;
import java.awt.image.BufferedImage;



/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
*/
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPanel.DrawMode;
import com.github.sarxos.webcam.WebcamPanel.Painter;

public class MyPainter implements Painter {
	/**
	 * Webcam device name.
	 */
	private String name = null;
	/**
	 * Lat repaint time, uset for debug purpose.
	 */
	private long lastRepaintTime = -1;
	
	/**
	* Is antialiasing enabled (true by default).
	*/
	private boolean antialiasingEnabled = true;

	
	/**
	* @return True is antialiasing is enabled, false otherwise
	*/
	public boolean isAntialiasingEnabled() {
		return antialiasingEnabled;
	}
	
	/**
	 * Buffered image resized to fit into panel drawing area.
	 */
	private BufferedImage resizedImage = null;
	
	//MatOfRect faceDetections;
	
	/*public void setFaceDetections(MatOfRect detect) {
		this.faceDetections = detect;
	}*/

	
	@Override
	public void paintPanel(WebcamPanel owner, Graphics2D g2) {
	}
	
	@Override
	public void paintImage(WebcamPanel owner, BufferedImage image, Graphics2D g2) {
		assert owner != null;
		assert image != null;
		assert g2 != null;
		
		int pw = owner.getWidth();
		int ph = owner.getHeight();
		int iw = image.getWidth();
		int ih = image.getHeight();
		Object antialiasing = g2.getRenderingHint(KEY_ANTIALIASING);
		Object rendering = g2.getRenderingHint(KEY_RENDERING);
		g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(KEY_RENDERING, VALUE_RENDER_SPEED);
		g2.setBackground(Color.BLACK);
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, pw, ph);
		//resized image position and size
		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;
		DrawMode drawMode = DrawMode.FIT;
		switch (drawMode) {
		case NONE:
			w = image.getWidth();
			h = image.getHeight();
			break;
		case FILL:
			w = pw;
			h = ph;
			break;
		case FIT:
			double s = Math.max((double) iw / pw, (double) ih / ph);
			double niw = iw / s;
			double nih = ih / s;
			double dx = (pw - niw) / 2;
			double dy = (ph - nih) / 2;
			w = (int) niw;
			h = (int) nih;
			x = (int) dx;
			y = (int) dy;
			break;
		}
		if (resizedImage != null) {
			resizedImage.flush();
		}
		if (w == image.getWidth() && h == image.getHeight() && !owner.isMirrored()) {
			resizedImage = image;
		} else {
			GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsConfiguration gc = genv.getDefaultScreenDevice().getDefaultConfiguration();
			Graphics2D gr = null;
			try {
				resizedImage = gc.createCompatibleImage(pw, ph);
				gr = resizedImage.createGraphics();
				gr.setComposite(AlphaComposite.Src);
				/*
				for (Map.Entry<RenderingHints.Key, Object> hint : imageRenderingHints.entrySet()) {
					gr.setRenderingHint(hint.getKey(), hint.getValue());
				}
				*/
				gr.setBackground(Color.BLACK);
				gr.setColor(Color.BLACK);
				gr.fillRect(0, 0, pw, ph);
				int sx1, sx2, sy1, sy2; // source rectangle coordinates
				int dx1, dx2, dy1, dy2; // destination rectangle coordinates
				dx1 = x;
				dy1 = y;
				dx2 = x + w;
				dy2 = y + h;
				if (owner.isMirrored()) {
					sx1 = iw;
					sy1 = 0;
					sx2 = 0;
					sy2 = ih;
				} else {
					sx1 = 0;
					sy1 = 0;
					sx2 = iw;
					sy2 = ih;
				}
				gr.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
				/*if (faceDetections != null) {
					int length = faceDetections.toArray().length;
					if (length > 0) {
						System.out.println("Detected " + length + " faces");
						// Draw a bounding box around each face.
						for (Rect rect : faceDetections.toArray()) {
							gr.drawRect(rect.x, rect.y, rect.width, rect.height);
						}
					}
				}	*/
			} finally {
				if (gr != null) {
					gr.dispose();
				}
			}
		}
		g2.drawImage(resizedImage, 0, 0, null);
		/*
		if (owner.isFPSDisplayed()) {
			String str = String.format("FPS: %.1f", webcam.getFPS());
			int sx = 5;
			int sy = ph - 5;
			// g2.setFont(getFont());
			g2.setColor(Color.BLACK);
			g2.drawString(str, sx + 1, sy + 1);
			g2.setColor(Color.WHITE);
			g2.drawString(str, sx, sy);
		}
		*/
		if (owner.isImageSizeDisplayed()) {
			String res = String.format("%d\u2A2F%d px", iw, ih);
			FontMetrics metrics = g2.getFontMetrics(g2.getFont());
			int sw = metrics.stringWidth(res);
			int sx = pw - sw - 5;
			int sy = ph - 5;
			// g2.setFont(getFont());
			g2.setColor(Color.BLACK);
			g2.drawString(res, sx + 1, sy + 1);
			g2.setColor(Color.WHITE);
			g2.drawString(res, sx, sy);
		}
		if (owner.isDisplayDebugInfo()) {
			if (lastRepaintTime < 0) {
				lastRepaintTime = System.currentTimeMillis();
			} else {
				long now = System.currentTimeMillis();
				String res = String.format("DEBUG: repaints per second: %.1f", (double) 1000 / (now - lastRepaintTime));
				lastRepaintTime = now;
				// g2.setFont(getFont());
				g2.setColor(Color.BLACK);
				g2.drawString(res, 6, 16);
				g2.setColor(Color.WHITE);
				g2.drawString(res, 5, 15);
			}
		}
		g2.setRenderingHint(KEY_ANTIALIASING, antialiasing);
		g2.setRenderingHint(KEY_RENDERING, rendering);
	}
}
