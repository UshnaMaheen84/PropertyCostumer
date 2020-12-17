package com.example.propertycostumer.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.propertycostumer.Plots;
import com.example.propertycostumer.PropertyDetail;
import com.example.propertycostumer.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PropertyAdapter extends FirebaseRecyclerAdapter<Plots, PropertyAdapter.PropertyViewHolder> {

    Context context;
    ProgressDialog progressDialog;


    public PropertyAdapter(@NonNull FirebaseRecyclerOptions<Plots> options, Context context, ProgressDialog progressDialog) {
        super(options);
        this.context = context;
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onBindViewHolder(@NonNull final PropertyViewHolder holder, int position, @NonNull final Plots model) {

        final ArrayList<String> imageUrl = model.getImageUrl();
        if (imageUrl != null && imageUrl.size() > 0) {
            Log.e("imagePosition", imageUrl.get(0));
            Picasso.get().load(imageUrl.get(0)).into(holder.plotImage);
        } else {
            Picasso.get().load(R.drawable.no_image).into(holder.plotImage);
        }


        final String key = getRef(position).getKey();

        String prprty_type_id = "";
        final String constructed = model.getConstructed();

        if (model.getProperty_type_id().equals("1")) {
            prprty_type_id = "Residential";
        }
        if (model.getProperty_type_id().equals("2")) {
            prprty_type_id = "Commercial";
        }

        holder.sold.setText(model.getIs_sold());
        holder.propertyType.setText(prprty_type_id);
        holder.plotname.setText("Location, " + model.getName());
        holder.square_yard.setText(model.getSq_yrds() + " Sq. Ft.");


        holder.pricerangeFrom.setText("PKR. " + model.getPlot_price_range_from());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PropertyDetail.class);
                intent.putExtra("key", key);
                intent.putExtra("imageUrl", imageUrl);
                context.startActivity(intent);
            }
        });
        Log.e("working", "bind working");
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview, parent, false);
        Log.e("working", "inflate working");

        return new PropertyViewHolder(view);
    }

    @Override
    public void onDataChanged() {
        progressDialog.dismiss();

    }


    class PropertyViewHolder extends RecyclerView.ViewHolder {

        TextView propertyType, plotname, square_yard, pricerangeFrom, sold;
        ImageView plotImage;
        CardView cardView;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.plotcardview);
            plotImage = itemView.findViewById(R.id.plotImage);
            propertyType = itemView.findViewById(R.id.tv_property_type);
            plotname = itemView.findViewById(R.id.tv_plot_name);
            square_yard = itemView.findViewById(R.id.tv_square_yard);
            pricerangeFrom = itemView.findViewById(R.id.priceRange);
            sold = itemView.findViewById(R.id.tv_sold);

        }
    }

}
