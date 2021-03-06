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
package org.kuali.kfs.gl.batch.service.impl;

import org.kuali.kfs.coa.businessobject.OrganizationReversion;
import org.kuali.kfs.coa.businessobject.OrganizationReversionDetail;
import org.kuali.kfs.coa.service.OrganizationReversionService;
import org.kuali.kfs.coa.service.impl.OrganizationReversionServiceImpl;
import org.kuali.kfs.sys.KFSConstants;
import org.kuali.kfs.sys.service.NonTransactional;

/**
 * A mock implementation of OrganizationReversionService, used by the OrganizationReversionLogicTest
 * @see org.kuali.kfs.gl.service.OrganizationReversionLogicTest
 */

@NonTransactional
public class OrganizationReversionMockServiceImpl extends OrganizationReversionServiceImpl implements OrganizationReversionService {
    public static final String DEFAULT_BUDGET_REVERSION_CHART = "BL";
    public static final String DEFAULT_BUDGET_REVERSION_ACCOUNT = "0211301";
    public static final String DEFAULT_CASH_REVERSION_CHART = "BL";
    public static final String DEFAULT_CASH_REVERSION_ACCOUNT = "0211401";

    /**
     * Always returns the same mock organization reversion record, no matter what keys are handed to it
     * @param fiscal year the fiscal year of the organization reversion record that will be returned
     * @param chart the chart of the organization reversion record that will be returned
     * @param orgCode the organization code of the organization reversion record that will be returned
     * @return an OrganizationReversion record with the given chart, org, and year, but everything else--budget reversion and cash reversion account information and category details--pre-set
     * @see org.kuali.kfs.coa.service.impl.OrganizationReversionServiceImpl#getByPrimaryId(java.lang.Integer, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public OrganizationReversion getByPrimaryId(Integer fiscalYear, String chartCode, String orgCode) {
        // always return the same OrganizationReversion no matter what
        OrganizationReversion orgRev = new OrganizationReversion();
        orgRev.setChartOfAccountsCode(chartCode);
        orgRev.setUniversityFiscalYear(fiscalYear);
        orgRev.setOrganizationCode(orgCode);
        orgRev.setBudgetReversionChartOfAccountsCode(DEFAULT_BUDGET_REVERSION_CHART);
        orgRev.setBudgetReversionAccountNumber(DEFAULT_BUDGET_REVERSION_ACCOUNT);
        orgRev.setCashReversionFinancialChartOfAccountsCode(DEFAULT_CASH_REVERSION_CHART);
        orgRev.setCashReversionAccountNumber(DEFAULT_CASH_REVERSION_ACCOUNT);
        orgRev.setCarryForwardByObjectCodeIndicator(true);

        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C01", KFSConstants.RULE_CODE_A));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C02", KFSConstants.RULE_CODE_C1));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C03", KFSConstants.RULE_CODE_C2));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C04", KFSConstants.RULE_CODE_N1));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C05", KFSConstants.RULE_CODE_N2));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C06", KFSConstants.RULE_CODE_A));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C07", KFSConstants.RULE_CODE_A));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C08", KFSConstants.RULE_CODE_R1));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C09", KFSConstants.RULE_CODE_R2));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C10", KFSConstants.RULE_CODE_A));
        orgRev.addOrganizationReversionDetail(createDetail(fiscalYear, chartCode, orgCode, "C11", KFSConstants.RULE_CODE_A));
        
        orgRev.refreshReferenceObject("chartOfAccounts");

        return orgRev;
    }

    /**
     * Creates a mock OrganizationReversionDetail record, based on the parameters
     * 
     * @param fiscalYear the fiscal year to set
     * @param chartCode the chart to set
     * @param orgCode the org code to set
     * @param categoryCode the category code of the record
     * @param categoryAlgorithm the algorithm to use for the record
     */
    private OrganizationReversionDetail createDetail(Integer fiscalYear, String chartCode, String orgCode, String categoryCode, String categoryAlgorithm) {
        OrganizationReversionDetail detail = new OrganizationReversionDetail();
        detail.setUniversityFiscalYear(new Integer(fiscalYear.intValue() - 1));
        detail.setChartOfAccountsCode(chartCode);
        detail.setOrganizationCode(orgCode);
        detail.setOrganizationReversionCategoryCode(categoryCode);
        detail.setOrganizationReversionCode(categoryAlgorithm);
        detail.setOrganizationReversionObjectCode("5000");
        detail.setActive(true);
        return detail;
    }


}
