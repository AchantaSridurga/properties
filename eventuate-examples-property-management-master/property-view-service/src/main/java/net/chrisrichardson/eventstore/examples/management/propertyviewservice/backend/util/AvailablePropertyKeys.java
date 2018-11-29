package net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.util;


public class AvailablePropertyKeys {

  public static String closingTimesKey(String zipCode, int dayOfWeek) {
    return RedisUtil.key("closingTimes", zipCode, dayOfWeek);
  }

}
