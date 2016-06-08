package com.hackerkernel.admin.savlife.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hackerkernel.admin.savlife.R;
import com.hackerkernel.admin.savlife.pojo.DonorListPojo;
import com.hackerkernel.admin.savlife.util.Util;
import com.hackerkernel.admin.savlife.constant.EndPoints;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Murtaza on 6/1/2016.
 */
public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.MyViewHolder> {
    private static final String TAG = DonorListAdapter.class.getSimpleName();
    private List<DonorListPojo> mList;
    private Activity context;


    public DonorListAdapter(Activity context) {
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
        DonorListPojo current = mList.get(position);
        holder.donorName.setText(current.getFullname());
        holder.donorBlood.setText(current.getBlood());
        holder.donorGender.setText(current.getGender());
        holder.donorAge.setText(current.getAge()+" years old");
        holder.donorMobile.setText(current.getMobile());
        holder.donorCity.setText(current.getCity());
        if (current.getLastDontaion().equals("null")){
            holder.donorLastDonation.setText("N/A");
        }else {
            holder.donorLastDonation.setText("Last donation: "+current.getLastDontaion());
        }


        if (!current.getImgUrl().isEmpty()){
            String url = EndPoints.IMAGE_BASE_URL + current.getImgUrl();
            Log.d(TAG,"HUS: "+url);
            Glide.with(context)
                    .load(url)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.donorImage);
        }else {
            holder.donorImage.setImageResource(R.drawable.placeholder_80_80);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.list_donor_image) ImageView donorImage;
        @Bind(R.id.list_donor_name) TextView donorName;
        @Bind(R.id.list_donor_blood) TextView donorBlood;
        @Bind(R.id.list_donor_gender) TextView donorGender;
        @Bind(R.id.list_donor_age) TextView donorAge;
        @Bind(R.id.list_donor_mobile) TextView donorMobile;
        @Bind(R.id.list_donor_city) TextView donorCity;
        @Bind(R.id.list_donor_last_donation) TextView donorLastDonation;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            String mobile = mList.get(pos).getMobile();
            //make a call to the number
            Util.dialNumber(context,mobile);
        }
    }

}
