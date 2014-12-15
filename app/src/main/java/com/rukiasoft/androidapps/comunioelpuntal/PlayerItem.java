package com.rukiasoft.androidapps.comunioelpuntal;

import android.content.Context;

import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.DatabaseHandler;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Player;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

import java.io.Serializable;

// Do not modify 

public class PlayerItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer ID;
    private String Name;
    private String Position;
    private String Owner;
    private String Team;
    private Integer Shield;


    PlayerItem(Player player, Context context) {
        this.ID = player.getId();
        this.Name = player.getNombre();
        this.Position = player.getDemarcacion();
        this.Owner = player.getPropietario();
        this.Team = player.getEquipo();
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        try {
            String shield = dbHandler.getShieldName(this.Team);
            this.Shield = ActivityTool.getDrawableIdFromString(context.getResources(), shield, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Integer getShield() {
        return Shield;
    }

    public void setShield(Integer shield) {
        Shield = shield;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer iD) {
        ID = iD;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getTeam() {
        return Team;
    }

    public void setTeam(String team) {
        Team = team;
    }


}