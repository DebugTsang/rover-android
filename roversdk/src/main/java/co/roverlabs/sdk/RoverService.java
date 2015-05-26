package co.roverlabs.sdk;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import co.roverlabs.sdk.ui.activity.BaseActivity;
import co.roverlabs.sdk.ui.activity.CardListActivity;


public class RoverService extends Service {

	private WindowManager windowManager;
	private ImageView roverHead;
	WindowManager.LayoutParams params;

	@Override
	public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);



		roverHead = new ImageView(this);


		params= new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;
		
		//this code is for dragging the chat head
		roverHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private boolean isMoving;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isMoving = false;
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!isMoving) {
                            Intent dialogIntent = new Intent(RoverService.this, CardListActivity.class);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialogIntent);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:

                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);


//                        if (params.y > 800) {
//                            RoverService.this.stopSelf();
//                        } else {
                            windowManager.updateViewLayout(roverHead, params);
//                        }
                        isMoving = Math.abs((params.y - initialY) + (params.x - initialX)) > 5;

                        return true;

                }
                return false;
            }
        });

		windowManager.addView(roverHead, params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (roverHead != null) {
            windowManager.removeView(roverHead);
        }
	}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null){
            return super.onStartCommand(intent, flags, startId);
        }

        int headIconId = intent.getIntExtra(BaseActivity.EXTRA_HEAD_ICON_ID, -1);
        if (headIconId > 0){
            roverHead.setImageResource(headIconId);
        }else{
            roverHead.setBackgroundResource(R.drawable.rover_head_appear);
            roverHead.setVisibility(View.VISIBLE);
            AnimationDrawable headAnimation = (AnimationDrawable) roverHead.getBackground();
            headAnimation.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
