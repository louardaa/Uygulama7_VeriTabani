package com.example.uygulama7_veritabani.Uyg3;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uygulama7_veritabani.R;

import java.util.ArrayList;

public class Uyg3 extends AppCompatActivity {

    SQLiteDatabase databese;
    Urun urun;
    ArrayList<Urun> urunler;
    ListView listeUrunler;
    UrunAdapter urunAdapter;

    Button btnKaydet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uyg3);

        urunler = new ArrayList<>();
        listeUrunler = findViewById(R.id.urunListe);

    }


    public void btnyenikayitekle(View view) {
        Intent i = new Intent(Uyg3.this, UrunKayit.class);
        i.putExtra("mod", "ekle");
        startActivity(i);
    }
    private void getAllUrunler(){
        SQLiteDatabase database = this.openOrCreateDatabase("Urun", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM urunler ",null);
        int kolonId = cursor.getColumnIndex("id");
        int kolonUrunAdi = cursor.getColumnIndex("urunadi");
        int kolonfiyat = cursor.getColumnIndex("fiyat");
        int kolonadet = cursor.getColumnIndex("adet");

        while (cursor.moveToNext()){
            int id = cursor.getInt(kolonId);
            String urunAdi = cursor.getString(kolonUrunAdi);
            double fiyat = cursor.getDouble(kolonfiyat);
            long adet = cursor.getLong(kolonadet);

            urun = new Urun(id, urunAdi, fiyat, adet, R.drawable.resim_yok);
            urunler.add(urun);
        }
        cursor.close();
    }
}

