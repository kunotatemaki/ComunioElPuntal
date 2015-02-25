package com.rukiasoft.androidapps.comunioelpuntal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import com.rukiasoft.androidapps.comunioelpuntal.ClassificationFragment.ClassificationFragmentSelectionListener;
import com.rukiasoft.androidapps.comunioelpuntal.CommunityFragment.CommunityFragmentSelectionListener;
import com.rukiasoft.androidapps.comunioelpuntal.GamerFragment.GamerFragmentSelectionListener;
import com.rukiasoft.androidapps.comunioelpuntal.comunication.gcm.ServerClient;
import com.rukiasoft.androidapps.comunioelpuntal.crashlogs.ExceptionHandler;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.DatabaseHandler;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Participante;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Puntuacion;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

public class MainActivity extends ActionBarActivity implements GamerFragmentSelectionListener,
        CommunityFragmentSelectionListener, ClassificationFragmentSelectionListener {

    private static final String TAG = "MainActivity";
    private static final int RESULT_START_ACTIVITY = 246;
    private static final int RESULT_SELECT_ACTIVITY = 247;
    private static final int RESULT_INSTALL_ACTIVITY = 248;
    private static final int RESULT_SETTINGS_ACTIVITY = 249;
    public static final int RESULT_LOAD_SELECT = 250;
    public static final int RESULT_LOAD_START_SCREEN = 251;

    private static PlayersFragment playersFragment = null;
    private static GamerFragment gamerFragment = null;
    private static TeamPlayersFragment teamPlayersFragment = null;
    private static CommunityFragment communityFragment = null;
    private static RemosFragment remosFragment = null;
    private static BonusFragment bonusFragment = null;
    private static ScoreFragment scoreFragment = null;
    private static NotificationFragment notificationFragment = null;
    private static BalanceFragment balanceFragment = null;
    private static ClassificationTabsFragment classificationTabsFragment = null;
    private static DatabaseHandler dbHandler = null;
    private static List<GamerInformation> gamers = new ArrayList<>();
    private static List<PlayerItem> players = new ArrayList<>();

    private static Boolean databaseLoaded = false;
    private static Boolean databaseDownloading = false;
    private static Boolean created = false;
    private static ServerClient serverClient;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mMainFragment = null;
    private Fragment mSecondaryFragment = null;
    public final static ArrayList<String> options = new ArrayList<>();
    private static Context context;
    private static DrawerLayout mDrawerLayout;
    private static ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationDrawerAdapter navigationDrawerAdapter;
    private Boolean firstStart = true;
    private static String myNameGamer = "";
    private Integer arrowCounter = 0;
    private final ArrayList<String> titulos = new ArrayList<>();

    private static SmoothProgressBar mProgressBar;
    private static JSONObject jornadasJSON = null;



    public enum Orientation {
        LANDSCAPE(0),
        PORTRAIT(1);
        private int mode;

        Orientation(int _mode) {
            if (_mode == 0) {
                //Log.d("ORIENTATION", "Constructor landscape");
                this.mode = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            } else if (_mode == 1) {
                //Log.d("ORIENTATION", "Constructor portrait");
                this.mode = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
        }

        public int getMode() {
            //Log.d("ORIENTATION", "devuelvo:" + String.valueOf(this.mode));
            return this.mode;
        }

        public void setMode(int _mode) {
            //Log.d("ORIENTATION", "pongo mode a:" + String.valueOf(_mode));
            this.mode = _mode;
        }
    }

    protected class MainActivityListener extends BroadcastReceiver {

        Activity activity;

        MainActivityListener(Activity _activity){
            this.activity = _activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ComunioConstants.START_SELECT_PLAYER_ACTIVITY))
            {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent secondIntent = new Intent(activity, SelectGamerActivity.class);
                        secondIntent.putExtra("type", StartScreenActivity.class.getSimpleName());
                        startActivity(secondIntent);
                    }
                });

            }else if (intent.getAction().equals(ComunioConstants.RESTART_INTEFACE_IN_MY_TEAM)) {
                ((MainActivity) activity).loadItemsNavigationDrawer();
                getCommunityFragment().restartFragment();
                getClassificationTabsFragment().restartFragment();
            }
        }
    }

    MainActivityListener mListener;

    //private static Orientation defaultOrientation = Orientation.PORTRAIT;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        context = this;
        mListener = new MainActivityListener(this);
        registerReceiver(mListener, new IntentFilter(ComunioConstants.START_SELECT_PLAYER_ACTIVITY));
        registerReceiver(mListener, new IntentFilter(ComunioConstants.RESTART_INTEFACE_IN_MY_TEAM));

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("gamers")) {
                gamers = (List<GamerInformation>) savedInstanceState.getSerializable("gamers");
            }
            if (savedInstanceState.containsKey("databaseLoaded")) {
                databaseLoaded = savedInstanceState.getBoolean("databaseLoaded");
            }
            if (savedInstanceState.containsKey("players")) {
                players = (List<PlayerItem>) savedInstanceState.getSerializable("players");
            }
        }
        super.onCreate(savedInstanceState);

        if (0 == gamers.size() || 0 == players.size())
            databaseLoaded = false;

        createFragments();

        setContentView(R.layout.main_activity);
        if (!ActivityTool.isForTablet(this))    //una sola pantalla
            ActivityTool.setOrientation(this, Orientation.PORTRAIT);
        else
            ActivityTool.setOrientation(this, Orientation.LANDSCAPE);


        //establezco las preferencias la primera vez
        if (ActivityTool.hasVibrator(getApplicationContext()))
            PreferenceManager.setDefaultValues(this, R.xml.options, false);
        else
            PreferenceManager.setDefaultValues(this, R.xml.options_not_vibrate, false);

        //Creo el cliente para conectarse con Google y con el servidor
        if (serverClient == null) {
            try {
                AssetManager am = getAssets();
                serverClient = new ServerClient(this, am.open("application.xml"));
                //am.close();
            } catch (IOException e) {
                Log.i(TAG, "Excepción al crear cliente: IOException");
            } catch (SAXException e) {
                Log.i(TAG, "Excepción al crear cliente: SAXException");
            } catch (ParserConfigurationException e) {
                Log.i(TAG, "Excepción al crear cliente: ParserConfigurationException");
            } catch (Exception e) {
                Log.i(TAG, "Excepción no controlada al crear cliente");
            }
        }

        //registro el código, por si no lo he hecho antes.
        serverClient.conectar(ServerClient.AccessMode.ACTIVITY);

        ExceptionHandler.register(this);


        if (ActivityTool.getAppVersion(this) == ActivityTool.getIntegerFromPreferences(this, ComunioConstants.PROPERTY_VERSION_APP_DOWNLOADED)) {
            Log.d(TAG, "ya puedo borrar la versión descargada");
            File file = new File(Environment.getExternalStorageDirectory() + ComunioConstants.DIRECTORY_APP + ComunioConstants.NOMBRE_APP);
            boolean deleted = file.delete();
            Log.d(TAG, "borrada versión descargada: " + deleted);
            ActivityTool.savePreferences(this, ComunioConstants.PROPERTY_INSTALL_UPDATED_APP, false);

        }
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {

            setSupportActionBar(mToolbar);
            showTitle(getSupportActionBar().getTitle().toString());
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        navigationDrawerAdapter = new NavigationDrawerAdapter(getApplicationContext());
        mDrawerList.setAdapter(navigationDrawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_closed) {
            public void onDrawerClosed(View view) {
                //getSupportActionBar().setTitle(mTitle);
                showCurrentTitle();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                showTitle(getString(R.string.choose_option));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()                
            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.setToolbarNavigationClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        mProgressBar = (SmoothProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.progressiveStop();
        ActivityTool.HideProgress(mProgressBar, this);

        if (ActivityTool.getBooleanFromPreferences(this, ComunioConstants.PROPERTY_INSTALL_UPDATED_APP)) {
            Log.d(TAG, "actualizar la app");
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + ComunioConstants.DIRECTORY_APP + ComunioConstants.NOMBRE_APP)), "application/vnd.android.package-archive");
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, RESULT_INSTALL_ACTIVITY);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else if (!databaseLoaded) {
            Log.i(TAG, "leo database");
            Intent intent = new Intent(MainActivity.this, StartScreenActivity.class);
            startActivityForResult(intent, RESULT_START_ACTIVITY);
        } else {
            loadItemsNavigationDrawer();
        }

    }


    void loadItemsNavigationDrawer() {

        navigationDrawerAdapter.clear();
        myNameGamer = ActivityTool.getStringFromPreferences(getApplicationContext(),
                ComunioConstants.PROPERTY_MY_TEAM);

        navigationDrawerAdapter.add(new MainItem(getResources().getString(R.string.my_team),
                R.drawable.my_team,
                new Intent()
                        .putExtra("Name", "MyTeam")
                        .putExtra("Text", R.string.my_team)
                        .putExtra("Gamer", myNameGamer)
        ));
        navigationDrawerAdapter.add(new MainItem(getResources().getString(R.string.classification),
                R.drawable.podium,
                new Intent()
                        .putExtra("Name", "Classification")
                        .putExtra("Text", R.string.classification)
        ));
        navigationDrawerAdapter.add(new MainItem(getResources().getString(R.string.community),
                R.drawable.community,
                new Intent()
                        .putExtra("Name", "Community")
                        .putExtra("Text", R.string.community)
        ));
        navigationDrawerAdapter.add(new MainItem(getResources().getString(R.string.players),
                R.drawable.players,
                new Intent()
                        .putExtra("Name", "Players")
                        .putExtra("Text", R.string.players)
        ));
        navigationDrawerAdapter.add(new MainItem(getResources().getString(R.string.notifications),
                R.drawable.speaker_man,
                new Intent()
                        .putExtra("Name", "Notifications")
                        .putExtra("Text", R.string.notifications)
        ));
        Log.d(TAG, "pulso cero programmatically");
        mDrawerList.performItemClick(null, 0, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    @Override
    public void onResume() {
        String clase = "";
        if(mMainFragment != null) {
            clase = mMainFragment.getClass().toString();
        }
        Log.d(TAG, "onResume: " + clase);
        if(isNeededToCreateFragments()) {
            Log.d(TAG, "re-creando fragments");
            createFragments();
        }
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intentData) {
        Log.i(TAG, "vuelvo de for result: " + requestCode + " : " + resultCode);

        if (requestCode == RESULT_START_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if (!isGamerSelected()) {
                    Log.d(TAG, "no hay nombre, lanzo la pantalla de selección");
                    Intent intent = new Intent(this, SelectGamerActivity.class);
                    startActivityForResult(intent, RESULT_SELECT_ACTIVITY);
                } else {
                    new Handler().post(new Runnable() {
                        public void run() {
                            //loadFragmentsOnCreate();
                            loadItemsNavigationDrawer();
                        }
                    });
                }
            } else if (resultCode == RESULT_LOAD_SELECT){
               Intent intent = new Intent(this, SelectGamerActivity.class);
               intent.putExtra("mode", RESULT_LOAD_START_SCREEN);
               startActivityForResult(intent, RESULT_SELECT_ACTIVITY);
            }else
                Log.d(TAG, "vuelvo sin haber puesto ok");
        } else if (requestCode == RESULT_SELECT_ACTIVITY) {
            if(resultCode == RESULT_OK)
                if (!isCreated())
                    new Handler().post(new Runnable() {
                        public void run() {
                            //loadFragmentsOnCreate();
                            loadItemsNavigationDrawer();
                        }
                    });

        } else if (requestCode == RESULT_SETTINGS_ACTIVITY) {
            if (!isCreated()) {
                if (!myNameGamer.equals(ActivityTool.getStringFromPreferences(getApplicationContext(),
                        ComunioConstants.PROPERTY_MY_TEAM))) {
                    Log.d(TAG, "como ha cambiado, cargo");
                    getGamerFragment(ActivityTool.getStringFromPreferences(getApplicationContext(),
                            ComunioConstants.PROPERTY_MY_TEAM)).updateAllFragment();

                    new Handler().post(new Runnable() {
                        public void run() {
                            loadItemsNavigationDrawer();
                        }
                    });
                }
            }

        } else if (requestCode == RESULT_INSTALL_ACTIVITY) {
            Log.d(TAG, "vuelvo de instalar");
            if (resultCode == RESULT_OK)
                Log.d(TAG, "ha llegado ok");
            else if (resultCode == RESULT_CANCELED)
                Log.d(TAG, "ha llegado ko");
            else
                Log.d(TAG, "ni puta idea de lo que llegó");
            Intent intent = new Intent(MainActivity.this, StartScreenActivity.class);
            startActivityForResult(intent, RESULT_START_ACTIVITY);
        }

    }


    @Override
    public void onBackPressed() {
        arrowCounter--;
        if (arrowCounter <= 0) {
            arrowCounter = 0;
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        }

        super.onBackPressed();
        showPreviousTitle();
//int i = 1/0;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "grabo");
        if (gamers.size() > 0)
            savedInstanceState.putSerializable("gamers", (Serializable) getGamers());
        savedInstanceState.putBoolean("databaseLoaded", databaseLoaded);
        if (players.size() > 0)
            savedInstanceState.putSerializable("players", (Serializable) players);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        created = false;
        unregisterReceiver(mListener);
    }

    public static boolean isCreated() {
        return created;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.menu_connect).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle item selection

        Intent finalIntent;
        switch (item.getItemId()) {

            case R.id.menu_connect:
                //String fecha = ActivityTool.getStringFromPreferences(this, ComunioConstants.PROPERTY_LAST_UPDATED);
                String fecha = ActivityTool.getStringFromPreferences(this, ComunioConstants.PROPERTY_LAST_UPDATED);
                if (fecha.equals(""))
                    fecha = "null";
                Log.d(TAG, "la fecha es: " + fecha);
                serverClient.connectToDownloadDatabase(fecha, false);
                //serverClient.conectar(ServerClient.AccessMode.ACTIVITY);

                return true;
            case R.id.menu_settings:
                finalIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(finalIntent, RESULT_SETTINGS_ACTIVITY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static ServerClient GetServerClient() {
        return serverClient;
    }

    void onNavigationDrawerItemSelected(MainItem item) {

        Log.i(TAG, "Entered onNavigationDrawerItemSelected");
        Bundle extras = item.getIntent().getExtras();

        // If there is no FeedFragment instance, then create one
        String name;
        name = extras.getString("Name");
        mSecondaryFragment = null;
        mFragmentManager = getSupportFragmentManager();
        if (name.compareTo("MyTeam") == 0) {
            String myName = extras.getString("Gamer");
            if (myName.compareTo("") == 0) {
                ActivityTool.showToast(this, getResources().getString(R.string.no_gamer_selected));
                return;
            }
            if (getGamers().size() == 0) {
                ActivityTool.showToast(this, getResources().getString(R.string.no_gamers_loaded));
                return;
            }
            mMainFragment = getGamerFragment(myName);
            ((GamerFragment) mMainFragment).updateAllFragment();


            if (ActivityTool.isForTablet(this)) {
                teamPlayersFragment.setPlayerList(
                        ((GamerFragment) mMainFragment).getPlayerItemsForTablets());
                teamPlayersFragment.setCabeceraView("");
                mSecondaryFragment = teamPlayersFragment;
            }
        } else if (name.compareTo("Players") == 0) {
            mMainFragment = playersFragment;
            if (!((PlayersFragment) mMainFragment).hasInformation()) {
                ActivityTool.showToast(this, getResources().getString(R.string.no_players_loaded));
                return;
            }
        } else if (name.compareTo("Community") == 0) {
            mMainFragment = communityFragment;
            if (getGamers().size() == 0) {
                ActivityTool.showToast(this, getResources().getString(R.string.no_gamers_loaded));
                return;
            }
        } else if (name.compareTo("Notifications") == 0) {
            mMainFragment = notificationFragment;
        } else if (name.compareTo("Classification") == 0) {
            mMainFragment = classificationTabsFragment;
        } else {
            ActivityTool.showToast(this, getResources().getString(R.string.coming_soon));
            return;
        }
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (!ActivityTool.isForTablet(this)) {
            if (!mMainFragment.isAdded()) {
                mFragmentTransaction.replace(R.id.fragment_container, mMainFragment);
                if (firstStart)
                    firstStart = false;
                else
                    mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                mFragmentTransaction.commit();
            }
        } else {
            if (!mMainFragment.isAdded()) {
                mFragmentTransaction.replace(R.id.primary_fragment_container, mMainFragment);
            }
            if (null != mSecondaryFragment) {
                showSecondaryFragment();
                if (!mSecondaryFragment.isAdded()) {
                    mFragmentTransaction.replace(R.id.secondary_fragment_container, mSecondaryFragment);
                }
            } else {
                hideSecondaryFragment();
            }
            mFragmentTransaction.commit();
        }
        mFragmentManager.executePendingTransactions();
        if (mMainFragment instanceof GamerFragment)
            ((GamerFragment) mMainFragment).resetMarcador();

        updateAndShowTitleArray(getResources().getString(extras.getInt("Text")));
        mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        arrowCounter = 0;
    }

    @Override
    public void onGamerOptionSelected(MainItem item) {
        Log.i(TAG, "Entered onGamerOptionSelected");
        Bundle extras = item.getIntent().getExtras();
        // If there is no FeedFragment instance, then create one
        String name;
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        name = extras.getString("Name");
        if (name.compareTo("MyPlayers") == 0) {
            @SuppressWarnings("unchecked")
            List<PlayerItem> player = (List<PlayerItem>) extras.getSerializable("Players");
            teamPlayersFragment.setPlayerList(player);
            teamPlayersFragment.setCabeceraView("");
            mMainFragment = teamPlayersFragment;
        } else if (name.compareTo("Signings") == 0) {
            @SuppressWarnings("unchecked")
            List<PlayerItem> player = (List<PlayerItem>) extras.getSerializable("Players");
            Integer money = extras.getInt("Money");
            teamPlayersFragment.setPlayerList(player);
            teamPlayersFragment.setCabeceraView(ActivityTool.getFormatedCurrencyNumber(money) + " €");
            mMainFragment = teamPlayersFragment;
        } else if (name.compareTo("Sales") == 0) {
            @SuppressWarnings("unchecked")
            List<PlayerItem> player = (List<PlayerItem>) extras.getSerializable("Players");
            Integer money = extras.getInt("Money");
            Log.d(TAG, "Voy a poner en ventas: " + ActivityTool.getFormatedCurrencyNumber(money) + " €");
            teamPlayersFragment.setPlayerList(player);
            teamPlayersFragment.setCabeceraView(ActivityTool.getFormatedCurrencyNumber(money) + " €");
            mMainFragment = teamPlayersFragment;
        } else if (name.compareTo("Remos") == 0) {
            @SuppressWarnings("unchecked")
            List<Puntuacion> puntuaciones = (List<Puntuacion>) extras.getSerializable("Puntuaciones");
            remosFragment.setPuntuaciones(puntuaciones);
            mMainFragment = remosFragment;
        } else if (name.compareTo("Bonus") == 0) {
            @SuppressWarnings("unchecked")
            List<Puntuacion> puntuaciones = (List<Puntuacion>) extras.getSerializable("Puntuaciones");
            bonusFragment.setPuntuaciones(puntuaciones);
            mMainFragment = bonusFragment;
        } else if (name.compareTo("Scores") == 0) {
            @SuppressWarnings("unchecked")
            List<Puntuacion> puntuaciones = (List<Puntuacion>) extras.getSerializable("Puntuaciones");
            scoreFragment.setPuntuaciones(puntuaciones);
            mMainFragment = scoreFragment;
        } else if (name.compareTo("Balance") == 0) {
            GamerInformation gamer = (GamerInformation) extras.getSerializable("Gamer");
            balanceFragment.setGamer(gamer);
            mMainFragment = balanceFragment;
        } else
            ActivityTool.showToast(this, getResources().getString(R.string.coming_soon));

        if (!ActivityTool.isForTablet(this)) {
            mFragmentTransaction.replace(R.id.fragment_container, mMainFragment);
            mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mFragmentTransaction.addToBackStack(null).commit();
            mFragmentManager.executePendingTransactions();
        } else {
            if (!mMainFragment.isAdded())
                mFragmentTransaction.replace(R.id.secondary_fragment_container, mMainFragment).commit();
        }
        mFragmentManager.executePendingTransactions();
        //getSupportActionBar().setTitle(getResources().getString(extras.getInt("Text")));
        if (!ActivityTool.isForTablet(this)) {
            updateAndShowTitleArray(getResources().getString(extras.getInt("Text")));
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            arrowCounter++;
        }
        //getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onCommunityFragmentItemSelected(GamerInformation item) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mMainFragment = getGamerFragment(item.getParticipante().getNombre());
        if (!ActivityTool.isForTablet(this)) {
            mFragmentTransaction.replace(R.id.fragment_container, mMainFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        } else {
            showSecondaryFragment();
            teamPlayersFragment.setPlayerList(
                    ((GamerFragment) mMainFragment).getPlayerItemsForTablets());
            teamPlayersFragment.setCabeceraView("");
            mSecondaryFragment = teamPlayersFragment;
            mFragmentTransaction.replace(R.id.primary_fragment_container, mMainFragment);
            if (!mSecondaryFragment.isAdded())
                mFragmentTransaction.replace(R.id.secondary_fragment_container, mSecondaryFragment);
            mFragmentTransaction.addToBackStack(null)
                    .commit();
        }
        mFragmentManager.executePendingTransactions();
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        arrowCounter++;
        updateAndShowTitleArray(getResources().getString(R.string.gamer));
    }

    @Override
    public void onClassificationFragmentItemSelected(GamerInformation item) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mMainFragment = getGamerFragment(item.getParticipante().getNombre());
        if (!ActivityTool.isForTablet(this)) {
            mFragmentTransaction.replace(R.id.fragment_container, mMainFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        } else {
            showSecondaryFragment();
            teamPlayersFragment.setPlayerList(
                    ((GamerFragment) mMainFragment).getPlayerItemsForTablets());
            teamPlayersFragment.setCabeceraView("");
            mSecondaryFragment = teamPlayersFragment;
            mFragmentTransaction.replace(R.id.primary_fragment_container, mMainFragment);
            if (!mSecondaryFragment.isAdded())
                mFragmentTransaction.replace(R.id.secondary_fragment_container, mSecondaryFragment);
            mFragmentTransaction.addToBackStack(null)
                    .commit();
        }
        mFragmentManager.executePendingTransactions();
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        arrowCounter++;
        updateAndShowTitleArray(getResources().getString(R.string.gamer));
    }

    public static Context getContext() {
        return context;
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onNavigationDrawerItemSelected(navigationDrawerAdapter.getItem(position));
        }
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public static Boolean ismDrawerOpen() {
        if(mDrawerList == null || mDrawerLayout == null)
            return false;

        return mDrawerLayout.isDrawerOpen(mDrawerList);
    }
    
	/*private void updateTitleArray(String title){
        titulos.add(title);
	}*/

    private void updateAndShowTitleArray(String title) {
        titulos.add(title);
        showCurrentTitle();
    }

    private void showCurrentTitle() {
        String title = "";
        if (titulos.size() > 0)
            title = titulos.get(titulos.size() - 1);
        showTitle(title);

    }

    private void showTitle(String title) {
        getSupportActionBar().setTitle(Html.fromHtml("<b>" + title + "</b>"));
    }

    private void showPreviousTitle() {
        if (titulos.size() > 0)
            titulos.remove(titulos.size() - 1);
        showCurrentTitle();
    }

    private static void createFragments() {
        playersFragment = new PlayersFragment();
        playersFragment.createAdapter();
        if (players.size() > 0)
            playersFragment.setPlayerItems(players);

        gamerFragment = new GamerFragment();

        teamPlayersFragment = new TeamPlayersFragment();

        communityFragment = new CommunityFragment();

        remosFragment = new RemosFragment();

        bonusFragment = new BonusFragment();

        scoreFragment = new ScoreFragment();

        notificationFragment = new NotificationFragment();

        balanceFragment = new BalanceFragment();

        classificationTabsFragment = new ClassificationTabsFragment();

        dbHandler = new DatabaseHandler(MainActivity.getContext());

    }

    private static Boolean isNeededToCreateFragments() {
        Boolean ret = false;
        if(playersFragment == null || gamerFragment == null || teamPlayersFragment == null ||
                communityFragment == null || remosFragment == null ||
                bonusFragment == null || scoreFragment == null ||
                notificationFragment == null || balanceFragment == null ||
                classificationTabsFragment == null || dbHandler == null ||
                playersFragment == null || playersFragment == null )
            ret = true;
        return ret;
    }


    public static List<Participante> loadGamerNamesFromDatabase() {

        return dbHandler.getAllGamers();
    }

    private static void setTextProgress(final String text, final TextView textView) {
        if (textView instanceof TextView) {
            textView.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(text);
                }
            });
        }
    }

    private static void setProgress(final ProgressBar progressBar, final int porcentaje) {
        if (progressBar instanceof ProgressBar) {
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(porcentaje);
                }
            });
        }
    }

    GamerFragment getGamerFragment(String nombre) {
        //Log.d(TAG, "busco el fragment");
        if (gamerFragment.getGamer() != null)
            if (gamerFragment.getGamer().getParticipante().getNombre().compareTo(nombre) == 0) {
                return gamerFragment;
            }
        for (int i = 0; i < gamers.size(); i++) {
            if (gamers.get(i).getParticipante().getNombre().compareTo(nombre) == 0) {
                Log.d(TAG, "cargo el fragment");
                gamerFragment.setGamer(gamers.get(i));
                Log.d(TAG, "hecho");
                break;
            }
        }
        return gamerFragment;
    }

    public static List<GamerInformation> getGamers() {
        return gamers;
    }

    public static DatabaseHandler getdbHandler() {
        return dbHandler;
    }

    public static SmoothProgressBar getProgressBar() {
        return mProgressBar;
    }

    private void hideSecondaryFragment() {
        FrameLayout layout = (FrameLayout) findViewById(R.id.secondary_fragment_container);
        LayoutParams params = layout.getLayoutParams();
        params.width = 0;
        layout.setLayoutParams(params);
    }

    private void showSecondaryFragment() {
        FrameLayout layout = (FrameLayout) findViewById(R.id.secondary_fragment_container);
        LayoutParams params = layout.getLayoutParams();
        params.width = LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(params);
    }

    public static Boolean isGamerSelected(){
        myNameGamer = ActivityTool.getStringFromPreferences(context, ComunioConstants.PROPERTY_MY_TEAM);
        return myNameGamer.compareTo("") != 0;
    }

    public static JSONObject getJornadasJSON() {
        if(jornadasJSON == null){
            jornadasJSON = ActivityTool.getJornadasJSON(MainActivity.getContext());
        }
        return jornadasJSON;
    }

    public static void resetJornadasJSON(){
        MainActivity.jornadasJSON = null;
    }

    public static GamerFragment getGamerFragment() {
        if(gamerFragment == null)
            MainActivity.createFragments();
        return gamerFragment;
    }

    public static CommunityFragment getCommunityFragment() {
        if(communityFragment == null)
            MainActivity.createFragments();
        return communityFragment;
    }

    public static ClassificationTabsFragment getClassificationTabsFragment() {
        if(classificationTabsFragment == null)
            MainActivity.createFragments();
        return classificationTabsFragment;
    }

    public static PlayersFragment getPlayersFragment() {
        if(playersFragment == null)
            MainActivity.createFragments();
        return playersFragment;
    }

    public static List<PlayerItem> getPlayers() {
        return players;
    }

    public static void setPlayers(List<PlayerItem> players) {
        MainActivity.players = players;
    }

    public static void setDatabaseLoaded(Boolean state) {
        MainActivity.databaseLoaded = state;
    }

    public static Boolean getDatabaseDownloading() {
        return databaseDownloading;
    }

    public static void setDatabaseDownloading(Boolean state) {
        MainActivity.databaseDownloading = state;
    }


}
