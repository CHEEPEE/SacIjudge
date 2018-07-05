package com.ijudge.sacijudge.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ijudge.sacijudge.R;
import com.ijudge.sacijudge.Utils;
import com.ijudge.sacijudge.mapmodels.ContestantMapModel;
import com.ijudge.sacijudge.mapmodels.ContestantRatingMapModel;
import com.ijudge.sacijudge.mapmodels.CriteriaMapModel;
import com.ijudge.sacijudge.models.ContestantModel;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class ContestantsRecyclerViewAdapter extends RecyclerView.Adapter<ContestantsRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<ContestantModel> contestantsModel;
    private Context context;
    private ConstraintLayout container;
    private String judgeId;



    public class MyViewHolder extends RecyclerView.ViewHolder{

       public TextView contestantName,contestantDescription,judgeId;


        public MyViewHolder(View view){
            super(view);
            contestantName = (TextView) view.findViewById(R.id.contestant_name);
            contestantDescription = (TextView) view.findViewById(R.id.contestant_description);
            container = (ConstraintLayout) view.findViewById(R.id.container);

        }
    }

    public ContestantsRecyclerViewAdapter(Context c, ArrayList<ContestantModel> contestantModel,String judgeId){
        this.contestantsModel = contestantModel;
        this.context = c;
        this.judgeId = judgeId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contestant_item,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ContestantModel contestantModelPos = contestantsModel.get(position);
        holder.contestantName.setText(contestantModelPos.getContestantName());
        holder.contestantDescription.setText(contestantModelPos.getContestantDescription());
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(v,position,contestantModelPos);
            }
        });
        FirebaseDatabase.getInstance().getReference().child(Utils.ratings())
                .child("event"+contestantModelPos.getEventId())
                .child("contestant"+contestantModelPos.getContestantId())
                .child("judge"+judgeId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Integer> total = new ArrayList<>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    final ContestantRatingMapModel contestantRatingMapModel = dataSnapshot1.getValue(ContestantRatingMapModel.class);
                    try {
                        FirebaseDatabase.getInstance().getReference()
                                .child(Utils.critiria()).child(contestantRatingMapModel.eventid)
                                .child(contestantRatingMapModel.criteriaId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        CriteriaMapModel criteriaMapModel = dataSnapshot.getValue(CriteriaMapModel.class);
                                        total.add(Integer.parseInt(criteriaMapModel.criteriaPercentage)
                                                *Integer.parseInt(contestantRatingMapModel.rating));
                                        int t = 0;
                                        for (int i = 0;i<total.size();i++){
                                            t+=total.get(i);
                                        }
                                        holder.contestantDescription.setText((t*.1)+" %");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        System.out.print(databaseError.toString());
                                    }
                                });
                    }catch (NullPointerException e){
                        System.out.print(e.toString());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return contestantsModel.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position,ContestantModel contestantModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


