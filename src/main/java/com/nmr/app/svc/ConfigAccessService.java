package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet.*;

/**
 * �ݒ�t�@�C��"config.ini"�ւ̃A�N�Z�T�N���X�B
 *
 * @author nomu.shunn
 */
public class ConfigAccessService extends CommonFileAccessService {

	// �ݒ�t�@�C���̃p�X
	private static final String CONFIG_INI = "./config" + Extension.INI.get();
	// ��ƃf�B���N�g���p�X�����ʂ��邽�߂̐ړ�����
	private static final String PATH_KEY = "path=";
	// ��ƃf�B���N�g���̃p�X
	private static String path = Util.EMPTY.get();

	/**
	 * �������Bconfig.ini����͂��āA��ƃf�B���N�g���̃p�X��ݒ�B
	 */
	public static void init() {
		try {
			System.out.println(CONFIG_INI);
			getLines(CONFIG_INI).forEach(s -> {
				if(s.startsWith(PATH_KEY)) {
					path = s.replaceFirst(PATH_KEY, Util.EMPTY.get()).trim();
				}
	        });
		} catch(IOException e) {
			ServiceLogger.error(Util.EMPTY.get());
			e.printStackTrace();
		}
	}

	/**
	 * ��ƃf�B���N�g���̃p�X���擾�B
	 * @return ��ƃf�B���N�g���̃p�X
	 */
	public static Path getWorkingDirPath() {
		return Paths.get(path);
	}
}
