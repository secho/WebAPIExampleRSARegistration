package cz.flexer.webapiexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.flexer.webapiexample.api.WebAPIClient;
import cz.flexer.webapiexample.model.AuthRegistration;
import cz.flexer.webapiexample.model.EncryptedMessage;
import cz.flexer.webapiexample.model.KeyResponse;
import cz.flexer.webapiexample.security.Cryptography;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.publicKeyTextView)
    TextView publicKeyTextView;

    private static final int REQUEST_ACTIVITY = 234;

    public static final String WEBAPIKEY =  "23ca15d9-6d69-4d5c-8410-af06d113745c";

    public static final String RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgPLLDjwbTaKNLHoYm2YwttYqzxxxiw" +
                                            "YJ9HJhiepSSNcOuR+kk9UhtLilhVAnih6Ve9J6a65I1rVLTLnyDa/jQVbqj8jZTHqCoAHlVbhT" +
                                            "DXeD4QFQCDUwVg+paARRsf+PYlrt6w9YOUHUuZON+tBFgIEBA8qM31K5PYjtQUk56XggxA8HZV" +
                                            "2h+9McBnipTMIVbA/Yu0zDfa7b8ckbIY9LLxgcN0A9OieZgY9UKOeDnliBb95bQoFwyfTAo65T" +
                                            "Kw49p4Hq3cgkMaOPbMnLWuIuSTopSzFD77YEyocQQQk5ajaJta2Wgwuf824sU7ZljCfJKBNKvO" +
                                            "DMbktPNsf6cgSr+wIDAQAB";

    private String code;
    private String datam1 = "{\"code\":\"";
    private String datam3 = "\", \"password\":\"heslo1\", \"deviceFingerprint\":\"asdfghjkl\", \"scope\":\"/v1/marcus\", \"nonce\": \"";
    private String datam5 = "\"}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(MainActivity.this);
    }

    @OnClick(R.id.buttonGetCode)
    protected void onButtonGetCodeClickHandler() {
        Intent mIntentWebLogin = new Intent(MainActivity.this,ActLoginGetCode.class);
        startActivityForResult(mIntentWebLogin, REQUEST_ACTIVITY);
    }

//    @OnClick(R.id.buttonGetRsa)
//    protected void onButtonGetRsaClickHandler() {
//        getPublicKey();
//    }

    @OnClick(R.id.sendEncMessage)
    protected void onButtonSendEncMessageClickHandler() {
        EncryptedMessage message = null;
        String preparedMessage = datam1 + code +datam3 + String.valueOf(Math.random()) + datam5;
        Log.v("(1) DATA TO BE ENCRYPTED", preparedMessage);
        try {
            message = Cryptography.encryptMessage(RSA_PUBLIC,preparedMessage);
        } catch (Exception e) {

        }

        Log.v("(2) ENCRYPTED MESSAGE",message.getData() + "   " + message.getSession());

        WebAPIClient.getWebAPIClient().postAuthRegistration(message, WEBAPIKEY,new Callback<AuthRegistration>() {

            @Override
            public void success(AuthRegistration authRegistration, Response response) {
                try{
                    Log.v("(2.8) REGISTRATION DATA TO DEC", authRegistration.getData());
                    Log.v("(2.9) SEK TO BE USED FOR DEC", Cryptography.tempSek);
                    Log.v("(3) DECRYPTED OUTPUT", Cryptography.aesDecrypt(authRegistration.getData(),Cryptography.tempSek));
                    JSONObject data = new JSONObject(Cryptography.aesDecrypt(authRegistration.getData(), Cryptography.tempSek));
                    Log.v("(4) ACCESS TOKEN", data.getString("accessToken"));
                    Toast.makeText(MainActivity.this,data.getString("accessToken"),Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.e("Callback Exception", e.getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }



//
//    private void getPublicKey() {
//        WebAPIClient.
//                getWebAPIClient().
//                getLockerKey(webApiKey,basicAuthEncoded, new Callback<KeyResponse>() {
//
//                    @Override
//                    public void success(KeyResponse keyResponse, Response response) {
//                        Log.v("RSA Public Key",String.valueOf(keyResponse.getPublicKey()));
//                        publicKeyTextView.setText(keyResponse.getPublicKey());
//                        publicKey = keyResponse.getPublicKey();
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//
//                    }
//                });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ACTIVITY) {

            switch (resultCode) {
                case RESULT_OK: {
                    String authCode = data.getStringExtra(ActLoginGetCode.EXTRA_SETTINGS_CODE);
                    code = authCode;
                    Toast.makeText(MainActivity.this,code, Toast.LENGTH_SHORT).show();
                    break;
                }
                case RESULT_CANCELED: {
                    break;
                }
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
