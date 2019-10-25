package cf.nhattruong.ehugo.Utils;

import android.content.SharedPreferences;

import android.annotation.SuppressLint;


public class TokenManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    @SuppressLint("CommitPrefEdits")
    private TokenManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences prefs) {
        if (INSTANCE == null) {
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }

    public void saveToken(AccessToken token) {
        editor.putString("ACCESS_TOKEN", token.getAccessToken()).commit();
    }

    public void deleteToken() {
        editor.remove("ACCESS_TOKEN").commit();
    }

    public AccessToken getToken() {
        AccessToken token = new AccessToken();
        token.setAccessToken(prefs.getString("ACCESS_TOKEN", null));
        return token;
    }
}
