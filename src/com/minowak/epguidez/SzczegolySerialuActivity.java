package com.minowak.epguidez;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SzczegolySerialuActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.przegladaj_seriale);
        
        ListView lv = (ListView) findViewById(R.id.serialeListView);
        ArrayList<String> dane;
        Intent intent = getIntent();
        dane = intent.getExtras().getStringArrayList("dane");
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, dane);
        lv.setAdapter(adapter);
	}
	
	public void anuluj(View view) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
}
