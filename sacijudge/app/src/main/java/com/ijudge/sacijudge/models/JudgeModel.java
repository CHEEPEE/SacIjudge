package com.ijudge.sacijudge.models;

public class JudgeModel {
    String eventid;
    String judgeDescription;
    String judgeId;
    String judgeName;

    public String getEventid(){
        return eventid;
    }
    public String getJudgeDescription(){
        return judgeDescription;
    }
    public String getJudgeId(){
        return judgeId;
    }
    public String getJudgeName(){
        return judgeName;
    }
    public void setEventid(String eventid){
        this.eventid = eventid;
    }
    public void setJudgeDescription(String judgeDescription){
        this.judgeDescription = judgeDescription;
    }
    public void setJudgeId(String judgeId){
        this.judgeId = judgeId;
    }
    public void setJudgeName(String judgeName){
        this.judgeName = judgeName;
    }
}
