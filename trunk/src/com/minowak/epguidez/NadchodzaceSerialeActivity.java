package com.minowak.epguidez;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NadchodzaceSerialeActivity extends Activity {
	private ArrayList<String> odcinki, linki, seriale; 
	private ProgressDialog dialog;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.przegladaj_seriale);
        
        odcinki = new ArrayList<String>();
        linki = new ArrayList<String>();
        
        Intent intent = getIntent();
        linki = intent.getExtras().getStringArrayList("linki");
        seriale = intent.getExtras().getStringArrayList("seriale");
        
        znajdzOdcinki();
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, odcinki);
        
        ListView lv = (ListView) findViewById(R.id.serialeListView);
        lv.setAdapter(adapter);
    }
	
	private void znajdzOdcinki() {
		Iterator<String> iter = linki.iterator();
		Iterator<String> iter2 = seriale.iterator();
				
		while(iter.hasNext()) {
			String link = iter.next();
			String nazwa = iter2.next();
			
			ArrayList<String> odc = new ArrayList<String>();
			ArrayList<String> nad = new ArrayList<String>();
			new HTMLParser(link, odc, nad, this, dialog).write(link);
		
			Iterator<String> iter3 = nad.iterator();
			while(iter3.hasNext()) {
				String step = iter3.next();
				odcinki.add(new String(nazwa + " " + step.split("\\s+")[1]));
			}
		}
	}
	
	public void anuluj(View view) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
}
