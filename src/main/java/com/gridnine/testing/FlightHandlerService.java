package com.gridnine.testing;

import java.time.ZoneId;
import java.util.List;

public interface FlightHandlerService {
    List<Flight> getFlightsWithDepartureLaterThanCurrentTime (List<Flight> flights, ZoneId zoneId);
    List<Flight> getFlightsWithDepartureLaterThanCurrentLocalTime (List<Flight> flights);
    List<Flight> getSegmentsWithDepartureEarlierThanArrival (List<Flight> flights);
    List<Flight> getFlightsWithGroundedTimeShorterThanTwoHours (List<Flight> flights);
}
