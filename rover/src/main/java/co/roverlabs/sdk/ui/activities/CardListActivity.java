package co.roverlabs.sdk.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.model.Card;
import co.roverlabs.sdk.ui.RecyclerViewOnItemSwipeListener;
/**
 * Created by SherryYang on 2015-03-03.
 */
public class CardListActivity extends BaseActivity {

    public static final String TAG = CardListActivity.class.getSimpleName();
    public static final String EXTRA_KEY_CARDS = "co.roverlabs.sdk.ui.activities.CardListActivity:EXTRA_KEY_CARDS";

    private RecyclerView mCardListRecyclerView;
    private LinearLayoutManager mCardListLayoutManager;
    private CardListAdapter mCardListAdapter;
    private List<Card> mCards;
    private Button mNewCardButton;
    private Animation mSlideIn;
    private Animation mSlidOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list);

        //RoverEventBus.getInstance().register(this);
        mHeadIconId = getIntent().getIntExtra(EXTRA_HEAD_ICON_ID, -1);
        Card[] cards = (Card[])getIntent().getParcelableArrayExtra(EXTRA_KEY_CARDS);
        mCards = Arrays.asList(cards);

        //we have nothing to do if there are no cards
        if(mCards.size() == 0) {
            finish();
        }

        mCardListRecyclerView = (RecyclerView)findViewById(R.id.card_list_recycler_view);
        mNewCardButton = (Button)findViewById(R.id.new_card_button);
        mSlideIn = AnimationUtils.loadAnimation(this, R.anim.button_slide_in);
        mSlidOut = AnimationUtils.loadAnimation(this, R.anim.button_slide_out);

        mCardListLayoutManager = new LinearLayoutManager(this);
        mCardListLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCardListRecyclerView.setLayoutManager(mCardListLayoutManager);

        mCardListAdapter = new CardListAdapter(mCards, this);

        mCardListRecyclerView.setAdapter(mCardListAdapter);
        mCardListRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                if (mCardListLayoutManager.findFirstVisibleItemPosition() == 0) {
                    if (mNewCardButton.getVisibility() == View.VISIBLE) {
                        mNewCardButton.startAnimation(mSlidOut);
                        mNewCardButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        RecyclerViewOnItemSwipeListener swipeTouchListener =
                new RecyclerViewOnItemSwipeListener(mCardListRecyclerView,
                        new RecyclerViewOnItemSwipeListener.SwipeListener() {

                            @Override
                            public boolean canSwipe(int position) {

                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {

                                for(int position : reverseSortedPositions) {
                                    //RoverEventBus.getInstance().post(new CardDiscardedEvent(mLatestVisitId, mCards.get(position).getId()));
                                    mCards.get(position).setDismissed(true);
                                    mCards.remove(position);
                                    mCardListAdapter.notifyItemRemoved(position);
                                }

                                mCardListAdapter.notifyDataSetChanged();

                                if(mCards.isEmpty()) {
                                    finish();
                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {

                                for(int position : reverseSortedPositions) {
                                    //RoverEventBus.getInstance().post(new CardDiscardedEvent(mLatestVisitId, mCards.get(position).getId()));
                                    mCards.get(position).setDismissed(true);
                                    mCards.remove(position);
                                    mCardListAdapter.notifyItemRemoved(position);
                                }

                                mCardListAdapter.notifyDataSetChanged();

                                if(mCards.isEmpty()) {
                                    finish();
                                }
                            }
                        });

        mCardListRecyclerView.addOnItemTouchListener(swipeTouchListener);

        mNewCardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mCardListRecyclerView.smoothScrollToPosition(0);
                mNewCardButton.startAnimation(mSlidOut);
                mNewCardButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mNewCardButton.setVisibility(View.INVISIBLE);
    }
}