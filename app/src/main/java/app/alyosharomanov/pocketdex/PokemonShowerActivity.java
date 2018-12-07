package app.alyosharomanov.pocketdex;
import PokeAPI.*;

import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.AndroidException;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class PokemonShowerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int SETTINGS_INT = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_shower);
        updateFullScreen();

        Intent curr_intent = getIntent();
        final int curr_pokemon = curr_intent.getIntExtra("curr_pokemon", 0);

        //set thread policy to allow inherent access
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //create pokemon
        Pokemon pokemon = null;
        try {
            pokemon = Interface.getPokemon(curr_pokemon);
        } catch (IOException | JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        //create all elements
        final ConstraintLayout contrainLayout = (ConstraintLayout) findViewById(R.id.pokemon_shower_background);
        final RoundedImageView background = (RoundedImageView) findViewById(R.id.pokemon_background);
        final ImageView image = (ImageView) findViewById(R.id.pokemon_image);
        final TextView title = (TextView) findViewById(R.id.title);
        final Button previous = (Button) findViewById(R.id.previous);
        final Button menu = (Button) findViewById(R.id.menu);
        final Button next = (Button) findViewById(R.id.next);

        /**set all elements**/
        contrainLayout.setBackgroundColor(Color.parseColor(Interface.getPokemonColor(pokemon.getTypes()[0])));
        previous.setBackgroundColor(darker(Color.parseColor(Interface.getPokemonColor(pokemon.getTypes()[0])), .75f));
        menu.setBackgroundColor(darker(Color.parseColor(Interface.getPokemonColor(pokemon.getTypes()[0])), .6f));
        next.setBackgroundColor(darker(Color.parseColor(Interface.getPokemonColor(pokemon.getTypes()[0])), .75f));

        //background image
        background.setCornerRadius(30);
        new SetBackgroundCanvas(background).execute(Interface.getBackgroundURL(pokemon));

        //image
        new SetImageCanvas(image).execute(pokemon.getUrl());

        //name of pokemon
        String nameCreator = (pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1))
                .replaceAll("-aria","")
                .replaceAll("-ordinary","")
                .replaceAll("-incarnate","")
                .replaceAll("-f","")
                .replaceAll("-m","");
        title.setText(nameCreator);

        //pokemon text
        final TextView name = (TextView) findViewById(R.id.pokemon_name);
        name.setText(nameCreator);

        final TextView number = (TextView) findViewById(R.id.pokemon_number);
        number.setText("#" + pokemon.getId());

        final TextView description = (TextView) findViewById(R.id.pokemon_description);
        description.setText(pokemon.getDescription());

        final TextView height = (TextView) findViewById(R.id.pokemon_height);
        double height_meters = (double) pokemon.getHeight() / 10;
        height.setText("Height: " + height_meters + "m");

        final TextView weight = (TextView) findViewById(R.id.pokemon_weight);
        double weight_kilos = (double) pokemon.getWeight() / 10;
        weight.setText("Weight: " + weight_kilos + "kg");

        final TextView hp = (TextView) findViewById(R.id.pokemon_hp);
        hp.setText("HP: " + pokemon.getHp());

        final TextView speed = (TextView) findViewById(R.id.pokemon_speed);
        speed.setText("Speed: " + pokemon.getSpeed());

        final TextView defense = (TextView) findViewById(R.id.pokemon_defense);
        defense.setText("Defense: " + pokemon.getDefence());

        final TextView attack = (TextView) findViewById(R.id.pokemon_attack);
        attack.setText("Attack: " + pokemon.getAttack());

        final TextView specialDefense = (TextView) findViewById(R.id.pokemon_specialDefense);
        specialDefense.setText("Special Defense: " + pokemon.getSpecialDefense());

        final TextView specialAttack = (TextView) findViewById(R.id.pokemon_specialAttack);
        specialAttack.setText("Special Attack: " +  pokemon.getSpecialAttack());

        //types

        LinearLayout types_menu = (LinearLayout) findViewById(R.id.type_menu);
        types_menu.setHorizontalGravity(1);
        for (int i = 0; i < pokemon.getTypes().length; i ++) {
            CardView.LayoutParams params_card = new CardView.LayoutParams(
                    CardView.LayoutParams.WRAP_CONTENT,
                    CardView.LayoutParams.WRAP_CONTENT,
                    1
            );
            params_card.setMargins(5, 5, 5, 5);

            CardView cardView = new CardView(this);
            cardView.setLayoutParams(params_card);
            cardView.setRadius(50);
            cardView.setBackgroundColor(Color.parseColor(Interface.getPokemonColor(pokemon.getTypes()[i])));

            TextView textView = new TextView(this);
            textView.setText(pokemon.getTypes()[i].substring(0, 1).toUpperCase() + pokemon.getTypes()[i].substring(1));
            textView.setTextColor(ContextCompat.getColor(this, R.color.white));
            textView.setTypeface(ResourcesCompat.getFont(this, R.font.pokemon_classic ), Typeface.BOLD);
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(5,5,5,5);

            cardView.addView(textView);
            types_menu.addView(cardView);
        }


        //menu
        final Pokemon finalPokemon = pokemon;
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDialog menuDialog = new MenuDialog();
                menuDialog.show(getSupportFragmentManager(), "MenuDialog");
            }});


        //back and forward buttons
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSettingsIntent = new Intent(PokemonShowerActivity.this, PokemonShowerActivity.class);
                int value = curr_pokemon - 1;
                if (value < 0) {
                    value = 0;
                }
                launchSettingsIntent.putExtra("curr_pokemon", value);
                startActivityForResult(launchSettingsIntent, SETTINGS_INT);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = curr_pokemon + 1;
                if (value > 949) {
                    value = 949;
                }
                Intent launchSettingsIntent = new Intent(PokemonShowerActivity.this, PokemonShowerActivity.class);
                launchSettingsIntent.putExtra("curr_pokemon", value);
                startActivityForResult(launchSettingsIntent, SETTINGS_INT);
            }
        });
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

    private class SetImageCanvas extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public SetImageCanvas(ImageView bmImage) {
            this.bmImage = bmImage;
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new DecelerateInterpolator());
            fadeIn.setDuration(1000);
            bmImage.setAnimation(fadeIn);

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
            //result = CropBitmapTransparency(result);
            result = Bitmap.createScaledBitmap(result, result.getWidth()*10, result.getHeight()*10, false);
            bmImage.setImageBitmap(result);
        }
    }

    private class SetBackgroundCanvas extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public SetBackgroundCanvas(ImageView bmImage) {
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
            bmImage.setImageBitmap(result);
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

    private void updateFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public static int darker (int color, float factor) {
        int a = Color.alpha( color );
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }
}
