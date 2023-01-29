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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingapplication.R;
import com.example.votingapplication.model.Candidate;
import com.example.votingapplication.model.Vote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.MyViewHolder>{

    Context context;
    List<Candidate> candidates;
    List<Integer> candidateSelected = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public VoteAdapter(Context context, List<Candidate> candidates){
        this.context = context;
        this.candidates = candidates;
    }

    public List<Vote> getCandidateSelected(){
        Log.d("Test", "data fetched");
        List<Vote> selectedVote = new ArrayList<>();
        for (int pos:
             candidateSelected) {
            db.collection("vote").document(candidates.get(pos).getId()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    JSONObject doc = new JSONObject(documentSnapshot.getData());
                                    try {
                                        Log.d("General", String.valueOf(doc.getBoolean("general")));
                                        Vote v = new Vote(
                                                documentSnapshot.getId(),
                                                doc.getString("name"),
                                                doc.getString("club"),
                                                doc.getBoolean("general"),
                                                doc.getInt("count")+1,
                                                "candidate/"+candidates.get(pos).getId()+".jpg",
                                                doc.getString("faculty")
                                        );
                                        selectedVote.add(v);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Log.d("Name", "Not Exist");
                                    Vote v = new Vote(
                                            candidates.get(pos).getId(),
                                            candidates.get(pos).getName(),
                                            candidates.get(pos).getClub(),
                                            candidates.get(pos).isGeneral(),
                                            1,
                                            candidates.get(pos).getImage(),
                                            candidates.get(pos).getFaculty()
                                    );
                                    selectedVote.add(v);
                                }
                            }
                        }
                    });
        }
        return selectedVote;
    }


    public void toggleVote(int position){
        if (candidateSelected.contains(position)){
            candidateSelected.remove(candidateSelected.indexOf(position));
            candidates.get(position).setChecked(false);
        }else {
            if ((candidateSelected.size()+1)>=candidates.size()){
                Toast.makeText(context, "Cannot vote more than " + (candidates.size()-1), Toast.LENGTH_SHORT).show();
            }else {
                candidateSelected.add(position);
                candidates.get(position).setChecked(true);
            }
        }
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public VoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Provide looks to our row

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_vote_list, parent, false);

        return new VoteAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Candidate candidateList = candidates.get(position);

        StorageReference pic = storage.getReference().child(candidateList.getImage());
        pic.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("General", String.valueOf(candidateList.isGeneral()));
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

        if (candidateList.isChecked()) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.purple_700));
            holder.t1.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.t2.setTextColor(ContextCompat.getColor(context, R.color.white));
        }else{
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.t1.setTextColor(ContextCompat.getColor(context, R.color.grey));
            holder.t2.setTextColor(ContextCompat.getColor(context, R.color.grey));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test", "Pressed");
                toggleVote(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView im1;
        TextView t1, t2;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cvHome);
            im1 = itemView.findViewById(R.id.cImg);
            t1 = itemView.findViewById(R.id.cName);
            t2 = itemView.findViewById(R.id.cClub);

        }
    }
}

