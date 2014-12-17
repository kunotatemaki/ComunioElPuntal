package com.rukiasoft.androidapps.comunioelpuntal.dataclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.rukiasoft.androidapps.comunioelpuntal.DatabaseOpenHelper;
import com.rukiasoft.androidapps.comunioelpuntal.NotificationItem;
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
        long resultado = 0;

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
            String[] args = new String[]{puntuacion.getJornada().toString()};
            if (!isTableExists(puntuacion.getTabla())) {
                mDbHelper.createScoreTable(puntuacion.getTabla());
            }
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
            String[] args = new String[]{fichaje.getId().toString()};
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

        //grabo los participantes
        JSONArray listGamersJSON;
        listGamersJSON = data.getJSONArray("participants");
        Log.d(TAG, "participants");
        for (int i = 0; i < listGamersJSON.length(); i++) {
            Participante participante = mapper.readValue(listGamersJSON.get(i).toString(), Participante.class);
            ContentValues values = new ContentValues();
            values.put(DatabaseOpenHelper.NOMBRE, participante.getNombre());
            values.put(DatabaseOpenHelper.LOGIN, participante.getLogin());
            values.put(DatabaseOpenHelper.EMAIL, participante.getEmail());
            values.put(DatabaseOpenHelper.GCM_REGID, participante.getGcm_regid());
            values.put(DatabaseOpenHelper.TABLA, participante.getTabla());
            String[] args = new String[]{participante.getNombre()};
            if (!isTableExists(ComunioConstants.TABLE_GAMERS)) {
                mDbHelper.createGamersTable();
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

        //grabo los equipos
        JSONArray listTeamsJSON;
        listTeamsJSON = data.getJSONArray("teams");
        Log.d(TAG, "teams");
        for (int i = 0; i < listTeamsJSON.length(); i++) {
            ContentValues values = new ContentValues();
            Teams team = mapper.readValue(listTeamsJSON.get(i).toString(), Teams.class);
            values.put(DatabaseOpenHelper.NOMBRE, team.getNombre());
            values.put(DatabaseOpenHelper.FOTO, team.getFoto());
            String[] args = new String[]{team.getNombre()};
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
            String[] args = new String[]{player.getNombre()};
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
                    participante.setLogin(c.getString(2));
                    participante.setEmail(c.getString(3));
                    participante.setGcm_regid(c.getString(4));
                    participante.setTabla(c.getString(5));
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
                    participante.setLogin(c.getString(2));
                    participante.setEmail(c.getString(3));
                    participante.setGcm_regid(c.getString(4));
                    participante.setTabla(c.getString(5));
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
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    Puntuacion puntuacion = new Puntuacion();
                    //cargo primero la puntuacion general para saber si esa jornada ya está metida o no
                    if (c.isNull(5))
                        continue;
                    else
                        puntuacion.setPuntuacion_general(c.getInt(5));
                    puntuacion.setId(c.getInt(0));
                    puntuacion.setJornada(c.getDouble(1));
                    if (c.isNull(2))
                        puntuacion.setPuntuacion_jornada(null);
                    else
                        puntuacion.setPuntuacion_jornada(c.getInt(2));
                    puntuacion.setPosicion_jornada(c.getInt(3));
                    puntuacion.setPrima_jornada(c.getInt(4) != 0);
                    puntuacion.setPosicion_general(c.getInt(6));
                    puntuacion.setPrima_general(c.getInt(7));
                    puntuacion.setPublicado(c.getInt(8) != 0);
                    puntuacion.setGoles(c.getInt(9));
                    puntuacion.setPortero(c.getInt(10) != 0);
                    puntuacion.setRemo_jugadores(c.getInt(11) != 0);
                    puntuacion.setRemo_equipo(c.getInt(12) != 0);
                    puntuacion.setRemo_trupita(c.getInt(13) != 0);
                    puntuaciones.add(puntuacion);

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

    public long insertNotification(Bundle extras) throws Exception {
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

