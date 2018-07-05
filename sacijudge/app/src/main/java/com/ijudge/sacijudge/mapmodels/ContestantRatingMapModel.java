package com.ijudge.sacijudge.mapmodels;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ContestantRatingMapModel {
    public String contestantid;
    public String eventid;
    public String judgeId;
    public String rating;
    public String criteriaId;




    public ContestantRatingMapModel(){

    }
    public ContestantRatingMapModel(String contestantId,
                                    String eventid, String judgeId,
                                    String rating, String criteriaId
    ){
        this.contestantid = contestantId;
        this.eventid = eventid;
        this.judgeId = judgeId;
        this.rating = rating;
        this.criteriaId = criteriaId;



    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("contestantid",contestantid);
        result.put("eventid",eventid);
        result.put("judgeId",judgeId);
        result.put("rating",rating);
        result.put("criteriaId",criteriaId);



        return result;
    }
}
