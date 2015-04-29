package co.roverlabs.sdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.otto.Subscribe;

import java.util.List;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.RoverConfigs;
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
    private CardListAdapter mCardListAdapter;
    private List<RoverCard> mLatestCards;
    private Button mNewCardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list);

        RoverEventBus.getInstance().register(this);

        mCardListRecyclerView = (RecyclerView)findViewById(R.id.card_list_recycler_view);
        mNewCardButton = (Button)findViewById(R.id.new_card_button);

        LinearLayoutManager cardListLayoutManager = new LinearLayoutManager(this);
        cardListLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCardListRecyclerView.setLayoutManager(cardListLayoutManager);

        mLatestCards = RoverVisitManager.getInstance(getApplicationContext()).getLatestVisit().getAccumulatedCards();

        if(mLatestCards.isEmpty()) {
            String launchActivityName = ((RoverConfigs) RoverUtils.readObjectFromSharedPrefs(getApplicationContext(), RoverConfigs.class, null)).getLaunchActivityName();
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

        mCardListAdapter = new CardListAdapter(mLatestCards, this);
        mCardListRecyclerView.setAdapter(mCardListAdapter);

        mNewCardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mCardListRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Subscribe
    public void onCardsAdded(RoverCardsAddedEvent event) {

        List<RoverCard> cards = event.getAddedCards();
        for(RoverCard card : cards) {
            mLatestCards.add(0, card);
        }
        mCardListAdapter.notifyDataSetChanged();
        mNewCardButton.setVisibility(View.VISIBLE);
    }

    /*
    @Subscribe
    public void onCardPositionChange(CardPositionChangeEvent event) {

        Log.d(TAG, "The card position is " + event.getPosition());
        if(event.getPosition() != 0) {
            mCardButton.setVisibility(View.VISIBLE);
        }
        else {
            mCardButton.setVisibility(View.GONE);
        }

    }

    public static class CardPositionChangeEvent {

        private int mPosition;

        CardPositionChangeEvent(int position) { mPosition = position; }

        public int getPosition() { return mPosition; }
    }
    */

}
