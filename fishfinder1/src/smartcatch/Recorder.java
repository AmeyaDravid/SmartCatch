package smartcatch;



import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;





public class Recorder {
	static Date date=new Date();
	
	
	//static SimpleDateFormat ft = new SimpleDateFormat ();
	   
	
	public Recorder(){
		
		
	}
	public static boolean recorderFlag=false;
	static JFrame recorderFrame=new JFrame();
	static JPanel recorderPanel=new JPanel();
	JLabel recorderLabel =new JLabel();
	static JFileChooser recordedVideo=new JFileChooser();
	
	
	
	public static int videoCounter=0;
	
	public static void deleteFiles() throws IOException, InterruptedException, SQLException{
			videoCounter++;
		
		   int numberOfFiles=new File(System.getProperty("user.dir")+"/camera").listFiles().length;
		   
		   //code for conversion of frames-individual images to video
		   convertImagesVideo.convert();	
			//deletion of all individual images
		   
			for(int i =0;i<=numberOfFiles;i++){
				File file=new File(System.getProperty("user.dir")+"/camera/"+i+".jpg");
				if(file.isFile()){
					file.delete();
				}
				else{
					
					break;
				}
				
			}
		new Recorder();
		
		
		
	}
	

}
