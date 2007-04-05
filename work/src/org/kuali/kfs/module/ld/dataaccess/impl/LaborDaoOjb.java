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
package org.kuali.module.labor.dao.ojb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.core.bo.user.PersonPayrollId;
import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.lookup.LookupUtils;
import org.kuali.core.util.KualiDecimal;
import org.kuali.module.budget.bo.CalculatedSalaryFoundationTracker;
import org.kuali.module.gl.util.OJBUtility;
import org.kuali.module.labor.bo.AccountStatusBaseFunds;
import org.kuali.module.labor.bo.AccountStatusCurrentFunds;
import org.kuali.module.labor.dao.LaborDao;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

/**
 * This class is a facade for Labor Distribution DAO balance inquiries
 */
public class LaborDaoOjb extends PersistenceBrokerDaoSupport implements LaborDao {
    private LaborDaoOjb dao;

    /**
     * 
     * @see org.kuali.module.labor.dao.LaborDao#getCSFTrackerData(java.util.Map)
     */
    public Collection getCSFTrackerData(Map fieldValues) {
        Criteria criteria = new Criteria();
        criteria.addAndCriteria(OJBUtility.buildCriteriaFromMap(fieldValues, new CalculatedSalaryFoundationTracker()));
        LookupUtils.applySearchResultsLimit(criteria);
        QueryByCriteria query = QueryFactory.newQuery(CalculatedSalaryFoundationTracker.class, criteria);
        return getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }

    /**
     * 
     * @see org.kuali.module.labor.dao.LaborDao#getCSFTrackerData(java.util.Map)
     */
    public Object getCSFTrackerTotal(Map fieldValues) {
        
        System.out.println("accountNumber:" + fieldValues.get("accountNumber"));
        System.out.println("universityFiscalYear:" + fieldValues.get("universityFiscalYear"));
        System.out.println("chartOfAccountsCode:" + fieldValues.get("chartOfAccountsCode"));
        System.out.println("accountNumber:" + fieldValues.get("accountNumber"));
        System.out.println("subAccountNumber:" + fieldValues.get("subAccountNumber"));       
        System.out.println("financialObjectCode:" + fieldValues.get("financialObjectCode"));
        System.out.println("financialSubObjectCode:" + fieldValues.get("financialSubObjectCode"));
        /*
        SELECT  
        sub_acct_nbr, 
        fin_object_cd, 
        fin_sub_obj_cd, 
        SUM(pos_csf_amt)
    FROM ld_csf_tracker_t 
GROUP BY univ_fiscal_yr, 
        fin_coa_cd, 
        account_nbr, 
        sub_acct_nbr, 
        fin_object_cd, 
        fin_sub_obj_cd;*/
        fieldValues.clear();
        fieldValues.put("accountNumber", "1031400");
        fieldValues.put("universityFiscalYear", "2006");
        fieldValues.put("chartOfAccountsCode", "BL");
        fieldValues.put("financialObjectCode", "2000");
        fieldValues.put("emplid", "0000001441");
        fieldValues.put("positionNumber", "00021695");
        
        Criteria criteria = new Criteria();
        criteria.addAndCriteria(OJBUtility.buildCriteriaFromMap(fieldValues, new CalculatedSalaryFoundationTracker()));
        LookupUtils.applySearchResultsLimit(criteria);
        System.out.println("criteria:" + criteria);
        
        QueryByCriteria query = QueryFactory.newQuery(CalculatedSalaryFoundationTracker.class, criteria);        
        Collection csf= getPersistenceBrokerTemplate().getCollectionByQuery(query);
        System.out.println("CSF Size:" + csf.size());
        System.out.println("CSF:" + csf);
//        ArrayList array = (ArrayList) csf;
//       CalculatedSalaryFoundationTracker csf = (CalculatedSalaryFoundationTracker) test;
        
        return new KualiDecimal("123.00");  
    }

    /**
     * 
     * @see org.kuali.module.labor.dao.LaborDao#getCurrentYearFunds(java.util.Map)
     */
    public Collection getCurrentYearFunds(Map fieldValues) {
        Criteria criteria = new Criteria();
        criteria.addAndCriteria(OJBUtility.buildCriteriaFromMap(fieldValues, new AccountStatusCurrentFunds()));
        LookupUtils.applySearchResultsLimit(criteria);
        QueryByCriteria query = QueryFactory.newQuery(AccountStatusCurrentFunds.class, criteria);
        Collection ledgerBalances = getPersistenceBrokerTemplate().getCollectionByQuery(query);
        for (Iterator iter = ledgerBalances.iterator(); iter.hasNext();) {
            AccountStatusCurrentFunds currentFund = (AccountStatusCurrentFunds) ledgerBalances;            
            new PersonPayrollId(currentFund.getEmplid());
            UniversalUser universalUser = null;
            
//            try{
  //              universalUser = SpringServiceLocator.getUniversalUserService().getUniversalUser(empl);
    //        }catch(UserNotFoundException e){
      //          return LaborConstants.BalanceInquiries.UnknownPersonName;
        //    }
       //     return universalUser.getPersonName();
        }
       return ledgerBalances;
    }

    /**
     * 
     * @see org.kuali.module.labor.dao.LaborDao#getBaseFunds(java.util.Map)
     */
    public Collection getBaseFunds(Map fieldValues) {
        Criteria criteria = new Criteria();
        criteria.addAndCriteria(OJBUtility.buildCriteriaFromMap(fieldValues, new AccountStatusBaseFunds()));
        LookupUtils.applySearchResultsLimit(criteria);
        QueryByCriteria query = QueryFactory.newQuery(AccountStatusBaseFunds.class, criteria);
        return getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }
    
    /**
     * 
     * @see org.kuali.module.labor.dao.LaborDao#getCurrentFunds(java.util.Map)
     */
    public Collection getCurrentFunds(Map fieldValues) {
        Criteria criteria = new Criteria();
        criteria.addAndCriteria(OJBUtility.buildCriteriaFromMap(fieldValues, new AccountStatusCurrentFunds()));
        LookupUtils.applySearchResultsLimit(criteria);
        QueryByCriteria query = QueryFactory.newQuery(AccountStatusCurrentFunds.class, criteria);
        return getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }
}