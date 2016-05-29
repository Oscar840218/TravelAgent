package com.example.oscar.travelagent2;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


public class Animation{


    private Thread animator_title,animator_btn1,animator_btn2,animator_btn3,animator_btn4;
    private android.view.animation.Animation fadeIn_title,fadeIn_btn;
    private TextView title_main,title_search;
    private Button btn1,btn2,btn3,btn4;

    public Animation(Context context,TextView text,Button btn1,Button btn2,Button btn3,Button btn4){
        title_main = text;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.btn4 = btn4;
        fadeIn_title = AnimationUtils.loadAnimation(context, R.anim.fadein_title);
        fadeIn_btn = AnimationUtils.loadAnimation(context, R.anim.fadein_btn);
        fadeIn_title.setAnimationListener(new MyListener_main());
        SetAnimate_Title(text);
        SetAnimate_btn(btn1,btn2,btn3,btn4);
    }
    public Animation(Context context,TextView text){

        title_search = text;
        fadeIn_title = AnimationUtils.loadAnimation(context, R.anim.fadein_title);
        fadeIn_title.setAnimationListener(new MyListener_search());
        SetAnimate_Title(text);
    }
    private void SetAnimate_Title(final TextView text) {
        animator_title = new Thread(new Runnable() {
            @Override
            public void run() {
                text.startAnimation(fadeIn_title);
            }
        });

    }
    private void SetAnimate_btn(final Button btn1,final Button btn2,final Button btn3,final Button btn4){
        animator_btn1 = new Thread(new Runnable() {
            @Override
            public void run() {
                fadeIn_btn.setAnimationListener(new MyListener_btn());
                btn1.startAnimation(fadeIn_btn);
            }
        });
        animator_btn2 = new Thread(new Runnable() {
            @Override
            public void run() {
                fadeIn_btn.setAnimationListener(new MyListener_btn());
                btn2.startAnimation(fadeIn_btn);
            }
        });
        animator_btn3 = new Thread(new Runnable() {
            @Override
            public void run() {
                fadeIn_btn.setAnimationListener(new MyListener_btn());
                btn3.startAnimation(fadeIn_btn);
            }
        });
        animator_btn4 = new Thread(new Runnable() {
            @Override
            public void run() {
                fadeIn_btn.setAnimationListener(new MyListener_btn());
                btn4.startAnimation(fadeIn_btn);
            }
        });
    }

    public void startAnimation_main(){
        Runnable start = new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(animator_title,250);
                handler.postDelayed(animator_btn1,500);
                handler.postDelayed(animator_btn2,1000);
                handler.postDelayed(animator_btn3,1500);
                handler.postDelayed(animator_btn4,2000);
            }
        };
        start.run();
    }
    public void startAnimation_search(){
        animator_title.start();
    }
    private class MyListener_main implements android.view.animation.Animation.AnimationListener {
        @Override
        public void onAnimationStart(android.view.animation.Animation animation) {
        }
        @Override
        public void onAnimationEnd(android.view.animation.Animation animation) {
                title_main.setVisibility(View.VISIBLE);
        }
        @Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {

        }
    }
    private class MyListener_btn implements android.view.animation.Animation.AnimationListener {
        @Override
        public void onAnimationStart(android.view.animation.Animation animation) {
        }
        @Override
        public void onAnimationEnd(android.view.animation.Animation animation) {
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            btn4.setVisibility(View.VISIBLE);
        }
        @Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {


        }
    }

    private class MyListener_search implements android.view.animation.Animation.AnimationListener {
        @Override
        public void onAnimationStart(android.view.animation.Animation animation) {

        }

        @Override
        public void onAnimationEnd(android.view.animation.Animation animation) {
            title_search.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {

        }
    }
}
