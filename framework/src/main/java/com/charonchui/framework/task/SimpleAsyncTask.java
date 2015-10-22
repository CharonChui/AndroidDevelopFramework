package com.charonchui.framework.task;

import android.os.Handler;

/**
 * Created by Administrator on 2015/10/22.
 */
public abstract class SimpleAsyncTask {
    private Handler handler = new Handler();

    /**
     * 后台任务执行之前 提示用户的界面操作.
     */
    public abstract void onPreExecute();

    /**
     * 后台任务执行之后 更新界面的操作.
     */
    public abstract void onPostExecute(boolean success);

    /**
     * 在后台执行的一个耗时的操作.
     */
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
