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
package org.kuali.module.purap.rules;

import static org.kuali.test.fixtures.UserNameFixture.*;

import org.kuali.core.service.DocumentService;
import org.kuali.kfs.context.SpringContext;
import org.kuali.module.financial.document.AccountingDocumentTestUtils;
import org.kuali.module.purap.document.PurchaseOrderDocument;
import org.kuali.module.purap.fixtures.PurchaseOrderChangeDocumentFixture;
import org.kuali.test.ConfigureContext;

@ConfigureContext(session = PARKE)
public class PurchaseOrderPaymentHoldRemoveHoldRuleTest extends PurapRuleTestBase {

    PurchaseOrderPaymentHoldDocumentRule holdRule;
    PurchaseOrderRemoveHoldDocumentRule removeRule;
    PurchaseOrderDocument po;

    protected void setUp() throws Exception {
        super.setUp();
        po = new PurchaseOrderDocument();
        holdRule = new PurchaseOrderPaymentHoldDocumentRule();
        removeRule = new PurchaseOrderRemoveHoldDocumentRule();
    }

    protected void tearDown() throws Exception {
        holdRule = null;
        removeRule = null;
        po = null;
        super.tearDown();
    }
    
    private void savePO(PurchaseOrderDocument po) {
        po.prepareForSave(); 
        try {
            AccountingDocumentTestUtils.saveDocument(po, SpringContext.getBean(DocumentService.class));
        }
        catch (Exception e) {
            throw new RuntimeException("Problems saving PO: " + e);
        }
    }

    @ConfigureContext(session = PARKE, shouldCommitTransactions=true)
    public void testPaymentHoldValidate_Open() {
        po = PurchaseOrderChangeDocumentFixture.STATUS_OPEN.generatePO();
        savePO(po);      
        assertTrue(holdRule.processValidation(po));
    }

    @ConfigureContext(session = PARKE, shouldCommitTransactions=true)
    public void testPaymentHoldValidate_Closed() {
        po = PurchaseOrderChangeDocumentFixture.STATUS_CLOSED.generatePO();
        savePO(po);       
        assertFalse(holdRule.processValidation(po));
    }

    @ConfigureContext(session = RORENFRO, shouldCommitTransactions=true)
    public void testPaymentHoldValidate_InvalidUser() {
        po = PurchaseOrderChangeDocumentFixture.STATUS_OPEN.generatePO();
        savePO(po);        
        assertFalse(holdRule.processValidation(po));
    }

    @ConfigureContext(session = PARKE, shouldCommitTransactions=true)
    public void testRemoveHoldValidate_PaymentHold() {
        po = PurchaseOrderChangeDocumentFixture.STATUS_PAYMENT_HOLD.generatePO();
        savePO(po);
        assertTrue(removeRule.processValidation(po));
    }

    @ConfigureContext(session = PARKE, shouldCommitTransactions=true)
    public void testRemoveHoldValidate_Open() {
        po = PurchaseOrderChangeDocumentFixture.STATUS_OPEN.generatePO();
        savePO(po);
        assertFalse(removeRule.processValidation(po));
    }

    @ConfigureContext(session = PARKE, shouldCommitTransactions=true)
    public void testRemoveHoldValidate_Closed() {
        po = PurchaseOrderChangeDocumentFixture.STATUS_CLOSED.generatePO();
        savePO(po);
        assertFalse(removeRule.processValidation(po));
    }

    @ConfigureContext(session = RORENFRO, shouldCommitTransactions=true)
    public void testRemoveHoldValidate_InvalidUser() {
        po = PurchaseOrderChangeDocumentFixture.STATUS_PAYMENT_HOLD.generatePO();
        savePO(po);
        assertFalse(removeRule.processValidation(po));
    }
}
