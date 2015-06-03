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

import co.roverlabs.sdk.models.Customer;
import co.roverlabs.sdk.utils.SharedPrefUtils;

/**
 * Beacon discovery and card view rendering manager
 * <p>
 * Use {@link #getInstance(android.content.Context)} for the global singleton instance. Chain it
 * using {@link #with(co.roverlabs.sdk.Config)} to setup the config before using it.
 *
 */
public class Rover {

    static final String TAG = "Rover";
    private static Rover singleton;

    Config config;
    final Context context;
    Customer customer;

    //beacon monitoring management
    private ILocationHelper mLocationHelper;
    private VisitManager mVisitManager;

    volatile boolean isLoggingEnabled;

    private Rover(Context context) {
        this.context = context.getApplicationContext();
        mVisitManager = new VisitManager(this);
        mLocationHelper = FactoryUtils.getDefaultLocationHelper(context, mVisitManager.handler);

    }
    public static Rover getInstance(Context context) {
        if(singleton == null) {
            singleton = new Rover(context);
        }
        return singleton;
    }

    /**
     * Setup config values before starting monitoring.
     *
     * @throws RuntimeException if the config is not complete
     * @param config
     * @return {@link Rover}
     */
    public Rover with(Config config) {
        //make sure config is not null and it is complete
        checkConfig(config);

        //save config
        setConfig(config);

        //restart monitoring as the UUID might have been changed in the new config
        if (mLocationHelper != null && mLocationHelper.isMonitoringStarted()){
            stopMonitoring();
            startMonitoring();
        }

        return this;
    }

    /**
     * Starts searching for beacons in the background
     */
    public void startMonitoring() {
        checkConfig(config);
        mLocationHelper.startMonitoring(config.getUuid());
    }

    /**
     * Starts searching for beacons in the background
     */
    public void stopMonitoring() {
        mLocationHelper.stopMonitoring();
    }

    /**
     * makes sure the config is complete and throws RuntimeException otherwise
     *
     * @throws RuntimeException (config incomplete or null)
     * @param config
     */
    private void checkConfig(Config config){
        if (config == null){
            Log.e(TAG, "Unable to proceed with null config");
            throw new RuntimeException("Rover config cannot be null");
        }

        if(!config.isComplete()) {
            Log.e(TAG, "Unable to proceed with incomplete config");

            //TODO: add a smarter message to tell the client what's missing in config
            throw new RuntimeException("Rover cannot be set up - configurations incomplete");
        }
    }

    /**
     * Update the local config variable and stores it in the local storage
     *
     * @param config
     */
    private void setConfig(Config config){
        this.config = config;
        SharedPrefUtils.writeObjectToSharedPrefs(context, config);
    }
}