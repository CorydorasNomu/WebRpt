package com.nmr.app.svc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.nmr.app.util.ConstSet;

/**
 * jar内に抱え込んでいるリソースファイルへのアクセサクラス。
 *
 * @author nomu.shunn
 */
public class ResourceAccessService {

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
    public static List<String> getHTMLContents() throws IOException {
        // ストリームから文字列への変換
        return convert(HTML_TEMPLATE_URL);
    }

    /**
     * Jar内のCSSテンプレートの内容を取得する。
     * @return Jar内のCSSテンプレートの内容
     * @throws IOException CSSテンプレートの内容取得に失敗
     */
    public static List<String> getCSSContents() throws IOException {
        // ストリームから文字列への変換
        return convert(CSS_TEMPLATE_URL);
    }

    private static List<String> convert(String template) throws IOException {
        List<String> contents = new ArrayList<>();
        // リソースを読み込んだストリームからリストを生成
        try(InputStream is = ResourceAccessService.class.getResourceAsStream(template);
                InputStreamReader isr = new InputStreamReader(is, ConstSet.UTF8);
                BufferedReader br = new BufferedReader(isr)) {
            br.lines().forEach(line -> {
                contents.add(line);
            });
        }
        return contents;
    }
}
