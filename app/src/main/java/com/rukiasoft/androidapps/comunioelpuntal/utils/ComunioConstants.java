package com.rukiasoft.androidapps.comunioelpuntal.utils;

public class ComunioConstants {
    public static final String DATABASE_NAME = "comunio";
    public static final String DEMARCACION = "demarcacion";
    public static final String EQUIPOS = "equipos";
    public static final String PARTICIPANTES = "participantes";
    public static final String PARTICIPANTES_MAS_COMPUTER = "participantes_mas_computer";
    public static final String JORNADA = "jornada";
    public static final String REMO_JORNADA = "remo_jornada";
    public static final String SANCIONES = "sanciones";
    public static final String[] PLAYING_POSITION = {"portero", "defensa", "medio", "delantero"};
    public static final String COMPUTER = "Computer";
    public static final String[] TIPOS_SANCIONES = {"Máximo número de Jugadores", "Máximo de jugadores por equipo", "Trúpitas"};

    public static final String TABLE_PLAYERS = "jugadores";
    public static final String TABLE_TEAMS = "equipos";
    public static final String TABLE_GAMERS = "participantes";
    public static final String TABLE_SIGNING = "fichajes";
    final public static String TABLE_NOTIFICATIONS = "Notifications";

    public static final Integer START_ROUND = 1;
    public static final Integer FINAL_ROUND = 19;
    public static final Integer BONUS_GOAL = 150000;
    public static final Integer BONUS_GOALKEEPER = 150000;
    public static final Integer BONUS_LAST_IN_ROUND = 500000;
    public static final Integer BONUS_LAST_IN_CLASSIFICATION = 500000;
    public static final Integer REMO_MAX_PLAYERS = 3000000;
    public static final Integer REMO_MAX_TEAMS = 3000000;
    public static final Integer REMO_TRUPITAS = 7000000;
    public static final Integer BONUS_POINTS = 10000;
    public static final Integer STARTING_MONEY = 20000000;
    public static final Integer MIN_MONEY_GREEN = 1000000;

    public static final Integer MAX_PLAYERS_TEAM = 16;
    public static final Integer MAX_PLAYERS_EACH_TEAM = 3;

    public static final Integer N_PLAYERS_BONUS_LAST_IN_CLASSIFICATION = 2;
    //se pueden cobrar 3 primas máximo en 5 jornadas
    public static final Integer N_BONUS_LAST_CLASSIFICATION = 3;
    public static final Integer N_ROUNDS_LAST_CLASSIFICATION = 5;
    public static final Integer CODIGO_NO_COBRA_PRIMA = 0;
    public static final Integer CODIGO_SI_COBRA_PRIMA = 1;
    public static final Integer CODIGO_NO_COBRA_PRIMA_POR_REITERACION = 2;
    public static final String COBRA_JORNADA = "Torpe de la jornada";
    public static final String COBRA_GENERAL = "Torpe de la general";
    public static final String COBRA_GENERAL_NO = "NO cobra - Torpe reiterado";
    public static final String COBRA_GOLES = "%num goles";
    public static final String COBRA_PORTERO = "Portería a cero";

    //dídigos de texto del tipo de usuario
    public static final String ALL_DATABASE = "all_database";
    public static final String ADMINISTRATOR_TEXT = "Administrador";
    public static final String MAINTENANCE_TEXT = "Mantenimiento";
    public static final String USER_TEXT = "Usuario";


    //Errores devueltos por el servidor
    public static final Integer ERROR_NO_ERROR = 0;
    public static final Integer ERROR_DATA_BASE = 1;    //Error de acceso a la base de datos
    public static final Integer ERROR_NO_SUBSCRIPTORS = 2;    //nadie está suscrito
    public static final Integer ERROR_NO_PUSH = 3;    //nadie está suscrito
    public static final Integer ERROR_SELECT = 4;    //error cargando datos en select
    public static final Integer ERROR_JORNADA = 5; //error no hay información de la jornada guardada


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
    public static final String PROPERTY_DOWNLOADING_DATABASE = "downloading_database";
    public static final String PROPERTY_VERSION_APP_DOWNLOADED = "version_app_downloaded";
    public static final String PROPERTY_VERSION_APP_FOR_DOWNLOADING = "version_app_for_downloading";

    public static final Integer DATABASE_REFRESH = 1000 * 3600 * 24;

    public static final String MENSAJE_CAPORAL = "caporal";
    public static final String MENSAJE_RULER = "ruler";

    public static final String DIRECTORY_APP = "/ComunioElPuntal/";
    public static final String NOMBRE_APP = "ComunioElPuntal.apk";


    public ComunioConstants() {

    }
}
