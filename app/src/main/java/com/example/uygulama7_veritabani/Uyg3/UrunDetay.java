package com.example.uygulama7_veritabani.Uyg3;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.uygulama7_veritabani.R;

import java.io.ByteArrayOutputStream;

public class UrunDetay extends AppCompatActivity {


    SQLiteDatabase database;
    TextView urunadi;
    TextView urunFiyat;
    TextView urunAdet;
    Button btnDegistir;
    Button btnGeri;
    Button btnSil;
    Button btnResimEkle;
    ImageView urunResim;
    int id;
    ActivityResultLauncher<Intent> galleryLauncher;
    ActivityResultLauncher<String> galleryPermisson;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uyg3_kayit);
        urunadi=findViewById(R.id.txtUrunadi);
        urunFiyat=findViewById(R.id.txtUrunFiyati);
        urunAdet=findViewById(R.id.txtUrunAdet);
        btnDegistir=findViewById(R.id.btnDegistir);
        btnGeri=findViewById(R.id.btnGeri);
        btnSil=findViewById(R.id.btnSil);
        btnResimEkle=findViewById(R.id.btnResimEkle);
        urunResim=findViewById(R.id.urunResim);
        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);
        try {
            database=this.openOrCreateDatabase("urunler",MODE_PRIVATE,null);
            Cursor cursor=database.rawQuery("SELECT * FROM urunler WHERE id=?",
            new String[]{String.valueOf(id)});
            int kolonUrunadi=cursor.getColumnIndex("urunadi");
            int kolonFiyat=cursor.getColumnIndex("fiyat");
            int kolonAdet=cursor.getColumnIndex("adet");
            int kolonResim=cursor.getColumnIndex("resim");
            while (cursor.moveToNext()){
                urunadi.setText(cursor.getString(kolonUrunadi));
                urunFiyat.setText(cursor.getString(kolonFiyat)+"");
                urunAdet.setText(cursor.getInt(kolonAdet)+"");
                byte[] bytes=cursor.getBlob(kolonResim);
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                urunResim.setImageBitmap(bitmap);
            }
            cursor.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.d("SERVİS", "onCreate: "+ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE));
        registerLauncher();

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "İzin gerekli", Toast.LENGTH_SHORT).show();
            }
            else
            {
                galleryPermisson.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        btnResimEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToGallery=new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(intentToGallery);
            }
        });
        btnDegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),UrunKayit.class);
                intent.putExtra("mod","degistir");
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });
        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String SORGU="DELETE FROM urunler WHERE id=?";
                    SQLiteStatement durumlar=database.compileStatement(SORGU);
                    durumlar.bindLong(1,id);
                    durumlar.execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
                finish();
            }
        });
        btnGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(UrunDetay.this,UrunKayit.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    public Bitmap resimKucultucu(Bitmap b,int buyukluk){
        double oran=(double) b.getWidth()/(double) b.getHeight();
        double uzunluk=(double)buyukluk/oran;
        return bitmap.createScaledBitmap(bitmap,buyukluk,
                (int)uzunluk, true);
    }
    public void Kaydet(){
        Bitmap kucukResim = resimKucultucu(bitmap, 250);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        kucukResim.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        try {
            ContentValues contentValues=new ContentValues();
            contentValues.put("resim",bytes);
            database.update("urunler",contentValues,"id="+id,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void registerLauncher(){
        galleryLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()==RESULT_OK){
                            Intent intentResult=result.getData();
                            if(intentResult!=null){
                                Uri galleryUri=intentResult.getData();
                                try {
                                    if(Build.VERSION.SDK_INT>=28){
                                        ImageDecoder.Source source=ImageDecoder.createSource(getContentResolver(),galleryUri);
                                        bitmap= ImageDecoder.decodeBitmap(source);
                                        urunResim.setImageBitmap(bitmap);
                                        Kaydet();
                                    }else{
                                        bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),galleryUri);
                                        urunResim.setImageBitmap(bitmap);
                                        Kaydet();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
        galleryPermisson=registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result){
                            Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            galleryLauncher.launch(intentToGallery);
                        }else{
                            Toast.makeText(UrunDetay.this,"İzin vermeniz gerekli!"
                                    ,Toast.
                                    LENGTH_LONG).show();
                        }
                    }
                });
    }
}



