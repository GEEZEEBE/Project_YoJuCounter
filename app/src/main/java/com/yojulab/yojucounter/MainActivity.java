package com.yojulab.yojucounter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.stetho.Stetho;
import com.yojulab.yojucounter.database.ConstantsImpl;
import com.yojulab.yojucounter.database.DBProvider;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ConstantsImpl {

    RecyclerAdapter adapter;
//    ExtendRecyclerAdapter adapter;
    private DBProvider db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(BuildConfig.DEBUG){
            Context context = getApplicationContext();
            Stetho.initializeWithDefaults(this);
        }
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);

        db = new DBProvider(this);
        db.open();

        adapter = new RecyclerAdapter(db);

        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 드래그앤드롭 시
                adapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 아이템 스와이프 시
                adapter.removeAtPosition(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        Bundle bundle = new Bundle();
        Class<?> cls = null;
        switch (id) {
            case R.id.action_add:
                openDialog();
                break;
//            case R.id.action_modify:
//                break;
//            case R.id.action_settings:
//                break;
            case R.id.action_statistic:
                cls = StatisticActivity.class;
                intent = new Intent(this, cls);
//                intent.putExtra("subActivity","Send Value Directly!");
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        dialog.setContentView(R.layout.add_layout);

        dialog.setTitle("Add Title");

        Button button_cancel = (Button)dialog.findViewById(R.id.button_cancel);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        Button button_ok = (Button)dialog.findViewById(R.id.button_ok);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = (EditText)dialog.findViewById(R.id.title);
                HashMap<String,Object> hashMap = new HashMap<String,Object>();
                hashMap.put(COUNTER_NAME, title.getText().toString());
                hashMap.put(COUNT_NUMBER, "0");
                adapter.addItem(0,hashMap);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
