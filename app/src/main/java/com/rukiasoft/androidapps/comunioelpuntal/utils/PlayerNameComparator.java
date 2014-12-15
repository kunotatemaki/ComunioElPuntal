package com.rukiasoft.androidapps.comunioelpuntal.utils;

import com.rukiasoft.androidapps.comunioelpuntal.PlayerItem;

import java.io.Serializable;

public class PlayerNameComparator implements java.util.Comparator<PlayerItem>, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(PlayerItem p1, PlayerItem p2) {
        return p1.getName().compareTo(p2.getName());
    }
}
