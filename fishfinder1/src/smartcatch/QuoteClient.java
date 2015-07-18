package smartcatch;

import java.io.*;
import java.net.*;

public class QuoteClient {
    public static void main(String[] args) throws IOException,UnknownHostException,SecurityException 
    {
    	DatagramPacket mDatagramPacket =null;
    	DatagramSocket mDatagramSocket = null;
		InetAddress client_adress = null;
        try {
            client_adress = InetAddress.getByName("10.0.0.13");
        } catch (UnknownHostException e) {
        	//showDialog(R.string.error_invalidaddr);
            return;
        }     
        try {
            mDatagramSocket = new DatagramSocket(5555, client_adress);
            mDatagramSocket.setReuseAddress(true);
        } catch (SocketException e) {
            mDatagramSocket = null;
        	//showDialog(R.string.error_neterror);
            return;
        }
        
        byte[] buf = new byte[256];
        
        try {
            mDatagramPacket = new DatagramPacket(buf, buf.length, client_adress, 5555);
        } catch (Exception e) {
        	mDatagramSocket.close();
        	mDatagramSocket = null;
        	//showDialog(R.string.error_neterror);
        	return;
        }
        //BufferedWriter mBufferwriter = new BufferedWriter(new FileWriter("graph.csv"));
        while(true)
        {
	        try {
				mDatagramSocket.receive(mDatagramPacket);
				String text = new String(mDatagramPacket.getData(),0, mDatagramPacket.getLength());
				System.out.println(text);
				//mBufferwriter.write(text + "\r\n");
				//mBufferwriter.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
        }
    }
}	
    
       
