package com.nehr.ui;

import java.awt.EventQueue;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.UIManager;


public class DisclamerAlert {

	private JFrame frmDisclaimerAlertForm;
	private JTextField textField;
	private JTextField textField_1;
	ArrayList<String> newDisclaimer = new ArrayList<String>(); 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisclamerAlert window = new DisclamerAlert();
					window.frmDisclaimerAlertForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DisclamerAlert() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		String fromCalendarDate = null;
		String toCalendarDate = null;
		// String disclaimerHTML = null;
		
		
		frmDisclaimerAlertForm = new JFrame();
		frmDisclaimerAlertForm.getContentPane().setBackground(SystemColor.activeCaption);
		frmDisclaimerAlertForm.setTitle("Disclaimer Alert Form (Version 1.0 PROD) - Created by Syamil");
		frmDisclaimerAlertForm.setBounds(100, 100, 667, 450);
		frmDisclaimerAlertForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDisclaimerAlertForm.getContentPane().setLayout(null);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(frmDisclaimerAlertForm.getContentPane(), popupMenu);
		
		//Disclaimer Alert Form Label
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 651, 32);
		frmDisclaimerAlertForm.getContentPane().add(panel);
		
		JLabel lblDisclaimerAlertForm = new JLabel("Disclaimer Alert Form");
		lblDisclaimerAlertForm.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel.add(lblDisclaimerAlertForm);
		
		//FROM panel
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0)), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
		panel_1.setBounds(10, 43, 314, 86);
		frmDisclaimerAlertForm.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblfromSelectData = new JLabel("[FROM] Select Date and Time:");
		lblfromSelectData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblfromSelectData.setBounds(5, 11, 193, 23);
		panel_1.add(lblfromSelectData);
		
		final JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(15, 45, 160, 20);
		panel_1.add(dateChooser);
		
		//TESTING FOR PROD, CHOOSE OLD DATES ONLY (SETTED TO MAXDATE)
		//dateChooser.setMaxSelectableDate(new Date());
		
		dateChooser.setDateFormatString("dd-MMM-yy");
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1.00", "1.30", "2.00", "2.30", "3.00", "3.30", "4.00", "4.30", "5.00", "5.30", "6.00", "6.30", "7.00", "7.30", "8.00", "8.30", "9.00", "9.30", "10.00", "10.30", "11.00", "11.30", "12.00", "12.30"}));
		comboBox.setBounds(186, 45, 65, 20);
		panel_1.add(comboBox);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"AM", "PM"}));
		comboBox_1.setBounds(261, 45, 45, 20);
		panel_1.add(comboBox_1);
		
	/*	Testing calendar and time entries
	 
		JLabel lblDate = new JLabel("Date:");
		lblDate.setBounds(213, 101, 46, 14);
		panel_1.add(lblDate);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(213, 121, 86, 20);
		textField.setText(calendar.getDate().toString());
		panel_1.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Time:");
		lblNewLabel_1.setBounds(213, 152, 46, 14);
		panel_1.add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(213, 177, 86, 20);
		textField_1.setText(comboBox.getSelectedItem().toString() + "." + comboBox_1.getSelectedItem().toString());
		panel_1.add(textField_1);
		textField_1.setColumns(10);
	*/	
		
		//TO Panel
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0)), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
		panel_2.setBounds(324, 43, 314, 86);
		frmDisclaimerAlertForm.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("[TO] Select Date and Time:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(5, 11, 178, 24);
		panel_2.add(lblNewLabel);
		
		final JDateChooser dateChooser_1 = new JDateChooser();
		
		//TESTING FOR PROD, CHOOSE OLD DATES ONLY (SETTED TO MAXDATE)
		//dateChooser_1.setMaxSelectableDate(new Date());
		
		dateChooser_1.setDateFormatString("dd-MMM-yy");
		dateChooser_1.setBounds(15, 45, 160, 20);
		panel_2.add(dateChooser_1);
		
		final JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"1.00", "1.30", "2.00", "2.30", "3.00", "3.30", "4.00", "4.30", "5.00", "5.30", "6.00", "6.30", "7.00", "7.30", "8.00", "8.30", "9.00", "9.30", "10.00", "10.30", "11.00", "11.30", "12.00", "12.30"}));
		comboBox_2.setBounds(185, 45, 65, 20);
		panel_2.add(comboBox_2);
		
		final JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"AM", "PM"}));
		comboBox_3.setBounds(260, 45, 45, 20);
		panel_2.add(comboBox_3);
		
		//Disclaimer Details Panel
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 176, 425, 224);
		frmDisclaimerAlertForm.getContentPane().add(scrollPane);
		
		final JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		
		JLabel lblDisclaimerDetails = new JLabel("Disclaimer Details:");
		lblDisclaimerDetails.setBackground(new Color(180, 180, 180));
		lblDisclaimerDetails.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDisclaimerDetails.setBounds(10, 142, 193, 23);
		frmDisclaimerAlertForm.getContentPane().add(lblDisclaimerDetails);
		
		//Action Buttons
		
		//Error Handling Message Box
		final JTextArea textArea_1 = new JTextArea();
		textArea_1.setForeground(Color.RED);
		textArea_1.setWrapStyleWord(true);
		textArea_1.setLineWrap(true);
		textArea_1.setEditable(false);
		textArea_1.setBackground(SystemColor.activeCaption);
		textArea_1.setBounds(445, 260, 193, 140);
		frmDisclaimerAlertForm.getContentPane().add(textArea_1);
		
		//CLEAR Disclaimer Details
		JButton btnClear = new JButton("CLEAR");
		btnClear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				textArea.setText(null);
				//clear array
				newDisclaimer.clear();
				//clear error label
				textArea_1.setText("");
			}
		});
		btnClear.setBounds(496, 178, 89, 23);
		frmDisclaimerAlertForm.getContentPane().add(btnClear);
		

		
		
		JButton btnSubmit = new JButton("SUBMIT");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
				//Clear cache before 'COLLECTING DATA'
				System.out.println("newDisclaimer ArrayList size before removing elements : " + newDisclaimer.size());
				newDisclaimer.clear();
				System.out.println("newDisclaimer ArrayList size after removing elements : " + newDisclaimer.size());
				
				//Testing purposes for converting text to HTML
				//String disclaimerHTML = Html.textToHTML(textArea.getText());
				//System.out.println(Html.textToHTML(textArea.getText()));
				
				//Collect all data required and sent it over to database
				// 1- Combine all data into a whole string
				// 2- Collect data from all sources and put into an array
				// 3- Send it over to the dialog
				
			// 1 --------- Combine Calendar Data
				DateFormat fmt = new SimpleDateFormat("dd-MMM-yy");
				String fromCalendarDate = fmt.format(dateChooser.getDate()).toString().toUpperCase() + " " + comboBox.getSelectedItem() + ".00.000000000 " + comboBox_1.getSelectedItem();

				String toCalendarDate = fmt.format(dateChooser_1.getDate()).toString().toUpperCase() + " " + comboBox_2.getSelectedItem() + ".00.000000000 " + comboBox_3.getSelectedItem();
				
				//Test print out in console
				System.out.println(fromCalendarDate);
				System.out.println(toCalendarDate);
				
			// 2 ------------- Collect Data
				newDisclaimer.add(fromCalendarDate);
				newDisclaimer.add(toCalendarDate);
				newDisclaimer.add(textArea.getText());
				//newDisclaimer.add(disclaimerHTML);
				
				//Test print collection
				System.out.println(newDisclaimer);
				
				Confirmation newWindow = new Confirmation(newDisclaimer);
				newWindow.setVisible(true);
				
				}
				//Display Error in label when the data value is empty
				catch (java.lang.NullPointerException exSubmit) {
					
					System.out.println(exSubmit);
					textArea_1.setText("Please complete your entry!");
					
					//Display which one is incomplete
					if (dateChooser.getDate() == null){
						textArea_1.setText(textArea_1.getText() + "\n\n" + "Empty Field in [FROM] Calendar");
					}
					if (dateChooser_1.getDate() == null){
						textArea_1.setText(textArea_1.getText() + "\n" + "Empty Field in [TO] Calendar");
					}
					if (textArea.getText().equals("")){
						textArea_1.setText(textArea_1.getText() + "\n" + "Empty Field in Disclaimer Details");
					}
				}
			}
		});
		
		btnSubmit.setBounds(496, 233, 89, 23);
		frmDisclaimerAlertForm.getContentPane().add(btnSubmit);
		

		

	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
