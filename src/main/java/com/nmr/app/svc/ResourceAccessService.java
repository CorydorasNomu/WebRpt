package com.nmr.app.svc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet.Common;

/**
 * jar内に抱え込んでいるリソースファイルへのアクセサクラス。
 *
 * @author nomu.shunn
 */
public class ResourceAccessService extends CommonFileAccessService {

	// HTMLテンプレートのファイル名
	public static final String HTML_TEMPLATE      =	"template.html";
	// CSSテンプレートのファイル名
	public static final String CSS_TEMPLATE       =	"template.css";
	// HTMLテンプレートのリソース内パス
	private static final String HTML_TEMPLATE_URL = "/html/" + HTML_TEMPLATE;
	// CSSテンプレートのリソース内パス
	private static final String CSS_TEMPLATE_URL  =	"/css/"  + CSS_TEMPLATE;

	/**
	 * コンストラクタ
	 */
	public ResourceAccessService() {
		// noop
	}

	/**
	 * Jar内のHTMLテンプレートの内容を取得する。
	 * @return Jar内のHTMLテンプレートの内容
	 * @throws IOException HTMLテンプレートの内容取得に失敗
	 */
	public static String getHTMLContents() throws IOException {
		// ストリームから文字列への変換
		return convert(ResourceAccessService.class.getResourceAsStream(HTML_TEMPLATE_URL));
	}

	/**
	 * Jar内のCSSテンプレートの内容を取得する。
	 * @return Jar内のCSSテンプレートの内容
	 * @throws IOException CSSテンプレートの内容取得に失敗
	 */
	public static String getCSSContents() throws IOException {
		// ストリームから文字列への変換
		return convert(ResourceAccessService.class.getResourceAsStream(CSS_TEMPLATE_URL));
	}

	private static String convert(InputStream in) throws IOException {
		String contents = Common.EMPTY.get();
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(in, Common.UTF8.get()))) {
			contents = br.lines().collect(Collectors.joining(Common.NEW_LINE.get()));
		} catch(IOException e) {
			ServiceLogger.error("Fail to get InputStream of resources in a jar.");
			throw e;
		}
		return contents;
	}
}
