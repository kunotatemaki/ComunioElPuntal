package com.rukiasoft.androidapps.comunioelpuntal.dataclasses;

import java.io.Serializable;

class Teams implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer Id;
    private String nombre;
    private String foto;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

}
