package com.nmr.app.util;

import java.util.ArrayList;

/**
 * 汎用的に使用される定数を管理するクラス。
 *
 * @author nomu.shunn
 */
public class ConstSet {

	public enum FilePath {
		CURRENT("./"),
		SEPARATOR("/");

		private final String str;

		private FilePath(String s) {
			str = s;
		}
		public String get() {
			return str;
		}
	}

	public enum Extension {
		HTML(".html"),
		CSS(".css"),
		JPG(".jpg"),
		PNG(".png"),
		INI(".ini"),
		TXT(".txt"),
		LOG(".log");

		private final String str;

		private Extension(String s) {
			str = s;
		}
		public String get() {
			return str;
		}
	}

	public enum Regex {
		ESCAPE("\\\\");

		private final String str;

		private Regex(String s) {
			str = s;
		}
		public String get() {
			return str;
		}
	}

	public enum Common {
		SPACE(" "),
		EMPTY(""),
		COMMA(","),
		ATB("\t"),
		NEW_LINE(System.lineSeparator()),
		UTF8("UTF-8"),
		DIR_TIMESTAMP("MMdd_HHmmss"),
		LOG_TIMESTAMP_DATE("yyy-MM-dd"),
		LOG_TIMESTAMP_TIME("HH:mm:ss.SSS");

		private final String str;

		private Common(String s) {
			str = s;
		}
		public String get() {
			return str;
		}
	}

	public enum TABLE_HEADER {
		HEADER_0("HEADER_0"),
		HEADER_1("HEADER_1"),
		HEADER_2("HEADER_2"),
		HEADER_3("HEADER_3"),
		HEADER_4("HEADER_4"),
		HEADER_5("HEADER_5"),
		HEADER_6("HEADER_6"),
		HEADER_7("HEADER_7"),
		HEADER_8("HEADER_8");

		private final String str;

		private TABLE_HEADER(String s) {
			str = s;
		}

		/**
		 * パラメタテーブルの値置換のためのキーリストを取得する。
		 * @return ヘッダ置換のためのキーリスト
		 */
		public static ArrayList<String> get() {
			ArrayList<String> keyList = new ArrayList<>();
			for (TABLE_HEADER num : values()) {
	            keyList.add(num.val());
	        }
			return keyList;
		}
		private String val() {
			return str;
		}
	}

	public enum TABLE_VALUE {
		VAL_0("VAL_0"),
		VAL_1("VAL_1"),
		VAL_2("VAL_2"),
		VAL_3("VAL_3"),
		VAL_4("VAL_4"),
		VAL_5("VAL_5"),
		VAL_6("VAL_6"),
		VAL_7("VAL_7"),
		VAL_8("VAL_8");

		private final String str;

		private TABLE_VALUE(String s) {
			str = s;
		}

		/**
		 * パラメタテーブルのヘッダ置換のためのキーリストを取得する。
		 * @return ヘッダ置換のためのキーリスト
		 */
		public static ArrayList<String> get() {
			ArrayList<String> keyList = new ArrayList<>();
			for (TABLE_VALUE num : values()) {
	            keyList.add(num.val());
	        }
			return keyList;
		}
		private String val() {
			return str;
		}
	}
}