/*
 * The Kuali Financial System, a comprehensive financial management system for higher education.
 *
 * Copyright 2005-2014 The Kuali Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kuali.kfs.module.cg.document;

import static org.kuali.kfs.sys.KFSPropertyConstants.AWARD_ACCOUNTS;
import static org.kuali.kfs.sys.KFSPropertyConstants.AWARD_FUND_MANAGERS;
import static org.kuali.kfs.sys.KFSPropertyConstants.AWARD_PROJECT_DIRECTORS;
import static org.kuali.kfs.sys.KFSPropertyConstants.AWARD_SUBCONTRACTORS;
import static org.kuali.kfs.sys.KFSPropertyConstants.DOCUMENT;
import static org.kuali.kfs.sys.KFSPropertyConstants.NEW_MAINTAINABLE_OBJECT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.kfs.integration.ar.AccountsReceivableModuleBillingService;
import org.kuali.kfs.module.cg.CGConstants;
import org.kuali.kfs.module.cg.CGPropertyConstants;
import org.kuali.kfs.module.cg.businessobject.Award;
import org.kuali.kfs.module.cg.businessobject.AwardAccount;
import org.kuali.kfs.module.cg.businessobject.AwardFundManager;
import org.kuali.kfs.module.cg.businessobject.AwardOrganization;
import org.kuali.kfs.module.cg.businessobject.AwardProjectDirector;
import org.kuali.kfs.module.cg.businessobject.AwardSubcontractor;
import org.kuali.kfs.module.cg.businessobject.CGProjectDirector;
import org.kuali.kfs.module.cg.businessobject.Proposal;
import org.kuali.kfs.module.cg.document.validation.impl.AwardRuleUtil;
import org.kuali.kfs.module.cg.service.ContractsAndGrantsBillingService;
import org.kuali.kfs.sys.KFSConstants;
import org.kuali.kfs.sys.KFSKeyConstants;
import org.kuali.kfs.sys.KFSPropertyConstants;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.Maintainable;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.maintenance.MaintenanceLock;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * Methods for the Award maintenance document UI.
 */
public class AwardMaintainableImpl extends ContractsGrantsBillingMaintainable {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AwardMaintainableImpl.class);

    private static volatile AccountsReceivableModuleBillingService accountsReceivableModuleBillingService;
    /**
     * This method is called for refreshing the Agency before display to show the full name in case the agency number was changed by
     * hand before any submit that causes a redisplay.
     */
    @Override
    public void processAfterRetrieve() {
        refreshAward(false);
        super.processAfterRetrieve();
    }

     /**
     * Not to copy over the Accounts tab, Predetermined tab, Milestone schedule tab, award account tab and award budgets tab when
     * copying
     */
    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> parameters) {
        super.processAfterCopy(document, parameters);

        Award oldAward = (Award) document.getOldMaintainableObject().getBusinessObject();
        Award newAward = (Award) document.getNewMaintainableObject().getBusinessObject();

        if (ObjectUtils.isNotNull(oldAward) && ObjectUtils.isNotNull(newAward)){
            // Clear Accounts
            oldAward.getAwardAccounts().clear();
            newAward.getAwardAccounts().clear();
        }

        getAward().setMilestoneSchedule(SpringContext.getBean(AccountsReceivableModuleBillingService.class).getMilestoneSchedule());
        getAward().setPredeterminedBillingSchedule(SpringContext.getBean(AccountsReceivableModuleBillingService.class).getPredeterminedBillingSchedule());
    }

    /**
     * Setting the initial field value to that specified by the default invoice parameter and default billing frequency parameter,
     * otherwise defaulting to invoice by account and monthly, respectively.
     */
    @Override
    public void processAfterNew(MaintenanceDocument document, Map<String, String[]> parameters) {
        super.processAfterNew(document, parameters);

        // Retrieving Default Invoicing Option
        String defaultInvoiceParm = getAccountsReceivableModuleBillingService().getDefaultInvoicingOption();

        // Retrieving Default Billing Schedule
        String defaultBillingScheduleParm = getAccountsReceivableModuleBillingService().getDefaultBillingFrequency();

        // Set Invoicing Option
        getAward().setInvoicingOptionCode(defaultInvoiceParm);

        // Set Billing Schedule
        if (StringUtils.isNotBlank(defaultBillingScheduleParm)) {
            getAward().setBillingFrequencyCode(defaultBillingScheduleParm);
        }
        else {
        	getAward().setBillingFrequencyCode(CGConstants.MONTHLY_BILLING_SCHEDULE_CODE);
        }
    }

    /**
     * This method is called for refreshing the Agency before a save to display the full name in case the agency number was changed
     * by hand just before the save.
     */
    @Override
    public void prepareForSave() {
        refreshAward(false);
        List<AwardProjectDirector> directors = getAward().getAwardProjectDirectors();
        if (directors.size() == 1) {
            directors.get(0).setAwardPrimaryProjectDirectorIndicator(true);
        }
        List<AwardFundManager> managers = getAward().getAwardFundManagers();
        if (managers != null && managers.size() == 1) {
            managers.get(0).setPrimaryFundManagerIndicator(true);
        }
        List<AwardOrganization> organizations = getAward().getAwardOrganizations();
        if (organizations.size() == 1) {
            organizations.get(0).setAwardPrimaryOrganizationIndicator(true);
        }
        // need to populate the synthetic keys for these records
        // since we can not depend on the keys which exist, we need to determine all those which could match
        // so we can avoid them
        List<AwardSubcontractor> awardSubcontractors = getAward().getAwardSubcontractors();
        if (awardSubcontractors != null && !awardSubcontractors.isEmpty()) {
            // convert the list into a map of lists containing the used award subcontractor number/amendment number
            Map<String, List<AwardSubcontractor>> subcontractorAwardMap = new HashMap<String, List<AwardSubcontractor>>();
            List<AwardSubcontractor> newSubcontractorRecords = new ArrayList<AwardSubcontractor>();
            for (AwardSubcontractor awardSubcontractor : awardSubcontractors) {
                if (!StringUtils.isBlank(awardSubcontractor.getAwardSubcontractorNumber())) {
                    // already has key - add to map
                    if (!subcontractorAwardMap.containsKey(awardSubcontractor.getSubcontractorNumber())) {
                        subcontractorAwardMap.put(awardSubcontractor.getSubcontractorNumber(), new ArrayList<AwardSubcontractor>());
                    }
                    subcontractorAwardMap.get(awardSubcontractor.getSubcontractorNumber()).add(awardSubcontractor);
                }
                else {
                    // new record, add to new map
                    newSubcontractorRecords.add(awardSubcontractor);
                }
            }

            // now, loop over the new records
            for (AwardSubcontractor awardSubcontractor : newSubcontractorRecords) {
                String awardSubcontractorNumber = "1";
                String awardSubcontractorAmendmentNumber = "1";
                // get the other ones for the same subcontractor
                List<AwardSubcontractor> oldSubcontractors = subcontractorAwardMap.get(awardSubcontractor.getSubcontractorNumber());
                if (oldSubcontractors != null) {
                    // we have a hit - find the first non-used number
                    // build an array from the unsorted list
                    boolean[][] nums = new boolean[100][100];
                    for (AwardSubcontractor oldSub : oldSubcontractors) {
                        try {
                            nums[Integer.valueOf(oldSub.getAwardSubcontractorNumber())][Integer.valueOf(oldSub.getAwardSubcontractorAmendmentNumber())] = true;
                        }
                        catch (NumberFormatException ex) {
                            // do nothing
                            LOG.warn("Unexpected non-integer award subcontractor / amendment number: " + oldSub.getAwardSubcontractorNumber() + " / " + oldSub.getAwardSubcontractorAmendmentNumber());
                        }
                    }
                    // iterate over the array to get the first empty value
                    // loop over the awardSubcontractorNumbers first
                    boolean isFoundNumbers = false;
                    for (int i = 1; i <= 99; i++) {
                        for (int j = 1; j <= 99; j++) {
                            if (!nums[j][i]) {
                                // save the values
                                awardSubcontractorNumber = Integer.toString(j);
                                awardSubcontractorAmendmentNumber = Integer.toString(i);
                                // mark the cell as used before the next pass
                                nums[j][i] = true;
                                // just a flag to allow us to break out of both loops
                                isFoundNumbers = true;
                                break;
                            }
                        }
                        if (isFoundNumbers) {
                            break;
                        }
                        // JHK - yes, this will break down if there are more than 9801 subcontracts
                        // however, the UI will probably break down far before then...
                    }
                }
                awardSubcontractor.setAwardSubcontractorNumber(awardSubcontractorNumber);
                awardSubcontractor.setAwardSubcontractorAmendmentNumber(awardSubcontractorAmendmentNumber);
            }

        }
        super.prepareForSave();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void processAfterPost(MaintenanceDocument document, Map<String, String[]> parameters) {
        super.processAfterPost(document, parameters);
        Award newAward = (Award) document.getNewMaintainableObject().getBusinessObject();

        // KFSTP-16 Check for null proposal number before saving
        if (ObjectUtils.isNotNull(newAward)){
            Proposal proposal = newAward.getProposal();

            if (ObjectUtils.isNotNull(proposal) && proposal.getProposalNumber() != null){
                SpringContext.getBean(BusinessObjectService.class).save(newAward.getProposal());
            }
        }
    }

    /**
     * This method is called for refreshing the Agency after a lookup to display its full name without AJAX.
     *
     * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#refresh(java.lang.String, java.util.Map,
     *      org.kuali.rice.kns.document.MaintenanceDocument)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void refresh(String refreshCaller, Map fieldValues, MaintenanceDocument document) {
        if (StringUtils.equals(CGPropertyConstants.PROPOSAL_LOOKUPABLE, (String) fieldValues.get(KFSConstants.REFRESH_CALLER))) {

            boolean isAwarded = AwardRuleUtil.isProposalAwarded(getAward());
            if (isAwarded) {
                String pathToMaintainable = DOCUMENT + "." + NEW_MAINTAINABLE_OBJECT;
                GlobalVariables.getMessageMap().addToErrorPath(pathToMaintainable);
                GlobalVariables.getMessageMap().putError(KFSPropertyConstants.PROPOSAL_NUMBER, KFSKeyConstants.ERROR_AWARD_PROPOSAL_AWARDED, new String[] { getAward().getProposalNumber().toString() });
                GlobalVariables.getMessageMap().removeFromErrorPath(pathToMaintainable);
            }

            // SEE KULCG-315 for details on why this code is commented out.
            // if (AwardRuleUtil.isProposalInactive(getAward())) {
            // GlobalVariables.getMessageMap().putError(KFSPropertyConstants.PROPOSAL_NUMBER,
            // KFSKeyConstants.ERROR_AWARD_PROPOSAL_INACTIVE, new String[] { getAward().getProposalNumber().toString() });
            // }

            // copy over proposal values after refresh
            if (!isAwarded) {
                refreshAward(true);
                fieldValues.put(KFSConstants.REFERENCES_TO_REFRESH, "proposal");
                super.refresh(refreshCaller, fieldValues, document);
                getAward().populateFromProposal(getAward().getProposal());
                refreshAward(true);
            }
        }
        else {
            refreshAward(KFSConstants.KUALI_LOOKUPABLE_IMPL.equals(fieldValues.get(KFSConstants.REFRESH_CALLER)));
            super.refresh(refreshCaller, fieldValues, document);
        }

    }

    /**
     * Load related objects from the database as needed.
     *
     * @param refreshFromLookup
     */
    private void refreshAward(boolean refreshFromLookup) {
        Award award = getAward();
        award.refreshNonUpdateableReferences();

        getNewCollectionLine(AWARD_SUBCONTRACTORS).refreshNonUpdateableReferences();
        getNewCollectionLine(AWARD_PROJECT_DIRECTORS).refreshNonUpdateableReferences();
        getNewCollectionLine(AWARD_FUND_MANAGERS).refreshNonUpdateableReferences();
        getNewCollectionLine(AWARD_ACCOUNTS).refreshNonUpdateableReferences();

        // the org list doesn't need any refresh
        refreshNonUpdateableReferences(award.getAwardOrganizations());
        refreshNonUpdateableReferences(award.getAwardAccounts());
        refreshNonUpdateableReferences(award.getAwardSubcontractors());
        refreshAwardProjectDirectors(refreshFromLookup);
        refreshAwardFundManagers(refreshFromLookup);
    }

    /**
     * Refresh the collection of associated AwardProjectDirectors.
     *
     * @param refreshFromLookup a lookup returns only the primary key, so ignore the secondary key when true
     */
    private void refreshAwardProjectDirectors(boolean refreshFromLookup) {
        if (refreshFromLookup) {
            getNewCollectionLine(AWARD_PROJECT_DIRECTORS).refreshNonUpdateableReferences();
            refreshNonUpdateableReferences(getAward().getAwardProjectDirectors());

            getNewCollectionLine(AWARD_ACCOUNTS).refreshNonUpdateableReferences();
            refreshNonUpdateableReferences(getAward().getAwardAccounts());
        }
        else {
            refreshWithSecondaryKey((AwardProjectDirector) getNewCollectionLine(AWARD_PROJECT_DIRECTORS));
            for (AwardProjectDirector projectDirector : getAward().getAwardProjectDirectors()) {
                refreshWithSecondaryKey(projectDirector);
            }

            refreshWithSecondaryKey((AwardAccount) getNewCollectionLine(AWARD_ACCOUNTS));
            for (AwardAccount account : getAward().getAwardAccounts()) {
                refreshWithSecondaryKey(account);
            }
        }
    }

    /**
     * Refresh the collection of associated AwardFundManagers.
     *
     * @param refreshFromLookup a lookup returns only the primary key, so ignore the secondary key when true
     */
    private void refreshAwardFundManagers(boolean refreshFromLookup) {
        if (refreshFromLookup) {
            getNewCollectionLine(AWARD_FUND_MANAGERS).refreshNonUpdateableReferences();
            refreshNonUpdateableReferences(getAward().getAwardFundManagers());

            getNewCollectionLine(AWARD_ACCOUNTS).refreshNonUpdateableReferences();
            refreshNonUpdateableReferences(getAward().getAwardAccounts());
        }
        else {
            refreshWithSecondaryKey((AwardFundManager) getNewCollectionLine(AWARD_FUND_MANAGERS));
            for (AwardFundManager fundManager : getAward().getAwardFundManagers()) {
                refreshWithSecondaryKey(fundManager);
            }
        }
    }

    /**
     * @param collection
     */
    private static void refreshNonUpdateableReferences(Collection<? extends PersistableBusinessObject> collection) {
        for (PersistableBusinessObject item : collection) {
            item.refreshNonUpdateableReferences();
        }
    }

    /**
     * Refreshes the reference to ProjectDirector, giving priority to its secondary key. Any secondary key that it has may be user
     * input, so that overrides the primary key, setting the primary key. If its primary key is blank or nonexistent, then leave the
     * current reference as it is, because it may be a nonexistent instance which is holding the secondary key (the username, i.e.,
     * principalName) so we can redisplay it to the user for correction. If it only has a primary key then use that, because it may
     * be coming from the database, without any user input.
     *
     * @param director the ProjectDirector to refresh
     */
    private static void refreshWithSecondaryKey(CGProjectDirector director) {
        Person cgdir = director.getProjectDirector();
        if (ObjectUtils.isNotNull(cgdir)) {
            String secondaryKey = cgdir.getPrincipalName();
            if (StringUtils.isNotBlank(secondaryKey)) {
                Principal dir = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(secondaryKey);
                director.setPrincipalId(dir == null ? null : dir.getPrincipalId());
            }
            if (StringUtils.isNotBlank(director.getPrincipalId())) {
                Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(director.getPrincipalId());
                if (principal != null) {
                    ((PersistableBusinessObject) director).refreshNonUpdateableReferences();
                }
            }
        }
    }

    /**
     * Refreshes the reference to FundManager, giving priority to its secondary key. Any secondary key that it has may be user
     * input, so that overrides the primary key, setting the primary key. If its primary key is blank or nonexistent, then leave the
     * current reference as it is, because it may be a nonexistent instance which is holding the secondary key (the username, i.e.,
     * principalName) so we can redisplay it to the user for correction. If it only has a primary key then use that, because it may
     * be coming from the database, without any user input.
     *
     * @param director the FundManager to refresh
     */
    private static void refreshWithSecondaryKey(AwardFundManager fundManager) {
        Person cdFundMgr = fundManager.getFundManager();
        if (ObjectUtils.isNotNull(cdFundMgr)) {
            String secondaryKey = cdFundMgr.getPrincipalName();
            if (StringUtils.isNotBlank(secondaryKey)) {
                Person fundMgr = SpringContext.getBean(PersonService.class).getPersonByPrincipalName(secondaryKey);
                fundManager.setPrincipalId(fundMgr == null ? null : fundMgr.getPrincipalId());
            }
            if (StringUtils.isNotBlank(fundManager.getPrincipalId())) {
                Person person = SpringContext.getBean(PersonService.class).getPerson(fundManager.getPrincipalId());
                if (person != null) {
                    ((PersistableBusinessObject) fundManager).refreshNonUpdateableReferences();
                }
            }
        }
    }

    /**
     * Gets the underlying Award.
     *
     * @return
     */
    public Award getAward() {
        return (Award) getBusinessObject();
    }

    /**
     * Called for refreshing the {@link Subcontractor} on {@link ProposalSubcontractor} before adding to the proposalSubcontractors
     * collection on the proposal. this is to ensure that the summary fields are show correctly. i.e. {@link Subcontractor} name
     *
     * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#addNewLineToCollection(java.lang.String)
     */
    @Override
    public void addNewLineToCollection(String collectionName) {
        refreshAward(false);
        super.addNewLineToCollection(collectionName);
    }

    /**
     * Called to manipulate which sections are shown on the Award Maintenance document
     *
     * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#getSections(org.kuali.rice.kns.document.MaintenanceDocument,
     *      org.kuali.rice.kns.maintenance.Maintainable)
     */
    @Override
    public List getSections(MaintenanceDocument document, Maintainable oldMaintainable) {

        Award award = getAward();
        // To set billing frequency to "LOC Billing" if LOC Fund is chosen - Cannot be done via scripts, as they get
        // refreshed back to old values.
        // To be optimized if there is a better approach.
        if (StringUtils.isNotEmpty(award.getLetterOfCreditFundCode()) && StringUtils.isNotBlank(award.getLetterOfCreditFundCode())) {
        	award.setBillingFrequencyCode(CGConstants.LOC_BILLING_SCHEDULE_CODE);
        }

        return super.getSections(document, oldMaintainable);
    }

    /**
     * If the Contracts & Grants Billing (CGB) enhancement is disabled, we don't want to
     * process sections only related to CGB.
     *
     * @return list of section ids to ignore
     */
    @Override
    protected Collection<?> getSectionIdsToIgnore() {
        if (!SpringContext.getBean(AccountsReceivableModuleBillingService.class).isContractsGrantsBillingEnhancementActive()) {
            return SpringContext.getBean(ContractsAndGrantsBillingService.class).getAwardContractsGrantsBillingSectionIds();
        } else {
            return CollectionUtils.EMPTY_COLLECTION;
        }
    }

    /**
     * This method overrides the parent method to check the status of the award document and change the linked
     * {@link ProposalStatus} to A (Approved) if the {@link Award} is now in approved status.
     *
     * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#doRouteStatusChange(org.kuali.rice.krad.bo.DocumentHeader)
     */
    @Override
    public void doRouteStatusChange(DocumentHeader header) {
        super.doRouteStatusChange(header);

        Award award = getAward();
        WorkflowDocument workflowDoc = header.getWorkflowDocument();

        // Use the isProcessed() method so this code is only executed when the final approval occurs
        if (workflowDoc.isProcessed()) {
            Proposal proposal = award.getProposal();
            proposal.setProposalStatusCode(Proposal.AWARD_CODE);
            SpringContext.getBean(BusinessObjectService.class).save(proposal);
        }

    }

    @Override
    public List<MaintenanceLock> generateMaintenanceLocks() {
        List<MaintenanceLock> locks = super.generateMaintenanceLocks();
        return locks;
    }

    public static AccountsReceivableModuleBillingService getAccountsReceivableModuleBillingService() {
        if (accountsReceivableModuleBillingService == null) {
            accountsReceivableModuleBillingService = SpringContext.getBean(AccountsReceivableModuleBillingService.class);
        }
        return accountsReceivableModuleBillingService;
    }
}
