package app.alyosharomanov.pocketdex;
import PokeAPI.*;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class PokemonShowerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final int SETTINGS_INT = 1;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_shower);

        Intent i = getIntent();
        final int curr_pokemon = i.getIntExtra("curr_pokemon", 0);

        //set thread policy to allow inherent access
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //set fullscreen on all systems
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        //create pokemon
        Pokemon pokemon = null;
        try {
            pokemon = Interface.getPokemon(curr_pokemon);
        } catch (IOException | JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        //create all elements
        ImageView image = (ImageView) findViewById(R.id.pokemon_image);
        TextView name = (TextView) findViewById(R.id.pokemon_name);
        Button previous = (Button) findViewById(R.id.previous);
        Button current = (Button) findViewById(R.id.menu);
        Button next = (Button) findViewById(R.id.next);

        //set all elements

        new SetImage(image).execute(pokemon.getUrl());

        String nameCreator = (pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1))
                .replaceAll("-aria","")
                .replaceAll("-ordinary","")
                .replaceAll("-incarnate","");
        name.setText(nameCreator);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSettingsIntent = new Intent(PokemonShowerActivity.this, PokemonShowerActivity.class);
                launchSettingsIntent.putExtra("curr_pokemon", curr_pokemon - 1);
                startActivityForResult(launchSettingsIntent, SETTINGS_INT);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSettingsIntent = new Intent(PokemonShowerActivity.this, PokemonShowerActivity.class);
                launchSettingsIntent.putExtra("curr_pokemon", curr_pokemon + 1);
                startActivityForResult(launchSettingsIntent, SETTINGS_INT);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private class SetImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public SetImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Bitmap cropped = CropBitmapTransparency(result);
            Bitmap scaled = Bitmap.createScaledBitmap(cropped, cropped.getWidth()*10, cropped.getHeight()*10, false);
            bmImage.setImageBitmap(scaled);
        }
    }

    Bitmap CropBitmapTransparency(Bitmap sourceBitmap)
    {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for(int y = 0; y < sourceBitmap.getHeight(); y++)
        {
            for(int x = 0; x < sourceBitmap.getWidth(); x++)
            {
                int alpha = (sourceBitmap.getPixel(x, y) >> 24) & 255;
                if(alpha > 0)   // pixel is not 100% transparent
                {
                    if(x < minX)
                        minX = x;
                    if(x > maxX)
                        maxX = x;
                    if(y < minY)
                        minY = y;
                    if(y > maxY)
                        maxY = y;
                }
            }
        }
        if((maxX < minX) || (maxY < minY))
            return null; // Bitmap is entirely transparent

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }


}
