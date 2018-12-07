package app.alyosharomanov.pocketdex;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


public class MenuDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final int SETTINGS_INT = 1;
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_menu, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        Button search = (Button) layout.findViewById(R.id.menu_search);
        Button list = (Button) layout.findViewById(R.id.menu_list);
        Button credits = (Button) layout.findViewById(R.id.menu_credits);
        Button back = (Button) layout.findViewById(R.id.menu_back);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSettingsIntent = new Intent(getContext(), PokemonListActivity.class);
                startActivityForResult(launchSettingsIntent, SETTINGS_INT);
            }
        });

        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSettingsIntent = new Intent(getContext(), CreditsActivity.class);
                startActivityForResult(launchSettingsIntent, SETTINGS_INT);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

}