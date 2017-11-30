package com.rokid.voicerec;

import android.os.Parcelable;
import android.os.Parcel;

public class BearKidResult implements Parcelable {
	public int type;
	public int activation;
	public int extype;
	public String asr;
	public String nlp;
	public String action;
	public double energy;
	public double location;

	public BearKidResult() {
	}

	public BearKidResult(Parcel source) {
		type = source.readInt();
		switch (type) {
		case TYPE_LOCATION:
			location = source.readFloat();
			break;
		case TYPE_VOICE_INFO:
			energy = source.readFloat();
			break;
		case TYPE_ACTIVATION:
			activation = source.readInt();
			break;
		case TYPE_INTERMEDIATE:
			asr = source.readString();
			break;
		case TYPE_ASR:
			asr = source.readString();
			break;
		case TYPE_NLP:
			nlp = source.readString();
			action = source.readString();
			break;
		case TYPE_DEACTIVE:
			break;
		case TYPE_EXCEPTION:
			extype = source.readInt();
			break;
		}
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(type);
		switch (type) {
		case TYPE_LOCATION:
			out.writeDouble(location);
			break;
		case TYPE_VOICE_INFO:
			out.writeDouble(energy);
			break;
		case TYPE_ACTIVATION:
			out.writeInt(activation);
			break;
		case TYPE_INTERMEDIATE:
			out.writeString(asr);
			break;
		case TYPE_ASR:
			out.writeString(asr);
			break;
		case TYPE_NLP:
			out.writeString(nlp);
			out.writeString(action);
			break;
		case TYPE_DEACTIVE:
			break;
		case TYPE_EXCEPTION:
			out.writeInt(extype);
			break;
		}
	}

	public static final Creator<BearKidResult> CREATOR = new Creator<BearKidResult>() {
		@Override
		public BearKidResult[] newArray(int size) {
				return new BearKidResult[size];
		}

		@Override
		public BearKidResult createFromParcel(Parcel source) {
				return new BearKidResult(source);
		}
	};

	public static final int TYPE_LOCATION = 0;
	public static final int TYPE_VOICE_INFO = 1;
	public static final int TYPE_ACTIVATION = 2;
	public static final int TYPE_INTERMEDIATE = 3;
	public static final int TYPE_ASR = 4;
	public static final int TYPE_NLP = 5;
	public static final int TYPE_DEACTIVE = 6;
	public static final int TYPE_EXCEPTION = 7;
}
