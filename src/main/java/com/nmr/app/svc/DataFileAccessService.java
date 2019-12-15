package com.nmr.app.svc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.nmr.app.log.ServiceLogger;
import com.nmr.app.util.ConstSet;
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

            Consumer<Path> mkMap = p -> {
                String fileName = p.toFile().getName();
                if(fileName.endsWith(Extension.JPG.get()) || fileName.endsWith(Extension.PNG.get()))
                    imgs.add(p.toAbsolutePath());
                if(INFO_TXT.equals(fileName))
                    info = p.toAbsolutePath();		// 付帯情報のパスを取得
            };

            files.filter(p -> p.toFile().isFile())
                .forEach(mkMap);
        } catch(IOException e) {
            ServiceLogger.error("Fail to get file path list of a data directory.");
            throw e;
        }
    }

    /**
     * 画像データのパスを取得する。
     * @return 画像データのパス
     */
    public HashMap<String, Path> getImages() {
        // 画像を保持するマップ
        HashMap<String, Path> imgMap = new HashMap<>();

        int counter = 0;
        for(String tag : ConstSet.ImagePathTag) {
            try {
                imgMap.put(tag, imgs.get(counter));
            } catch(IndexOutOfBoundsException e) {
                ServiceLogger.warn("You do NOT have " + (counter+1) + " image data.");
            }
            counter++;
        }
        return imgMap;
    }

    /**
     * 付帯情報ファイルのパスを取得する。
     * @return 付帯情報ファイル
     */
    public Path getInfo() {
        return info;
    }
}
