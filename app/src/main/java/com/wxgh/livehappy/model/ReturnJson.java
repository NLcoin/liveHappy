package com.wxgh.livehappy.model;

import java.util.List;

/**
 * Created by 98016 on 2016/7/13 0013.
 */
public class ReturnJson {

    /**
     * error : 200
     * users : [{"Usersinfoid":"1","UsersinfoName":"1","UsersinfoPhone":"1","UsersinfoPhoto":"1","UsersinfoSex":"1","Usersinfo_Age":"1","Zan":"1","UserSignature":"1"}]
     */

    private int error;

    private List<Users> users;//用户信息


    //------------------------------------GET/SET--------------------------------

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

}
