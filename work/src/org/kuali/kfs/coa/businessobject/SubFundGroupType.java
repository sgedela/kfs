/*
 * Copyright (c) 2004, 2005 The National Association of College and University 
 * Business Officers, Cornell University, Trustees of Indiana University, 
 * Michigan State University Board of Trustees, Trustees of San Joaquin Delta 
 * College, University of Hawai'i, The Arizona Board of Regents on behalf of the 
 * University of Arizona, and the r*smart group.
 * 
 * Licensed under the Educational Community License Version 1.0 (the "License"); 
 * By obtaining, using and/or copying this Original Work, you agree that you 
 * have read, understand, and will comply with the terms and conditions of the 
 * Educational Community License.
 * 
 * You may obtain a copy of the License at:
 * 
 * http://kualiproject.org/license.html
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,  DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
 * THE SOFTWARE.
 */

package org.kuali.module.chart.bo;

import java.util.LinkedHashMap;

import org.kuali.core.bo.BusinessObjectBase;

/**
 * @author Kuali Nervous System Team ()
 */
public class SubFundGroupType extends BusinessObjectBase {

    private String subFundGroupTypeCode;
    private String subFundGroupTypeDescription;
    private boolean subFundGroupTypeActiveIndicator;

    /**
     * Default constructor.
     */
    public SubFundGroupType() {

    }

    /**
     * Gets the subFundGroupTypeCode attribute.
     * 
     * @return - Returns the subFundGroupTypeCode
     * 
     */
    public String getSubFundGroupTypeCode() {
        return subFundGroupTypeCode;
    }

    /**
     * Sets the subFundGroupTypeCode attribute.
     * 
     * @param - subFundGroupTypeCode The subFundGroupTypeCode to set.
     * 
     */
    public void setSubFundGroupTypeCode(String subFundGroupTypeCode) {
        this.subFundGroupTypeCode = subFundGroupTypeCode;
    }


    /**
     * Gets the subFundGroupTypeDescription attribute.
     * 
     * @return - Returns the subFundGroupTypeDescription
     * 
     */
    public String getSubFundGroupTypeDescription() {
        return subFundGroupTypeDescription;
    }

    /**
     * Sets the subFundGroupTypeDescription attribute.
     * 
     * @param - subFundGroupTypeDescription The subFundGroupTypeDescription to set.
     * 
     */
    public void setSubFundGroupTypeDescription(String subFundGroupTypeDescription) {
        this.subFundGroupTypeDescription = subFundGroupTypeDescription;
    }


    /**
     * Gets the subFundGroupTypeActiveIndicator attribute.
     * 
     * @return - Returns the subFundGroupTypeActiveIndicator
     * 
     */
    public boolean getSubFundGroupTypeActiveIndicator() {
        return subFundGroupTypeActiveIndicator;
    }

    /**
     * Sets the subFundGroupTypeActiveIndicator attribute.
     * 
     * @param - subFundGroupTypeActiveIndicator The subFundGroupTypeActiveIndicator to set.
     * 
     */
    public void setSubFundGroupTypeActiveIndicator(boolean subFundGroupTypeActiveIndicator) {
        this.subFundGroupTypeActiveIndicator = subFundGroupTypeActiveIndicator;
    }


    /**
     * @see org.kuali.bo.BusinessObjectBase#toStringMapper()
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap m = new LinkedHashMap();
        m.put("subFundGroupTypeCode", this.subFundGroupTypeCode);
        return m;
    }
}
