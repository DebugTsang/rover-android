package co.roverlabs.sdk.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.squareup.otto.Subscribe;

import java.util.List;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.RoverService;
import co.roverlabs.sdk.events.RoverCardsAddedEvent;
import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.managers.RoverVisitManager;
import co.roverlabs.sdk.models.RoverCard;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-03-03.
 */
public class CardListActivity extends Activity {

    public static final String TAG = CardListActivity.class.getSimpleName();
    private RecyclerView mCardListRecyclerView;
    private LinearLayoutManager mCardListLayoutManager;
    private CardListAdapter mCardListAdapter;
    private String mLatestVisitId;
    private List<RoverCard> mLatestCards;
    private Button mNewCardButton;
    private Animation mSlideIn;
    private Animation mSlidOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list);

        RoverEventBus.getInstance().register(this);



        //stopService(new Intent(getApplication(), ChatHeadService.class));

        mCardListRecyclerView = (RecyclerView)findViewById(R.id.card_list_recycler_view);
        mNewCardButton = (Button)findViewById(R.id.new_card_button);
        mSlideIn = AnimationUtils.loadAnimation(this, R.anim.button_slide_in);
        mSlidOut = AnimationUtils.loadAnimation(this, R.anim.button_slide_out);

        mCardListLayoutManager = new LinearLayoutManager(this);
        mCardListLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCardListRecyclerView.setLayoutManager(mCardListLayoutManager);

        mLatestVisitId = RoverVisitManager.getInstance(getApplicationContext()).getLatestVisit().getId();
        mLatestCards = RoverVisitManager.getInstance(getApplicationContext()).getLatestVisit().getAccumulatedCards();

        if(mLatestCards.isEmpty()) {
            String launchActivityName = ((RoverConfigs)RoverUtils.readObjectFromSharedPrefs(getApplicationContext(), RoverConfigs.class, null)).getLaunchActivityName();
            try {
                Intent intent = new Intent(this, Class.forName(launchActivityName));
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            catch (ClassNotFoundException e) {
                Log.e(TAG, "Cannot launch application - cannot find launch activity name", e);
            }
            finish();
        }

        mCardListAdapter = new CardListAdapter(mLatestVisitId, mLatestCards, this);
        mCardListRecyclerView.setAdapter(mCardListAdapter);

        mCardListRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                if (mCardListLayoutManager.findFirstVisibleItemPosition() == 0) {
                    if(mNewCardButton.getVisibility() == View.VISIBLE) {
                        mNewCardButton.startAnimation(mSlidOut);
                        mNewCardButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        mNewCardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mCardListRecyclerView.smoothScrollToPosition(0);
                mNewCardButton.startAnimation(mSlidOut);
                mNewCardButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Subscribe
    public void onCardsAdded(RoverCardsAddedEvent event) {

        List<RoverCard> cards = event.getAddedCards();

        for(RoverCard card : cards) {
            mLatestCards.add(0, card);
            mCardListAdapter.notifyItemInserted(0);
        }

        mNewCardButton.setVisibility(View.VISIBLE);
        mNewCardButton.startAnimation(mSlideIn);
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (isRoverServiceRunning()){
            stopService(new Intent(getApplication(), RoverService.class));
        }
        //RoverEventBus.getInstance().post(new RoverNotificationEvent(RoverConstants.NOTIFICATION_ACTION_CANCEL));
    }

    @Override
    protected void onStop() {

        super.onStop();
        mNewCardButton.setVisibility(View.INVISIBLE);
        if (!isRoverServiceRunning()){
            startService(new Intent(getApplication(), RoverService.class));

        }
        //RoverEventBus.getInstance().post(new RoverNotificationEvent(RoverConstants.NOTIFICATION_ACTION_CANCEL));
    }


    private boolean isRoverServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (RoverService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
