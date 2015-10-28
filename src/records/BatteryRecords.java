package records;

import indexes.BatIndex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Interfaces.BatteryWrapper;

public class BatteryRecords {
	// The data of the file battery.csv saved in indexes and lists//
	// ------------------------------------------------------------------------------------------------------------------------------------//
	private String filepath;
	private HashMap<String, BatIndex> batrecords;
	private ArrayList<BatteryWrapper> filerecs;

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public BatteryRecords(String filepath) {
		setFilepath(filepath);
		setFilerecs(parse());
		batrecords = new HashMap<String, BatIndex>();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	// Parse the file and returned to a list of records//
	private ArrayList<BatteryWrapper> parse() {
		ArrayList<BatteryWrapper> lista = new ArrayList<BatteryWrapper>();
		try {
			File file = new File(filepath);
			Scanner scan = new Scanner(file);
			scan.nextLine();
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] parts = line.split("\t");
				lista.add(new BatteryWrapper(parts[1], Integer
						.parseInt(parts[2]), Integer.parseInt(parts[3]),
						Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
						parts[6]));
			}
			scan.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return lista;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	// Creates an index of the given key select if you want hashmap only
	// Navigable map only or both//
	public void createIndex(String key, boolean hm) {
		batrecords.put(key, new BatIndex(key, filerecs, hm));
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public BatIndex getIndex(String key) {
		return batrecords.get(key);
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
	public ArrayList<BatteryWrapper> getFilerecs() {
		return filerecs;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public void setFilerecs(ArrayList<BatteryWrapper> filerecs) {
		this.filerecs = filerecs;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public HashMap<String, BatIndex> getBatrecords() {
		return batrecords;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------//

	// ------------------------------------------------------------------------------------------------------------------------------------//
	public void setBatrecords(HashMap<String, BatIndex> batrecords) {
		this.batrecords = batrecords;
	}
	// ------------------------------------------------------------------------------------------------------------------------------------//
}
