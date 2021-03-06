package com.github.randoapp;

import android.app.AlarmManager;

public class Constants {

    public static final String ALBUM_NAME = "Rando";
    public static final String CACHE_FOLDER = "com.github.randoapp";

    public static final String FILEPATH = "FILEPATH";
    public static final String RANDO_PAIR = "randoPair";
    public static final String RANDO_PAIRS_NUMBER = "RANDO_PAIRS_NUMBER";
    public static final String NOT_PAIRED_RANDO_PAIRS_NUMBER = "NOT_PAIRED_RANDO_PAIRS_NUMBER";

    public static final String IMAGE_FILTER = "image/*";
    public static final String IMAGE_PREFIX = "IMG_";
    public static final String IMAGE_POSTFIX = ".jpg";
    public static final int JPEG_QUALITY = 80;

    public static final int LOCATION_PERIOD = 20000;

    public static final String ERROR = "error";
    public static final int UNAUTHORIZED_CODE = 400;

    public static final int PAGE_SIZE = 1;

    public static final int BON_APPETIT_BUTTON_SIZE = 100;

    public static final int RANDO_MARGIN_PORTRAIT = 30;

    public static final int RANDO_PADDING_PORTRAIT_COLUMN_LEFT = 15;
    public static final int RANDO_PADDING_PORTRAIT_COLUMN_RIGHT = 5;
    public static final int RANDO_PADDING_PORTRAIT_COLUMN_TOP = 15;
    public static final int RANDO_PADDING_PORTRAIT_COLUMN_BOTTOM = 5;

    public static final int RANDO_PADDING_LANDSCAPE_COLUMN_LEFT = 10;
    public static final int RANDO_PADDING_LANDSCAPE_COLUMN_RIGHT = 5;
    public static final int RANDO_PADDING_LANDSCAPE_COLUMN_TOP = 15;
    public static final int RANDO_PADDING_LANDSCAPE_COLUMN_BOTTOM = 5;

    public static final int SIZE_SMALL = 480;
    public static final int SIZE_MEDIUM = 800;
    public static final int SIZE_LARGE = 1200;

    public static final String SERVER_HOST = "95.85.19.94";
    public static final String SERVER_URL = "http://" + SERVER_HOST;

    public static final int CONNECTION_TIMEOUT = 6000;

    public static final long SERVICE_SHORT_PAUSE = 30 * 1000;
    public static final long SERVICE_LONG_PAUSE = AlarmManager.INTERVAL_HALF_HOUR;


    //Shared Preferences
    public static final String PREFERENCES_FILE_NAME = "rando.prefs";
    public static final String AUTH_TOKEN = "auth.token";
    public static final String TRAINING_FRAGMENT_SHOWN = "training.fragment.shown";

    public static final String AUTH_TOKEN_PARAM = "token";
    public static final String SIGNUP_EMAIL_PARAM = "email";
    public static final String SIGNUP_PASSWORD_PARAM = "password";
    public static final String FACEBOOK_EMAIL_PARAM = "email";
    public static final String FACEBOOK_ID_PARAM = "id";
    public static final String FACEBOOK_TOKEN_PARAM = "token";
    public static final String GOOGLE_EMAIL_PARAM = "email";
    public static final String GOOGLE_TOKEN_PARAM = "token";
    public static final String GOOGLE_FAMILY_NAME_PARAM = "family_name";
    public static final String ANONYMOUS_ID_PARAM = "id";
    public static final String BON_APPETIT_PARAM = "bonAppetit";
    public static final String IMAGE_URL_PARAM = "imageURL";
    public static final String IMAGE_URL_SIZES_PARAM = "imageSizeURL";
    public static final String MAP_URL_SIZES_PARAM = "mapSizeURL";
    public static final String SMALL_PARAM = "small";
    public static final String MEDIUM_PARAM = "medium";
    public static final String LARGE_PARAM = "large";
    public static final String RANDO_ID_PARAM = "randoId";
    public static final String MAP_URL_PARAM = "mapURL";
    public static final String CREATION_PARAM = "creation";
    public static final String USER_PARAM = "user";
    public static final String STRANGER_PARAM = "stranger";
    public static final String RANDOS_PARAM = "randos";
    public static final String LATITUDE_PARAM = "latitude";
    public static final String LONGITUDE_PARAM = "longitude";

    public static final int CAMERA_MIN_SIZE = 1536;

    public static final String ERROR_MESSAGE_PARAM = "message";
    public static final String ERROR_CODE_PARAM = "code";

    public static final String IMAGE_PARAM = "image";
    public static final String LOG_PARAM = "log";

    public static final String IMAGE_MIME_TYPE = "image/jpeg";
    public static final String SIGNUP_URL = SERVER_URL + "/user";
    public static final String FACEBOOK_URL = SERVER_URL + "/facebook";
    public static final String GOOGLE_URL = SERVER_URL + "/google";
    public static final String ANONYMOUS_URL = SERVER_URL + "/anonymous";
    public static final String LOGOUT_URL = SERVER_URL + "/logout";
    public static final String FETCH_USER_URL = SERVER_URL + "/user";
    public static final String ULOAD_RANDO_URL = SERVER_URL + "/image";
    public static final String REPORT_URL = SERVER_URL + "/report/";
    public static final String BON_APPETIT_URL = SERVER_URL + "/bonappetit/";
    public static final String LOG_URL = SERVER_URL + "/log";

    public static final String REPORT_BROADCAST = "Report";
    public static final String SYNC_SERVICE_BROADCAST = "SyncService";

    public static final String NEED_NOTIFICATION = "Notification";

    public static final String GOOGLE_AUTH_SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    public static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    public static final String LOG_FILE_NAME = "rando.log";
    public static final int LOG_BUFFER_SIZE = 1024;
    public static final long LOG_SRVICE_INTERVAL = AlarmManager.INTERVAL_DAY;

    private static final int NUMBER_OF_IMAGES_IN_ONE_SET = 4; //stranger's + user's images and maps
    public static final int NUMBER_OF_IMAGES_FOR_CACHING = NUMBER_OF_IMAGES_IN_ONE_SET * 2; //2 visible image sets per screen

}