package com.rokid.bean.response.responseinfo.action.confirm;

import com.rokid.bean.response.responseinfo.action.BaseActionBean;

import java.util.List;

/**
 * Defines the Confirm content for confirm request
 * <p>
 * Author: xupan.shi
 * Version: V0.1 2017/3/9
 */
public class ConfirmBean extends BaseActionBean{

    private String confirmIntent;
    private String confirmSlot;
    private List<String> optionWords;

    public String getConfirmIntent() {
        return confirmIntent;
    }

    public void setConfirmIntent(String confirmIntent) {
        this.confirmIntent = confirmIntent;
    }

    public String getConfirmSlot() {
        return confirmSlot;
    }

    public void setConfirmSlot(String confirmSlot) {
        this.confirmSlot = confirmSlot;
    }

    public List<String> getOptionWords() {
        return optionWords;
    }

    public void setOptionWords(List<String> optionWords) {
        this.optionWords = optionWords;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}


