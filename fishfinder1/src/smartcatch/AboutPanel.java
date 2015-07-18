package smartcatch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class AboutPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TablePanel tablePanel;
	private IconPanel iconPanel; 

	public AboutPanel() {
		setBackground(Color.white);
		this.setLayout(new GridLayout(2, 1));
		tablePanel = new TablePanel();
		iconPanel = new IconPanel();
		this.add(tablePanel);
		this.add(iconPanel);
	}
		
	public String getPreference (String name) {
		String ret =  tablePanel.getPreference(name);
		return (ret);
	}
}

class TablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JScrollPane scrollPane;
	private MyTableModel model = new MyTableModel();
	private JLabel versionLabel; 
	private SmartCatch app = smartcatch.SmartCatch.app;

	public TablePanel () {
		Dimension dim = new Dimension (1200,600);
		loadProperties();
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		scrollPane = new JScrollPane(table);
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		table.setMaximumSize(dim);
		table.setPreferredSize(dim);
		
		versionLabel = new JLabel(app.version, JLabel.CENTER);
		this.add(versionLabel, BorderLayout.NORTH);
		this.setMaximumSize(dim);
		this.setPreferredSize(dim);
	}
		
	public String getPreference (String name) {
		String ret = null;
		for (int i = 0; i < model.data.length; i++) {
			String str = model.data[i][0].toString();
			if (str.equals(name)) {
				if (model.data[i][1] != null) {
					ret = model.data[i][1].toString();
					if (ret == null || ret.isEmpty()) {
						ret = model.data[i][2].toString();
					}
				}
				else
					ret = model.data[i][2].toString();
				return ret;
			}
		}
		return (ret);
	}
	
	class MyTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] columnNames = { "name", "value", "default", "minimum", "maximum" };
	    public Object[][] defaultData = 
	    { 
			{ Cn.SubmarineLeftLightPort, " ", "023", "", "" },
			{ Cn.SubmarineLeftLightRelayPort, " ", "021", "", "" },
			{ Cn.SubmarineRightLightPort, " ", "022", "", "" },
			{ Cn.SubmarineRightLightRelayPort, " ", "020", "", "" },
			{ Cn.SubmarineTemperaturePort, " ", "007", "", "" },
			{ Cn.SubmarineHumidityPort, " ", "007", "", "" },
			{ Cn.SubmarineTiltPort, " ", "014", "", "" },
			{ Cn.SubmarineTiltMin, "", 65, "", "" },
			{ Cn.SubmarineTiltMax, "", 125, "", "" },
			{ Cn.SubmarineIPAddress, " ", "10.0.0.240", "", "" }, 
			{ Cn.SubmarineIPPort, " ", "80", "", "" },
			{ Cn.SubmarineTimeoutSeconds, " ", "5", "", "" }, 
			{ Cn.SubmarineLightMedium, " ", "180", "", "" },
			{ Cn.SubmarineLightHigh, " ", "255", "", "" },
			{ Cn.VideoURL, " ", "http://10.0.0.250/", "", "" },
			{ Cn.TempHumidityStyle, " ", "radial", "", "" },
			{ Cn.FPSLimit, " ", "20", ".016", "50" },
		};
	    
	    public Object[][] data;
	    
	    public MyTableModel () {
	    	super();
	    	data = new Object[defaultData.length][5];
	    	for (int i = 0; i < defaultData.length; i++) {
	    		for (int k = 0; k < 5; k++) {
	    			data[i][k] = defaultData[i][k];
	    		}
	    	}
	    }

	    public int getColumnCount() {
	        return columnNames.length;
	    }

	    public int getRowCount() {
	        return data.length;
	    }

	    public String getColumnName(int col) {
	        return columnNames[col];
	    }

	    public Object getValueAt(int row, int col) {
	        return data[row][col];
	    }

	    @SuppressWarnings({ "rawtypes", "unchecked" })
		public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	    }

	    /*
	     * Don't need to implement this method unless your table is
	     * editable.
	     */
	    public boolean isCellEditable(int row, int col) {
	        return (col == 1);
	    }

	    /*
	     * Don't need to implement this method unless your table's
	     * data can change.
	     */
	    public void setValueAt(Object value, int row, int col) {
	        data[row][col] = value;
	        fireTableCellUpdated(row, col);
	        saveProperties();
	    }
	}	
	
	private void saveProperties() {
		try {
			String home = System.getProperty("user.home");
			File props = new File(home + "/smartcatch.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(props));
			for (int i = 0; i < model.data.length; i++) {
	            String name = model.data[i][0].toString();
	            String value = model.data[i][1].toString();
				bw.write( name  + "=" + value + "\r\n");
	        }
			bw.flush();
			bw.close();
		} catch (IOException ex) {
			app.debug.report(ex);
		}
	}
	
	private void updateData(String name, String value) {
		for (int i = 0; i < model.data.length; i++) {
			if (model.data[i][0].equals(name)) {
				model.data[i][1] = value;
				model.data[i][2] = model.defaultData[i][2];
				model.data[i][3] = model.defaultData[i][3];
			}
		}
	}
	
	private void loadProperties() {
		try {
			String home = System.getProperty("user.home");
			File props = new File(home + "/smartcatch.txt");
			if (props.exists()) {
				Properties properties = new Properties();
				FileInputStream is = new FileInputStream(props);
				properties.load(is);
				is.close();
                Enumeration<?> names = properties.propertyNames();
                while (names.hasMoreElements()) {
                    String name = (String) names.nextElement();
                    String value = (String) properties.get(name);
                    updateData(name, value);
                }				
			}
		} catch (FileNotFoundException fnf) {
			app.debug.report(fnf);
		}
		catch (IOException io) {
			app.debug.report(io);
		}
	}
}

class IconPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public IconPanel() {
		Icon icon = new ImageIcon("img/fishanimated.gif");
		JLabel label = new JLabel(icon);
		this.setLayout(new BorderLayout());
		this.add(label, BorderLayout.CENTER);
		this.setPreferredSize(new Dimension (400, 400));
	}
}
