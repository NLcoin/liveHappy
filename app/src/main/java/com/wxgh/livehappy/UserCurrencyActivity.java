package com.wxgh.livehappy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.wxgh.livehappy.adapter.MyFansAdapter;
import com.wxgh.livehappy.app.MyApplication;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.ConstantManger;
import com.wxgh.livehappy.utils.StaticManger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserCurrencyActivity extends AppCompatActivity {
    private Users users;
    private List<Users> lists;
    private MyFansAdapter myfansAdapter;
    private ListView lv_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_currency);
        users = StaticManger.getCurrentUser(this);
        lv_list= (ListView) findViewById(R.id.lv_list);
        initData();

//        对应的行布局  item_user_currency_layout
//        imageview控件的图片说明：plusfollow加关注  cancelfollow取消关注
    }

    private void initData() {
        lists=new ArrayList<Users>();
        int i=getIntent().getIntExtra("typyFriend",-1);
        String url="";
        if (i==1){
            url = ConstantManger.SERVER_IP + ConstantManger.SELECTRELATIONSHIPBYID;
        }else if (i==2){
            url = ConstantManger.SERVER_IP + ConstantManger.SELECTRELATIONSHIPBYFRID;
        }else{
            url = ConstantManger.SERVER_IP + ConstantManger.SELECTMYFRIEND;
        }
        RequestBody requestBody = new FormBody.Builder().add("Uid", users.getUsersinfoid()).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(0);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject a = new JSONObject(json);
                    String error = a.getString("error");
                    if ("200".equals(error)) {//成功
                        Message message=new Message();
                        message.what=2;
                        message.obj=json;
                        handler.sendMessage(message);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(MyApplication.getContext(), "网络异常！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    break;
                case 2:
                    String json= (String) msg.obj;
                    try {
                        JSONObject a = new JSONObject(json);
                        JSONArray jsonArray = a.getJSONArray("list");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Users us=new Users();
                            us.setUsersinfoid(object.getString("Usersinfoid"));
                            us.setUsersinfoName(object.getString("UsersinfoName"));
                            us.setUsersinfoPhone(object.getString("UsersinfoPhone"));
                            us.setUsersinfoPhoto(object.getString("UsersinfoPhoto"));
                            us.setUsersinfoSex(object.getString("UsersinfoSex"));
                            us.setUsersinfo_Age(object.getString("Usersinfo_Age"));
                            us.setZan(object.getString("Zan"));
                            us.setUserSignature(object.getString("UserSignature"));
                            lists.add(us);
                        }
                        if (lists!=null)
                        myfansAdapter=new MyFansAdapter(UserCurrencyActivity.this,lists,R.layout.item_user_currency_layout);
                        lv_list.setAdapter(myfansAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
