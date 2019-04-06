package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet;
import com.nmr.app.util.ConstSet.Extension;
import com.nmr.app.util.ConstSet.Common;

/**
 * �ݒ�t�@�C��"config.ini"�ւ̃A�N�Z�T�N���X�B
 *
 * @author nomu.shunn
 */
public class ConfigAccessService extends CommonFileAccessService {

	// �ݒ�t�@�C���̃p�X
	private static final String CONFIG_INI = ConstSet.FilePath.CURRENT.get() + "config" + Extension.INI.get();
	// ��ƃf�B���N�g���p�X�����ʂ��邽�߂̐ړ�����
	private static final String PATH_KEY = "path=";
	// �p�����^�e�[�u���̃w�b�_�Q�����ʂ��邽�߂̐ړ�����
	private static final String TABLE_KEY = "table=";
	// ��ƃf�B���N�g���̃p�X
	private static String path = Common.EMPTY.get();
	// �p�����^�e�[�u���̃w�b�_�Q
	private static ArrayList<String> tableHeaders = new ArrayList<>();

	/**
	 * �������Bconfig.ini�����
	 */
	public static void init() throws IOException {
		try {
			getLines(CONFIG_INI).forEach(s -> {
				if(s.startsWith(PATH_KEY)) {
					// ��ƃf�B���N�g���̃p�X���擾
					path = s.replaceFirst(PATH_KEY, Common.EMPTY.get()).trim();
				} else if(s.startsWith(TABLE_KEY)) {
					// �p�����^�e�[�u���̃w�b�_���擾
					String[] arrays = s.replaceFirst(TABLE_KEY, Common.EMPTY.get()).trim().split(Common.COMMA.get());
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
	 * ��ƃf�B���N�g���̃p�X���擾�B
	 * @return ��ƃf�B���N�g���̃p�X
	 */
	public static Path getWorkingDirPath() {
		return Paths.get(path);
	}

	/**
	 * �p�����^�e�[�u���̃w�b�_���擾�B
	 * @return �p�����^�e�[�u���̃w�b�_�̃��X�g
	 */
	public static ArrayList<String> getParamTableHeaders() {
		return tableHeaders;
	}
}
