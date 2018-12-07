package app.alyosharomanov.pocketdex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;

import PokeAPI.Interface;
import PokeAPI.Pokemon;

public class PokemonListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int SETTINGS_INT = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);
        updateFullScreen();

        LinearLayout menu = (LinearLayout) findViewById(R.id.pokemon_list);
        String[] pokemonList = null;
        try {
            pokemonList = Interface.getPokemonList();
                   } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        //int[] id = new int[pokemonList.length];

        for (int i = 0; i < pokemonList.length; i++) {
            Button button = new Button(this);
            button.setText((i + 1) + ". " + pokemonList[i]);
            button.setGravity(Gravity.LEFT);
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.background));
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            button.setTextSize(16);
            button.setTypeface(ResourcesCompat.getFont(this, R.font.pokemon_classic ));

            final int currentpokemon = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchSettingsIntent = new Intent(PokemonListActivity.this, PokemonShowerActivity.class);
                    launchSettingsIntent.putExtra("curr_pokemon", currentpokemon);
                    startActivityForResult(launchSettingsIntent, SETTINGS_INT);
                }
            });

            menu.addView(button);
        }

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        //new SetButtonColor().execute(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFullScreen();
    }

    /**private class SetButtonColor extends AsyncTask<int[], Void, String[]> {
        Button[] buttons;

        @Override
        protected String[] doInBackground(int[]... ints) {
            int[] list = ints[0];
            buttons = new Button[list.length];
            String[] pokemon_types = new String[list.length];
            for (int i = 0; i < list.length; i++) {
                buttons[i]  = findViewById(list[i]);
                Pokemon pokemon = null;
                try {
                    pokemon = Interface.getPokemon(i);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                pokemon_types[i] = pokemon.getTypes()[0];
            }
            Toast.makeText(PokemonListActivity.this, "Done Loading", Toast.LENGTH_SHORT).show();
            return pokemon_types;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            for (int i = 0; i < strings.length; i++) {
                buttons[i].setBackgroundColor(Color.parseColor(Interface.getPokemonColor(strings[i])));
            }
            findViewById(R.id.pokemon_list).invalidate();
        }
    }**/

    private void updateFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

}
