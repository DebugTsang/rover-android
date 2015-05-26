package co.roverlabs.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.events.RoverRangeResultEvent;
import co.roverlabs.sdk.models.RoverCustomer;


public class DemoActivity extends ActionBarActivity {
    
    public static final String TAG = DemoActivity.class.getName();
    private TextView beaconsInRangeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Rover rover = Rover.getInstance(this.getApplicationContext());

        RoverCustomer roverCustomer = new RoverCustomer();
        //roverCustomer.setName("Sherry Yang");
        //roverCustomer.setEmail("sherry@roverlabs.co");
        //roverCustomer.addTraits("gender", "female");

        RoverConfigs roverConfigs = new RoverConfigs();
        //Sean's account
        //roverConfigs.setUuid("ED5CF03C-4DB2-4794-B56E-C5F61DE05C69");
        //roverConfigs.setAppId("d1a3969e600bd72dfa3d8fd0054a5909");
        //Personal account for testing
        roverConfigs.setUuid("F352DB29-6A05-4EA2-A356-9BFAC2BB3316");
        roverConfigs.setAppId("ff259b8f81ba2a2fd227445e2b3dbaca3e9552ff1663fa3f46e89a284bc9aaa0");
        //roverConfigs.setUuid("B435CE53-9EC2-4B20-99D1-070FEF1CDA2E");
        //roverConfigs.setAppId("2d0ca52a350d03b6dcce601088bcdfbb");
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(R.drawable.icon);
        roverConfigs.setRoverHeadIconId(R.drawable.rover);
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

        //TODO: Remove after testing
        Button simulateButton = (Button)findViewById(R.id.simulate_button);
        simulateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rover.simulate();
            }
        });

        //TODO: Remove after testing
        RoverEventBus.getInstance().register(this);
        beaconsInRangeText = (TextView)findViewById(R.id.beacons_in_range);

    }

    @Subscribe
    public void getBeaconsInRange(RoverRangeResultEvent event) {

        beaconsInRangeText.setText(event.displayBeaconsInRange());
    }
}
