package com.minowak.epguidez;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class DodajSerialActivity extends Activity {
	// TODO uzupelnic
	static final String[] SERIALE = { 
				"Futurama", "Big Bang Theory", "How I Met Your Mother", "House M.D.", "Californication", "Weeds", "Chuck", 
				"South Park", "Family Guy", "Breaking Bad", "Mad Men", "True Blood", "The Walking Dead", "Entourage",
				"Skins", "24h"};
	static final String[] LINKS = {
				"futurama", "bigbangtheory", "howimetyourmother", "house", "californication", "weeds", "chuck",
				"southpark", "familyguy", "breakingbad", "madmen", "trueblood", "walkingdead", "entourage", "skins", "24"};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_serial);
        
        AutoCompleteTextView tytulySeriali = (AutoCompleteTextView) findViewById(R.id.serialeAutoComplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, SERIALE );
       tytulySeriali.setAdapter(adapter);
    }
	
	public void anuluj(View view) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
	
	public void dodaj(View view) {
		AutoCompleteTextView tytul = (AutoCompleteTextView) findViewById(R.id.serialeAutoComplete);
		Intent resultIntent = new Intent();
		
		int index = 0;
		for(int i = 0 ; i < SERIALE.length ; i++) {
			if(SERIALE[i].equals(tytul.getText().toString())) {
				index = i;
				break;
			}
		}
		
		resultIntent.putExtra("tytul", tytul.getText().toString());
		resultIntent.putExtra("link", LINKS[index]);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
