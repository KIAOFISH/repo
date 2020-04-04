package com.teamalpha.bloodpals.data.bloodbanks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BloodBanksApiResponse {

    @SerializedName("help")
    @Expose
    private String help;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("result")
    @Expose
    private Result result;

    public BloodBanksApiResponse(String help, boolean success, Result result) {
        this.help = help;
        this.success = success;
        this.result = result;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
