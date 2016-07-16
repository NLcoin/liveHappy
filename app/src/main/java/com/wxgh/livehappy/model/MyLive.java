package com.wxgh.livehappy.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class MyLive {
    public String error;
    public List<Model> list;

    public class Model {
        public String LiveID;
        public String liveTitle;
        public String BroadcastAddress;
        public String UserCount;
        public String UserID;
        public String UserName;
        public String PicPath;
        public String UserSignature;
        public String Zan;
    }
}
