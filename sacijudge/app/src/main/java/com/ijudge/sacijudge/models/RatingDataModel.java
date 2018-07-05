package com.ijudge.sacijudge.models;

public class RatingDataModel {
    private String contestantid;
    private String eventid;
    private String judgeId;
    private String rating;
    private String criteriaId;

    public String getContestantid() {
        return contestantid;
    }

    public String getCriteriaId() {
        return criteriaId;
    }

    public String getEventid() {
        return eventid;
    }

    public String getJudgeId() {
        return judgeId;
    }

    public String getRating() {
        return rating;
    }

    public void setContestantid(String contestantid) {
        this.contestantid = contestantid;
    }

    public void setCriteriaId(String criteriaId) {
        this.criteriaId = criteriaId;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public void setJudgeId(String judgeId) {
        this.judgeId = judgeId;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
