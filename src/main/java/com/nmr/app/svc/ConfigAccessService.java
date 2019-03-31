package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet;
import com.nmr.app.util.ConstSet.Extension;
import com.nmr.app.util.ConstSet.Util;

/**
 * 設定ファイル"config.ini"へのアクセサクラス。
 *
 * @author nomu.shunn
 */
public class ConfigAccessService extends CommonFileAccessService {

	// 設定ファイルのパス
	private static final String CONFIG_INI = ConstSet.Path.CURRENT.get() + "config" + Extension.INI.get();
	// 作業ディレクトリパスを識別するための接頭文字
	private static final String PATH_KEY = "path=";
	// 作業ディレクトリのパス
	private static String path = Util.EMPTY.get();

	/**
	 * 初期化。config.iniを解析して、作業ディレクトリのパスを設定。
	 */
	public static void init() throws IOException {
		try {
			getLines(CONFIG_INI).forEach(s -> {
				if(s.startsWith(PATH_KEY)) {
					path = s.replaceFirst(PATH_KEY, Util.EMPTY.get()).trim();
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
}
