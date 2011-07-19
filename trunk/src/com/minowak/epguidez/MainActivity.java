package com.minowak.epguidez;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	private enum Request { DODANO, USUNIETO, };
	private SQLiteDatabase db;
	private ArrayList<String> seriale;
	private ArrayList<String> linki;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Tworze/Otwieram baze danych
        db = openOrCreateDatabase("epguidez.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(1);
        db.setLocale(Locale.getDefault());
        db.setLockingEnabled(true);
        
        // Tworze tabele
        // TODO uzupelnic
        try {
        	db.execSQL("CREATE TABLE seriale ( id INTEGER PRIMARY KEY AUTOINCREMENT, tytul TEXT, link TEXT )");
        	//db.execSQL("CREATE TABLE daty_odcinkow ( id INTEGER, odcinek TEXT, data DATE, nazwa TEXT, FOREIGN KEY(id) REFERENCES seriale(id))");
        } catch(Exception e) {
        	// TODO nothing to do :(
        }
        
        seriale = new ArrayList<String>();
        linki = new ArrayList<String>();
        readDB();
    }
    
    public void dodajSerial(View view) {
    	Intent intent = new Intent(this, DodajSerialActivity.class);
    	startActivityForResult(intent, Request.DODANO.ordinal());
    }
    
    public void przegladajSeriale(View view) {
    	Intent intent = new Intent(this, PrzegladajSerialeActivity.class);
    	intent.putExtra("seriale", seriale);
    	intent.putExtra("linki", linki);
    	
    	startActivity(intent);
    }
    
    public void nadchodzaceSeriale(View view) {
    	Intent intent = new Intent(this, NadchodzaceSerialeActivity.class);
    	intent.putExtra("seriale", linki);
    	startActivity(intent);
    }
    
    public void usunSerial(View view) {
    	Intent intent = new Intent(this, UsunSerialActivity.class);
    	intent.putExtra("seriale", seriale);
    	
    	startActivityForResult(intent, Request.USUNIETO.ordinal());
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == Request.DODANO.ordinal() && resultCode == Activity.RESULT_OK) {
    		String tytul = data.getExtras().getString("tytul");
    		String link = data.getExtras().getString("link");
    		// Dodaje do bazy danych
    		ContentValues values = new ContentValues();
    		values.put("tytul", tytul);
    		values.put("link", link);
    		try {
    			db.insertOrThrow("seriale", null, values);
    			Toast.makeText(this, "Dodano", Toast.LENGTH_SHORT).show();
    		} catch(Exception e) {
    			Toast.makeText(this, "Nie udalo sie dodac", Toast.LENGTH_SHORT).show();
    		}
    		readDB();
    	} else
    	if(requestCode == Request.USUNIETO.ordinal() && resultCode == Activity.RESULT_OK) {
    		try {
    			String tytul = data.getExtras().getString("tytul");
    			db.delete("seriale", "tytul=?", new String[] { tytul });
    			Toast.makeText(this, "Usunieto", Toast.LENGTH_SHORT).show();
    		} catch(Exception e) {
    			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    		}
    		readDB();
    	}
    }
    
    private void readDB() {
    	seriale.clear();
    	linki.clear();
    	Cursor cursor = db.query("seriale", null, null, null, null, null, null);
    	cursor.moveToFirst();
    	while(cursor.isAfterLast() == false) {
    		seriale.add(cursor.getString(1));    		
    		linki.add(cursor.getString(2));
    		
    		cursor.moveToNext();
    	}
    }
}