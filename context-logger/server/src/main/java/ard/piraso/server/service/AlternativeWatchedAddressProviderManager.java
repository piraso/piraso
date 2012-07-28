/*
 * Copyright (c) 2012. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ard.piraso.server.service;

import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides alternative watched address given the provided watched address by user.
 */
public class AlternativeWatchedAddressProviderManager implements AlternativeWatchedAddressProvider {

    public static final AlternativeWatchedAddressProviderManager INSTANCE = new AlternativeWatchedAddressProviderManager();


    private List<AlternativeWatchedAddressProvider> providers;

    private AlternativeWatchedAddressProviderManager() {
        providers = new LinkedList<AlternativeWatchedAddressProvider>();

        // localhost provider
        addProvider(new AlternativeWatchedAddressProvider() {
            public String[] getAlternatives(String watchedAddr) {
                if(watchedAddr.equals("127.0.0.1")) {
                    return new String[] {"0:0:0:0:0:0:0:1%0"};
                }

                return null;
            }
        });
    }


    public void addProvider(AlternativeWatchedAddressProvider provider) {
        Validate.notNull(provider, "provider argument should not be null.");
        providers.add(provider);
    }

    public String[] getAlternatives(String watchedAddr) {
        List<String> results = new ArrayList<String> ();

        for(AlternativeWatchedAddressProvider provider : providers) {
            String[] alternatives = provider.getAlternatives(watchedAddr);

            if(alternatives != null) {
                results.addAll(Arrays.asList(alternatives));
            }
        }

        return results.toArray(new String[results.size()]);
    }
}
