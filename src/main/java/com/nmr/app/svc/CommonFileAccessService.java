package com.nmr.app.svc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * �t�@�C���A�N�Z�X�̋��ʏ�������`�����N���X�B
 *
 * @author nomu.shunn
 */
public class CommonFileAccessService {

	/**
	 * �t�@�C������͂��ċL�q���e���s���ƂɃ��X�g�ɂ��Ď擾����B
	 * @param path ��͑Ώۃt�@�C���̃p�X
	 * @return ��͂��ꂽ�t�@�C�����e
	 * @throws IOException ��͂Ɏ��s
	 */
	public static List<String> getLines(String path) throws IOException {
		return Files.lines(Paths.get(path),
				StandardCharsets.UTF_8).collect(Collectors.toList());
	}
}
