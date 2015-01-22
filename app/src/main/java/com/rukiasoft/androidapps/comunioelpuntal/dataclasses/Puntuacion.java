package com.rukiasoft.androidapps.comunioelpuntal.dataclasses;

import java.io.Serializable;

import com.rukiasoft.androidapps.comunioelpuntal.MainActivity;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

public class Puntuacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer Id = null;
    private String tabla = null;
    private String nombre = null;
    private Double jornada = null;
    private Integer puntuacion_jornada = null;
    private Integer posicion_jornada = null;
    private Integer puntuacion_general = null;
    private Integer posicion_general = null;

    private Boolean publicado = false;
    private Integer goles = 0;
    private Boolean portero = false;
    private Boolean remo_equipo = false;
    private Boolean remo_jugadores = false;
    private Boolean remo_trupita = false;
    private Boolean prima_jornada = false;
    private Integer prima_general = 0;

    private Integer dinero_prima_jornada = 0;
    private Integer dinero_prima_general = 0;
    private Integer dinero_goles = 0;
    private Integer dinero_portero = 0;
    private Integer dinero_remo_equipo = 0;
    private Integer dinero_remo_jugadores = 0;
    private Integer dinero_remo_trupita = 0;

    public Integer getPosicion_general() {
        return posicion_general;
    }

    public void setPosicion_general(Integer posicion_general) {
        this.posicion_general = posicion_general;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Double getJornada() {
        return jornada;
    }

    public void setJornada(Double jornada) {
        this.jornada = jornada;
    }

    public Integer getPuntuacion_jornada() {
        return puntuacion_jornada;
    }

    public void setPuntuacion_jornada(Integer puntuacion_jornada) {
        this.puntuacion_jornada = puntuacion_jornada;
    }

    public Integer getPosicion_jornada() {
        return posicion_jornada;
    }

    public void setPosicion_jornada(Integer posicion_jornada) {
        this.posicion_jornada = posicion_jornada;
    }

    public Integer getPuntuacion_general() {
        return puntuacion_general;
    }

    public void setPuntuacion_general(Integer puntuacion_general) {
        this.puntuacion_general = puntuacion_general;
    }

    public Boolean getPublicado() {
        return publicado;
    }

    public void setPublicado(Boolean publicado) {
        this.publicado = publicado;
    }

    public Integer getGoles() {
        return goles;
    }

    public void setGoles(Integer goles) {
        this.goles = goles;
        dinero_goles = this.goles * MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_GOAL);
    }

    public Boolean getPortero() {
        return portero;
    }

    public void setPortero(Boolean portero) {
        this.portero = portero;
        dinero_portero = this.portero ? MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_GOALKEEPER) : 0;
    }

    public Boolean getRemo_equipo() {
        return remo_equipo;
    }

    public void setRemo_equipo(Boolean remo_equipo) {
        this.remo_equipo = remo_equipo;
        dinero_remo_equipo = this.remo_equipo ? MainActivity.getdbHandler().getOption(ComunioConstants.REMO_MAX_TEAMS) : 0;
    }

    public Boolean getRemo_jugadores() {
        return remo_jugadores;
    }

    public void setRemo_jugadores(Boolean remo_jugadores) {
        this.remo_jugadores = remo_jugadores;
        dinero_remo_jugadores = this.remo_jugadores ? MainActivity.getdbHandler().getOption(ComunioConstants.REMO_MAX_PLAYERS) : 0;
    }

    public Boolean getRemo_trupita() {
        return remo_trupita;
    }

    public void setRemo_trupita(Boolean remo_trupita) {
        this.remo_trupita = remo_trupita;
        dinero_remo_trupita = this.remo_trupita ? MainActivity.getdbHandler().getOption(ComunioConstants.REMO_TRUPITAS) : 0;
    }

    public Boolean getPrima_jornada() {
        return prima_jornada;
    }

    public void setPrima_jornada(Boolean prima_jornada) {
        this.prima_jornada = prima_jornada;
        dinero_prima_jornada = this.prima_jornada ? MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_LAST_IN_ROUND) : 0;
    }

    public Integer getPrima_general() {
        return prima_general;
    }

    public void setPrima_general(Integer prima_general) {
        this.prima_general = prima_general;
        dinero_prima_general = this.prima_general.equals(ComunioConstants.CODIGO_SI_COBRA_PRIMA) ? MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_LAST_IN_ROUND) : 0;
    }

    public Integer getDinero_prima_jornada() {
        return dinero_prima_jornada;
    }

    public void setDinero_prima_jornada(Integer dinero_prima_jornada) {
        this.dinero_prima_jornada = dinero_prima_jornada;
    }

    public Integer getDinero_prima_general() {
        return dinero_prima_general;
    }

    public void setDinero_prima_general(Integer dinero_prima_general) {
        this.dinero_prima_general = dinero_prima_general;
    }

    public Integer getDinero_goles() {
        return dinero_goles;
    }

    public void setDinero_goles(Integer dinero_goles) {
        this.dinero_goles = dinero_goles;
    }

    public Integer getDinero_portero() {
        return dinero_portero;
    }

    public void setDinero_portero(Integer dinero_portero) {
        this.dinero_portero = dinero_portero;
    }

    public Integer getDinero_remo_equipo() {
        return dinero_remo_equipo;
    }

    public void setDinero_remo_equipo(Integer dinero_remo_equipo) {
        this.dinero_remo_equipo = dinero_remo_equipo;
    }

    public Integer getDinero_remo_jugadores() {
        return dinero_remo_jugadores;
    }

    public void setDinero_remo_jugadores(Integer dinero_remo_jugadores) {
        this.dinero_remo_jugadores = dinero_remo_jugadores;
    }

    public Integer getDinero_remo_trupita() {
        return dinero_remo_trupita;
    }

    public void setDinero_remo_trupita(Integer dinero_remo_trupita) {
        this.dinero_remo_trupita = dinero_remo_trupita;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
