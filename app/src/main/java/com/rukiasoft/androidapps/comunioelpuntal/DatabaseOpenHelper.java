package com.rukiasoft.androidapps.comunioelpuntal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

public class DatabaseOpenHelper extends SQLiteOpenHelper implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    final private static String TAG = "DBScoresOpenHelper";

    final public static String ID = "id";
    final public static String JORNADA = "jornada";
    final public static String PUNTUACION_JORNADA = "puntuacion_jornada";
    final public static String POSICION_JORNADA = "posicion_jornada";
    final public static String PRIMA_JORNADA = "prima_jornada";
    final public static String PUNTUACION_GENERAL = "puntuacion_general";
    final public static String POSICION_GENERAL = "posicion_general";
    final public static String PRIMA_GENERAL = "prima_general";
    final public static String PUBLICADO = "publicado";
    final public static String GOLES = "goles";
    final public static String PORTERO = "portero";
    final public static String REMO_JUGADORES = "remo_jugadores";
    final public static String REMO_EQUIPO = "remo_equipo";
    final public static String REMO_TRUPITA = "remo_trupita";
    final public static String NOMBRE = "nombre";
    final public static String COMPRADOR = "comprador";
    final public static String VENDEDOR = "vendedor";
    final public static String PRECIO = "precio";
    final public static String DEMARCACION = "demarcacion";
    final public static String EQUIPO = "equipo";
    final public static String FOTO = "foto";
    final public static String PROPIETARIO = "propietario";
    final public static String EMAIL = "email";
    final public static String GCM_REGID = "gcm_regid";
    final public static String TABLA = "tabla";
    final public static String BODY_MESSAGE = "body_message";
    final public static String TITLE_MESSAGE = "title_message";
    final public static String READ = "read";
    final public static String TIMESTAMP_SERVER = "timestamp_server";
    final public static String TIMESTAMP_NOTIFICATION = "timestamp_device";
    final public static String OPTION = "option";
    final public static String VALUE = "value";
    final public static String JORNADA_INICIO = "j_inicio";
    final public static String JORNADA_FINAL = "j_final";
    final public static String PUNTOS_INICIO = "puntos_inicio";
    final public static String PRIMA_INICIAL = "prima_inicial";

    final public static String[] columnsScore = {ID, JORNADA, PUNTUACION_JORNADA, POSICION_JORNADA, PRIMA_JORNADA,
            PUNTUACION_GENERAL, POSICION_GENERAL, PRIMA_GENERAL, PUBLICADO, GOLES, PORTERO, REMO_JUGADORES,
            REMO_EQUIPO, REMO_TRUPITA};
    final public static String[] columnsSignings = {ID, NOMBRE, COMPRADOR, VENDEDOR, PRECIO, JORNADA};
    final public static String[] columnsGamers = {ID, NOMBRE, EMAIL, GCM_REGID, TABLA, JORNADA_INICIO,
    JORNADA_FINAL, PUNTOS_INICIO, PRIMA_INICIAL};
    final public static String[] columnsTeams = {ID, NOMBRE, FOTO};
    final public static String[] columnsPlayers = {ID, NOMBRE, DEMARCACION, EQUIPO, PROPIETARIO};
    final public static String[] columnsNotifications = {ID, TIMESTAMP_NOTIFICATION, TIMESTAMP_SERVER, TITLE_MESSAGE, BODY_MESSAGE, READ};
    final public static String[] columnsConfiguration = {ID, OPTION, VALUE};

    private final String CREATE_SIGNING_CMD = "CREATE TABLE IF NOT EXISTS " + ComunioConstants.TABLE_SIGNING + " (" + ID + " INTEGER, "
            + NOMBRE + " TEXT NOT NULL, "
            + COMPRADOR + " TEXT NOT NULL, "
            + VENDEDOR + " TEXT NOT NULL, "
            + PRECIO + " INTEGER, "
            + JORNADA + " REAL)";

    private final String CREATE_GAMERS_CMD = "CREATE TABLE IF NOT EXISTS " + ComunioConstants.TABLE_GAMERS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOMBRE + " TEXT NOT NULL, "
            + EMAIL + " TEXT, "
            + GCM_REGID + " TEXT, "
            + TABLA + " TEXT NOT NULL, "
            + JORNADA_INICIO + " REAL, "
            + JORNADA_FINAL + " REAL, "
            + PUNTOS_INICIO + " INTEGER, "
            + PRIMA_INICIAL + " INTEGER)";

    private final String CREATE_TEAMS_CMD = "CREATE TABLE IF NOT EXISTS " + ComunioConstants.TABLE_TEAMS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOMBRE + " TEXT NOT NULL, "
            + FOTO + " TEXT NOT NULL)";

    private final String CREATE_PLAYERS_CMD = "CREATE TABLE IF NOT EXISTS " + ComunioConstants.TABLE_PLAYERS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOMBRE + " TEXT NOT NULL, "
            + DEMARCACION + " TEXT NOT NULL, "
            + EQUIPO + " TEXT NOT NULL, "
            + PROPIETARIO + " TEXT NOT NULL)";

    final private static String CREATE_NOTIFICATIONS_CMD = "CREATE TABLE IF NOT EXISTS " + ComunioConstants.TABLE_NOTIFICATIONS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TIMESTAMP_NOTIFICATION + " TEXT NOT NULL, "
            + TIMESTAMP_SERVER + " TEXT NOT NULL, "
            + TITLE_MESSAGE + " TEXT NOT NULL, "
            + BODY_MESSAGE + " TEXT NOT NULL, "
            + READ + " INTEGER)";

    final private static String CREATE_CONFIGURATION_CMD = "CREATE TABLE IF NOT EXISTS " + ComunioConstants.TABLE_CONF + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + OPTION + " TEXT NOT NULL, "
            + VALUE + " INTEGER)";

    final private static Integer VERSION = 1;
    final private Context mContext;


    public DatabaseOpenHelper(Context context/*, List<Participante> participantes*/) {
        super(context, ComunioConstants.DATABASE_NAME, null, VERSION);
        this.mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");

        db.execSQL(CREATE_SIGNING_CMD);
        db.execSQL(CREATE_GAMERS_CMD);
        db.execSQL(CREATE_TEAMS_CMD);
        db.execSQL(CREATE_PLAYERS_CMD);
        db.execSQL(CREATE_NOTIFICATIONS_CMD);
        db.execSQL(CREATE_CONFIGURATION_CMD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {

        db.execSQL("PRAGMA writable_schema = 1;");
        db.execSQL("delete from sqlite_master where type in ('table', 'index', 'trigger');");
        db.execSQL("PRAGMA writable_schema = 0;");
        db.execSQL("VACUUM;");
        db.execSQL("PRAGMA INTEGRITY_CHECK;");

        db.execSQL("DROP TABLE IF EXISTS " + ComunioConstants.TABLE_SIGNING);
        db.execSQL(CREATE_SIGNING_CMD);
        db.execSQL("DROP TABLE IF EXISTS " + ComunioConstants.TABLE_GAMERS);
        db.execSQL(CREATE_GAMERS_CMD);
        db.execSQL("DROP TABLE IF EXISTS " + ComunioConstants.TABLE_TEAMS);
        db.execSQL(CREATE_TEAMS_CMD);
        db.execSQL("DROP TABLE IF EXISTS " + ComunioConstants.TABLE_PLAYERS);
        db.execSQL(CREATE_PLAYERS_CMD);
        db.execSQL("DROP TABLE IF EXISTS " + ComunioConstants.TABLE_NOTIFICATIONS);
        db.execSQL(CREATE_NOTIFICATIONS_CMD);
        db.execSQL("DROP TABLE IF EXISTS " + ComunioConstants.TABLE_CONF );
        db.execSQL(CREATE_CONFIGURATION_CMD);
    }

    public void deleteDatabase() {
        Log.d(TAG, "deleteDatabase");
        mContext.deleteDatabase(ComunioConstants.DATABASE_NAME);
    }

    public void createScoreTable(String tableName) {
        String CREATE_SCORE_CMD = "CREATE TABLE IF NOT EXISTS %table_name (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + JORNADA + " REAL, "
                + PUNTUACION_JORNADA + " INTEGER, "
                + POSICION_JORNADA + " INTEGER, "
                + PRIMA_JORNADA + " INTEGER, "
                + PUNTUACION_GENERAL + " INTEGER, "
                + POSICION_GENERAL + " INTEGER, "
                + PRIMA_GENERAL + " INTEGER, "
                + PUBLICADO + " BOOLEAN, "
                + GOLES + " INTEGER, "
                + PORTERO + " BOOLEAN, "
                + REMO_JUGADORES + " BOOLEAN, "
                + REMO_EQUIPO + " BOOLEAN, "
                + REMO_TRUPITA + " BOOLEAN)";
        String createCommand = CREATE_SCORE_CMD.replace("%table_name", tableName);
        this.getWritableDatabase().execSQL(createCommand);
    }

    public void createSigningTable() {
        this.getWritableDatabase().execSQL(CREATE_SIGNING_CMD);
    }

    public void createGamersTable() {
        this.getWritableDatabase().execSQL(CREATE_GAMERS_CMD);
    }

    public void createTeamsTable() {
        this.getWritableDatabase().execSQL(CREATE_TEAMS_CMD);
    }

    public void createPlayersTable() {
        this.getWritableDatabase().execSQL(CREATE_PLAYERS_CMD);
    }

    public void createNotificationsTable() {
        this.getWritableDatabase().execSQL(CREATE_NOTIFICATIONS_CMD);
    }

    public void createConfigurationTable() {
        this.getWritableDatabase().execSQL(CREATE_CONFIGURATION_CMD);
    }


}
