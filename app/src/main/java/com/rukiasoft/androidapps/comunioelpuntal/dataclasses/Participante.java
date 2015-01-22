package com.rukiasoft.androidapps.comunioelpuntal.dataclasses;

import java.io.Serializable;

public class Participante implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer Id;
    private String nombre;
    private String login;
    private String email;
    private String gcm_regid;
    private String tabla;
    private Double j_inicio;
    private Double j_final;
    private Integer puntos_inicio;
    private Integer prima_inicial;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGcm_regid() {
        return gcm_regid;
    }

    public void setGcm_regid(String gcm_regid) {
        this.gcm_regid = gcm_regid;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Double getJ_inicio() {
        return j_inicio;
    }

    public void setJ_inicio(Double j_inicio) {
        this.j_inicio = j_inicio;
    }

    public Double getJ_final() {
        return j_final;
    }

    public void setJ_final(Double j_final) {
        this.j_final = j_final;
    }

    public Integer getPuntos_inicio() {
        return puntos_inicio;
    }

    public void setPuntos_inicio(Integer puntos_inicio) {
        this.puntos_inicio = puntos_inicio;
    }

    public Integer getPrima_inicial() {
        return prima_inicial;
    }

    public void setPrima_inicial(Integer prima_inicial) {
        this.prima_inicial = prima_inicial;
    }
}
