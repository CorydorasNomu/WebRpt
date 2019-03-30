package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.nmr.app.util.ConstSet.Util;

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
	// パラメタ値のキーのMap
	private static final ArrayList<String> KEY_LIST = new ArrayList<>(
			Arrays.asList("KEY_0", "KEY_1", "KEY_2", "KEY_3", "KEY_4", "KEY_5", "KEY_6", "KEY_7", "KEY_8"));
	// パラメタ値のセパレタ
	private static final String VAL_SEPARATOR = Util.SPACE.get();
	// パラメタ値の記載部の文字列
	private String vals = Util.EMPTY.get();
	// コメントの内容
	private String comment = Util.EMPTY.get();

	/**
	 * コンストラクタ
	 * @param info 付帯情報ファイルのパス
	 */
	public InfoAccessService(Path info) throws IOException {
		if(info != null) {
			initValues(getLines(info.toString()));
			initComment(getLines(info.toString()));
		}
	}

	/**
	 * 付帯情報ファイルからパラメタ値を取得する。
	 * @return パラメタ値のリスト
	 */
	public HashMap<String, Float> getValues() {
		// パラメタ値を保持するマップ
		HashMap<String, Float> valsMap = new HashMap<>();
		if(!Util.EMPTY.get().equals(vals)) {
			// パラメタ値をfloat変換してマップに格納
			String[] valsArray = vals.split(VAL_SEPARATOR);
			for(int i = 0; i < valsArray.length; i++) {
				valsMap.put(KEY_LIST.get(i), Float.parseFloat(valsArray[i]));
			}
		} else {
			// パラメタ値がない場合は空の値をセット
			KEY_LIST.forEach(k -> {
				valsMap.put(k, null);
			});
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
		return KEY_LIST;
	}

	private void initValues(List<String> info) {
		// パラメタ値が記述されている行の抽出
		info.forEach(line -> {
			if(line.startsWith(Header.PARAMS.get()))
				vals = line.replaceAll(Header.PARAMS.get(), Util.EMPTY.get());
		});
	}

	private void initComment(List<String> info) {
		// 初期化
		comment = Util.EMPTY.get();
		// コメントが記述されている行の抽出
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
