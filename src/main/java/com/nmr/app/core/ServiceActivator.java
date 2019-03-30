package com.nmr.app.core;

import java.io.IOException;
import java.nio.file.Files;

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
			// ���|�[�g�쐬
			new ReportServiceMgr().create(Files.list(ConfigAccessService.getWorkingDirPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
