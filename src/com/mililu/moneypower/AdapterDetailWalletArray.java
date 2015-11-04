package com.mililu.moneypower;

import java.text.NumberFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterDetailWalletArray extends ArrayAdapter<Diary>{
	private Activity context;
	private int layout;
	private List<Diary>list;
	Typeface tf;
	
	public AdapterDetailWalletArray(Context context, int textViewResourceId, List<Diary> objects) {
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
		
		TextView NameDiary = (TextView) row.findViewById(R.id.tv_layoutdetailwallet_name);
		//txt1.setTypeface(tf);
		TextView Date=(TextView) row.findViewById(R.id.tv_layoutdetailwallet_date);	
		//txt2.setTypeface(tf);
		TextView Amount=(TextView) row.findViewById(R.id.tv_layoutdetailwallet_money);
		TextView Notice=(TextView) row.findViewById(R.id.tv_layoutdetailwallet_notice);
		Diary data=list.get(position);
		
		if (data.getType() == 1) // neu la thu vao
		{
			NameDiary.setText(data.getName_income()==null?"":data.getName_income().toString());
			Amount.setTextColor(Color.BLUE);
		}
		else if (data.getType() == 2) // neu la chi 
		{
			NameDiary.setText(data.getName_expen()==null?"":data.getName_expen().toString());
			Amount.setTextColor(Color.RED); // chinh mau chu (#FF0000 = 16711680 = RED)
		}
		if ((String.valueOf(data.getDay())=="")|| (String.valueOf(data.getDay())=="") || (String.valueOf(data.getDay())=="")) {
			Date.setText("");
		}
		else {
			Date.setText(data.getDay() + "/" + data.getMonth() + "/" + data.getYear());
		}
		//Amount.setText(String.valueOf(data.getAmount())==null?"":data.getAmount() + "VND");
		Amount.setText(NumberFormat.getCurrencyInstance().format(data.getAmount()));
		Notice.setText(data.getNotice()==null?"":data.getNotice().toString());
		
		return row;
	}
}
