package co.roverlabs.sdk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.managers.RoverVisitManager;
import co.roverlabs.sdk.models.RoverVisit;

/**
 * Created by SherryYang on 2015-02-04.
 */
public class CardActivity extends Activity {
    
    public static final String TAG = CardActivity.class.getSimpleName();
    private RoverVisitManager mVisitManager;
    private TextView mTitleView;
    private TextView mMessageView;
    private Button mButton;
    private RoverVisit mLatestVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        
        mVisitManager = RoverVisitManager.getInstance(this.getApplicationContext());
        
        mTitleView = (TextView)findViewById(R.id.cardTitle);
        mMessageView = (TextView)findViewById(R.id.cardMessage);
        mButton = (Button)findViewById(R.id.cardButton);
        
        mLatestVisit = mVisitManager.getLatestVisit();
        mTitleView.setText(mLatestVisit.getTouchpoints().get(0).getCards().get(0).getTitle());
        mMessageView.setText("This is card ID " + mLatestVisit.getTouchpoints().get(0).getCards().get(0).getId());
        mButton.setText("OK");
        
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        
        super.onResume();
        mLatestVisit = mVisitManager.getLatestVisit();
    }
}
