package com.yibao.biggirl.util;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import static android.support.design.widget.Snackbar.make;


/**
 * 作者：Stran on 2017/3/28 01:31
 * 描述：${}
 * 邮箱：strangermy@outlook.com
 */
public class SnakbarUtil {
    private static int errorColor   = Color.rgb(255, 64, 129);
    private static int successColor = Color.rgb(90, 181, 63);
    private static int saveColor    = Color.argb(255, 245, 115, 160);
    private static int ExitColor    = Color.argb(255, 230, 195, 65);

    /**
     * 下载成功提示
     */
    public static void showSuccessStatus(View view) {
        Snackbar snackbar = make(view, "图片保存成功 -_-", Snackbar.LENGTH_LONG);
        snackbar.getView()
                .setBackgroundColor(successColor);
        snackbar.show();

    }

    /**
     * 下载失败提示
     */
    public static void showErrorStatus(View view) {

        Snackbar snackbar = make(view, "图片保存失败 -_-", Snackbar.LENGTH_LONG);
        snackbar.getView()
                .setBackgroundColor(errorColor);
        snackbar.show();

    }


    /**
     * 保存图片提示
     */
    public static void savePic(final View view, final String url)
    {


        Snackbar snackbar = make(view, "可以将图片保存起来-_-", Snackbar.LENGTH_LONG).setAction("保存图片",
                                                                                       view1 -> ImageUitl.downloadPic(
                                                                                               url,
                                                                                               true));
        snackbar.getView()
                .setBackgroundColor(saveColor);
        snackbar.show();
    }

    /**
     * 网络异常提示
     */
    public static void netErrors(View view)
    {

        Snackbar snackbar = make(view, "网络异常，请检查您的网络连接 -_-", Snackbar.LENGTH_SHORT);
        snackbar.getView()
                .setBackgroundColor(errorColor);

        snackbar.show();
    }

    /**
     * 退出程序
     */
    public static void finishActivity(View view)
    {

        Snackbar snackbar = make(view, "再按一次我就离开了 -_-", Snackbar.LENGTH_SHORT);
        snackbar.getView()
                .setBackgroundColor(ExitColor);
        snackbar.show();

    }

    /**
     * 关闭Snakbar
     */
    public static void setWallpaer(View view)
    {
        int      color    = Color.rgb(90, 181, 63);
        Snackbar snackbar = make(view, "壁纸设置成功  -_-", Snackbar.LENGTH_LONG);
        snackbar.getView()
                .setBackgroundColor(color);
        snackbar.show();

    }


}