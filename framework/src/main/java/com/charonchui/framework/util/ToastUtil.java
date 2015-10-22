package com.charonchui.framework.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.charonchui.framework.R;

/**
 * Custom Toast utility, cancel the previous toast when showing a toast.
 */
public class ToastUtil {
	public static final int LENGTH_SHORT = 0;
	public static final int LENGTH_LONG = 1;
	private static View mToastView;
	private static WindowManager mWindowManager;
	private static int sDuration;

	private final static int WHAT = 100;
	private static View mOldView;

	/**
	 * Use this to get the location of the toast.
	 */
	private static Toast mToast;
	private static TextView mTextView;
	private WindowManager.LayoutParams mLayoutParams;

	private static ToastUtil instance = null;

	private static Handler toastHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			cancelOldAlert();
			int id = msg.what;
			if (WHAT == id) {
				cancelCurrentAlert();
			}
		}
	};

	@SuppressLint("ShowToast")
	private ToastUtil(Context context) {
		mWindowManager = (WindowManager) context.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

		mToastView = LayoutInflater.from(context).inflate(R.layout.toast_view,
				null);
		mTextView = (TextView) mToastView.findViewById(R.id.toast_text);

		mLayoutParams = new WindowManager.LayoutParams();
		mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		mLayoutParams.format = PixelFormat.TRANSLUCENT;
		mLayoutParams.windowAnimations = android.R.style.Animation_Toast;
		mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
		mLayoutParams.setTitle("Toast");
		mLayoutParams.gravity = mToast.getGravity();
	}

	private static ToastUtil getInstance(Context context) {
		if (instance == null) {
			synchronized (ToastUtil.class) {
				if (instance == null) {
					instance = new ToastUtil(context);
				}
			}
		}
		return instance;
	}

	public static ToastUtil makeText(Context context, CharSequence text,
			int duration) {
		ToastUtil util = getInstance(context);
		sDuration = duration;
		mToast.setText(text);
		mTextView.setText(text);
		return util;
	}

	public static ToastUtil makeText(Context context, int resId, int duration) {
		ToastUtil util = getInstance(context);
		sDuration = duration;
		mToast.setText(resId);
		mTextView.setText(context.getResources().getString(resId));
		return util;
	}

	/**
	 * Show the toast with specified time
	 */
	public void show() {
		long time = 0;
		switch (sDuration) {
		case LENGTH_SHORT:
			time = 2000;
			break;
		case LENGTH_LONG:
			time = 3500;
			break;
		default:
			time = 2000;
			break;
		}

		// cancel the previous toast
		cancelOldAlert();
		toastHandler.removeMessages(WHAT);
		mLayoutParams.y = mToast.getYOffset();
		mWindowManager.addView(mToastView, mLayoutParams);

		mOldView = mToastView;
		toastHandler.sendEmptyMessageDelayed(WHAT, time);
	}

	private static void cancelOldAlert() {
		if (mOldView != null && mOldView.getParent() != null) {
			mWindowManager.removeView(mOldView);
		}
	}

	public static void cancelCurrentAlert() {
		if (mToastView != null && mToastView.getParent() != null) {
			mWindowManager.removeView(mToastView);
		}
	}
}
