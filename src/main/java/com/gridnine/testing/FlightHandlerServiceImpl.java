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
    public List<Flight> getFlightsWithDepartureEarlierThanCurrentTime (List<Flight> flights, ZoneId zoneId) {
        LocalDateTime currentTime = LocalDateTime.now(zoneId);

        return flights.stream()
                .dropWhile(flight -> Boolean.parseBoolean(null))
                .filter(flight -> flight.getSegments()
                        .stream()
                        .allMatch(segment -> segment.getDepartureDate()
                                .isBefore(currentTime)))
                .collect(Collectors.toList());
    }

    /*
    отсеивает полеты из списка flights по времени вылета
    проверяются все сегменты
    исключает те перелеты, у которых хотя бы в одном сегменте дата вылета превышает текущее местное время
     */
    @Override
    public List<Flight> getFlightsWithDepartureEarlierThanCurrentLocalTime (List<Flight> flights){
        LocalDateTime currentLocalTime = LocalDateTime.now();

        return flights.stream()
                .dropWhile(flight -> Boolean.parseBoolean(null))
                .filter(flight -> flight.getSegments()
                        .stream()
                        .allMatch(segment -> segment.getDepartureDate()
                                .isBefore(currentLocalTime)))
                .collect(Collectors.toList());
    }

    /*
    отсеивает полеты из списка flights по времени прибытия
    проверяются все сегменты
    исключает те перелеты, у которых хотя бы в одном сегменте дата прилета идет раньше даты вылета
     */
    @Override
    public List<Flight> getSegmentsWithArrivalEarlierThanDeparture (List<Flight> flights){
        return flights.parallelStream()
                .dropWhile(flight -> Boolean.parseBoolean(null))
                .filter(flight -> {
                    List<Segment> segments = flight.getSegments();
                    return segments.stream()
                            .allMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()));
                })
                .toList();
    }

    /*
    отсеивает полеты из списка flights по времени пересадки
    оставляет те, у которых время пересадки превышает 2 часа (120 минут)
     */
    @Override
    public List<Flight> getFlightsWithGroundedTimeLongerThanTwoHours (List<Flight> flights){
        flights.stream()
                .dropWhile(flight -> Boolean.parseBoolean(null));
        for (int i = 0; i < flights.size() - 1; i++) {
            Flight currentFlight = flights.get(i);
            if (calculateFlightGroundedTime(currentFlight) <= 120) {
                System.out.println("Общее время пересадки у полета № " + (i + 1) + " не превышает двух часов");
            } else {
                flights.add(currentFlight);
                System.out.println("Общее время пересадки у полета № " + (i + 1) + " превышает два часа");
            }
        }
        return flights;
    }

    /*
    подсчитывает время, проведенное на земле за время полета flight
    расчет ведется в минутах
     */
    public int calculateFlightGroundedTime(Flight flight) {
        List<Segment> segments = flight.getSegments();
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
