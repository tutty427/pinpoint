/*
 * Copyright 2017 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.profiler.context.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.navercorp.pinpoint.bootstrap.config.ProfilerConfig;
import com.navercorp.pinpoint.rpc.client.DefaultPinpointClientFactory;
import com.navercorp.pinpoint.rpc.client.PinpointClientFactory;

/**
 * @author Taejin Koo
 */
public class SpanStatClientFactoryProvider implements Provider<PinpointClientFactory> {

    private final ProfilerConfig profilerConfig;

    @Inject
    public SpanStatClientFactoryProvider(ProfilerConfig profilerConfig) {
        if (profilerConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }

        this.profilerConfig = profilerConfig;
    }

    public PinpointClientFactory get() {
        int workerCount = 0;

        if ("TCP".equalsIgnoreCase(profilerConfig.getSpanDataSenderTransportType())) {
            workerCount++;
        }
        if ("TCP".equalsIgnoreCase(profilerConfig.getStatDataSenderTransportType())) {
            workerCount++;
        }

        if (workerCount == 0) {
            return null;
        } else {
            PinpointClientFactory pinpointClientFactory = new DefaultPinpointClientFactory(1, workerCount);
            pinpointClientFactory.setWriteTimeoutMillis(1000 * 3);
            pinpointClientFactory.setRequestTimeoutMillis(1000 * 5);
            return pinpointClientFactory;
        }
    }

}