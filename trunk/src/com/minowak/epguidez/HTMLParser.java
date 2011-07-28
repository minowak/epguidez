package com.minowak.epguidez;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

public class HTMLParser extends Thread {
	private ProgressDialog _pd;
	private Activity parent;
	private ArrayList<String> _dane;
	private String _name;
	private ArrayList<String> _nad;
	
	private String parseLine(String line) {
		String temp = new String("");
		boolean tag = false;
		for(int i = 0 ; i < line.length() ; i++) {
			if(line.charAt(i) == '<'  || (line.charAt(i) == '&' && line.charAt(i+1) == 'b'))
				tag = true;
			else
			if(line.charAt(i) == '>'  || line.charAt(i) == '\n')
				tag = false;
			else
			if(!tag)
				temp = temp + line.charAt(i);
		}
		//usuwanie [recap] i [trailer] oraz spacji
		String result = new String("");
		tag = false;
		boolean space = false;
		boolean skipSpace = false;
		boolean skipHash = false;

		for(int i = 0 ; i < temp.length() ; i++) {
			if(space && temp.charAt(i) == ' ') {
				skipSpace = true;
				if(!skipHash) {
					result = result + "#";
					skipHash = true;
				}
			}
			else {
				skipSpace = false;
				skipHash = false;
			}
			
			if(temp.charAt(i) == '[')// || temp.charAt(i) == '&')
				tag = true;
			else
			if(tag && temp.charAt(i) == ']')// || temp.charAt(i) == '\n')
				tag = false;
			else
			if(!tag && !skipSpace) {
				result = result + temp.charAt(i);
			}
			if(!tag && temp.charAt(i) == ' ')
				space = true;
			else space = false;
		}
		result = result.trim();
		
		if(result.length() == 0 || result.startsWith("Other")) return null;
	//	String result2 = new String("");
		// ponizsze przez pomylke usuwalo daty w niektorych wierszach 
	/*	if(lgth == 4) {
			int l = 0;
			boolean skip = false;
			// usuwanie prod #
			for(int i = 0 ; i < result.length() ; i++) {
				if(result.charAt(i) == '#') 
					if(++l == 2)
						skip = true;
					else skip = false;
				if(!skip)
					result2 = result2 + result.charAt(i);
			}
		} else return result;*/
		
		//System.out.println(result);
		return result;
	}
	
	public void write (String name) {
		try {
			URL url = new URL("http://epguides.com/" + name + "/");
			URLConnection connection = url.openConnection();
			DataInputStream in = new DataInputStream(connection.getInputStream());
			// tu mam tresc strony w HTML
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			
			boolean startParsing = false;
			
			while(r.ready()) {
				
				String line = r.readLine();
				if(line.equals("<div id=\"eplist\">")) {
					startParsing = true;
					for(int i = 0 ; i < 6 ; i++)
						line = r.readLine();
				}
				else
				if(startParsing) {
					if(line.equals("</div>") || line.startsWith("Other"))
						startParsing = false;
					else {
						String tmp = parseLine(line);
						
						if(tmp != null) {//fw.write(tmp + "\n");
							String [] pola = tmp.split("#");
							String odcinek = null, data = null, nazwa = null;
							if(pola.length == 4) {
								odcinek = pola[1];
								data = pola[2];
								nazwa = pola[3];
							} else
							if(pola.length == 5) {
								odcinek = pola[1];
								data = pola[3];
								nazwa = pola[4];
							}
							
							_dane.add(odcinek + " " + data + " " + nazwa);
							if(_nad != null) {
								if(aired(data)) {
									_nad.add(odcinek + " " + data + " " + nazwa);
								}
							}
						}
					}
				}
			}
		//	fw.close();
		} catch(Exception e) {		
			Toast.makeText(parent, e.toString(),
			Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        
        return dateFormat.format(date);
    }
	
	public HTMLParser(String name, ArrayList<String> dane, ArrayList<String> nad, Activity parent, ProgressDialog pd) {
		_name = name;
		_dane = dane;
		this.parent = parent;
		_nad = nad;
		_pd = pd;
		//write(_name);
	}
	
	public void run() {
		write(_name);
		_pd.dismiss();
	}
	
	private boolean aired(String date) {
		date = date.trim();
		if(date.startsWith("UNAIRED")) return false;
		String [] dates = date.split("/");
		if(dates[1].equals("Jan")) dates[1] = "01"; else
		if(dates[1].equals("Feb")) dates[1] = "02"; else
		if(dates[1].equals("Mar")) dates[1] = "03"; else
		if(dates[1].equals("Apr")) dates[1] = "04"; else
		if(dates[1].equals("May")) dates[1] = "05"; else
		if(dates[1].equals("Jun")) dates[1] = "06"; else
		if(dates[1].equals("Jul")) dates[1] = "07"; else
		if(dates[1].equals("Aug")) dates[1] = "08"; else
		if(dates[1].equals("Sep")) dates[1] = "09"; else
		if(dates[1].equals("Oct")) dates[1] = "10"; else
		if(dates[1].equals("Nov")) dates[1] = "11"; else
		if(dates[1].equals("Dec")) dates[1] = "12";
		String aired_date = dates[0] + "/" + dates[1] + "/" + dates[2];
		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		try {
			Date date1 = df.parse(aired_date);
			Date date2 = df.parse(getDate());
			//System.out.println("Date1 = " + date1 + " Date2 = " + date2);
			if(date1.compareTo(date2) > 0) return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}


