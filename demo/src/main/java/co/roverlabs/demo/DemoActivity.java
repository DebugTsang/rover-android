package co.roverlabs.demo;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
            Rover.getInstance(this.getApplicationContext()).setUuid("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
            Rover.getInstance(this.getApplicationContext()).setAppId("ff259b8f81ba2a2fd227445e2b3dbaca3e9552ff1663fa3f46e89a284bc9aaa0");
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
