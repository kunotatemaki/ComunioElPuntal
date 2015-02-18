package com.rukiasoft.androidapps.comunioelpuntal.dataclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.rukiasoft.androidapps.comunioelpuntal.DatabaseOpenHelper;
import com.rukiasoft.androidapps.comunioelpuntal.MainActivity;
import com.rukiasoft.androidapps.comunioelpuntal.NotificationItem;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHandler implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //DataBase
    private SQLiteDatabase mDB = null;
    private DatabaseOpenHelper mDbHelper;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Context context;
    private final static String TAG = "DatabaseHandler";

    public DatabaseHandler(Context context) {
        this.context = context;
    }

    public long storeData(JSONObject data) throws Exception {
        //Log.d(TAG, "STOREDATA");
        long resultado;
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            resultado = insertData(data);
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "no almaceno");
            throw e;
        }
        return resultado;
    }

    // Insert data in database
    private long insertData(JSONObject data) throws Exception {
        Log.d(TAG, "Insert data in database: ");
        long resultado;

        //grabo las configuraciones
        Integer value = data.getInt(ComunioConstants.BONUS_GOAL);
        if (!isTableExists(ComunioConstants.TABLE_CONF)) {
            mDbHelper.createConfigurationTable();
        }
        ContentValues option = new ContentValues();
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.BONUS_GOAL);
        option.put(DatabaseOpenHelper.VALUE, value);
        String[] args = new String[]{ComunioConstants.BONUS_GOAL};
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.BONUS_GOALKEEPER);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.BONUS_GOALKEEPER);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.BONUS_GOALKEEPER;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.BONUS_LAST_IN_ROUND);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.BONUS_LAST_IN_ROUND);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.BONUS_LAST_IN_ROUND;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.BONUS_LAST_IN_CLASSIFICATION);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.BONUS_LAST_IN_CLASSIFICATION);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.BONUS_LAST_IN_CLASSIFICATION;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.REMO_MAX_PLAYERS);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.REMO_MAX_PLAYERS);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.REMO_MAX_PLAYERS;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.REMO_MAX_TEAMS);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.REMO_MAX_TEAMS);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.REMO_MAX_TEAMS;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.REMO_TRUPITAS);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.REMO_TRUPITAS);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.REMO_TRUPITAS;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.STARTING_MONEY);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.STARTING_MONEY);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.STARTING_MONEY;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.BONUS_POINTS);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.BONUS_POINTS);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.BONUS_POINTS;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.MAX_PLAYERS_TEAM);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.MAX_PLAYERS_TEAM);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.MAX_PLAYERS_TEAM;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        value = data.getInt(ComunioConstants.MAX_PLAYERS_EACH_TEAM);
        option.put(DatabaseOpenHelper.OPTION, ComunioConstants.MAX_PLAYERS_EACH_TEAM);
        option.put(DatabaseOpenHelper.VALUE, value);
        args[0] = ComunioConstants.MAX_PLAYERS_EACH_TEAM;
        resultado = mDB.update(ComunioConstants.TABLE_CONF, option, DatabaseOpenHelper.OPTION + "=?", args);
        if (resultado < 0)
            throw new Exception("Error actualizando datos de configuracion");
        else if (resultado == 0) {
            resultado = mDB.insert(ComunioConstants.TABLE_CONF, null, option);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de configuracion");
        }

        Double dValue = data.getDouble(ComunioConstants.PROPERTY_START_ROUND);
        ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_START_ROUND, dValue);

        dValue = data.getDouble(ComunioConstants.PROPERTY_FINAL_ROUND);
        ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_FINAL_ROUND, dValue);

        dValue = data.getDouble(ComunioConstants.PROPERTY_CURRENT_ROUND);
        ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_CURRENT_ROUND, dValue);

        //grabo los nombres de las jornadas almacenadas
        JSONObject jornadas = data.getJSONObject(ComunioConstants.PROPERTY_JORNADAS);
        ActivityTool.savePreferences(context, ComunioConstants.PROPERTY_JORNADAS, jornadas.toString());
        MainActivity.resetJornadasJSON();

        //grabo los participantes
        JSONArray listGamersJSON;
        listGamersJSON = data.getJSONArray("participants");
        Log.d(TAG, "participants");
        if (!isTableExists(ComunioConstants.TABLE_GAMERS)) {
            mDbHelper.createGamersTable();
        }
        for (int i = 0; i < listGamersJSON.length(); i++) {
            Participante participante = mapper.readValue(listGamersJSON.get(i).toString(), Participante.class);
            ContentValues values = new ContentValues();
            values.put(DatabaseOpenHelper.NOMBRE, participante.getNombre());
            values.put(DatabaseOpenHelper.EMAIL, participante.getEmail());
            values.put(DatabaseOpenHelper.GCM_REGID, participante.getGcm_regid());
            values.put(DatabaseOpenHelper.TABLA, participante.getTabla());
            values.put(DatabaseOpenHelper.JORNADA_INICIO, participante.getJ_inicio());
            values.put(DatabaseOpenHelper.JORNADA_FINAL, participante.getJ_final());
            values.put(DatabaseOpenHelper.PUNTOS_INICIO, participante.getPuntos_inicio());
            values.put(DatabaseOpenHelper.PRIMA_INICIAL, participante.getPrima_inicial());
            args[0] = participante.getNombre();

            if (!isTableExists(participante.getTabla())) {
                mDbHelper.createScoreTable(participante.getTabla());
            }
            resultado = mDB.update(ComunioConstants.TABLE_GAMERS, values, DatabaseOpenHelper.NOMBRE + "=?", args);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de participantes");
            else if (resultado == 0) {
                resultado = mDB.insert(ComunioConstants.TABLE_GAMERS, null, values);
                if (resultado < 0)
                    throw new Exception("Error actualizando datos de participantes");
            }
        }

        //grabo los resultados de las jornadas
        JSONArray listResultadosJSON;
        listResultadosJSON = data.getJSONArray("score");
        Log.d(TAG, "score");
        for (int i = 0; i < listResultadosJSON.length(); i++) {
            Puntuacion puntuacion = mapper.readValue(listResultadosJSON.get(i).toString(), Puntuacion.class);
            ContentValues values = new ContentValues();
            values.put(DatabaseOpenHelper.JORNADA, puntuacion.getJornada());
            values.put(DatabaseOpenHelper.PUNTUACION_JORNADA, puntuacion.getPuntuacion_jornada());
            values.put(DatabaseOpenHelper.POSICION_JORNADA, puntuacion.getPosicion_jornada());
            values.put(DatabaseOpenHelper.PRIMA_JORNADA, puntuacion.getPrima_jornada());
            values.put(DatabaseOpenHelper.PUNTUACION_GENERAL, puntuacion.getPuntuacion_general());
            values.put(DatabaseOpenHelper.POSICION_GENERAL, puntuacion.getPosicion_general());
            values.put(DatabaseOpenHelper.PRIMA_GENERAL, puntuacion.getPrima_general());
            values.put(DatabaseOpenHelper.PUBLICADO, puntuacion.getPublicado());
            values.put(DatabaseOpenHelper.GOLES, puntuacion.getGoles());
            values.put(DatabaseOpenHelper.PORTERO, puntuacion.getPortero());
            values.put(DatabaseOpenHelper.REMO_JUGADORES, puntuacion.getRemo_jugadores());
            values.put(DatabaseOpenHelper.REMO_EQUIPO, puntuacion.getRemo_equipo());
            values.put(DatabaseOpenHelper.REMO_TRUPITA, puntuacion.getRemo_trupita());
            args[0] = puntuacion.getJornada().toString();

            resultado = mDB.update(puntuacion.getTabla(), values, DatabaseOpenHelper.JORNADA + "=?", args);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de: " + puntuacion.getTabla());
            else if (resultado == 0) {
                resultado = mDB.insert(puntuacion.getTabla(), null, values);
                if (resultado < 0)
                    throw new Exception("Error insertando datos de: " + puntuacion.getTabla());
            }
        }

        //grabo los fichajes
        JSONArray listFichajesJSON;
        listFichajesJSON = data.getJSONArray("signings");
        Log.d(TAG, "signings");
        for (int i = 0; i < listFichajesJSON.length(); i++) {
            Signing fichaje = mapper.readValue(listFichajesJSON.get(i).toString(), Signing.class);
            ContentValues values = new ContentValues();
            values.put(DatabaseOpenHelper.ID, fichaje.getId());
            values.put(DatabaseOpenHelper.NOMBRE, fichaje.getNombre());
            values.put(DatabaseOpenHelper.COMPRADOR, fichaje.getComprador());
            values.put(DatabaseOpenHelper.VENDEDOR, fichaje.getVendedor());
            values.put(DatabaseOpenHelper.PRECIO, fichaje.getPrecio());
            values.put(DatabaseOpenHelper.JORNADA, fichaje.getJornada());
            args[0] = fichaje.getId().toString();
            if (!isTableExists(ComunioConstants.TABLE_SIGNING)) {
                mDbHelper.createSigningTable();
            }
            resultado = mDB.update(ComunioConstants.TABLE_SIGNING, values, DatabaseOpenHelper.ID + "=?", args);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de fichajes");
            else if (resultado == 0) {
                resultado = mDB.insert(ComunioConstants.TABLE_SIGNING, null, values);
                if (resultado < 0)
                    throw new Exception("Error actualizando datos de fichajes");
            }
        }


        //grabo los equipos
        JSONArray listTeamsJSON;
        listTeamsJSON = data.getJSONArray("teams");
        Log.d(TAG, "teams");
        for (int i = 0; i < listTeamsJSON.length(); i++) {
            ContentValues values = new ContentValues();
            Teams team = mapper.readValue(listTeamsJSON.get(i).toString(), Teams.class);
            values.put(DatabaseOpenHelper.NOMBRE, team.getNombre());
            values.put(DatabaseOpenHelper.FOTO, team.getFoto());
            args[0] = team.getNombre();
            if (!isTableExists(ComunioConstants.TABLE_TEAMS)) {
                mDbHelper.createTeamsTable();
            }
            resultado = mDB.update(ComunioConstants.TABLE_TEAMS, values, DatabaseOpenHelper.NOMBRE + "=?", args);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de equipos");
            else if (resultado == 0) {
                resultado = mDB.insert(ComunioConstants.TABLE_TEAMS, null, values);
                if (resultado < 0)
                    throw new Exception("Error actualizando datos de equipos");
            }
        }

        //grabo los jugadores
        JSONArray listPlayersJSON;
        listPlayersJSON = data.getJSONArray("players");
        Log.d(TAG, "players");
        for (int i = 0; i < listPlayersJSON.length(); i++) {
            Player player = mapper.readValue(listPlayersJSON.get(i).toString(), Player.class);
            ContentValues values = new ContentValues();
            values.put(DatabaseOpenHelper.NOMBRE, player.getNombre());
            values.put(DatabaseOpenHelper.DEMARCACION, player.getDemarcacion());
            values.put(DatabaseOpenHelper.EQUIPO, player.getEquipo());
            values.put(DatabaseOpenHelper.PROPIETARIO, player.getPropietario());
            args[0] = player.getNombre();
            if (!isTableExists(ComunioConstants.TABLE_PLAYERS)) {
                mDbHelper.createPlayersTable();
            }
            resultado = mDB.update(ComunioConstants.TABLE_PLAYERS, values, DatabaseOpenHelper.NOMBRE + "=?", args);
            if (resultado < 0)
                throw new Exception("Error actualizando datos de jugadores");
            else if (resultado == 0) {
                resultado = mDB.insert(ComunioConstants.TABLE_PLAYERS, null, values);
                if (resultado < 0)
                    throw new Exception("Error actualizando datos de jugadores");
            }
        }



        Log.d(TAG, "resultado que devuelvo: " + resultado);
        return resultado;
    }

    boolean isTableExists(String tableName) {

        Cursor cursor = mDB.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public Integer getOption(String option){
        Integer valor = 0;
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            String column = DatabaseOpenHelper.OPTION + "=?";
            String[] args = new String[]{option};

            Cursor c = mDB.query(ComunioConstants.TABLE_CONF,
                    DatabaseOpenHelper.columnsConfiguration, column, args, null, null, null);

            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    valor = c.getInt(2);
                } while (c.moveToNext());
            }
            c.close();
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "getPlayerList");
            throw e;
        }
        return valor;
    }

    public List<Player> getPlayerList(String name, String _column) {
        List<Player> players = new ArrayList<>();
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            String order = DatabaseOpenHelper.ID + " ASC";
            String column = null;
            String[] args = null;
            if (_column != null) {
                column = _column + "=?";
                args = new String[]{name};
            }
            Cursor c = mDB.query(ComunioConstants.TABLE_PLAYERS,
                    DatabaseOpenHelper.columnsPlayers, column, args, null, null,
                    order);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    Player player = new Player();
                    player.setId(Integer.parseInt(c.getString(0)));
                    player.setNombre(c.getString(1));
                    player.setDemarcacion(c.getString(2));
                    player.setEquipo(c.getString(3));
                    player.setPropietario(c.getString(4));
                    players.add(player);
                } while (c.moveToNext());
            }
            c.close();
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "getPlayerList");
            throw e;
        }
        return players;
    }

    public String getShieldName(String teamName) {
        String sTeam = "";
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            String order = DatabaseOpenHelper.ID + " ASC";
            String[] args = new String[]{teamName};
            Cursor c = mDB.query(ComunioConstants.TABLE_TEAMS,
                    DatabaseOpenHelper.columnsTeams, DatabaseOpenHelper.NOMBRE + "=?", args, null, null,
                    order);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    sTeam = c.getString(2);
                } while (c.moveToNext());
            }
            c.close();
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "getShieldName");
            throw e;
        }
        return sTeam;
    }

    public Participante getGamerByName(String name) {
        Participante participante = new Participante();
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            String[] args = new String[]{name};
            Cursor c = mDB.query(ComunioConstants.TABLE_GAMERS,
                    DatabaseOpenHelper.columnsGamers, DatabaseOpenHelper.NOMBRE + "=?", args, null, null, null);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    participante.setId(c.getInt(0));
                    participante.setNombre(c.getString(1));
                    participante.setEmail(c.getString(2));
                    participante.setGcm_regid(c.getString(3));
                    participante.setTabla(c.getString(4));
                    participante.setJ_inicio(c.getDouble(5));
                    participante.setJ_final(c.getDouble(6));
                    participante.setPuntos_inicio(c.getInt(7));
                    participante.setPrima_inicial(c.getInt(8));
                } while (c.moveToNext());
            }
            c.close();
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "getGamerByName");
            throw e;
        }
        return participante;
    }

    public List<Participante> getAllGamers() {
        List<Participante> participantes = new ArrayList<>();
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            String order = DatabaseOpenHelper.NOMBRE + " ASC";
            Cursor c = mDB.query(ComunioConstants.TABLE_GAMERS,
                    DatabaseOpenHelper.columnsGamers, null, null, null, null, order);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    Participante participante = new Participante();
                    participante.setId(c.getInt(0));
                    participante.setNombre(c.getString(1));
                    participante.setEmail(c.getString(2));
                    participante.setGcm_regid(c.getString(3));
                    participante.setTabla(c.getString(4));
                    participante.setJ_inicio(c.getDouble(5));
                    participante.setJ_final(c.getDouble(6));
                    participante.setPuntos_inicio(c.getInt(7));
                    participante.setPrima_inicial(c.getInt(8));
                    participantes.add(participante);
                } while (c.moveToNext());
            }
            c.close();
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "getAllGamers");
            throw e;
        }
        return participantes;
    }

    public List<Signing> getSignings(String name, String _column) {
        List<Signing> fichajes = new ArrayList<>();
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            String order = DatabaseOpenHelper.JORNADA + " ASC";
            String column = _column + "=?";
            String[] args = new String[]{name};
            Cursor c = mDB.query(ComunioConstants.TABLE_SIGNING,
                    DatabaseOpenHelper.columnsSignings, column, args, null, null,
                    order);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    if (c.getInt(4) < 0)
                        continue;
                    Signing fichaje = new Signing();
                    fichaje.setId(c.getInt(0));
                    fichaje.setNombre(c.getString(1));
                    fichaje.setComprador(c.getString(2));
                    fichaje.setVendedor(c.getString(3));
                    fichaje.setPrecio(c.getInt(4));
                    fichaje.setJornada(c.getDouble(5));
                    fichajes.add(fichaje);
                } while (c.moveToNext());
            }
            c.close();
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "getSignings");
            throw e;
        }
        return fichajes;

    }

    public List<Puntuacion> getScores(Participante participante) {
        List<Puntuacion> puntuaciones = new ArrayList<>();
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            String order = DatabaseOpenHelper.JORNADA + " ASC";
            Cursor c = mDB.query(participante.getTabla(),
                    DatabaseOpenHelper.columnsScore, null, null, null, null,
                    order);
            Double jActual = ActivityTool.getValorJornadaActual(context);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    Puntuacion puntuacion = new Puntuacion();
                    //cargo primero la puntuacion general para saber si esa jornada ya está metida o no
                    puntuacion.setId(c.getInt(0));
                    puntuacion.setJornada(c.getDouble(1));
                    if(puntuacion.getJornada() <= participante.getJ_final()
                            && puntuacion.getJornada() >= participante.getJ_inicio()) {
                        if(puntuacion.getJornada() <= jActual) {
                            if (c.isNull(2))
                                puntuacion.setPuntuacion_jornada(null);
                            else
                                puntuacion.setPuntuacion_jornada(c.getInt(2));
                            puntuacion.setPuntuacion_general(c.getInt(5));
                            puntuacion.setPosicion_jornada(c.getInt(3));
                            puntuacion.setPrima_jornada(c.getInt(4) != 0);
                            puntuacion.setPosicion_general(c.getInt(6));
                            puntuacion.setPrima_general(c.getInt(7));
                            puntuacion.setPublicado(c.getInt(8) != 0);
                            puntuacion.setGoles(c.getInt(9));
                            puntuacion.setPortero(c.getInt(10) != 0);
                        }
                        puntuacion.setRemo_jugadores(c.getInt(11) != 0);
                        puntuacion.setRemo_equipo(c.getInt(12) != 0);
                        puntuacion.setRemo_trupita(c.getInt(13) != 0);
                        puntuaciones.add(puntuacion);
                    }

                } while (c.moveToNext());
            }
            c.close();
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "getScores");
            throw e;
        }
        return puntuaciones;

    }

    public long insertNotification(Bundle extras) {
        Log.d(TAG, "Insert notification in database: ");
        long resultado;
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                    context.getResources().getConfiguration().locale);
            String formattedDate = df.format(c.getTime());
            extras.putString("timestamp_notification", formattedDate);
            resultado = insertNotificationData(extras);
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "no almaceno la notificacion");
            throw e;
        }
        return resultado;
    }

    // Insert data in database
    private long insertNotificationData(Bundle extras) {
        long resultado;
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.TIMESTAMP_NOTIFICATION, extras.getString("timestamp_notification"));
        values.put(DatabaseOpenHelper.TIMESTAMP_SERVER, extras.getString("timestamp_server"));
        values.put(DatabaseOpenHelper.TITLE_MESSAGE, extras.getString("title_message"));
        values.put(DatabaseOpenHelper.BODY_MESSAGE, extras.getString("body_message"));
        values.put(DatabaseOpenHelper.READ, 0);
        if (!isTableExists(ComunioConstants.TABLE_NOTIFICATIONS)) {
            mDbHelper.createNotificationsTable();
        }
        resultado = mDB.insert(ComunioConstants.TABLE_NOTIFICATIONS, null, values);
        return resultado;
    }

    public List<NotificationItem> loadNotifications() {
        List<NotificationItem> notifications = new ArrayList<>();
        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            String order = DatabaseOpenHelper.TIMESTAMP_SERVER + " ASC";
            Cursor c = mDB.query(ComunioConstants.TABLE_NOTIFICATIONS,
                    DatabaseOpenHelper.columnsNotifications, null, null, null, null, order);
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    boolean read;
                    read = c.getInt(5) != 0;
                    NotificationItem item = new NotificationItem(c.getInt(0), c.getString(2),
                            c.getString(3), c.getString(4), read);
                    notifications.add(item);
                } while (c.moveToNext());
            }
            c.close();
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception" + e.getMessage() + "::" + e.getCause());
            return notifications;
        }
        //Log.d(TAG, "cargo notifications: " + notifications.size());
        return notifications;
    }

    public void setNotificationItemRead(int id) {

        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put(DatabaseOpenHelper.READ, 1);
            mDB.update(ComunioConstants.TABLE_NOTIFICATIONS, valores, DatabaseOpenHelper.ID + "=" + id, null);
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception" + e.getMessage() + "::" + e.getCause());
        }
    }

    public void deleteNotificationItemsDataBase() {

        try {
            mDbHelper = new DatabaseOpenHelper(context);
            mDB = mDbHelper.getWritableDatabase();
            int result = mDB.delete(ComunioConstants.TABLE_NOTIFICATIONS, null, null);
            Log.d(TAG, "Se han borrado " + result + "entradas de la base de datos");
            mDB.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception" + e.getMessage() + "::" + e.getCause());
        }


    }
}

