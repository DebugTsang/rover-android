package co.roverlabs.sdk.ui.activity;

import android.app.Activity;
import android.content.Intent;

import com.squareup.otto.Subscribe;

import co.roverlabs.sdk.RoverService;
import co.roverlabs.sdk.events.RoverVisitExpiredEvent;
import co.roverlabs.sdk.utilities.Utils;

/**
 * Created by arsent on 15-05-22.
 */
public class BaseActivity extends Activity{

    public static final String EXTRA_HEAD_ICON_ID = "co.roverlabs.sdk.ui.activity.CardListActivity:head_icon";
    protected static int mHeadIconId;

    //used to optimizing the service lifecycle
    protected boolean shouldStartService = true;

    @Override
    protected void onResume() {
        super.onResume();
        shouldStartService = true; //reset service optimizer flag
        if (Utils.isRoverServiceRunning(this)){
            stopService(new Intent(getApplication(), RoverService.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!Utils.isRoverServiceRunning(this) && shouldStartService){
            stopService();
        }
    }

    public void setShouldStartService(boolean shouldStartService) {
        this.shouldStartService = shouldStartService;
    }

    protected void stopService(){
        Intent intent = new Intent(getApplication(), RoverService.class);
        intent.putExtra(EXTRA_HEAD_ICON_ID, mHeadIconId);
        startService(new Intent(getApplication(), RoverService.class));
    }


}
