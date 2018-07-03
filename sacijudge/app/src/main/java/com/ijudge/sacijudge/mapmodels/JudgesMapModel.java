package com.ijudge.sacijudge.mapmodels;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class JudgesMapModel {
    public String eventid;
    public String judgeDescription;
    public String judgeId;
    public String judgeName;
    public String eventKey;
    public String judgeKey;


    public JudgesMapModel(){

    }
    public JudgesMapModel(String eventid, String judgeDescription, String judgeId,String judgeName){
        this.eventid = eventid;
        this.judgeDescription = judgeDescription;
        this.judgeId = judgeId;
        this.judgeName = judgeName;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("eventid",eventid);
        result.put("judgeDescription",judgeDescription);
        result.put("judgeId",judgeId);
        result.put("judgeName",judgeName);



        return result;
    }
    public void setEventKey(String eventKey){
        this.eventKey = eventKey;
    }
    public void setJudgeKey(String judgeKey){
        this.judgeKey = judgeKey;
    }

}
