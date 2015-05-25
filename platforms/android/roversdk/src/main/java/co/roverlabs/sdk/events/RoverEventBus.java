package co.roverlabs.sdk.events;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by SherryYang on 2015-03-06.
 */
public final class RoverEventBus {

    private static final Bus ROVER_BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() { return ROVER_BUS; }

    private RoverEventBus() { }
}