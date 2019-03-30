package com.nmr.app.core;

import java.io.IOException;
import java.nio.file.Files;

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
			// レポート作成
			new ReportServiceMgr().create(Files.list(ConfigAccessService.getWorkingDirPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
