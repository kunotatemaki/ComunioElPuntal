package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ComunioConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruler on 2014.
 */
public class SelectGamerActivity extends Activity {
    private final static String TAG = "SelectGamerActivity";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //ExceptionHandler.register(this); 
        setContentView(R.layout.select_gamer);
        if (!ActivityTool.isForTablet(this))    //una sola pantalla
            ActivityTool.setOrientation(this, MainActivity.Orientation.PORTRAIT);
        else
            ActivityTool.setOrientation(this, MainActivity.Orientation.LANDSCAPE);
        Spinner cmbOpciones = (Spinner) findViewById(R.id.select_gamer_spinner);
        String nombre = ActivityTool.getStringFromPreferences(getApplicationContext(), ComunioConstants.PROPERTY_MY_TEAM);
        Integer position = 0;
        final ArrayList<String> datos = new ArrayList<>();
        List<GamerInformation> participantes = MainActivity.getGamers();
        if (participantes.size() == 0) {
            ActivityTool.showToast(this, "no hay participantes", Toast.LENGTH_LONG);
            setResult(RESULT_CANCELED);
            finish();
        }
        for (int i = 0; i < participantes.size(); i++) {
            datos.add(participantes.get(i).getParticipante().getNombre());
            if (nombre.compareTo(participantes.get(i).getParticipante().getNombre()) == 0)
                position = i;
        }
        ArrayAdapter<String> adaptador =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, datos);

        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        cmbOpciones.setAdapter(adaptador);
        cmbOpciones.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        ActivityTool.savePreferences(getApplicationContext(),
                                ComunioConstants.PROPERTY_MY_TEAM, datos.get(position));

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        cmbOpciones.setSelection(position);
        Button boton = (Button) findViewById(R.id.select_gamer_button);
        boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String nombre = ActivityTool.getStringFromPreferences(getApplicationContext(),
                        ComunioConstants.PROPERTY_MY_TEAM);
                Log.d(TAG, "salvo: " + nombre);
                if (nombre.compareTo("") == 0)
                    ActivityTool.showToast(getParent(), getResources().getString(R.string.pick_no_team), Toast.LENGTH_LONG);
                else {
                    setResult(RESULT_OK);
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {

    }

}
