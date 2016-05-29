package com.example.oscar.travelagent2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Note_ADD_Page extends AppCompatActivity {

    private EditText editText;
    private Button btn;
    private String old_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note__add__page);
        editText = (EditText)findViewById(R.id.note_text);
        btn = (Button)findViewById(R.id.add_ensure_button);
        btn.setOnClickListener(btn_onclick);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            old_str = bundle.getString("data");
            editText.setText(old_str);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog();
    }
    private View.OnClickListener btn_onclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String note = editText.getText().toString();
            if(!note.equals("")){
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("note", note);
                if(old_str!=null){
                    bundle.putString("old_note",old_str);
                }
                intent.putExtras(bundle);
                intent.setClass(Note_ADD_Page.this, Note_Page.class);
                startActivity(intent);
            }else{
                Toast.makeText(Note_ADD_Page.this, "無輸入文字!", Toast.LENGTH_LONG).show();
            }
        }
    };
    private void AlertDialog(){
        final String note = editText.getText().toString();
        if(!note.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(Note_ADD_Page.this);
            builder.setMessage("需保存資料？");
            builder.setTitle("警告");
            builder.setCancelable(false);
            builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("note",note);
                    if(old_str!=null){
                        bundle.putString("old_note",old_str);
                    }
                    intent.putExtras(bundle);
                    intent.setClass(Note_ADD_Page.this, Note_Page.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent().setClass(Note_ADD_Page.this, Note_Page.class));
                }
            });
            builder.create().show();
        }else{
            Intent intent = new Intent();
            intent.setClass(Note_ADD_Page.this, Note_Page.class);
            startActivity(intent);
        }

    }
}
