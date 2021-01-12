package com.example.test2;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.test2.Database.Tables.Mesec;
import com.example.test2.Database.Tables.Racun;
import com.example.test2.Database.ViewModels.KuponkoViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class Notification_reciver extends BroadcastReceiver {

    private KuponkoViewModel viewModel;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1) {
            Application application = (Application)context.getApplicationContext();
            viewModel = new KuponkoViewModel(application);

            Mesec mesec = viewModel.getAllMesec().get(0);

            Calendar date = Calendar.getInstance();
            date.setTime(mesec.getDatum());
            date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
            date.set(Calendar.HOUR_OF_DAY, date.getActualMaximum(Calendar.HOUR_OF_DAY));
            date.set(Calendar.MINUTE, date.getActualMaximum(Calendar.MINUTE));
            date.set(Calendar.SECOND, date.getActualMaximum(Calendar.SECOND));
            date.set(Calendar.MILLISECOND, date.getActualMaximum(Calendar.MILLISECOND));
            mesec.setRacuni((ArrayList<Racun>) viewModel.getAllRacunsByMonth(mesec.getDatum(), date.getTime()));

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Notification_start.CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_racunkologo)
                    .setContentTitle("Mesečna poraba - " + mesec.getDisplayDate())
                    .setContentText("Porabili ste - " + String.format("%.2f", mesec.getStroski()) + "€")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        }
    }
}
