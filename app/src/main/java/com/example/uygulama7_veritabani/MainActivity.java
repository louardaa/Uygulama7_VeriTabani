package com.example.uygulama7_veritabani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.uygulama7_veritabani.Uyg3.UrunKayit;
import com.example.uygulama7_veritabani.Uyg3.Uyg3;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDb();
    }

    private void createDb() {
        database = this.openOrCreateDatabase("Urun", MODE_PRIVATE, null);
        String TABLO = "CREATE TABLE IF NOT EXISTS urunler(id INTEGER PRIMARY KEY, urunadi TEXT, fiyat DOUBLE, adet INTEGER)";
        database.execSQL(TABLO);
    }

    public void uyg1Goster(View view) {
        Intent i = new Intent(MainActivity.this, UrunKayit.class);
        i.putExtra("mod", "ekle");
        startActivity(i);
    }
}

