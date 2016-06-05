package com.hackerkernel.admin.savlife.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hackerkernel.admin.savlife.R;
import com.hackerkernel.admin.savlife.activity.DonorDetailActivity;
import com.hackerkernel.admin.savlife.pojo.DonorListPojo;

import java.util.List;

/**
 * Created by Murtaza on 6/1/2016.
 */
public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.MyViewHolder> {
    private static final String TAG = DonorListAdapter.class.getSimpleName();
    private List<DonorListPojo> mList;
    private Context context;


    public DonorListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<DonorListPojo> list){
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.donor_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DonorListPojo pojo = mList.get(position);
        holder.userName.setText(pojo.getUserName());
        holder.bloodGroup.setText(pojo.getUserBloodGroup());

        if (!pojo.getImageUrl().isEmpty()){
            String url = "" + pojo.getImageUrl();

            Glide.with(context)
                    .load(url)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.userImage);
        }else {
            holder.userImage.setImageResource(R.drawable.placeholder_80_80);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView userName, bloodGroup;
        private ImageView userImage;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            userName = (TextView) itemView.findViewById(R.id.donor_name);
            bloodGroup = (TextView) itemView.findViewById(R.id.blood_group_text);
            userImage = (ImageView) itemView.findViewById(R.id.donor_image);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            String id = mList.get(pos).getUserId();
            Intent intent = new Intent(context, DonorDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
