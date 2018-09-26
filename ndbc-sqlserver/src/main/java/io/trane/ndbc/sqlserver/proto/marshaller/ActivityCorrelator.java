/*
 * Microsoft JDBC Driver for SQL Server Copyright(c) Microsoft Corporation All rights reserved. This program is made
 * available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */

package io.trane.ndbc.sqlserver.proto.marshaller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ActivityCorrelator provides the APIs to access the ActivityId in TLS
 */
public final class ActivityCorrelator {

  private static Map<Long, ActivityId> ActivityIdTlsMap = new ConcurrentHashMap<Long, ActivityId>();

  static void cleanupActivityId() {
    // remove the ActivityId that belongs to this thread.
    final long uniqueThreadId = Thread.currentThread().getId();

    if (ActivityIdTlsMap.containsKey(uniqueThreadId))
      ActivityIdTlsMap.remove(uniqueThreadId);
  }

  // Get the current ActivityId in TLS
  static ActivityId getCurrent() {
    // get the value in TLS, not reference
    final long uniqueThreadId = Thread.currentThread().getId();

    // Since the Id for each thread is unique, this assures that the below if
    // statement is run only once per thread.
    if (!ActivityIdTlsMap.containsKey(uniqueThreadId))
      ActivityIdTlsMap.put(uniqueThreadId, new ActivityId());

    return ActivityIdTlsMap.get(uniqueThreadId);
  }

  // Increment the Sequence number of the ActivityId in TLS
  // and return the ActivityId with new Sequence number
  public static ActivityId getNext() {
    // Get the current ActivityId in TLS
    final ActivityId activityId = getCurrent();

    // Increment the Sequence number
    activityId.Increment();

    return activityId;
  }

  static void setCurrentActivityIdSentFlag() {
    final ActivityId activityId = getCurrent();
    activityId.setSentFlag();
  }
}
