package com.example.finalprojectcop4655.Fragments;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectcop4655.R;
import com.example.finalprojectcop4655.Details.DetailsActivity;
import com.example.finalprojectcop4655.Utility.Common;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Data> mArrayList;
    Context context;
    private final int CATEGORY = 1;
    private final int HEADER = 2;

    public RecyclerViewAdapter(Context context, ArrayList<Data> arrayList) {
        this.context = context;
        this.mArrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restraunt_title, parent, false);
            return new ViewHolder(view);
        }else if(viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false);
            return new ViewHolderHeader(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restraunt_title, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mArrayList.get(position).mType!= null && mArrayList.get(position).mType.equals(Common.CATEGORY))
            return CATEGORY;
        else if(mArrayList.get(position).mType!= null && mArrayList.get(position).mType.equals(Common.HEADER))
            return HEADER;
        else
            return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(mArrayList.get(position).mType!= null && mArrayList.get(position).mType.equals(Common.CATEGORY)){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.mName.setText(mArrayList.get(position).mTitle);
            viewHolder.mDescription.setText(mArrayList.get(position).mReview);
            Common.loadImages(context, mArrayList.get(position).mImage_url, viewHolder.mImageView);
        }else if(mArrayList.get(position).mType!= null && mArrayList.get(position).mType.equals(Common.HEADER) ){
            ViewHolderHeader viewHolder = (ViewHolderHeader) holder;
            viewHolder.mHeader.setText(mArrayList.get(position).mHeaderTitle+"("+mArrayList.get(position).mOccurence+")");
        }

    }



    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName, mDescription;
        RelativeLayout mParent;
        ImageView mImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            mParent = itemView.findViewById(R.id.parent);
            mName = itemView.findViewById(R.id.name);
            mDescription = itemView.findViewById(R.id.review);
            mImageView = itemView.findViewById(R.id.icon);
            mParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(Common.RESTURANT_ID, mArrayList.get(getLayoutPosition()).mId); //Your id
                    context.startActivity(intent);
                }
            });
        }

    }

    class ViewHolderHeader extends RecyclerView.ViewHolder{

        TextView mHeader;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            mHeader = itemView.findViewById(R.id.header_text);
        }
    }
}







