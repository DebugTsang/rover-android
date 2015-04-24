package co.roverlabs.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.models.RoverCustomer;


public class DemoActivity extends ActionBarActivity {
    
    public static final String TAG = DemoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Rover rover = Rover.getInstance(this.getApplicationContext());

        RoverCustomer roverCustomer = new RoverCustomer();
        roverCustomer.setName("Sherry Yang");
        roverCustomer.setEmail("sherry@roverlabs.co");
        roverCustomer.addTraits("gender", "female");

        RoverConfigs roverConfigs = new RoverConfigs();
        //Sean's account
//        roverConfigs.setUuid("7931D3AA-299B-4A12-9FCC-D66F2C5D2462");
//        roverConfigs.setAppId("eae9edb6352b8fec6618d3d9cb96f2e795e1c2df1ad5388af807b05d8dfcd7d6");
        //Personal account for testing
        roverConfigs.setUuid("F352DB29-6A05-4EA2-A356-9BFAC2BB3316");
        roverConfigs.setAppId("ff259b8f81ba2a2fd227445e2b3dbaca3e9552ff1663fa3f46e89a284bc9aaa0");
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(R.drawable.icon);
        roverConfigs.setSandBoxMode(false);

        rover.setCustomer(roverCustomer);
        rover.setConfigurations(roverConfigs);

        rover.startMonitoring();

        //TODO: Remove after testing
        Button showCardsButton = (Button)findViewById(R.id.show_cards_button);

        showCardsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rover.showCards();
            }
        });
    }
}
