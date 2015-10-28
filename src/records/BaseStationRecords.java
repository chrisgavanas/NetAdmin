package records;

import indexes.BsIndex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Interfaces.BaseStationWrapper;

public class BaseStationRecords {
	// The data of the file basestation.csv saved in indexes and lists//
	// ------------------------------------------------------------------------------------------------------------------------------------//
	private String filepath;
	private HashMap<String, BsIndex> bsrecords;
	private ArrayList<BaseStationWrapper> filerecs;

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public BaseStationRecords(String filepath) {
		setFilepath(filepath);
		setFilerecs(parse());
		bsrecords = new HashMap<String, BsIndex>();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	// Creates an index of the given key select if you want hashmap only
	// Navigable map only or both//
	public void createIndex(String key, boolean hm) {
		bsrecords.put(key, new BsIndex(key, filerecs, hm));
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public HashMap<String, BsIndex> getBsrecords() {
		return bsrecords;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public void setBsrecords(HashMap<String, BsIndex> bsrecords) {
		this.bsrecords = bsrecords;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public String getFilepath() {
		return filepath;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	// Parse the file and returned to a list of records//
	private ArrayList<BaseStationWrapper> parse() {
		ArrayList<BaseStationWrapper> lista = new ArrayList<BaseStationWrapper>();
		try {
			File file = new File(filepath);
			Scanner scan = new Scanner(file);
			scan.nextLine();
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] parts = line.split("\t");
				if (parts[7].equals("No Latitude")
						|| parts[8].equals("No Longitude")
						|| parts[7].equals("No latitude yet")
						|| parts[8].equals("No longitude yet"))
					lista.add(new BaseStationWrapper(parts[1], parts[2],
							Integer.parseInt(parts[3]), Integer
									.parseInt(parts[4]), Long
									.parseLong(parts[5]), Integer
									.parseInt(parts[6]), 666, 666, parts[9]));
				else
					lista.add(new BaseStationWrapper(parts[1], parts[2],
							Integer.parseInt(parts[3]), Integer
									.parseInt(parts[4]), Long
									.parseLong(parts[5]), Integer
									.parseInt(parts[6]), Double
									.parseDouble(parts[7]), Double
									.parseDouble(parts[8]), parts[9]));
			}
			scan.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return lista;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public BsIndex getIndex(String key) {
		return bsrecords.get(key);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public ArrayList<BaseStationWrapper> getFilerecs() {
		return filerecs;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public void setFilerecs(ArrayList<BaseStationWrapper> filerecs) {
		this.filerecs = filerecs;
	}
	// ------------------------------------------------------------------------------------------------------------------------------------//
}
