package com.nmr.app.svc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.nmr.app.log.ServiceLogger;

/**
 * ファイルアクセスの共通処理が定義されるクラス。
 *
 * @author nomu.shunn
 */
public class CommonFileAccessService {

    /**
     * ファイルを解析して記述内容を行ごとにリストにして取得する。
     * @param path 解析対象ファイルのパス
     * @return 解析されたファイル内容
     * @throws IOException 解析に失敗
     */
    public static List<String> getLines(String path) throws IOException {
        List<String> contents = null;
        try {
            contents = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch(IOException e) {
            ServiceLogger.error("Fail to get contents of \"" + path + "\".");
            throw e;
        }
        return contents;
    }
}
