package smartcatch;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.device.arecont.Arecont;
import com.sun.media.jai.codecimpl.JPEGCodec;
import com.sun.media.jai.codecimpl.JPEGImageEncoder;
/*
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;
*/
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.Webcam;

public class VideoPanel extends JPanel implements WebcamListener {
	private static final long serialVersionUID = 1L;
	VideoThread runner;
	public boolean faceDetectionOn = false;
	SmartCatch app = smartcatch.SmartCatch.app;
	MyWebcamPanel wcpanel;

	class VideoThread extends Thread {

		public VideoThread() {
		}

		public void run() {
			try {

				String videoURL = app.aboutPanel.getPreference(Cn.VideoURL);
				IpCamDeviceRegistry.register(new Arecont("ARECONT", videoURL));
				Webcam.setDriver(new IpCamDriver());
				wcpanel = new MyWebcamPanel(Webcam.getDefault(), false);
				wcpanel.setFPSDisplayed(true);
				wcpanel.setImageSizeDisplayed(true);
				wcpanel.setFitArea(false);
				String str = app.aboutPanel.getPreference(Cn.FPSLimit);
				if (str != null) {
					Double val = Double.valueOf(str);
					if (val != null) {
						int fpsLimit = val.intValue();
						wcpanel.setFPSLimit(fpsLimit);
					}
				}
				wcpanel.webcam.addWebcamListener(VideoPanel.this);
				add(wcpanel);
				validate();
				wcpanel.start();
			} catch (WebcamException ex) {
				JOptionPane.showMessageDialog(VideoPanel.this, "WebcamException: " + ex.getMessage());
				app.camera.isConnected = false;
				app.rightPanel.updateCameraConnected();
			}
		}
	}
	
	/* face recognition stuff
	faceDetector = new CascadeClassifier(getClass().getResource("/lbpcascade_frontalface.xml").getPath());

	if (capture.isOpened()) {
		int width = 640; // dim.width;
		int height = 480; // dim.height;
		capture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, width);
		capture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, height);

		while (true) {
			capture.read(webcam_image);
			if (!webcam_image.empty()) {
				if (faceDetectionOn) {
					MatOfRect faceDetections = new MatOfRect();
					faceDetector.detectMultiScale(webcam_image,
							faceDetections);

					System.out.println(String.format("Detected %s faces",
							faceDetections.toArray().length));

					// Draw a bounding box around each face.
					for (Rect rect : faceDetections.toArray()) {
						Core.rectangle(webcam_image, new Point(rect.x,
								rect.y), new Point(rect.x + rect.width,
								rect.y + rect.height),
								new Scalar(0, 255, 0));
					}
				}

				temp = matToBufferedImage(webcam_image);
				panel.setimage(temp);
				panel.repaint();
			} else {
				System.out
						.println(" --(!) No captured frame -- Break!");
				break;
			}
			if (requestStop) {
				break;
			}
		}
		capture.release();
	}
	else {
		String str = "Could not open video from: " + videoStr;
		JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.ERROR_MESSAGE);
	}
	*/
	
	/*
	public static Mat image2Mat(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		Mat out = new Mat(width, height, CvType.CV_8UC1);
		DataBufferByte buf = (DataBufferByte)image.getRaster().getDataBuffer();
		byte[] pixels =  buf.getData();

		out.put(0, 0, pixels);
		return (out);
	}
	*/
	
	/*
	public static Mat img2Mat(BufferedImage in)
    {
          Mat out;
          byte[] data;
          int r, g, b;
          int width = in.getWidth();
          int height = in.getHeight();

          if(in.getType() == BufferedImage.TYPE_INT_RGB)
          {
              out = new Mat(width, height, CvType.CV_8UC3);
              data = new byte[height * width * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, height, width, null, 0, height);
              for(int i = 0; i < dataBuff.length; i++)
              {
                  data[i*3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                  data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                  data[i*3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
              }
          }
          else
          {
              out = new Mat(width, height, CvType.CV_8UC1);
              data = new byte[height * width * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, height, width, null, 0, height);
              for(int i = 0; i < dataBuff.length; i++)
              {
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i] = (byte)((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
              }
           }
           out.put(0, 0, data);
           return out;
     } 
     */

	/**
	 * Converts/writes a Mat into a BufferedImage.
	 * 
	 * @param matrix
	 *            Mat of type CV_8UC3 or CV_8UC1
	 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
	 */
	/*
	public static BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int) matrix.elemSize();
		byte[] data = new byte[cols * rows * elemSize];
		int type;
		matrix.get(0, 0, data);
		switch (matrix.channels()) {
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;
			// bgr to rgb
			byte b;
			for (int i = 0; i < data.length; i = i + 3) {
				b = data[i];
				data[i] = data[i + 2];
				data[i + 2] = b;
			}
			break;
		default:
			return null;
		}
		BufferedImage image2 = new BufferedImage(cols, rows, type);
		image2.getRaster().setDataElements(0, 0, cols, rows, data);
		return image2;
	}
	*/
	

	
	public boolean isPlaying() {
		if (runner != null) {
			return true;		}
		return false;
	}

	public void stop() {
		if (runner != null) {
			wcpanel.stop();
		}
		runner = null;
	}
	
	public void start() {
		if (runner == null) {
			runner = new VideoThread();
		}
		runner.start();
	}

	public VideoPanel () {
		this.setLayout(new BorderLayout());
		// start();
	}
	
	static int idx = 0;
	
	class MyWebcamPanel extends WebcamPanel {
		private static final long serialVersionUID = 1L;
		private Webcam webcam;
		MyPainter painter = new MyPainter();
		Painter defaultPainter = this.getDefaultPainter();
		
		public MyWebcamPanel(Webcam webcam, boolean start) {
			super (webcam, null, start);
			this.webcam = webcam;
		}
		
		private boolean separateImages = false;
		File mjpeg;
		JPEGImageEncoder n_code;
		FileOutputStream fos;
		
		private void saveImage(BufferedImage bi) {
			if (bi != null) {
				if (separateImages) {
					try {
					    File outputfile = new File("images/im" + idx++ + ".png");
					    ImageIO.write(bi, "png", outputfile);
					} catch (IOException e) {
						System.out.println("IOException: " + e.getMessage());
					}
				}
				else { 
					try {
						if (idx == 0) {
							mjpeg = new File("im.mjpeg");
							mjpeg.createNewFile();
		                    fos = new FileOutputStream( mjpeg ); 
		                    ImageIO.write(bi, "jpeg", fos);
		                    //n_code = JPEGCodec.createJPEGEncoder( fos );  
						}
		                //n_code.encode( bi );  
		                fos.flush();  
					}
                    catch ( IOException x ) {  
                    	System.out.println ("IOException: " + x.getMessage());
                    }
                }  
			}
		}

		/*
		public void paintComponent(Graphics g) {
			BufferedImage tempImage = webcam.getImage();
			saveImage(tempImage);
			if (tempImage != null) {
				if (faceDetectionOn) {
					setPainter(painter);
					MatOfRect faceDetections = new MatOfRect();
					Mat mat = image2Mat (tempImage);
					faceDetector.detectMultiScale(mat, faceDetections);
					painter.setFaceDetections(faceDetections);
				}
				else {
					setPainter(defaultPainter);
				}
			}
			super.paintComponent(g);
		}
	    */
	}

	@Override
	public void webcamOpen(WebcamEvent we) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void webcamClosed(WebcamEvent we) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void webcamDisposed(WebcamEvent we) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void webcamImageObtained(WebcamEvent we) {
		BufferedImage image = we.getImage();
		// TODO Auto-generated method stub
		
	}
}

