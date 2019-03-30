package com.nmr.app.svc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.nmr.app.util.ConstSet.Util;

/**
 * jar���ɕ�������ł��郊�\�[�X�t�@�C���ւ̃A�N�Z�T�N���X�B
 *
 * @author nomu.shunn
 */
public class ResourceAccessService extends CommonFileAccessService {

	// HTML�e���v���[�g�̃t�@�C����
	public static final String HTML_TEMPLATE      =	"template.html";
	// CSS�e���v���[�g�̃t�@�C����
	public static final String CSS_TEMPLATE       =	"template.css";
	// HTML�e���v���[�g�̃��\�[�X���p�X
	private static final String HTML_TEMPLATE_URL = "/html/" + HTML_TEMPLATE;
	// CSS�e���v���[�g�̃��\�[�X���p�X
	private static final String CSS_TEMPLATE_URL  =	"/css/"  + CSS_TEMPLATE;

	/**
	 * �R���X�g���N�^
	 */
	public ResourceAccessService() {
		// noop
	}

	/**
	 * Jar����HTML�e���v���[�g�̓��e���擾����B
	 * @return Jar����HTML�e���v���[�g�̓��e
	 * @throws IOException HTML�e���v���[�g�̓��e�擾�Ɏ��s
	 */
	public static String getHTMLContents() throws IOException {
		// �X�g���[�����當����ւ̕ϊ�
		return convert(ResourceAccessService.class.getResourceAsStream(HTML_TEMPLATE_URL));
	}

	/**
	 * Jar����CSS�e���v���[�g�̓��e���擾����B
	 * @return Jar����CSS�e���v���[�g�̓��e
	 * @throws IOException CSS�e���v���[�g�̓��e�擾�Ɏ��s
	 */
	public static String getCSSContents() throws IOException {
		// �X�g���[�����當����ւ̕ϊ�
		return convert(ResourceAccessService.class.getResourceAsStream(CSS_TEMPLATE_URL));
	}

	private static String convert(InputStream in) throws IOException {
		String contents = Util.EMPTY.get();
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(in, Util.UTF8.get()))) {
			contents = br.lines().collect(Collectors.joining(Util.NEW_LINE.get()));
		}
		return contents;
	}

	/**
	 * �t�@�C�����e�̊e�s�����X�g�Ŏ擾�B
	 * @param path �t�@�C���̃p�X
	 * @return �t�@�C�����e�̊e�s��ێ��������X�g
	 * @throws IOException �t�@�C�����e�̎擾�Ɏ��s
	 */
	protected List<String> getFileLines(String path) throws IOException {
		return Files.lines(Paths.get(path),
								StandardCharsets.UTF_8).collect(Collectors.toList());
	}
}