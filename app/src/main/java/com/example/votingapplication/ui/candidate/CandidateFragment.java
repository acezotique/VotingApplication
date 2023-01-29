package com.example.votingapplication.ui.candidate;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.votingapplication.Adapter.CandidateAdapter;
import com.example.votingapplication.databinding.FragmentCandidateBinding;
import com.example.votingapplication.model.Candidate;
import com.example.votingapplication.model.Vote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CandidateFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FragmentCandidateBinding binding;
    private List<Candidate> listCandidate = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CandidateViewModel galleryViewModel =
                new ViewModelProvider(this).get(CandidateViewModel.class);

        binding = FragmentCandidateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CandidateAdapter candidateAdapter = new CandidateAdapter(getContext(), listCandidate);

        db.collection("candidate")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                JSONObject data = new JSONObject(document.getData());
                                try {
                                    Candidate candidate = new Candidate(
                                            document.getId(),
                                            "candidate/"+document.getId()+".jpg",
                                            data.getString("name"),
                                            data.getString("club"),
                                            data.getBoolean("general"),
                                            data.getString("faculty")
                                    );
                                    listCandidate.add(candidate);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                candidateAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        binding.recyclerView10.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerView10.setAdapter(candidateAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}