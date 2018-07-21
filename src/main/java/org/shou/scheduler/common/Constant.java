package org.shou.scheduler.common;

import java.util.Calendar;

public class Constant {
	static final String CONTEXT_PATH = "contextPath";/***项目根路径*/
	static String VERSION = String.valueOf(System.currentTimeMillis());//版本号，重启的时间
	static final int NOW_YEAY = Calendar.getInstance().get(Calendar.YEAR);
}
