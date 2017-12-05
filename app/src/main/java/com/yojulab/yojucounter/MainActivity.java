package com.yojulab.yojucounter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    RecyclerAdapter adapter;
    ArrayList<HashMap<String,Object>> arrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<HashMap<String,Object>>();

        adapter = new RecyclerAdapter(arrayList);

        recyclerView.setAdapter(adapter);

        HashMap<String,Object> hashMap = null;
        hashMap = new HashMap<String,Object>();
        hashMap.put("title", "Chapter Two");
        adapter.addItem(hashMap);
        hashMap = new HashMap<String,Object>();
        hashMap.put("title", "Chapter Four");
        adapter.addItem(hashMap);
        hashMap = new HashMap<String,Object>();
        hashMap.put("title", "Chapter Seven");
        adapter.addItem(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
            case R.id.action_modify:
                break;
            case R.id.action_settings:
                break;
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
                hashMap.put("title", title.getText().toString());
                adapter.addItem(hashMap);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
