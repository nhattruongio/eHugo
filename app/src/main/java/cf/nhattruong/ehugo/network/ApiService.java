package cf.nhattruong.ehugo.network;

import java.util.ArrayList;

import cf.nhattruong.ehugo.Models.CustomerModel;
import cf.nhattruong.ehugo.Models.DealModel;
import cf.nhattruong.ehugo.Utils.AccessToken;
import cf.nhattruong.ehugo.Models.HouseModel;
import cf.nhattruong.ehugo.Models.Message;
import cf.nhattruong.ehugo.Models.RoomModel;
import cf.nhattruong.ehugo.Models.UserModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(
            @Field("refresh_token") String refreshToken
    );

    @POST("get_data_user")
    @FormUrlEncoded
    Call<UserModel> get_data_user(
            @Field("token") String token
    );


    @POST("list_house")
    @FormUrlEncoded
    Call<ArrayList<HouseModel>> list_houses(
            @Field("token") String token
    );

    @POST("cre_house")
    @FormUrlEncoded
    Call<HouseModel> cre_house(
            @Field("name") String name,
            @Field("address") String address,
            @Field("quantity") String quantity,
            @Field("description") String description,
            @Field("token") String token
    );

    @POST("list_room")
    @FormUrlEncoded
    Call<ArrayList<RoomModel>> list_room(
            @Field("token") String token,
            @Field("house_id") Integer house_id
    );

    @POST("cre_room")
    @FormUrlEncoded
    Call<RoomModel> cre_room(
            @Field("name") String name,
            @Field("price") float price,
            @Field("acreage") float acreage,
            @Field("description") String description,
            @Field("house_id") Integer house_id,
            @Field("token") String token
    );


    @POST("list_customer")
    @FormUrlEncoded
    Call<ArrayList<CustomerModel>> list_customer(
            @Field("token") String token
    );


    @POST("get_once_room")
    @FormUrlEncoded
    Call<RoomModel> get_once_room(
            @Field("room_id") String room_id,
            @Field("token") String token
    );

    @POST("check-status")
    @FormUrlEncoded
    Call<Message> check_status(@Field("token") String token);


    @POST("cre_deal")
    @FormUrlEncoded
    Call<DealModel> cre_deal(
            @Field("room_id") Integer room_id,
            @Field("name") String name,
            @Field("date_of_birth") String date_of_birth,
            @Field("date_out") String date_out,
            @Field("description") String description,
            @Field("address") String address,
            @Field("tel") String tel,
            @Field("down_payment") Float down_payment,
            @Field("total_payment") Float total_payment,
            @Field("token") String token
    );

    @POST("show_customer")
    @FormUrlEncoded
    Call<CustomerModel> show_customer(
            @Field("name") String name,
            @Field("date_of_birth") String date_of_birth,
            @Field("address") String address,
            @Field("tel") String tel,
            @Field("token") String token
    );

    @FormUrlEncoded
    Call<CustomerModel> show_customer_inroom(
            @Field("room_id") Integer room_id,
            @Field("token") String token
    );

}
