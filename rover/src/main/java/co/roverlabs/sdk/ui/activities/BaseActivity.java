package co.roverlabs.sdk.ui.activities;

import android.app.Activity;

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
//        if (RoverUtils.isRoverServiceRunning(this)){
//            stopService(new Intent(getApplication(), RoverService.class));
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (!RoverUtils.isRoverServiceRunning(this) && shouldStartService){
//            Intent intent = new Intent(getApplication(), RoverService.class);
//            intent.putExtra(EXTRA_HEAD_ICON_ID, mHeadIconId);
//            startService(new Intent(getApplication(), RoverService.class));
//        }
    }

    public boolean shouldStartService() {
        return shouldStartService;
    }

    public void setShouldStartService(boolean shouldStartService) {
        this.shouldStartService = shouldStartService;
    }
}
