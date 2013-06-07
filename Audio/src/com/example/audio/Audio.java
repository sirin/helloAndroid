package com.example.audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;

public class Audio extends Activity {
	private MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int resId;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			resId = R.raw.up;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			resId = R.raw.down;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			resId = R.raw.left;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			resId = R.raw.right;
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			resId = R.raw.enter;
			break;
		case KeyEvent.KEYCODE_A:
			resId = R.raw.a;
			break;
		case KeyEvent.KEYCODE_S:
			resId = R.raw.s;
			break;
		case KeyEvent.KEYCODE_D:
			resId = R.raw.d;
			break;
		case KeyEvent.KEYCODE_F:
			resId = R.raw.f;
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		if (mp != null) {
			mp.release();
		}
		mp = MediaPlayer.create(this, resId);
		mp.start();
		
		return true;
	}

}
