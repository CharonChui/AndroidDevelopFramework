package com.charonchui.framework.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些与InputStream相关的工具类
 */
public class IOUtil {

	/**
	 * 将InputStream流转换成BitmapDrawable。
	 * 
	 * @param is
	 *            InputStream流
	 * @return BitmapDrawable是Drawable的直接子类，可用于Drawable对象。
	 * @throws IOException
	 */
	public static BitmapDrawable toBitmapDrawable(InputStream is)
			throws IOException {
		@SuppressWarnings("deprecation")
		BitmapDrawable bitmapDrawable = new BitmapDrawable(is);
		is.close();
		return bitmapDrawable;
	}

	/**
	 * 将InputStream流转换成Bitmap对象。
	 * 
	 * @param is
	 *            InputStream对象
	 * @return Bitmap对象
	 * @throws IOException
	 */
	public static Bitmap toBitmap(InputStream is) throws IOException {
		if (null == is)
			return null;
		return toBitmapDrawable(is).getBitmap();
	}

	/**
	 * 将InputStream转换成StringBuilder对象。
	 * 
	 * @param is
	 *            InputStream对象
	 * @return StringBuilder
	 * @throws IOException
	 */
	public static StringBuilder toStringBuffer(InputStream is)
			throws IOException {
		if (null == is)
			return null;
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuilder buffer = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) {
			buffer.append(line).append("\n");
		}
		is.close();
		return buffer;
	}

	/**
	 * 将InputStream转换成字符串。
	 * 
	 * @param is
	 *            InputStream对象
	 * @return 字符串
	 * @throws IOException
	 */
	public static String toString(InputStream is) throws IOException {
		if (null == is)
			return null;
		return toStringBuffer(is).toString();
	}

	/**
	 * 将InputStream流转换为字符串，并指定编码类型。
	 * 
	 * @param is
	 *            InputStream流
	 * @param encoding
	 *            编码类型
	 * @return 字符串
	 * @throws IOException
	 */
	public static String convertToString(InputStream is, String encoding)
			throws IOException {
		if (null == is)
			return null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				encoding));
		char cache[] = new char[1024];
		int cacheSize = -1;
		StringBuilder buffer = new StringBuilder();
		while ((cacheSize = reader.read(cache)) != -1) {
			buffer.append(new String(cache, 0, cacheSize));
			cacheSize = reader.read(cache);
		}
		is.close();
		return buffer.toString();
	}

	/**
	 * 将InputStream转换成字节数组。
	 * 
	 * @param is
	 *            InputStream对象
	 * @return 字节数组
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream is) throws IOException {
		if (null == is)
			return null;
		byte[] cache = new byte[2 * 1024];
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		for (int length; (length = is.read(cache)) != -1;) {
			buffer.write(cache, 0, length);
		}
		is.close();
		return buffer.toByteArray();
	}

	/**
	 * 获取流编码类型
	 * 
	 * @param is
	 *            InputStream流
	 * @return 编码类型
	 * @throws IOException
	 */
	public static String getStreamEncoding(InputStream is) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		bis.mark(2);
		byte[] first3bytes = new byte[3];
		bis.read(first3bytes);
		bis.reset();
		String encoding = null;
		if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
				&& first3bytes[2] == (byte) 0xBF) {
			encoding = "utf-8";
		} else if (first3bytes[0] == (byte) 0xFF
				&& first3bytes[1] == (byte) 0xFE) {
			encoding = "unicode";
		} else if (first3bytes[0] == (byte) 0xFE
				&& first3bytes[1] == (byte) 0xFF) {
			encoding = "utf-16be";
		} else if (first3bytes[0] == (byte) 0xFF
				&& first3bytes[1] == (byte) 0xFF) {
			encoding = "utf-16le";
		} else {
			encoding = "GBK";
		}
		return encoding;
	}

	/**
	 * 获取HTML文本流的编码类型
	 * 
	 * @param is
	 *            HTML文本流
	 * @return 编码类型
	 * @throws IOException
	 */
	public static String getEncodingFromHTML(InputStream is) throws IOException {
		final int FIND_CHARSET_CACHE_SIZE = 4 * 1024;
		BufferedInputStream bis = new BufferedInputStream(is);
		bis.mark(FIND_CHARSET_CACHE_SIZE);
		byte[] cache = new byte[FIND_CHARSET_CACHE_SIZE];
		bis.read(cache);
		bis.reset();
		return getHtmlCharset(new String(cache));
	}

	/**
	 * 获取HTML文本字符集类型
	 * 
	 * @param content
	 *            HTML内容
	 * @return 字符集类型
	 */
	public static String getHtmlCharset(String content) {
		String encoding = null;
		final String CHARSET_REGX = "<meta.*charset=\"?([a-zA-Z0-9-_/]+)\"?";
		Matcher m = Pattern.compile(CHARSET_REGX).matcher(content);
		if (m.find()) {
			encoding = m.group(1);
		}
		return encoding;
	}
}
