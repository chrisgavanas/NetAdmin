package records;

import indexes.WifIndex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Interfaces.WifiWrapper;

public class WifiRecords 
{
//The data of the file wifi.csv saved in indexes and lists//
//------------------------------------------------------------------------------------------------------------------------------------//
	private String filepath;
	private HashMap<String,WifIndex> wifrecords;
	private ArrayList<WifiWrapper> filerecs;
//------------------------------------------------------------------------------------------------------------------------------------//
	
//------------------------------------------------------------------------------------------------------------------------------------//
	public   WifiRecords(String filepath) 
	{
		setFilepath(filepath);
		setFilerecs(parse());
		wifrecords=new HashMap<String,WifIndex>();
	}
//------------------------------------------------------------------------------------------------------------------------------------//
//Parse the file and returned to a list of records//
	private ArrayList<WifiWrapper> parse()
	{
		ArrayList<WifiWrapper> lista = new ArrayList<WifiWrapper>();
		try
		{
			File file=new File(filepath);
			Scanner scan = new Scanner(file);
			scan.nextLine();
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] parts = line.split("\t");
				lista.add(new WifiWrapper(parts[1],parts[2],parts[3],Integer.parseInt(parts[4]),Integer.parseInt(parts[5]),Double.parseDouble(parts[6]),Double.parseDouble(parts[7]),parts[8]));
			}
			scan.close();
			
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return lista;
	}
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
//Creates an index of the given key select if you want hashmap only Navigable map only or both//
	public void createIndex(String key, boolean hm)
	{
		 wifrecords.put(key,new WifIndex(key,filerecs,hm));
	}
//------------------------------------------------------------------------------------------------------------------------------------//
	
//------------------------------------------------------------------------------------------------------------------------------------//
	public WifIndex getIndex(String key)
	{
		return wifrecords.get(key);
	}
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
	public String getFilepath() 
	{
		return filepath;
	}
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
	public void setFilepath(String filepath) 
	{
		this.filepath = filepath;
	}
//------------------------------------------------------------------------------------------------------------------------------------//
	
//------------------------------------------------------------------------------------------------------------------------------------//
	public ArrayList<WifiWrapper> getFilerecs() 
	{
		return filerecs;
	}
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
	public void setFilerecs(ArrayList<WifiWrapper> filerecs) 
	{
		this.filerecs = filerecs;
	}
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
	public HashMap<String,WifIndex> getBsrecords() 
	{
		return wifrecords;
	}
//------------------------------------------------------------------------------------------------------------------------------------//

//------------------------------------------------------------------------------------------------------------------------------------//
	public void setBsrecords(HashMap<String,WifIndex> bsrecords) 
	{
		this.wifrecords = bsrecords;
	}
//------------------------------------------------------------------------------------------------------------------------------------//
}
