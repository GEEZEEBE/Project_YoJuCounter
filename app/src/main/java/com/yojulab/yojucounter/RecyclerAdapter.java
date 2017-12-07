package com.yojulab.yojucounter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yojulab.yojucounter.database.DBProvider;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sanghunoh on 08/11/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<HashMap<String,Object>> arrayList ;
    private DBProvider db;

    public RecyclerAdapter(){
        this.arrayList = new ArrayList<HashMap<String,Object>>();
    }

    public RecyclerAdapter(ArrayList<HashMap<String,Object>> arrayList){
        this.arrayList = arrayList;
    }

    public RecyclerAdapter(DBProvider db){
        this.db = db;
        this.arrayList = (ArrayList<HashMap<String, Object>>) db.getItemsAsDate();
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
        notifyItemRemoved(position);
    }

    public void removeAtPosition(int position) {
        if (position < arrayList.size()) {
            db.deleteItem(arrayList.get(position));
            // 데이터를 삭제한다
            arrayList.remove(position);
            // 삭제했다고 Adapter 알린다
            notifyItemRemoved(position);

        }
    }

    public void moveItem(int fromPosition, int toPosition) {
//        final String text = arrayList.get(fromPosition);
//        arrayList.remove(fromPosition);
//        arrayList.add(toPosition, text);
        notifyItemMoved(fromPosition, toPosition);
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
        holder.uniqueId.setText((String)hashMap.get("unique_id"));
        holder.itemTitle.setText((String)hashMap.get("title"));
        holder.itemCount.setText((String)hashMap.get("count_number"));
        holder.fkUniqueId.setText((String)hashMap.get("fk_unique_id"));
        holder.itemCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.parseInt(((TextView) holder.itemCount).getText().toString()) + 1;
                ((TextView) holder.itemCount).setText(count.toString());
                String uniqueId = holder.uniqueId.getText().toString();
                String fkUniqueId = holder.fkUniqueId.getText().toString();
                String title = null;
                if(uniqueId.equals("")){
                    title = holder.itemTitle.getText().toString();
                    uniqueId = db.addItem(title);
                    fkUniqueId = db.addSubItem(uniqueId);
                    ((TextView) holder.uniqueId).setText(uniqueId);
                    ((TextView) holder.fkUniqueId).setText(fkUniqueId);
                } else {
                    db.increaseCount(fkUniqueId, count);
                }
            }
        });
        holder.itemMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.parseInt(((TextView) holder.itemCount).getText().toString()) - 1;
                count = (count <= 0)? 0 : count;
                ((TextView) holder.itemCount).setText(count.toString());
                String fkUniqueId = holder.fkUniqueId.getText().toString();
                db.increaseCount(fkUniqueId, count);
            }
        });
        holder.itemReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) holder.itemCount).setText("0");
                String fkUniqueId = holder.fkUniqueId.getText().toString();
                db.increaseCount(fkUniqueId, 0);
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
        public TextView uniqueId;
        public TextView fkUniqueId;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemReset = (ImageView)itemView.findViewById(R.id.item_reset);
            itemMinus = (ImageView)itemView.findViewById(R.id.item_minus);
            itemPlus = (ImageView)itemView.findViewById(R.id.item_plus);
            itemCount = (TextView)itemView.findViewById(R.id.item_count);
            uniqueId = (TextView)itemView.findViewById(R.id.unique_id);
            fkUniqueId = (TextView)itemView.findViewById(R.id.fk_unique_id);
        }
    }
}
