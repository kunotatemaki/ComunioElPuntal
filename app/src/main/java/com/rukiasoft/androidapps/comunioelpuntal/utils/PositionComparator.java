package com.rukiasoft.androidapps.comunioelpuntal.utils;

import com.rukiasoft.androidapps.comunioelpuntal.PlayerItem;

public class PositionComparator implements java.util.Comparator<PlayerItem> {
    @Override
    public int compare(PlayerItem p1, PlayerItem p2) {
        String position1 = p1.getPosition();
        String position2 = p2.getPosition();
        Integer codigo1 = 0;
        Integer codigo2 = 0;
        for (int i = 0; i < ComunioConstants.PLAYING_POSITION.length; i++) {
            if (position2.compareTo(ComunioConstants.PLAYING_POSITION[i]) == 0)
                codigo2 = i;
            if (position1.compareTo(ComunioConstants.PLAYING_POSITION[i]) == 0)
                codigo1 = i;
        }
        return codigo1.compareTo(codigo2);
    }
}
