/*
******************************           DO NOT EDIT THIS FILE!!!           				*********************************
******************************         Please edit CustomRelation.java instead!        		*********************************	
******************************         不要编辑这个文件，这个文件每次都会被机器人覆盖!!!              *********************************
******************************    CustomRelation.java专门用于定制，该文件存在的时候不会被覆盖      	*********************************


*/
package com.doublechaintech.retailscm;
import java.util.HashMap;
import java.util.Map;

public class BaseRelation{

	
	Map<String, String> relationMapping ;
	
	public String getRelation(String fromType, String fromId, String targetField, String targetId)
	{
		//the entry for external calls, ugly code with many path just works, using a map is fine but lose the way to override the methods
		
		if(relationMapping == null){
			prepareRelation();
		}

		String key = fromType+"->"+targetField;
		
		String relation = relationMapping.get(key);
		if(relation == null){
			throw new IllegalArgumentException("Not able to find any relation to the target type: "+ targetField);
		}
		return relation;
		
	}
	
	protected void addGenericRelation(String fromType, String relation,String targetField)
	{
		if(relationMapping == null){
			relationMapping = new HashMap<String,String>();
		}
		String key = fromType.trim()+"->"+targetField.trim();
		relationMapping.put(key, relation);
	}
	protected void replaceGenericRelation(String fromType, String relation,String targetField)
	{
		addGenericRelation( fromType, relation, targetField );
	}
	
	
	
	Map<String, String[]> relationIndex ;
	protected void addRelationIndex(String fromType,String related[])
	{
		if(relationIndex == null){
			relationIndex = new HashMap<String,String[]>();
		}
		
		relationIndex.put(fromType, related);
	}
	protected void replaceRelationIndex(String fromType,String related[])
	{
		addRelationIndex( fromType, related);
	}
	
	public String getTableFieldName(String expr){
		//the expr looks like owner:DecorationAccelerator
		String[] splitedValues = expr.split(":");
		if(splitedValues.length < 1){
			throw new IllegalArgumentException("Not able to split expr: "+expr);
		}
		
		return splitedValues[0];
	}
	public String getBeanFieldName(String expr){
		//the expr looks like owner:DecorationAccelerator
		String[] splitedValues = getTableFieldName(expr).split("_");
		String ret = splitedValues[0];
		for(int i=1;i<splitedValues.length;i++){
			
			ret = ret+splitedValues[i].substring(0,1).toUpperCase()+splitedValues[i].substring(1);
			
		}
		
		return ret;
	}
	public String getFieldType(String expr){
		//the expr looks like owner:DecorationAccelerator
		String[] splitedValues = expr.split(":");
		if(splitedValues.length < 2){
			throw new IllegalArgumentException("Not able to split expr: "+expr);
		}
		
		return splitedValues[1];
	}
	public String [] getRelationIndex(String type){
		
		if(relationIndex == null){
			prepareRelationIndex();
		}
		
		String relations [] =relationIndex.get(type);
		if(relations == null){
			//throw new IllegalArgumentException("Not able to find related objects for type: "+ type);
		}
		return relations;
	}
	
	protected void prepareRelationIndex()
	{
		
		
		String [] catalogRelatedObjectNames = {"owner:RetailStoreCountryCenter"};
		addRelationIndex("Catalog",catalogRelatedObjectNames);

		String [] levelOneCategoryRelatedObjectNames = {"catalog:Catalog"};
		addRelationIndex("LevelOneCategory",levelOneCategoryRelatedObjectNames);

		String [] levelTwoCategoryRelatedObjectNames = {"parent_category:LevelOneCategory"};
		addRelationIndex("LevelTwoCategory",levelTwoCategoryRelatedObjectNames);

		String [] levelThreeCategoryRelatedObjectNames = {"parent_category:LevelTwoCategory"};
		addRelationIndex("LevelThreeCategory",levelThreeCategoryRelatedObjectNames);

		String [] productRelatedObjectNames = {"parent_category:LevelThreeCategory"};
		addRelationIndex("Product",productRelatedObjectNames);

		String [] skuRelatedObjectNames = {"product:Product"};
		addRelationIndex("Sku",skuRelatedObjectNames);

		String [] retailStoreProvinceCenterRelatedObjectNames = {"country:RetailStoreCountryCenter"};
		addRelationIndex("RetailStoreProvinceCenter",retailStoreProvinceCenterRelatedObjectNames);

		String [] provinceCenterDepartmentRelatedObjectNames = {"province_center:RetailStoreProvinceCenter"};
		addRelationIndex("ProvinceCenterDepartment",provinceCenterDepartmentRelatedObjectNames);

		String [] provinceCenterEmployeeRelatedObjectNames = {"department:ProvinceCenterDepartment","province_center:RetailStoreProvinceCenter"};
		addRelationIndex("ProvinceCenterEmployee",provinceCenterEmployeeRelatedObjectNames);

		String [] retailStoreCityServiceCenterRelatedObjectNames = {"belongs_to:RetailStoreProvinceCenter"};
		addRelationIndex("RetailStoreCityServiceCenter",retailStoreCityServiceCenterRelatedObjectNames);

		String [] cityPartnerRelatedObjectNames = {"city_service_center:RetailStoreCityServiceCenter"};
		addRelationIndex("CityPartner",cityPartnerRelatedObjectNames);

		String [] potentialCustomerRelatedObjectNames = {"city_service_center:RetailStoreCityServiceCenter","city_partner:CityPartner"};
		addRelationIndex("PotentialCustomer",potentialCustomerRelatedObjectNames);

		String [] potentialCustomerContactPersonRelatedObjectNames = {"potential_customer:PotentialCustomer"};
		addRelationIndex("PotentialCustomerContactPerson",potentialCustomerContactPersonRelatedObjectNames);

		String [] potentialCustomerContactRelatedObjectNames = {"potential_customer:PotentialCustomer","city_partner:CityPartner","contact_to:PotentialCustomerContactPerson"};
		addRelationIndex("PotentialCustomerContact",potentialCustomerContactRelatedObjectNames);

		String [] cityEventRelatedObjectNames = {"city_service_center:RetailStoreCityServiceCenter"};
		addRelationIndex("CityEvent",cityEventRelatedObjectNames);

		String [] eventAttendanceRelatedObjectNames = {"potential_customer:PotentialCustomer","city_event:CityEvent"};
		addRelationIndex("EventAttendance",eventAttendanceRelatedObjectNames);

		String [] retailStoreRelatedObjectNames = {"retail_store_country_center:RetailStoreCountryCenter","city_service_center:RetailStoreCityServiceCenter","creation:RetailStoreCreation","investment_invitation:RetailStoreInvestmentInvitation","franchising:RetailStoreFranchising","decoration:RetailStoreDecoration","opening:RetailStoreOpening","closing:RetailStoreClosing"};
		addRelationIndex("RetailStore",retailStoreRelatedObjectNames);

		String [] retailStoreMemberRelatedObjectNames = {"owner:RetailStoreCountryCenter"};
		addRelationIndex("RetailStoreMember",retailStoreMemberRelatedObjectNames);

		String [] consumerOrderRelatedObjectNames = {"consumer:RetailStoreMember","store:RetailStore"};
		addRelationIndex("ConsumerOrder",consumerOrderRelatedObjectNames);

		String [] consumerOrderLineItemRelatedObjectNames = {"biz_order:ConsumerOrder"};
		addRelationIndex("ConsumerOrderLineItem",consumerOrderLineItemRelatedObjectNames);

		String [] consumerOrderShippingGroupRelatedObjectNames = {"biz_order:ConsumerOrder"};
		addRelationIndex("ConsumerOrderShippingGroup",consumerOrderShippingGroupRelatedObjectNames);

		String [] consumerOrderPaymentGroupRelatedObjectNames = {"biz_order:ConsumerOrder"};
		addRelationIndex("ConsumerOrderPaymentGroup",consumerOrderPaymentGroupRelatedObjectNames);

		String [] consumerOrderPriceAdjustmentRelatedObjectNames = {"biz_order:ConsumerOrder"};
		addRelationIndex("ConsumerOrderPriceAdjustment",consumerOrderPriceAdjustmentRelatedObjectNames);

		String [] retailStoreMemberCouponRelatedObjectNames = {"owner:RetailStoreMember"};
		addRelationIndex("RetailStoreMemberCoupon",retailStoreMemberCouponRelatedObjectNames);

		String [] memberWishlistRelatedObjectNames = {"owner:RetailStoreMember"};
		addRelationIndex("MemberWishlist",memberWishlistRelatedObjectNames);

		String [] memberRewardPointRelatedObjectNames = {"owner:RetailStoreMember"};
		addRelationIndex("MemberRewardPoint",memberRewardPointRelatedObjectNames);

		String [] memberRewardPointRedemptionRelatedObjectNames = {"owner:RetailStoreMember"};
		addRelationIndex("MemberRewardPointRedemption",memberRewardPointRedemptionRelatedObjectNames);

		String [] memberWishlistProductRelatedObjectNames = {"owner:MemberWishlist"};
		addRelationIndex("MemberWishlistProduct",memberWishlistProductRelatedObjectNames);

		String [] retailStoreMemberAddressRelatedObjectNames = {"owner:RetailStoreMember"};
		addRelationIndex("RetailStoreMemberAddress",retailStoreMemberAddressRelatedObjectNames);

		String [] retailStoreMemberGiftCardRelatedObjectNames = {"owner:RetailStoreMember"};
		addRelationIndex("RetailStoreMemberGiftCard",retailStoreMemberGiftCardRelatedObjectNames);

		String [] retailStoreMemberGiftCardConsumeRecordRelatedObjectNames = {"owner:RetailStoreMemberGiftCard","biz_order:ConsumerOrder"};
		addRelationIndex("RetailStoreMemberGiftCardConsumeRecord",retailStoreMemberGiftCardConsumeRecordRelatedObjectNames);

		String [] goodsSupplierRelatedObjectNames = {"belong_to:RetailStoreCountryCenter"};
		addRelationIndex("GoodsSupplier",goodsSupplierRelatedObjectNames);

		String [] supplierProductRelatedObjectNames = {"supplier:GoodsSupplier"};
		addRelationIndex("SupplierProduct",supplierProductRelatedObjectNames);

		String [] productSupplyDurationRelatedObjectNames = {"product:SupplierProduct"};
		addRelationIndex("ProductSupplyDuration",productSupplyDurationRelatedObjectNames);

		String [] supplyOrderRelatedObjectNames = {"buyer:RetailStoreCountryCenter","seller:GoodsSupplier"};
		addRelationIndex("SupplyOrder",supplyOrderRelatedObjectNames);

		String [] supplyOrderLineItemRelatedObjectNames = {"biz_order:SupplyOrder"};
		addRelationIndex("SupplyOrderLineItem",supplyOrderLineItemRelatedObjectNames);

		String [] supplyOrderShippingGroupRelatedObjectNames = {"biz_order:SupplyOrder"};
		addRelationIndex("SupplyOrderShippingGroup",supplyOrderShippingGroupRelatedObjectNames);

		String [] supplyOrderPaymentGroupRelatedObjectNames = {"biz_order:SupplyOrder"};
		addRelationIndex("SupplyOrderPaymentGroup",supplyOrderPaymentGroupRelatedObjectNames);

		String [] retailStoreOrderRelatedObjectNames = {"buyer:RetailStore","seller:RetailStoreCountryCenter"};
		addRelationIndex("RetailStoreOrder",retailStoreOrderRelatedObjectNames);

		String [] retailStoreOrderLineItemRelatedObjectNames = {"biz_order:RetailStoreOrder"};
		addRelationIndex("RetailStoreOrderLineItem",retailStoreOrderLineItemRelatedObjectNames);

		String [] retailStoreOrderShippingGroupRelatedObjectNames = {"biz_order:RetailStoreOrder"};
		addRelationIndex("RetailStoreOrderShippingGroup",retailStoreOrderShippingGroupRelatedObjectNames);

		String [] retailStoreOrderPaymentGroupRelatedObjectNames = {"biz_order:RetailStoreOrder"};
		addRelationIndex("RetailStoreOrderPaymentGroup",retailStoreOrderPaymentGroupRelatedObjectNames);

		String [] warehouseRelatedObjectNames = {"owner:RetailStoreCountryCenter"};
		addRelationIndex("Warehouse",warehouseRelatedObjectNames);

		String [] storageSpaceRelatedObjectNames = {"warehouse:Warehouse"};
		addRelationIndex("StorageSpace",storageSpaceRelatedObjectNames);

		String [] smartPalletRelatedObjectNames = {"warehouse:Warehouse"};
		addRelationIndex("SmartPallet",smartPalletRelatedObjectNames);

		String [] goodsShelfRelatedObjectNames = {"storage_space:StorageSpace","supplier_space:SupplierSpace","damage_space:DamageSpace"};
		addRelationIndex("GoodsShelf",goodsShelfRelatedObjectNames);

		String [] goodsShelfStockCountRelatedObjectNames = {"shelf:GoodsShelf"};
		addRelationIndex("GoodsShelfStockCount",goodsShelfStockCountRelatedObjectNames);

		String [] stockCountIssueTrackRelatedObjectNames = {"stock_count:GoodsShelfStockCount"};
		addRelationIndex("StockCountIssueTrack",stockCountIssueTrackRelatedObjectNames);

		String [] goodsAllocationRelatedObjectNames = {"goods_shelf:GoodsShelf"};
		addRelationIndex("GoodsAllocation",goodsAllocationRelatedObjectNames);

		String [] goodsRelatedObjectNames = {"sku:Sku","receiving_space:ReceivingSpace","goods_allocation:GoodsAllocation","smart_pallet:SmartPallet","shipping_space:ShippingSpace","transport_task:TransportTask","retail_store:RetailStore","biz_order:SupplyOrder","retail_store_order:RetailStoreOrder"};
		addRelationIndex("Goods",goodsRelatedObjectNames);

		String [] goodsMovementRelatedObjectNames = {"goods:Goods"};
		addRelationIndex("GoodsMovement",goodsMovementRelatedObjectNames);

		String [] supplierSpaceRelatedObjectNames = {"warehouse:Warehouse"};
		addRelationIndex("SupplierSpace",supplierSpaceRelatedObjectNames);

		String [] receivingSpaceRelatedObjectNames = {"warehouse:Warehouse"};
		addRelationIndex("ReceivingSpace",receivingSpaceRelatedObjectNames);

		String [] shippingSpaceRelatedObjectNames = {"warehouse:Warehouse"};
		addRelationIndex("ShippingSpace",shippingSpaceRelatedObjectNames);

		String [] damageSpaceRelatedObjectNames = {"warehouse:Warehouse"};
		addRelationIndex("DamageSpace",damageSpaceRelatedObjectNames);

		String [] warehouseAssetRelatedObjectNames = {"owner:Warehouse"};
		addRelationIndex("WarehouseAsset",warehouseAssetRelatedObjectNames);

		String [] transportFleetRelatedObjectNames = {"owner:RetailStoreCountryCenter"};
		addRelationIndex("TransportFleet",transportFleetRelatedObjectNames);

		String [] transportTruckRelatedObjectNames = {"owner:TransportFleet"};
		addRelationIndex("TransportTruck",transportTruckRelatedObjectNames);

		String [] truckDriverRelatedObjectNames = {"belongs_to:TransportFleet"};
		addRelationIndex("TruckDriver",truckDriverRelatedObjectNames);

		String [] transportTaskRelatedObjectNames = {"end:RetailStore","driver:TruckDriver","truck:TransportTruck","belongs_to:TransportFleet"};
		addRelationIndex("TransportTask",transportTaskRelatedObjectNames);

		String [] transportTaskTrackRelatedObjectNames = {"movement:TransportTask"};
		addRelationIndex("TransportTaskTrack",transportTaskTrackRelatedObjectNames);

		String [] accountSetRelatedObjectNames = {"country_center:RetailStoreCountryCenter","retail_store:RetailStore","goods_supplier:GoodsSupplier"};
		addRelationIndex("AccountSet",accountSetRelatedObjectNames);

		String [] accountingSubjectRelatedObjectNames = {"account_set:AccountSet"};
		addRelationIndex("AccountingSubject",accountingSubjectRelatedObjectNames);

		String [] accountingPeriodRelatedObjectNames = {"account_set:AccountSet"};
		addRelationIndex("AccountingPeriod",accountingPeriodRelatedObjectNames);

		String [] accountingDocumentTypeRelatedObjectNames = {"accounting_period:AccountSet"};
		addRelationIndex("AccountingDocumentType",accountingDocumentTypeRelatedObjectNames);

		String [] accountingDocumentRelatedObjectNames = {"accounting_period:AccountingPeriod","document_type:AccountingDocumentType"};
		addRelationIndex("AccountingDocument",accountingDocumentRelatedObjectNames);

		String [] originalVoucherRelatedObjectNames = {"belongs_to:AccountingDocument"};
		addRelationIndex("OriginalVoucher",originalVoucherRelatedObjectNames);

		String [] accountingDocumentLineRelatedObjectNames = {"belongs_to:AccountingDocument","accounting_subject:AccountingSubject"};
		addRelationIndex("AccountingDocumentLine",accountingDocumentLineRelatedObjectNames);

		String [] levelOneDepartmentRelatedObjectNames = {"belongs_to:RetailStoreCountryCenter"};
		addRelationIndex("LevelOneDepartment",levelOneDepartmentRelatedObjectNames);

		String [] levelTwoDepartmentRelatedObjectNames = {"belongs_to:LevelOneDepartment"};
		addRelationIndex("LevelTwoDepartment",levelTwoDepartmentRelatedObjectNames);

		String [] levelThreeDepartmentRelatedObjectNames = {"belongs_to:LevelTwoDepartment"};
		addRelationIndex("LevelThreeDepartment",levelThreeDepartmentRelatedObjectNames);

		String [] skillTypeRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("SkillType",skillTypeRelatedObjectNames);

		String [] responsibilityTypeRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("ResponsibilityType",responsibilityTypeRelatedObjectNames);

		String [] terminationReasonRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("TerminationReason",terminationReasonRelatedObjectNames);

		String [] terminationTypeRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("TerminationType",terminationTypeRelatedObjectNames);

		String [] occupationTypeRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("OccupationType",occupationTypeRelatedObjectNames);

		String [] leaveTypeRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("LeaveType",leaveTypeRelatedObjectNames);

		String [] salaryGradeRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("SalaryGrade",salaryGradeRelatedObjectNames);

		String [] interviewTypeRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("InterviewType",interviewTypeRelatedObjectNames);

		String [] trainingCourseTypeRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("TrainingCourseType",trainingCourseTypeRelatedObjectNames);

		String [] publicHolidayRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("PublicHoliday",publicHolidayRelatedObjectNames);

		String [] terminationRelatedObjectNames = {"reason:TerminationReason","type:TerminationType"};
		addRelationIndex("Termination",terminationRelatedObjectNames);

		String [] employeeRelatedObjectNames = {"company:RetailStoreCountryCenter","department:LevelThreeDepartment","occupation:OccupationType","responsible_for:ResponsibilityType","current_salary_grade:SalaryGrade"};
		addRelationIndex("Employee",employeeRelatedObjectNames);

		String [] instructorRelatedObjectNames = {"company:RetailStoreCountryCenter"};
		addRelationIndex("Instructor",instructorRelatedObjectNames);

		String [] companyTrainingRelatedObjectNames = {"company:RetailStoreCountryCenter","instructor:Instructor","training_course_type:TrainingCourseType"};
		addRelationIndex("CompanyTraining",companyTrainingRelatedObjectNames);

		String [] employeeCompanyTrainingRelatedObjectNames = {"employee:Employee","training:CompanyTraining","scoring:Scoring"};
		addRelationIndex("EmployeeCompanyTraining",employeeCompanyTrainingRelatedObjectNames);

		String [] employeeSkillRelatedObjectNames = {"employee:Employee","skill_type:SkillType"};
		addRelationIndex("EmployeeSkill",employeeSkillRelatedObjectNames);

		String [] employeePerformanceRelatedObjectNames = {"employee:Employee"};
		addRelationIndex("EmployeePerformance",employeePerformanceRelatedObjectNames);

		String [] employeeWorkExperienceRelatedObjectNames = {"employee:Employee"};
		addRelationIndex("EmployeeWorkExperience",employeeWorkExperienceRelatedObjectNames);

		String [] employeeLeaveRelatedObjectNames = {"who:Employee","type:LeaveType"};
		addRelationIndex("EmployeeLeave",employeeLeaveRelatedObjectNames);

		String [] employeeInterviewRelatedObjectNames = {"employee:Employee","interview_type:InterviewType"};
		addRelationIndex("EmployeeInterview",employeeInterviewRelatedObjectNames);

		String [] employeeAttendanceRelatedObjectNames = {"employee:Employee"};
		addRelationIndex("EmployeeAttendance",employeeAttendanceRelatedObjectNames);

		String [] employeeQualifierRelatedObjectNames = {"employee:Employee"};
		addRelationIndex("EmployeeQualifier",employeeQualifierRelatedObjectNames);

		String [] employeeEducationRelatedObjectNames = {"employee:Employee"};
		addRelationIndex("EmployeeEducation",employeeEducationRelatedObjectNames);

		String [] employeeAwardRelatedObjectNames = {"employee:Employee"};
		addRelationIndex("EmployeeAward",employeeAwardRelatedObjectNames);

		String [] employeeSalarySheetRelatedObjectNames = {"employee:Employee","current_salary_grade:SalaryGrade","paying_off:PayingOff"};
		addRelationIndex("EmployeeSalarySheet",employeeSalarySheetRelatedObjectNames);

		String [] payingOffRelatedObjectNames = {"paid_for:Employee"};
		addRelationIndex("PayingOff",payingOffRelatedObjectNames);

		String [] pageRelatedObjectNames = {"page_type:PageType","mobile_app:MobileApp"};
		addRelationIndex("Page",pageRelatedObjectNames);

		String [] pageTypeRelatedObjectNames = {"mobile_app:MobileApp"};
		addRelationIndex("PageType",pageTypeRelatedObjectNames);

		String [] slideRelatedObjectNames = {"page:Page"};
		addRelationIndex("Slide",slideRelatedObjectNames);

		String [] uiActionRelatedObjectNames = {"page:Page"};
		addRelationIndex("UiAction",uiActionRelatedObjectNames);

		String [] sectionRelatedObjectNames = {"page:Page"};
		addRelationIndex("Section",sectionRelatedObjectNames);

		String [] userAllowListRelatedObjectNames = {"domain:UserDomain"};
		addRelationIndex("UserAllowList",userAllowListRelatedObjectNames);

		String [] secUserRelatedObjectNames = {"domain:UserDomain"};
		addRelationIndex("SecUser",secUserRelatedObjectNames);

		String [] userAppRelatedObjectNames = {"sec_user:SecUser"};
		addRelationIndex("UserApp",userAppRelatedObjectNames);

		String [] quickLinkRelatedObjectNames = {"app:UserApp"};
		addRelationIndex("QuickLink",quickLinkRelatedObjectNames);

		String [] listAccessRelatedObjectNames = {"app:UserApp"};
		addRelationIndex("ListAccess",listAccessRelatedObjectNames);

		String [] loginHistoryRelatedObjectNames = {"sec_user:SecUser"};
		addRelationIndex("LoginHistory",loginHistoryRelatedObjectNames);

		String [] candidateElementRelatedObjectNames = {"container:CandidateContainer"};
		addRelationIndex("CandidateElement",candidateElementRelatedObjectNames);

		String [] wechatWorkappIdentityRelatedObjectNames = {"sec_user:SecUser"};
		addRelationIndex("WechatWorkappIdentity",wechatWorkappIdentityRelatedObjectNames);

		String [] wechatMiniappIdentityRelatedObjectNames = {"sec_user:SecUser"};
		addRelationIndex("WechatMiniappIdentity",wechatMiniappIdentityRelatedObjectNames);

		String [] keyPairIdentityRelatedObjectNames = {"key_type:PublicKeyType","sec_user:SecUser"};
		addRelationIndex("KeyPairIdentity",keyPairIdentityRelatedObjectNames);

		String [] publicKeyTypeRelatedObjectNames = {"domain:UserDomain"};
		addRelationIndex("PublicKeyType",publicKeyTypeRelatedObjectNames);

	
	
	}
	protected static final String TRUST_CHAIN_READ = "R";
	protected static final String TRUST_CHAIN_WRITE = "W";
	protected static final String TRUST_CHAIN_MANAGEMENT = "M";
	protected static final String TRUST_CHAIN_EXECUTION = "X";
	
	protected static final String TRUST_READ = "r";
	protected static final String TRUST_WRITE = "w";
	protected static final String TRUST_MANAGEMENT = "m";
	protected static final String TRUST_EXECUTION = "x";
	
	protected static final String TRUST_CHAIN_ALL = "MXWR";
	
	
	//small 'r','w','m','x' mean no chain trust, just trust the same level
	//default for reading trust chain, the default sequence are MXWR, the order is not affect the result
	protected void prepareRelation()
	{
		addGenericRelation("Catalog"                               ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("LevelOneCategory"                      ,TRUST_CHAIN_READ,"catalog");
		addGenericRelation("LevelTwoCategory"                      ,TRUST_CHAIN_READ,"parentCategory");
		addGenericRelation("LevelThreeCategory"                    ,TRUST_CHAIN_READ,"parentCategory");
		addGenericRelation("Product"                               ,TRUST_CHAIN_READ,"parentCategory");
		addGenericRelation("Sku"                                   ,TRUST_CHAIN_READ,"product");
		addGenericRelation("RetailStoreProvinceCenter"             ,TRUST_CHAIN_READ,"country");
		addGenericRelation("ProvinceCenterDepartment"              ,TRUST_CHAIN_READ,"provinceCenter");
		addGenericRelation("ProvinceCenterEmployee"                ,TRUST_CHAIN_READ,"department");
		addGenericRelation("ProvinceCenterEmployee"                ,TRUST_CHAIN_READ,"provinceCenter");
		addGenericRelation("RetailStoreCityServiceCenter"          ,TRUST_CHAIN_READ,"belongsTo");
		addGenericRelation("CityPartner"                           ,TRUST_CHAIN_READ,"cityServiceCenter");
		addGenericRelation("PotentialCustomer"                     ,TRUST_CHAIN_READ,"cityServiceCenter");
		addGenericRelation("PotentialCustomer"                     ,TRUST_CHAIN_READ,"cityPartner");
		addGenericRelation("PotentialCustomerContactPerson"        ,TRUST_CHAIN_READ,"potentialCustomer");
		addGenericRelation("PotentialCustomerContact"              ,TRUST_CHAIN_READ,"potentialCustomer");
		addGenericRelation("PotentialCustomerContact"              ,TRUST_CHAIN_READ,"cityPartner");
		addGenericRelation("PotentialCustomerContact"              ,TRUST_CHAIN_READ,"contactTo");
		addGenericRelation("CityEvent"                             ,TRUST_CHAIN_READ,"cityServiceCenter");
		addGenericRelation("EventAttendance"                       ,TRUST_CHAIN_READ,"potentialCustomer");
		addGenericRelation("EventAttendance"                       ,TRUST_CHAIN_READ,"cityEvent");
		addGenericRelation("RetailStore"                           ,TRUST_CHAIN_READ,"retailStoreCountryCenter");
		addGenericRelation("RetailStore"                           ,TRUST_CHAIN_READ,"cityServiceCenter");
		addGenericRelation("RetailStore"                           ,TRUST_CHAIN_READ,"creation");
		addGenericRelation("RetailStore"                           ,TRUST_CHAIN_READ,"investmentInvitation");
		addGenericRelation("RetailStore"                           ,TRUST_CHAIN_READ,"franchising");
		addGenericRelation("RetailStore"                           ,TRUST_CHAIN_READ,"decoration");
		addGenericRelation("RetailStore"                           ,TRUST_CHAIN_READ,"opening");
		addGenericRelation("RetailStore"                           ,TRUST_CHAIN_READ,"closing");
		addGenericRelation("RetailStoreMember"                     ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("ConsumerOrder"                         ,TRUST_CHAIN_READ,"consumer");
		addGenericRelation("ConsumerOrder"                         ,TRUST_CHAIN_READ,"store");
		addGenericRelation("ConsumerOrderLineItem"                 ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("ConsumerOrderShippingGroup"            ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("ConsumerOrderPaymentGroup"             ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("ConsumerOrderPriceAdjustment"          ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("RetailStoreMemberCoupon"               ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("MemberWishlist"                        ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("MemberRewardPoint"                     ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("MemberRewardPointRedemption"           ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("MemberWishlistProduct"                 ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("RetailStoreMemberAddress"              ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("RetailStoreMemberGiftCard"             ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("RetailStoreMemberGiftCardConsumeRecord",TRUST_CHAIN_READ,"owner");
		addGenericRelation("RetailStoreMemberGiftCardConsumeRecord",TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("GoodsSupplier"                         ,TRUST_CHAIN_READ,"belongTo");
		addGenericRelation("SupplierProduct"                       ,TRUST_CHAIN_READ,"supplier");
		addGenericRelation("ProductSupplyDuration"                 ,TRUST_CHAIN_READ,"product");
		addGenericRelation("SupplyOrder"                           ,TRUST_CHAIN_READ,"buyer");
		addGenericRelation("SupplyOrder"                           ,TRUST_CHAIN_READ,"seller");
		addGenericRelation("SupplyOrderLineItem"                   ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("SupplyOrderShippingGroup"              ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("SupplyOrderPaymentGroup"               ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("RetailStoreOrder"                      ,TRUST_CHAIN_READ,"buyer");
		addGenericRelation("RetailStoreOrder"                      ,TRUST_CHAIN_READ,"seller");
		addGenericRelation("RetailStoreOrderLineItem"              ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("RetailStoreOrderShippingGroup"         ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("RetailStoreOrderPaymentGroup"          ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("Warehouse"                             ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("StorageSpace"                          ,TRUST_CHAIN_READ,"warehouse");
		addGenericRelation("SmartPallet"                           ,TRUST_CHAIN_READ,"warehouse");
		addGenericRelation("GoodsShelf"                            ,TRUST_CHAIN_READ,"storageSpace");
		addGenericRelation("GoodsShelf"                            ,TRUST_CHAIN_READ,"supplierSpace");
		addGenericRelation("GoodsShelf"                            ,TRUST_CHAIN_READ,"damageSpace");
		addGenericRelation("GoodsShelfStockCount"                  ,TRUST_CHAIN_READ,"shelf");
		addGenericRelation("StockCountIssueTrack"                  ,TRUST_CHAIN_READ,"stockCount");
		addGenericRelation("GoodsAllocation"                       ,TRUST_CHAIN_READ,"goodsShelf");
		addGenericRelation("Goods"                                 ,TRUST_CHAIN_READ,"sku");
		addGenericRelation("Goods"                                 ,TRUST_CHAIN_READ,"receivingSpace");
		addGenericRelation("Goods"                                 ,TRUST_CHAIN_READ,"goodsAllocation");
		addGenericRelation("Goods"                                 ,TRUST_CHAIN_READ,"smartPallet");
		addGenericRelation("Goods"                                 ,TRUST_CHAIN_READ,"shippingSpace");
		addGenericRelation("Goods"                                 ,TRUST_CHAIN_READ,"transportTask");
		addGenericRelation("Goods"                                 ,TRUST_CHAIN_READ,"retailStore");
		addGenericRelation("Goods"                                 ,TRUST_CHAIN_READ,"bizOrder");
		addGenericRelation("Goods"                                 ,TRUST_CHAIN_READ,"retailStoreOrder");
		addGenericRelation("GoodsMovement"                         ,TRUST_CHAIN_READ,"goods");
		addGenericRelation("SupplierSpace"                         ,TRUST_CHAIN_READ,"warehouse");
		addGenericRelation("ReceivingSpace"                        ,TRUST_CHAIN_READ,"warehouse");
		addGenericRelation("ShippingSpace"                         ,TRUST_CHAIN_READ,"warehouse");
		addGenericRelation("DamageSpace"                           ,TRUST_CHAIN_READ,"warehouse");
		addGenericRelation("WarehouseAsset"                        ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("TransportFleet"                        ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("TransportTruck"                        ,TRUST_CHAIN_READ,"owner");
		addGenericRelation("TruckDriver"                           ,TRUST_CHAIN_READ,"belongsTo");
		addGenericRelation("TransportTask"                         ,TRUST_CHAIN_READ,"end");
		addGenericRelation("TransportTask"                         ,TRUST_CHAIN_READ,"driver");
		addGenericRelation("TransportTask"                         ,TRUST_CHAIN_READ,"truck");
		addGenericRelation("TransportTask"                         ,TRUST_CHAIN_READ,"belongsTo");
		addGenericRelation("TransportTaskTrack"                    ,TRUST_CHAIN_READ,"movement");
		addGenericRelation("AccountSet"                            ,TRUST_CHAIN_READ,"countryCenter");
		addGenericRelation("AccountSet"                            ,TRUST_CHAIN_READ,"retailStore");
		addGenericRelation("AccountSet"                            ,TRUST_CHAIN_READ,"goodsSupplier");
		addGenericRelation("AccountingSubject"                     ,TRUST_CHAIN_READ,"accountSet");
		addGenericRelation("AccountingPeriod"                      ,TRUST_CHAIN_READ,"accountSet");
		addGenericRelation("AccountingDocumentType"                ,TRUST_CHAIN_READ,"accountingPeriod");
		addGenericRelation("AccountingDocument"                    ,TRUST_CHAIN_READ,"accountingPeriod");
		addGenericRelation("AccountingDocument"                    ,TRUST_CHAIN_READ,"documentType");
		addGenericRelation("OriginalVoucher"                       ,TRUST_CHAIN_READ,"belongsTo");
		addGenericRelation("AccountingDocumentLine"                ,TRUST_CHAIN_READ,"belongsTo");
		addGenericRelation("AccountingDocumentLine"                ,TRUST_CHAIN_READ,"accountingSubject");
		addGenericRelation("LevelOneDepartment"                    ,TRUST_CHAIN_READ,"belongsTo");
		addGenericRelation("LevelTwoDepartment"                    ,TRUST_CHAIN_READ,"belongsTo");
		addGenericRelation("LevelThreeDepartment"                  ,TRUST_CHAIN_READ,"belongsTo");
		addGenericRelation("SkillType"                             ,TRUST_CHAIN_READ,"company");
		addGenericRelation("ResponsibilityType"                    ,TRUST_CHAIN_READ,"company");
		addGenericRelation("TerminationReason"                     ,TRUST_CHAIN_READ,"company");
		addGenericRelation("TerminationType"                       ,TRUST_CHAIN_READ,"company");
		addGenericRelation("OccupationType"                        ,TRUST_CHAIN_READ,"company");
		addGenericRelation("LeaveType"                             ,TRUST_CHAIN_READ,"company");
		addGenericRelation("SalaryGrade"                           ,TRUST_CHAIN_READ,"company");
		addGenericRelation("InterviewType"                         ,TRUST_CHAIN_READ,"company");
		addGenericRelation("TrainingCourseType"                    ,TRUST_CHAIN_READ,"company");
		addGenericRelation("PublicHoliday"                         ,TRUST_CHAIN_READ,"company");
		addGenericRelation("Termination"                           ,TRUST_CHAIN_READ,"reason");
		addGenericRelation("Termination"                           ,TRUST_CHAIN_READ,"type");
		addGenericRelation("Employee"                              ,TRUST_CHAIN_READ,"company");
		addGenericRelation("Employee"                              ,TRUST_CHAIN_READ,"department");
		addGenericRelation("Employee"                              ,TRUST_CHAIN_READ,"occupation");
		addGenericRelation("Employee"                              ,TRUST_CHAIN_READ,"responsibleFor");
		addGenericRelation("Employee"                              ,TRUST_CHAIN_READ,"currentSalaryGrade");
		addGenericRelation("Instructor"                            ,TRUST_CHAIN_READ,"company");
		addGenericRelation("CompanyTraining"                       ,TRUST_CHAIN_READ,"company");
		addGenericRelation("CompanyTraining"                       ,TRUST_CHAIN_READ,"instructor");
		addGenericRelation("CompanyTraining"                       ,TRUST_CHAIN_READ,"trainingCourseType");
		addGenericRelation("EmployeeCompanyTraining"               ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeCompanyTraining"               ,TRUST_CHAIN_READ,"training");
		addGenericRelation("EmployeeCompanyTraining"               ,TRUST_CHAIN_READ,"scoring");
		addGenericRelation("EmployeeSkill"                         ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeSkill"                         ,TRUST_CHAIN_READ,"skillType");
		addGenericRelation("EmployeePerformance"                   ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeWorkExperience"                ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeLeave"                         ,TRUST_CHAIN_READ,"who");
		addGenericRelation("EmployeeLeave"                         ,TRUST_CHAIN_READ,"type");
		addGenericRelation("EmployeeInterview"                     ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeInterview"                     ,TRUST_CHAIN_READ,"interviewType");
		addGenericRelation("EmployeeAttendance"                    ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeQualifier"                     ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeEducation"                     ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeAward"                         ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeSalarySheet"                   ,TRUST_CHAIN_READ,"employee");
		addGenericRelation("EmployeeSalarySheet"                   ,TRUST_CHAIN_READ,"currentSalaryGrade");
		addGenericRelation("EmployeeSalarySheet"                   ,TRUST_CHAIN_READ,"payingOff");
		addGenericRelation("PayingOff"                             ,TRUST_CHAIN_READ,"paidFor");
		addGenericRelation("Page"                                  ,TRUST_CHAIN_READ,"pageType");
		addGenericRelation("Page"                                  ,TRUST_CHAIN_READ,"mobileApp");
		addGenericRelation("PageType"                              ,TRUST_CHAIN_READ,"mobileApp");
		addGenericRelation("Slide"                                 ,TRUST_CHAIN_READ,"page");
		addGenericRelation("UiAction"                              ,TRUST_CHAIN_READ,"page");
		addGenericRelation("Section"                               ,TRUST_CHAIN_READ,"page");
		addGenericRelation("UserAllowList"                         ,TRUST_CHAIN_READ,"domain");
		addGenericRelation("SecUser"                               ,TRUST_CHAIN_READ,"domain");
		addGenericRelation("UserApp"                               ,TRUST_CHAIN_READ,"secUser");
		addGenericRelation("QuickLink"                             ,TRUST_CHAIN_READ,"app");
		addGenericRelation("ListAccess"                            ,TRUST_CHAIN_READ,"app");
		addGenericRelation("LoginHistory"                          ,TRUST_CHAIN_READ,"secUser");
		addGenericRelation("CandidateElement"                      ,TRUST_CHAIN_READ,"container");
		addGenericRelation("WechatWorkappIdentity"                 ,TRUST_CHAIN_READ,"secUser");
		addGenericRelation("WechatMiniappIdentity"                 ,TRUST_CHAIN_READ,"secUser");
		addGenericRelation("KeyPairIdentity"                       ,TRUST_CHAIN_READ,"keyType");
		addGenericRelation("KeyPairIdentity"                       ,TRUST_CHAIN_READ,"secUser");
		addGenericRelation("PublicKeyType"                         ,TRUST_CHAIN_READ,"domain");
	
	}

	


}


