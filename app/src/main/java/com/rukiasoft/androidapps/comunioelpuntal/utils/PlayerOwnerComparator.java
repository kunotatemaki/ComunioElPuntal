package com.rukiasoft.androidapps.comunioelpuntal.utils;

import com.rukiasoft.androidapps.comunioelpuntal.PlayerItem;

import java.io.Serializable;

public class PlayerOwnerComparator implements java.util.Comparator<PlayerItem>, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(PlayerItem p1, PlayerItem p2) {
        String owner1 = p1.getOwner();
        String owner2 = p2.getOwner();
        if (owner2.compareTo(ComunioConstants.COMPUTER) == 0)
            return -1;
        else if (owner1.compareTo(ComunioConstants.COMPUTER) == 0)
            return 1;
        else
            return owner1.compareTo(owner2);
    }
}
