<%--
 Copyright 2006-2007 The Kuali Foundation.
 
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
<%@ include file="/jsp/kfs/kfsTldHeader.jsp"%>

<c:set var="documentAttributes" value="${DataDictionary.CustomerInvoiceDocument.attributes}" />
<%@ attribute name="subResultRows" required="true" type="java.util.List" description="The rows of fields that we'll iterate to display." %>

<tr>
	<td colspan="5">
		<br />
			<center>
			<table class="datatable-80" cellspacing="0" cellpadding="0" width="100%">
				<thead>
					<tr>
						<th>
							Write Off
						</th>
			    		<c:forEach items="${subResultRows[0].columns}" var="column" begin="0" end="${fn:length(subResultRows[0].columns)}" varStatus="columnLoopStatus">
							<th class="sortable">
								${column.columnTitle}
							</th>
						</c:forEach>
					</tr>
				</thead>

				<c:forEach items="${subResultRows}" var="row" varStatus="rowLoopStatus" begin="0" end="${fn:length(subResultRows)}">
					<tr>
					    <td>
							<c:set var="checked" value="${empty KualiForm.compositeObjectIdMap[row.objectId] ? '' : 'checked=checked'}" />							
							<input type="checkbox" title="Select" name="${row.objectId}" value="checked" ${checked} />
						</td>
						<c:forEach items="${row.columns}" var="column" begin="0" end="${fn:length(row.columns)}">
							<td class="infocell" title="${column.propertyValue}">
								<c:if test="${!empty column.propertyURL}">
									<a href="<c:out value="${column.propertyURL}"/>" target="blank">
								</c:if>
								
								<c:out value="${fn:substring(column.propertyValue, 0, column.maxLength)}"/>
								<c:if test="${column.maxLength gt 0 && fn:length(column.propertyValue) gt column.maxLength}">...</c:if>
								
								<c:if test="${!empty column.propertyURL}"></a></c:if>
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
			</center>
		<br />
	</td>
</tr>
