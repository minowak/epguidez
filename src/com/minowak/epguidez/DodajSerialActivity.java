package com.minowak.epguidez;

import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DodajSerialActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_serial);
        
    }
	
	public void anuluj(View view) {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
	
	public void dodaj(View view) {
		EditText tytul = (EditText) findViewById(R.id.serialeAutoComplete);
		EditText linki = (EditText) findViewById(R.id.linkiAutoComplete);
		Intent resultIntent = new Intent();
		
		if(tytul.getText().toString().length() == 0 || linki.getText().toString().length() == 0) {
			Toast.makeText(this, "Wypelnij wszystkie pola", Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				// czy istnieje
				URL url = new URL("http://epguides.com/" + linki.getText().toString() + "/");
				url.openConnection().getInputStream();
			} catch (Exception e) {
				Toast.makeText(this, "Zly link", Toast.LENGTH_SHORT).show();
				return;
			}
			
			resultIntent.putExtra("tytul", tytul.getText().toString());
			resultIntent.putExtra("link", linki.getText().toString());
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}
	}
}
