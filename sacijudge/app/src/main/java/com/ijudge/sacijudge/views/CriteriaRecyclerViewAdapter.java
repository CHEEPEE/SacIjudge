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
import com.ijudge.sacijudge.mapmodels.ContestantRatingMapModel;
import com.ijudge.sacijudge.models.CriteriaModel;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class CriteriaRecyclerViewAdapter extends RecyclerView.Adapter<CriteriaRecyclerViewAdapter.MyViewHolder> {
   private ArrayList<CriteriaModel> criteriaModels = new ArrayList<>();
   private Context context;
   private String contestantId;
   private String judgeId;
   private boolean rate = true;
   private ArrayList<Boolean> validateRatings = new ArrayList<>();



    public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView criteriaName,criteriaPercent,percent;
    ConstraintLayout container;




        public MyViewHolder(View view){
            super(view);
            criteriaName = (TextView) view.findViewById(R.id.criteriaName);
            criteriaPercent = (TextView) view.findViewById(R.id.criteriaPercent);
            container = (ConstraintLayout) view.findViewById(R.id.container);
            percent = (TextView) view.findViewById(R.id.percent);

        }
    }

    public CriteriaRecyclerViewAdapter(Context c, ArrayList<CriteriaModel> criteriaModels,String contestantId,String judgeId){
      this.criteriaModels = criteriaModels;
      this.context = c;
      this.contestantId = contestantId;
      this.judgeId = judgeId;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.criteria_item,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

      /*  container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(v,position,contestantModelPos);
            }
        });*/
      holder.container.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              mOnItemClickLitener.onItemClick(v,position,criteriaModels.get(position));
          }
      });

      holder.criteriaName.setText(criteriaModels.get(position).getCriteriaName());
        FirebaseDatabase.getInstance().getReference()
                .child(Utils.ratings())
                .child("event"+criteriaModels.get(position).getEventKey())
                .child("contestant"+contestantId)
                .child("judge"+judgeId)
                .child(criteriaModels.get(position).getCriteriaKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ContestantRatingMapModel contestantRatingMapModel = dataSnapshot.getValue(ContestantRatingMapModel.class);

               try {
                   holder.criteriaPercent.setText(contestantRatingMapModel.rating);
                   validateRatings.add(true);
               }catch (NullPointerException e){


               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
      holder.percent.setText(criteriaModels.get(position).getCriteriaPercentage()+"%");
    }

    @Override
    public int getItemCount() {
        return criteriaModels.size();
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, CriteriaModel criteriaModel);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public boolean validateRate(){


        return criteriaModels.size()==validateRatings.size();
    }

}


