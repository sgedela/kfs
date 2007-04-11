<%--
 Copyright 2007 The Kuali Foundation.
 
 Licensed under the Educational Community License, Version 1.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl1.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/jsp/core/tldHeader.jsp"%>
<c:set var="budgetConstructionMonthlyAttributes"
	value="${DataDictionary['BudgetConstructionMonthly'].attributes}" />

<kul:page showDocumentInfo="false"
	htmlFormAction="budgetMonthlyBudget" renderMultipart="true"
	showTabButtons="true"
	docTitle="BC Monthly"
    transactionalDocument="false"
	>

<%--	<kul:hiddenDocumentFields /> --%>
    <bc:monthlyBudget />
	<kul:panelFooter />

<%--TODO need to create save and close(and prompt to save) actions that calls returnToCaller --%>
    <div id="globalbuttons" class="globalbuttons">
        <c:if test="${!KualiForm.editingMode['systemViewOnly'] && KualiForm.editingMode['fullEntry']}">
	        <html:image src="images/buttonsmall_save.gif" styleClass="globalbuttons" property="methodToCall.returnToCaller" title="save" alt="save"/>
	    </c:if>
        <html:image src="images/buttonsmall_close.gif" styleClass="globalbuttons" property="methodToCall.returnToCaller" title="close" alt="close"/>
    </div>

</kul:page>
