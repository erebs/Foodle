package com.erebsindia.foodle.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.erebsindia.foodle.R;
import com.erebsindia.foodle.models.MainCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class MainCategoryAdaptor extends RecyclerView.Adapter<MainCategoryAdaptor.MyViewHolder>
{
    Context ctx;
    private List<MainCategoryModel> mainCategoryModels = new ArrayList<>();
    MainCategoryModel mainCategoryModel;

    public MainCategoryAdaptor(Context ctx)
    {
        this.ctx = ctx;
    }

    public void renewItems(List<MainCategoryModel> list)
    {
        this.mainCategoryModels = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_view, parent,false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position)
    {
            mainCategoryModel = mainCategoryModels.get(position);
            holder.Name.setText(mainCategoryModel.getName());
            Glide.with(holder.itemView).load(mainCategoryModel.getImage()).into(holder.Image);
            holder.Click.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                    {
//                        Intent intents = new Intent(ctx.getApplicationContext(), SubCategoryActivity.class);
//                        intents.putExtra("categoryId",mCategories.get(position).getId());
//                        intents.putExtra("categoryName",mCategories.get(position).getName());
//                        ctx.startActivity(intents);
                    }
            });
    }

    @Override
    public int getItemCount()
    {
        return mainCategoryModels.size();
    }

    public void clear()
    {
        int size = mainCategoryModels.size();
        mainCategoryModels.clear();
        notifyItemRangeRemoved(0, size);
        Log.e("ClearTS","Done");
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView Name;
        ImageView Image;
        LinearLayout Click;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            Name = itemView.findViewById(R.id.main_cat_name);
            Image = itemView.findViewById(R.id.main_cat_image);
            Click = itemView.findViewById(R.id.main_cat_click);
        }

    }
}