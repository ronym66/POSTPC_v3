package com.example.rony.v3;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AsyncTaskActivity extends AppCompatActivity {

    ArrayList<MyAsyncTask> mTasksArray;
    MyAsyncTask mRunningTask;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTasksArray = new ArrayList<>();
        mRunningTask = null;
        mHandler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        Button createButton = findViewById(R.id.createButton2);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTasksArray.size() < 10) {
                    mTasksArray.add(new AsyncTaskActivity.MyAsyncTask());
                } else {
                    Toast.makeText(AsyncTaskActivity.this, "Can't Create more than ten Tasks at a Time!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button startButton = findViewById(R.id.startButton2);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRunningTask != null) {
                    Toast.makeText(AsyncTaskActivity.this, "A Task is Using The View!", Toast.LENGTH_LONG).show();
                } else if (mTasksArray.size() == 0) {
                    Toast.makeText(AsyncTaskActivity.this, "There is No Task to Start!", Toast.LENGTH_LONG).show();
                } else {
                    mRunningTask = mTasksArray.get(0);
                    mTasksArray.remove(0);
                    mRunningTask.execute();
                }

            }
        });

        Button stopButton = findViewById(R.id.cancelButton2);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRunningTask != null) {
                    mRunningTask.cancel(true);
                    mRunningTask = null;
                } else {
                    Toast.makeText(AsyncTaskActivity.this, "There is No Task Running!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 10; i++) {
                if(!isCancelled()) {
                    final int tmp = i;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView counterView = findViewById(R.id.counterView2);
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
                            TextView counterView = findViewById(R.id.counterView2);
                            counterView.setText("Canceled!");
                        }
                    });
                    mRunningTask = null;
                    return null;
                }
            }
            if(!isCancelled()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView counterView = findViewById(R.id.counterView2);
                        counterView.setText("Done!");
                    }
                });
            }

            mRunningTask = null;
            return null;
        }
    }
}
