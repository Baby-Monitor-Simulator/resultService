package com.babymonitor.resultService.model;
import org.springframework.data.annotation.Id;

//@Document(collection = "result")
public class Result {
    @Id
    private String id;
    private String result;
    private int session;
    private int user;

    public Result() {}

    public Result( String result, int user, int session) {
        this.user = user;
        this.session = session;
        this.result = result;
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

    public int getUser() {
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

    public void setUser(int user) {
        this.user = user;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
