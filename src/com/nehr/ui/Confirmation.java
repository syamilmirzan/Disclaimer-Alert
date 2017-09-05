package com.nehr.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import com.nehr.service.Html;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.text.SimpleDateFormat;
import oracle.jdbc.*;

public class Confirmation extends JDialog {

	 public static Properties props = new Properties();
	 InputStream input = null;
	 public static SimpleDateFormat inputDateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
	 public static SimpleDateFormat logformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
     Connection connection = null;

	public void testConnection(){
		System.out.println("-------- Oracle JDBC Connection Testing ------");

        try 
        {
        	
        	input = new FileInputStream("queries.properties");

    		// load a properties file
        	props.load(input);

            Class.forName("oracle.jdbc.driver.OracleDriver");

        } 
        catch (ClassNotFoundException e) {

            System.err.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Where is your Oracle JDBC Driver?");
        }
        catch (FileNotFoundException e)
        {
        	System.err.println("File not found for Props!");
        	JOptionPane.showMessageDialog(null, "file not found for props!");
            e.printStackTrace();
        }
        catch(IOException e)
        {
        	System.err.println("I am expected here");
        	JOptionPane.showMessageDialog(null, "I am expected here");
            e.printStackTrace();
        }

        System.out.println("Oracle JDBC Driver Registered!");
        JOptionPane.showMessageDialog(null, "Oracle JDBC Driver Registered!");

        try {

            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@10.10.21.23:1521/LIFERAY", "LIFERAY", "liferay#1234");
            
            connection.setAutoCommit(true);

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection Failed! Check output console");

        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
            JOptionPane.showMessageDialog(null, "Connection Failed!");

        }
    
	}
	
	//WINDOW FORM CODES
	
	private final JPanel contentPanel = new JPanel();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {

			Confirmation dialog = new Confirmation(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @param newDisclaimer 
	 */
	public Confirmation(final ArrayList<String> newDisclaimer) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setTitle("Confirmation");
		setBounds(100, 100, 450, 307);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Confirm Submitting This Form?");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblNewLabel.setBounds(20, 11, 206, 35);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblTheDataYou = new JLabel("The data you have entered is:");
			lblTheDataYou.setBounds(20, 46, 161, 14);
			contentPanel.add(lblTheDataYou);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(20, 71, 404, 146);
			contentPanel.add(scrollPane);
			{
				//Export the data from newDisclaimer into the display
				
				JTextArea txtrTest = new JTextArea();

				
				//Message Display
				String display = "From: " + newDisclaimer.get(0).toString() + "\n" + 
				"To: " + newDisclaimer.get(1).toString() + "\n\n" + 
				"Dislaimer Message: " + "\n" + newDisclaimer.get(2).toString();
				txtrTest.setText(display);
				txtrTest.setLineWrap(true);
				txtrTest.setEditable(false);
				txtrTest.setWrapStyleWord(true);
				scrollPane.setViewportView(txtrTest);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//Testing purposes for converting text to HTML
						//String disclaimerHTML = Html.textToHTML(textArea.getText());
						//String rawText = newDisclaimer.get(2).toString();
						String html = Html.textToHTML(newDisclaimer.get(2).toString());
						System.out.println(html);
						
						//test connection to database and importing properties file
						String result = null;
						testConnection();
						
						
						PreparedStatement p1 = null;
						PreparedStatement p2 = null;
						
						ResultSet s1 = null;
						ResultSet s2 = null;
						
						// Retrieving the latest disclaimer entry
						
						// SELECT MAX(IDENTIFIER) FROM LIFERAY.DISCLAIMER_SYSALERT
						String retrieve = props.getProperty("retrieveLatestDisclaimerID");
						System.out.println("Querying: " + retrieve);
						
						int latest = 0;
						try {
							p1 = connection.prepareStatement(retrieve);
							//p1.setString(1, newDisclaimer.get(0).toString());
							//p1.setString(2, newDisclaimer.get(1).toString());
							//p1.setString(3, html);
							
							s1 = p1.executeQuery();
							
							while(s1.next()){
							System.out.println(s1.getString(1));
							latest = Integer.parseInt(s1.getString(1));
							}
							
							System.err.println("RETRIEVAL SUCCESS!!");
							JOptionPane.showMessageDialog(null, "RETRIEVAL SUCCESSFUL!");
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						// Insert a new disclaimer entry
						
						// INSERT INTO LIFERAY.DISCLAIMER_SYSALERT (IDENTIFIER,ENTRY_TYPE,CONTENT,ICON_REQ,VALIDITY_START_DT,VALIDITY_END_DT,CREATED_DATE_TIME) 
						// values ([ID},'Alert',[HTML],'null',to_timestamp([FROM],'DD-MON-RR HH.MI.SSXFF AM'),to_timestamp([TO],'DD-MON-RR HH.MI.SSXFF AM'),to_timestamp([CREATED],'DD-MON-RR HH.MI.SSXFF AM'));

						String insert = props.getProperty("insertDisclaimerEntry");
						System.out.println("Querying: " + insert);
						latest = latest + 1;
						String newID = Integer.toString(latest);
						Date date = new Date();
						try {
							p2 = connection.prepareStatement(insert);
							p2.setString(1, newID);
							p2.setString(2, html);
							p2.setString(3, newDisclaimer.get(0).toString());
							p2.setString(4, newDisclaimer.get(1).toString());
							p2.setString(5, inputDateformat.format(date));
							//p1.setString(1, newDisclaimer.get(0).toString());
							//p1.setString(2, newDisclaimer.get(1).toString());
							//p1.setString(3, html);
							
							s2 = p2.executeQuery();
							
							System.err.println("INSERTION SUCCESS!!");
							JOptionPane.showMessageDialog(null, "INSERTION SUCCESSFUL!");
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "INSERTION FAILED: " + e1 );
						}
						
						JOptionPane.showMessageDialog(null, "Closing! ....");
						//Close Window
						dispose();
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
