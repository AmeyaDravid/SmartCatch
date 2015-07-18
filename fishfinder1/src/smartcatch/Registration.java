package smartcatch;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.codec.binary.Base64;
/*import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;*/

public class Registration extends JFrame implements ActionListener{

	
	JLabel l1,l2,l3,l4,l5,l6,l7;
	JTextField t1,t2,t3,t4,t5;
	JPasswordField p1,p2;
	JButton btn1;
	
	Registration()
	{
		
		setTitle("New User Registration");
        setVisible(true);
        setSize(1024, 1024);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new JLabel(new ImageIcon("/home/darksnake/Desktop/ali.png")));
		l1 = new JLabel("First Name");
		l1.setForeground(Color.white);
		l2 = new JLabel("Last Name");
		l2.setForeground(Color.white);
		l3 = new JLabel("Address");
		l3.setForeground(Color.white);
		l4 = new JLabel("Ph no.");
		l4.setForeground(Color.white);
		l5 = new JLabel("Email Id");
		l5.setForeground(Color.white);
		l6 = new JLabel("Password");
		l6.setForeground(Color.white);
		l7 = new JLabel("Confirm Password");
		l7.setForeground(Color.white);
		
		t1 = new JTextField();
		t2 = new JTextField();
		t3 = new JTextField();
		t4 = new JTextField();
		t5 = new JTextField();
		p1 = new JPasswordField();
		p2 = new JPasswordField();
		
		btn1 = new JButton("Submit");
		
		l1.setBounds(80, 70, 200, 30);
        l2.setBounds(80, 110, 200, 30);
        l3.setBounds(80, 150, 200, 30);
        l4.setBounds(80, 190, 200, 30);
        l5.setBounds(80, 230, 200, 30);
        l6.setBounds(80, 270, 200, 30);
        l7.setBounds(80, 310, 200, 30);
        
        
        t1.setBounds(300, 70, 200, 30);
        t2.setBounds(300, 110, 200, 30);
        t3.setBounds(300, 150, 200, 30);
        t4.setBounds(300, 190, 200, 30);
        t5.setBounds(300, 230, 200, 30);
        p1.setBounds(300, 270, 200, 30);
        p2.setBounds(300, 310, 200, 30);
        btn1.setBounds(250, 350, 100, 30);
	
        add(l1);
        add(t1);
        add(l2);
        add(t2);
        add(l3);
        add(t3);
        add(l4);
        add(t4);
        add(l5);
        add(t5);
        add(l6);
        add(p1);
        add(l7);
        add(p2);
        add(btn1);
        
        
      btn1.addActionListener(this);
	
	
	}
	private void showData() {
		
String str1 = t1.getText();
String str2 = t2.getText();
String str3 = t3.getText();
String str4 = t4.getText();
String str5 = t5.getText();

        char[] p = p1.getPassword();
        String str6 = new String(p);
        char[] cp = p2.getPassword();
        String str7 = new String(cp);
	if(!str6.equals(str7))
	{	JOptionPane.showMessageDialog(null,
                "Incorrect password");
     return;   
	}
	
	//String query = ("SELECT uic_firstname FROM tbl_user_info WHERE  ");
	
		
    

	
	
	
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
            
            MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] o_pass = str6.getBytes();
			md.update(o_pass);
			byte[] hash = md.digest();
			/*String strpass = new String(hash);*/
			Base64 base64 = new Base64();  
			String strpassword=base64.encodeBase64String(hash);

			rs = st.executeQuery("SELECT uic_firstname FROM tbl_user_info where uic_firstname = '"+str1+"'");
			/*if(query.equals(t1))
		    	{JOptionPane.showMessageDialog(null,
		                "User already exists");
		    */ 
			
			while(rs.next()){
				JOptionPane.showMessageDialog(null,
		                "User already exists");
						return;
			}

            String sql = "INSERT INTO tbl_user_info " +
                    "VALUES ('"+str1+"','"+str2+"','"+str3+"','"+str4+"','"+str5+"','"+strpassword+"')";
            
                        st.executeUpdate(sql);
            
            this.dispose();
            
            new Login();
            
                        
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
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	

}
