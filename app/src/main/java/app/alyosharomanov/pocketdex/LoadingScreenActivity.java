package app.alyosharomanov.pocketdex;

import android.app.LauncherActivity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

import PokeAPI.Interface;
import PokeAPI.Pokemon;

public class LoadingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int SETTINGS_INT = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        updateFullScreen();

        TextView loadingText = (TextView) findViewById(R.id.loadingText);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(3000);
        loadingText.setAnimation(fadeIn);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent launchScreenIntent = new Intent(LoadingScreenActivity.this, PokemonShowerActivity.class);
                startActivityForResult(launchScreenIntent, SETTINGS_INT);
            }
        }, 3500);
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
