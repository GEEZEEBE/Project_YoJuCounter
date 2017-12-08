package com.yojulab.yojucounter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yojulab.yojucounter.database.ConstantsImpl;
import com.yojulab.yojucounter.database.DBProvider;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sanghunoh on 08/11/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements ConstantsImpl {

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

    public void removeAtPosition(int position) {
        if (position < arrayList.size()) {
            int cnt = db.unUseItem(arrayList.get(position));
            // 데이터를 삭제한다
            if(cnt > 0){
                arrayList.remove(position);
                // 삭제했다고 Adapter 알린다
                notifyItemRemoved(position);
            }
        }
    }

    public void moveItem(int fromPosition, int toPosition) {
        HashMap<String,Object> hashMap = arrayList.get(fromPosition);
        arrayList.remove(fromPosition);
        arrayList.add(toPosition, hashMap);
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
        holder.dailyUniqueId.setText((String)hashMap.get(DAILY_UNIQUE_ID));
        holder.counterName.setText((String)hashMap.get(COUNTER_NAME));
        holder.countNumber.setText(hashMap.get(COUNT_NUMBER).toString());
        holder.informationUniqueId.setText((String)hashMap.get(INFORMATION_UNIQUE_ID));
        holder.countNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.parseInt(((TextView) holder.countNumber).getText().toString()) + 1;
                ((TextView) holder.countNumber).setText(count.toString());
                String dailyUniqueId = holder.dailyUniqueId.getText().toString();
                if(dailyUniqueId.equals("")){
                    String informationUniqueId = null;
                    String counterName = holder.counterName.getText().toString();
                    informationUniqueId = db.addItem(counterName);
                    dailyUniqueId = db.addSubItem(informationUniqueId);
                    ((TextView) holder.dailyUniqueId).setText(dailyUniqueId);
                    ((TextView) holder.informationUniqueId).setText(informationUniqueId);
                } else {
                    db.increaseCount(dailyUniqueId, count);
                }
            }
        });
        holder.itemMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.parseInt(((TextView) holder.countNumber).getText().toString()) - 1;
                count = (count <= 0)? 0 : count;
                ((TextView) holder.countNumber).setText(count.toString());
                String dailyUniqueId = holder.informationUniqueId.getText().toString();
                db.increaseCount(dailyUniqueId, count);
            }
        });
        holder.itemReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String informationUniqueId = holder.informationUniqueId.getText().toString();
                int cnt = db.increaseCount(informationUniqueId, 0);
                if(cnt > 0){
                    ((TextView) holder.countNumber).setText("0");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView counterName;
        public ImageView itemReset;
        public ImageView itemMinus;
        public ImageView itemPlus;
        public TextView countNumber;
        public TextView dailyUniqueId;
        public TextView informationUniqueId;

        public ViewHolder(final View itemView) {
            super(itemView);
            informationUniqueId = (TextView)itemView.findViewById(R.id.information_unique_id);
            counterName = (TextView)itemView.findViewById(R.id.counter_name);
            itemReset = (ImageView)itemView.findViewById(R.id.item_reset);
            itemMinus = (ImageView)itemView.findViewById(R.id.item_minus);
            itemPlus = (ImageView)itemView.findViewById(R.id.item_plus);
            countNumber = (TextView)itemView.findViewById(R.id.count_number);
            dailyUniqueId = (TextView)itemView.findViewById(R.id.daily_unique_id);
        }
    }
}
