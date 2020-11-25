
package com.android.mytaskseat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestModel {

    @SerializedName("auth_key")
    @Expose
    private Integer authKey;
    @SerializedName("password")
    @Expose
    private Integer password;

    public Integer getAuthKey() {
        return authKey;
    }

    public void setAuthKey(Integer authKey) {
        this.authKey = authKey;
    }

    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }

}
