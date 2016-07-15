package com.wxgh.livehappy;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wxgh.livehappy.app.MyApplication;
import com.wxgh.livehappy.model.Users;
import com.wxgh.livehappy.utils.ConstantManger;
import com.wxgh.livehappy.utils.StaticManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 钱包
 */
public class WalletActivity extends AppCompatActivity implements View.OnClickListener{
    private Users user = null;
    private TextView balance;//余额
    private TextView txt_income;//今日收入
    private TextView txt_spending;//今日支出

    private ImageView imageButton2;//返回按钮
    private TextView create; //完成
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        user = StaticManger.getCurrentUser(this);
        initView();
        initData();
    }
    private void initView() {
        balance= (TextView) findViewById(R.id.balance);
        txt_income= (TextView) findViewById(R.id.txt_income);
        txt_spending= (TextView) findViewById(R.id.txt_spending);
        balance.setText(user.getBalance());
        create = (TextView) findViewById(R.id.create);
        imageButton2 = (ImageView) findViewById(R.id.imageButton2);
        create.setText("账单");
        imageButton2.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    private void initData() {
        String url = ConstantManger.SERVER_IP + ConstantManger.SELECTEARNINGSBYTODAY;
        RequestBody requestBody = new FormBody.Builder().add("UserID", user.getUsersinfoid()).build();
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
                    Toast.makeText(MyApplication.getContext(), "网络异常！", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(MyApplication.getContext(), "网络异常！", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    String json= (String) msg.obj;
                    try {
                        JSONObject a = new JSONObject(json);
                        String income = a.getString("TodayIncome");
                        String spending = a.getString("TodayConsumption");
                        String balance1 = a.getString("Balance");
                        balance.setText(balance1+" 元");
                        txt_income.setText(income+" 元");
                        txt_spending.setText(spending+" 元");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageButton2:
                finish();
                break;
            case R.id.create:
                break;
        }
    }
}
