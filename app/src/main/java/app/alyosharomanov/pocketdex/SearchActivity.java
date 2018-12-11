package app.alyosharomanov.pocketdex;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import PokeAPI.Interface;
import PokeAPI.Pokemon;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    final int SETTINGS_INT = 1;
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] pokemonNameList;
    ArrayList<PokemonNames> arraylist = new ArrayList<PokemonNames>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        updateFullScreen();

        Button back = (Button) findViewById(R.id.back_search);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        try {
            pokemonNameList = Interface.getPokemonList();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        list = (ListView) findViewById(R.id.listview);
        list.setOnItemClickListener(this);
        for (int i = 0; i < pokemonNameList.length; i++) {
            PokemonNames pokemonNames = new PokemonNames(pokemonNameList[i], i);
            // Binds all strings into an array
            arraylist.add(pokemonNames);
        }

        adapter = new ListViewAdapter(this, arraylist);
        list.setAdapter(adapter);
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

        updateFullScreen();
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent intent = new Intent();
        int pos = ((PokemonNames) list.getItemAtPosition(position)).getPokemonNumber();

        intent.setClass(this, PokemonShowerActivity.class);
        intent.putExtra("curr_pokemon", pos);
        startActivity(intent);
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        updateFullScreen();
        String text = s;
        adapter.filter(s);
        return false;
    }
}
