package com.minowak.epguidez;

import java.util.ArrayList;
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
			//odcinki
			handler.sendEmptyMessage(0);
		}
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			// co po zaladowaniu ?
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
