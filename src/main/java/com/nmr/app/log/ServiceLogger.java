package com.nmr.app.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nmr.app.util.ConstSet.Common;
import com.nmr.app.util.ConstSet.Extension;
import com.nmr.app.util.ConstSet.FilePath;

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
		logFile = new File(FilePath.CURRENT.get() + LOG_FILE + Extension.LOG.get());
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
	 * ERROR���x���̃��O���e�ƃg���[�X���o�b�t�@�ɏ������ށB
	 * @param msg ���b�Z�[�W
	 */
	public static void error(String msg) {
		create(Level.ERROR.get() + msg);
	}

	/**
	 * ERROR���x���̃��O���e�ƃg���[�X���o�b�t�@�ɏ������ށB
	 * @param msg ���b�Z�[�W
	 */
	public static void error(String msg, Throwable t) {
		create(Level.ERROR.get() + msg);
		trace(t);
	}

	/**
	 * �X�^�b�N�g���[�X�̓��e���o�b�t�@�ɏ������ށB
	 * @param e �X�^�b�N�g���[�X
	 */
	public static void trace(Throwable t) {
		logBuilder.append(t.toString() + Common.NEW_LINE.get());
		StackTraceElement[] elm = t.getStackTrace();
		for(StackTraceElement  e : elm) {
			logBuilder.append(Common.ATB.get() + e.toString() + Common.NEW_LINE.get());
		}
	}

	private static String timeStamp() {
		// ���ݎ���
		Date stamp = new Date(System.currentTimeMillis());
		// ���O�̓��t���𐶐�
		String date = new SimpleDateFormat(Common.LOG_TIMESTAMP_DATE.get()).format(stamp);
		// ���O�̎��ԕ��𐶐�
		String time = new SimpleDateFormat(Common.LOG_TIMESTAMP_TIME.get()).format(stamp);

		return date + Common.SPACE.get() + time + Common.SPACE.get();
	}

	private static void create(String msg) {
		logBuilder.append(timeStamp());
		logBuilder.append(msg + Common.NEW_LINE.get());
	}
}
