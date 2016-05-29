package com.example.oscar.travelagent2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;


public class Online_Chatroom extends AppCompatActivity {

    private static final String SERVER_ADDRESS = "http://188.166.215.252:7210";
    private static String message="";
    private String name ="";
    private Button socket_send_btn;
    private EditText text;
    private TextView msgwall;
    private ScrollView scroll;
    private Socket socket;
    {
        try{
            socket = IO.socket(SERVER_ADDRESS);
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online__chatroom);
        socket_send_btn = (Button) findViewById(R.id.socket_send_btn);
        socket_send_btn.setOnClickListener(btn_onclick);
        text = (EditText) findViewById(R.id.socket_send_msg);
        msgwall = (TextView) findViewById(R.id.socket_msg);
        scroll = (ScrollView)findViewById(R.id.msg_scroll);
        showAlertDialog();
    }

    private void showAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setTitle("請輸入暱稱");
        builder.setIcon(R.drawable.ic_setting_light);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent().setClass(Online_Chatroom.this, MainActivity.class));
            }
        });
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input.getText().toString();
                if (!name.equals("")) {
                    dialog.dismiss();
                    init();
                } else {
                    showAlertDialog();
                    Toast.makeText(Online_Chatroom.this, "不能空白!", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.create().show();
    }

    private View.OnClickListener btn_onclick = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String msg = text.getText().toString();
            text.setText("");
            addMessage(name+":"+msg);

            socket.emit("message", msg,name);
        }
    };

    private void init(){
        socket.connect();
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on("back_msg", handleIncomingMessages);
        socket.on("userjoined",handleJoin);
        socket.emit("userconnect",name);
    }

    private void addMessage(String msg){
        message += msg +"\n"+"\n";
        msgwall.setText(message);
        scroll.fullScroll(View.FOCUS_DOWN);
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Online_Chatroom.this, "非開放時段", Toast.LENGTH_LONG).show();
                    socket_send_btn.setEnabled(false);
                }
            });
        }
    };

    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){

        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Object data =  args[0];
                    String message,name;
                    if (data!=null) {
                        JSONObject json = (JSONObject) data;
                        try {
                            message = json.getString("message");
                            name = json.getString("name");
                        } catch (JSONException e) {
                            return;
                        }
                        addMessage(name+":"+message);
                    }

                }
            });
        }
    };
    private Emitter.Listener handleJoin = new Emitter.Listener(){

        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Object data =  args[0];
                    String name;
                    if (data!=null) {
                        JSONObject json = (JSONObject) data;
                        try {
                            name = json.getString("name");
                        } catch (JSONException e) {
                            return;
                        }
                        addMessage(name+"加入聊天室");
                    }

                }
            });
        }
    };
    @Override
    public void onDestroy(){
        super.onDestroy();
        socket.disconnect();
    }

    public Activity getActivity() {
        return Online_Chatroom.this;
    }
}
