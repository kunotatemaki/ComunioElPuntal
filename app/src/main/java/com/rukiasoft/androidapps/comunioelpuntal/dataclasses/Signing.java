package com.rukiasoft.androidapps.comunioelpuntal.dataclasses;

import java.io.Serializable;

public class Signing implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer Id;
    private String nombre;
    private String comprador;
    private String vendedor;
    private Integer precio;
    private Double jornada;

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

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Double getJornada() {
        return jornada;
    }

    public void setJornada(Double jornada) {
        this.jornada = jornada;
    }

}
