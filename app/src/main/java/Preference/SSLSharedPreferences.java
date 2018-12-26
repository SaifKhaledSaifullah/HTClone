package Preference;

import android.content.Context;
import android.content.SharedPreferences;

import Utils.AppConfig;

public class SSLSharedPreferences {

    private static SSLSharedPreferences myPreferences;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private SSLSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConfig.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public static SSLSharedPreferences getSharedPreferences(Context context) {
        if (myPreferences == null) myPreferences = new SSLSharedPreferences(context);
        return myPreferences;
    }

    public void setUserEmail(String userEmail){
        editor.putString(AppConfig.USER_EMAIL, userEmail);
        editor.apply();
    }

    public String getUserEmail(){
        //if no data is available for Config.USER_NAME then this getString() method returns
        //a default value that is mentioned in second parameter
        return sharedPreferences.getString(AppConfig.USER_EMAIL, "");
    }

    public void setLoginState(boolean isLoggedIn){
        editor.putBoolean(AppConfig.IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean getLogInState(){
        return sharedPreferences.getBoolean(AppConfig.IS_LOGGED_IN, false); //assume the default value is false
    }

}
