package com.example.uygulama7_veritabani.Uyg3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.uygulama7_veritabani.R;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UrunKayit extends AppCompatActivity {

    SQLiteDatabase database;
    EditText urunadi;
    EditText urunFiyat;
    EditText urunAdet;
    Button btnKaydet;
    int id;

    public void kayitgec (View view) {
        Intent i = new Intent(UrunKayit.this, Uyg3.class);

        startActivity(i);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uyg3_kayit);
        urunadi = findViewById(R.id.editKayitUrunadi);
        urunFiyat = findViewById(R.id.editKayitFiyat);
        urunAdet = findViewById(R.id.editKayitAdet);
        btnKaydet = findViewById(R.id.btnKayit);
        Intent intent = getIntent();

        id=intent.getIntExtra("id",0);
        String mod=intent.getStringExtra("mod");
        database=this.openOrCreateDatabase("Urun",MODE_PRIVATE,null);
         if(mod.equals("degistir")){
            try {
                Cursor cursor=database.rawQuery("SELECT urunadi,fiyat,adet FROM urunler WHERE id=?",
                new String[]{String.valueOf(id)});
                int kolonUrunadi=cursor.getColumnIndex("urunadi");
                int kolonFiyat=cursor.getColumnIndex("fiyat");
                int kolonAdet=cursor.getColumnIndex("adet");
                while (cursor.moveToNext()){
                    urunadi.setText(cursor.getString(kolonUrunadi));
                    urunFiyat.setText(cursor.getString(kolonFiyat)+"-");
                    urunAdet.setText(cursor.getInt(kolonAdet)+"-");
                }
                cursor.close();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
         btnKaydet.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (mod.equals("degistir")) {
                     String SORGU = "UPDATE urunler SET urunadi=?,fiyat=?,adet=? WHERE id=?";
                     SQLiteStatement durumlar = database.compileStatement(SORGU);
                     durumlar.bindString(1,
                             urunadi.getText().toString());
                     durumlar.bindDouble(2,
                             Double.parseDouble(urunFiyat.getText().toString()));
                     durumlar.bindLong(3,
                             Integer.parseInt(urunAdet.getText().toString()));
                     durumlar.bindLong(4, id);
                     durumlar.execute();
                 } else {

                 }
                     String SORGU = "INSERT INTO urunler(urunadi,fiyat,adet) VALUES(?,?,?)";
                     SQLiteStatement durumlar = database.compileStatement(SORGU);
                     durumlar.bindString(1, urunadi.getText().toString());
                     durumlar.bindDouble(2, Double.parseDouble(urunFiyat.getText().toString()));
                     durumlar.bindLong(3, Integer.parseInt(urunAdet.getText().toString()));
                     durumlar.execute();
                 }
                 public void kayituygec (View view) {


             }
         });
    }
}


