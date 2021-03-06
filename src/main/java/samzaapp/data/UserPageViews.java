/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package samzaapp.data;


import org.codehaus.jackson.annotate.JsonProperty;

/**
 * User page view count.
 */
public class UserPageViews {
  private final String userId;
  private final int count;

  /**
   * Constructs a user page view count.
   *
   * @param userId the id of the user viewing the pages
   * @param count number of page views by the user
   */
  public UserPageViews(
      @JsonProperty("userId") String userId,
      @JsonProperty("count") int count) {
    this.userId = userId;
    this.count = count;
  }

  public String getUserId() {
    return userId;
  }

  public int getCount() {
    return count;
  }
}
