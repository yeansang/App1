package com.test.ysahn.app1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
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
    ListView mListView;
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

        mListView = (ListView) findViewById(R.id.listView);
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
                    PopupMenu pop = new PopupMenu(MainActivity.this, view);
                    getMenuInflater().inflate(R.menu.menu_listview,pop.getMenu());

                    final int index = i;

                    //팝업메뉴 리스너 설정
                    pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId() == R.id.delete){
                                String temp = mListAdapter.getItem(index);
                                dbConnecter.remove(index);
                                refresh();
                                Toast.makeText(getApplicationContext(),temp+" is removed",Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    });
                    pop.show();
                return true;
            }
        });

        mListView.setAdapter(mListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            int result = data.getIntExtra("result",0);
            int num = data.getIntExtra("listnum",0);
            Log.d("test",result+"/"+num);
            switch (result){
                case 0:
                    break;
                case 1:
                    String temp = mListAdapter.getItem(num);
                    dbConnecter.remove(num);
                    refresh();
                    Toast.makeText(getApplicationContext(),temp+" is removed",Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    public float dp2px(float dipValue) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
