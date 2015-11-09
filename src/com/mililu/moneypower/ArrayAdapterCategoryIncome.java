package com.mililu.moneypower;

import java.util.List;

import com.mililu.moneypower.classobject.Income;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterCategoryIncome extends ArrayAdapter<Income>{
	private Activity context;
	private int layout;
	private List<Income>list;
	private Typeface tf;
	
	public ArrayAdapterCategoryIncome(Context context, int textViewResourceId, List<Income> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=(Activity) context;
		this.layout=textViewResourceId;
		this.list=objects;
		
		// khai bao font
		tf = Typeface.createFromAsset(context.getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null){
			LayoutInflater flater = context.getLayoutInflater();
			View row = flater.inflate(layout, parent,false);
			
			// Set reference
			TextView incomename =(TextView) row.findViewById(R.id.tv_layoutcategoryitem_name);
			
			// Set font
			incomename.setTypeface(tf);
			
			Income data=list.get(position);
			incomename.setText(data.getName()==null?"":data.getName().toString());
			return row;
			}
		return convertView;
	}
}
