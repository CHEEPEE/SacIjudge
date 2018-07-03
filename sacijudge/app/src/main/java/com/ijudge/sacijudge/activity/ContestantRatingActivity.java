package com.ijudge.sacijudge.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ijudge.sacijudge.mapmodels.ContestantMapModel;
import com.ijudge.sacijudge.R;
import com.ijudge.sacijudge.Utils;
import com.ijudge.sacijudge.mapmodels.CriteriaMapModel;
import com.ijudge.sacijudge.models.CriteriaModel;
import com.ijudge.sacijudge.views.CriteriaRecyclerViewAdapter;

import java.util.ArrayList;

public class ContestantRatingActivity extends AppCompatActivity {
    String contestantId,eventId;
    ArrayList<CriteriaModel> criteriaModels = new ArrayList<>();
    TextView contestantName;
    DatabaseReference mDatabase;
    CriteriaRecyclerViewAdapter criteriaRecyclerViewAdapter;
    RecyclerView criteriaList;
    DatabaseReference mdatabase;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestant_rating);
        contestantName = (TextView) findViewById(R.id.contestantName);
        criteriaList = (RecyclerView) findViewById(R.id.criteriaList);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        contestantId = getIntent().getExtras().getString("contestantId");
        eventId = getIntent().getExtras().getString("eventId");
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
        criteriaRecyclerViewAdapter = new CriteriaRecyclerViewAdapter(ContestantRatingActivity.this,criteriaModels);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ContestantRatingActivity.this);
        criteriaList.setLayoutManager(layoutManager);
        criteriaList.setAdapter(criteriaRecyclerViewAdapter);
        getCriteria();
        criteriaRecyclerViewAdapter.setOnItemClickListener(new CriteriaRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, CriteriaModel criteriaModel) {
                rateContestantDialog(criteriaModel.getCriteriaName());
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

    private void rateContestantDialog(String criteriaName){
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

        dialog.show();
        Window window = dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private String getText(TextView textView){
        return textView.getText().toString();
    }
}
