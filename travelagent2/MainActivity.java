package com.example.oscar.travelagent2;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.*;


public class MainActivity extends AppCompatActivity {

    private String account="",password="";
    private Dialog login_dialog;
    private EditText login_account,login_password;
    private DBHandler dbHandler;
    private App_Handler app_handler;
    public static Member User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button search = (Button) findViewById(R.id.start_search);
        Button login = (Button) findViewById(R.id.start_login);
        Button chat = (Button) findViewById(R.id.start_chat);
        Button enroll = (Button) findViewById(R.id.start_enroll);
        Button exit = (Button) findViewById(R.id.start_exit);
        Button paper = (Button)findViewById(R.id.start_paper);
        Button setting = (Button)findViewById(R.id.start_setting);
        search.setOnClickListener(btn_searchOnclick);
        login.setOnClickListener(btn_loginOnclick);
        enroll.setOnClickListener(btn_enrollOnclick);
        chat.setOnClickListener(btn_chatOnclick);
        exit.setOnClickListener(btn_exitOnclick);
        paper.setOnClickListener(btn_paperOnclick);
        setting.setOnClickListener(btn_settingOnclick);
        dbHandler = new DBHandler(this,null,null,1);
        app_handler = new App_Handler(MainActivity.this);
        if(HaveData()){
            User = dbHandler.UserChecked(account,password);
        }

    }
    /***前往地圖介面按鈕***/
    private View.OnClickListener btn_searchOnclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(app_handler.HaveInternet()){
                Intent goSearch = new Intent();
                goSearch.setClass(MainActivity.this, SearchMainPage.class);
                startActivity(goSearch);
            }else{
                Toast.makeText(MainActivity.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
            }
        }
    };
    /***前往登入介面按鈕***/
    private View.OnClickListener btn_loginOnclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(app_handler.HaveInternet()) {
               if(User==null){
                   LoginDialog();
               }else{
                   CheckDialog();
               }
            }else{
                Toast.makeText(MainActivity.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
            }
        }
    };
    /***前往註冊介面按鈕***/
    private View.OnClickListener btn_enrollOnclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(app_handler.HaveInternet()) {
                Intent goEnroll = new Intent();
                goEnroll.setClass(MainActivity.this, Register_Form.class);
                startActivity(goEnroll);
            }else{
                Toast.makeText(MainActivity.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
            }
        }
    };
    /***前往聊天室介面按鈕***/
    private View.OnClickListener btn_chatOnclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(app_handler.HaveInternet()) {
                if( Patterns.WEB_URL.matcher("http://188.166.227.114:7210").matches()){
                    startActivity(new Intent().setClass(MainActivity.this, Online_Chatroom.class));
                }else{
                    Toast.makeText(MainActivity.this, "非開放時段!", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(MainActivity.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
            }
        }
    };
    /***前往便條紙介面按鈕***/
    private View.OnClickListener btn_paperOnclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent go_paper = new Intent();
            go_paper.setClass(MainActivity.this,Note_Page.class);
            startActivity(go_paper);
        }
    };
    /***前往設定介面按鈕***/
    private View.OnClickListener btn_settingOnclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(app_handler.HaveInternet()) {
                if (app_handler.isMember()) {
                    Intent go_user_info = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("get_info",User);
                    go_user_info.putExtras(bundle);
                    go_user_info.setClass(MainActivity.this, User_Information.class);
                    startActivity(go_user_info);
                }else{
                    Toast.makeText(MainActivity.this, "請先登入會員!", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(MainActivity.this, "請確認手機網路狀態!", Toast.LENGTH_LONG).show();
            }
        }
    };
    /***離開介面按鈕***/
    private View.OnClickListener btn_exitOnclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            AppExit();
        }
    };

    private void LoginDialog(){
        login_dialog = new Dialog(MainActivity.this);
        login_dialog.setTitle("會員登入系統");
        login_dialog.setCancelable(false);
        login_dialog.setContentView(R.layout.content_login_dialog);
        Button login_btn_ok = (Button)login_dialog.findViewById(R.id.btnOk);
        Button login_btn_cancel = (Button)login_dialog.findViewById(R.id.btnCancel);
        login_account = (EditText) login_dialog.findViewById(R.id.login_account);
        login_password = (EditText) login_dialog.findViewById(R.id.login_password);
        login_btn_ok.setOnClickListener(login_ok_click);
        login_btn_cancel.setOnClickListener(login_cancel_click);
        login_dialog.show();
    }
    private void CheckDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("切換使用者？");
        builder.setTitle("提示");
        builder.setCancelable(false);
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginDialog();
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
    private View.OnClickListener login_ok_click = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String account = String.valueOf(login_account.getText());
            String password = String.valueOf(login_password.getText());
            Member member = dbHandler.UserChecked(account, password);
            if(member==null || account.equals("") || password.equals("")){
                login_dialog.cancel();
                Toast.makeText(MainActivity.this,"登入失敗!",Toast.LENGTH_LONG).show();
            }else{
                if(User!=null && User.get_email().equals(member.get_email())){
                    Toast.makeText(MainActivity.this,"已登入狀態!",Toast.LENGTH_LONG).show();
                }else{
                    SaveData(member);
                    User = member;
                    Intent go_enroll_search = new Intent();
                    go_enroll_search.setClass(MainActivity.this, SearchMainPage.class);
                    login_dialog.cancel();
                    startActivity(go_enroll_search);
                    Toast.makeText(MainActivity.this,"登入成功!",Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    private View.OnClickListener login_cancel_click= new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            login_dialog.cancel();
        }
    };
    private void SaveData(Member member){
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("account", member.get_email());
        editor.putString("password", member.get_password());
        editor.apply();
    }
    private boolean HaveData(){
        boolean spf_exist = true;
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("account","");
        password = sharedPreferences.getString("password","");
        if(account.equals("") || password.equals("")){
            spf_exist = false;
        }
        return spf_exist;
    }
    private void AppExit()
    {
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
