package com.wxgh.livehappy.utils;

/**
 * Created by 98016 on 2016/7/13 0013.
 */
public class ConstantManger {
    public static final String SERVER_IP = "http://123.206.45.56:8080/LiveMusic/";//项目地址
    public static final String THIRD_PARTY_LOGIN = "ThirdPartyLogin.ssm";//第三方登录 参数：Access_token=1& Openid=1& ThirdPartyType=1&appid=1
    public static final String INSERT_USERS_INFO = "insertUsersInfo.ssm";//手机号密码注册 参数：? Phone =1&Password =1
    public static final String SELECT_BY_PHONE = "selectByPhone.ssm";//根据手机号查询用户信息   参数：?UserPhone =1
    public static final String IMG_FILE = "ImgFile";//服务器地址存储路径   参数：?UserPhone =1&PassWord =1
    public static final String USER_LOGIN = "userLogin.ssm";//手机密码登陆   参数：?UserPhone =1&PassWord =1
    public static final String UPDATE_IMG="addShareImage.ssm";//单图片上传
    public static final String UPDATEUSERSINFOPICPATH="updateUsersInfo.ssm";//修改个人信息
    public static final String SELECTEARNINGSBYTODAY="selectEarningsByToday.ssm"; //今日账单
}
