package co.roverlabs.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.models.RoverCustomer;


public class DemoActivity extends ActionBarActivity {
    
    public static final String TAG = DemoActivity.class.getName();
    private final String SHARED_PREFS_NAME = "RoverDemoPrefs";
    private final String UI_STATE = "UI State";
    private String mState;
    private TextView mMonitorTextView;
    private TextView mHiTextView;
    private TextView mIdView;
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPhoneEditText;
    private Button mSaveButton;
    private Button mNewButton;
    private Button mResetButton;
    private Rover mRover;
    private RoverCustomer mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        if(mRover == null) {
            mRover = Rover.getInstance(getApplicationContext());
        }

        mCustomer = mRover.getCustomer();

        //TextView
        mMonitorTextView = (TextView)findViewById(R.id.monitoring_text_view);
        mHiTextView = (TextView)findViewById(R.id.hi_text_view);
        mIdView = (TextView)findViewById(R.id.customer_id_text);

        //EditText
        mNameEditText = (EditText)findViewById(R.id.customer_name_edit_text);
        mEmailEditText = (EditText)findViewById(R.id.customer_email_edit_text);
        mPhoneEditText = (EditText)findViewById(R.id.customer_phone_edit_text);

        //Button
        mSaveButton = (Button)findViewById(R.id.save_customer_button);
        mNewButton = (Button)findViewById(R.id.new_customer_button);
        mResetButton = (Button)findViewById(R.id.reset_button);

        setButtonOnClickListeners();
        showMonitorUI();
        setRover();
    }

    public void setButtonOnClickListeners() {

        mSaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                saveCustomer();
            }
        });

        mNewButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                createNewCustomer();
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reset();
            }
        });
    }

    public void showMonitorUI() {

        mState = "MONITORING";

        mIdView.setText("ID: " + mCustomer.getId());

        if(mCustomer.getName() != null) {
            mHiTextView.setText("Hi, " + mCustomer.getName());
        }
        mMonitorTextView.setText(R.string.monitoring_message);

        mNameEditText.setVisibility(View.GONE);
        mEmailEditText.setVisibility(View.GONE);
        mPhoneEditText.setVisibility(View.GONE);

        mIdView.setVisibility(View.VISIBLE);
        mMonitorTextView.setVisibility(View.VISIBLE);
        mHiTextView.setVisibility(View.VISIBLE);

        mSaveButton.setVisibility(View.GONE);
        mNewButton.setVisibility(View.VISIBLE);
        mResetButton.setVisibility(View.VISIBLE);
    }

    public void showNewCustomerUI() {

        mState = "NEW";

        mIdView.setText("ID: " + mCustomer.getId());

        mNameEditText.setText("");
        mEmailEditText.setText("");
        mPhoneEditText.setText("");

        mNameEditText.setVisibility(View.VISIBLE);
        mEmailEditText.setVisibility(View.VISIBLE);
        mPhoneEditText.setVisibility(View.VISIBLE);

        mIdView.setVisibility(View.VISIBLE);
        mMonitorTextView.setVisibility(View.GONE);
        mHiTextView.setVisibility(View.GONE);

        mSaveButton.setVisibility(View.VISIBLE);
        mNewButton.setVisibility(View.GONE);
        mResetButton.setVisibility(View.GONE);
    }

    public void saveCustomer() {

        mRover.stopMonitoring();
        mRover.resetVisit();

        String name = mNameEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String phone = mPhoneEditText.getText().toString();

        hideKeyboard();

        if(!TextUtils.isEmpty(name)) {
            mCustomer.setName(name);
        }
        if(!TextUtils.isEmpty(email)) {
            mCustomer.setEmail(email);
        }
        if(!TextUtils.isEmpty(phone)) {
            mCustomer.addTraits("phone", phone);
        }

        mRover.setCustomer(mCustomer);
        mRover.startMonitoring();

        showMonitorUI();
    }

    public void createNewCustomer() {

        mRover.stopMonitoring();
        mCustomer = mRover.resetCustomer();
        showNewCustomerUI();
    }

    public void reset() {

        mRover.stopMonitoring();
        mRover.resetVisit();
        Toast.makeText(getApplicationContext(), R.string.reset_message, Toast.LENGTH_LONG).show();
        mRover.startMonitoring();
    }

    public void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setRover() {

        RoverConfigs roverConfigs = new RoverConfigs();
        roverConfigs.setUuid("7931D3AA-299B-4A12-9FCC-D66F2C5D2462");
        roverConfigs.setAppId("eae9edb6352b8fec6618d3d9cb96f2e795e1c2df1ad5388af807b05d8dfcd7d6");
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(R.drawable.icon);
        roverConfigs.setSandBoxMode(false);

        mRover.setCustomer(mCustomer);
        mRover.setConfigurations(roverConfigs);

        mRover.startMonitoring();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString(UI_STATE, mState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        mState = savedInstanceState.getString(UI_STATE);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {

        mState = readStringFromSharedPrefs(getApplicationContext(), UI_STATE, null);

        if(mState != null) {
            if(mState.equals("MONITORING")) {
                showMonitorUI();
            }
            else {
                showNewCustomerUI();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {

        writeStringToSharedPrefs(getApplicationContext(), UI_STATE, mState);
        super.onPause();
    }

    public String readStringFromSharedPrefs(Context con, String key, String defaultValue) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(key, defaultValue);
    }

    public void writeStringToSharedPrefs(Context con, String key, String value) {

        SharedPreferences sharedPrefs = con.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
