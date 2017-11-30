package com.rokid.voicerec;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.rokid.speech.Speech;
import com.rokid.speech.SpeechCallback;

import java.io.InputStream;

public class VoiceRecognize implements Runnable {
	public void initialize(Context ctx, Callback cb, SpeechCallback scb) {
		_context = ctx;
		_callback = cb;
		_speech_callback = scb;
		init_sdk();
		createAudioRecord();

		_working = true;
		_turn_on_record = new BooleanObject(false);
		_recording = new BooleanObject(false);
		_thread = new Thread(this);
		// ensure thread running
		synchronized (_thread) {
			_thread.start();
			try {
				_thread.wait();
			} catch (Exception e) {
			}
		}
	}

	public void startRecognize() {
		if (_audio_record == null)
			return;
		synchronized (_turn_on_record) {
			_turn_on_record.value = true;
			_turn_on_record.notify();
		}
		synchronized (_recording) {
			while (!_recording.value) {
				try {
					_recording.wait();
				} catch (Exception e) {
				}
			}
		}
	}

	public void stopRecognize() {
		if (_audio_record == null)
			return;
		_turn_on_record.value = false;
		synchronized (_recording) {
			while (_recording.value) {
				try {
					_recording.wait();
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void run() {
		synchronized (_thread) {
			_thread.notify();
		}

		byte[] audio_data = new byte[audio_data_buf_size_];
		int id;
		int count;
		while (_working) {
			synchronized (_turn_on_record) {
				if (!_turn_on_record.value) {
					try {
						Log.i(tag_, "audio record thread: wait for startRecognize");
						_turn_on_record.wait();
						Log.i(tag_, "audio record thread: awake");
					} catch (Exception e) {
					}
					continue;
				}
			}

			synchronized (_recording) {
				_recording.value = true;
				_recording.notify();
			}

			try {
				_audio_record.startRecording();
				id = _speech.startVoice(_speech_callback);
				while (_turn_on_record.value) {
					count = _audio_record.read(audio_data, 0, audio_data_buf_size_);
					Log.i(tag_, "AudioRecord record " + count + " bytes");
					if (count < 0) {
						Log.w(tag_, "AudioRecord.read failed: " + count);
						if (_callback != null)
							_callback.onError();
						return;
					} else if (count > 0) {
						_speech.putVoice(id, audio_data, 0, count);
					}
				}
				_speech.endVoice(id);
				_audio_record.stop();
			} catch (Exception e) {
				e.printStackTrace();
				if (_callback != null)
					_callback.onError();
				break;
			}

			synchronized (_recording) {
				_recording.value = false;
				_recording.notify();
			}
		}
	}

	private void init_sdk() {
		_speech = new Speech();
		AssetManager am = _context.getAssets();
		InputStream is = null;
		
		try {
			is = am.open("speech_config.json");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		_speech.prepare(is);
		try {
			is.close();
		} catch (Exception e) {
		}
	}

	private void createAudioRecord() {
		// for android sdk 19
		int size = AudioRecord.getMinBufferSize(sample_rate_,
				channel_config_, audio_encoding_);
		size *= 2;
		_audio_record = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION,
				sample_rate_, channel_config_, audio_encoding_, size);
		/** for android sdk 23
		AudioFormat audio_format = new AudioFormat.Builder()
			.setEncoding(audio_encoding_)
			.setSampleRate(sample_rate_)
			.setChannelMask(channel_config_)
			.build();
		int size = AudioRecord.getMinBufferSize(sample_rate_,
				channel_config_, audio_encoding_);
		size *= 2;
		_audio_record = new AudioRecord.Builder()
			.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
			.setAudioFormat(audio_format)
			.setBufferSizeInBytes(size)
			.build();
		*/
	}

	private Context _context;
	private Thread _thread;
	private Speech _speech;
	private AudioRecord _audio_record;
	private Callback _callback;
	private SpeechCallback _speech_callback;
	private boolean _working;
	private BooleanObject _turn_on_record;
	private BooleanObject _recording;

	private static final int sample_rate_ = 16000;
	private static final int channel_config_ = AudioFormat.CHANNEL_IN_MONO;
	private static final int audio_encoding_ = AudioFormat.ENCODING_PCM_16BIT;
	private static final int audio_data_buf_size_ = 24 * 1024;
	private static final String tag_ = "SpeechDemo.Recognize";

	public static interface Callback {
		void onError();
	}

	private static class BooleanObject {
		public boolean value;

		public BooleanObject(boolean v) {
			value = v;
		}
	}
}
