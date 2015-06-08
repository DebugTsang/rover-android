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
import co.roverlabs.sdk.model.Touchpoint;
import co.roverlabs.sdk.model.Visit;
import co.roverlabs.sdk.network.listeners.RoverObjectSaveListener;


public class Rover implements VisitManager.IVisitManagerListener {

    public static final String TAG = Rover.class.getSimpleName();
    private static Rover sSharedInstance;
    private Context mContext;
    private Config mConfig;
    private VisitManager mVisitManager;

    public static Rover setup(Context context, Config config) {

        if(sSharedInstance != null) {
            Log.e(TAG, "ERROR: Rover was already setup!");
            return sSharedInstance;
        }
        sSharedInstance = new Rover(context, config);
        return sSharedInstance;
    }
    
    public static Rover getInstance() {

        if(sSharedInstance == null) {
            Log.e(TAG, "ERROR: getInstance() called before setup()");
        }
        return sSharedInstance;
    }

    private Rover(Context context, Config config) {

        mContext = context.getApplicationContext();
        mConfig = config;
        mVisitManager = new VisitManager(context.getApplicationContext());
        mVisitManager.setListener(this);
        mVisitManager.getRegionManager().setUuid(config.getUuid());
    }

    public Context getContext() {

        return mContext;
    }
    
    public void startMonitoring() {

        mVisitManager.getRegionManager().startMonitoring();
    }

    public void stopMonitoring() {

        mVisitManager.getRegionManager().stopMonitoring();
    }
    
    /********************************************************************************
    /* VisitManager.IVisitListener
    ********************************************************************************/

    @Override
    public boolean shouldCreateVisit(VisitManager manager, Visit visit) {

        visit.setSimulation(mConfig.getSandBoxMode());

        NetworkManager.save(new RoverObjectSaveListener() {

            @Override
            public void onSaveSuccess() {

            }

            @Override
            public void onSaveFailure() {

            }
        }, visit);
        return true;
    }

    @Override
    public void onEnterLocation(VisitManager manager, Visit visit) {

    }

    @Override
    public void onPotentiallyExitLocation(VisitManager manager, Visit visit) {

    }

    @Override
    public void onVisitExpire(VisitManager manager, Visit visit) {

    }

    @Override
    public void onEnterTouchpoints(VisitManager manager, List<Touchpoint> touchpoints, Visit visit) {

    }

    @Override
    public void onExitTouchpoints(VisitManager manager, List<Touchpoint> touchpoints, Visit visit) {

    }

}
