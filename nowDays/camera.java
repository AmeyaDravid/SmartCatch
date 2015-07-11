
import com.mysql.jdbc.Statement;

public class databaseConnect {
	
		Statement st = null;
		static ResultSet rs = null;
		static String dbUrl = "jdbc:mysql://localhost/SmartCatch";
		static String dbUsr = "root";
		static String dbPass = "versace123";
		public static void main() throws ClassNotFoundException, SQLException{
			try {
				Class.forName("com.mysql.jdbc.Driver");
				//System.out.println("pass");
				java.sql.Connection conn = DriverManager.getConnection (dbUrl,dbUsr,dbPass);
			    java.sql.Statement st = conn.createStatement();

			    rs = st.executeQuery("SELECT * from smarty");
			    System.out.println(rs);
			} finally{}

		}
	
}


