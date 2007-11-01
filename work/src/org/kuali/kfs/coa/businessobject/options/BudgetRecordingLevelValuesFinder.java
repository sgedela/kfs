/*
 * Copyright 2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.module.chart.lookup.keyvalues;

import org.kuali.module.chart.bo.codes.BudgetRecordingLevel;

/**
 * This class creates a new finder for our forms view (creates a drop-down of {@link BudgetRecordingLevel}s)
 * 
 * 
 */
public class BudgetRecordingLevelValuesFinder extends KualiSystemCodeValuesFinder {

    /**
     * This method is called by the {@link KualiSystemCodeValuesFinder} that creates
     * a list out of the code and the code and description from the class
     * @see org.kuali.module.chart.lookup.keyvalues.KualiSystemCodeValuesFinder#getValuesClass()
     */
    protected Class getValuesClass() {
        return BudgetRecordingLevel.class;
    }

}
