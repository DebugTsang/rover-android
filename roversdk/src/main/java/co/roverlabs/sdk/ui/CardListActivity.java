package co.roverlabs.sdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.managers.RoverVisitManager;
import co.roverlabs.sdk.models.RoverCard;
import co.roverlabs.sdk.utilities.RoverUtils;

/**
 * Created by SherryYang on 2015-03-03.
 */
public class CardListActivity extends Activity {

    public static final String TAG = CardListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list);

        RecyclerView cardListRecyclerView = (RecyclerView)findViewById(R.id.card_list_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cardListRecyclerView.setLayoutManager(linearLayoutManager);

        List<RoverCard> latestCards = RoverVisitManager.getInstance(getApplicationContext()).getLatestVisit().getAccumulatedCards();

        if(latestCards.isEmpty()) {
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

        CardListAdapter cardListAdapter = new CardListAdapter(latestCards, this);
        cardListRecyclerView.setAdapter(cardListAdapter);
    }
}
