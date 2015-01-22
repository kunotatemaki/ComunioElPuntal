package com.rukiasoft.androidapps.comunioelpuntal;

import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Participante;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Player;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Puntuacion;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Signing;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GamerInformation implements Serializable {

    /**
     *
     */
    //private static final String TAG = "GamerInformation";
    private static final long serialVersionUID = 1L;
    private final List<PlayerItem> playerItems = new ArrayList<>();
    private final List<PlayerItem> ventas = new ArrayList<>();
    private final List<PlayerItem> fichajes = new ArrayList<>();
    private List<Puntuacion> puntuaciones = new ArrayList<>();
    private Integer dineroTotal = 0;
    private Integer dineroPrimasGoles = 0;
    private Integer dineroPrimasPortero = 0;
    private Integer dineroPrimasJornada = 0;
    private Integer dineroPrimasGeneral = 0;
    private Integer dineroFichajes = 0;
    private Integer dineroVentas = 0;
    private Integer dineroRemoJugadores = 0;
    private Integer dineroRemoEquipos = 0;
    private Integer dineroRemoTrupita = 0;
    private Integer dineroPuntos = 0;
    private Integer numeroJugadores = 0;
    private Integer puntosTotales = 0;
    private Integer currentRanking = 0;
    private Integer primaInicial = 0;
    private Participante participante;


    public GamerInformation() {
        //MainActivity.getdbHandler() = new DatabaseHandler(MainActivity.getContext());

    }

    public void setGamerInfo(Participante gamer) {
        this.participante = gamer;
        loadGamerInfo();
    }

    private void loadGamerInfo() {
        dineroTotal = 0;
        dineroPrimasGoles = 0;
        dineroPrimasPortero = 0;
        dineroPrimasJornada = 0;
        dineroPrimasGeneral = 0;
        dineroFichajes = 0;
        dineroVentas = 0;
        dineroRemoJugadores = 0;
        dineroRemoEquipos = 0;
        dineroRemoTrupita = 0;
        currentRanking = 0;
        primaInicial = 0;

        List<Player> players;
        try {
            players = MainActivity.getdbHandler().getPlayerList(participante.getNombre(),
                    DatabaseOpenHelper.PROPIETARIO);
            numeroJugadores = players.size();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        playerItems.clear();
        for (int i = 0; i < players.size(); i++) {
            playerItems.add(new PlayerItem(players.get(i), MainActivity.getContext()));
        }

        //Log.d(TAG, "cargando fichajes");
        List<Signing> movimientos;
        players.clear();
        try {
            movimientos = MainActivity.getdbHandler().getSignings(participante.getNombre(),
                    DatabaseOpenHelper.COMPRADOR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        fichajes.clear();
        for (int i = 0; i < movimientos.size(); i++) {
            Player player = null;
            try {
                player = MainActivity.getdbHandler().getPlayerList(movimientos.get(i).getNombre(), DatabaseOpenHelper.NOMBRE).get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dineroFichajes += movimientos.get(i).getPrecio();
            if (!players.contains(player)) {
                players.add(player);
                fichajes.add(new PlayerItem(players.get(i), MainActivity.getContext()));
            }
        }

        //Log.d(TAG, "cargando ventas");
        players.clear();
        try {
            movimientos = MainActivity.getdbHandler().getSignings(participante.getNombre(),
                    DatabaseOpenHelper.VENDEDOR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        ventas.clear();
        for (int i = 0; i < movimientos.size(); i++) {
            Player player = null;
            try {
                player = MainActivity.getdbHandler().getPlayerList(movimientos.get(i).getNombre(), DatabaseOpenHelper.NOMBRE).get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dineroVentas += movimientos.get(i).getPrecio();
            if (!players.contains(player)) {
                players.add(player);
                ventas.add(new PlayerItem(players.get(i), MainActivity.getContext()));
            }
        }

        //Log.d(TAG, "cargando puntuaciones");
        try {
            puntuaciones = MainActivity.getdbHandler().getScores(participante);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        for (int i = 0; i < puntuaciones.size(); i++) {
            Puntuacion puntuacion = puntuaciones.get(i);
            if (puntuacion.getPuntuacion_jornada() != null)
                dineroPuntos += puntuacion.getPuntuacion_jornada() * MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_POINTS);
            dineroPrimasGoles += puntuacion.getGoles() * MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_GOAL);
            dineroPrimasPortero += puntuacion.getPortero() ? MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_GOALKEEPER) : 0;
            dineroPrimasJornada += puntuacion.getPrima_jornada() ? MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_LAST_IN_ROUND) : 0;
            dineroPrimasGeneral += puntuacion.getPrima_general().equals(ComunioConstants.CODIGO_SI_COBRA_PRIMA) ? MainActivity.getdbHandler().getOption(ComunioConstants.BONUS_LAST_IN_CLASSIFICATION) : 0;
            dineroRemoJugadores += puntuacion.getRemo_jugadores() ? MainActivity.getdbHandler().getOption(ComunioConstants.REMO_MAX_PLAYERS) : 0;
            dineroRemoEquipos += puntuacion.getRemo_equipo() ? MainActivity.getdbHandler().getOption(ComunioConstants.REMO_MAX_TEAMS) : 0;
            dineroRemoTrupita += puntuacion.getRemo_trupita() ? MainActivity.getdbHandler().getOption(ComunioConstants.REMO_TRUPITAS) : 0;
            puntosTotales = puntuacion.getPuntuacion_general();

            currentRanking = puntuacion.getPosicion_general();
        }

        primaInicial = participante.getPrima_inicial();

        dineroTotal = MainActivity.getdbHandler().getOption(ComunioConstants.STARTING_MONEY) + primaInicial - dineroFichajes + dineroPuntos
                + dineroPrimasGoles + dineroPrimasPortero + dineroPrimasGeneral + dineroPrimasJornada
                - dineroRemoEquipos - dineroRemoJugadores - dineroRemoTrupita + dineroVentas;
    }

    public Integer getDineroTotal() {
        return dineroTotal;
    }

    public void setDineroTotal(Integer dineroTotal) {
        this.dineroTotal = dineroTotal;
    }

    public Integer getDineroPrimasGoles() {
        return dineroPrimasGoles;
    }

    public void setDineroPrimasGoles(Integer dineroPrimasGoles) {
        this.dineroPrimasGoles = dineroPrimasGoles;
    }

    public Integer getDineroPrimasPortero() {
        return dineroPrimasPortero;
    }

    public void setDineroPrimasPortero(Integer dineroPrimasPortero) {
        this.dineroPrimasPortero = dineroPrimasPortero;
    }

    public Integer getDineroPrimasJornada() {
        return dineroPrimasJornada;
    }

    public void setDineroPrimasJornada(Integer dineroPrimasJornada) {
        this.dineroPrimasJornada = dineroPrimasJornada;
    }

    public Integer getDineroPrimasGeneral() {
        return dineroPrimasGeneral;
    }

    public void setDineroPrimasGeneral(Integer dineroPrimasGeneral) {
        this.dineroPrimasGeneral = dineroPrimasGeneral;
    }

    public Integer getDineroFichajes() {
        return dineroFichajes;
    }

    public void setDineroFichajes(Integer dineroFichajes) {
        this.dineroFichajes = dineroFichajes;
    }

    public Integer getDineroVentas() {
        return dineroVentas;
    }

    public void setDineroVentas(Integer dineroVentas) {
        this.dineroVentas = dineroVentas;
    }

    public Integer getDineroRemoJugadores() {
        return dineroRemoJugadores;
    }

    public void setDineroRemoJugadores(Integer dineroRemoJugadores) {
        this.dineroRemoJugadores = dineroRemoJugadores;
    }

    public Integer getDineroRemoEquipos() {
        return dineroRemoEquipos;
    }

    public void setDineroRemoEquipos(Integer dineroRemoEquipos) {
        this.dineroRemoEquipos = dineroRemoEquipos;
    }

    public Integer getDineroRemoTrupita() {
        return dineroRemoTrupita;
    }

    public void setDineroRemoTrupita(Integer dineroRemoTrupita) {
        this.dineroRemoTrupita = dineroRemoTrupita;
    }

    public Integer getDineroPuntos() {
        return dineroPuntos;
    }

    public void setDineroPuntos(Integer dineroPuntos) {
        this.dineroPuntos = dineroPuntos;
    }

    public Participante getParticipante() {
        return participante;
    }

    public List<PlayerItem> getPlayerItems() {
        return playerItems;
    }

    public List<PlayerItem> getVentas() {
        return ventas;
    }

    public List<PlayerItem> getFichajes() {
        return fichajes;
    }

    public List<Puntuacion> getPuntuaciones() {
        return puntuaciones;
    }

    public Integer getNumeroJugadores() {
        return numeroJugadores;
    }

    public Integer getPuntosTotales() {
        return puntosTotales;
    }

    public Integer getCurrentRanking() {
        return currentRanking;
    }

    public void setCurrentRanking(Integer currentRanking) {
        this.currentRanking = currentRanking;
    }

    public Integer getPrimaInicial() {
        return primaInicial;
    }

    public void setPrimaInicial(Integer primaInicial) {
        this.primaInicial = primaInicial;
    }

}
