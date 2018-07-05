package com.ijudge.sacijudge.activity;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ijudge.sacijudge.mapmodels.ContestantMapModel;
import com.ijudge.sacijudge.R;
import com.ijudge.sacijudge.Utils;
import com.ijudge.sacijudge.mapmodels.ContestantRatingMapModel;
import com.ijudge.sacijudge.mapmodels.CriteriaMapModel;
import com.ijudge.sacijudge.models.CriteriaModel;
import com.ijudge.sacijudge.views.CriteriaRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContestantRatingActivity extends AppCompatActivity {
    String contestantId,eventId,judgeId;
    ArrayList<CriteriaModel> criteriaModels = new ArrayList<>();
    TextView contestantName;
    DatabaseReference mDatabase;
    CriteriaRecyclerViewAdapter criteriaRecyclerViewAdapter;
    RecyclerView criteriaList;
    DatabaseReference mdatabase;
    Context context;
    ConstraintLayout container;
    TextView done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestant_rating);
        container = (ConstraintLayout) findViewById(R.id.container);
        contestantName = (TextView) findViewById(R.id.contestantName);
        criteriaList = (RecyclerView) findViewById(R.id.criteriaList);
        done = (TextView) findViewById(R.id.done);

        mdatabase = FirebaseDatabase.getInstance().getReference();
        contestantId = getIntent().getExtras().getString("contestantId");
        eventId = getIntent().getExtras().getString("eventId");
        judgeId = getIntent().getExtras().getString("judgeId");
        context = ContestantRatingActivity.this;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Utils.candidates()).child(eventId).child(contestantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ContestantMapModel contestantMapModel = dataSnapshot.getValue(ContestantMapModel.class);
            contestantName.setText(contestantMapModel.contestantname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        criteriaRecyclerViewAdapter = new CriteriaRecyclerViewAdapter(ContestantRatingActivity.this,criteriaModels,contestantId,judgeId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ContestantRatingActivity.this);
        criteriaList.setLayoutManager(layoutManager);
        criteriaList.setAdapter(criteriaRecyclerViewAdapter);
        getCriteria();
        criteriaRecyclerViewAdapter.setOnItemClickListener(new CriteriaRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, CriteriaModel criteriaModel) {
                rateContestantDialog(criteriaModel.getCriteriaName(),criteriaModel.getCriteriaKey());
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getCriteria(){

        mdatabase.child(Utils.critiria()).child(eventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                criteriaModels.clear();
                Utils.callToast(context,dataSnapshot.toString());
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    CriteriaMapModel criteriaMapModel = dataSnapshot1.getValue(CriteriaMapModel.class);
                    CriteriaModel criteriaModel = new CriteriaModel();
                    criteriaModel.setCriteriaName(criteriaMapModel.criteriaName);
                    criteriaModel.setEventKey(criteriaMapModel.eventKey);
                    criteriaModel.setCriteriaKey(criteriaMapModel.criteriaKey);
                    criteriaModel.setCriteriaPercentage(criteriaMapModel.criteriaPercentage);

                    criteriaModels.add(criteriaModel);
                }
                criteriaRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void rateContestantDialog(String criteriaName, final String criteriaId){
        final String eventId,judgeId,rating,contestantId;

        final Dialog dialog = new Dialog(ContestantRatingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.rating_dialog);
        TextView nameCriteria = (TextView) dialog.findViewById(R.id.criteriaName);
        nameCriteria.setText(criteriaName);
        final TextView inputRating = (TextView) dialog.findViewById(R.id.inputRating);

        final TextView[] inputNumber = new TextView[11];

        int textInputId[] = {R.id.zero,R.id.one,R.id.two,R.id.three,R.id.four,R.id.five,R.id.six,R.id.seven,R.id.eight,R.id.nine,R.id.ten};
        for (int i = 0;i<textInputId.length;i++){
            final int index = i;
            inputNumber[i] = (TextView) dialog.findViewById(textInputId[i]);
            inputNumber[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    inputRating.setText(getText(inputNumber[index]));
                }
            });
        }
        ImageView checkButton = (ImageView) dialog.findViewById(R.id.done);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!inputRating.getText().toString().equals("")){
                   Utils.callToast(context,inputRating.getContext().toString());
                   submitContestantRating(inputRating.getText().toString(),criteriaId);
                   dialog.dismiss();
               }
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private String getText(TextView textView){
        return textView.getText().toString();
    }
    private void submitContestantRating(String rating,
                                        String criteriaId){
        ContestantRatingMapModel contestantRatingMapModel = new ContestantRatingMapModel(
                contestantId,eventId,judgeId,rating,criteriaId
        );

        Map<String,Object> profileValue = contestantRatingMapModel.toMap();
        Map<String,Object> childupdates = new HashMap<>();
        childupdates.put(criteriaId,profileValue);
        mdatabase.child(Utils.ratings()).child("event"+eventId).child("contestant"+contestantId).child("judge"+judgeId).updateChildren(childupdates).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.popup(container,e.toString());
            }
        });

    }
}
