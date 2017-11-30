package com.yibao.biggirl.mvp.music.musiclist;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.yibao.biggirl.MyApplication;
import com.yibao.biggirl.R;
import com.yibao.biggirl.model.music.MusicInfo;
import com.yibao.biggirl.model.music.MusicStatusBean;
import com.yibao.biggirl.service.AudioPlayService;
import com.yibao.biggirl.util.LogUtil;
import com.yibao.biggirl.util.RxBus;
import com.yibao.biggirl.util.StringUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author：Sid
 * Des：${音乐通知栏}
 * Time:2017/9/3 02:12
 */
public class MusicNoification {


    private static RemoteViews remoteView;
    private static RxBus       mBus;

    public static Notification getNotification(Context context, MusicInfo info)
    {

        mBus = MyApplication.getIntstance()
                            .bus();
        remoteView = getRemotViews(context,
                                   StringUtil.getAlbulm(info.getAlbumId()),
                                   info.getTitle(),
                                   info.getArtist());


        //        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification.Builder builder = new Notification.Builder(context);
        Notification notification = builder.setSmallIcon(R.mipmap.biggirl)
                                           .setOngoing(true)
                                           .setAutoCancel(true)
                                           .setContent(remoteView)
                                           .build();
        notification.bigContentView = remoteView;

        return notification;
    }

    private static RemoteViews getRemotViews(Context context,
                                             Uri uri,
                                             String songName,
                                             String artistName)
    {
        remoteView = new RemoteViews(context.getPackageName(), R.layout.music_notify);
        remoteView.setTextViewText(R.id.widget_title, songName);
        remoteView.setTextViewText(R.id.widget_artist, artistName);
        remoteView.setImageViewUri(R.id.widget_album, uri);     //通知栏的专辑图片
        remoteView.setImageViewResource(R.id.widget_close, R.mipmap.notifycation_close);
        remoteView.setImageViewResource(R.id.widget_prev, R.mipmap.notifycation_prev);
        remoteView.setImageViewResource(R.id.widget_next, R.mipmap.notifycation_next);
        updatePlayBtn(); //通知栏的播放按钮监听
        remoteViewListenr(context, remoteView);
        return remoteView;

    }

    private static void updatePlayBtn() {
        mBus.toObserverable(MusicStatusBean.class)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(bean -> {
                LogUtil.d("*****  Noti  " + bean.isPlay);
                if (bean.isPlay) {
                    remoteView.setImageViewResource(R.id.widget_play, R.mipmap.notifycation_play);
                } else {
                    remoteView.setImageViewResource(R.id.widget_play, R.mipmap.notifycation_pause);

                }
            });


    }

    private static void remoteViewListenr(Context context, RemoteViews remoteViews) {
        Intent intent = new Intent(AudioPlayService.ACTION_MUSIC);

        //Root
        intent.putExtra(AudioPlayService.BUTTON_ID, AudioPlayService.ROOT);
        PendingIntent intentRoot = PendingIntent.getBroadcast(context,
                                                              AudioPlayService.ROOT,
                                                              intent,
                                                              PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notice, intentRoot);

        //Pre
        intent.putExtra(AudioPlayService.BUTTON_ID, AudioPlayService.PREV);
        PendingIntent intentPrev = PendingIntent.getBroadcast(context,
                                                              AudioPlayService.PREV,
                                                              intent,
                                                              PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_prev, intentPrev);

        //Play
        intent.putExtra(AudioPlayService.BUTTON_ID, AudioPlayService.PLAY);
        PendingIntent intentPlay = PendingIntent.getBroadcast(context,
                                                              AudioPlayService.PLAY,
                                                              intent,
                                                              PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_play, intentPlay);
        //Next
        intent.putExtra(AudioPlayService.BUTTON_ID, AudioPlayService.NEXT);
        PendingIntent intentNext = PendingIntent.getBroadcast(context,
                                                              AudioPlayService.NEXT,
                                                              intent,
                                                              PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_next, intentNext);
        //Close
        intent.putExtra(AudioPlayService.BUTTON_ID, AudioPlayService.CLOSE);
        PendingIntent intentClose = PendingIntent.getBroadcast(context,
                                                               AudioPlayService.CLOSE,
                                                               intent,
                                                               PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget_close, intentClose);


    }


}