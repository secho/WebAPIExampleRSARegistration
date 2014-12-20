package cz.flexer.webapiexample.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Student on 12/12/2014.
 */
public class KeyResponse {

    @Expose
    private String apiKey;

    @Expose
    private boolean enabled;

    @Expose
    private boolean otpEnabled;

    @Expose
    private String publicKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isOtpEnabled() {
        return otpEnabled;
    }

    public void setOtpEnabled(boolean otpEnabled) {
        this.otpEnabled = otpEnabled;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "KeyResponse{" +
                "apiKey='" + apiKey + '\'' +
                ", enabled=" + enabled +
                ", otpEnabled=" + otpEnabled +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
