package com.androidtalk.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextOperation {

    public static final String TAG = TextOperation.class.getSimpleName();

    /**
     * 根据路径filePath来读取文件中内容
     *
     * @param filePath
     * @return
     */
    public static String ReadTxtFile(String filePath) {
        String path = filePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.v(TAG, "文件不存在!");
        } else {
            try {
                InputStream inputStream = new FileInputStream(file);
                if (inputStream != null) {
                    InputStreamReader streamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String line;
                    //分行读取
                    while ((line = bufferedReader.readLine()) != null) {
                        content += line + "\n";
                    }
                    inputStream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(TAG, "TestFile:" + e.getMessage());
            }
        }
        return content;
    }

}