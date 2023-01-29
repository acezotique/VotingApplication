package com.example.votingapplication.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingapplication.R;
import com.example.votingapplication.model.Vote;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    Context context;
    List<Vote> vote;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public HomeAdapter(Context context, List<Vote> vote){
        this.context = context;
        this.vote = vote;
    }

    @NonNull
    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home_result, parent, false);
        return new HomeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Vote voteList = vote.get(position);
        holder.cName.setText(voteList.getName());
        holder.cClub.setText(voteList.getClub());
        holder.cCount.setText("Total Vote: "+String.valueOf(voteList.getCount()));
        StorageReference pic = storage.getReference().child(voteList.getImage());
        pic.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                holder.cImg.setImageBitmap(bmp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vote.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cName, cClub, cCount;
        ImageView cImg;
        CardView cvHome;

        public MyViewHolder(View itemView){
            super(itemView);
            cvHome = itemView.findViewById(R.id.cvHome);
            cImg = itemView.findViewById(R.id.cImg);
            cName = itemView.findViewById(R.id.cName);
            cCount = itemView.findViewById(R.id.candidateVote);
            cClub = itemView.findViewById(R.id.cClub);
        }
    }

}
