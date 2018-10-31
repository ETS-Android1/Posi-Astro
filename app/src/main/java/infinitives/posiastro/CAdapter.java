package infinitives.posiastro;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by VedX&Div on 10/23/2017.
 */

public class CAdapter extends RecyclerView.Adapter<CAdapter.ViewHolder> {

    private Context context;
    private ArrayList<astro> astrolist;
    private int Images[];


    public CAdapter(Context context, ArrayList<astro> astrolist, int[] images) {
        this.context = context;
        this.astrolist = astrolist;
        Images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cview, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final astro as = astrolist.get(position);
        holder.textView.setText(as.getName());
        final int images = Images[position];
        Glide.with(context).load(images).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, json.class);
                intent.putExtra("data", as.getCountry());
                intent.putExtra("image",images);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return astrolist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CardView cardView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card);
            textView = itemView.findViewById(R.id.text);

        }
    }
}