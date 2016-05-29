package com.example.oscar.travelagent2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Note_Page extends AppCompatActivity {

    private String note="",str_remove="",old_note;
    private Button btn;
    private ArrayList<String> note_list;
    private Set<String> retrive_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn = (Button)findViewById(R.id.add_button);
        btn.setOnClickListener(btn_onClick);
        final Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            note = bundle.getString("note");
            old_note = bundle.getString("old_note");
        }
        if(HaveData()){
            note_list = new ArrayList<>(retrive_data);
            if(!note.equals("")){
                if(old_note!=null){
                    for(int i=0;i<note_list.size();i++){
                        if(note_list.get(i).equals(old_note)){
                            note_list.set(i,note);
                            save_note(note_list);
                        }
                    }
                }else{
                    note_list.add(note);
                    save_note(note_list);
                }

            }

        }else{
            note_list =new ArrayList<>();
            save_note(note_list);
        }
        ListView myListView = (ListView)findViewById(R.id.note_listView);
        ListAdapter myList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,note_list);
        myListView.setAdapter(myList);
        myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String word = String.valueOf(parent.getItemAtPosition(position));
                        Intent intent = new Intent();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("data",word);
                        intent.putExtras(bundle1);
                        intent.setClass(Note_Page.this,Note_ADD_Page.class);
                        startActivity(intent);
                    }
                });
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                str_remove = String.valueOf(parent.getItemAtPosition(position));
                DeleteDialog(str_remove);

                return true;
            }

        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent().setClass(Note_Page.this, MainActivity.class));
    }
    private View.OnClickListener btn_onClick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent go_add = new Intent();
            go_add.setClass(Note_Page.this,Note_ADD_Page.class);
            startActivity(go_add);
        }
    };
    private void save_note(ArrayList<String> arrayList){
        SharedPreferences sharedPreferences = getSharedPreferences("user_note", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>();
        set.addAll(arrayList);
        editor.putStringSet("add_user_note", set);
        editor.apply();
    }
    private boolean HaveData(){
        boolean data = false;
        SharedPreferences sharedPreferences = getSharedPreferences("user_note", Context.MODE_PRIVATE);
        retrive_data = sharedPreferences.getStringSet("add_user_note", null);
        if(retrive_data!=null){
            data = true;
        }
        return data;
    }
    private void DeleteDialog(String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(Note_Page.this);
        builder.setMessage("要刪除資料？");
        builder.setTitle("警告");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                note_list.remove(str_remove);
                save_note(note_list);
                startActivity(new Intent().setClass(Note_Page.this, Note_Page.class));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
