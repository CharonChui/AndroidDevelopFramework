package com.charonchui.framework.task;


public abstract class BaseTaskExecuteAdapter<Result> implements
		BaseAsyncTask.TaskExecuteListener<Result> {

	@Override
	public void onPreExecute() {

	}

	@Override
	public abstract void onPostExecute(Result result);

	@Override
	public void onCancelled() {

	}
}
