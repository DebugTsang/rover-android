package co.roverlabs.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.models.RoverCustomer;


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

        //mRover.setCustomerName("Sherry Yang");
        //mRover.setCustomerEmail("sherry@roverlabs.co");
        //Map<String, Object> customerTraits = new HashMap<>();
        //customerTraits.put("gender", "female");
        //mRover.setCustomerTraits(customerTraits);

        RoverCustomer roverCustomer = new RoverCustomer();
        roverCustomer.setName("Ata Namvari");
        roverCustomer.setEmail("ata@roverlabs.co");
        roverCustomer.addTraits("gender", "male");

        RoverConfigs roverConfigs = new RoverConfigs();
        roverConfigs.setUuid("7931D3AA-299B-4A12-9FCC-D66F2C5D2462");
        roverConfigs.setAppId("eae9edb6352b8fec6618d3d9cb96f2e795e1c2df1ad5388af807b05d8dfcd7d6");
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(R.drawable.icon);
        roverConfigs.setSandBoxMode(false);

        mRover.setCustomer(roverCustomer);
        mRover.setConfigurations(roverConfigs);

        mRover.startMonitoring();
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_demo, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
