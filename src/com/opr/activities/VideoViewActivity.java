package com.opr.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.opr.utils.Appconstants;

public class VideoViewActivity extends Activity {

	// Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
    String propId = "";
 
    // Insert your Video URL
   // String VideoURL = "http://192.168.1.57:8084/OnlinePlotRental/VIDEO/733731.MP4";
    String VideoURL = "http://192.168.1.109:8084/OnlinePlotRental/VIDEO/";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		propId = getIntent().getExtras().getString(Appconstants.PROPERTY_ID);
		//Toast.makeText(getApplicationContext(), propId, Toast.LENGTH_SHORT).show();
		
		StringBuilder builder = new StringBuilder();
		builder.append(VideoURL);
		builder.append(propId);
		builder.append(".MP4");
		
		
		videoview = (VideoView) findViewById(R.id.VideoView);
		
		pDialog = new ProgressDialog(VideoViewActivity.this);
		pDialog.setMessage("Buffering....");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		
		MediaController mediaController = new MediaController(VideoViewActivity.this);
		mediaController.setAnchorView(videoview);
		
		Uri video = Uri.parse(builder.toString());
		videoview.setMediaController(mediaController);
		videoview.setVideoURI(video);
		
		videoview.requestFocus();
		videoview.setOnPreparedListener(new OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });
	}

}
