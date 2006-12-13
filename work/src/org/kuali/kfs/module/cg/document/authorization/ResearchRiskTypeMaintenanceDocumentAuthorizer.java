/*
 * Copyright 2005-2006 The Kuali Foundation.
 * 
 * $Source: /opt/cvs/kfs/work/src/org/kuali/kfs/module/cg/document/authorization/ResearchRiskTypeMaintenanceDocumentAuthorizer.java,v $
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License")
;
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
package org.kuali.module.kra.routingform.document;

import java.util.List;

import org.kuali.core.authorization.MaintenanceDocumentAuthorizations;
import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.document.MaintenanceDocument;
import org.kuali.core.document.MaintenanceDocumentAuthorizerBase;
import org.kuali.core.service.BusinessObjectService;
import org.kuali.core.util.ObjectUtils;
import org.kuali.core.util.SpringServiceLocator;
import org.kuali.module.kra.budget.KraPropertyConstants;
import org.kuali.module.kra.routingform.bo.ResearchRiskType;
import org.kuali.module.purap.PurapPropertyConstants;
import org.kuali.module.purap.bo.VendorSupplierDiversity;
import org.kuali.module.purap.util.PurapUtils;

public class ResearchRiskTypeMaintenanceDocumentAuthorizer extends MaintenanceDocumentAuthorizerBase {
    
    @Override
    public MaintenanceDocumentAuthorizations getFieldAuthorizations(MaintenanceDocument document, UniversalUser user) {
        
        MaintenanceDocumentAuthorizations auths = new MaintenanceDocumentAuthorizations();
        ResearchRiskType researchRisk = (ResearchRiskType) document.getNewMaintainableObject().getBusinessObject();
        BusinessObjectService service = SpringServiceLocator.getBusinessObjectService();
        ResearchRiskType persistedResearchRisk = (ResearchRiskType) service.retrieve(researchRisk);
        
        //If the research risk exists in db, set read-only fields
        if (ObjectUtils.isNotNull(persistedResearchRisk)) {
            auths.addReadonlyAuthField(KraPropertyConstants.RESEARCH_RISK_TYPE_DESCRIPTION);
            auths.addReadonlyAuthField(KraPropertyConstants.CONTROL_ATTRIBUTE_TYPE_CODE);
        }

        return auths;
    }
}
