package com.minowak.epguidez;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class UsunSerialActivity extends Activity {
	public ArrayList<String> seriale;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.przegladaj_seriale);
        
        Intent intent = getIntent();
        seriale = intent.getExtras().getStringArrayList("seriale");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, seriale);
        ListView lv = (ListView) findViewById(R.id.serialeListView);
        lv.setAdapter(adapter);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	Intent resultIntent = new Intent();
            	resultIntent.putExtra("tytul", ((TextView)view).getText().toString());
            	setResult(Activity.RESULT_OK, resultIntent);
            	finish();
            }
          });
	}
	
	public void anuluj(View view) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
}
