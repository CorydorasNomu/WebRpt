package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import com.nmr.app.util.ConstSet;
import com.nmr.app.util.ConstSet.Util;

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
	public ReportServiceMgr() {
		ConfigAccessService.init();
	}

	/**
	 * レポートを作成する。
	 * @param dirStream レポート出力対象の作業ディレクトリのパスストリーム
	 * @throws IOException レポートの作成に失敗
	 */
	public void create(Stream<Path> dirStream) throws IOException {
		// レポート出力ディレクトリの作成
		Files.createDirectory(getReportDirPath());
		// HTMLサービスの生成
		HTMLService htmlService = new HTMLService(getReportDirPath());

		// レポートのインデックス作成
		htmlService.createIndex(dirStream);
		// スタイルシート作成
		htmlService.createCSS();
		// レポートページ作成
		htmlService.createHTML();
	}

	/**
	 * レポート出力ディレクトリのパスを取得。
	 * @return path レポート出力ディレクトリのパス
	 */
	private Path getReportDirPath() {
		// 一意のディレクトリ名を日時から生成
		SimpleDateFormat sdf = new SimpleDateFormat(Util.TIME_FORMAT.get());
		String date = sdf.format(new Date(System.currentTimeMillis()));

		// ディレクトリ名を生成
		String reportDirName = Util.EMPTY.get();
		String str = ConfigAccessService.getWorkingDirPath().toString();
		if(str.endsWith(ConstSet.Path.SEPARATOR.get())) {
			reportDirName = REPORT_DIR_PREFIX + date;
		} else {
			reportDirName = ConstSet.Path.SEPARATOR.get() + REPORT_DIR_PREFIX + date;
		}

		return Paths.get(str + reportDirName);
	}
}
