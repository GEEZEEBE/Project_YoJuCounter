package com.yojulab.yojucounter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sanghunoh on 08/11/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<HashMap<String,Object>> arrayList ;

    public RecyclerAdapter(){
        this.arrayList = new ArrayList<HashMap<String,Object>>();
    }

    public RecyclerAdapter(ArrayList<HashMap<String,Object>> arrayList){
        this.arrayList = new ArrayList<HashMap<String,Object>>();
        this.arrayList = arrayList;
    }

    public void addItem(HashMap<String,Object> hashMap){
        this.arrayList.add(hashMap);
        notifyDataSetChanged();
    }

    public void addItem(int position, HashMap<String,Object> hashMap){
        this.arrayList.add(position, hashMap);
        notifyItemInserted(position);
    }

    public void removeItem(int position){
        this.arrayList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String,Object> hashMap = arrayList.get(position);
        holder.itemTitle.setText((String)hashMap.get("title"));
        holder.itemCount.setText("0");
        holder.itemCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.parseInt(((TextView) holder.itemCount).getText().toString()) + 1;
                ((TextView) holder.itemCount).setText(count.toString());
            }
        });
        holder.itemMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.parseInt(((TextView) holder.itemCount).getText().toString()) - 1;
                count = (count <= 0)? 0 : count;
                ((TextView) holder.itemCount).setText(count.toString());
            }
        });
        holder.itemReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) holder.itemCount).setText("0");
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemTitle;
        public ImageView itemReset;
        public ImageView itemMinus;
        public ImageView itemPlus;
        public TextView itemCount;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemReset = (ImageView)itemView.findViewById(R.id.item_reset);
            itemMinus = (ImageView)itemView.findViewById(R.id.item_minus);
            itemPlus = (ImageView)itemView.findViewById(R.id.item_plus);
            itemCount = (TextView)itemView.findViewById(R.id.item_count);
        }
    }
}
