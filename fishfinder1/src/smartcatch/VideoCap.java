package smartcatch;



import com.github.sarxos.webcam.Webcam;


public class VideoCap {
	static Webcam webcam;
	public static int i=1;
	public static int j=0;
	static double alpha=RightPanel.showSliderDemo2();
	static boolean flag=false;

    

   

    VideoCap(){
        Webcam webcam=Webcam.getWebcams().get(0);
        webcam.open();
        
    } 
 
   /* BufferedImage getOneFrame(boolean flag) throws InterruptedException, IOException, SQLException {
    	
    	i++;
    	BufferedImage img=webcam.getImage();
        cap.read(mat2Img.mat);
        Mat image = mat2Img.mat;
        
        
        //get the data from the sensor and display on the image: temporary used database is SmartCatch
        
        Core.putText(mat2Img.mat,"salinity:"+salinity,new Point (10,40),Core.FONT_ITALIC,new Double(1),new  Scalar(255));
        Core.putText(mat2Img.mat,"temperature:"+temperature,new Point (250,40),Core.FONT_ITALIC,new Double(1),new  Scalar(255));
        //Core.putText(mat2Img.mat,"saturation" + RightPanel.showSliderDemo2(),new Point (100,140),Core.FONT_ITALIC,new Double(1),new  Scalar(255));
        //mat.get(0, 0, dat);
        {
        	if(flag==false){
        		UtilImageIO.saveImage(img, System.getProperty("user.home")+"/"+j+".jpg");
            	j++;
        	}
        	if(RightPanel.decideStartOrStop%2==1){
        		j=0;
        	}
        }
        
        
       // Color.HSBtoRGB(hue, saturation, brightness);
        return img;
    }*/
}
