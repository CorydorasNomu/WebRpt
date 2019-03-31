package com.nmr.app.core;

import java.nio.file.Files;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.svc.ConfigAccessService;
import com.nmr.app.svc.ReportServiceMgr;

/**
 * サービス起動時に呼び出されるクラス。
 *
 * @author nomu.shunn
 */
public class ServiceActivator {

	public static void main(String[] args) {
		try {
			// ログサービスの初期化
			ServiceLogger.init();
			ServiceLogger.info("WebRpt start.");

			// レポート作成
			new ReportServiceMgr().create(Files.list(ConfigAccessService.getWorkingDirPath()));

		} catch (Throwable e) {
			ServiceLogger.error("WebRpt interrupted incorrectly.");
			ServiceLogger.trace(e);
		} finally {
			// ログサービスを終了
			ServiceLogger.info("WebRpt end.");
			ServiceLogger.terminate();
		}
	}

}
