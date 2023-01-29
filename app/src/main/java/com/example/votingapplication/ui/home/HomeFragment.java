package com.example.votingapplication.ui.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.votingapplication.Adapter.HomeAdapter;
import com.example.votingapplication.Adapter.VoteAdapter;
import com.example.votingapplication.R;
import com.example.votingapplication.databinding.FragmentHomeBinding;
import com.example.votingapplication.model.Candidate;
import com.example.votingapplication.model.Student;
import com.example.votingapplication.model.Vote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Student student;
    private HomeAdapter generalVoteAdapter, voteAdapter;
    private List<Vote> generalVote = new ArrayList<>();
    private List<Vote> facultyVote = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel welcomeText =
                new ViewModelProvider(this).get(HomeViewModel.class);
        String faculty = null;

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        if (getArguments() != null) {
            student = (Student) getArguments().getSerializable("student");
            welcomeText.setText(student.getName());
            binding.textView8.setText(student.getFaculty());
            faculty = student.getFaculty();
            welcomeText.getText().observe(getViewLifecycleOwner(), textView::setText);
        }

        generalVoteAdapter = new HomeAdapter(getContext(), generalVote);
        voteAdapter = new HomeAdapter(getContext(), facultyVote);

        db.collection("vote")
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
                                    Vote vote = new Vote(
                                            document.getId(),
                                            data.getString("name"),
                                            data.getString("club"),
                                            data.getBoolean("general"),
                                            data.getInt("count"),
                                            "candidate/"+document.getId()+".jpg",
                                            data.getString("faculty")
                                    );
                                    generalVote.add(vote);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            generalVoteAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("vote")
                .whereEqualTo("general", false)
                .whereEqualTo("faculty", faculty)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                JSONObject data = new JSONObject(document.getData());
                                try {
                                    Vote vote = new Vote(
                                            document.getId(),
                                            data.getString("name"),
                                            data.getString("club"),
                                            data.getBoolean("general"),
                                            data.getInt("count"),
                                            "candidate/"+document.getId()+".jpg",
                                            data.getString("faculty")
                                    );
                                    facultyVote.add(vote);
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

        binding.rcvGeneral.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.rcvGeneral.setAdapter(generalVoteAdapter);
        binding.rcvFaculty.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        binding.rcvFaculty.setAdapter(voteAdapter);
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_vote, getArguments());
            }
        });
        TextView rule1 = binding.text3;
        TextView rule2 = binding.text4;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                rule1.setText("1. General : Choose "+(generalVote.size()-1)+" candidates.");
                rule2.setText("2. Faculty : Choose "+(facultyVote.size()-1)+" candidates.");
            }
        }, 1000);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}