package com.ijudge.sacijudge.mapmodels;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CriteriaMapModel {
    public String criteriaKey,criteriaName, criteriaPercentage,eventKey;


    public CriteriaMapModel(){

    }
    public CriteriaMapModel(String criteriaKey, String criteriaName, String criteriaPercentage, String eventKey){
        this.criteriaKey = criteriaKey;
        this.criteriaName = criteriaName;
        this.criteriaPercentage = criteriaPercentage;
        this.eventKey = eventKey;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();

        result.put("criteriaKey",criteriaKey);
        result.put("criteriaName",criteriaName);
        result.put("criteriaPercentage", criteriaPercentage);
        result.put("eventKey",eventKey);

        return result;
    }


}
