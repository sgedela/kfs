/*
 * Copyright 2007-2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kfs.module.bc.businessobject.options;

import java.util.ArrayList;
import java.util.List;

import org.kuali.kfs.module.bc.BCConstants;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.core.api.util.KeyValue; import org.kuali.rice.core.api.util.ConcreteKeyValue;

/**
 * returns text field delimiter values
 */
public class TextFieldDelimiterValuesFinder extends KeyValuesBase {

    /*
     * @see org.kuali.keyvalues.KeyValuesFinder#getKeyValues()
     */
    public List getKeyValues() {
        List keyValues = new ArrayList();
        keyValues.add(new ConcreteKeyValue(BCConstants.RequestImportTextFieldDelimiter.QUOTE.getDelimiter(), BCConstants.RequestImportTextFieldDelimiter.QUOTE.toString()));
        keyValues.add(new ConcreteKeyValue(BCConstants.RequestImportTextFieldDelimiter.NOTHING.getDelimiter(), BCConstants.RequestImportTextFieldDelimiter.NOTHING.toString()));
        keyValues.add(new ConcreteKeyValue(BCConstants.RequestImportTextFieldDelimiter.OTHER.getDelimiter(), BCConstants.RequestImportTextFieldDelimiter.OTHER.toString()));
        
        return keyValues;
    }
}
