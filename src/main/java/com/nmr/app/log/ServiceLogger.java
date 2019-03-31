package com.nmr.app.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nmr.app.util.ConstSet.Extension;
import com.nmr.app.util.ConstSet.Path;
import com.nmr.app.util.ConstSet.Util;

/**
 * ���ʂ̃��O�T�[�r�X�N���X�B
 *
 * @author nomu.shunn
 */
public class ServiceLogger {

	// ���O�t�@�C����
	private static final String LOG_FILE = "svc";
	// ���O�t�@�C��
	private static File logFile = null;
	// ���O���e��ێ�����o�b�t�@
	private static StringBuilder logBuilder = null;

	private enum Level {
		INFO("INFO :"),
		WARN("WARN :"),
		ERROR("ERROR:"),
		TRACE("TRACE:");

		private final String str;

		private Level(String s) {
			str = s;
		}
		public String get() {
			return str;
		}
	}

	/**
	 * ���O�T�[�r�X�̏�����
	 */
	public static void init() {
		logFile = new File(Path.CURRENT.get() + LOG_FILE + Extension.LOG.get());
		if (logFile != null && logFile.exists())	// ���Ƀ��O�t�@�C��������΍폜
			logFile.delete();
		logBuilder = new StringBuilder();		// ���O�o�b�t�@�̐���
		info("ServiceLogger initialized successfully.");
	}

	/**
	 * ���O�T�[�r�X�̏I��
	 */
	public static void terminate() {
		// �o�b�t�@�����O����Ă��郍�O���e�̏����o��
		try(FileWriter w = new FileWriter(logFile)) {
			w.write(logBuilder.toString());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * INFO���x���̃��O���e���o�b�t�@�ɏ������ށB
	 * @param msg ���b�Z�[�W
	 */
	public static void info(String msg) {
		create(Level.INFO.get() + msg);
	}

	/**
	 * WARN���x���̃��O���e���o�b�t�@�ɏ������ށB
	 * @param msg ���b�Z�[�W
	 */
	public static void warn(String msg) {
		create(Level.WARN.get() + msg);
	}

	/**
	 * ERROR���x���̃��O���e���o�b�t�@�ɏ������ށB
	 * @param msg ���b�Z�[�W
	 */
	public static void error(String msg) {
		create(Level.ERROR.get() + msg);
	}

	/**
	 * �X�^�b�N�g���[�X�̓��e���o�b�t�@�ɏ������ށB
	 * @param e �X�^�b�N�g���[�X
	 */
	public static void trace(StackTraceElement[] elm) {
		for(StackTraceElement e : elm) {
			logBuilder.append(e.toString());
		}
	}

	private static String timeStamp() {
		// ���ݎ���
		Date stamp = new Date(System.currentTimeMillis());
		// ���O�̓��t���𐶐�
		String date = new SimpleDateFormat(Util.LOG_TIMESTAMP_DATE.get()).format(stamp);
		// ���O�̎��ԕ��𐶐�
		String time = new SimpleDateFormat(Util.LOG_TIMESTAMP_TIME.get()).format(stamp);

		return date + Util.SPACE.get() + time + Util.SPACE.get();
	}

	private static void create(String msg) {
		logBuilder.append(timeStamp());
		logBuilder.append(msg + Util.NEW_LINE.get());
	}
}
