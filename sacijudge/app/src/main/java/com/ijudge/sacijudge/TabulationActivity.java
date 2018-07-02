package com.ijudge.sacijudge;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.SlidingRootNavLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TabulationActivity extends AppCompatActivity {
    RecyclerView rv_contestants;
    String eventId;
    String judgeId;
    TextView contestName;
    ImageView ic_menu;
    ContestantsRecyclerViewAdapter contestantsRecyclerViewAdapter;
    ArrayList<ContestantModel> contestantModelArrayList = new ArrayList<>();
    SlidingRootNav slidingRootNav;
    ImageView menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabulation);
        rv_contestants = (RecyclerView) findViewById(R.id.rv_contestants);
        contestName = (TextView) findViewById(R.id.contestName);
        Bundle bundle = getIntent().getExtras();
        eventId = bundle.getString("eventId");
        judgeId = bundle.getString("judgeID");
        menu = (ImageView) findViewById(R.id.ic_menu);
        FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("eventname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contestName.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withRootViewScale(0.7f)
                .withMenuLayout(R.layout.tabulation_side_nav)
                .withSavedState(savedInstanceState)
                .withContentClickableWhenMenuOpened(true)
                .inject();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slidingRootNav.isMenuOpened()==true){
                    slidingRootNav.closeMenu(true);
                }else {
                    slidingRootNav.openMenu(true);
                }
            }
        });


        contestantsRecyclerViewAdapter = new ContestantsRecyclerViewAdapter(TabulationActivity.this,contestantModelArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TabulationActivity.this);
        rv_contestants.setLayoutManager(layoutManager);
        rv_contestants.setAdapter(contestantsRecyclerViewAdapter);
        FirebaseDatabase.getInstance().getReference().child("candidates").child(eventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    ContestantModel contestantModel = new ContestantModel();
                    ContestantMapModel contestantMapModel = dataSnapshot1.getValue(ContestantMapModel.class);
                    contestantModel.setContestantId(contestantMapModel.contestantid);
                    contestantModel.setContestantName(contestantMapModel.contestantname);
                    contestantModel.setContestantDescription(contestantMapModel.contestantDescription);
                    contestantModel.setEventId(contestantMapModel.eventid);
                    contestantModelArrayList.add(contestantModel);
                }
                contestantsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        confirmExit();

    }

    private void confirmExit(){
        final Dialog dialog = new Dialog(TabulationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.confirm_exit);
        TextView cancel  = (TextView) dialog.findViewById(R.id.cancelbtn);
        TextView logout = (TextView) dialog.findViewById(R.id.logoutBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TabulationActivity.this,AccessContest.class);
                startActivity(i);
                finish();
            }
        });
       /* TextView title = (TextView) dialog.findViewById(R.id.dTitle);
        TextView buttonClose = (TextView) dialog.findViewById(R.id.buttonClose);
        TextView overview = (TextView) dialog.findViewById(R.id.dOverview);
        TextView release_date = (TextView) dialog.findViewById(R.id.release_date);
        release_date.setText("Release Date: "+moviesModel.getRelease_date());
        ImageView dPosterImage = (ImageView) dialog.findViewById(R.id.dPosterImage);
        TextView popularity = (TextView) dialog.findViewById(R.id.popularity);
        TextView vote = (TextView) dialog.findViewById(R.id.vote);
        vote.setText("Average Vote: "+moviesModel.getVote_average()+"/10");
        title.setText(moviesModel.getTitle());
        overview.setText(moviesModel.getOverview());
        popularity.setText("Popularity: "+moviesModel.getPopularity());

        GlideApp.with(context).load(Utils.posterPrePath+moviesModel.getPoster_path()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(dPosterImage);*/

        dialog.show();
        Window window = dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
