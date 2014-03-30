package com.smak.mygdxgame;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AndroidApplication implements IActivityRequestHandler   {
	
	  protected AdView adView;

	    private final int SHOW_ADS = 1;
	    private final int HIDE_ADS = 0;

	    protected Handler handler = new Handler()
	    {
	        @Override
	        public void handleMessage(Message msg) {
	            switch(msg.what) {
	                case SHOW_ADS:
	                {
	                    adView.setVisibility(View.VISIBLE);
	                    break;
	                }
	                case HIDE_ADS:
	                {
	                    adView.setVisibility(View.GONE);
	                    break;
	                }
	            }
	        }
	    };
	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        cfg.useCompass = false;
        cfg.useAccelerometer =  false;
     
        RelativeLayout layout = new RelativeLayout(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View gameView = initializeForView(new SmakTetsGame(this), false);
        
        RelativeLayout.LayoutParams gameViewParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        gameViewParams.bottomMargin = 1;
        
        layout.addView(gameView,gameViewParams);

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("INSERT_YOUR_ADMOB_ID");  //  <= insert admob id

        RelativeLayout.LayoutParams adParams =   new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        adParams.addRule(RelativeLayout.ABOVE);

        layout.addView(adView, adParams);

        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
        .build();
 
        adView.loadAd(adRequest);

        setContentView(layout);
    }

	@Override
	public void showAds(boolean show) {
		   handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);		
	}
	
	
	  @Override
	  public void onResume() {
	    super.onResume();
	    if (adView != null) {
	      adView.resume();
	    }
	  }

	  @Override
	  public void onPause() {
	    if (adView != null) {
	      adView.pause();
	    }
	    super.onPause();
	  }

	  @Override
	  public void onDestroy() {
	    if (adView != null) {
	      adView.destroy();
	    }
	    super.onDestroy();
	  }

}