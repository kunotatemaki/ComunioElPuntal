package com.rukiasoft.androidapps.comunioelpuntal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.Spanned;

import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.DatabaseHandler;
import com.rukiasoft.androidapps.comunioelpuntal.dataclasses.Signing;
import com.rukiasoft.androidapps.comunioelpuntal.utils.ActivityTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruler on 2014.
 */
public class PlayerSigningDialogFragment extends DialogFragment {

    public static PlayerSigningDialogFragment newInstance(String name) {
        PlayerSigningDialogFragment frag = new PlayerSigningDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String name = getArguments().getString("name");
        DatabaseHandler dbHandler = new DatabaseHandler(getActivity().getApplicationContext());
        List<Signing> fichajes;
        try {
            fichajes = dbHandler.getSignings(name, DatabaseOpenHelper.NOMBRE);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        List<Spanned> items = new ArrayList<>();
        String patron = getActivity().getApplicationContext().getResources().getString(R.string.signings_pattern);
        if (fichajes.size() == 0)
            items.add(Html.fromHtml(getActivity().getApplicationContext().getResources().getString(R.string.no_signings)));
        else {

            for (int i = 0; i < fichajes.size(); i++) {
                String precio = ActivityTool.getFormatedCurrencyNumber(fichajes.get(i).getPrecio());
                String frase = patron.replace("_precio_", "<font color='olive'><b>" + precio + "</b></font>");
                frase = frase.replace("€", "<font color='olive'><b>€</b></font>");
                frase = frase.replaceAll("_salto_", "<br>");
                frase = frase.replace("_comprador_", "<font color='blue'><b>" + fichajes.get(i).getComprador() + "</b></font>");
                frase = frase.replace("_vendedor_", "<font color='red'><b>" + fichajes.get(i).getVendedor() + "</b></font>");
                frase = frase.replace("_jornada_", "<b>" + ActivityTool.getStringFromDouble(fichajes.get(i).getJornada()) + "</b>");
                items.add(Html.fromHtml(frase));
            }
        }
        CharSequence[] cs = items.toArray(new CharSequence[items.size()]);
        Drawable icon = getResources().getDrawable(R.drawable.market);
        Bitmap b = ((BitmapDrawable) icon).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 48, 48, false);
        icon = new BitmapDrawable(getResources(), bitmapResized);

        return new AlertDialog.Builder(getActivity())
                .setIcon(icon)
                .setTitle(name)
                .setItems(cs, null)
                .setPositiveButton(R.string.accept,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                PlayerSigningDialogFragment.this.getDialog().cancel();
                            }
                        }
                )
                .create();
    }
}