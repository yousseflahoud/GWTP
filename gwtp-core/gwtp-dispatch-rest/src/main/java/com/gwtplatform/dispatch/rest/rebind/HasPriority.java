/*
 * Copyright 2014 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtplatform.dispatch.rest.rebind;

import java.util.Comparator;

public interface HasPriority {
    Comparator<HasPriority> COMPARATOR = new Comparator<HasPriority>() {
        @Override
        public int compare(HasPriority o1, HasPriority o2) {
            return o1.getPriority() - o2.getPriority();
        }
    };
    int DEFAULT_PRIORITY = 15;

    byte getPriority();
}
