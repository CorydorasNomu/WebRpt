package com.nmr.app.util;

import java.util.Arrays;
import java.util.List;

/**
 * 汎用的に使用される定数を管理するクラス。
 *
 * @author nomu.shunn
 */
public class ConstSet {

    public static final String UTF8 = "UTF-8";

    public static final List<String> TableHeaderTags = Arrays.asList(
        "HEADER_0",
        "HEADER_1",
        "HEADER_2",
        "HEADER_3",
        "HEADER_4",
        "HEADER_5",
        "HEADER_6",
        "HEADER_7",
        "HEADER_8"
    );

    public static final List<String> TableValueTags = Arrays.asList(
        "VAL_0",
        "VAL_1",
        "VAL_2",
        "VAL_3",
        "VAL_4",
        "VAL_5",
        "VAL_6",
        "VAL_7",
        "VAL_8"
    );

    public static final List<String> ImagePathTag = Arrays.asList(
        "%img00%",
        "%img01%"
    );

    public enum Symbol {
        CURRENT("./"),
        SEPARATOR("/"),
        ESCAPE("\\\\"),
        SPACE(" "),
        EMPTY(""),
        COMMA(","),
        TAB("\t"),
        NEW_LINE(System.lineSeparator());

        private final String symbol;

        private Symbol(String s) {
            symbol = s;
        }
        public String get() {
            return symbol;
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

        private final String ext;

        private Extension(String s) {
            ext = s;
        }
        public String get() {
            return ext;
        }
    }

    public enum TimeStamp {
        DIR("MMdd_HHmmss"),
        DATE("yyy-MM-dd"),
        TIME("HH:mm:ss.SSS");

        private final String stamp;

        private TimeStamp(String s) {
            stamp = s;
        }
        public String get() {
            return stamp;
        }
    }
}