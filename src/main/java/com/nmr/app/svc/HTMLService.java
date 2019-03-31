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
 * レポートページのHTMLを生成するクラス。
 *
 * @author nomu.shunn
 */
public class HTMLService extends CommonFileAccessService {

	// 置換で使用する定数の宣言
	private enum Replace {
		TAG("%"),			// 置換開始の識別子
		TITLE("title"),		// タイトルの置換文字列
		IMAGE("img"),		// 画像の置換文字列
		COMMENT("comment"),	// コメントの置換文字列
		PREV("prev"),		// PREVリンクの置換文字列
		NEXT("next");		// NEXTリンクの置換文字列

		private final String str;

		private Replace(String s) {
			str = s;
		}

		public String get() {
			return str;
		}
	}

	// レポート出力用ディレクトリのパス
	private Path reportDirPath = null;
	//　レポートページのインデックス
	private ArrayList<IndexInfo> indexInfo = new ArrayList<>();
	// レポートページのHTMLソース
	private String contents = Util.EMPTY.get();

	/**
	 * コンストラクタ
	 * @param path レポート出力用ディレクトリのパス
	 */
	public HTMLService(Path path) {
		reportDirPath = path;
	}

	/**
	 * レポートページのインデックスを作成する。
	 * @param str データディレクトリのパスストリーム
	 */
	public void createIndex(Stream<Path> str) {
		// インデックスを追加
		Consumer<Path> addIndex = p -> {
			String pageTitle = p.toFile().getName();
			String pagePath  = reportDirPath.toFile().getAbsolutePath() +
					ConstSet.Path.SEPARATOR.get() + pageTitle + Extension.HTML.get();
			indexInfo.add(new IndexInfo(p, pageTitle, pagePath));
		};
		/* 名前が"report_"から始まるディレクトリはレポート出力用と
		 * 見なして、レポート作成対象のデータディレクトリ群から除外。
		 * */
		str.filter(p -> p.toFile().isDirectory() &&
						!p.toFile().getName().startsWith(ReportServiceMgr.REPORT_DIR_PREFIX))
			.forEach(addIndex);
	}

	/**
	 * スタイルシートを作成する。
	 * @throws IOException スタイルシートの作成に失敗
	 */
	public void createCSS() throws IOException {
		// スタイルシートの出力先
		String name = ConstSet.Path.SEPARATOR.get() + ResourceAccessService.CSS_TEMPLATE;
		String cssPath = reportDirPath.toFile().getAbsolutePath() + name;

		try(FileWriter fw = new FileWriter(cssPath)) {
			// スタイルシートの内容には変更がないので、リソースの中身をそのまま出力
        	fw.write(ResourceAccessService.getCSSContents());
        } catch(IOException e) {
        	ServiceLogger.error("Fail to create FileWriter object. Target Path: " + cssPath.toString());
        	throw e;
        }
	}

	/**
	 * データディレクトリ内のデータをもとにレポートページを作成する。
	 * @throws IOException レポートページの作成に失敗
	 */
	public void createHTML() throws IOException {
		// インデックス情報
		IndexInfo idx = null;
		for(int i = 0; i < indexInfo.size(); i++) {
			// サービスの生成
			idx = indexInfo.get(i);
			DataFileAccessService dataFiles = new DataFileAccessService(idx.getDirPath());
			InfoAccessService info = new InfoAccessService(dataFiles.getInfo());

			// HTMLテンプレートの内容を取得
	    	contents = ResourceAccessService.getHTMLContents();

	    	setTitle(idx.getTitle());	// タイトルを置換
	    	setLink(i);						// リンクを置換
	    	setValues(info);				// パラメタ値を置換
	    	setImages(dataFiles);		// 画像を置換
	    	setComment(info);		// コメントを置換

	    	// レポートページの出力
	    	try(FileWriter fw = new FileWriter(idx.getPath())) {
	    		fw.write(contents);
	    	} catch(IOException e) {
	    		ServiceLogger.error("Fail to create FileWriter object. Target Path: " + idx.getPath().toString());
	    		throw e;
	    	}
		}
	}

	private void setLink(int index) {
		// 置換対象文字列
		String targetPrev = Replace.TAG.get() + Replace.PREV.get() + Replace.TAG.get();
		String targetNext = Replace.TAG.get() + Replace.NEXT.get() + Replace.TAG.get();
		// リンク先ファイルのパス
		// TOPのページなら「PREV」は存在しない
		String prev = index == 0 ? Util.EMPTY.get() : ConstSet.Path.CURRENT.get() +
									indexInfo.get(index - 1).getTitle() + Extension.HTML.get();
		// 最後のページなら「NEXT」は存在しない
		String next = index == indexInfo.size() - 1 ? Util.EMPTY.get() : ConstSet.Path.CURRENT.get() +
									indexInfo.get(index + 1).getTitle() + Extension.HTML.get();
		// 置換実行
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
		 * コンストラクタ
		 * @param d データディレクトリのパス
		 * @param t レポートページのタイトル
		 * @param p レポートページのパス
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
