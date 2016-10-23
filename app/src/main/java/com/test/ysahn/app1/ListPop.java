package com.test.ysahn.app1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Vector;

public class ListPop extends Activity {

    private ListView mListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_list_pop);

        final Intent intent = getIntent();
        mListview = (ListView)findViewById(R.id.list_view);

        Vector<String> button = new Vector<>();
        button.add("Delete");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,button);
        mListview.setAdapter(arrayAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) intent.putExtra("result",1);
                else intent.putExtra("result",0);
                ListPop.this.finish();
            }
        });
    }
}
