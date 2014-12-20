package cz.flexer.webapiexample.model;

import com.google.gson.annotations.Expose;

/**
 * Created by secho on 13.12.14.
 */
public class EncryptedMessage {

    @Expose
    private String session;

    @Expose
    private String data;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LockerRegToken{" +
                "session='" + session + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
