package infinitives.posiastro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class json extends AppCompatActivity {

    private TextView text;
    private ImageView imageView;
    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-1361359682897035/3342940024");


        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        text = findViewById(R.id.texts);
        imageView = findViewById(R.id.imageload);
        Intent intent = getIntent();
        Glide.with(getApplicationContext()).load(intent.getIntExtra("image", 0)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        text.setText(intent.getStringExtra("data"));
    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
}