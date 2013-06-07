package com.example.video;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.VideoView;

public class Video extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		VideoView video = (VideoView) findViewById(R.id.video);
		video.setVideoPath("/data/samplevideo.mp4");
		video.start();
	}

	

}
