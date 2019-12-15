package com.nmr.app.svc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet.Extension;
import com.nmr.app.util.ConstSet.Symbol;

/**
 * レポートページのHTMLを生成するクラス。
 *
 * @author nomu.shunn
 */
public class HTMLService {

    // 置換で使用する定数の宣言
    private enum Tags {
        TAG("%"),               // 置換開始の識別子
        TITLE("%title%"),       // タイトルの置換文字列
        COMMENT("%comment%"),   // コメントの置換文字列
        PREV("%prev%"),         // PREVリンクの置換文字列
        NEXT("%next%");         // NEXTリンクの置換文字列

        private final String str;

        private Tags(String s) {
            str = s;
        }
        public String get() {
            return str;
        }
    }

    // レポート出力用ディレクトリのパス
    private Path reportDirPath = null;
    // レポートページのインデックス
    private ArrayList<IndexInfo> indexInfo = new ArrayList<>();
    // レポートページのHTMLソース
    private List<String> htmlContents = new ArrayList<>();

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
                    Symbol.SEPARATOR.get() + pageTitle + Extension.HTML.get();
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
        String name = Symbol.SEPARATOR.get() + ResourceAccessService.CSS_TEMPLATE;
        String cssPath = reportDirPath.toFile().getAbsolutePath() + name;

        try {
            // スタイルシートの内容には変更がないので、リソースの中身をそのまま出力
            Files.write(Paths.get(cssPath), ResourceAccessService.getCSSContents(),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        } catch(IOException e) {
            ServiceLogger.error("Fail to create CSS from \"" + cssPath.toString() + "\"");
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
            htmlContents = ResourceAccessService.getHTMLContents();

            // タイトルを置換
            htmlContents = replace(Tags.TITLE.get(), idx.getTitle());

            // リンクを置換
            setLink(i);

            // パラメタテーブルのヘッダを置換
            info.getTableHeaders().forEach((tag, val) -> {
                htmlContents = replace(Tags.TAG.get() + tag + Tags.TAG.get(), val);
            });

            // パラメタテーブルの値を置換
            info.getTableValues().forEach((tag, val) -> {
                htmlContents = replace(
                        Tags.TAG.get() + tag + Tags.TAG.get(),
                        val == null ? Symbol.EMPTY.get() : val
                );
            });

            // 画像を置換
            dataFiles.getImages().forEach((tag, path) -> {
                htmlContents = replace(tag, path.toString().replaceAll(Symbol.ESCAPE.get(), Symbol.SEPARATOR.get()));
            });

            // コメントを置換
            htmlContents = replace(Tags.COMMENT.get(), info.getComment());

            // レポートページの出力
            try {
                Files.write(Paths.get(idx.getPath()), htmlContents,
                        StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            } catch(IOException e) {
                ServiceLogger.error("Fail to create report pages: \"" + idx.getPath() + "\"");
                throw e;
            }
        }
    }

    private void setLink(int index) {
        // リンク先のパス。TOPのページなら「PREV」は存在しない
        String prev = index == 0 ? Symbol.EMPTY.get() : Symbol.CURRENT.get() +
                                    indexInfo.get(index - 1).getTitle() + Extension.HTML.get();
        // リンク先のパス。最後のページなら「NEXT」は存在しない
        String next = index == indexInfo.size() - 1 ? Symbol.EMPTY.get() : Symbol.CURRENT.get() +
                                    indexInfo.get(index + 1).getTitle() + Extension.HTML.get();
        // リンクを置換
        if(index == 0) {
            htmlContents = replace(Tags.NEXT.get(), next);
            htmlContents = replace(Tags.PREV.get(), Symbol.EMPTY.get());
        } else if(index == (indexInfo.size() - 1)) {
            htmlContents = replace(Tags.NEXT.get(), Symbol.EMPTY.get());
            htmlContents = replace(Tags.PREV.get(), prev);
        } else {
            htmlContents = replace(Tags.NEXT.get(), next);
            htmlContents = replace(Tags.PREV.get(), prev);
        }
    }

    private List<String> replace(String tag, String val) {
        return htmlContents.stream()
                .map(line -> line.replaceAll(tag, val))
                .collect(Collectors.toList());
    }

    /**
     * レポートページのインデックス情報。
     */
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
