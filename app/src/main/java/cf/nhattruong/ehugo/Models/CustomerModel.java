package cf.nhattruong.ehugo.Models;
import com.google.gson.annotations.SerializedName;

public class CustomerModel {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    private String name;

    @SerializedName("user_id")
    private int user_id;

//    @SerializedName("date_of_birth")
//    private String date_of_birth;

    @SerializedName("tel")
    private String tel;

    @SerializedName( "address" )
    private String address;

    public CustomerModel(int id, String name, int user_id, String tel, String address) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
//        this.date_of_birth = date_of_birth;
        this.tel = tel;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

//    public String getDate_of_birth() {
//        return date_of_birth;
//    }
//
//    public void setDate_of_birth(String date_of_birth) {
//        this.date_of_birth = date_of_birth;
//    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
