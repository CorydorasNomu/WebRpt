package com.nmr.app.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nmr.app.util.ConstSet.Common;
import com.nmr.app.util.ConstSet.Extension;
import com.nmr.app.util.ConstSet.FilePath;

/**
 * 共通のログサービスクラス。
 *
 * @author nomu.shunn
 */
public class ServiceLogger {

	// ログファイル名
	private static final String LOG_FILE = "svc";
	// ログファイル
	private static File logFile = null;
	// ログ内容を保持するバッファ
	private static StringBuilder logBuilder = null;

	private enum Level {
		INFO("INFO :"),
		WARN("WARN :"),
		ERROR("ERROR:"),
		TRACE("TRACE:");

		private final String str;

		private Level(String s) {
			str = s;
		}
		public String get() {
			return str;
		}
	}

	/**
	 * ログサービスの初期化
	 */
	public static void init() {
		logFile = new File(FilePath.CURRENT.get() + LOG_FILE + Extension.LOG.get());
		if (logFile != null && logFile.exists())	// 既にログファイルがあれば削除
			logFile.delete();
		logBuilder = new StringBuilder();		// ログバッファの生成
		info("ServiceLogger initialized successfully.");
	}

	/**
	 * ログサービスの終了
	 */
	public static void terminate() {
		// バッファリングされているログ内容の書き出し
		try(FileWriter w = new FileWriter(logFile)) {
			w.write(logBuilder.toString());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * INFOレベルのログ内容をバッファに書き込む。
	 * @param msg メッセージ
	 */
	public static void info(String msg) {
		create(Level.INFO.get() + msg);
	}

	/**
	 * WARNレベルのログ内容をバッファに書き込む。
	 * @param msg メッセージ
	 */
	public static void warn(String msg) {
		create(Level.WARN.get() + msg);
	}

	/**
	 * ERRORレベルのログ内容とトレースをバッファに書き込む。
	 * @param msg メッセージ
	 */
	public static void error(String msg) {
		create(Level.ERROR.get() + msg);
	}

	/**
	 * ERRORレベルのログ内容とトレースをバッファに書き込む。
	 * @param msg メッセージ
	 */
	public static void error(String msg, Throwable t) {
		create(Level.ERROR.get() + msg);
		trace(t);
	}

	/**
	 * スタックトレースの内容をバッファに書き込む。
	 * @param e スタックトレース
	 */
	public static void trace(Throwable t) {
		logBuilder.append(t.toString() + Common.NEW_LINE.get());
		StackTraceElement[] elm = t.getStackTrace();
		for(StackTraceElement  e : elm) {
			logBuilder.append(Common.ATB.get() + e.toString() + Common.NEW_LINE.get());
		}
	}

	private static String timeStamp() {
		// 現在時刻
		Date stamp = new Date(System.currentTimeMillis());
		// ログの日付部を生成
		String date = new SimpleDateFormat(Common.LOG_TIMESTAMP_DATE.get()).format(stamp);
		// ログの時間部を生成
		String time = new SimpleDateFormat(Common.LOG_TIMESTAMP_TIME.get()).format(stamp);

		return date + Common.SPACE.get() + time + Common.SPACE.get();
	}

	private static void create(String msg) {
		logBuilder.append(timeStamp());
		logBuilder.append(msg + Common.NEW_LINE.get());
	}
}
