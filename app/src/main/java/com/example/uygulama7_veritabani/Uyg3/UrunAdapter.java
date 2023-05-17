package com.example.uygulama7_veritabani.Uyg3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.uygulama7_veritabani.R;

import java.util.ArrayList;

public class UrunAdapter extends ArrayAdapter<Urun> {
    ArrayList<Urun> urunler;
    Context context;

    public UrunAdapter(@NonNull Context context, ArrayList<Urun> urunler) {
        super(context, 0, urunler);
        this.context = context;
        this.urunler = urunler;
    }

    @Override
    public int getCount() {
        return urunler.size();
    }

    @Override
    public Urun getItem(int i) {
        return urunler.get(i);
    }


    public long getItemID(int i) {
        return urunler.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView satirUrunAdı;
        TextView satirUrunFiyat;
        TextView satirUrunAdet;
        ImageView satırUrunResmi;
        Urun Urun = urunler.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.uyg3_listview_satir, null);
        }
        satirUrunAdı = view.findViewById(R.id.txtUrunadi);
        satirUrunFiyat = view.findViewById(R.id.txtUrunFiyati);
        satirUrunAdet = view.findViewById(R.id.txtUrunAdet);
        satırUrunResmi = view.findViewById(R.id.urunResim);

        satirUrunAdı.setText(satirUrunAdı.getText());
        satirUrunFiyat.setText(String.format("%.02f", Urun.getFiyat())+" TL");
        satirUrunAdet.setText(Urun.getAdet() + "");
        satırUrunResmi.setImageResource(Urun.getResim());

        return  view;


    }
}
