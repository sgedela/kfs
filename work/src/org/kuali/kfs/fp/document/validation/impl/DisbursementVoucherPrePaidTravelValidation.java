/*
 * Copyright 2008 The Kuali Foundation.
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
package org.kuali.kfs.fp.document.validation.impl;

import org.kuali.kfs.fp.businessobject.DisbursementVoucherPreConferenceDetail;
import org.kuali.kfs.fp.document.DisbursementVoucherDocument;
import org.kuali.kfs.fp.document.service.DisbursementVoucherTaxService;
import org.kuali.kfs.sys.KFSConstants;
import org.kuali.kfs.sys.KFSKeyConstants;
import org.kuali.kfs.sys.KFSPropertyConstants;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.document.AccountingDocument;
import org.kuali.kfs.sys.document.validation.GenericValidation;
import org.kuali.kfs.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.kfs.sys.service.ParameterEvaluator;
import org.kuali.kfs.sys.service.ParameterService;
import org.kuali.rice.kns.service.DictionaryValidationService;
import org.kuali.rice.kns.util.ErrorMap;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KualiDecimal;

public class DisbursementVoucherPrePaidTravelValidation extends GenericValidation {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DisbursementVoucherPrePaidTravelValidation.class);

    private ParameterService parameterService;
    private AccountingDocument accountingDocumentForValidation;

    /**
     * @see org.kuali.kfs.sys.document.validation.Validation#validate(org.kuali.kfs.sys.document.validation.event.AttributedDocumentEvent)
     */
    public boolean validate(AttributedDocumentEvent event) {
        LOG.info("validate start");
        
        DisbursementVoucherDocument document = (DisbursementVoucherDocument) accountingDocumentForValidation;
        DisbursementVoucherPreConferenceDetail preConferenceDetail = document.getDvPreConferenceDetail();
        
        if (!isTravelPrepaidPaymentReason(document)) {
            return true;
        }
        
        ErrorMap errors = GlobalVariables.getErrorMap();
        errors.addToErrorPath(KFSPropertyConstants.DOCUMENT);

        errors.addToErrorPath(KFSPropertyConstants.DV_PRE_CONFERENCE_DETAIL);
        SpringContext.getBean(DictionaryValidationService.class).validateBusinessObjectsRecursively(preConferenceDetail, 1);
        if (!errors.isEmpty()) {
            errors.removeFromErrorPath(KFSPropertyConstants.DV_PRE_CONFERENCE_DETAIL);
            errors.addToErrorPath(KFSPropertyConstants.DOCUMENT);
            return false;
        }

        /* check conference end date is not before conference start date */
        if (preConferenceDetail.getDisbVchrConferenceEndDate().compareTo(preConferenceDetail.getDisbVchrConferenceStartDate()) < 0) {
            errors.putError(KFSPropertyConstants.DISB_VCHR_CONFERENCE_END_DATE, KFSKeyConstants.ERROR_DV_CONF_END_DATE);
        }

        /* total on prepaid travel must equal Check Total */
        /* if tax has been taken out, need to add back in the tax amount for the check */
        KualiDecimal paidAmount = document.getDisbVchrCheckTotalAmount();
        paidAmount = paidAmount.add(SpringContext.getBean(DisbursementVoucherTaxService.class).getNonResidentAlienTaxAmount(document));
        if (paidAmount.compareTo(preConferenceDetail.getDisbVchrConferenceTotalAmt()) != 0) {
            errors.putErrorWithoutFullErrorPath(KFSConstants.GENERAL_PREPAID_TAB_ERRORS, KFSKeyConstants.ERROR_DV_PREPAID_CHECK_TOTAL);
        }

        errors.removeFromErrorPath(KFSPropertyConstants.DV_PRE_CONFERENCE_DETAIL);
        errors.removeFromErrorPath(KFSPropertyConstants.DOCUMENT);

        return errors.isEmpty();
    }
    
    /**
     * Returns whether the document's payment reason is for prepaid travel
     * 
     * @param disbursementVoucherDocument
     * @return true if payment reason is for pre-paid travel reason
     */
    private boolean isTravelPrepaidPaymentReason(DisbursementVoucherDocument disbursementVoucherDocument) {
        ParameterEvaluator travelPrepaidPaymentReasonEvaluator = parameterService.getParameterEvaluator(DisbursementVoucherDocument.class, DisbursementVoucherRuleConstants.PREPAID_TRAVEL_PAY_REASONS_PARM_NM, disbursementVoucherDocument.getDvPayeeDetail().getDisbVchrPaymentReasonCode());
        return travelPrepaidPaymentReasonEvaluator.evaluationSucceeds();
    }

    /**
     * Sets the accountingDocumentForValidation attribute value.
     * 
     * @param accountingDocumentForValidation The accountingDocumentForValidation to set.
     */
    public void setAccountingDocumentForValidation(AccountingDocument accountingDocumentForValidation) {
        this.accountingDocumentForValidation = accountingDocumentForValidation;
    }

    /**
     * Sets the parameterService attribute value.
     * @param parameterService The parameterService to set.
     */
    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    /**
     * Gets the accountingDocumentForValidation attribute. 
     * @return Returns the accountingDocumentForValidation.
     */
    public AccountingDocument getAccountingDocumentForValidation() {
        return accountingDocumentForValidation;
    }
}
