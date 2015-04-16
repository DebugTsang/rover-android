package co.roverlabs.demo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.models.RoverCustomer;


public class DemoActivity extends Activity {
    
    public static final String TAG = DemoActivity.class.getName();
    private final String SHARED_PREFS_NAME = "RoverDemoPrefs";
    private final String UI_STATE = "UI State";
    private String mState;
    private TextView mMonitorTextView;
    private TextView mResetTextView;
    private TextView mReadyTextView;
    private TextView mHiTextView;
    private TextView mIdView;
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPhoneEditText;
    private Button mSaveButton;
    private Button mNewButton;
    private Button mResetButton;
    private Button mMonitorStartButton;
    private Button mMonitorStopButton;
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
        mResetTextView = (TextView)findViewById(R.id.reset_text_view);
        mHiTextView = (TextView)findViewById(R.id.hi_text_view);
        mIdView = (TextView)findViewById(R.id.customer_id_text);
        mReadyTextView = (TextView)findViewById(R.id.ready_text_view);

        //EditText
        mNameEditText = (EditText)findViewById(R.id.customer_name_edit_text);
        mEmailEditText = (EditText)findViewById(R.id.customer_email_edit_text);
        mPhoneEditText = (EditText)findViewById(R.id.customer_phone_edit_text);

        //Button
        mSaveButton = (Button)findViewById(R.id.save_customer_button);
        mNewButton = (Button)findViewById(R.id.new_customer_button);
        mResetButton = (Button)findViewById(R.id.reset_button);
        mMonitorStartButton = (Button)findViewById(R.id.monitor_start_button);
        mMonitorStopButton = (Button)findViewById(R.id.monitor_stop_button);

        setButtonOnClickListeners();
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

        mMonitorStartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startMonitoring();
            }
        });

        mMonitorStopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                stopMonitoring();
            }
        });
    }

    public void showNewCustomerUI() {

        mState = "NEW";

        //mIdView.setText("ID: " + mRover.getCustomerId());
        mNameEditText.setText("");
        mEmailEditText.setText("");
        mPhoneEditText.setText("");

        mNameEditText.setVisibility(View.VISIBLE);
        mEmailEditText.setVisibility(View.VISIBLE);
        mPhoneEditText.setVisibility(View.VISIBLE);

        mIdView.setVisibility(View.GONE);
        mMonitorTextView.setVisibility(View.GONE);
        mHiTextView.setVisibility(View.GONE);
        mReadyTextView.setVisibility(View.GONE);
        mResetTextView.setVisibility(View.GONE);

        mMonitorStartButton.setVisibility(View.GONE);
        mMonitorStopButton.setVisibility(View.GONE);
        mSaveButton.setVisibility(View.VISIBLE);
        mNewButton.setVisibility(View.GONE);
        mResetButton.setVisibility(View.GONE);
    }

    public void showReadyUI() {

        mState = "READY";

        mIdView.setText("ID: " + mRover.getCustomerId());
        mHiTextView.setText("Hi, " + mCustomer.getName());

        mNameEditText.setVisibility(View.GONE);
        mEmailEditText.setVisibility(View.GONE);
        mPhoneEditText.setVisibility(View.GONE);

        mIdView.setVisibility(View.VISIBLE);
        mMonitorTextView.setVisibility(View.GONE);
        mHiTextView.setVisibility(View.VISIBLE);
        mReadyTextView.setVisibility(View.VISIBLE);
        mResetTextView.setVisibility(View.GONE);

        mMonitorStartButton.setVisibility(View.VISIBLE);
        mMonitorStopButton.setVisibility(View.GONE);
        mSaveButton.setVisibility(View.GONE);
        mNewButton.setVisibility(View.VISIBLE);
        mResetButton.setVisibility(View.GONE);
    }

    public void showResetUI() {

        mState = "RESET";

        mIdView.setText("ID: " + mRover.getCustomerId());
        mHiTextView.setText("Hi, " + mCustomer.getName());

        mNameEditText.setVisibility(View.GONE);
        mEmailEditText.setVisibility(View.GONE);
        mPhoneEditText.setVisibility(View.GONE);

        mIdView.setVisibility(View.VISIBLE);
        mMonitorTextView.setVisibility(View.GONE);
        mHiTextView.setVisibility(View.VISIBLE);
        mReadyTextView.setVisibility(View.GONE);
        mResetTextView.setVisibility(View.VISIBLE);

        mMonitorStartButton.setVisibility(View.VISIBLE);
        mMonitorStopButton.setVisibility(View.GONE);
        mSaveButton.setVisibility(View.GONE);
        mNewButton.setVisibility(View.VISIBLE);
        mResetButton.setVisibility(View.GONE);
    }

    public void showMonitorUI(boolean monitoring) {

        mIdView.setText("ID: " + mRover.getCustomerId());
        mHiTextView.setText("Hi, " + mCustomer.getName());

        if(monitoring) {
            mState = "MONITORING";
            mMonitorTextView.setText(R.string.monitoring_started_message);
            mMonitorStartButton.setVisibility(View.GONE);
            mMonitorStopButton.setVisibility(View.VISIBLE);
            mResetButton.setVisibility(View.GONE);
        }
        else {
            mState = "NOT_MONITORING";
            mMonitorTextView.setText(R.string.monitoring_stopped_message);
            mMonitorStartButton.setVisibility(View.VISIBLE);
            mMonitorStopButton.setVisibility(View.GONE);
            mResetButton.setVisibility(View.VISIBLE);
        }

        mIdView.setVisibility(View.VISIBLE);
        mMonitorTextView.setVisibility(View.VISIBLE);
        mHiTextView.setVisibility(View.VISIBLE);
        mReadyTextView.setVisibility(View.GONE);
        mResetTextView.setVisibility(View.GONE);

        mNameEditText.setVisibility(View.GONE);
        mEmailEditText.setVisibility(View.GONE);
        mPhoneEditText.setVisibility(View.GONE);

        mSaveButton.setVisibility(View.GONE);
        mNewButton.setVisibility(View.VISIBLE);
    }

    public void saveCustomer() {

        boolean invalid = false;
        View focusView = null;
        String name = mNameEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String phone = mPhoneEditText.getText().toString();

        if(TextUtils.isEmpty(name)) {
            mNameEditText.setError(getString(R.string.error_field_required));
            focusView = mNameEditText;
            invalid = true;
        }

        if(TextUtils.isEmpty(email)) {
            mEmailEditText.setError(getString(R.string.error_field_required));
            focusView = mEmailEditText;
            invalid = true;
        }

        if(invalid) {
            focusView.requestFocus();
        }
        else {
            hideKeyboard();
            mCustomer = new RoverCustomer();
            mCustomer.setId(mRover.createCustomerId());
            mCustomer.setName(name);
            mCustomer.setEmail(email);
            if(!TextUtils.isEmpty(phone)) {
                mCustomer.addTraits("phone", phone);
            }
            setRover();
            showReadyUI();
        }
    }

    public void createNewCustomer() {

        mRover.stopMonitoring();
        mRover.reset();
        showNewCustomerUI();
    }

    public void reset() {

        mRover.resetVisit();
        showReadyUI();
    }

    public void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void startMonitoring() {

        mRover.startMonitoring();
        showMonitorUI(true);
    }

    public void stopMonitoring() {

        mRover.stopMonitoring();
        showMonitorUI(false);
    }

    public void setRover() {

        if(mRover == null) {
            mRover = Rover.getInstance(getApplicationContext());
        }

        RoverConfigs roverConfigs = new RoverConfigs();
        roverConfigs.setUuid("7931D3AA-299B-4A12-9FCC-D66F2C5D2462");
        roverConfigs.setAppId("eae9edb6352b8fec6618d3d9cb96f2e795e1c2df1ad5388af807b05d8dfcd7d6");
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(R.drawable.icon);
        roverConfigs.setSandBoxMode(true);

        mRover.setCustomer(mCustomer);
        mRover.setConfigurations(roverConfigs);
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
            if(mState.equals("READY")) {
                showReadyUI();
            }
            else if(mState.equals("RESET")) {
                showResetUI();
            }
            else if(mState.equals("MONITORING")) {
                showMonitorUI(true);
            }
            else if(mState.equals("NOT_MONITORING")) {
                showMonitorUI(false);
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
