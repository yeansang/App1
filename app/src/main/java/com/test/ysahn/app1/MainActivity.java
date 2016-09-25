package com.test.ysahn.app1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    SwipeMenuListView mListView;
    EditText mEditText;
    Button mButton;
    DBConnecter dbConnecter;
    int count=0;
    ArrayAdapter<String> mListAdapter;
    //ArrayList<String> itemList = new ArrayList<String>();

    private void refresh(){
        mListAdapter.clear();
        Vector<String> memos = dbConnecter.getAll();
        mListAdapter.addAll(memos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("Memo");
        setContentView(R.layout.activity_main);

        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        mEditText = (EditText)findViewById(R.id.editText);
        mButton = (Button)findViewById(R.id.button);

        dbConnecter = new DBConnecter(getApplicationContext(), "memo.db",null,1);

        Vector<String> memos = dbConnecter.getAll();

        mListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,memos);

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
                mListView.smoothCloseMenu();
                return true;
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth((int)dp2px(90));
                deleteItem.setIcon(R.drawable.ic_trash);
                // set a icon
                //deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int i, SwipeMenu menu, int index) {
                if(index==0){
                    String temp = mListAdapter.getItem(i);
                    dbConnecter.remove(i);
                    refresh();
                    Toast.makeText(getApplicationContext(),temp+" is removed",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        mListView.setMenuCreator(creator);

        mListView.setAdapter(mListAdapter);
    }

    public float dp2px(float dipValue) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
