/*
 * Copyright (c) 2004, 2005 The National Association of College and University Business Officers,
 * Cornell University, Trustees of Indiana University, Michigan State University Board of Trustees,
 * Trustees of San Joaquin Delta College, University of Hawai'i, The Arizona Board of Regents on
 * behalf of the University of Arizona, and the r*smart group.
 * 
 * Licensed under the Educational Community License Version 1.0 (the "License"); By obtaining,
 * using and/or copying this Original Work, you agree that you have read, understand, and will
 * comply with the terms and conditions of the Educational Community License.
 * 
 * You may obtain a copy of the License at:
 * 
 * http://kualiproject.org/license.html
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.kuali.module.gl.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.kuali.core.service.PersistenceService;
import org.kuali.core.util.SpringServiceLocator;
import org.kuali.module.gl.bo.SufficientFundBalances;
import org.kuali.module.gl.bo.SufficientFundRebuild;
import org.kuali.module.gl.dao.SufficientFundBalancesDao;
import org.kuali.module.gl.dao.SufficientFundRebuildDao;
import org.kuali.module.gl.dao.UnitTestSqlDao;
import org.kuali.test.KualiTestBaseWithSpring;
import org.kuali.test.WithTestSpringContext;
import org.springframework.beans.factory.BeanFactory;

@WithTestSpringContext
public class SufficientFundsRebuilderServiceTest extends KualiTestBaseWithSpring {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ScrubberServiceTest.class);

    protected BeanFactory beanFactory;
    private SufficientFundsRebuilderService sufficientFundsRebuilderService = null;
    private SufficientFundRebuildDao sufficientFundRebuildDao = null;
    private SufficientFundBalancesDao sufficientFundBalancesDao = null;
    protected PersistenceService persistenceService;
    protected UnitTestSqlDao unitTestSqlDao = null;

    protected void setUp() throws Exception {
        super.setUp();
        LOG.debug("setUp() started");

        beanFactory = SpringServiceLocator.getBeanFactory();

        sufficientFundsRebuilderService = (SufficientFundsRebuilderService) beanFactory.getBean("glSufficientFundsRebuilderService");
        sufficientFundRebuildDao = (SufficientFundRebuildDao) beanFactory.getBean("glSufficientFundRebuildDao");
        sufficientFundBalancesDao = (SufficientFundBalancesDao) beanFactory.getBean("glSufficientFundBalancesDao");
        persistenceService = (PersistenceService) beanFactory.getBean("persistenceService");
        unitTestSqlDao = (UnitTestSqlDao) beanFactory.getBean("glUnitTestSqlDao");
    }

    // testAddedSFBLRecords
    public void testConversion() throws Exception {
        String[] expectedOutput = new String[] { 
                "2007BL2220090    H                1                1                1",
                "2007BL2231406BCASL                0                0                0",
                "2007BL2231406CASHL                0                0                0",
                "2007BL2231406FUBLL                0                0                0",
                "2007BL2231406PRINL                0           180.35                0",
                "2007BL2231406RESVL                0                0                0",
                "2007BL2231406S&E L            12000             9.55                0",
                "2007BL2231406TRAVL                0           2558.9                0",
                "2007BL2231407GENXC                1                1                1",
                "2007BL22314081800O                0                0                0",
                "2007BL22314084938O                0           348.27                0",
                "2007BL22314085000O                0                0                0",
                "2007BL22314085215O                0              100                0",
                "2007BL22314088000O                0                0                0",
                "2007BL22314089041O                0                0                0",
                "2007BL22314089892O                0                0                0",
                "2007BL22314089899O                0                0                0"
        };

        updateAccount();
        clearSufficientFundBalanceTable();
        clearSufficientFundRebuildTable();
        populateGLSFRebuildTableForConversion();
        populateGLSFBalanceTableForConversion();
        prepareGLBalancesTable();
        sufficientFundsRebuilderService.rebuildSufficientFunds();
        assertSFRBEmpty();
        assertSFBLEntries(expectedOutput);
    }

    // testAddedSFBLRecords
    public void testAddedSFBLRecords() throws Exception {
        String[] expectedOutput = new String[] { 
                "2007BL2220090    H         10756.57                0            503.5",
                "2007BL2231406BCASL                0                0                0",
                "2007BL2231406CASHL                0                0                0",
                "2007BL2231406FUBLL                0                0                0",
                "2007BL2231406PRINL                0           180.35                0",
                "2007BL2231406RESVL                0                0                0",
                "2007BL2231406S&E L            12000             9.55                0",
                "2007BL2231406TRAVL                0           2558.9                0",
                "2007BL2231407ASSTC                0                0                0",
                "2007BL2231407FDBLC                0                0                0",
                "2007BL2231407GENXC                0          -984.12                0",
                "2007BL2231407LIABC                0                0                0",
                "2007BL2231407OTREC                0                0                0",
                "2007BL2231407RSRXC                0                0                0",
                "2007BL22314081800O                0                0                0",
                "2007BL22314084938O                0           348.27                0",
                "2007BL22314085000O                0                0                0",
                "2007BL22314085215O                0              100                0",
                "2007BL22314088000O                0                0                0",
                "2007BL22314089041O                0                0                0",
                "2007BL22314089892O                0                0                0",
                "2007BL22314089899O                0                0                0",
                "2007BL2231415    A            12000           2748.8                0"
        };

        updateAccount();
        clearSufficientFundBalanceTable();
        clearSufficientFundRebuildTable();
        populateGLSFRebuildTable();
        prepareGLBalancesTable();
        sufficientFundsRebuilderService.rebuildSufficientFunds();
        assertSFRBEmpty();
        assertSFBLEntries(expectedOutput);
    }

    protected void loadInputTransactions(String[] transactions) {
        for (int i = 0; i < transactions.length; i++) {
            createSFRB(transactions[i]);
        }
    }

    protected SufficientFundRebuild createSFRB(String line) {
        SufficientFundRebuild sfrb = new SufficientFundRebuild(line);

        // This is being done to fool the caching. If it isn't done, when
        // we try to retrieve this entry later, none of the referenced tables will
        // be loaded.
        persistenceService.retrieveNonKeyFields(sfrb);

        sufficientFundRebuildDao.save(sfrb);
        return sfrb;
    }

    protected void clearSufficientFundBalanceTable() {
        unitTestSqlDao.sqlCommand("delete from gl_sf_balances_t");
    }

    protected void clearSufficientFundRebuildTable() {
        unitTestSqlDao.sqlCommand("delete from gl_sf_rebuild_t");
    }

    /**
     * Check that the SFRB table is empty.
     */
    protected void assertSFRBEmpty() {
        List l = unitTestSqlDao.sqlSelect("select * from GL_SF_REBUILD_T");
        assertEquals("GL_SF_REBUILD_T should be empty", 0, l.size());
    }

    /**
     * Check all the entries in gl_sf_balances_t against the data passed in. If any of them are different, assert an error.
     * 
     * @param requiredSFBLs
     */
    protected void assertSFBLEntries(String[] requiredSFBLs) {

        Collection c = sufficientFundBalancesDao.testingGetAllEntries();
        
        assertEquals("Wrong number of SFBL", requiredSFBLs.length, c.size());

        if (requiredSFBLs.length == c.size()) {
            int count = 0;
            for (Iterator iter = c.iterator(); iter.hasNext();) {
                SufficientFundBalances foundSFBL = (SufficientFundBalances) iter.next();
                if (!requiredSFBLs[count].equals(foundSFBL.getLine())) {
                    System.err.println("Found:     " + foundSFBL.getLine());
                    System.err.println("Should be: " + requiredSFBLs[count]);
                    fail("SF balance doesn't match");
                }
                ++count;
            }
        }
    }

    private void updateAccount() {
        unitTestSqlDao.sqlCommand("update ca_account_t set acct_sf_cd = 'H' where account_nbr = '2220090'");
        unitTestSqlDao.sqlCommand("update ca_account_t set acct_sf_cd = 'L' where account_nbr = '2231406'");
        unitTestSqlDao.sqlCommand("update ca_account_t set acct_sf_cd = 'C' where account_nbr = '2231407'");
        unitTestSqlDao.sqlCommand("update ca_account_t set acct_sf_cd = 'O' where account_nbr = '2231408'");
        unitTestSqlDao.sqlCommand("update ca_account_t set acct_sf_cd = 'A' where account_nbr = '2231415'");
    }

    private void populateGLSFRebuildTable() {
        unitTestSqlDao.sqlCommand("delete from GL_SF_REBUILD_T");
        unitTestSqlDao.sqlCommand("insert into GL_SF_REBUILD_T (fin_coa_cd,acct_fobj_typ_cd,acct_nbr_fobj_cd,obj_id,ver_nbr) values ('BA','A','6044913',sys_guid(),1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_REBUILD_T (fin_coa_cd,acct_fobj_typ_cd,acct_nbr_fobj_cd,obj_id,ver_nbr) values ('BL','A','1031420',sys_guid(),1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_REBUILD_T (fin_coa_cd,acct_fobj_typ_cd,acct_nbr_fobj_cd,obj_id,ver_nbr) values ('BL','A','2220090',sys_guid(),1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_REBUILD_T (fin_coa_cd,acct_fobj_typ_cd,acct_nbr_fobj_cd,obj_id,ver_nbr) values ('BL','A','2231406',sys_guid(),1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_REBUILD_T (fin_coa_cd,acct_fobj_typ_cd,acct_nbr_fobj_cd,obj_id,ver_nbr) values ('BL','A','2231407',sys_guid(),1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_REBUILD_T (fin_coa_cd,acct_fobj_typ_cd,acct_nbr_fobj_cd,obj_id,ver_nbr) values ('BL','A','2231408',sys_guid(),1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_REBUILD_T (fin_coa_cd,acct_fobj_typ_cd,acct_nbr_fobj_cd,obj_id,ver_nbr) values ('BL','A','2231415',sys_guid(),1)");
    }

    private void populateGLSFRebuildTableForConversion() {
        unitTestSqlDao.sqlCommand("delete from GL_SF_REBUILD_T");
        unitTestSqlDao.sqlCommand("insert into GL_SF_REBUILD_T (fin_coa_cd,acct_fobj_typ_cd,acct_nbr_fobj_cd,obj_id,ver_nbr) values ('BL','O','PRIN',sys_guid(),1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_REBUILD_T (fin_coa_cd,acct_fobj_typ_cd,acct_nbr_fobj_cd,obj_id,ver_nbr) values ('BL','O','4938',sys_guid(),1)");
    }

    private void populateGLSFBalanceTableForConversion() {
        unitTestSqlDao.sqlCommand("delete from GL_SF_BALANCES_T");
        unitTestSqlDao.sqlCommand("insert into GL_SF_BALANCES_T (UNIV_FISCAL_YR, FIN_COA_CD, ACCOUNT_NBR, FIN_OBJECT_CD, OBJ_ID, VER_NBR, ACCT_SF_CD, CURR_BDGT_BAL_AMT, ACCT_ACTL_XPND_AMT, ACCT_ENCUM_AMT) values ('2007','BL','2220090','    ',sys_guid(),1,'H',1,1,1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_BALANCES_T (UNIV_FISCAL_YR, FIN_COA_CD, ACCOUNT_NBR, FIN_OBJECT_CD, OBJ_ID, VER_NBR, ACCT_SF_CD, CURR_BDGT_BAL_AMT, ACCT_ACTL_XPND_AMT, ACCT_ENCUM_AMT) values ('2007','BL','2231406','PRIN',sys_guid(),1,'L',1,1,1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_BALANCES_T (UNIV_FISCAL_YR, FIN_COA_CD, ACCOUNT_NBR, FIN_OBJECT_CD, OBJ_ID, VER_NBR, ACCT_SF_CD, CURR_BDGT_BAL_AMT, ACCT_ACTL_XPND_AMT, ACCT_ENCUM_AMT) values ('2007','BL','2231406','S&E ',sys_guid(),1,'H',1,1,1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_BALANCES_T (UNIV_FISCAL_YR, FIN_COA_CD, ACCOUNT_NBR, FIN_OBJECT_CD, OBJ_ID, VER_NBR, ACCT_SF_CD, CURR_BDGT_BAL_AMT, ACCT_ACTL_XPND_AMT, ACCT_ENCUM_AMT) values ('2007','BL','2231406','TRAV',sys_guid(),1,'H',1,1,1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_BALANCES_T (UNIV_FISCAL_YR, FIN_COA_CD, ACCOUNT_NBR, FIN_OBJECT_CD, OBJ_ID, VER_NBR, ACCT_SF_CD, CURR_BDGT_BAL_AMT, ACCT_ACTL_XPND_AMT, ACCT_ENCUM_AMT) values ('2007','BL','2231407','GENX',sys_guid(),1,'C',1,1,1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_BALANCES_T (UNIV_FISCAL_YR, FIN_COA_CD, ACCOUNT_NBR, FIN_OBJECT_CD, OBJ_ID, VER_NBR, ACCT_SF_CD, CURR_BDGT_BAL_AMT, ACCT_ACTL_XPND_AMT, ACCT_ENCUM_AMT) values ('2007','BL','2231408','4938',sys_guid(),1,'H',1,1,1)");
        unitTestSqlDao.sqlCommand("insert into GL_SF_BALANCES_T (UNIV_FISCAL_YR, FIN_COA_CD, ACCOUNT_NBR, FIN_OBJECT_CD, OBJ_ID, VER_NBR, ACCT_SF_CD, CURR_BDGT_BAL_AMT, ACCT_ACTL_XPND_AMT, ACCT_ENCUM_AMT) values ('2007','BL','2231408','5215',sys_guid(),1,'H',1,1,1)");
    }

    private void prepareGLBalancesTable() {
        unitTestSqlDao.sqlCommand("delete from gl_balance_t where account_nbr in ('6044913','1031420','2220090','2231406','2231407','2231408','2231415')");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','1031420','-----','8000','---','AC','AS',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','5050','---','AC','EX',sys_guid(),1,466,0,0,0,0,0,0,0,0,40,60,32,74,0,260,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','8000','---','AC','AS',sys_guid(),1,10756.57,0,0,0,0,0,0,0,0,-330.88,-1119.2,17391.01,-1068.19,-2828.47,-1287.7,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','1699','---','AC','TI',sys_guid(),1,20000,0,0,0,0,0,0,0,0,0,0,20000,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','4308','---','AC','EX',sys_guid(),1,1.48,0,0,0,0,0,0,0,0,1.48,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','4166','---','AC','EX',sys_guid(),1,215,0,0,0,0,0,0,0,0,0,0,0,0,0,215,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','5000','---','NB','EX',sys_guid(),1,-9243.43,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-9243.43)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','5055','---','AC','EX',sys_guid(),1,110,0,0,0,0,0,0,0,0,0,0,110,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','9892','---','EX','FB',sys_guid(),1,503.5,0,0,0,0,0,0,0,0,657.65,956.75,-1110.9,-453.25,1031.5,-578.25,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','6100','---','EX','EX',sys_guid(),1,503.5,0,0,0,0,0,0,0,0,657.65,956.75,-1110.9,-453.25,1031.5,-578.25,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','9899','---','NB','FB',sys_guid(),1,10756.57,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10756.57)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','1800','---','NB','TI',sys_guid(),1,-20000,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-20000)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','6200','---','AC','EX',sys_guid(),1,1445.86,0,0,0,0,0,0,0,0,0,0,0,0,1445.86,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','5000','---','AC','EX',sys_guid(),1,302.95,0,0,0,0,0,0,0,0,50,215,0,0,0,37.95,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','6140','---','AC','EX',sys_guid(),1,25.5,0,0,0,0,0,0,0,0,0,25.5,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2220090','-----','6100','---','AC','EX',sys_guid(),1,6676.64,0,0,0,0,0,0,0,0,239.4,818.7,2466.99,994.19,1382.61,774.75,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','0110','---','BB','IN',sys_guid(),1,-12000,12000,0,0,0,0,0,0,0,-12000,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','9899','---','NB','FB',sys_guid(),1,-2748.8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2748.8)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','5000','---','NB','EX',sys_guid(),1,-2748.8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2748.8)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','5027','---','AC','EX',sys_guid(),1,9.55,0,0,0,0,0,0,0,0,0,0,0,0,0,9.55,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','4110','---','AC','EX',sys_guid(),1,92.55,0,0,0,0,0,0,0,0,0,0,0,0,0,92.55,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','5000','---','CB','EX',sys_guid(),1,0,12000,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','0110','---','CB','IN',sys_guid(),1,0,12000,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','9899','---','AC','FB',sys_guid(),1,0,12007.47,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','9892','---','EX','FB',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','4055','---','AC','EX',sys_guid(),1,87.8,0,0,62.31,0,0,0,25.49,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','8000','---','AC','AS',sys_guid(),1,-2748.8,12007.47,0,-62.31,0,0,-2558.9,-25.49,0,0,0,0,0,0,-102.1,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','6200','---','EX','EX',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','6200','---','AC','EX',sys_guid(),1,2558.9,0,0,0,0,0,2558.9,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231406','-----','5000','---','BB','EX',sys_guid(),1,-12000,12000,0,0,0,0,0,0,0,-12000,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','1800','---','AC','IN',sys_guid(),1,49.7,0,0,0,0,0,0,0,0,0,0,0,49.7,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','9899','---','NB','FB',sys_guid(),1,1033.82,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1033.82)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','5000','---','NB','EX',sys_guid(),1,984.12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,984.12)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','1800','---','NB','IN',sys_guid(),1,-49.7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-49.7)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','9041','---','AC','LI',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','5200','---','AC','EX',sys_guid(),1,1305,0,0,0,0,0,0,0,0,0,0,0,0,0,1305,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','9892','---','EX','FB',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','5200','---','EX','EX',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','9899','---','AC','FB',sys_guid(),1,0,597.82,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','4938','---','AC','EX',sys_guid(),1,18.04,0,0,0,8.99,9.05,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','5000','---','AC','EX',sys_guid(),1,12.34,0,0,0,0,0,12.34,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','8000','---','AC','AS',sys_guid(),1,1033.82,597.82,0,0,-348.69,-9.05,-12.34,0,2894.2,0,-235,0,49.7,0,-1305,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','4061','---','AC','EX',sys_guid(),1,-2554.5,0,0,0,339.7,0,0,0,-2894.2,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231407','-----','4616','---','AC','EX',sys_guid(),1,235,0,0,0,0,0,0,0,0,0,235,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','1800','---','AC','IN',sys_guid(),1,50,0,0,0,0,0,0,0,0,0,50,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','8000','---','AC','AS',sys_guid(),1,-398.27,1918.46,0,0,-291.66,-56.61,0,0,0,0,50,0,0,0,-100,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','9899','---','NB','FB',sys_guid(),1,-398.27,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-398.27)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','5000','---','NB','EX',sys_guid(),1,-448.27,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-448.27)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','1800','---','NB','IN',sys_guid(),1,-50,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-50)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','9041','---','AC','LI',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,100,-100,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','5215','---','AC','EX',sys_guid(),1,100,0,0,0,0,0,0,0,0,0,0,0,0,100,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','9892','---','EX','FB',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','5215','---','EX','EX',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','4938','---','AC','EX',sys_guid(),1,348.27,0,0,0,291.66,56.61,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231408','-----','9899','---','AC','FB',sys_guid(),1,0,1918.46,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','0110','---','BB','IN',sys_guid(),1,-12000,12000,0,0,0,0,0,0,0,-12000,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','9899','---','NB','FB',sys_guid(),1,-2748.8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2748.8)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','5000','---','NB','EX',sys_guid(),1,-2748.8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2748.8)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','5027','---','AC','EX',sys_guid(),1,9.55,0,0,0,0,0,0,0,0,0,0,0,0,0,9.55,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','4110','---','AC','EX',sys_guid(),1,92.55,0,0,0,0,0,0,0,0,0,0,0,0,0,92.55,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','5000','---','CB','EX',sys_guid(),1,0,12000,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','0110','---','CB','IN',sys_guid(),1,0,12000,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','9899','---','AC','FB',sys_guid(),1,0,12007.47,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','9892','---','EX','FB',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','4055','---','AC','EX',sys_guid(),1,87.8,0,0,62.31,0,0,0,25.49,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','8000','---','AC','AS',sys_guid(),1,-2748.8,12007.47,0,-62.31,0,0,-2558.9,-25.49,0,0,0,0,0,0,-102.1,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','6200','---','EX','EX',sys_guid(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','6200','---','AC','EX',sys_guid(),1,2558.9,0,0,0,0,0,2558.9,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BL','2231415','-----','5000','---','BB','EX',sys_guid(),1,-12000,12000,0,0,0,0,0,0,0,-12000,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','1476','---','AC','IC',sys_guid(),1,18675,0,0,2700,900,1425,0,1050,0,0,0,0,0,0,12600,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','9899','---','AC','FB',sys_guid(),1,0,49525.04,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','8000','---','AC','AS',sys_guid(),1,2700,49525.04,0,0,0,0,0,0,0,0,0,0,0,0,2700,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','1800','---','NB','IC',sys_guid(),1,-18675,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-18675)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','9899','---','NB','FB',sys_guid(),1,21375,0,0,0,0,0,0,0,0,0,0,0,0,0,0,21375)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','1800','---','NB','IN',sys_guid(),1,-2700,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-2700)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','1476','---','AC','IN',sys_guid(),1,2700,0,0,0,0,0,0,0,0,0,0,0,0,0,2700,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','4768','---','IE','EX',sys_guid(),1,0,0.03,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','9891','---','IE','FB',sys_guid(),1,0,0.03,0,0,0,0,0,0,0,0,0,0,0,0,0,0)");
        unitTestSqlDao.sqlCommand("insert into gl_balance_t (UNIV_FISCAL_YR,FIN_COA_CD,ACCOUNT_NBR,SUB_ACCT_NBR,FIN_OBJECT_CD,FIN_SUB_OBJ_CD,FIN_BALANCE_TYP_CD,FIN_OBJ_TYP_CD,OBJ_ID,VER_NBR,ACLN_ANNL_BAL_AMT,FIN_BEG_BAL_LN_AMT,CONTR_GR_BB_AC_AMT,MO1_ACCT_LN_AMT,MO2_ACCT_LN_AMT,MO3_ACCT_LN_AMT,MO4_ACCT_LN_AMT,MO5_ACCT_LN_AMT,MO6_ACCT_LN_AMT,MO7_ACCT_LN_AMT,MO8_ACCT_LN_AMT,MO9_ACCT_LN_AMT,MO10_ACCT_LN_AMT,MO11_ACCT_LN_AMT,MO12_ACCT_LN_AMT,MO13_ACCT_LN_AMT) values ('2007','BA','6044913','-----','9897','---','AC','FB',sys_guid(),1,-18675,0,0,-2700,-900,-1425,0,-1050,0,0,0,0,0,0,-12600,0)");
    }
}
