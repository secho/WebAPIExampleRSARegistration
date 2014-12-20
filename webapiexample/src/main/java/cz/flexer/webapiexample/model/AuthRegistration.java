package cz.flexer.webapiexample.model;


import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by secho on 12.12.14.
 */
public class AuthRegistration {

    @Expose
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AuthRegistration{" +
                "data='" + data + '\'' +
                '}';
    }
}
