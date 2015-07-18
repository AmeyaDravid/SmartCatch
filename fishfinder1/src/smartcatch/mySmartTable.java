package smartcatch;


import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class mySmartTable {
	
  public static void createTable() {
    JPanel frame = SmartCatch.comPanel;
    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    
    final JTable table = new JTable();
    DefaultTableModel model =new DefaultTableModel();
    table.setModel(model);
    model.addColumn("serial number");
    model.addColumn("name of file");
    //model.addRow(new String[] {"1","file.mov"});
    File file=new File(System.getProperty("user.dir")+"/video");
    String[] fileString=file.list();
    int length=fileString.length;
    for (int i=0;i<length;i++){
    	model.addRow(new String[]{" "+i,System.getProperty("user.dir")+"/video/"+fileString[i]});
    }
    JScrollPane scrollPane = new JScrollPane(table);
    frame.add(scrollPane);
    //frame.setBounds(0, 0, 640, 480);
    //frame.setSize(1000, 1000);
    //frame.setVisible(true);
    table.addMouseListener(new MouseListener() {
		
		
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			int row = table.rowAtPoint(arg0.getPoint());
	        int col = table.columnAtPoint(arg0.getPoint());
	        if (row >= 0 && col >= 0) {
	        	File file=new File(table.getValueAt(row,col).toString());
				if(file.isFile()){
				try {
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
	        	//JOptionPane.showMessageDialog(null,"Value in the cell clicked :"+table.getValueAt(row,col).toString());

	        }
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	});

  }
}