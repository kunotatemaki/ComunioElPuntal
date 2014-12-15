package com.rukiasoft.androidapps.comunioelpuntal.dataclasses;

public class Posicion {
    private String nombre;
    private String tabla;
    private Integer puntos;
    private Integer prima = 0;
    private Double jornada;
    private String tipoPosicion = "";
    private String tipoPrima = "";
    private Integer posicion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Integer getPrima() {
        return prima;
    }

    public void setPrima(Integer prima) {
        this.prima = prima;
    }

    public Double getJornada() {
        return jornada;
    }

    public void setJornada(Double jornada) {
        this.jornada = jornada;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public String getTipoPosicion() {
        return tipoPosicion;
    }

    public void setTipoPosicion(String tipoPosicion) {
        this.tipoPosicion = tipoPosicion;
    }

    public String getTipoPrima() {
        return tipoPrima;
    }

    public void setTipoPrima(String tipoPrima) {
        this.tipoPrima = tipoPrima;
    }

}
