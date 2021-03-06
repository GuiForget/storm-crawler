/**
 * Licensed to DigitalPebble Ltd under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * DigitalPebble licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.digitalpebble.storm.crawler.filtering.basic;

import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.JsonNode;

import com.digitalpebble.storm.crawler.filtering.URLFilter;

public class BasicURLFilter implements URLFilter {

    boolean removeAnchorPart = true;

    public String filter(String URL) {
        if (removeAnchorPart) {
            try {
                URL theURL = new URL(URL);
                String anchor = theURL.getRef();
                if (anchor != null)
                    URL = URL.replace("#" + anchor, "");
            } catch (MalformedURLException e) {
                return null;
            }
        }

        return URL;
    }

    public void configure(JsonNode paramNode) {
        JsonNode node = paramNode.get("removeAnchorPart");
        if (node != null)
            removeAnchorPart = node.getBooleanValue();
    }

}
