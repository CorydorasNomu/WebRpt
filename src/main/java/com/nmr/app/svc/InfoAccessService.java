package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet.Common;
import com.nmr.app.util.ConstSet.TABLE_HEADER;
import com.nmr.app.util.ConstSet.TABLE_VALUE;

public class InfoAccessService extends CommonFileAccessService {

	// �t�я��t�@�C�����̃w�b�_
	private enum Header {
		PARAMS("PARAMS:"),		// �p�����^�l
		COMMENT("#Comment");	// �R�����g

		private final String str;

		private Header(String s) {
			str = s;
		}

		public String get() {
			return str;
		}
	}
	// �p�����^�l�̃L�[��Map
	private static ArrayList<String> tableKeyList = null;
	// �p�����^�l�̃Z�p���^
	private static final String VAL_SEPARATOR = Common.SPACE.get();
	// �p�����^�l�̋L�ڕ��̕�����
	private String vals = Common.EMPTY.get();
	// �R�����g�̓��e
	private String comment = Common.EMPTY.get();

	/**
	 * �R���X�g���N�^
	 * @param info �t�я��t�@�C���̃p�X
	 */
	public InfoAccessService(Path info) throws IOException {
		if(info != null) {
			initValues(getLines(info.toString()));
			initComment(getLines(info.toString()));
		}
	}

	/**
	 * �p�����^�e�[�u���̃w�b�_�u��Map���擾����B
	 * key�̓��\�[�X��HTML���̒u�����ʎq�Bvalue�͐ݒ�t�@�C���̃e�[�u���w�b�_���B
	 * @return �p�����^�e�[�u���̃w�b�_�u��Map
	 */
	public HashMap<String, String> getHeaders() {
		// �w�b�_�u��Map
		HashMap<String, String> headersMap = new HashMap<>();

		int counter = 0;
		for(String s : TABLE_HEADER.get()) {
			try {
				headersMap.put(s, ConfigAccessService.getParamTableHeaders().get(counter));
			} catch(IndexOutOfBoundsException e) {
				headersMap.put(s, Common.EMPTY.get());
				ServiceLogger.warn("You do NOT set enough headers in a param table.");
			}
			counter++;
		}
		return headersMap;
	}

	/**
	 * �p�����^�e�[�u���̒l�u��Map���擾����B
	 * key�̓��\�[�X��HTML���̒u�����ʎq�Bvalue�͕t�я��t�@�C���̒l���B
	 * @return �p�����^�e�[�u���̒l�u��Map
	 */
	public HashMap<String, Float> getValues() {
		// �p�����^�l��ێ�����}�b�v
		HashMap<String, Float> valsMap = new HashMap<>();

		if(!Common.EMPTY.get().equals(vals)) {
			int counter = 0;
			for(String s : TABLE_VALUE.get()) {
				try {
					// �p�����^�l��float�ϊ����ă}�b�v�Ɋi�[
					valsMap.put(s, Float.parseFloat(vals.split(VAL_SEPARATOR)[counter]));
				} catch(IndexOutOfBoundsException | NumberFormatException e) {
					valsMap.put(s, new Float(0));
					ServiceLogger.error("Value [" + (counter+1) + "] has been converted to \"0\".", e);
				}
				counter++;
			}
		} else {
			// �p�����^�l���Ȃ��ꍇ�͋�̒l���Z�b�g
			TABLE_VALUE.get().forEach(v -> {
				valsMap.put(v, null);
			});
			ServiceLogger.warn("You have no params in \"info.txt\".");
		}
		return valsMap;
	}

	/**
	 * �R�����g���擾����B
	 * @return �R�����g�̕�����
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * �p�����^�l��Key�}�b�v���擾����B
	 * @return �p�����^�l��Key�}�b�v
	 */
	public ArrayList<String> getKeyMap() {
		return tableKeyList;
	}

	private void initValues(List<String> info) {
		// �p�����^�l���L�q����Ă���s�̒��o
		info.forEach(line -> {
			if(line.startsWith(Header.PARAMS.get()))
				vals = line.replaceAll(Header.PARAMS.get(), Common.EMPTY.get());
		});
	}

	private void initComment(List<String> info) {
		// ������
		comment = Common.EMPTY.get();
		// �R�����g���L�q����Ă���s�̒��o
		boolean isCommentArea = false;
		for(String line : info) {
			if(isCommentArea)
				comment = comment + line + Common.NEW_LINE.get();
			else
				if(Header.COMMENT.get().equals(line))
					isCommentArea = true;
		}
	}
}
