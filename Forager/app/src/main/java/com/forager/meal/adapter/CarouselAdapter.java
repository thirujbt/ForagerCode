package com.forager.meal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.forager.meal.listener.PurchaseResponse;
import com.forager.meal.ui.R;

import java.util.ArrayList;

public class CarouselAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private ArrayList<Integer> mData = new ArrayList<Integer>(0);
    private Context mContext;
    String[] mmonth;
    String[] mdoller;
    String[] mhead;
    Activity activity;
    /* ImageButton buy;
     TextView head,month,doller;*/
    viewHolder mviewHolder;

    customButtonListener customListner;
    public PurchaseResponse mPurchaseInterface;

    public interface customButtonListener {
        public void onButtonClickListner(int position);
    }
    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }


    public CarouselAdapter(Context context) {
        mContext = context;
        // this.mContext = mContext;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mmonth = mContext.getResources().getStringArray(R.array.month);
        mdoller = mContext.getResources().getStringArray(R.array.doller);
        mhead = mContext.getResources().getStringArray(R.array.save);

    }


    @Override
    public int getCount() {

        return mmonth.length;
    }

    @Override
    public Object getItem(int pos) {
        return mmonth[pos];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.fragment_purchase_item, parent, false);

            mviewHolder = new viewHolder();
            convertView.setTag(mviewHolder);
        } else {
            mviewHolder = (viewHolder) convertView.getTag();
        }

        mviewHolder.head = (TextView) convertView.findViewById(R.id.head);
        mviewHolder.month = (TextView) convertView.findViewById(R.id.month);
        mviewHolder.doller = (TextView) convertView.findViewById(R.id.doller);
         mviewHolder.buy = (ImageButton) convertView.findViewById(R.id.buy);

        if (position == 0) {
            mviewHolder.head.setText(mhead[position]);
            mviewHolder.head.setVisibility(View.INVISIBLE);
        } else {
            mviewHolder.head.setText(mhead[position]);
            mviewHolder.head.setVisibility(View.VISIBLE);
        }

        //head.setText(mhead[arg0]);
     //   final String temp = getItem(position);
        mviewHolder.month.setText(mmonth[position]);
        mviewHolder.doller.setText(mdoller[position]);


/*
        mviewHolder.buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position);
                }

            }
        });*/
/*
        mviewHolder.buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
           Toast.makeText(mContext,"BUTTON CLICKD",Toast.LENGTH_SHORT).show();

                //    mPurchaseInterface.startpurchase(position);

            }
        });*/

      /*  mviewHolder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });*/


        return convertView;
    }

    class viewHolder {
        ImageButton buy;
        TextView head, month, doller;

    }

    View.OnClickListener test = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "test", Toast.LENGTH_SHORT).show();

        }
    };


}

/**
 * ViewHolder class for layout.<br />
 * <br />
 * Auto-created on 2016-05-11 19:59:00 by Android Layout Finder
 * (http://www.buzzingandroid.com/tools/android-layout-finder)
 */
/*private static class ViewHolder {
    public final TextView head;
	public final TextView month;
	public final TextView doller;
	public final ImageButton buy;

	private ViewHolder(TextView head, TextView month, TextView doller, ImageButton buy) {
		this.head = head;
		this.month = month;
		this.doller = doller;
		this.buy = buy;
	}

	public static ViewHolder create(LinearLayout rootView) {
		TextView head = (TextView)rootView.findViewById( R.id.head );
		TextView month = (TextView)rootView.findViewById( R.id.month );
		TextView doller = (TextView)rootView.findViewById( R.id.doller );
		ImageButton buy = (ImageButton)rootView.findViewById( R.id.buy );
		return new ViewHolder( head, month, doller, buy );
	}
}*/
