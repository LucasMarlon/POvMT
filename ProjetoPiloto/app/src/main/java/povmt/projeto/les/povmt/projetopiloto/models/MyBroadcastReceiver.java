package povmt.projeto.les.povmt.projetopiloto.models;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import povmt.projeto.les.povmt.projetopiloto.R;
import povmt.projeto.les.povmt.projetopiloto.views.MainActivity;

public class MyBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context pContext, Intent pIntent) {
        if (pIntent.getAction().equals(MainActivity.ACTION)) {
            Log.d("Alarm Receiver", "onReceive called");
            NotificationCompat.Builder notificationBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(pContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("POvMAT")
                        .setContentText("Você tem atividades para adicionar o Ti");
            Intent resultIntent = new Intent(pContext, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(pContext);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) pContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, notificationBuilder.build());
        }
    }


}