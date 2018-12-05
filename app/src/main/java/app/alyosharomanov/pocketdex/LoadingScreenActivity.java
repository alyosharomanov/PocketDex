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

import PokeAPI.Interface;

public class LoadingScreenActivity extends AppCompatActivity {

    Intent music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final int SETTINGS_INT = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        //set fullscreen on all systems
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        music = new Intent(LoadingScreenActivity.this, BackgroundSoundService.class);
        startService(music);

        TextView loadingText = (TextView) findViewById(R.id.loadingText);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(3000);
        loadingText.setAnimation(fadeIn);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent launchScreenIntent = new Intent(LoadingScreenActivity.this, PokemonShowerActivity.class);
                launchScreenIntent.putExtra("music", music);
                startActivityForResult(launchScreenIntent, SETTINGS_INT);
            }
        }, 3500);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //set fullscreen on all systems
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
