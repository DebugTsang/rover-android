package co.roverlabs.sdk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import co.roverlabs.sdk.R;
import co.roverlabs.sdk.managers.RoverVisitManager;
import co.roverlabs.sdk.models.RoverCard;
import co.roverlabs.sdk.models.RoverVisit;

/**
 * Created by SherryYang on 2015-03-03.
 */
public class CardListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //TODO: Shallow copy of the RoverVisit object, need to implement deep copy
        RoverVisit latestVisit = RoverVisitManager.getInstance(this.getApplicationContext()).getLatestVisit();
        List<RoverCard> latestCards = latestVisit.getTouchPoints().get(0).getCards();
        CardListAdapter cardListAdapter = new CardListAdapter(createImageInCardList(latestCards), this);
        recyclerView.setAdapter(cardListAdapter);
    }
    
    private List<RoverCard> createImageInCardList(List<RoverCard> cards) {
        
        for(int i = 0; i < cards.size(); i++) {
            if(i % 2 == 0) {
                cards.get(i).setImageResourceId(R.drawable.image1);
            }
            else {
                cards.get(i).setImageResourceId(R.drawable.image2);
            }
        }
        return cards;
    }
}
