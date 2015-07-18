package smartcatch;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class databaseConnect {
		static Connection conn;
		static Statement st = null;
		static ResultSet rs = null;
		static String dbUrl = "jdbc:mysql://localhost/SmartCatch";
		static String dbUsr = "root";
		static String dbPass = "versace123";
		static double this_array[]=new double[10];
		public static void main() throws ClassNotFoundException, SQLException{
			String url="jdbc:mysql://localhost/SmartCatch";
	    	String user = "root";
	    	String password = "versace123";
	    	String classname="com.mysql.jdbc.Driver";
	    	//Statement st = null;
	        ResultSet rs = null;
	        //double[] this_array;
	    	
	    	try {
	    		Class.forName(classname);
	    		//Connection con = null;
	            
	            /*String dbUrl = "jdbc:mysql://localhost/SmartCatch";
	            String dbUsr = "root";
	            String dbPass = "versace123";*/
	    		
	            Connection con = (Connection) DriverManager.getConnection(dbUrl,dbUsr,dbPass);
	            System.out.println("Success");
	            java.sql.Statement st = con.createStatement();

	            rs =  st.executeQuery("SELECT * from smarty");
	            int i=0;
	            
	           
	           while(rs.next()& i<this_array.length){
	        	   this_array[i]=rs.getFloat("salinity");
	        	   //float salinity=rs.getFloat("salinity");
	        	   //this_array[i]=salinity;
	        	   
	        	   System.out.println(this_array[i]);
	        	   i++;
	           }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	


		}
	
}

