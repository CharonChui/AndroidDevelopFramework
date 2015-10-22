package com.charonchui.framework.task;

import android.os.Handler;

public abstract class SimpleAsyncTask {
    private Handler handler = new Handler();

    public abstract void onPreExecute();
    public abstract void onPostExecute(boolean success);
    public abstract boolean doInBackground();

    public void execute() {
        onPreExecute();
        new Thread() {
            public void run() {
                final boolean success = doInBackground();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        onPostExecute(success);
                    }
                });
            };
        }.start();
    }
}
