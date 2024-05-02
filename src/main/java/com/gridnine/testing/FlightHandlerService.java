package com.gridnine.testing;

import java.time.ZoneId;
import java.util.List;

public interface FlightHandlerService {
    List<Flight> getFlightsWithDepartureEarlierThanCurrentTime (List<Flight> flights, ZoneId zoneId);
    List<Flight> getFlightsWithDepartureEarlierThanCurrentLocalTime (List<Flight> flights);
    List<Flight> getSegmentsWithArrivalEarlierThanDeparture (List<Flight> flights);
    List<Flight> getFlightsWithGroundedTimeLongerThanTwoHours (List<Flight> flights);
}
