package com.nmr.app.log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nmr.app.util.ConstSet.Extension;
import com.nmr.app.util.ConstSet.Symbol;
import com.nmr.app.util.ConstSet.TimeStamp;

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
    private static List<String> log = new ArrayList<>();
    // ログ情報レベルのヘッダ
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
        // 既にログファイルがあれば削除
        if (logFile != null && logFile.exists()) logFile.delete();
        info("ServiceLogger initialized successfully.");
    }

    /**
     * ログサービスの終了
     */
    public static void terminate() {
        // バッファリングされているログ内容の書き出し
        try {
            String logFile = Symbol.CURRENT.get() + LOG_FILE + Extension.LOG.get();
            Files.write(Paths.get(logFile), log,
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * INFOレベルのログ内容をバッファに書き込む。
     * @param msg メッセージ
     */
    public static void info(String msg) {
        log.add(timeStamp() + Level.INFO.get() + msg);
    }

    /**
     * WARNレベルのログ内容をバッファに書き込む。
     * @param msg メッセージ
     */
    public static void warn(String msg) {
        log.add(timeStamp() + Level.WARN.get() + msg);
    }

    /**
     * ERRORレベルのログ内容をバッファに書き込む。
     * @param msg メッセージ
     */
    public static void error(String msg) {
        log.add(timeStamp() + Level.ERROR.get() + msg);
    }

    /**
     * ERRORレベルのログ内容とトレースをバッファに書き込む。
     * @param msg メッセージ
     */
    public static void error(String msg, Throwable t) {
        log.add(timeStamp() + Level.ERROR.get() + msg);
        trace(t);
    }

    /**
     * スタックトレースの内容をバッファに書き込む。
     * @param t 例外オブジェクト
     */
    public static void trace(Throwable t) {
        log.add(t.toString());
        StackTraceElement[] elm = t.getStackTrace();
        for(StackTraceElement  e : elm) {
            log.add(Symbol.TAB.get() + e.toString());
        }
    }

    private static String timeStamp() {
        // 現在時刻
        Date stamp = new Date(System.currentTimeMillis());
        // ログの日付部を生成
        String date = new SimpleDateFormat(TimeStamp.DATE.get()).format(stamp);
        // ログの時間部を生成
        String time = new SimpleDateFormat(TimeStamp.TIME.get()).format(stamp);

        return date + Symbol.SPACE.get() + time + Symbol.SPACE.get();
    }
}
