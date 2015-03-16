package co.roverlabs.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import co.roverlabs.sdk.Rover;


public class DemoActivity extends ActionBarActivity {
    
    public static final String TAG = DemoActivity.class.getName();
    private Rover mRover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        
        mRover = Rover.getInstance(this);

        //Testing
        //mRover.setUuid("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
        //In office
        mRover.setUuid("647086E7-89A6-439C-9E3B-4A2268F13FC6");
        mRover.setAppId("d1309e050df0a1fa2abc0eb3023f69ad7543fb8dce64d16d6f6f45719da7c923");
        //mRover.setNotificationIconId(R.drawable.icon);
        mRover.completeSetUp();
        mRover.startMonitoring();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
