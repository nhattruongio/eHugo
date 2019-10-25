package cf.nhattruong.ehugo.Models;

import com.google.gson.annotations.SerializedName;

public class RoomModel {
    @SerializedName("id")
    public int id_room;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("house_id")
    private int house_id;

    @SerializedName("name")
    private String name_room;

    @SerializedName("status")
    private String status_room;

    @SerializedName("price")
    private float price_room;

    @SerializedName("acreage")
    private float acreage;

    @SerializedName("description")
    private String description_room;

    @SerializedName("created_at")
    private String created_at_room;

    public RoomModel(int id_room, int user_id, int house_id, String name_room, String status_room,float acreage, float price_room, String description_room, String created_at_room) {
        this.id_room = id_room;
        this.user_id = user_id;
        this.house_id = house_id;
        this.name_room = name_room;
        this.status_room = status_room;
        this.acreage = acreage;
        this.price_room = price_room;
        this.description_room = description_room;
        this.created_at_room = created_at_room;
    }

    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getHouse_id() {
        return house_id;
    }

    public void setHouse_id(int house_id) {
        this.house_id = house_id;
    }

    public String getName_room() {
        return name_room;
    }

    public void setName_room(String name_room) {
        this.name_room = name_room;
    }

    public String getStatus_room() {
        return status_room;
    }

    public float getAcreage() {
        return acreage;
    }

    public void setAcreage(float acreage) {
        this.acreage = acreage;
    }

    public void setStatus_room(String status_room) {
        this.status_room = status_room;
    }

    public float getPrice_room() {
        return price_room;
    }

    public void setPrice_room(float price_room) {
        this.price_room = price_room;
    }

    public String getDescription_room() {
        return description_room;
    }

    public void setDescription_room(String description_room) {
        this.description_room = description_room;
    }

    public String getCreated_at_room() {
        return created_at_room;
    }

    public void setCreated_at_room(String created_at_room) {
        this.created_at_room = created_at_room;
    }
}
