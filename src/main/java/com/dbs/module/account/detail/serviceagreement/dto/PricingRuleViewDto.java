package com.dbs.module.account.detail.serviceagreement.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class PricingRuleViewDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ccId;
	private String createdBy;
	private Date createdDate;
	private String description;
	private Date endDate;
	private String entityId;
	private String name;
	private Integer pricingRuleId;
	private Date startDate;
	private String status;
	private String updatedBy;
	private Date updatedDate;
	private String remark;
	private Integer apphierId;
	private Integer flag;
	private Boolean isApprover;
	private String approvalStatus;
	private Integer tAppId;
	private String approvalType;

	private List<PricingRuleDetailViewDto> mPricingRuleDetails;

	private List<PricingRuleCriteriaViewDto> rPricingRuleCriterias;

	private List<PricingRuleCriteriaDataViewDto> rPricingRuleCriteriaDatas;
	
	private List<AttachmentListDto> mAttachmentLists;
	
	private ApprovalHeaderViewDto approvalDetail;
	

	@Override
	public String toString() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
			return getClass().getName();
		}
	}
}
