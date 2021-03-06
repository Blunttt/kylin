/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kylin.job.impl.curator;

import org.apache.kylin.common.KylinConfig;
import org.apache.kylin.job.engine.JobEngineConfig;
import org.apache.kylin.job.exception.SchedulerException;
import org.apache.kylin.job.lock.MockJobLock;

import java.io.Closeable;
import java.io.IOException;

/**
 */
public class ExampleServer implements Closeable {

    private String address;
    private CuratorScheduler scheduler;

    public ExampleServer(String address) throws Exception {
        this.address = address;

        KylinConfig kylinConfig = KylinConfig.getInstanceFromEnv();
        KylinConfig kylinConfig1 = KylinConfig.createKylinConfig(kylinConfig);
        kylinConfig1.setProperty("kylin.server.host-address", address);

        scheduler = new CuratorScheduler();
        scheduler.init(new JobEngineConfig(kylinConfig1), new MockJobLock());
        if (!scheduler.hasStarted()) {
            throw new RuntimeException("scheduler has not been started");
        }
    }

    public String getAddress() {
        return address;
    }

    @Override
    public void close() throws IOException {

        if (scheduler!= null)
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
               //
            }
    }

}
