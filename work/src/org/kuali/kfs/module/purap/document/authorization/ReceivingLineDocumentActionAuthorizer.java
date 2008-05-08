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
package org.kuali.module.purap.document.authorization;

import java.util.Map;

import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.util.GlobalVariables;
import org.kuali.kfs.context.SpringContext;
import org.kuali.module.purap.document.PurchaseOrderDocument;
import org.kuali.module.purap.document.ReceivingLineDocument;
import org.kuali.module.purap.service.ReceivingService;

/**
 * This class determines permissions for a user to view the
 * buttons on Purchase Order Document.
 * 
 */
public class ReceivingLineDocumentActionAuthorizer {

    private ReceivingLineDocument receivingLine;
    
    /**
     * Constructs a ReceivingLineDocumentActionAuthorizer.
     * 
     * @param po A ReceivingLineDocument
     */
    public ReceivingLineDocumentActionAuthorizer(ReceivingLineDocument rl, Map editingMode) {
        
        this.receivingLine = rl;
        
    }

    /**
     * Determines if a receiving document can be created for the purchase order.
     * 
     * @return
     */
    public boolean canCreateCorrection() {        
        return SpringContext.getBean(ReceivingService.class).canCreateReceivingCorrectionDocument(receivingLine);
    }
}
