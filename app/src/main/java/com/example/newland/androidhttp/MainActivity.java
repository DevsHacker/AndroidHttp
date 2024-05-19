package com.example.newland.androidhttp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static TextView tempText;
    static String tempData;
    static TextView humText;
    static String humData;
    static Button fan;
    static boolean isopen = false;
    static RotateAnimation rotateAnimation;
    static LinearInterpolator linearInterpolator;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tempText.setText("温度数据："+tempData+"°C");
            humText.setText("湿度数据："+humData+"°C");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempText = findViewById(R.id.temp);
        humText = findViewById(R.id.hum);
        fan = findViewById(R.id.fan);

        rotateAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        linearInterpolator = new LinearInterpolator();
        rotateAnimation.setDuration(500);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(linearInterpolator);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Get();
                handler.postDelayed(this,2000);
                handler.sendEmptyMessage(1);
            }
        };
        handler.postDelayed(runnable,2000);
    }

    public static void Get(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                tempData = HttpHelper.get_Data("m_temp");
                humData = HttpHelper.get_Data("m_hum");
            }
        }).start();
    }

    public void Fan(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isopen){
                    isopen = false;
                    HttpHelper.turn(147461,"m_fan","0");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fan.clearAnimation();
                        }
                    });
                }else{
                    isopen = true;
                    HttpHelper.turn(147461,"m_fan","1");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fan.startAnimation(rotateAnimation);
                        }
                    });
                }
            }
        }).start();
    }
}
