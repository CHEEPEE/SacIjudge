package com.ijudge.sacijudge.models;

public class CriteriaModel {
    private String criteriaKey;
    private String criteriaName;
    private String criteriaPercentage;
    private String eventKey;

    public String getCriteriaKey() {
        return criteriaKey;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public String getCriteriaPercentage() {
        return criteriaPercentage;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setCriteriaKey(String criteriaKey) {
        this.criteriaKey = criteriaKey;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public void setCriteriaPercentage(String criteriaPercentage) {
        this.criteriaPercentage = criteriaPercentage;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

}

