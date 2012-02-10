package com.minowak.epguidez;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.URL;

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
		EditText linki = (EditText) findViewById(R.id.linkiAutoComplete);
		Intent resultIntent = new Intent();
		
		String title = null;
		
		if(linki.getText().toString().length() == 0) {
			Toast.makeText(this, "Wypelnij wszystkie pola", Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				// czy istnieje
				URL url = new URL("http://epguides.com/" + linki.getText().toString() + "/");
				DataInputStream in = new DataInputStream(url.openConnection().getInputStream());
				BufferedReader r = new BufferedReader(new InputStreamReader(in));
				
				// znajduje tytul
				while(r.ready()) {
					String line = r.readLine();
					
					if(line.startsWith("<h1>")) {
						title = new String("");
						boolean startParse = false;
						for(int i = 4 ; i < line.length() ; i++) {
							if(startParse) {
								if(line.charAt(i) != '<')
									title = title + line.charAt(i);
							}
							if(startParse && line.charAt(i) == '<') {
								startParse = false;
								break;
							}
							if(line.charAt(i) == '>')
								startParse = true;
						}
					}
				}
			} catch (Exception e) {
				Toast.makeText(this, "Zly link", Toast.LENGTH_SHORT).show();
				return;
			}
			
			//resultIntent.putExtra("tytul", tytul.getText().toString());
			resultIntent.putExtra("tytul", title);
			resultIntent.putExtra("link", linki.getText().toString());
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}
	}
}
