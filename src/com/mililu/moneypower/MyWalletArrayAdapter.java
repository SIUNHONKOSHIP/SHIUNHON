package com.mililu.moneypower;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.Typeface;

public class MyWalletArrayAdapter extends ArrayAdapter<Wallet>{
	
	private Activity context;
	private int layout;
	private List<Wallet>list;
	Typeface tf;
	
	public MyWalletArrayAdapter(Context context, int textViewResourceId, List<Wallet> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=(Activity) context;
		this.layout=textViewResourceId;
		this.list=objects;
		
		tf = Typeface.createFromAsset(context.getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	}	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater flater = context.getLayoutInflater();
		View row = flater.inflate(layout, parent,false);
		
		TextView txt1=(TextView) row.findViewById(R.id.tv_row1);
		txt1.setTypeface(tf);
		TextView txt2=(TextView) row.findViewById(R.id.tv_row2);	
		txt2.setTypeface(tf);
		
		Wallet data=list.get(position);
		txt1.setText(data.getName()==null?"":data.getName().toString());
		txt2.setText(data.getMoney()==null?"":data.getMoney().toString());
		
//		if((position/2)==0){
//			row.setBackgroundColor(Color.rgb(204, 238, 255));;
//		}
		
		return row;
	}

	
}