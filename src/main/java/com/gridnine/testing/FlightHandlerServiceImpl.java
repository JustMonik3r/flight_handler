package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;


public class FlightHandlerServiceImpl implements FlightHandlerService {

    /*
    отсеивает полеты из списка flights по времени вылета
    проверяются все сегменты
    исключает те перелеты, у которых хотя бы в одном сегменте дата вылета превышает текущее время в часовом поясе zoneId
     */
    @Override
    public List<Flight> getFlightsWithDepartureLaterThanCurrentTime (List<Flight> flights, ZoneId zoneId) {
        LocalDateTime currentTime = LocalDateTime.now(zoneId);

        return flights.stream()
                .filter(flight -> flight.getSegments()
                        .stream()
                        .allMatch(segment -> segment.getDepartureDate()
                                .isAfter(currentTime)))
                .collect(Collectors.toList());
    }

    /*
    отсеивает полеты из списка flights по времени вылета
    проверяются все сегменты
    исключает те перелеты, у которых хотя бы в одном сегменте дата вылета превышает текущее местное время
     */
    @Override
    public List<Flight> getFlightsWithDepartureLaterThanCurrentLocalTime (List<Flight> flights){
        LocalDateTime currentLocalTime = LocalDateTime.now();

        return flights.stream()
                .filter(flight -> flight.getSegments()
                        .stream()
                        .allMatch(segment -> segment.getDepartureDate()
                                .isAfter(currentLocalTime)))
                .collect(Collectors.toList());
    }

    /*
    отсеивает полеты из списка flights по времени прибытия
    проверяются все сегменты
    исключает те перелеты, у которых хотя бы в одном сегменте дата прилета идет раньше даты вылета
     */
    @Override
    public List<Flight> getSegmentsWithDepartureEarlierThanArrival (List<Flight> flights){
        return flights.parallelStream()
                .filter(flight -> {
                    List<Segment> segments = flight.getSegments();
                    return segments.stream()
                            .allMatch(segment -> segment.getDepartureDate().isBefore(segment.getArrivalDate()));
                })
                .toList();
    }

    /*
    отсеивает полеты из списка flights по времени пересадки
    оставляет те, у которых время пересадки превышает 2 часа (120 минут)
     */
    @Override
    public List<Flight> getFlightsWithGroundedTimeShorterThanTwoHours (List<Flight> flights){
        return flights.parallelStream()
                .filter(flight -> {
                    List<Segment> segments = flight.getSegments();
                    int totalGroundedTime = calculateFlightGroundedTime(segments);
                    return totalGroundedTime < 120;
                })
                .toList();
    }

    /*
    подсчитывает время, проведенное на земле за время полета flight
    расчет ведется в минутах
     */
    public int calculateFlightGroundedTime(List<Segment> segments) {
        int flightGroundedTime = 0;
        for (int i = 0; i < segments.size() - 1; i++) {
            Segment currentSegment = segments.get(i);
            Segment nextSegment = segments.get(i + 1);
            LocalDateTime currentSegmentArrival = currentSegment.getArrivalDate();
            LocalDateTime nextSegmentDeparture = nextSegment.getDepartureDate();
            Duration segmentGroundedTime = Duration.between(currentSegmentArrival, nextSegmentDeparture);
            flightGroundedTime = Math.toIntExact(flightGroundedTime + segmentGroundedTime.toMinutes());
        }
        return flightGroundedTime;
    }
}
