/*
 * Copyright 2009 The Kuali Foundation.
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
package org.kuali.kfs.module.endow.businessobject;

import java.util.LinkedHashMap;

import org.kuali.kfs.sys.KFSConstants;
import org.kuali.rice.krad.bo.KualiCodeBase;

/**
 * Business Object for Security Reporting Group table
 */
public class SecurityReportingGroup extends KualiCodeBase {

    private Integer securityReportingGrpOrder;

    /**
     * This method gets the securityReportingGrpOrder
     * 
     * @return securityReportingGrpOrder
     */
    public Integer getSecurityReportingGrpOrder() {
        return securityReportingGrpOrder;
    }

    /**
     * This method sets the securityReportingGrpOrder
     * 
     * @param securityReportingGrpOrder
     */
    public void setSecurityReportingGrpOrder(Integer securityReportingGrpOrder) {
        this.securityReportingGrpOrder = securityReportingGrpOrder;
    }

    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */
    
    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap m = new LinkedHashMap();
        m.put(KFSConstants.GENERIC_CODE_PROPERTY_NAME, this.code);
        return m;
    }

}
