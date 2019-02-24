package com.example.kengo.servicesample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

public class SoundManagerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private MediaPlayer player;

    @Override
    public void onCreate(){
        player = new MediaPlayer();
        // 通知チャネルのID文字列を用意
        String id = "soundmanagerservice_notification_channel";
        //通知チャネル名をstring.xmlから取得
        String name = getString(R.string.notification_channel_name);
        // 通知チャネルの重要度を標準に設定
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        // 通知チャネルを生成
        NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 通知チャネルを設定
        notificationManager.createNotificationChannel((notificationChannel));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String mediaFileUriStr = "android.resource://" + getPackageName() + "/" + R.raw.test;
        Uri mediaFileUri = Uri.parse(mediaFileUriStr);
        try{
            player.setDataSource(SoundManagerService.this, mediaFileUri);
            player.setOnPreparedListener(new PlayerPreparedListener());
            player.setOnCompletionListener(new PlayerCompletionListener());
            player.prepareAsync();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        if (player.isPlaying()){
            player.stop();
        }
        player.release();
        player = null;
    }

    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener{
        @Override
        public void onPrepared(MediaPlayer mp){
            mp.start();
            NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(SoundManagerService.this, "soundmanagerservice_notification_channel");
            // 通知エリアに表示されるアイコン
            nbuilder.setSmallIcon(android.R.drawable.ic_dialog_info);
            // 通知ドロワーでの表示タイトルを設定
            nbuilder.setContentTitle(getString(R.string.msg_notification_title_start));
            // 通知ドロワーでの表示メッセージを設定
            nbuilder.setContentText(getString(R.string.msg_notification_text_start));
            // 起動先Activityクラスを指定したIntentオブジェクトを生成
            Intent intent = new Intent(SoundManagerService.this, SoundStartActivity.class);
            // 起動先アクティビティに引き継ぎデータを格納
            intent.putExtra("fromNotification", true);
            // PendingIntentオブジェクトを取得
            PendingIntent stopServiceIntent = PendingIntent.getActivity(SoundManagerService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            // PendingIntentオブジェクトをビルダーに設定
            nbuilder.setContentIntent(stopServiceIntent);
            // タップされた通知メッセージを自動的に消去するように設定
            nbuilder.setAutoCancel(true);
            // BuilderからNotificationオブジェクトを生成
            Notification notification = nbuilder.build();
            // NotificationManagerオブジェクトを取得
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知
            notificationManager.notify(1, notification);
        }
    }

    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mp){
            // Notioficationを作成するBuilderクラス生成
            NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(SoundManagerService.this, "soundmanagerservice_notification_channel");
            // 通知エリアに表示されるアイコン
            nbuilder.setSmallIcon(android.R.drawable.ic_dialog_info);
            // 通知ドロワーでの表示タイトルを設定
            nbuilder.setContentTitle(getString(R.string.msg_notification_title_finish));
            // 通知ドロワーでの表示メッセージを設定
            nbuilder.setContentText(getString(R.string.msg_notification_text_finish));
            // BuliderからNotificationオブジェクトを生成
            Notification notification = nbuilder.build();
            // NotificationManagerオブジェクトを取得
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知
            notificationManager.notify(0, notification);
            stopSelf();
        }
    }
}
