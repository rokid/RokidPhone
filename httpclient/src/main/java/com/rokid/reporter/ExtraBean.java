package com.rokid.reporter;

import com.rokid.bean.base.BaseBean;

/**
 * Created by fanfeng on 2017/6/29.
 */

public class ExtraBean extends BaseBean {

    private MediaExtraBean media;
    private VoiceExtraBean voice;

    public MediaExtraBean getMedia() {
        return media;
    }

    public void setMedia(MediaExtraBean media) {
        this.media = media;
    }

    public VoiceExtraBean getVoice() {
        return voice;
    }

    public void setVoice(VoiceExtraBean voice) {
        this.voice = voice;
    }

    public static class MediaExtraBean extends BaseExtraBean {

        public MediaExtraBean(String state) {
            super(state);
        }

        public MediaExtraBean(String state, String itemId) {
            super(state, itemId);
        }

        public MediaExtraBean(String state, String progress, String duration) {
            super(state, progress, duration);
        }

        public MediaExtraBean(String state, String token, String progress, String duration) {
            super(state, token, progress, duration);
        }

        public MediaExtraBean(String state, String itemId, String token, String progress, String duration) {
            super(state, itemId, token, progress, duration);
        }
    }


    public static class VoiceExtraBean extends BaseExtraBean {

        public VoiceExtraBean(String state) {
            super(state);
        }

        public VoiceExtraBean(String state, String itemId) {
            super(state, itemId);
        }
    }

    public static abstract class BaseExtraBean extends BaseBean{
        private String state;
        private String itemId;
        private String token;
        private String progress;
        private String duration;

        public BaseExtraBean(String state) {
            this.state = state;
        }

        public BaseExtraBean(String state, String itemId) {
            this.state = state;
            this.itemId = itemId;
        }

        public BaseExtraBean(String state, String progress, String duration) {
            this.state = state;
            this.progress = progress;
            this.duration = duration;
        }

        public BaseExtraBean(String state, String token, String progress, String duration) {
            this.state = state;
            this.token = token;
            this.progress = progress;
            this.duration = duration;
        }

        public BaseExtraBean(String state, String itemId, String token, String progress, String duration) {
            this.state = state;
            this.itemId = itemId;
            this.token = token;
            this.progress = progress;
            this.duration = duration;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }


}
