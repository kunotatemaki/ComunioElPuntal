package com.rukiasoft.androidapps.comunioelpuntal.utils;

public class ComunioConstants {
    public static final String DATABASE_NAME = "comunio";

    public static final String TABLE_PLAYERS = "jugadores";
    public static final String TABLE_TEAMS = "equipos";
    public static final String TABLE_GAMERS = "participantes";
    public static final String TABLE_SIGNING = "fichajes";
    final public static String TABLE_NOTIFICATIONS = "Notifications";
    public static final String TABLE_CONF = "conf";

    public static final Integer MIN_MONEY_GREEN = 1000000;

    //para los intents
    public  static final String DATABASE_DOWNLOADED_ACTION_INTENT = "com.rukiasoft.androidapps.comunioelpuntal.action.DATABASE";
    public static final String NOTIFICATION_ACTION_INTENT = "com.rukiasoft.androidapps.comunioelpuntal.action.NOTIFICATION";
    public static final String START_LOAD_ON_START_SCREEN = "com.rukiasoft.androidapps.comunioelpuntal.action.START_LOAD_ON_START_SCREEN";
    public static final String FINISH_LOAD_ON_START_SCREEN = "com.rukiasoft.androidapps.comunioelpuntal.action.FINISH_LOAD_ON_START_SCREEN";
    public static final String SET_TEXT_PROGRESS_ACTION_INTENT = "com.rukiasoft.androidapps.comunioelpuntal.action.SET_TEXT_PROGRESS_ACTION_INTENT";
    public static final String SET_PROGRESS_BAR_ACTION_INTENT = "com.rukiasoft.androidapps.comunioelpuntal.action.SET_PROGRESS_BAR_ACTION_INTENT";
    public static final String START_SELECT_PLAYER_ACTIVITY = "com.rukiasoft.androidapps.comunioelpuntal.action.START_SELECT_PLAYER_ACTIVITY";
    public static final String RESTART_INTEFACE_IN_MY_TEAM = "com.rukiasoft.androidapps.comunioelpuntal.action.RESTART_INTEFACE_IN_MY_TEAM";

    public static final String FORMAT_DATE_TIME = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_DATE_TIME_LOG = "yyyy-MM-dd__HH-mm-ss";
    public static final String PROPERTY_LAST_UPDATED = "option_last_updated";
    public static final String PROPERTY_MY_TEAM = "option_my_team";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    public static final String PROPERTY_EMAIL = "email";
    public static final String PROPERTY_DOWNLOAD_UPDATED_APP = "download_updated_app";
    public static final String PROPERTY_INSTALL_UPDATED_APP = "install_updated_app";
    public static final String PROPERTY_PATH_DOWNLOAD_APP = "path_download_app";
    //public static final String PROPERTY_DOWNLOADING_DATABASE = "downloading_database";
    public static final String PROPERTY_VERSION_APP_DOWNLOADED = "version_app_downloaded";
    public static final String PROPERTY_VERSION_APP_FOR_DOWNLOADING = "version_app_for_downloading";
    public static final String PROPERTY_JORNADAS = "jornadas";
    public static final String PROPERTY_START_ROUND = "ronda_inicial";
    public static final String PROPERTY_FINAL_ROUND = "ronda_final";
    public static final String PROPERTY_CURRENT_ROUND = "current_round";

    public static final Integer DATABASE_REFRESH = 1000 * 3600 * 12;

     public static final String DIRECTORY_APP = "/ComunioElPuntal/";
    public static final String NOMBRE_APP = "ComunioElPuntal.apk";


    public static final String[] PLAYING_POSITION = {"portero","defensa","medio","delantero"};
    public static final String COMPUTER = "Computer";
    public static final String[] TIPOS_SANCIONES = {"Máximo número de Jugadores","Máximo de jugadores por equipo","Trúpitas"};

    public static final String BONUS_GOAL = "prima_gol";
    public static final String BONUS_GOALKEEPER = "prima_portero";
    public static final String BONUS_LAST_IN_ROUND = "prima_torpe_jornada";
    public static final String BONUS_LAST_IN_CLASSIFICATION = "prima_torpe_general";
    public static final String REMO_MAX_PLAYERS = "remo_limite_jugadores";
    public static final String REMO_MAX_TEAMS = "remo_limite_jugadores_mismo_equipo";
    public static final String REMO_TRUPITAS = "remo_trupitas";
    public static final String BONUS_POINTS = "prima_punto";
    public static final String STARTING_MONEY = "dinero_inicial";
    public static final String MAX_PLAYERS_TEAM = "max_jugadores";
    public static final String MAX_PLAYERS_EACH_TEAM = "max_jugadores_mismo_equipo";
    //se pueden cobrar 3 primas máximo en 5 jornadas
    public static final Integer CODIGO_NO_COBRA_PRIMA = 0;
    public static final Integer CODIGO_SI_COBRA_PRIMA = 1;
    public static final Integer CODIGO_NO_COBRA_PRIMA_POR_REITERACION = 2;

    public static final Integer ERROR_NO_PUSH = 3;	//nadie está suscrito


    public static final String MENSAJE_CAPORAL = "caporal";
    public static final String MENSAJE_RULER = "ruler";

    public ComunioConstants() {

    }
}
