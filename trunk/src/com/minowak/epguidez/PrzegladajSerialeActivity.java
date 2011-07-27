package com.minowak.epguidez;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PrzegladajSerialeActivity extends Activity {
	private ArrayList<String> seriale;
	private ArrayList<String> linki;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.przegladaj_seriale);
        
        Intent intent = getIntent();
        seriale = intent.getExtras().getStringArrayList("seriale");
        linki = intent.getExtras().getStringArrayList("linki");
        
        ListView serialeListView = (ListView) findViewById(R.id.serialeListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, seriale);
        serialeListView.setAdapter(adapter);
        
        ListView lv = (ListView) findViewById(R.id.serialeListView);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	ProgressDialog dialog = ProgressDialog.show(PrzegladajSerialeActivity.this, "", 
                        "Ladowanie...", true);
            	
            	ArrayList<String> dane = new ArrayList<String>();
            	
            	Iterator<String> iter1 = seriale.iterator();
            	Iterator<String> iter2 = linki.iterator();
            	
            	String link = null;
            	while(iter1.hasNext() && iter2.hasNext()) {
            		String str1 = iter1.next();
            		link = iter2.next();
            		
            		if( ((TextView)view).getText().toString().equals(str1))
            			break;
            	}
            	if(link != null) {
            		new HTMLParser( link, dane, null, PrzegladajSerialeActivity.this, dialog).write(link);
            	}
            	
            	Intent intent = new Intent(PrzegladajSerialeActivity.this.getApplicationContext(), SzczegolySerialuActivity.class);
            	intent.putExtra("dane", dane);
            	startActivity(intent);
            	dialog.cancel();
            	
            }
          });
    }
	
	public void anuluj(View view) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
}
