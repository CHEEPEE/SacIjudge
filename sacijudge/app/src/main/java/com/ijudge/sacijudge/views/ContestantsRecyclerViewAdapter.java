package com.ijudge.sacijudge.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ijudge.sacijudge.R;
import com.ijudge.sacijudge.models.ContestantModel;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class ContestantsRecyclerViewAdapter extends RecyclerView.Adapter<ContestantsRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<ContestantModel> contestantsModel;
    private Context context;
    private ConstraintLayout container;



    public class MyViewHolder extends RecyclerView.ViewHolder{

       public TextView contestantName,contestantDescription;


        public MyViewHolder(View view){
            super(view);
            contestantName = (TextView) view.findViewById(R.id.contestant_name);
            contestantDescription = (TextView) view.findViewById(R.id.contestant_description);
            container = (ConstraintLayout) view.findViewById(R.id.container);

        }
    }

    public ContestantsRecyclerViewAdapter(Context c, ArrayList<ContestantModel> contestantModel){
        this.contestantsModel = contestantModel;
        this.context =c;
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


