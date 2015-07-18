package smartcatch;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class convertImagesVideo {
static Date date;
	
	
	//static SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");

public static  void convert() throws IOException{
	
File directory = new File(System.getProperty("user.dir")+"/camera");
File[] files = directory.listFiles();
List<String> list = new ArrayList<String>();
date=new Date();
for(int i=0;i<files.length;i++) {
	//System.out.println("/home/darksnake/Desktop/camera/"+i+".jpg");

list.add(System.getProperty("user.dir")+"/camera/"+i+".jpg");

}
Collections.sort(list);
//int i = -1;

//change directory over here ,in the next line

DataOutputStream out = new DataOutputStream(new FileOutputStream(System.getProperty("user.dir")+"/video/"+date+".avi"));
for(int j=0;j<files.length;j++ ) {

//change directory over here ,in the next line
DataInputStream in = new DataInputStream(new FileInputStream(System.getProperty("user.dir")+"/camera/"+j+".jpg"));

while( in.available() > 0 ) {
byte data[] = new byte[ in.available()];
in.read(data);
out.write(data);
}
in.close();
}
out.close();
}
}