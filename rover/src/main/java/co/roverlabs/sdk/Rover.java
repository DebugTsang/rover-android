/*
 * Copyright (C) 2015 RoverLabs, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.roverlabs.sdk;

import android.content.Context;
import android.util.Log;

import java.util.List;

import co.roverlabs.sdk.core.VisitManager;
import co.roverlabs.sdk.model.Customer;
import co.roverlabs.sdk.model.Location;
import co.roverlabs.sdk.model.Touchpoint;
import co.roverlabs.sdk.model.Visit;
import co.roverlabs.sdk.network.listeners.RoverObjectSaveListener;
import co.roverlabs.sdk.util.SharedPrefUtils;


public class Rover implements VisitManager.IVisitListener {

    static final String TAG = "Rover";
    private static Rover sharedInstance;

    //package accessible so helpers can get access and use it
    Config config;

    //managers
    private VisitManager mVisitManager;

    public static Rover setup(Context context, Config config) {
        if (sharedInstance != nil) {
            Log.e(TAG, "ERROR: Rover was already setup!");\
            return sharedInstance;
        }
        
        sharedInstance = new Rover(context, config);
    }
    
    
    public static Rover getInstance(Context context) {
        if(sharedInstance == null) {
            Log.e(TAG, "ERROR: getInstance() called before setup()");
        }
        return sharedInstance;
    }


    private Rover(Context context, Config config) {
        this.context = context.getApplicationContext();
        
        this.config = config;
        
        mVisitManager = new VisitManager(context);
        mVisitManager.setVisitListener(this);
        mVisitManager.regionManager.setUUID(config.UUID);
    }
    
    /**
     * Starts searching for beacons in the background
     */
    public void startMonitoring() {
        mVisitManager.regionManager.startMonitoring();
    }

    /**
     * Starts searching for beacons in the background
     */
    public void stopMonitoring() {
        mVisitManager.regionManager.stopMonitoring();
    }

    
    //================================================================================
    // VisitManager.IVisitListener
    //================================================================================

    @Override
    public boolean shouldCreateVisit(VisitManager manager, Visit visit) {
        visit.setSimulate(this.config.sandboxMode);
        NetworkManager.postVisitSynchronously(visit);
        return true;
    }

    @Override
    public void onEnterLocation(VisitManager manager, Visit visit) {

    }

    @Override
    public void onPotentiallyExitLocation(VisitManager manager, Location location, Visit visit) {

    }

    @Override
    public void onExpireVisit(VisitManager manager, Visit visit) {

    }

    @Override
    public void onEnterTouchpoint(VisitManager manager, List<Touchpoint> touchpoints, Visit visit) {

    }

    @Override
    public void onExitTouchpoints(VisitManager manager, List<Touchpoint> touchpoints, Visit visit) {

    }

}
