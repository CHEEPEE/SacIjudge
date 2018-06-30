package com.ijudge.sacijudge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 19/01/2018.
 */

public class ContestantsRecyclerViewAdapter extends RecyclerView.Adapter<ContestantsRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<ContestantModel> contestantsModel;
    private Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder{

       public TextView contestantName,contestantDescription;


        public MyViewHolder(View view){
            super(view);
            contestantName = (TextView) view.findViewById(R.id.contestant_name);
            contestantDescription = (TextView) view.findViewById(R.id.contestant_description);

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
        ContestantModel contestantModel = contestantsModel.get(position);
        holder.contestantName.setText(contestantModel.getContestantName());
        holder.contestantDescription.setText(contestantModel.getContestantDescription());

    }

    @Override
    public int getItemCount() {
        return contestantsModel.size();
    }
}


