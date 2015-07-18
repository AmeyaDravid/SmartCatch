package smartcatch;

import javax.swing.*;

import org.apache.commons.codec.binary.Base64;


import java.awt.*;
import java.awt.event.*;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.MessageDigest;  

public class Login extends JFrame implements ActionListener
{
    
	public static boolean toSmartCatch=false;
	JLabel  l2, l3;
    JTextField tf1;
    JButton btn1;
    JButton btn2;
    JPasswordField p1;
    String email;
    String pass;
    String str2;
    /*private Pattern pattern;
	private Matcher matcher;*/

	/*private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/
	String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    
    Login()
    {
  	
    	
    	setTitle("SmartCatch Login");
        setVisible(true);
    	
    	setSize(1024, 1024);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        
        setContentPane(new JLabel(new ImageIcon("/home/darksnake/Desktop/ali.png")));
       
        l2 = new JLabel("Enter Email:");
        l3 = new JLabel("Enter Password:");
        l2.setFont(new Font("Serif", Font.BOLD, 20));
        l3.setFont(new Font("Serif", Font.BOLD, 20));
        tf1 = new JTextField();
        p1 = new JPasswordField();
        btn1 = new JButton("Login");
        btn2 = new JButton("Register");
       // l1.setBounds(100, 30, 400, 30);
        l2.setBounds(80, 70, 200, 30);
        l3.setBounds(80, 110, 200, 30);
        tf1.setBounds(300, 70, 200, 30);
        p1.setBounds(300, 110, 200, 30);
        btn1.setBounds(100, 160, 100, 30);
        btn2.setBounds(300,	 160,100, 30);
        //add(l1);
        add(l2);
        add(tf1);
        add(l3);
        add(p1);
        add(btn1);
        //add(btn2);
        btn1.addActionListener(this);
        btn2.addActionListener(this);
    }
 
    public void actionPerformed(ActionEvent e)
    {
		 String command = e.getActionCommand();

    	if (command.equals("Login")) {
    		//toSmartCatch=true;
    	
    		showData();
        
    	
    	}
    	
    		//showData();

    	        if (command.equals("Register")) {
    	        	this.dispose();
    	        	new Registration();
    	        }
    	
    }
    
    public void showData()
    {
        
        
        
        

        String str1 = tf1.getText();
        
        char[] p = p1.getPassword();
        str2 = new String(p);
      
       
       Boolean b = str1.matches(EMAIL_REGEX);
        if(b)
        {
        	Connection conn = null;
            Statement st = null;
            ResultSet rs = null;
            String dbUrl = "jdbc:mysql://localhost/smartcatch";
            String dbUsr = "root";
            String dbPass = "versace123";

            
            try{
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection (dbUrl,dbUsr,dbPass);
                st = conn.createStatement();

                rs = st.executeQuery("SELECT uic_email,uic_password FROM tbl_user_info where uic_email = '"+str1+"'");
                System.out.println("pass"+str1);

      
                
                
                while(rs.next()){
                	
                	String pass = rs.getString("uic_password");
                	MessageDigest md = MessageDigest.getInstance("SHA1");
        			byte[] o_pass = str2.getBytes();
        			md.update(o_pass);
        			byte[] hash = md.digest();
        			String strpass = new String(hash);
        			Base64 base64 = new Base64();  
        			String strpassword=base64.encodeBase64String(hash); 
        			System.out.println("pass"+strpassword);
        			if(strpassword.equals(pass))
        				{this.dispose();
        				toSmartCatch=true;	}			
        				//new SmartCatch();}
                	//Get values
        			else
        				JOptionPane.showMessageDialog(null,
                                "Incorrect email-Id or password..Try Again with correct detail");

                	return;
                	
                }
                JOptionPane.showMessageDialog(null,
                        "Incorrect email-Id or password..Try Again with correct detail");
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        	
        }
        else
        {
        	JOptionPane.showMessageDialog(null,  "Invalid email-Id");
       	 return;
        }
 }
    public static void main(String arr[])
    {
        new Login();
    
    
    }
}