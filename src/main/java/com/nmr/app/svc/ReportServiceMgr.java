package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet.Symbol;
import com.nmr.app.util.ConstSet.TimeStamp;

/**
 * レポートページ作成サービスの管理クラス。
 *
 * @author nomu.shunn
 */
public class ReportServiceMgr {

    // レポート出力用ディレクトリを識別するための接頭文字
    static final String REPORT_DIR_PREFIX = "report_";

    /**
     * コンストラクタ
     */
    public ReportServiceMgr() throws IOException {
        ConfigAccessService.init();
    }

    /**
     * レポートを作成する。
     * @param dirStream レポート出力対象の作業ディレクトリのパスストリーム
     * @throws IOException レポートの作成に失敗
     */
    public void create(Stream<Path> dirStream) throws IOException {
        // レポート出力ディレクトリの作成
        createDirectory();
        // HTMLサービスの生成
        HTMLService htmlService = new HTMLService(getReportDirPath());

        // レポートのインデックス作成
        htmlService.createIndex(dirStream);
        // スタイルシート作成
        htmlService.createCSS();
        // レポートページ作成
        htmlService.createHTML();
    }

    // レポート出力ディレクトリのパスを取得
    private Path getReportDirPath() {
        // ディレクトリ名のタイムスタンプ部を生成
        SimpleDateFormat sdf = new SimpleDateFormat(TimeStamp.DIR.get());
        String date = sdf.format(new Date(System.currentTimeMillis()));

        // ディレクトリ名を生成
        String reportDirName = Symbol.EMPTY.get();
        String str = ConfigAccessService.getWorkingDirPath().toString();
        if(str.endsWith(Symbol.SEPARATOR.get())) {
            reportDirName = REPORT_DIR_PREFIX + date;
        } else {
            reportDirName = Symbol.SEPARATOR.get() + REPORT_DIR_PREFIX + date;
        }

        return Paths.get(str + reportDirName);
    }

    private void createDirectory() throws IOException {
        try {
            Files.createDirectory(getReportDirPath());
        } catch(IOException e) {
            ServiceLogger.error("Fail to create a report directory. Target Path: " + getReportDirPath().toString());
            throw e;
        }
    }
}
