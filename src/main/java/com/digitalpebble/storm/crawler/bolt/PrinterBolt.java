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

package com.digitalpebble.storm.crawler.bolt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/** Dummy indexer which displays the fields on the std out **/

@SuppressWarnings("serial")
public class PrinterBolt extends BaseRichBolt {
    OutputCollector _collector;

    public void prepare(Map conf, TopologyContext context,
            OutputCollector collector) {
        _collector = collector;
    }

    public void execute(Tuple tuple) {
        Iterator<String> iterator = tuple.getFields().iterator();
        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            Object obj = tuple.getValueByField(fieldName);

            if (obj instanceof byte[])
                System.out.println(fieldName + "\t"
                        + tuple.getBinaryByField(fieldName).length + " bytes");
            else if (obj instanceof HashMap) {
                // probably metadata <String,String[]>
                HashMap<String, String[]> md = (HashMap<String, String[]>) obj;
                Iterator<Entry<String, String[]>> mditer = md.entrySet()
                        .iterator();
                while (mditer.hasNext()) {
                    Entry<String, String[]> entry = mditer.next();
                    for (String val : entry.getValue()) {
                        System.out.println(fieldName + "." + entry.getKey()
                                + "\t" + trimValue(val));
                    }
                }
            } else {
                String value = tuple.getValueByField(fieldName).toString();
                System.out.println(fieldName + "\t" + trimValue(value));
            }

        }
        _collector.ack(tuple);
    }

    private String trimValue(String value) {
        if (value.length() > 100)
            return value.length() + " chars";
        return value;
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

}