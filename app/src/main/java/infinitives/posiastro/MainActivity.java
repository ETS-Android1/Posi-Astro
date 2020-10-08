package infinitives.posiastro;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ArrayList<astro> astros;
    
    // API LINK EXPOSED
    public static String URL = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1RS75MD36VEQUjxdlNAw8bxcp_umIh09neUm69y2hJNM&sheet=Sheet1";
    RecyclerView recycler;
    CAdapter cAdapter;
    TextView textView, moto;
    LinearLayout linearLayout;
    private InterstitialAd interstitialAd;
    private boolean exit = false;


    int Images[] = {R.drawable.aries, R.drawable.taurus,
            R.drawable.gemini, R.drawable.cancer,
            R.drawable.leo, R.drawable.virgo,
            R.drawable.libra, R.drawable.scorpio,
            R.drawable.sagittarius, R.drawable.cap,
            R.drawable.aquarius, R.drawable.piscesss};
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interstitialAd = new InterstitialAd(this);
        // Ad Unit Removed 
        interstitialAd.setAdUnitId("");
        linearLayout = findViewById(R.id.linear);
        recycler = findViewById(R.id.recycle);
        textView = findViewById(R.id.text);
        moto = findViewById(R.id.texts);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmerAnimation();
        astros = new ArrayList<>();
        int nocol = Utility.calNoRows(getApplicationContext());

        recycler.setLayoutManager(new GridLayoutManager(this, nocol));
        recycler.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recycler.setAdapter(cAdapter);
        final AdRequest.Builder request = new AdRequest.Builder();
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


        Json();


    }

    private void Json() {
        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                shimmerFrameLayout.setDuration(700);
                recycler.setVisibility(View.VISIBLE);
                linearLayout.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                linearLayout.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                textView.setTextSize(22);
                moto.setVisibility(View.GONE);
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Sheet1");

                    for (int i = 0; i <= jsonArray.length(); i++) {
                        JSONObject objectj = jsonArray.getJSONObject(i);
                        astro astro = new astro(objectj.getString("name"), objectj.getString("country"));
                        astros.add(astro);
                        //  Toast.makeText(json.this, "" + astro.getName(), Toast.LENGTH_SHORT).show();

                    }

                    /*
                    Use Gson implementaion;
                    Gson gson = new Gson();
                    List<Sheet1> s1 = gson.fromJson(response,Sheet1.java);
                    can pass list to recycler adapter.
                    */

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                cAdapter = new CAdapter(MainActivity.this, astros, Images);
                recycler.setAdapter(cAdapter);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static class Utility {
        public static int calNoRows(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpwidth = displayMetrics.widthPixels / displayMetrics.density;
            int noofcol = (int) (dpwidth / 100);
            return noofcol;
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

}
