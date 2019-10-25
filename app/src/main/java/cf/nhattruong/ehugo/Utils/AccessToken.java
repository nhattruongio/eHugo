package cf.nhattruong.ehugo.Utils;

import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
