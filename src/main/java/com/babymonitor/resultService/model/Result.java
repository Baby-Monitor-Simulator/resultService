package com.babymonitor.resultService.model;
import org.springframework.data.annotation.Id;

import java.util.UUID;

//@Document(collection = "result")
public class Result {
    @Id
    private String id;
    private String result;
    private int session;
    private UUID user;
    private SimType simType;

    public Result() {}

    public Result( String result, UUID user, int session, SimType simType) {
        this.user = user;
        this.session = session;
        this.result = result;
        this.simType = simType;
    }

    @Override
    public String toString() {
        return String.format(
                "result:[id=%s, result='%s', user='%s', session='%s']",
                id, result, user, session);
    }

    public String getId() {
        return id;
    }

    public UUID getUser() {
        return user;
    }

    public int getSession() {
        return session;
    }

    public String getResult() {
        return result;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public SimType getSimType() {
        return simType;
    }

    public void setSimType(SimType simType) {
        this.simType = simType;
    }
}
