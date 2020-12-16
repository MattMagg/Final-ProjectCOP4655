package com.example.finalprojectcop4655.Details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectcop4655.R;
import com.example.finalprojectcop4655.Utility.Common;

import java.util.List;

public class DetailsViewAdapter extends RecyclerView.Adapter<DetailsViewAdapter.ViewHolder> {

    List<String> arrayList;
    Context context;

    public DetailsViewAdapter(Context context, List<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DetailsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewAdapter.ViewHolder holder, int position) {
        Common.loadImages(context, arrayList.get(position), holder.mimageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mimageView;

        public ViewHolder(View view) {
            super(view);
            mimageView = view.findViewById(R.id.review_images);

        }

    }
}
