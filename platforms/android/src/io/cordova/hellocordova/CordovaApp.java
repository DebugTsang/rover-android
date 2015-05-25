/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package io.cordova.hellocordova;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.apache.cordova.CordovaActivity;

import co.roverlabs.sdk.Rover;
import co.roverlabs.sdk.RoverConfigs;
import co.roverlabs.sdk.models.RoverCustomer;

public class CordovaApp extends CordovaActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.init();
        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);


        final Rover rover = Rover.getInstance(this.getApplicationContext());

        RoverCustomer roverCustomer = new RoverCustomer();
        roverCustomer.setName("Sherry Yang");
        roverCustomer.setEmail("sherry@roverlabs.co");
        roverCustomer.addTraits("gender", "female");

        RoverConfigs roverConfigs = new RoverConfigs();
        //Sean's account
        //roverConfigs.setUuid("7931D3AA-299B-4A12-9FCC-D66F2C5D2462");
        //roverConfigs.setAppId("eae9edb6352b8fec6618d3d9cb96f2e795e1c2df1ad5388af807b05d8dfcd7d6");
        //Personal account for testing
        roverConfigs.setUuid("F352DB29-6A05-4EA2-A356-9BFAC2BB3316");
        roverConfigs.setAppId("ff259b8f81ba2a2fd227445e2b3dbaca3e9552ff1663fa3f46e89a284bc9aaa0");
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(R.drawable.icon);
        roverConfigs.setSandBoxMode(true);

        rover.setCustomer(roverCustomer);
        rover.setConfigurations(roverConfigs);

        rover.startMonitoring();
        rover.simulate();
//        //TODO: Remove after testing
//        Button showCardsButton = (Button)findViewById(R.id.show_cards_button);
//        showCardsButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                rover.showCards();
//            }
//        });
//
//        //TODO: Remove after testing
//        Button simulateButton = (Button)findViewById(R.id.simulate_button);
//        simulateButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                rover.simulate();
//            }
//        });

    }
}
