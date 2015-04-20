package co.roverlabs.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.models.RoverCustomer;


public class DemoActivity extends ActionBarActivity {
    
    public static final String TAG = DemoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        Rover rover = Rover.getInstance(this.getApplicationContext());

        RoverCustomer roverCustomer = new RoverCustomer();
        roverCustomer.setName("Sherry Yang");
        roverCustomer.setEmail("sherry@roverlabs.co");
        roverCustomer.addTraits("gender", "female");

        RoverConfigs roverConfigs = new RoverConfigs();
        roverConfigs.setUuid("7931D3AA-299B-4A12-9FCC-D66F2C5D2462");
        roverConfigs.setAppId("eae9edb6352b8fec6618d3d9cb96f2e795e1c2df1ad5388af807b05d8dfcd7d6");
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(R.drawable.icon);
        roverConfigs.setSandBoxMode(false);

        rover.setCustomer(roverCustomer);
        rover.setConfigurations(roverConfigs);

        rover.startMonitoring();
    }
}
