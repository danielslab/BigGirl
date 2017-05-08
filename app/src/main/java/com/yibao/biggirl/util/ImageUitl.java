package com.yibao.biggirl.util;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.yibao.biggirl.MyApplication;
import com.yibao.biggirl.model.girl.DownGrilProgressData;
import com.yibao.biggirl.network.Api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者：Stran on 2017/3/23 03:23
 * 描述：${保存妹子}
 * 邮箱：strangermy@outlook.com
 */
public class ImageUitl {

    /**
     * 保存图片
     */
    public static void downloadPic(String url, boolean isShowPhotos)
    {
        String name = getNameFromUrl(url);
        File   path = new File(Constants.dir);
        if (!path.exists()) {
            path.mkdir();
        }
        File file = new File(path + "/", name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        Request request = new Request.Builder().url(url)
                                               .build();
        MyApplication.defaultOkHttpClient()
                     .newCall(request)
                     .enqueue(new Callback() {
                         @Override
                         public void onFailure(okhttp3.Call call, IOException e) {

                         }


                         @Override
                         public void onResponse(okhttp3.Call call, Response response)

                         {
                             InputStream      is;
                             byte[]           buf = new byte[1024 * 4];
                             int              len;
                             FileOutputStream fos = null;
                             //                      String SDPath = Environment.getExternalStorageDirectory()
                             //                                                 .getAbsolutePath();
                             is = response.body()
                                          .byteStream();
                             long total = response.body()
                                                  .contentLength();
                             try {
                                 fos = new FileOutputStream(file);

                                 long sum = 0;
                                 while ((len = is.read(buf)) != -1) {

                                     fos.write(buf, 0, len);
                                     sum += len;
                                     int progress = (int) (sum * 1.0f / total * 100);
                                     LogUtil.d("progress=" + progress);
                                     MyApplication.getIntstance()
                                                  .bus()
                                                  .post(new DownGrilProgressData(progress));

                                 }
                                 fos.flush();
                                 fos.close();
                                 LogUtil.d("文件下载成功");

                             } catch (IOException e) {
                                 e.printStackTrace();
                             } finally {
                                 try {
                                     fos.close();
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }


                         }


                     });
        if (isShowPhotos) {
            try {
                MediaStore.Images.Media.insertImage(MyApplication.getIntstance()
                                                                 .getContentResolver(),
                                                    file.getAbsolutePath(),
                                                    name,
                                                    null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            MyApplication.getIntstance()
                         .sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                                   Uri.parse("file://" + file)));
        }


    }


    //初始化数据
    public static List<String> getDefultUrl(List<String> list) {
        Collections.addAll(list, Api.picUrlArr);
        return list;
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    private static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

}







