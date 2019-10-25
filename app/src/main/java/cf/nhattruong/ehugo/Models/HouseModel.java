package cf.nhattruong.ehugo.Models;

import com.google.gson.annotations.SerializedName;

public class HouseModel {
    @SerializedName("id")
    public int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("name")
    private String name_house;

    @SerializedName("address")
    private String address_house;

    @SerializedName("quantity")
    private String quantity_house;

    @SerializedName("description")
    private String description_house;

    @SerializedName("created_at")
    private String created_at;

    public HouseModel(int id, int user_id, String name_house, String address_house, String quantity_house, String description_house, String created_at) {
        this.id = id;
        this.user_id = user_id;
        this.name_house = name_house;
        this.address_house = address_house;
        this.quantity_house = quantity_house;
        this.description_house = description_house;
        this.created_at = created_at;
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

    public String getName_house() {
        return name_house;
    }

    public void setName_house(String name_house) {
        this.name_house = name_house;
    }

    public String getAddress_house() {
        return address_house;
    }

    public void setAddress_house(String address_house) {
        this.address_house = address_house;
    }

    public String getQuantity_house() {
        return quantity_house;
    }

    public void setQuantity_house(String quantity_house) {
        this.quantity_house = quantity_house;
    }

    public String getDescription_house() {
        return description_house;
    }

    public void setDescription_house(String description_house) {
        this.description_house = description_house;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
