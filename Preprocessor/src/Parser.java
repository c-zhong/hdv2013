import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.LogStream;
import java.util.HashMap;
import java.util.Iterator;

import au.com.bytecode.opencsv.CSVReader;


public class Parser {
	
	static int logStartDate = 1;
	static int logStartHour = 8;
	
	static String fileName = "E:\\VAST 2013\\bbexport-wiz2 - Copy.csv";
	
	public static void main(String[] args) {
		
		countHighStatusPerHour();
		
		/*
		for(int i = 1; i < 10; i++){
			
			outputHourMat(i);
		}
		*/
	}
	
	private static void outputHourMat(int hourID){
		int hostCount = 0;
		try 
		{

			CSVReader reader = new CSVReader(new FileReader(fileName));
			
			String outFile = "E:\\VAST 2013\\HourMat" + hourID + ".dat";   // the file name 
			File file = new File(outFile);
			FileWriter  fos = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fos);
			
			String[] nextLine;
			reader.readNext();
			
			int curHour = -1;
			int hourCount = 1;
			
			HashMap<String, HostInfo> HostMap = new HashMap<String, HostInfo>();
			
			while ((nextLine = reader.readNext()) != null) {
				
				String timeStr1 = nextLine[13];
				
				String servName = nextLine[2];
				
				String[] strs = timeStr1.split(" ");
				
				int status = Integer.parseInt(nextLine[4].trim());
				
				int hour = Integer.parseInt(strs[1].split(":")[0]);
				
				if(curHour == -1)
					curHour = hour;
				
				if(curHour != hour){
					
					curHour = hour;
					hourCount++;

				}
				
				if(curHour != hourID)
					continue;
				
				if(!HostMap.containsKey(nextLine[1])){
					HostInfo hostInfo = new HostInfo();
					
					if(servName.equalsIgnoreCase("cpu"))
						hostInfo.cpu = status;
					else if(servName.equalsIgnoreCase("conn"))
						hostInfo.conn = status;
					else if(servName.equalsIgnoreCase("disk"))
						hostInfo.disk = status;
					else if(servName.equalsIgnoreCase("pagefile"))
						hostInfo.pagefile = status;
					else if(servName.equalsIgnoreCase("mem"))
						hostInfo.mem = status;
					else if(servName.equalsIgnoreCase("stmp"))
						hostInfo.stmp = status;
					
					HostMap.put(nextLine[1], hostInfo);
					
					hostCount++;
				}

			}
			
			outputHourHostInfo(HostMap, bw);
			
			reader.close();
			bw.close();
		}catch(Exception e){
		    e.printStackTrace();
		}
		
		System.out.println(hostCount);
	}
	
	private static void outputHourHostInfo(HashMap<String, HostInfo> map, BufferedWriter bw){
		
		java.util.Iterator<String> itr = map.keySet().iterator();
		
		try {
		
		int itemCount = 0;
		
		while(itr.hasNext()){
			HostInfo info = map.get(itr.next());
			
			
				bw.write(info.cpu + "\t" + info.mem + "\t" + info.disk + "\t" + info.pagefile + "\t" + info.stmp + "\t" + info.conn);
				
				itemCount++;
				if(itemCount == 12){
					bw.write('\n');
					itemCount = 0;
				}else{
					bw.write('\t');
				}
				
				

			
		}
		
		for(int i = itemCount; i < 12; i++){
			bw.write("1\t1\t1\t1\t1\t1\t");
		}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void countHighStatusPerHour(){
		
		int count = 0;
		
		try 
		{
			String fileName = "D:\\VAST 2013\\bbexport-wiz2 - Copy.csv";
			
			CSVReader reader = new CSVReader(new FileReader(fileName));
			
			String outFile = "D:\\VAST 2013\\levelCountHour.dat";   // the file name 
			File file = new File(outFile);
			FileWriter  fos = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fos);
			
			String[] nextLine;
			reader.readNext();
			
			int maxHourID = Integer.MIN_VALUE;
			int minHourID = Integer.MAX_VALUE;
			
			int[] hourLevel2Count = new int[24 * 15];
			int[] hourLevel3Count = new int[24 * 15];
			int[] hourLevel4Count = new int[24 * 15];
			int[] hourLevel5Count = new int[24 * 15];
			
			while ((nextLine = reader.readNext()) != null) {
				
				int status = Integer.parseInt(nextLine[4].trim());
				
				String timeStr1 = nextLine[13];
				
				String[] strs = timeStr1.split(" ");
				
				String[] dateStrs = strs[0].split("-");
				
				int date = Integer.parseInt(dateStrs[2]);
				
				int hour = Integer.parseInt(strs[1].split(":")[0]);
				
		        int hourID = getHourID(date, hour);
		        
		        if(status == 2)
		        	hourLevel2Count[hourID]++;
		        else if(status == 3)
		        	hourLevel3Count[hourID]++;
		        else if(status == 4)
		        	hourLevel4Count[hourID]++;
		        else if(status == 5)
		        	hourLevel5Count[hourID]++;
		        
		        if(hourID > maxHourID)
		        	maxHourID = hourID;
		        else if(hourID < minHourID)
		        	minHourID = hourID;
		        
			}
			reader.close();
			
			System.out.println("Min = " + minHourID);
			System.out.println("Max = " + maxHourID);
			for(int i = minHourID; i <= maxHourID; i++)
				bw.write(i + "\t" + hourLevel2Count[i] + "\t" + hourLevel3Count[i] + "\t" + hourLevel4Count[i] + "\n");
			
			
			bw.close();
		}catch(Exception e){
		    e.printStackTrace();
		}
		
		System.out.println(count);
		
	}
	
	private static int getHourID(int date, int hour){
		
		return (date - logStartDate) * 24 + (hour - logStartHour);
		
	}
	
}

class HostInfo{
	
	int cpu = 1;
	int disk = 1;
	int mem = 1;
	int conn = 1;
	int pagefile = 1;
	int stmp = 1;
	
	
}
