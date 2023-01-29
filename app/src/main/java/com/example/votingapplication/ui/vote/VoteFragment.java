package com.example.votingapplication.ui.vote;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.votingapplication.Adapter.VoteAdapter;
import com.example.votingapplication.R;
import com.example.votingapplication.databinding.FragmentVoteBinding;
import com.example.votingapplication.model.Candidate;
import com.example.votingapplication.model.Student;
import com.example.votingapplication.model.Vote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VoteFragment extends Fragment {

    private FragmentVoteBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Student student;
    private VoteAdapter voteGeneralAdapter, voteAdapter;
    private final List<Candidate> listGeneralCandidate = new ArrayList<>();
    private final List<Candidate> listCandidate = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VoteViewModel slideshowViewModel =
                new ViewModelProvider(this).get(VoteViewModel.class);

        binding = FragmentVoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getArguments() != null) {
            student = (Student) getArguments().getSerializable("student");
            binding.textView6.setText(student.getFaculty());
        }

        voteGeneralAdapter = new VoteAdapter(getContext(), listGeneralCandidate);
        voteAdapter = new VoteAdapter(getContext(), listCandidate);

        db.collection("candidate")
                .whereEqualTo("general", true)
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
                                    listGeneralCandidate.add(candidate);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            voteGeneralAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("candidate")
                .whereEqualTo("general", false)
                .whereEqualTo("faculty", student.getFaculty())
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
                            }
                            voteAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        binding.recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView1.setAdapter(voteGeneralAdapter);
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView2.setAdapter(voteAdapter);

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fnVote(view);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void fnVote(View view){
        List<Vote> selectedGeneralCandidate = voteGeneralAdapter.getCandidateSelected();
        List<Vote> selectedCandidate = voteAdapter.getCandidateSelected();
        List<Vote> voteSubmit = new ArrayList<>();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (selectedGeneralCandidate.size() == 0){
                    Log.d("Error", "Null");
                }
                for (Vote gcandidate:
                        selectedGeneralCandidate) {
                    Log.d("Name", gcandidate.getName());
                    db.collection("vote").document(gcandidate.getId()).set(gcandidate);
                    voteSubmit.add(gcandidate);
                }
                for (Vote candidate:
                        selectedCandidate) {
                    voteSubmit.add(candidate);
                }
                for (Vote v:
                        voteSubmit) {
                    db.collection("vote").document(v.getId()).set(v);
                    Log.d("Result", "Successfully vote for: "+ v.getName());
                }
            }
        }, 1000);
        Toast.makeText(getContext(), "Successfully vote.", Toast.LENGTH_SHORT)
                .show();
        NavController navController = Navigation.findNavController(getActivity(),
                R.id.nav_host_fragment_content_main);
        navController.popBackStack(R.id.nav_home, true);
        navController.navigate(R.id.nav_home, getArguments());
    }
}