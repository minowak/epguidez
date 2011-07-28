package com.minowak.epguidez;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NadchodzaceSerialeActivity extends Activity implements Runnable {
	private ArrayList<String> odcinki, linki, seriale; 
	private ProgressDialog dialog;
	
	public void run() {
		znajdzOdcinki();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.przegladaj_seriale);
        
        odcinki = new ArrayList<String>();
        linki = new ArrayList<String>();
        
        Intent intent = getIntent();
        linki = intent.getExtras().getStringArrayList("linki");
        seriale = intent.getExtras().getStringArrayList("seriale");
        
        dialog = ProgressDialog.show(this, "Czekaj", "Szukam...", true, false);
        
        Thread thread = new Thread(this);
        thread.start();
         
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(NadchodzaceSerialeActivity.this, R.layout.list_item, odcinki);
	        
	     ListView lv = (ListView) findViewById(R.id.serialeListView);
	     lv.setAdapter(adapter);
    }
	
	private void znajdzOdcinki() {
		Iterator<String> iter = linki.iterator();
		Iterator<String> iter2 = seriale.iterator();
	
		try {
			while(iter.hasNext()) {
				String link = iter.next();
				String nazwa = iter2.next();
			
				ArrayList<String> odc = new ArrayList<String>();
				ArrayList<String> nad = new ArrayList<String>();
				new HTMLParser(link, odc, nad, this, dialog).write(link);
		
				Iterator<String> iter3 = nad.iterator();
				while(iter3.hasNext()) {
					String step = iter3.next();
					if(step.split("\\s+").length >= 2)
						odcinki.add(new String(step.split("\\s+")[1] + " - " + nazwa));
				}
			}
		} catch(Exception e) {
			// nothing to do
		} finally {
			handler.sendEmptyMessage(0);
		}
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			// co po zaladowaniu ?
			Comparator<String> cmp = new DateComparator();
			Collections.sort(odcinki, cmp);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(NadchodzaceSerialeActivity.this, R.layout.list_item, odcinki);
	        
	        ListView lv = (ListView) findViewById(R.id.serialeListView);
	        lv.setAdapter(adapter);
		}
	};
	
	public void anuluj(View view) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
}

class DateComparator implements Comparator<String> {
	public int compare(String str1, String str2) {
		String a = str1.split("\\s+")[0];
		String b = str2.split("\\s+")[0];
		
		String [] dates = a.split("/");
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
		String aired_date_a = dates[0] + "/" + dates[1] + "/" + dates[2];
		
		dates = b.split("/");
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
		String aired_date_b = dates[0] + "/" + dates[1] + "/" + dates[2];
		
		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		
		try {
			Date d1 = df.parse(aired_date_a);
			Date d2 = df.parse(aired_date_b);
			
			return d1.compareTo(d2);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
}
