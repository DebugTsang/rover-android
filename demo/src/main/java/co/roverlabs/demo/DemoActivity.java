package co.roverlabs.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import net.hockeyapp.android.CrashManager;

import co.roverlabs.sdk.Config;
import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.model.Customer;


public class DemoActivity extends ActionBarActivity {
    
    public static final String TAG = DemoActivity.class.getName();
    private TextView beaconsInRangeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        checkForCrashes();

        final Rover rover = Rover.getInstance(this.getApplicationContext());

        Customer roverCustomer = new Customer();
        //roverCustomer.setName("Sherry Yang");
        //roverCustomer.setEmail("sherry@roverlabs.co");
        //roverCustomer.addTraits("gender", "female");

        Config roverConfigs = new Config();
        //Sean's account
        roverConfigs.setUuid("7931D3AA-299B-4A12-9FCC-D66F2C5D2462");
        roverConfigs.setAppId("eae9edb6352b8fec6618d3d9cb96f2e795e1c2df1ad5388af807b05d8dfcd7d6");
        //Personal account for testing
//        roverConfigs.setUuid("F352DB29-6A05-4EA2-A356-9BFAC2BB3316");
//        roverConfigs.setAppId("ff259b8f81ba2a2fd227445e2b3dbaca3e9552ff1663fa3f46e89a284bc9aaa0");
        //roverConfigs.setUuid("B435CE53-9EC2-4B20-99D1-070FEF1CDA2E");
        //roverConfigs.setAppId("2d0ca52a350d03b6dcce601088bcdfbb");
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(R.drawable.icon);
        roverConfigs.setRoverHeadIconId(R.drawable.rover);
        roverConfigs.setSandBoxMode(false);

        //rover.setCustomer(roverCustomer);
        rover.with(roverConfigs).startMonitoring();

//        //TODO: Remove after testing
//        Button showCardsButton = (Button)findViewById(R.id.show_cards_button);
//        showCardsButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                rover.showCards();
//            }
//        });
//
//        //TODO: Remove after testing
//        Button simulateButton = (Button)findViewById(R.id.simulate_button);
//        simulateButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                rover.simulate();
//            }
//        });
//
//        //TODO: Remove after testing
//        RoverEventBus.getInstance().register(this);
//        beaconsInRangeText = (TextView)findViewById(R.id.beacons_in_range);

    }

//    @Subscribe
//    public void getBeaconsInRange(RoverRangeResultEvent event) {
//
//        beaconsInRangeText.setText(event.displayBeaconsInRange());
//    }

    private void checkForCrashes() {
        CrashManager.register(this, "2c84af4bc65b042f529a3d1bb97e1dc8");
    }
}
