package com.nmr.app.core;

import java.nio.file.Files;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.svc.ConfigAccessService;
import com.nmr.app.svc.ReportServiceMgr;

/**
 * �T�[�r�X�N�����ɌĂяo�����N���X�B
 *
 * @author nomu.shunn
 */
public class ServiceActivator {

	public static void main(String[] args) {
		try {
			// ���O�T�[�r�X�̏�����
			ServiceLogger.init();
			ServiceLogger.info("WebRpt start.");

			// ���|�[�g�쐬
			new ReportServiceMgr().create(Files.list(ConfigAccessService.getWorkingDirPath()));

		} catch (Throwable e) {
			ServiceLogger.error("WebRpt interrupted incorrectly.");
			ServiceLogger.trace(e);
		} finally {
			// ���O�T�[�r�X���I��
			ServiceLogger.info("WebRpt end.");
			ServiceLogger.terminate();
		}
	}

}
