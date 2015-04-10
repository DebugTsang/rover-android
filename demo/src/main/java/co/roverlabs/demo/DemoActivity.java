package co.roverlabs.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.RoverConfigs;


public class DemoActivity extends ActionBarActivity {
    
    public static final String TAG = DemoActivity.class.getName();
    private Rover mRover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mRover = Rover.getInstance(this.getApplicationContext());

        //Production
        //UUID - "7931D3AA-299B-4A12-9FCC-D66F2C5D2462"
        //App ID - "eae9edb6352b8fec6618d3d9cb96f2e795e1c2df1ad5388af807b05d8dfcd7d6"

        mRover.setCustomerName("Sherry Yang");
        mRover.setCustomerEmail("sherry@roverlabs.co");
        Map<String, Object> customerTraits = new HashMap<>();
        customerTraits.put("gender", "female");
        mRover.setCustomerTraits(customerTraits);

        RoverConfigs roverConfigs = new RoverConfigs();
        roverConfigs.setUuid("F352DB29-6A05-4EA2-A356-9BFAC2BB3316");
        roverConfigs.setAppId("ff259b8f81ba2a2fd227445e2b3dbaca3e9552ff1663fa3f46e89a284bc9aaa0");
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(R.drawable.icon);
        roverConfigs.setSandBoxMode(false);

        mRover.setConfigurations(roverConfigs);
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
