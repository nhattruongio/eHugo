package cf.nhattruong.ehugo.Models;

import com.google.gson.annotations.SerializedName;

public class DealModel {
    @SerializedName("id")
    public int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("room_id")
    private int room_id;

    @SerializedName("customer_id")
    private  int customer_id;

    @SerializedName("date_out")
    private  String date_out;

    @SerializedName("description")
    private  String description;

    @SerializedName("down_payment")
    private float down_payment;

    @SerializedName( "total_payment" )
    private float total_payment;

    @SerializedName("name")
    private  String name;

    @SerializedName("date_of_birth")
    private  String date_of_birth;

    @SerializedName("address")
    private String address;

    @SerializedName("tel")
    private String tel;

    public DealModel(int id, int user_id, int room_id, int customer_id, String date_out, String description, float down_payment, float total_payment, String name, String date_of_birth, String address, String tel) {
        this.id = id;
        this.user_id = user_id;
        this.room_id = room_id;
        this.customer_id = customer_id;
        this.date_out = date_out;
        this.description = description;
        this.down_payment = down_payment;
        this.total_payment = total_payment;
        this.name = name;
        this.date_of_birth = date_of_birth;
        this.address = address;
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getDate_out() {
        return date_out;
    }

    public void setDate_out(String date_out) {
        this.date_out = date_out;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDown_payment() {
        return down_payment;
    }

    public void setDown_payment(float down_payment) {
        this.down_payment = down_payment;
    }

    public float getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(float total_payment) {
        this.total_payment = total_payment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
