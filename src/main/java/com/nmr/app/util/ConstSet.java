package com.nmr.app.util;

/**
 * 汎用的に使用される定数を管理するクラス。
 *
 * @author nomu.shunn
 */
public class ConstSet {

	public enum Path {
		CURRENT("./"),
		SEPARATOR("/");

		private final String str;

		private Path(String s) {
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

	public enum Util {
		SPACE(" "),
		EMPTY(""),
		ATB("\t"),
		NEW_LINE(System.lineSeparator()),
		UTF8("UTF-8"),
		DIR_TIMESTAMP("MMdd_HHmmss"),
		LOG_TIMESTAMP_DATE("yyy-MM-dd"),
		LOG_TIMESTAMP_TIME("HH:mm:ss.SSS");

		private final String str;

		private Util(String s) {
			str = s;
		}

		public String get() {
			return str;
		}
	}
}