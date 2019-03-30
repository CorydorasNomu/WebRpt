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
 * ���|�[�g�y�[�W�쐬�T�[�r�X�̊Ǘ��N���X�B
 *
 * @author nomu.shunn
 */
public class ReportServiceMgr {

	// ���|�[�g�o�͗p�f�B���N�g�������ʂ��邽�߂̐ړ�����
	static final String REPORT_DIR_PREFIX = "report_";

	/**
	 * �R���X�g���N�^
	 */
	public ReportServiceMgr() {
		ConfigAccessService.init();
	}

	/**
	 * ���|�[�g���쐬����B
	 * @param dirStream ���|�[�g�o�͑Ώۂ̍�ƃf�B���N�g���̃p�X�X�g���[��
	 * @throws IOException ���|�[�g�̍쐬�Ɏ��s
	 */
	public void create(Stream<Path> dirStream) throws IOException {
		// ���|�[�g�o�̓f�B���N�g���̍쐬
		Files.createDirectory(getReportDirPath());
		// HTML�T�[�r�X�̐���
		HTMLService htmlService = new HTMLService(getReportDirPath());

		// ���|�[�g�̃C���f�b�N�X�쐬
		htmlService.createIndex(dirStream);
		// �X�^�C���V�[�g�쐬
		htmlService.createCSS();
		// ���|�[�g�y�[�W�쐬
		htmlService.createHTML();
	}

	/**
	 * ���|�[�g�o�̓f�B���N�g���̃p�X���擾�B
	 * @return path ���|�[�g�o�̓f�B���N�g���̃p�X
	 */
	private Path getReportDirPath() {
		// ��ӂ̃f�B���N�g������������琶��
		SimpleDateFormat sdf = new SimpleDateFormat(Util.TIME_FORMAT.get());
		String date = sdf.format(new Date(System.currentTimeMillis()));

		// �f�B���N�g�����𐶐�
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