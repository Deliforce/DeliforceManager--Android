package com.app.deliforcemanager.glypmseWrapper;

import android.content.Context;

import com.glympse.android.toolbox.listener.GListener;
import com.glympse.android.toolbox.listener.GSource;
import com.glympse.enroute.android.api.EE;
import com.glympse.enroute.android.api.EnRouteFactory;
import com.glympse.enroute.android.api.GEnRouteManager;

public class EnRouteWrapper implements GListener {
    private boolean _stopped = false;
    private GEnRouteManager _manager;
    private static EnRouteWrapper _instance;
    private Context _context;

    public static EnRouteWrapper instance() {
        if ( null == _instance ) {
            _instance = new EnRouteWrapper();
        }
        return _instance;
    }

    // Init must be called at least once by the hosting app before any further use. This allows the
    // app to provide an instance of Context.
    public void init(Context context)
    {
        _context = context;
    }

    public void createManager() {
        _manager = EnRouteFactory.createEnRouteManager(_context);

        // We need to listen to the manager so we know when the EnRouteEvents.ENROUTE_MANAGER_STOPPED event is
        //  fired. After this event is fired, this instance of the manager is no longer usable and
        //  a new instance must be created.
        _manager.addListener(this);
    }

    public GEnRouteManager manager() {
        if ( null == _context ) {
            throw new RuntimeException("EnRouteWrapper.instance().init(...) must be called once before accessing EnRouteWrapper.instance().manager()");
        }

        if ( null == _manager || _stopped ) {
            // The old instance of the manager is no longer usable, we need to create a new one.
            createManager();
            _stopped = false;
        }
        return _manager;
    }

    /**
     * GListener section
     */

    public void eventsOccurred(GSource source, int listener, int events, Object param1, Object param2) {
        if ( EE.LISTENER_ENROUTE_MANAGER == listener )
        {
            if ( 0 != ( EE.ENROUTE_MANAGER_STOPPED & events ) )
            {
                stopped();
            }
        }
    }

    public void stopped() {
        // Set the stopped flag so we know to recreate the manager the next time an instance is needed.
        _stopped = true;
    }
}