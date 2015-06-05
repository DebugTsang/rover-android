package co.roverlabs.sdk.core;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.roverlabs.sdk.model.Location;
import co.roverlabs.sdk.model.Region;
import co.roverlabs.sdk.model.Touchpoint;
import co.roverlabs.sdk.model.Visit;

/**
 * Created by ars on 15-06-02.
 */
public class VisitManager implements RegionManager.IRegionListener {
    static final String TAG = VisitManager.class.getSimpleName();

    public interface IVisitListener {
        boolean shouldCreateVisit(VisitManager manager, Visit visit);
        void onEnterLocation(VisitManager manager, Visit visit);
        void onPotentiallyExitLocation(VisitManager manager, Location location, Visit visit);
        void onExpireVisit(VisitManager manager, Visit visit);
        void onEnterTouchpoint(VisitManager manager, List<Touchpoint> touchpoints, Visit visit);
        void onExitTouchpoints(VisitManager manager, List<Touchpoint> touchpoints, Visit visit);
    }

    private Visit mLatestVisit;
    public IVisitListener mVisitListener;
    private RegionManager mRegionManager;

    public VisitManager(Context context){
        mRegionManager = new RegionManager(context);
        mRegionManager.setListener(this);
    }

    public RegionManager getRegionManager() {
        return this.mRegionManager;
    }

    public void setVisitListener(IVisitListener visitListener){
        this.mVisitListener = visitListener;
    }



    @Override
    public void onEnteredRegions(RegionManager manager, List<Region> regions){
        // TO BE FILLED IN
    }

    @Override
    public void onExitedRegions(RegionManager manager, List<Region> regions) {
        // TO BE FILLED IN
    }

}
