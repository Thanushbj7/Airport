import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
	import java.util.zip.ZipInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
	import java.util.zip.ZipEntry;
	import java.util.zip.ZipInputStream;


public class CopyTableExtract {



	
	
	 public static void main(String[] args) {
	        // Specify the folder containing the zip files
	        String folderPath = "C:/Users/Windows/Downloads/zipfiles";
	        // Specify the path for the Excel file
	        String excelFilePath = "C:/Excel/file.xlsx";

	        // Call the method to unzip files, read logs, and create the Excel file
	        unzipAndReadLogsAndCreateExcel(folderPath, excelFilePath);
	    }

	 private static void unzipAndReadLogsAndCreateExcel(String folderPath, String excelFilePath) {
	        File folder = new File(folderPath);
	        File[] zipFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".zip"));

	        if (zipFiles != null) {
	            try (Workbook workbook = new XSSFWorkbook()) {
	                // Create a new sheet
	                Sheet sheet = workbook.createSheet("Sheet1");

	                // Create headers
	                Row headerRow = sheet.createRow(0);
	                String[] headers = {"RUN_DATE", "RUN_CYCLE", "TABLE_NAME", "WEEKLY_DAILY", "RECORD_COUNT", "START_DATE", "END_DATE"};
	                for (int i = 0; i < headers.length; i++) {
	                    Cell cell = headerRow.createCell(i);
	                    cell.setCellValue(headers[i]);
	                }
//Map<String,Integer> zipFileNameCount;
	               // Map<String, Integer> zipFileNameCount = countOccurrences(zipFiles, targetString);
	                int rowNum = 1; // Start writing data from the second row

	                for (File zipFile : zipFiles) {
	                	 String zipFileName = zipFile.getName();
	                	//zipFileNameCount=new HashMap<>();
	                	// if (zipFileName.contains(targetString)) {
	                      //   int runCycleCount = zipFileNameCount.get(zipFileName);

	                    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
	                    //tring ZipFileName=ZipFile.getName();
	                   //if(!zNameCount.containsKey(zipFileName))
	                        byte[] buffer = new byte[1024];
	                        ZipEntry zipEntry = zis.getNextEntry();
	                        
//String zipFileName=zipFile.getName();
//if(zipFileName != null){
//if(!zipFileNameCount.containsKey(zipFileName)){
//zipFileNameCount.put(zipFileName,1);
//}else{
	//int runCycleCount=zipFileNameCount.get(zipFileName);
	//zipFileNameCount.put(zipFileName,runCycleCount+1);
//}
//int runCycleCount=zipFileNameCount.get(zipFileName);

	                        while (zipEntry != null) {
	                            String entryName = zipEntry.getName();
	                            File newFile = new File(folder, entryName);

	                            // Create directories if they do not exist
	                            if (zipEntry.isDirectory()) {
	                                newFile.mkdirs();
	                            } else {
	                                // Create parent directories for non-directory entries
	                                new File(newFile.getParent()).mkdirs();

	                                // Extract the file
	                                try (FileOutputStream fos = new FileOutputStream(newFile)) {
	                                    int len;
	                                    while ((len = zis.read(buffer)) > 0) {
	                                        fos.write(buffer, 0, len);
	                                    }
	                                }

	                                // Check if the entry is a log file and does not contain "Truncate" in its name
	                                if (entryName.toLowerCase().endsWith(".log") && !entryName.toLowerCase().contains("truncate")&& !entryName.toLowerCase().contains("email_report") && !entryName.toLowerCase().contains("TaccountInfo")) {
	                                    // Process the log file and extract date and time
	                                	//String lastLine=null;
	                                    Date runDate = extractDateFromLogFile(newFile);
                                       //int recordCount=extractNumberBeforeSuccessful(lastLine);
	                                    // Map table name based on file name
	                                    String tableName = mapTableName(newFile.getName());

	                                    // Extract start date from the line containing "Decrypting..."
	                                    Date startDate = extractStartDate(newFile);
                                         Date endDate=extractEndDate(newFile,sheet,rowNum);
                                         String weeklyDaily = mapWeeklyDaily(newFile.getName());
                                        // Row row=sheet.getRow(rowNum);
                                         //if(runCycleCount != 0){
                                        // Cell runCycleCell=row.createCell(1);
                                        // if(runCycleCell != null){
                                        // runCycleCell.setCellValue(runCycleCount);
                                       //  }
                                        // }
	                                    // Add log data to Excel file
                                         BufferedReader bufferReader=new BufferedReader(new FileReader(newFile));
                                         String lastLine = null,line;
                                         String keyword="successful";
                                        // int recordCount=0;
                                         if(bufferReader !=null){
                                        	 while((line=bufferReader.readLine()) !=null){
                                        		 lastLine=line;
                                        	 }
                                        	 
                                        	
                                        	 
                                         }
                                        int recordCount=extractNumberBeforeSuccessful(lastLine,keyword);
	                                    addLogDataToExcel(sheet, rowNum++, runDate, tableName, startDate,endDate,weeklyDaily,recordCount);
	                                }
	                                }
	                          //  }

	                            // Move to the next entry
	                            zipEntry = zis.getNextEntry();
	                        }

	                        System.out.println("Unzipped and processed logs: " + zipFile.getName());

	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                	 }
	                //}
	                

	                // Write the workbook to a file
	                try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
	                    workbook.write(fileOut);
	                    System.out.println("Excel file created successfully at: " + excelFilePath);
	                }

	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("No zip files found in the specified folder.");
	        }
	    }
private static Date extractEndDate(File logFile,Sheet sheet,int rowNum){
	try(RandomAccessFile randomAccessFile=new RandomAccessFile(logFile,"r")){
		long length=randomAccessFile.length();
		if(length==0){
			return null;
		}
		long position=length-1;
		randomAccessFile.seek(position);
		while(position > 0 && randomAccessFile.readByte() !='\n'){
			position--;
			randomAccessFile.seek(position);
		}
			byte[] bytes=new byte[(int) (length-position)];
			randomAccessFile.read(bytes);
			String lastLine=new String(bytes).trim();
			BufferedReader bufferReader=new BufferedReader(new FileReader(logFile));
			String last,line;
			if(bufferReader!=null){
				while((line=bufferReader.readLine()) !=null){
					lastLine=line;
				}
			}
			String keyword="successful";
			int recordCountCell=extractNumberBeforeSuccessful(lastLine,keyword);
			//if(recordCountCell!=-1){
				Row row=sheet.getRow(rowNum);
				//Cell setRecordCount=row.createCell(4);
				//setRecordCount.setCellValue(recordCountCell);
				//System.out.println("Extracted number before 'successful':"+recordCountCell);
			//}else{
				//System.out.println("No number found before 'successful'in the line.");
			//}
			if(!lastLine.isEmpty()){
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try{
				Date endDate=dateFormat.parse(lastLine);
				//int recordCount=extractRecordCount(lastLine);
				
				//Cell endDateCell=row.createCell(6);
				//endDateCell.setCellValue(endDate);
				
				return endDate;
			}catch(ParseException e){
				System.out.println("Error parsing end date from log file:"+logFile.getName());
				e.printStackTrace();
			}
		}
	}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	
}
private static int extractNumberBeforeSuccessful(String lastLine, String keyword){
	String regex="(\\d+)\\s*successful";
	Pattern pattern=Pattern.compile(regex);
	Matcher matcher=pattern.matcher(lastLine);
	if(matcher.find()){
		String numberStr=matcher.group(1);
		try{
			return Integer.parseInt(numberStr);
			
		}catch(NumberFormatException e){
			System.err.println("Error parsing number:"+numberStr);
			e.printStackTrace();
		}
	}
	  return 0;
}
private static Map<String, Integer> countOccurrences(File[] zipFiles, String targetString) {
    Map<String, Integer> zipFileNameCount = new HashMap<>();
    
    for (File zipFile : zipFiles) {
        String zipFileName = zipFile.getName();
        
        // Check if the target string is present in the zip file name
        if (zipFileName.contains("Copy_Tables_extract_log_files")) {
            zipFileNameCount.put(zipFileName, zipFileNameCount.getOrDefault(zipFileName, 0) + 1);
        }else if(zipFileName.contains("trade_review_extract_log_files")) {
            zipFileNameCount.put(zipFileName, zipFileNameCount.getOrDefault(zipFileName, 0) + 1);
        }else if(zipFileName.contains("EblotterExtract__log_files")) {
            zipFileNameCount.put(zipFileName, zipFileNameCount.getOrDefault(zipFileName, 0) + 1);
        }else if(zipFileName.contains("SFDC_History")) {
            zipFileNameCount.put(zipFileName, zipFileNameCount.getOrDefault(zipFileName, 0) + 1);
        }else if(zipFileName.contains("sfdcEBlotter")) {
            zipFileNameCount.put(zipFileName, zipFileNameCount.getOrDefault(zipFileName, 0) + 1);
        }
    }

    return zipFileNameCount;
}

private static int extractRecordCount(String lastLine){
	String[] parts=lastLine.split("\\D+");
	if(parts.length>0){
		return Integer.parseInt(parts[parts.length-1]);
	}else{
		return 0;
	}
}
	    private static void readLogFile(File logFile) {
	        // Read and process the log file as needed
	        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                // Process each line of the log file
	                System.out.println(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    private static Date extractDateFromLogFile(File logFile) {
	        // Read and process the first line of the log file to extract date and time
	        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
	            String firstLine = reader.readLine();
	            if (firstLine != null) {
	                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                try {
	                    return dateFormat.parse(firstLine);
	                } catch (ParseException e) {
	                    System.out.println("Error parsing date from log file: " + logFile.getName());
	                    e.printStackTrace();
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    private static Date extractStartDate(File logFile) {
	        // Read and process the lines of the log file to extract date and time from the line containing "Decrypting..."
	        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
	            String line;
	            boolean decryptingFound=false;
	            while ((line = reader.readLine()) != null) {
	                if (decryptingFound) {
	                	//if(line.length()>=32){
	                		//String dateString=line.substring(0, 13).trim();
	                	//}
	                    // Extract date and time from the line
	                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Adjust the format based on your log
	                    try {
	                        // Assuming the date and time start at index 13 and end at index 32
	                        return dateFormat.parse(line.trim());
	                    } catch (ParseException e) {
	                        System.out.println("Error parsing start date from log file: " + logFile.getName());
	                        e.printStackTrace();
	                    }
	                }
	                if (line.contains("Decrypting...")){
	                	decryptingFound=true;
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }private static String mapWeeklyDaily(String logFileName) {
	        // Map "W" to WEEKLY_DAILY if the logfile name contains certain strings, otherwise map as "D"
	        if (logFileName.toLowerCase().contains("sfdcfinancialaccountextractprocess") ||
	            logFileName.toLowerCase().contains("sfdcfinancialaccountteamextractprocess")) {
	            return "W";
	        } else {
	            return "D";
	        }
	    }
	    private static String mapTableName(String fileName) {
	        // Map the table name based on the file name
	    	if (fileName.toLowerCase().contains("sfdcregistrationmapextractprocess")) {
	            return "SFDC_W_TR_REGISTRATION_MAP";
	        }else if (fileName.contains("sfdcSbrLetterLogRelExtractProcess")) {
	            return "SBR_W_REG_LETTER_LOG_REL_SFDC";
	        }else if (fileName.contains("sfdcSponserNamesExtractProcess")) {
	            return "SFDC_W_SPONSOR_NAMES";
	        }else if (fileName.contains("sfdcClientExtractProcess")) {
	            return "SFDC_W_CLIENT";
	        }else if (fileName.contains("sfdcRegistrationExtractProcess")) {
	            return "SFDC_W_REGISTRATION";
	        }else if (fileName.contains("oracleEblotterExtractProcess")) {
	            return "SFDC_EBLOTTER";
	        }else if (fileName.contains("sfdcEBlottereBlotterExtractProcess")) {
	            return "SFDC_EBLOTTER";
	        }else if (fileName.contains("sfdcRegMemberExtractProcess")) {
	            return "SFDC_W_REGISTRATION_MEMBERS";
	        }else if (fileName.contains("sfdcRegBeneficiaryExtractProcess")) {
	            return "SFDC_W_BENEFICIARY";
	        }else if (fileName.contains("sfdcClientDisclosureExtractProcess")) {
	            return "SFDC_W_CLIENT_DISCLOSURE";
	        }else if (fileName.contains("sfdcPortfolioReviewExtractProcess")) {
	            return "SFDC_W_PORTFOLIO_REVIEW";
	        }else if (fileName.contains("SFDCHistoryAccountHistoryExtract")) {
	            return "SBR_ACCOUNT_HISTORY_SFDC";
	        }else if (fileName.contains("SFDCHistoryRegClientmemberHistoryExtract")) {
	            return "SBR_REG_MEMBER_HISTORY_SFDC";
	        }else if (fileName.contains("SFDCHistoryRegistrationHistoryExtract")) {
	            return "SBR_REGISTRATION_HISTORY_SFDC";
	        }else if (fileName.contains("SFDCHistoryRegistrationLogExtract")) {
	            return "SBR_REG_LETTER_LOG_SFDC";
	        }else if (fileName.contains("SFDCHistoryRegistrationLogtable_T2_Extract")) {
	            return "SBR_REG_LETTER_LOG_T2_SFDC";
	        }else if (fileName.contains("sfdcEBlotterChecksExtractProcess")) {
	            return "SFDC_CHECKS";
	        }else if (fileName.contains("sfdcEBlotterTradesExtractProcess")) {
	            return "SFDC_TRADES";
	        }else if (fileName.toLowerCase().contains("sfdc_emailload_log")) {
	            return "SFDC_USER";
	        }else if (fileName.contains("sfdcFinancialAccountExtractProcess")) {
	            return "SFDC_W_FINANCIAL_ACCOUNT";
	        }else if (fileName.contains("sfdcFinancialAccountTeamExtractProcess")) {
	            return "SFDC_W_FINANCIAL_ACCOUNT_TEAM";
	        }
	        // Add more conditions if needed for other file names
	        // Example: if (fileName.contains("someOtherPattern")) return "SomeOtherTable";

	        // Default to an empty string if no mapping is found
	        return "";
	    }

	    private static void addLogDataToExcel(Sheet sheet, int rowNum, Date runDate, String tableName,Date startDate,Date endDate,String weeklyDaily,int recordCount) {
	        Row row = sheet.createRow(rowNum);

	        // Add the extracted run date to the RUN_DATE column
	        Cell cellRunDate = row.createCell(0);
	        if (runDate != null) {
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            cellRunDate.setCellValue(dateFormat.format(runDate));
	       
	        }
	       // Row row=sheet.getRow(rowNum);
	       // Cell cellLastLine=row.createCell(4);
	       // if(recordCount!=null){
	        //	cellLatLine.setCellValue(recordCount);
	       // }
	        

	        // Add the mapped table name to the TABLE_NAME column
	        Cell cellTableName = row.createCell(2);
	        cellTableName.setCellValue(tableName);
	        Cell cellStartDate=row.createCell(5);
	        Cell cellWeeklyDaily=row.createCell(3);
	        Cell setRecordCount=row.createCell(4);
	        cellWeeklyDaily.setCellValue(weeklyDaily);
	        if(startDate!=null){
	        	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        	
	        	cellStartDate.setCellValue(dateFormat.format(startDate));
	        }
	        Cell cellEndDate=row.createCell(6);
	         if(endDate!=null){
	        	SimpleDateFormat dateFormate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        	cellEndDate.setCellValue(dateFormate.format(endDate));
	        }setRecordCount.setCellValue(recordCount);
	         
	    }
	}