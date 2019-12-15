package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet.Extension;
import com.nmr.app.util.ConstSet.Symbol;

/**
 * 設定ファイル"config.ini"へのアクセサクラス。
 *
 * @author nomu.shunn
 */
public class ConfigAccessService extends CommonFileAccessService {

    // 設定ファイルのパス
    private static final String CONFIG_INI = Symbol.CURRENT.get() + "config" + Extension.INI.get();
    // 作業ディレクトリパスを識別するための接頭文字
    private static final String PATH_KEY = "path=";
    // パラメタテーブルのヘッダ群を識別するための接頭文字
    private static final String TABLE_KEY = "table=";
    // 作業ディレクトリのパス
    private static String path = Symbol.EMPTY.get();
    // パラメタテーブルのヘッダ群
    private static ArrayList<String> tableHeaders = new ArrayList<>();

    /**
     * 初期化。config.iniを解析
     */
    public static void init() throws IOException {
        try {
            getLines(CONFIG_INI).forEach(s -> {
                if(s.startsWith(PATH_KEY)) {
                    // 作業ディレクトリのパスを取得
                    path = s.replaceFirst(PATH_KEY, Symbol.EMPTY.get()).trim();
                } else if(s.startsWith(TABLE_KEY)) {
                    // パラメタテーブルのヘッダを取得
                    String[] arrays = s.replaceFirst(TABLE_KEY, Symbol.EMPTY.get()).trim().split(Symbol.COMMA.get());
                    for(String r : arrays) {
                        tableHeaders.add(r.trim());
                    }
                }
            });
        } catch(IOException e) {
            ServiceLogger.error("Fail to parse the ini file.");
            throw e;
        }
    }

    /**
     * 作業ディレクトリのパスを取得。
     * @return 作業ディレクトリのパス
     */
    public static Path getWorkingDirPath() {
        return Paths.get(path);
    }

    /**
     * パラメタテーブルのヘッダを取得。
     * @return パラメタテーブルのヘッダのリスト
     */
    public static ArrayList<String> getParamTableHeaders() {
        return tableHeaders;
    }
}
