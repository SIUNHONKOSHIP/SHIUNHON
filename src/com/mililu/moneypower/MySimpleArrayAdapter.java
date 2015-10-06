package com.mililu.moneypower;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<Wallet>{
	
	private Activity context;
	private int layout;
	private List<Wallet>list;
	public MySimpleArrayAdapter(Context context, int textViewResourceId, List<Wallet> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=(Activity) context;
		this.layout=textViewResourceId;
		this.list=objects;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater flater=context.getLayoutInflater();
		View row=flater.inflate(layout, parent,false);
		TextView txt1=(TextView) row.findViewById(R.id.tv_row1);
		TextView txt2=(TextView) row.findViewById(R.id.tv_row2);
		/*txt1.setTextAlignment(Gravity.LEFT);
 		txt2.setTextAlignment(Gravity.LEFT);
	 	txt3.setTextAlignment(Gravity.LEFT);*/
		Wallet data=list.get(position);
		txt1.setText(data.getName()==null?"":data.getName().toString());
		txt2.setText(data.getMoney()==null?"":data.getMoney().toString());
		if(position==0){
			row.setBackgroundColor(Color.rgb(190, 190, 190));;
		}
		return row;
	}
}