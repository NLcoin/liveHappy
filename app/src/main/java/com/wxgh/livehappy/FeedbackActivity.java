package com.wxgh.livehappy;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
 * 反馈页面
 */
public class FeedbackActivity extends AppCompatActivity {
    private TextView txt_submit;
    private EditText txt_context;
    private Users user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        user = StaticManger.getCurrentUser(this);
        txt_submit= (TextView) findViewById(R.id.txt_submit);
        txt_context= (EditText) findViewById(R.id.txt_context);
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String context=txt_context.getText().toString();
                if ("".equals(context) || context.length() > 120) {
                    Toast.makeText(FeedbackActivity.this, "反馈内容不能为空并少于120个汉子！", Toast.LENGTH_SHORT).show();
                    return;
                }
                StaticManger.showProgressDialog(FeedbackActivity.this);
                feedback(context);

            }
        });
    }

    private void feedback(String context){
        String url = ConstantManger.SERVER_IP + ConstantManger.INSERTFEEDBACK;
        RequestBody requestBody = new FormBody.Builder().add("Uid", user.getUsersinfoid()).add("Content",context).build();
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
                        handler.sendEmptyMessage(2);
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
            StaticManger.destroyDialog();
            switch (msg.what) {
                case 0:
                    Toast.makeText(MyApplication.getContext(), "网络异常！", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(MyApplication.getContext(), "网络异常！", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(MyApplication.getContext(), "反馈成功！", Toast.LENGTH_LONG).show();
                    txt_context.setText("");
                    break;
            }
        }
    };
}
