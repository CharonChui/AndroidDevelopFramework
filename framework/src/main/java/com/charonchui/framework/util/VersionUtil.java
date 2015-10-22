package com.charonchui.framework.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Get the version and check update.
 */
public class VersionUtil {

	/**
	 * Get the current version name.
	 * 
	 * @param context
	 *            Context
	 * @return will return "" if exception occured.
	 */
	public static String getVersionName(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * get current version code
	 * 
	 * @param context
	 *            Context
	 * @return current version code, it will be -1 when exception occur.
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Compares two version strings
	 * 
	 * @param v1
	 *            first version string.
	 * @param v2
	 *            second version string.
	 * @return 0 if the versions are equal, 1 if version v1 is before version
	 *         v2, -1 if version v1 is after version v2, -2 if version format is
	 *         invalid.
	 */
	public static int compareVersions(String v1, String v2) {
		if (v1 == null || v2 == null || v1.trim().equals("")
				|| v2.trim().equals(""))
			return -2;
		else if (v1.equals(v2))
			return 0;
		else {
			boolean valid1 = v1.matches("\\d+\\.\\d+\\.\\d+");
			boolean valid2 = v2.matches("\\d+\\.\\d+\\.\\d+");

			if (valid1 && valid2) {
				int[] nums1;
				int[] nums2;

				try {
					nums1 = convertStringArrayToIntArray(v1.split("\\."));
					nums2 = convertStringArrayToIntArray(v2.split("\\."));
				} catch (NumberFormatException e) {
					return -2;
				}

				if (nums1.length == 3 && nums2.length == 3) {
					if (nums1[0] < nums2[0])
						return 1;
					else if (nums1[0] > nums2[0])
						return -1;
					else {
						if (nums1[1] < nums2[1])
							return 1;
						else if (nums1[1] > nums2[1])
							return -1;
						else {
							if (nums1[2] < nums2[2])
								return 1;
							else if (nums1[2] > nums2[2])
								return -1;
							else {
								return 0;
							}
						}
					}
				} else {
					return -2;
				}
			} else {
				return -2;
			}
		}
	}

	private static int[] convertStringArrayToIntArray(String[] stringArray)
			throws NumberFormatException {
		if (stringArray != null) {
			int intArray[] = new int[stringArray.length];
			for (int i = 0; i < stringArray.length; i++) {
				intArray[i] = Integer.parseInt(stringArray[i]);
			}
			return intArray;
		}
		return null;
	}
}
