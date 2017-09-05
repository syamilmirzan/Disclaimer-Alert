package com.nehr.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.ArrayList;


/*import weblogic.security.UserConfigFileManager;
import weblogic.security.UsernameAndPassword;*/
 
 public class ConcertoStats{ 

 public static Properties props = new Properties();
 public static SimpleDateFormat inputDateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
 public static SimpleDateFormat logformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

Connection dbsoa = null ;
Connection dbods = null;
Connection dbcdb = null;



/* get SOA connection Details*/
 public void getSOAConnectionDetails(){

	try{

		
		Class.forName("oracle.jdbc.driver.OracleDriver");
	

		String userName = props.getProperty("db.oracle.connect.soausername");
		String passWord =  props.getProperty("db.oracle.connect.soapassword");
		String url		=  props.getProperty("db.oracle.connect.soaurl");
		
		
		dbsoa =DriverManager.getConnection(url, userName,passWord);  
		
		}catch (ClassNotFoundException e) {
			System.out.println("Unable to locate OracleDriver");
			e.printStackTrace();
			return;
	
		}catch (SQLException e){
			System.out.println("SQL Exception123");
			e.printStackTrace();
		return;
		}

		
		System.out.println("Successfully connected to data base");

 }

 /* get CDB data base connection details */
public void getCDBDBConnectionDetails(){

	try{

		
		Class.forName("oracle.jdbc.driver.OracleDriver");
	

		String userName = props.getProperty("db.oracle.connect.cdbusername");
		String passWord = props.getProperty("db.oracle.connect.cdbpassword");
		String url		= props.getProperty("db.oracle.connect.cdburl");
		
		
		dbcdb =DriverManager.getConnection(url, userName,passWord);  
		
		}catch (ClassNotFoundException e) {
			System.out.println("Unable to locate OracleDriver");
			e.printStackTrace();
			return;
	
		}catch (SQLException e){
			System.out.println("SQL Exception123");
			e.printStackTrace();
		return;
		}

		
		System.out.println("Successfully connected to data base");

}

 


/* 
Function used to get the concerto stats for unique user count,
Total login count of current date and last week date.
Calculate % of unique user login= ((Current Unique Login- Last Week Unique Login)/Last Week Unique Login)*100)
calculate % of Total user login =((current Total Login - Last Week Total Login)/Last Week Total Login)*100)


Calculate SOA message percentage 
% of SOA = CompletedMessage/TotalMessage
if %0f SOA message less then 80 % than inbount persistence is not ok

*/

 public void getConcertoStats(){

		System.out.println("Stats Query function running");

		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt1a = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt2a = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt3a = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt4a = null;
		PreparedStatement pstmt5 = null;
		PreparedStatement pstmt6 = null;
		PreparedStatement pstmt7 = null;
		PreparedStatement pstmt8 = null;
		PreparedStatement pstmt9 = null;
		PreparedStatement pstmt10 = null;
		PreparedStatement pstmt10b = null;
		PreparedStatement pstmt11 = null;
		PreparedStatement pstmt11b = null;
		PreparedStatement pstmt12 = null;
		PreparedStatement pstmt13 = null; 
		PreparedStatement pstmt14 = null;

		ResultSet rs1 = null;
		ResultSet rs1a = null;
		ResultSet rs2 = null;
		ResultSet rs2a = null;
		ResultSet rs3 = null;
		ResultSet rs3a = null;
		ResultSet rs4 = null;
		ResultSet rs4a = null;
		ResultSet rs5 = null;
		ResultSet rs6 = null;
		ResultSet rs7 = null;
		ResultSet rs8 = null;
		ResultSet rs9 = null;
		ResultSet rs10 = null;
		ResultSet rs10b = null;
		ResultSet rs11 = null;
		ResultSet rs11b =null;
		ResultSet rs12 = null;
		ResultSet rs13 = null;
		ResultSet rs14 = null;

		
		int countCurrentUnique = 0;
		int countLastWeekUnique = 0;
		int countTotallogin = 0 ;
		int countLastWeekTotalLogin = 0;
		int countNoOfPatientSearches = 0;
		float total3hrsmess = 0;
	    float completed3hrmess = 0;
		float percentageUniqueLogin = 0 ;
		float percentageTotalLogin = 0;
		int totalMessCount = 0;
		int totalMessCount3hrs = 0;
		int tempCDBcount = 0;
		float diffUniqueLogin = 0;
		float diffTotalLogin = 0;
		float percentageMess = 0 ;
		

		String compareUnique = "More";
		String compareTotal = "More";
		String statsTime = null; 
		String lastWeekTime = null;
		String percentageUniLogin = null;
		String percentageTotLogin = null;

		String currentUniqueCDBQuery = null;
		String distinctUsersCount = null;
		String currentTotalQuery = null;
		String currentTotalCDBQuery = null;
		String currentTotalPatientSearches = null;
		String lastWeekUniqueCDBQuery = null;
		String lastWeekTotalCDBQuery = null;
		String dayBefore= null;
		String dayBeforeWeek = null;
		String inboundPersistenceQuery = null;
		String liferayCurrentTrafficQuery = null;
		String  odsExcludedUsers = "";

		//StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		StringBuffer sb3 = new StringBuffer();
		
		Set<String> todayUniqueUserLogin = new HashSet<String>();
		Set<String> lastWeekUniqueUserLogin = new HashSet<String>();

		ArrayList<String> definedClusterNames = new ArrayList<String>();
		ArrayList<String> clusterArr = new ArrayList<String>();
		ArrayList<String> newCluster = new ArrayList<String>();
		//Set<String> newCluster = new HashSet<String>();
			try{
				
				/*retrieve ODS excluded users here*/
				odsExcludedUsers = props.getProperty("odsExcludedUsers");

				/* Concerto Stats Query start here*/
				pstmt5 = dbcdb.prepareStatement(props.getProperty("getSysdateandtime"));
				
				try{
				rs5 = pstmt5.executeQuery();
				}catch (Exception e){
					System.out.println("error in loop 1");
					e.printStackTrace();
				}

				while (rs5.next()) {
				 statsTime = rs5.getString(1);
				 lastWeekTime =rs5.getString(2);
				 dayBefore = rs5.getString(3);
				 dayBeforeWeek =rs5.getString(4);
				 			
				}
				//String statsZeroTime = "21/04/2016 00:00:00";  //Hard code value for test
				String statsZeroTime  = new String(statsTime);
				statsZeroTime = statsZeroTime.substring(11,13);
				if (statsZeroTime.equals("00"))
				{
					statsTime = dayBefore + " 00:00:00";
					lastWeekTime =dayBeforeWeek+ " 00:00:00";
					System.out.println("statsTime: "+ statsTime + "dayBefore:: "+ dayBefore);

					distinctUsersCount = props.getProperty("distinctUsersCount");
					distinctUsersCount = distinctUsersCount.concat(odsExcludedUsers).concat(")");

					currentUniqueCDBQuery = props.getProperty("dayBeforeUniqueCDBLogin");
					currentUniqueCDBQuery = currentUniqueCDBQuery.concat(odsExcludedUsers).concat(")");
					System.out.println("dayBeforeUniqueCDBLogin " + currentUniqueCDBQuery);

					currentTotalCDBQuery = props.getProperty("dayBeforeTotalCDBLogin");
					currentTotalCDBQuery = currentTotalCDBQuery.concat(odsExcludedUsers).concat(")");

					currentTotalPatientSearches = props.getProperty("dayBeforeTotalNoOfPatientSearches");
					currentTotalPatientSearches = currentTotalPatientSearches.concat(odsExcludedUsers).concat(")");

					lastWeekUniqueCDBQuery = props.getProperty("dayBeforeWeekUniqueCDBLogin");
					lastWeekUniqueCDBQuery = lastWeekUniqueCDBQuery.concat(odsExcludedUsers).concat(")");

					lastWeekTotalCDBQuery = props.getProperty("dayBeforeWeekTotalCDBLogin");
					lastWeekTotalCDBQuery = lastWeekTotalCDBQuery.concat(odsExcludedUsers).concat(")");

					inboundPersistenceQuery = props.getProperty("monitoring.daybefore.total.messages");
					liferayCurrentTrafficQuery = props.getProperty("liferay.current.traffic.prev.day");

				}else{
					
					distinctUsersCount = props.getProperty("distinctUsersCount");
					distinctUsersCount = distinctUsersCount.concat(odsExcludedUsers).concat(")");

					currentUniqueCDBQuery = props.getProperty("todayUniqueCDBUserLogin");
					currentUniqueCDBQuery = currentUniqueCDBQuery.concat(odsExcludedUsers).concat(")");
					System.out.println("todayUniqueCDBUserLogin " + currentUniqueCDBQuery);	

					currentTotalCDBQuery = props.getProperty("todayTotalCDBLogin");
					currentTotalCDBQuery = currentTotalCDBQuery.concat(odsExcludedUsers).concat(")");

					currentTotalPatientSearches = props.getProperty("totalNoOfPatientSearches");
					currentTotalPatientSearches = currentTotalPatientSearches.concat(odsExcludedUsers).concat(")");

					lastWeekUniqueCDBQuery = props.getProperty("lastWeekUniqueCDBUserLogin");
					lastWeekUniqueCDBQuery = lastWeekUniqueCDBQuery.concat(odsExcludedUsers).concat(")");

					lastWeekTotalCDBQuery = props.getProperty("lastWeekTotalCDBLogin");
					lastWeekTotalCDBQuery = lastWeekTotalCDBQuery.concat(odsExcludedUsers).concat(")");

					inboundPersistenceQuery =props.getProperty("monitoring.total.messages");
					liferayCurrentTrafficQuery = props.getProperty("liferay.current.traffic");

				}
				System.out.println("statsZeroTime:::"+ statsZeroTime);
				System.out.println("statsTime:::"+ statsTime); //16/10/2014 00:00
				System.out.println("lastWeekTime:::"+ lastWeekTime);

				pstmt1 = dbcdb.prepareStatement(distinctUsersCount);
				pstmt1.setString(1, statsTime);
				try{
					
					rs1 = pstmt1.executeQuery();
					System.out.println("Query 1  executed");	
				}catch (Exception e){
					System.out.println("error in loop 2");
					e.printStackTrace();
				}
				

				pstmt1a = dbcdb.prepareStatement(currentUniqueCDBQuery);
				pstmt1a.setString(1, statsTime);
				try{
					
					rs1a = pstmt1a.executeQuery();
					System.out.println("Query 1a  executed");	
				}catch (Exception e){
					System.out.println("error in loop 2a");
					e.printStackTrace();
				}
				


				pstmt2a = dbcdb.prepareStatement(currentTotalCDBQuery);
				pstmt2a.setString(1,statsTime);

				try{
					
				rs2a = pstmt2a.executeQuery();
				System.out.println("Query 2a  executed");

				}catch (Exception e){
					System.out.println("error in loop 3a");
				}

				

				pstmt3a = dbcdb.prepareStatement(lastWeekUniqueCDBQuery);
		
				pstmt3a.setString(1, lastWeekTime);
				try{
					
				 rs3a = pstmt3a.executeQuery();
				System.out.println("Query 3a executed");

				}catch (Exception e){
					System.out.println("error in loop 4a");
				}
				
				while(rs3a.next()){
					lastWeekUniqueUserLogin.add(rs3a.getString(1).trim().toUpperCase());
				}

				

				pstmt4a = dbcdb.prepareStatement(lastWeekTotalCDBQuery);
				pstmt4a.setString(1, lastWeekTime);

				try{
					
				
				 rs4a = pstmt4a.executeQuery();
				System.out.println("Query 4a executed");

				}catch (Exception e){
					System.out.println("error in loop 5a");
				}

				pstmt14 = dbcdb.prepareStatement(currentTotalPatientSearches);
				pstmt14.setString(1, statsTime);

				System.out.println(statsTime);
				try{
				System.out.println("Query 14 Executin");
				  rs14 = pstmt14.executeQuery();

				}catch (Exception e){
					System.out.println("error in loop 12");
					System.out.println(currentTotalPatientSearches);
				}

				
				/*Concerto stats query end here*/
				/* SOA stats query execution start here */
				pstmt6 = dbsoa.prepareStatement(inboundPersistenceQuery);
				
				try{
					
				  rs6 = pstmt6.executeQuery();
				System.out.println("Query 6 executed");

				}catch (Exception e){
					System.out.println("error in loop 6");
				}

				pstmt7 = dbsoa.prepareStatement(props.getProperty("monitoring.last3hours.messages"));
				
				
				try{
					
				  rs7 = pstmt7.executeQuery();
				System.out.println("Query 7 executed");

				}catch (Exception e){
					System.out.println("error in loop 7");
					e.printStackTrace();
				}

				pstmt8 = dbsoa.prepareStatement(props.getProperty("monitoring.last3hourstotal.message"));
				
				try{
					
				  rs8 = pstmt8.executeQuery();
				 System.out.println("Query 8 executed");

				}catch (Exception e){
					System.out.println("error in loop 8");
				}

				pstmt9 = dbsoa.prepareStatement(props.getProperty("monitoring.last3hourscomplete.message"));
				
				
				try{
					
				  rs9 = pstmt9.executeQuery();
				System.out.println("Query 9 executed");

				}catch (Exception e){
					System.out.println("error in loop 9");
					e.printStackTrace();
				}

				pstmt12 = dbsoa.prepareStatement(props.getProperty("monitoring.last3hrbysrcapp.messages"));
                                try{

                                  rs12 = pstmt12.executeQuery();
				System.out.println("Query 12 executed");

                                }catch (Exception e){
                                        System.out.println("error in loop 12");
                                        e.printStackTrace();
                                }
				/*SOA query Execution end here*/

				/* Concerto Last 3 hrs Traffic*/
			
					



                                pstmt10b = dbcdb.prepareStatement(props.getProperty("liferay.last3hours.traffic"));
                                try{

                                  rs10b = pstmt10b.executeQuery();
                                System.out.println("Query 10b executed");

                                }catch (Exception e){
                                        System.out.println("error in loop 10b");
                                        e.printStackTrace();
                                }


				System.out.println("Query 11b TRYING");
                                pstmt11b = dbcdb.prepareStatement(liferayCurrentTrafficQuery);
			
                                pstmt11b.setString(1, statsTime);
                                try{

                                 rs11b = pstmt11b.executeQuery();
                                 System.out.println("Query 11b executed");

                                }catch (Exception e){
                                        System.out.println("error in loop 11b");
                                        e.printStackTrace();
                                }




				/*Concerto Result set start here */

	while (rs1.next()) {
				countCurrentUnique = rs1.getInt(1); 
				
				}
	
			while (rs2a.next()) {
				tempCDBcount = rs2a.getInt(1); 
				countTotallogin = countTotallogin + tempCDBcount;
				
				}

				System.out.println("COUNT CDB RS2A:" + tempCDBcount);

				countLastWeekUnique = lastWeekUniqueUserLogin.size();


			while (rs4a.next()) {
				countLastWeekTotalLogin = countLastWeekTotalLogin + rs4a.getInt(1);				
			}

			while(rs14.next()){
				countNoOfPatientSearches = rs14.getInt(1); // + tempCDBcount;			
			}
			//while(rs2a.next()){
				//countNoOfPatientSearches = countNoOfPatientSearches + rs2a.getInt(1);
			//}

			
			
                        while (rs10b.next()) {

                                String clusterName = rs10b.getString(1);
                                String counts = rs10b.getString(2);

                                sb1.append(clusterName).append(": ").append(counts).append("\t");
                                sb1.append(System.getProperty("line.separator"));
                                clusterArr.add(clusterName);
                        }



			definedClusterNames.add("SHS_SCM");
			definedClusterNames.add("ILTC");
			definedClusterNames.add("NNJA_CPRS");
			definedClusterNames.add("AHPL_SCM");
			definedClusterNames.add("JHS_EPIC");
			definedClusterNames.add("AIC_IRMS");
			definedClusterNames.add("PORTAL");

			newCluster.add("SHS_SCM");
			newCluster.add("ILTC");
			newCluster.add("NNJA_CPRS");
			newCluster.add("AHPL_SCM");
			newCluster.add("JHS_EPIC");
			newCluster.add("AIC_IRMS");
			newCluster.add("PORTAL");
			
			for(String definedCluster: definedClusterNames){
				for(String cluster:clusterArr){
					if(definedCluster.equals(cluster)){
						newCluster.remove(definedCluster);
					}	

				}
			}	

			for(String cluster: newCluster){
				sb1.append(cluster).append(": 0").append("\t");
				sb1.append(System.getProperty("line.separator"));
			}

			clusterArr.clear();
			newCluster.clear();

			newCluster.add("SHS_SCM");
			newCluster.add("ILTC");
			newCluster.add("NNJA_CPRS");
			newCluster.add("AHPL_SCM");
			newCluster.add("JHS_EPIC");
			newCluster.add("AIC_IRMS");
			newCluster.add("PORTAL");

			System.out.println(liferayCurrentTrafficQuery);		
	

                        while (rs11b.next()) {

                                String clusterName = rs11b.getString(1);
                                String counts = rs11b.getString(2);
			
				System.out.println("MARK " + clusterName + " " + counts);	
					

                                sb2.append(clusterName).append(": ").append(counts).append("\t");
                                sb2.append(System.getProperty("line.separator"));
                                clusterArr.add(clusterName);
                        }

			for(String definedCluster: definedClusterNames){
				for(String cluster:clusterArr){
					if(definedCluster.equals(cluster)){
						newCluster.remove(definedCluster);
					}	

				}
			}	

			for(String cluster: newCluster){
				sb2.append(cluster).append(": 0").append("\t");
				sb2.append(System.getProperty("line.separator"));
			}

            while (rs12.next()) {

                String clusterName = rs12.getString(1);
                String counts = rs12.getString(2);

                sb3.append(clusterName).append(": ").append(counts).append("\t");
                sb3.append(System.getProperty("line.separator"));

            }
			/* Concerto result set end here*/
			/* SOA result start here*/
				while (rs6.next()) {
		
				totalMessCount = totalMessCount + rs6.getInt(2);
				//System.out.println("totalMessCount::: "+totalMessCount);

				/* sb.append(trasactionStatus).append(": ").append(messageCount).append("\t");
				 sb.append(System.getProperty("line.separator"));*/
				 
							
				}

				while (rs7.next()) {
					
				/*String trasactionStatus3 = rs7.getString(1);
				String messageCount3 = rs7.getString(2);*/
				totalMessCount3hrs = totalMessCount3hrs + rs7.getInt(2);

				/* sb1.append(trasactionStatus3).append(": ").append(messageCount3).append("\t");
				 sb1.append(System.getProperty("line.separator"));*/
				  
							
				}


				while (rs8.next()) {
					
					 total3hrsmess = rs8.getInt(1);
					 System.out.println("Last Three hours Total Message::: "+ total3hrsmess);
					 
				}

				while (rs9.next()) {
					
				
					 completed3hrmess = rs9.getInt(1);
					 System.out.println("Last Three hours Completed Message::: "+ completed3hrmess);
					
				}


			/*SOA result set end here*/
			/* concerto stats percentage calculation start here*/

				if (countCurrentUnique == countLastWeekUnique )
				{
					compareUnique = "Same";
				}else if (countCurrentUnique < countLastWeekUnique )
				{
					diffUniqueLogin = countLastWeekUnique - countCurrentUnique;
					compareUnique = "Less";
				}else {

					diffUniqueLogin = countCurrentUnique - countLastWeekUnique;
					
				}
				if (countTotallogin == countLastWeekTotalLogin)
				{
					compareTotal = "Same";
				}else if (countTotallogin < countLastWeekTotalLogin )
				{
					diffTotalLogin = countLastWeekTotalLogin - countTotallogin ;
					compareTotal = "Less";
				}else {

					diffTotalLogin = countTotallogin - countLastWeekTotalLogin;
					
				}

				

			}catch (SQLException e){
			System.out.println("Exception execution query");
			e.printStackTrace();
		}

		Date dt = new Date();
        DateFormat fileNameFormat = new SimpleDateFormat("ddMMyyyy'_'HHmm");
       

		if (!compareUnique.equals ("Same"))
		{
			percentageUniqueLogin = (diffUniqueLogin/countLastWeekUnique)*100;
			percentageUniLogin = String.valueOf(Math.round((percentageUniqueLogin)*10)/10.0) ;
				
		}
		
		if (!compareTotal.equals ("Same"))
		{
			percentageTotalLogin = (diffTotalLogin/countLastWeekTotalLogin)*100;
			percentageTotLogin = String.valueOf(Math.round((percentageTotalLogin)*10)/10.0) ;
			
		}
		
			
		try{
		
		FileWriter writer = new FileWriter("./out/ConcertoStats_"+ fileNameFormat.format(dt)+".txt"); //Create text file to write stats details
		boolean timeOut = false;
		System.out.println("Start written into text file");
		writer.write("Application Health Monitoring as of : " + statsTime);
		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");
		writer.write(System.getProperty( "line.separator" ));
		/*if (timeOut)
		{
			writer.write("There is patient search Time out Error");
		}else{
			writer.write("No patient search Time out Error");	
		}

		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");*/
		
		writer.write(System.getProperty( "line.separator" ));
		
		// REMOVE OUTBOUND WRITER
/*		
  		writer.write("Usage");
		writer.write(" ");
		writer.write(System.getProperty( "line.separator" ));
		writer.write("Unique User Count: "+countCurrentUnique);
		writer.write(System.getProperty( "line.separator" ));
		writer.write("Total Number of Logins: "+ countTotallogin);
		writer.write(System.getProperty( "line.separator" ));
		writer.write("Total Number of Patient Searches: "+countNoOfPatientSearches);
		writer.write(System.getProperty( "line.separator" ));
		writer.write("Lastweek Unique User Count: "+ countLastWeekUnique);
		writer.write(System.getProperty( "line.separator" ));
		writer.write("Lastweek Total Login : " +countLastWeekTotalLogin);
		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");
		writer.write(System.getProperty( "line.separator" ));
		writer.write("Compared to last week same time");
		writer.write(System.getProperty( "line.separator" ));
		if (!compareUnique.equals ("Same"))
		{
			if (percentageUniLogin.equals("0.0"))
				writer.write(percentageUniLogin + " % (" + (int)diffUniqueLogin +" numbers) "+ compareUnique + " Unique User");
			else
			    writer.write(percentageUniLogin + " % " + compareUnique +" Unique User");
		}else{
			writer.write( "Same number of unique Users");
		}
		
		writer.write(System.getProperty( "line.separator" ));
		if (!compareTotal.equals ("Same")){
			
			if (percentageTotLogin.equals("0.0"))
				writer.write(percentageTotLogin + " % (" + (int)diffTotalLogin+" numbers) "+ compareTotal + " Total login");
			else
			    writer.write(percentageTotLogin + " % " + compareTotal +" Total login");

			
		}else{

			writer.write( "Same number of total logins");
		}
*/

		//writer.write(System.getProperty( "line.separator" ));
		//writer.write("Compare to last week same time");

/*		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");
		writer.write(System.getProperty( "line.separator" ));
		writer.write("Last Three Hours Traffic:");
		writer.write(" ");
		writer.write(System.getProperty( "line.separator" ));
		writer.write(sb1.toString());

		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");
		writer.write(System.getProperty( "line.separator" ));
*/

/*
                writer.write("Today's Traffic:");
                writer.write(" ");
                writer.write(System.getProperty( "line.separator" ));
                writer.write(sb2.toString());

                writer.write(System.getProperty( "line.separator" ));
                writer.write(" ");
                writer.write(System.getProperty( "line.separator" ));
*/
		// END of REMOVE OUTBOUND WRITER

		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");


		writer.write(" ");
		writer.write(System.getProperty( "line.separator" ));
		writer.write("INBOUND STATS:");
		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");	
		writer.write(System.getProperty( "line.separator" ));
		writer.write("Total Inbound Messages for Today: "+ String.valueOf(totalMessCount));
		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");
		writer.write(System.getProperty( "line.separator" ));
		writer.write("Inbound Messages for Last Three Hours: "+ String.valueOf(totalMessCount3hrs));
		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");
		writer.write(System.getProperty( "line.separator" ));
                writer.write("INBOUND STATS PER SRC-APP for last 3hrs");
		writer.write(System.getProperty( "line.separator" ));
		writer.write(sb3.toString());
		/* SOA persistence calculation starts here */

		percentageMess = (completed3hrmess/total3hrsmess)*100;
		String perTotalMess = String.valueOf(Math.round((percentageMess)*10)/10.0) ;
		String subPerTotalMess = new String(perTotalMess);
		int i =subPerTotalMess.indexOf('.');
		
				subPerTotalMess = subPerTotalMess.substring(0,i);
					
				if (subPerTotalMess.equals("0."))
				{
					subPerTotalMess = "0";
				}
		int intTotalMess = Integer.parseInt (subPerTotalMess);
		
		String inboundStatus = null;

		if (intTotalMess > 80)
		{
			inboundStatus = "Inbound persistence is ok" ;
			
		}else inboundStatus ="Inbound persistence is not ok";

		writer.write(System.getProperty( "line.separator" ));
		writer.write(" ");
		writer.write(System.getProperty( "line.separator" )); 
		writer.write(inboundStatus); 

		writer.flush();
		writer.close();
		}catch(IOException e){
			System.out.println("file writer Exception");
			e.printStackTrace();
		}

		System.out.println("Query Successfully executed and written into concerto stats txt file");
		try{
		System.out.println("Closed all conncetion");
			}finally{
				try{dbsoa.close();}catch(Exception e){}

		}
	
				
 }

 public static void main(String[] arg){

		Date dt1 = new Date();
        DateFormat formater = new SimpleDateFormat("ddMMyyyy'_'HHmm");

		System.out.println("Execute Main Function on "+ formater.format(dt1));
	
	try{
	 props.load(new FileReader(new File("./conf/com.nehr.persistence.ConcertoStats.properties")));
	}catch(Exception e){
		e.printStackTrace();
	}
    
	ConcertoStats concertostats = new ConcertoStats();
	//	System.out.println("Testing Concerto Connection...");
	//	concertostats.getConcertoDBConnectionDetails();
	System.out.println("Testing SOA Connection...");
	concertostats.getSOAConnectionDetails();
	System.out.println("Testing CDB Connection...");
	concertostats.getCDBDBConnectionDetails();
	//System.out.println("Testing ODS Connection...");
	//concertostats.getODSDBConnectionDetails();
	concertostats.getConcertoStats();
	//cocertostats.getPatientSearchTimeOut();
	System.out.println("Execution Completed:::");

 }

 }

