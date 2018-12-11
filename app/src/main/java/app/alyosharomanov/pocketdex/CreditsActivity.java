package app.alyosharomanov.pocketdex;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

import PokeAPI.Interface;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        updateFullScreen();

        Button back = (Button) findViewById(R.id.back_credits);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        LinearLayout credits_pokemon = (LinearLayout) findViewById(R.id.credits_pokemon);
        String[] pokemons = null;
        try {
            pokemons = Interface.getPokemonList();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < pokemons.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(pokemons[i].substring(0, 1).toUpperCase() + pokemons[i].substring(1));
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(ResourcesCompat.getFont(this, R.font.pokemon_classic ));
            textView.setTextSize(16);
            credits_pokemon.addView(textView);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFullScreen();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateFullScreen();
    }

    private void updateFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
    }
}
