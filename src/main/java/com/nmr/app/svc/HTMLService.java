package com.nmr.app.svc;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet;
import com.nmr.app.util.ConstSet.Extension;
import com.nmr.app.util.ConstSet.Regex;
import com.nmr.app.util.ConstSet.Util;

/**
 * ���|�[�g�y�[�W��HTML�𐶐�����N���X�B
 *
 * @author nomu.shunn
 */
public class HTMLService extends CommonFileAccessService {

	// �u���Ŏg�p����萔�̐錾
	private enum Replace {
		TAG("%"),			// �u���J�n�̎��ʎq
		TITLE("title"),		// �^�C�g���̒u��������
		IMAGE("img"),		// �摜�̒u��������
		COMMENT("comment"),	// �R�����g�̒u��������
		PREV("prev"),		// PREV�����N�̒u��������
		NEXT("next");		// NEXT�����N�̒u��������

		private final String str;

		private Replace(String s) {
			str = s;
		}

		public String get() {
			return str;
		}
	}

	// ���|�[�g�o�͗p�f�B���N�g���̃p�X
	private Path reportDirPath = null;
	//�@���|�[�g�y�[�W�̃C���f�b�N�X
	private ArrayList<IndexInfo> indexInfo = new ArrayList<>();
	// ���|�[�g�y�[�W��HTML�\�[�X
	private String contents = Util.EMPTY.get();

	/**
	 * �R���X�g���N�^
	 * @param path ���|�[�g�o�͗p�f�B���N�g���̃p�X
	 */
	public HTMLService(Path path) {
		reportDirPath = path;
	}

	/**
	 * ���|�[�g�y�[�W�̃C���f�b�N�X���쐬����B
	 * @param str �f�[�^�f�B���N�g���̃p�X�X�g���[��
	 */
	public void createIndex(Stream<Path> str) {
		// �C���f�b�N�X��ǉ�
		Consumer<Path> addIndex = p -> {
			String pageTitle = p.toFile().getName();
			String pagePath  = reportDirPath.toFile().getAbsolutePath() +
					ConstSet.Path.SEPARATOR.get() + pageTitle + Extension.HTML.get();
			indexInfo.add(new IndexInfo(p, pageTitle, pagePath));
		};
		/* ���O��"report_"����n�܂�f�B���N�g���̓��|�[�g�o�͗p��
		 * ���Ȃ��āA���|�[�g�쐬�Ώۂ̃f�[�^�f�B���N�g���Q���珜�O�B
		 * */
		str.filter(p -> p.toFile().isDirectory() &&
						!p.toFile().getName().startsWith(ReportServiceMgr.REPORT_DIR_PREFIX))
			.forEach(addIndex);
	}

	/**
	 * �X�^�C���V�[�g���쐬����B
	 * @throws IOException �X�^�C���V�[�g�̍쐬�Ɏ��s
	 */
	public void createCSS() throws IOException {
		// �X�^�C���V�[�g�̏o�͐�
		String name = ConstSet.Path.SEPARATOR.get() + ResourceAccessService.CSS_TEMPLATE;
		String cssPath = reportDirPath.toFile().getAbsolutePath() + name;

		try(FileWriter fw = new FileWriter(cssPath)) {
			// �X�^�C���V�[�g�̓��e�ɂ͕ύX���Ȃ��̂ŁA���\�[�X�̒��g�����̂܂܏o��
        	fw.write(ResourceAccessService.getCSSContents());
        } catch(IOException e) {
        	ServiceLogger.error("Fail to create FileWriter object. Target Path: " + cssPath.toString());
        	throw e;
        }
	}

	/**
	 * �f�[�^�f�B���N�g�����̃f�[�^�����ƂɃ��|�[�g�y�[�W���쐬����B
	 * @throws IOException ���|�[�g�y�[�W�̍쐬�Ɏ��s
	 */
	public void createHTML() throws IOException {
		// �C���f�b�N�X���
		IndexInfo idx = null;
		for(int i = 0; i < indexInfo.size(); i++) {
			// �T�[�r�X�̐���
			idx = indexInfo.get(i);
			DataFileAccessService dataFiles = new DataFileAccessService(idx.getDirPath());
			InfoAccessService info = new InfoAccessService(dataFiles.getInfo());

			// HTML�e���v���[�g�̓��e���擾
	    	contents = ResourceAccessService.getHTMLContents();

	    	setTitle(idx.getTitle());	// �^�C�g����u��
	    	setLink(i);						// �����N��u��
	    	setValues(info);				// �p�����^�l��u��
	    	setImages(dataFiles);		// �摜��u��
	    	setComment(info);		// �R�����g��u��

	    	// ���|�[�g�y�[�W�̏o��
	    	try(FileWriter fw = new FileWriter(idx.getPath())) {
	    		fw.write(contents);
	    	} catch(IOException e) {
	    		ServiceLogger.error("Fail to create FileWriter object. Target Path: " + idx.getPath().toString());
	    		throw e;
	    	}
		}
	}

	private void setLink(int index) {
		// �u���Ώە�����
		String targetPrev = Replace.TAG.get() + Replace.PREV.get() + Replace.TAG.get();
		String targetNext = Replace.TAG.get() + Replace.NEXT.get() + Replace.TAG.get();
		// �����N��t�@�C���̃p�X
		// TOP�̃y�[�W�Ȃ�uPREV�v�͑��݂��Ȃ�
		String prev = index == 0 ? Util.EMPTY.get() : ConstSet.Path.CURRENT.get() +
									indexInfo.get(index - 1).getTitle() + Extension.HTML.get();
		// �Ō�̃y�[�W�Ȃ�uNEXT�v�͑��݂��Ȃ�
		String next = index == indexInfo.size() - 1 ? Util.EMPTY.get() : ConstSet.Path.CURRENT.get() +
									indexInfo.get(index + 1).getTitle() + Extension.HTML.get();
		// �u�����s
		if(index != 0 && index != (indexInfo.size() - 1))
			contents = contents.replaceAll(targetPrev, prev)
							   .replaceAll(targetNext, next);
		else if(index == 0)
			contents = contents.replaceAll(targetPrev, Util.EMPTY.get())
							   .replaceAll(targetNext, next);
		else if(index == (indexInfo.size() - 1))
			contents = contents.replaceAll(targetPrev, prev)
							   .replaceAll(targetNext, Util.EMPTY.get());
	}

	private void setTitle(String title) {
		String target = Replace.TAG.get() + Replace.TITLE.get() + Replace.TAG.get();
		contents = contents.replaceAll(target, title);
	}

	private void setValues(InfoAccessService info) {
		info.getValues().forEach((key, val) -> {
			String target = Replace.TAG.get() + key + Replace.TAG.get();
			String value  = val == null ? Util.EMPTY.get() : val.toString();
			contents = contents.replaceAll(target, value);
		});
	}

	private void setImages(DataFileAccessService dataFiles) {
		String target = Replace.TAG.get() + Replace.IMAGE.get() + Replace.TAG.get();
		dataFiles.getImages().forEach(p -> {
			contents = contents.replaceFirst(target,
					p.toString().replaceAll(Regex.ESCAPE.get(), ConstSet.Path.SEPARATOR.get()));
		});
	}

	private void setComment(InfoAccessService info) {
		String target = Replace.TAG.get() + Replace.COMMENT.get() + Replace.TAG.get();
		contents = contents.replaceAll(target, info.getComment());
	}

	class IndexInfo {

		private Path   dir   = null;
		private String title = "";
		private String path  = "";

		/**
		 * �R���X�g���N�^
		 * @param d �f�[�^�f�B���N�g���̃p�X
		 * @param t ���|�[�g�y�[�W�̃^�C�g��
		 * @param p ���|�[�g�y�[�W�̃p�X
		 */
		public IndexInfo(Path d, String t, String p) {
			dir = d;
			title = t;
			path  = p;
		}

		public Path getDirPath() {
			return dir;
		}

		public String getTitle() {
			return title;
		}

		public String getPath() {
			return path;
		}
	}
}
