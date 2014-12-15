package com.rukiasoft.androidapps.comunioelpuntal;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

import java.io.Serializable;


/**
 * Created by Ruler on 2014.
 */
public class SettingsFragment extends PreferenceFragment implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (ActivityTool.hasVibrator(getActivity().getApplicationContext()))
            addPreferencesFromResource(R.xml.options);
        else
            addPreferencesFromResource(R.xml.options_not_vibrate);
    }
}


