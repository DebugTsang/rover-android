package co.roverlabs.sdk;

/**
 * Created by ars on 15-06-02.
 */
public interface ILocationHelper {

    int ENTERED_REGION = 1;
    int EXITED_REGION = 2;

    void startMonitoring(String uuid);
    void stopMonitoring();
    boolean isMonitoringStarted();

    void startRanging();
    void stopRanging();
}
