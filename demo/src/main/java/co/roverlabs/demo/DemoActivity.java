package co.roverlabs.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import co.roverlabs.sdk.Rover;


public class DemoActivity extends ActionBarActivity {
    
    public static final String TAG = DemoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        if(!Rover.isSetUp() /* TODO: Change temp fix */) {
            Rover.getInstance(this.getApplicationContext()).setUuid("647086E7-89A6-439C-9E3B-4A2268F13FC6");
            Rover.getInstance(this.getApplicationContext()).setAppId("d1309e050df0a1fa2abc0eb3023f69ad7543fb8dce64d16d6f6f45719da7c923");
            Rover.getInstance(this.getApplicationContext()).setNotificationIconId(R.drawable.icon);
            Rover.getInstance(this.getApplicationContext()).startMonitoring();
        }
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
