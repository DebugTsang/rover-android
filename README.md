# rover-android

##Setting up


###For PhoneGap/Cordova Apps

1. Download rover-android from github
2. Include rover-android module into your Android Studio project
    2.1. File -> New -> Import Module
    2.2. Browse the rover-android directory inside rover-android-master.zip
3. In your main activity class (the one that extend CordovaActivity) add the following code inside onCreate method. Make sure to replace <UUID> and <APP ID> with your real config values.


        final Rover rover = Rover.getInstance(getApplicationContext());

        RoverCustomer roverCustomer = new RoverCustomer();

        RoverConfigs roverConfigs = new RoverConfigs();
        roverConfigs.setUuid(<UUID>);
        roverConfigs.setAppId(<APP ID>);
        
        roverConfigs.setLaunchActivityName(this.getClass().getName());
        roverConfigs.setNotificationIconId(<YOUR APP ICON RESOURCE ID>); //R.drawable.ic_launcher
        rover.setConfigurations(roverConfigs);

        rover.startMonitoring();
        
4. You are all set! Run the app.
