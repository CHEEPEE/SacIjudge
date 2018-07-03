package com.ijudge.sacijudge.models;

public class ContestantModel {
    private String contestantId;
    private String eventId;
    private String contestantName;
    private String contestantDescription;

    public String getContestantId(){
        return contestantId;
    }
    public String getEventId(){
        return eventId;
    }
    public String getContestantName(){
        return contestantName;
    }
    public String getContestantDescription(){
        return contestantDescription;
    }
    public void setContestantId(String contestantId){
        this.contestantId = contestantId;
    }
    public void setEventId(String eventId){
        this.eventId = eventId;
    }
    public void setContestantName(String contestantName){
        this.contestantName = contestantName;
    }
    public void setContestantDescription(String contestantDescription){
        this.contestantDescription = contestantDescription;
    }
}
