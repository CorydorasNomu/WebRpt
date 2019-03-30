package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.nmr.app.util.ConstSet.Util;

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
	private static final ArrayList<String> KEY_LIST = new ArrayList<>(
			Arrays.asList("KEY_0", "KEY_1", "KEY_2", "KEY_3", "KEY_4", "KEY_5", "KEY_6", "KEY_7", "KEY_8"));
	// �p�����^�l�̃Z�p���^
	private static final String VAL_SEPARATOR = Util.SPACE.get();
	// �p�����^�l�̋L�ڕ��̕�����
	private String vals = Util.EMPTY.get();
	// �R�����g�̓��e
	private String comment = Util.EMPTY.get();

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
	 * �t�я��t�@�C������p�����^�l���擾����B
	 * @return �p�����^�l�̃��X�g
	 */
	public HashMap<String, Float> getValues() {
		// �p�����^�l��ێ�����}�b�v
		HashMap<String, Float> valsMap = new HashMap<>();
		if(!Util.EMPTY.get().equals(vals)) {
			// �p�����^�l��float�ϊ����ă}�b�v�Ɋi�[
			String[] valsArray = vals.split(VAL_SEPARATOR);
			for(int i = 0; i < valsArray.length; i++) {
				valsMap.put(KEY_LIST.get(i), Float.parseFloat(valsArray[i]));
			}
		} else {
			// �p�����^�l���Ȃ��ꍇ�͋�̒l���Z�b�g
			KEY_LIST.forEach(k -> {
				valsMap.put(k, null);
			});
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
		return KEY_LIST;
	}

	private void initValues(List<String> info) {
		// �p�����^�l���L�q����Ă���s�̒��o
		info.forEach(line -> {
			if(line.startsWith(Header.PARAMS.get()))
				vals = line.replaceAll(Header.PARAMS.get(), Util.EMPTY.get());
		});
	}

	private void initComment(List<String> info) {
		// ������
		comment = Util.EMPTY.get();
		// �R�����g���L�q����Ă���s�̒��o
		boolean isCommentArea = false;
		for(String line : info) {
			if(isCommentArea)
				comment = comment + line + Util.NEW_LINE.get();
			else
				if(Header.COMMENT.get().equals(line))
					isCommentArea = true;
		}
	}
}
