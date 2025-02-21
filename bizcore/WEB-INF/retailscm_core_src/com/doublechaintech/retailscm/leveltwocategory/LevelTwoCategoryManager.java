
package com.doublechaintech.retailscm.leveltwocategory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.List;
import com.terapico.caf.DateTime;
import com.terapico.caf.Images;
import com.doublechaintech.retailscm.RetailscmUserContext;
import com.doublechaintech.retailscm.BaseEntity;
import com.doublechaintech.retailscm.BaseManager;
import com.doublechaintech.retailscm.SmartList;




public interface LevelTwoCategoryManager extends BaseManager{

		

  List<LevelTwoCategory> searchLevelTwoCategoryList(RetailscmUserContext ctx, LevelTwoCategoryRequest pRequest);
  LevelTwoCategory searchLevelTwoCategory(RetailscmUserContext ctx, LevelTwoCategoryRequest pRequest);
	public LevelTwoCategory createLevelTwoCategory(RetailscmUserContext userContext, String parentCategoryId,String name) throws Exception;
	public LevelTwoCategory updateLevelTwoCategory(RetailscmUserContext userContext,String levelTwoCategoryId, int levelTwoCategoryVersion, String property, String newValueExpr,String [] tokensExpr) throws Exception;
	public LevelTwoCategory loadLevelTwoCategory(RetailscmUserContext userContext, String levelTwoCategoryId, String [] tokensExpr) throws Exception;
	public void sendAllItems(RetailscmUserContext ctx) throws Exception ;
	public LevelTwoCategory internalSaveLevelTwoCategory(RetailscmUserContext userContext, LevelTwoCategory levelTwoCategory) throws Exception;
	public LevelTwoCategory internalSaveLevelTwoCategory(RetailscmUserContext userContext, LevelTwoCategory levelTwoCategory,Map<String,Object>option) throws Exception;

	public LevelTwoCategory transferToAnotherParentCategory(RetailscmUserContext userContext, String levelTwoCategoryId, String anotherParentCategoryId)  throws Exception;
 

	public void delete(RetailscmUserContext userContext, String levelTwoCategoryId, int version) throws Exception;
	public int deleteAll(RetailscmUserContext userContext, String secureCode ) throws Exception;
	public void onNewInstanceCreated(RetailscmUserContext userContext, LevelTwoCategory newCreated)throws Exception;
	public default void onUpdated(RetailscmUserContext userContext, LevelTwoCategory updated, Object actor, String methodName) throws Exception {};


	/*======================================================DATA MAINTENANCE===========================================================*/


	//public  LevelThreeCategoryManager getLevelThreeCategoryManager(RetailscmUserContext userContext, String levelTwoCategoryId, String name ,String [] tokensExpr)  throws Exception;

	public  LevelTwoCategory addLevelThreeCategory(RetailscmUserContext userContext, String levelTwoCategoryId, String name , String [] tokensExpr)  throws Exception;
	public  LevelTwoCategory removeLevelThreeCategory(RetailscmUserContext userContext, String levelTwoCategoryId, String levelThreeCategoryId, int levelThreeCategoryVersion,String [] tokensExpr)  throws Exception;
	public  LevelTwoCategory updateLevelThreeCategory(RetailscmUserContext userContext, String levelTwoCategoryId, String levelThreeCategoryId, int levelThreeCategoryVersion, String property, String newValueExpr,String [] tokensExpr)  throws Exception;

	/*

	*/


	public Object listByParentCategory(RetailscmUserContext userContext,String parentCategoryId) throws Exception;
	public Object listPageByParentCategory(RetailscmUserContext userContext,String parentCategoryId, int start, int count) throws Exception;
  




}


