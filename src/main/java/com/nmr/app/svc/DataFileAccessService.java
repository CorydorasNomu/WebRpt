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
 * データディレクトリ内にある各ファイルへのアクセサクラス。
 *
 * @author nomu.shunn
 */
public class DataFileAccessService {

	// 付帯情報ファイルのファイル名
	private static final String INFO_TXT = "info" + Extension.TXT.get();
	// 画像ファイルのパスのリスト
	private static ArrayList<Path> imgs;
	// 付帯情報ファイルのパス
	private static Path info;

	/**
	 * コンストラクタ
	 * @param dirPath データディレクトリのパス
	 */
	public DataFileAccessService(Path dirPath) throws IOException {
		init(dirPath);
	}

	private void init(Path path) throws IOException {
		// 変数の初期化
		imgs = new ArrayList<>();
		info = null;
		// データディレクトリ内のファイルのパス情報を取得
		try(Stream<Path> files = Files.list(path)) {

			Consumer<Path> set = p -> {
				String fileName = p.toFile().getName();
				if(fileName.endsWith(Extension.JPG.get()) || fileName.endsWith(Extension.PNG.get()))
					imgs.add(p.toAbsolutePath());	// 画像のパスを取得
				if(INFO_TXT.equals(fileName))
					info = p.toAbsolutePath();			// 付帯情報のパスを取得
			};

			files.filter(p -> p.toFile().isFile())
				.forEach(set);
		} catch(IOException e) {
			ServiceLogger.error("Fail to get file path list of a data directory.");
			throw e;
		}
	}

	/**
	 * 送信波形画像のパスを取得する。
	 * @return 送信波形画像のパス
	 */
	public ArrayList<Path> getImages() {
		return imgs;
	}

	/**
	 * 付帯情報ファイルのパスを取得する。
	 * @return 付帯情報ファイル
	 */
	public Path getInfo() {
		return info;
	}
}
