package com.test.ysahn.app1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    ListView mListView;
    EditText mEditText;
    Button mButton;
    DBConnecter dbConnecter;
    int count=0;
    ArrayAdapter<String> mListAdapter;
    ArrayList<String> itemList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("Memo");
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.listView);
        mEditText = (EditText)findViewById(R.id.editText);
        mButton = (Button)findViewById(R.id.button);

        dbConnecter = new DBConnecter(getApplicationContext(), "memo.db",null,1);

        Vector<String> memos = dbConnecter.getAll();
        if(memos != null) {
            for (String memo : memos) {
                itemList.add(memo);
            }
        }

        mListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemList);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = mEditText.getText().toString();
                if(temp.equals("")) return;
                mListAdapter.add(temp);
                dbConnecter.input(temp,count);
                count++;
                mEditText.setText("");
                mListView.setSelection(mListAdapter.getCount() - 1);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = mListAdapter.getItem(i);
                mListAdapter.remove(temp);
                dbConnecter.remove(i);
                Toast.makeText(getApplicationContext(),temp+" is removed",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mListView.setAdapter(mListAdapter);
    }
}
