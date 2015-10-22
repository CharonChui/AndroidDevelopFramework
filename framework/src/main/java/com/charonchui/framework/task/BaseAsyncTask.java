package com.charonchui.framework.task;

import android.os.AsyncTask;

/**
 * you should override the run() method.
 * @param <Params> params.
 * @param <Progress> progress.
 * @param <Result> result.
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {
	protected Result result;
	private TaskExecuteListener<Result> mTaskExecuteListener;

	public BaseAsyncTask(TaskExecuteListener<Result> baseAsyncTaskInter) {
		super();
		this.mTaskExecuteListener = baseAsyncTaskInter;
	}

	public void setTaskExecuteListener(
			TaskExecuteListener<Result> taskExecuteListener) {
		this.mTaskExecuteListener = taskExecuteListener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mTaskExecuteListener != null) {
			mTaskExecuteListener.onPreExecute();
		}
	}

	@Override
	protected Result doInBackground(Params... params) {
		try {
			return run(params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (mTaskExecuteListener != null) {
			setResult(result);
			mTaskExecuteListener.onPostExecute(result);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (mTaskExecuteListener != null) {
			mTaskExecuteListener.onCancelled();
		}
	}

	/**
	 * Backgroud run task.
	 * @param params params.
	 * @return result.
	 */
	protected abstract Result run(Params... params);

	private void setResult(Result result) {
		this.result = result;
	}

	public interface TaskExecuteListener<Result> {
		void onPreExecute();

		void onPostExecute(Result result);

		void onCancelled();
	}

}
