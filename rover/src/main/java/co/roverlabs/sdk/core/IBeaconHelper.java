package co.roverlabs.sdk.core;

/**
 * Created by ars on 15-06-02.
 */
interface IBeaconHelper {

    int ENTERED_REGION = 1;
    int EXITED_REGION = 2;

    void startMonitoring(String uuid);
    void stopMonitoring();
    boolean isMonitoringStarted();

    void startRanging();
    void stopRanging();
}



