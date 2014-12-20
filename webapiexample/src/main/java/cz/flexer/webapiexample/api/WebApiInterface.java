package cz.flexer.webapiexample.api;


import cz.flexer.webapiexample.model.AuthRegistration;
import cz.flexer.webapiexample.model.EncryptedMessage;
import cz.flexer.webapiexample.model.KeyResponse;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;


public interface WebApiInterface {

//    @GET("/admin/key/{webapikey}/locker")
//    KeyResponse keyHolder(@Path("webapikey") String webapikey);

    @GET("/admin/keys/{webapikey}/locker")
    void getLockerKey(@Path("webapikey") String webapikey,@Header("BasicAuth") String authorization,
                      Callback<KeyResponse> cb);

//    @POST("/locker")
//    AuthRegistration authChallenge(@Body EncryptedMessage encryptedMessage);

    @POST("/locker")
    void postAuthRegistration(@Body EncryptedMessage encryptedMessage,
                              @Header("WEB-API-KEY") String webapikey,
                              Callback<AuthRegistration> cb);


}
