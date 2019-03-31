package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet.Extension;

/**
 * �f�[�^�f�B���N�g�����ɂ���e�t�@�C���ւ̃A�N�Z�T�N���X�B
 *
 * @author nomu.shunn
 */
public class DataFileAccessService {

	// �t�я��t�@�C���̃t�@�C����
	private static final String INFO_TXT = "info" + Extension.TXT.get();
	// �摜�t�@�C���̃p�X�̃��X�g
	private static ArrayList<Path> imgs;
	// �t�я��t�@�C���̃p�X
	private static Path info;

	/**
	 * �R���X�g���N�^
	 * @param dirPath �f�[�^�f�B���N�g���̃p�X
	 */
	public DataFileAccessService(Path dirPath) throws IOException {
		init(dirPath);
	}

	private void init(Path path) throws IOException {
		// �ϐ��̏�����
		imgs = new ArrayList<>();
		info = null;
		// �f�[�^�f�B���N�g�����̃t�@�C���̃p�X�����擾
		try(Stream<Path> files = Files.list(path)) {

			Consumer<Path> set = p -> {
				String fileName = p.toFile().getName();
				if(fileName.endsWith(Extension.JPG.get()) || fileName.endsWith(Extension.PNG.get()))
					imgs.add(p.toAbsolutePath());	// �摜�̃p�X���擾
				if(INFO_TXT.equals(fileName))
					info = p.toAbsolutePath();			// �t�я��̃p�X���擾
			};

			files.filter(p -> p.toFile().isFile())
				.forEach(set);
		} catch(IOException e) {
			ServiceLogger.error("Fail to get file path list of a data directory.");
			throw e;
		}
	}

	/**
	 * ���M�g�`�摜�̃p�X���擾����B
	 * @return ���M�g�`�摜�̃p�X
	 */
	public ArrayList<Path> getImages() {
		return imgs;
	}

	/**
	 * �t�я��t�@�C���̃p�X���擾����B
	 * @return �t�я��t�@�C��
	 */
	public Path getInfo() {
		return info;
	}
}
