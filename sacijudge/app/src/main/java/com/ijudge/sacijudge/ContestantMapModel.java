package com.ijudge.sacijudge;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ContestantMapModel {
    public String contestantid;
    public String eventid;
    public String contestantname;
    public String contestantDescription;


    public ContestantMapModel(){

    }
    public ContestantMapModel(String contestantId, String eventId, String contestantName, String contestantDescription){
        this.contestantid = contestantId;
        this.eventid = eventId;
        this.contestantname = contestantName;
        this.contestantDescription = contestantDescription;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("contestantid",contestantid);
        result.put("eventid",eventid);
        result.put("contestantname",contestantname);
        result.put("contestantDescription",contestantDescription);

        return result;
    }


}
