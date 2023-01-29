package com.example.votingapplication.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingapplication.R;
import com.example.votingapplication.model.Candidate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.MyViewHolder>{

    Context context;
    List<Candidate> candidates;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public CandidateAdapter(Context context, List<Candidate> candidates){
        this.context = context;
        this.candidates = candidates;
    }

    @NonNull
    @Override
    public CandidateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Provide looks to our row

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_candidate_list, parent, false);

        return new CandidateAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Candidate candidateList = candidates.get(position);
        Log.d("Picture Path", candidateList.getImage());

        StorageReference pic = storage.getReference().child(candidateList.getImage());
        pic.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("Picture", "Success");
                Bitmap bmp = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                holder.im1.setImageBitmap(bmp);
            }
        });

        //tambah holder untuk image
        holder.t1.setText(candidateList.getName());
        holder.t2.setText(candidateList.getClub());
        holder.t3.setText(candidateList.getFaculty());

    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView t1,t2, t3;
        ImageView im1;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cvHome);
            im1 = itemView.findViewById(R.id.cImg);
            t1 = itemView.findViewById(R.id.t3);
            t2 = itemView.findViewById(R.id.title1);
            t3 = itemView.findViewById(R.id.t4);

        }
    }
}

