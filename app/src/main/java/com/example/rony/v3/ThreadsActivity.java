package com.example.rony.v3;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class ThreadsActivity extends AppCompatActivity {

    ArrayList<MyRunnable> mRunnablesArray;
    MyRunnable mRunningThread;
    Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRunnablesArray = new ArrayList<>();
        mRunningThread = null;
        mHandler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);

        Button createButton = findViewById(R.id.createButton1);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRunnablesArray.size() < 10) {
                    mRunnablesArray.add(new MyRunnable());
                } else {
                    Toast.makeText(ThreadsActivity.this, "Can't Create more than ten Tasks at a Time!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button startButton = findViewById(R.id.startButton1);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRunningThread != null) {
                    Toast.makeText(ThreadsActivity.this, "A Thread is Using The View!", Toast.LENGTH_LONG).show();
                } else if (mRunnablesArray.size() == 0) {
                    Toast.makeText(ThreadsActivity.this, "There is No Tasks to Start!", Toast.LENGTH_LONG).show();
                } else {
                    mRunningThread = mRunnablesArray.get(0);
                    new Thread(mRunningThread).start();
                    mRunnablesArray.remove(0);
                }

            }
        });

        Button stopButton = findViewById(R.id.cancelButton1);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRunningThread != null) {
                    mRunningThread.stop();
                    mRunningThread = null;
                } else {
                    Toast.makeText(ThreadsActivity.this, "There is No Thread Running!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private class MyRunnable implements Runnable {
        private boolean stopped;

        public void stop() {
            stopped = true;
        }

        @Override
        public void run() {
            stopped = false;
            for (int i = 0; i < 10; i++) {
                if (!stopped) {
                    final int tmp = i;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView counterView = findViewById(R.id.counterView1);
                            counterView.setText(String.valueOf(tmp + 1));
                        }
                    });
                    try {
                        Thread.sleep(500, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView counterView = findViewById(R.id.counterView1);
                            counterView.setText("Canceled!");
                        }
                    });
                }
            }
            if (!stopped) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView counterView = findViewById(R.id.counterView1);
                        counterView.setText("Done!");
                    }
                });
            }

            mRunningThread = null;
        }
    }
}
