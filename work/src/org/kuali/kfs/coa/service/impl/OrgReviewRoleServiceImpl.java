/*
 * Copyright 2010 The Kuali Foundation.
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
package org.kuali.kfs.coa.service.impl;

import java.util.Collections;
import java.util.List;

import org.kuali.kfs.coa.identity.KfsKimDocumentAttributeData;
import org.kuali.kfs.coa.identity.OrgReviewRole;
import org.kuali.kfs.coa.service.OrgReviewRoleService;
import org.kuali.kfs.sys.identity.KfsKimAttributes; import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.bo.role.dto.DelegateMemberCompleteInfo;
import org.kuali.rice.kim.api.common.delegate.DelegateTypeContract;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.bo.role.dto.RoleMemberCompleteInfo;
import org.kuali.rice.kim.bo.role.dto.RoleResponsibilityActionInfo;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.KimApiConstants; import org.kuali.rice.kim.api.KimConstants;

public class OrgReviewRoleServiceImpl implements OrgReviewRoleService {

    protected RoleService roleManagementService;
    protected KimTypeInfoService kimTypeInfoService;
    
    public void populateOrgReviewRoleFromRoleMember(OrgReviewRole orr, String roleMemberId) {
        List<RoleMemberCompleteInfo> roleMembers = (List<RoleMemberCompleteInfo>) roleManagementService.findRoleMembersCompleteInfo(Collections.singletonMap(KimConstants.PrimaryKeyConstants.ROLE_MEMBER_ID, roleMemberId));
        RoleMemberCompleteInfo roleMember = new RoleMemberCompleteInfo();
        if(roleMembers!=null && roleMembers.size()>0){
            roleMember = roleMembers.get(0);
        }
        orr.setRoleMemberId(roleMember.getRoleMemberId());
        orr.setKimDocumentRoleMember(roleMember);

        Role roleInfo = roleManagementService.getRole(roleMember.getRoleId());
        KimType typeInfo = kimTypeInfoService.getKimType(roleInfo.getKimTypeId());
        List<KfsKimDocumentAttributeData> attributes = orr.getAttributeSetAsQualifierList(typeInfo, roleMember.getQualifier());
        orr.setAttributes(attributes);
        orr.setRoleRspActions(getRoleRspActions(roleMember.getRoleMemberId()));
        orr.setRoleId(roleMember.getRoleId());
        orr.setActiveFromDate(roleMember.getActiveFromDate());
        orr.setActiveToDate(roleMember.getActiveToDate());
        populateObjectExtras(orr);
    }

    public void populateOrgReviewRoleFromDelegationMember(OrgReviewRole orr, String delegationMemberId) {
        DelegateMemberCompleteInfo delegationMember = roleManagementService.getDelegationMemberById(delegationMemberId);
        DelegateTypeContract delegation = roleManagementService.getDelegateTypeContractById(delegationMember.getDelegationId());
        Role roleInfo = roleManagementService.getRole(delegation.getRoleId());
        KimType typeInfo = kimTypeInfoService.getKimType(roleInfo.getKimTypeId());
        orr.setDelegationMemberId(delegationMember.getDelegationMemberId());
        orr.setRoleMemberId(delegationMember.getRoleMemberId());
        orr.setRoleRspActions(getRoleRspActions(delegationMember.getRoleMemberId()));
        orr.setAttributes(orr.getAttributeSetAsQualifierList(typeInfo, delegationMember.getQualifier()));
        orr.setRoleId(delegation.getRoleId());
        orr.setDelegationTypeCode(delegationMember.getDelegationTypeCode());
        orr.setRoleDocumentDelegationMember(delegationMember);
        populateObjectExtras(orr);
    }

    protected void populateObjectExtras( OrgReviewRole orr ) {
        Role role = orr.getRole(orr.getRoleId());
        //Set the role details
        orr.setRoleName(role.getName());
        orr.setNamespaceCode(role.getNamespaceCode());
        
        orr.setChartOfAccountsCode(orr.getAttributeValue(KfsKimAttributes.CHART_OF_ACCOUNTS_CODE));
        orr.setOrganizationCode(orr.getAttributeValue(KfsKimAttributes.ORGANIZATION_CODE));
        orr.setOverrideCode(orr.getAttributeValue(KfsKimAttributes.ACCOUNTING_LINE_OVERRIDE_CODE));
        orr.setFromAmount(orr.getAttributeValue(KfsKimAttributes.FROM_AMOUNT));
        orr.setToAmount(orr.getAttributeValue(KfsKimAttributes.TO_AMOUNT));
        orr.setFinancialSystemDocumentTypeCode(orr.getAttributeValue(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME));
        
        orr.getChart().setChartOfAccountsCode(orr.getChartOfAccountsCode());
        orr.getOrganization().setOrganizationCode(orr.getOrganizationCode());

        if(orr.getRoleRspActions()!=null && orr.getRoleRspActions().size()>0){
            orr.setActionTypeCode(orr.getRoleRspActions().get(0).getActionTypeCode());
            orr.setPriorityNumber(orr.getRoleRspActions().get(0).getPriorityNumber()==null?"":orr.getRoleRspActions().get(0).getPriorityNumber()+"");
            orr.setActionPolicyCode(orr.getRoleRspActions().get(0).getActionPolicyCode());
            orr.setForceAction(orr.getRoleRspActions().get(0).isForceAction());
        }
        if(orr.getPerson()!=null){
            orr.setPrincipalMemberPrincipalId(orr.getPerson().getPrincipalId());
            orr.setPrincipalMemberPrincipalName(orr.getPerson().getPrincipalName());
        }
        if(orr.getRole()!=null){
            orr.setRoleMemberRoleId(orr.getRole().getRoleId());
            orr.setRoleMemberRoleNamespaceCode(orr.getRole().getNamespaceCode());
            orr.setRoleMemberRoleName(orr.getRole().getRoleName());
        }
        if(orr.getGroup()!=null){
            orr.setGroupMemberGroupId(orr.getGroup().getGroupId());
            orr.setGroupMemberGroupNamespaceCode(orr.getGroup().getNamespaceCode());
            orr.setGroupMemberGroupName(orr.getGroup().getGroupName());
        }
    }
    
    protected List<RoleResponsibilityActionInfo> getRoleRspActions(String roleMemberId){
        return roleManagementService.getRoleMemberResponsibilityActionInfo(roleMemberId);
    }

    public void setRoleService(RoleService roleManagementService) {
        this.roleManagementService = roleManagementService;
    }

    public void setKimTypeInfoService(KimTypeInfoService kimTypeInfoService) {
        this.kimTypeInfoService = kimTypeInfoService;
    }
    
}
