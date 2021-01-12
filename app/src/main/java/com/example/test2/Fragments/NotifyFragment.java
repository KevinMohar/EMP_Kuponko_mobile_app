package com.example.test2.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.test2.Notification_reciver;
import com.example.test2.R;

import java.util.Calendar;

public class NotifyFragment extends Fragment {

    private View RootView;
    private Switch switcher;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String CHECK_SWITCH = "check_switch";
    private boolean isSwitched;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RootView = inflater.inflate(R.layout.fragment_notify, container, false);

        switcher = RootView.findViewById(R.id.switch_notify);
        loadData();
        updateViews();

        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSwitched = switcher.isChecked();
                saveData();

                Intent intent = new Intent(NotifyFragment.this.getContext(), Notification_reciver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(NotifyFragment.this.getContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                if (switcher.isChecked()) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 5*1000, AlarmManager.INTERVAL_DAY, pendingIntent);
                }
                else {
                    alarmManager.cancel(pendingIntent);
                }
            }
        });

        return RootView;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = this.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHECK_SWITCH, switcher.isChecked());
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = this.getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        isSwitched = sharedPreferences.getBoolean(CHECK_SWITCH, false);
    }

    public void updateViews() {
        switcher.setChecked(isSwitched);
    }
}