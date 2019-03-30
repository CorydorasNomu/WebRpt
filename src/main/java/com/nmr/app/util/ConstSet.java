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
		TXT(".txt");

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
		NEW_LINE(System.lineSeparator()),
		UTF8("UTF-8"),
		TIME_FORMAT("MMdd_HHmmss");

		private final String str;

		private Util(String s) {
			str = s;
		}

		public String get() {
			return str;
		}
	}
}