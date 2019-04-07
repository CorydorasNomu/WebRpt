package com.nmr.app.svc;

import java.io.FileNotFoundException;
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

	// 付帯情報ファイル内のヘッダ
	private enum Header {
		PARAMS("PARAMS:"),		// パラメタ値
		COMMENT("#Comment");	// コメント

		private final String str;

		private Header(String s) {
			str = s;
		}
		public String get() {
			return str;
		}
	}

	// パラメタ値のセパレタ
	private static final String VAL_SEPARATOR = Common.SPACE.get();
	// パラメタ値のキーのMap
	private static ArrayList<String> tableKeyList = null;
	// データディレクトリ
	private String dirName = Common.EMPTY.get();
	// パラメタ値の記載部の文字列
	private String vals = Common.EMPTY.get();
	// コメントの内容
	private String comment = Common.EMPTY.get();

	/**
	 * コンストラクタ
	 * @param info 付帯情報ファイルのパス
	 */
	public InfoAccessService(Path info) throws IOException {
		if(info == null || !info.toFile().exists()) {
			ServiceLogger.error("\"info.txt\" does NOT exist in a data directory.");
			throw new FileNotFoundException();
		}
		dirName = info.toFile().getPath();
		initValues(getLines(info.toString()));
		initComment(getLines(info.toString()));
	}

	/**
	 * パラメタテーブルのヘッダ置換Mapを取得する。
	 * keyはリソースのHTML内の置換識別子。valueは設定ファイルのテーブルヘッダ情報。
	 * @return パラメタテーブルのヘッダ置換Map
	 */
	public HashMap<String, String> getHeaders() {
		// ヘッダ置換Map
		HashMap<String, String> headersMap = new HashMap<>();

		int counter = 0;
		for(String s : TABLE_HEADER.get()) {
			try {
				headersMap.put(s, ConfigAccessService.getParamTableHeaders().get(counter));
			} catch(IndexOutOfBoundsException e) {
				headersMap.put(s, Common.EMPTY.get());
				ServiceLogger.warn(dirName + " : You do NOT set a header at [" + (counter+1) + "].");
			}
			counter++;
		}
		return headersMap;
	}

	/**
	 * パラメタテーブルの値置換Mapを取得する。
	 * keyはリソースのHTML内の置換識別子。valueは付帯情報ファイルの値情報。
	 * @return パラメタテーブルの値置換Map
	 */
	public HashMap<String, String> getValues() {
		// パラメタ値を保持するマップ
		HashMap<String, String> valsMap = new HashMap<>();

		if(!Common.EMPTY.get().equals(vals)) {
			int counter = 0;
			for(String s : TABLE_VALUE.get()) {
				try {
					// パラメタ値をfloat変換してマップに格納
					valsMap.put(s, vals.split(VAL_SEPARATOR)[counter]);
				} catch(IndexOutOfBoundsException e) {
					valsMap.put(s, Common.EMPTY.get());
					ServiceLogger.warn(dirName + " : You do NOT set a value at [" + (counter+1) + "].");
				}
				counter++;
			}
		} else {
			// パラメタ値がない場合は空の値をセット
			TABLE_VALUE.get().forEach(v -> {
				valsMap.put(v, null);
			});
			ServiceLogger.warn(dirName + " : You have no params in \"info.txt\".");
		}
		return valsMap;
	}

	/**
	 * コメントを取得する。
	 * @return コメントの文字列
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * パラメタ値のKeyマップを取得する。
	 * @return パラメタ値のKeyマップ
	 */
	public ArrayList<String> getKeyMap() {
		return tableKeyList;
	}

	private void initValues(List<String> info) {
		// パラメタ値が記述されている行の抽出
		info.forEach(line -> {
			if(line.startsWith(Header.PARAMS.get()))
				vals = line.replaceAll(Header.PARAMS.get(), Common.EMPTY.get());
		});
	}

	private void initComment(List<String> info) {
		// 初期化
		comment = Common.EMPTY.get();
		// コメントが記述されている行の抽出
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
