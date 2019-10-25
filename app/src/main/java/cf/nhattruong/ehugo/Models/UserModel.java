package cf.nhattruong.ehugo.Models;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    public int user_id;

    @SerializedName("name")
    public String user_name;

    @SerializedName("email")
    public String user_email;

    @SerializedName("created_at")
    public String created_at;

    public UserModel(int user_id, String user_name, String user_email, String created_at) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.created_at = created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
