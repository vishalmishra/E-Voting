package com.example.vishal.e_voting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.flaviofaria.kenburnsview.KenBurnsView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KenBurnsView kenBurnsView= (KenBurnsView) findViewById(R.id.kbview);
        kenBurnsView.animate().setDuration(500).start();
        ShimmerFrameLayout shimmer=(ShimmerFrameLayout)findViewById(R.id.shimmertv);
        shimmer.setDuration(1500);
        shimmer.setAutoStart(true);

        Thread timerThread=new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent=new Intent(MainActivity.this,Login.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
