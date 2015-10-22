package com.charonchui.framework.task;

import android.os.AsyncTask;

/**
 * 重写run方法
 * 
 * @param <Params>
 *            传递的参数
 * @param <Progress>
 *            进度
 * @param <Result>
 *            结果，如果执行失败传递null
 * @notice 为了能有效的使用，你不能传递<Void>,及时执行完后没有任何返回值，你最少要返回Boolean、
 *         Integer或String等对象，方便在得到结果后判断其是否为null。以此来判断执行是否成功。
 */

public abstract class BaseAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {
	// 用此来保存结果
	protected Result result;
	private TaskExecuteListener<Result> mTaskExecuteListener;

	public BaseAsyncTask(TaskExecuteListener<Result> baseAsyncTaskInter) {
		super();
		this.mTaskExecuteListener = baseAsyncTaskInter;
	}

	/**
	 * 设置AsyncTask执行的监听器
	 * 
	 * @param taskExecuteListener
	 */
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
	 * doInBackground内需要执行的内容，结果将通过返回值返回，在onPostExecute中会自动去setResult
	 * 
	 * @param params
	 * @return if 当执行出错时返回null。如果执行成功，那么会返回程序后的数据
	 */
	protected abstract Result run(Params... params);

	/**
	 * 将Task后台运行的结果进行设置，以便能通过getResut得到
	 */
	private void setResult(Result result) {
		this.result = result;
	}

	/**
	 * AsyncTask执行的监听
	 * 
	 * @Result 后台任务执行时返回的结果类型
	 */
	public interface TaskExecuteListener<Result> {
		/**
		 * 开始执行前的回调
		 */
		void onPreExecute();

		/**
		 * 执行完后的回调，可通过参数获取到后台运行的结果
		 * 
		 * @param result
		 *            执行返回的结果，如果执行失败会为null
		 */
		void onPostExecute(Result result);

		/**
		 * 取消时的回调，大多数情况用不到。
		 */
		void onCancelled();
	}

}
