
package com.doublechaintech.retailscm.accountingdocument;















import com.doublechaintech.retailscm.*;import com.doublechaintech.retailscm.BaseViewPage;import com.doublechaintech.retailscm.RetailscmUserContextImpl;import com.doublechaintech.retailscm.accountingdocument.AccountingDocument;import com.doublechaintech.retailscm.accountingdocumentline.AccountingDocumentLine;import com.doublechaintech.retailscm.accountingdocumenttype.AccountingDocumentType;import com.doublechaintech.retailscm.accountingdocumenttype.CandidateAccountingDocumentType;import com.doublechaintech.retailscm.accountingperiod.AccountingPeriod;import com.doublechaintech.retailscm.accountingperiod.CandidateAccountingPeriod;import com.doublechaintech.retailscm.accountingsubject.AccountingSubject;import com.doublechaintech.retailscm.iamservice.*;import com.doublechaintech.retailscm.originalvoucher.OriginalVoucher;import com.doublechaintech.retailscm.secuser.SecUser;import com.doublechaintech.retailscm.services.IamService;import com.doublechaintech.retailscm.tree.*;import com.doublechaintech.retailscm.treenode.*;import com.doublechaintech.retailscm.userapp.UserApp;import com.doublechaintech.retailscm.utils.ModelAssurance;
import com.terapico.caf.BlobObject;import com.terapico.caf.DateTime;import com.terapico.caf.Images;import com.terapico.caf.Password;import com.terapico.caf.baseelement.PlainText;import com.terapico.caf.viewpage.SerializeScope;
import com.terapico.uccaf.BaseUserContext;
import com.terapico.utils.*;
import java.math.BigDecimal;
import java.util.*;
import com.doublechaintech.retailscm.search.Searcher;


public class AccountingDocumentManagerImpl extends CustomRetailscmCheckerManager implements AccountingDocumentManager, BusinessHandler{

	// Only some of ods have such function
	
	// To test
	public BlobObject exportExcelFromList(RetailscmUserContext userContext, String id, String listName) throws Exception {

		Map<String,Object> tokens = AccountingDocumentTokens.start().withTokenFromListName(listName).done();
		AccountingDocument  accountingDocument = (AccountingDocument) this.loadAccountingDocument(userContext, id, tokens);
		//to enrich reference object to let it show display name
		List<BaseEntity> entityListToNaming = accountingDocument.collectRefercencesFromLists();
		accountingDocumentDaoOf(userContext).alias(entityListToNaming);

		return exportListToExcel(userContext, accountingDocument, listName);

	}
	@Override
	public BaseGridViewGenerator gridViewGenerator() {
		return new AccountingDocumentGridViewGenerator();
	}
	




  


	private static final String SERVICE_TYPE = "AccountingDocument";
	@Override
	public AccountingDocumentDAO daoOf(RetailscmUserContext userContext) {
		return accountingDocumentDaoOf(userContext);
	}

	@Override
	public String serviceFor(){
		return SERVICE_TYPE;
	}



	protected void throwExceptionWithMessage(String value) throws AccountingDocumentManagerException{

		Message message = new Message();
		message.setBody(value);
		throw new AccountingDocumentManagerException(message);

	}



 	protected AccountingDocument saveAccountingDocument(RetailscmUserContext userContext, AccountingDocument accountingDocument, String [] tokensExpr) throws Exception{
 		//return getAccountingDocumentDAO().save(accountingDocument, tokens);

 		Map<String,Object>tokens = parseTokens(tokensExpr);

 		return saveAccountingDocument(userContext, accountingDocument, tokens);
 	}

 	protected AccountingDocument saveAccountingDocumentDetail(RetailscmUserContext userContext, AccountingDocument accountingDocument) throws Exception{


 		return saveAccountingDocument(userContext, accountingDocument, allTokens());
 	}

 	public AccountingDocument loadAccountingDocument(RetailscmUserContext userContext, String accountingDocumentId, String [] tokensExpr) throws Exception{

 		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);

		checkerOf(userContext).throwExceptionIfHasErrors( AccountingDocumentManagerException.class);



 		Map<String,Object>tokens = parseTokens(tokensExpr);

 		AccountingDocument accountingDocument = loadAccountingDocument( userContext, accountingDocumentId, tokens);
 		//do some calc before sent to customer?
 		return present(userContext,accountingDocument, tokens);
 	}


 	 public AccountingDocument searchAccountingDocument(RetailscmUserContext userContext, String accountingDocumentId, String textToSearch,String [] tokensExpr) throws Exception{

 		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);

		checkerOf(userContext).throwExceptionIfHasErrors( AccountingDocumentManagerException.class);



 		Map<String,Object>tokens = tokens().allTokens().searchEntireObjectText(tokens().startsWith(), textToSearch).initWithArray(tokensExpr);

 		AccountingDocument accountingDocument = loadAccountingDocument( userContext, accountingDocumentId, tokens);
 		//do some calc before sent to customer?
 		return present(userContext,accountingDocument, tokens);
 	}



 	protected AccountingDocument present(RetailscmUserContext userContext, AccountingDocument accountingDocument, Map<String, Object> tokens) throws Exception {


		addActions(userContext,accountingDocument,tokens);
    

		AccountingDocument  accountingDocumentToPresent = accountingDocumentDaoOf(userContext).present(accountingDocument, tokens);

		List<BaseEntity> entityListToNaming = accountingDocumentToPresent.collectRefercencesFromLists();
		accountingDocumentDaoOf(userContext).alias(entityListToNaming);


		renderActionForList(userContext,accountingDocument,tokens);

		return  accountingDocumentToPresent;


	}



 	public AccountingDocument loadAccountingDocumentDetail(RetailscmUserContext userContext, String accountingDocumentId) throws Exception{
 		AccountingDocument accountingDocument = loadAccountingDocument( userContext, accountingDocumentId, allTokens());
 		return present(userContext,accountingDocument, allTokens());

 	}

	public Object prepareContextForUserApp(BaseUserContext userContext,Object targetUserApp) throws Exception{
		
        UserApp userApp=(UserApp) targetUserApp;
        return this.view ((RetailscmUserContext)userContext,userApp.getAppId());
        
    }

	


 	public Object view(RetailscmUserContext userContext, String accountingDocumentId) throws Exception{
 		AccountingDocument accountingDocument = loadAccountingDocument( userContext, accountingDocumentId, viewTokens());
 		markVisited(userContext, accountingDocument);
 		return present(userContext,accountingDocument, viewTokens());

	 }
	 public Object summaryView(RetailscmUserContext userContext, String accountingDocumentId) throws Exception{
		AccountingDocument accountingDocument = loadAccountingDocument( userContext, accountingDocumentId, viewTokens());
		accountingDocument.summarySuffix();
		markVisited(userContext, accountingDocument);
 		return present(userContext,accountingDocument, summaryTokens());

	}
	 public Object analyze(RetailscmUserContext userContext, String accountingDocumentId) throws Exception{
		AccountingDocument accountingDocument = loadAccountingDocument( userContext, accountingDocumentId, analyzeTokens());
		markVisited(userContext, accountingDocument);
		return present(userContext,accountingDocument, analyzeTokens());

	}
 	protected AccountingDocument saveAccountingDocument(RetailscmUserContext userContext, AccountingDocument accountingDocument, Map<String,Object>tokens) throws Exception{
 	
 		return accountingDocumentDaoOf(userContext).save(accountingDocument, tokens);
 	}
 	protected AccountingDocument loadAccountingDocument(RetailscmUserContext userContext, String accountingDocumentId, Map<String,Object>tokens) throws Exception{
		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);

		checkerOf(userContext).throwExceptionIfHasErrors( AccountingDocumentManagerException.class);



 		return accountingDocumentDaoOf(userContext).load(accountingDocumentId, tokens);
 	}

	







 	protected<T extends BaseEntity> void addActions(RetailscmUserContext userContext, AccountingDocument accountingDocument, Map<String, Object> tokens){
		super.addActions(userContext, accountingDocument, tokens);

		addAction(userContext, accountingDocument, tokens,"@create","createAccountingDocument","createAccountingDocument/","main","primary");
		addAction(userContext, accountingDocument, tokens,"@update","updateAccountingDocument","updateAccountingDocument/"+accountingDocument.getId()+"/","main","primary");
		addAction(userContext, accountingDocument, tokens,"@copy","cloneAccountingDocument","cloneAccountingDocument/"+accountingDocument.getId()+"/","main","primary");

		addAction(userContext, accountingDocument, tokens,"accounting_document.transfer_to_accounting_period","transferToAnotherAccountingPeriod","transferToAnotherAccountingPeriod/"+accountingDocument.getId()+"/","main","primary");
		addAction(userContext, accountingDocument, tokens,"accounting_document.transfer_to_document_type","transferToAnotherDocumentType","transferToAnotherDocumentType/"+accountingDocument.getId()+"/","main","primary");
		addAction(userContext, accountingDocument, tokens,"accounting_document.addOriginalVoucher","addOriginalVoucher","addOriginalVoucher/"+accountingDocument.getId()+"/","originalVoucherList","primary");
		addAction(userContext, accountingDocument, tokens,"accounting_document.removeOriginalVoucher","removeOriginalVoucher","removeOriginalVoucher/"+accountingDocument.getId()+"/","originalVoucherList","primary");
		addAction(userContext, accountingDocument, tokens,"accounting_document.updateOriginalVoucher","updateOriginalVoucher","updateOriginalVoucher/"+accountingDocument.getId()+"/","originalVoucherList","primary");
		addAction(userContext, accountingDocument, tokens,"accounting_document.copyOriginalVoucherFrom","copyOriginalVoucherFrom","copyOriginalVoucherFrom/"+accountingDocument.getId()+"/","originalVoucherList","primary");
		addAction(userContext, accountingDocument, tokens,"accounting_document.addAccountingDocumentLine","addAccountingDocumentLine","addAccountingDocumentLine/"+accountingDocument.getId()+"/","accountingDocumentLineList","primary");
		addAction(userContext, accountingDocument, tokens,"accounting_document.removeAccountingDocumentLine","removeAccountingDocumentLine","removeAccountingDocumentLine/"+accountingDocument.getId()+"/","accountingDocumentLineList","primary");
		addAction(userContext, accountingDocument, tokens,"accounting_document.updateAccountingDocumentLine","updateAccountingDocumentLine","updateAccountingDocumentLine/"+accountingDocument.getId()+"/","accountingDocumentLineList","primary");
		addAction(userContext, accountingDocument, tokens,"accounting_document.copyAccountingDocumentLineFrom","copyAccountingDocumentLineFrom","copyAccountingDocumentLineFrom/"+accountingDocument.getId()+"/","accountingDocumentLineList","primary");






	}// end method of protected<T extends BaseEntity> void addActions(RetailscmUserContext userContext, AccountingDocument accountingDocument, Map<String, Object> tokens){








  @Override
  public List<AccountingDocument> searchAccountingDocumentList(RetailscmUserContext ctx, AccountingDocumentRequest pRequest){
      pRequest.setUserContext(ctx);
      List<AccountingDocument> list = daoOf(ctx).search(pRequest);
      Searcher.enhance(list, pRequest);
      return list;
  }

  @Override
  public AccountingDocument searchAccountingDocument(RetailscmUserContext ctx, AccountingDocumentRequest pRequest){
    pRequest.limit(0, 1);
    List<AccountingDocument> list = searchAccountingDocumentList(ctx, pRequest);
    if (list == null || list.isEmpty()){
      return null;
    }
    return list.get(0);
  }

	public AccountingDocument createAccountingDocument(RetailscmUserContext userContext, String name,Date accountingDocumentDate,String accountingPeriodId,String documentTypeId) throws Exception
	{





		checkerOf(userContext).checkNameOfAccountingDocument(name);
		checkerOf(userContext).checkAccountingDocumentDateOfAccountingDocument(accountingDocumentDate);


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);



		AccountingDocument accountingDocument=createNewAccountingDocument();	

		accountingDocument.setName(name);
		accountingDocument.setAccountingDocumentDate(accountingDocumentDate);
			
		AccountingPeriod accountingPeriod = loadAccountingPeriod(userContext, accountingPeriodId,emptyOptions());
		accountingDocument.setAccountingPeriod(accountingPeriod);
		
		
			
		AccountingDocumentType documentType = loadAccountingDocumentType(userContext, documentTypeId,emptyOptions());
		accountingDocument.setDocumentType(documentType);
		
		

		accountingDocument = saveAccountingDocument(userContext, accountingDocument, emptyOptions());
		
		onNewInstanceCreated(userContext, accountingDocument);
		return accountingDocument;


	}
	protected AccountingDocument createNewAccountingDocument()
	{

		return new AccountingDocument();
	}

	protected void checkParamsForUpdatingAccountingDocument(RetailscmUserContext userContext,String accountingDocumentId, int accountingDocumentVersion, String property, String newValueExpr,String [] tokensExpr)throws Exception
	{
		



		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);
		checkerOf(userContext).checkVersionOfAccountingDocument( accountingDocumentVersion);


		if(AccountingDocument.NAME_PROPERTY.equals(property)){
		
			checkerOf(userContext).checkNameOfAccountingDocument(parseString(newValueExpr));
		

		}
		if(AccountingDocument.ACCOUNTING_DOCUMENT_DATE_PROPERTY.equals(property)){
		
			checkerOf(userContext).checkAccountingDocumentDateOfAccountingDocument(parseDate(newValueExpr));
		

		}

		

		


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);



	}



	public AccountingDocument clone(RetailscmUserContext userContext, String fromAccountingDocumentId) throws Exception{

		return accountingDocumentDaoOf(userContext).clone(fromAccountingDocumentId, this.allTokens());
	}

	public AccountingDocument internalSaveAccountingDocument(RetailscmUserContext userContext, AccountingDocument accountingDocument) throws Exception
	{
		return internalSaveAccountingDocument(userContext, accountingDocument, allTokens());

	}
	public AccountingDocument internalSaveAccountingDocument(RetailscmUserContext userContext, AccountingDocument accountingDocument, Map<String,Object> options) throws Exception
	{
		//checkParamsForUpdatingAccountingDocument(userContext, accountingDocumentId, accountingDocumentVersion, property, newValueExpr, tokensExpr);


		synchronized(accountingDocument){
			//will be good when the accountingDocument loaded from this JVM process cache.
			//also good when there is a ram based DAO implementation
			//make changes to AccountingDocument.
			if (accountingDocument.isChanged()){
			
			}

      //checkerOf(userContext).checkAndFixAccountingDocument(accountingDocument);
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, options);
			return accountingDocument;

		}

	}

	public AccountingDocument updateAccountingDocument(RetailscmUserContext userContext,String accountingDocumentId, int accountingDocumentVersion, String property, String newValueExpr,String [] tokensExpr) throws Exception
	{
		checkParamsForUpdatingAccountingDocument(userContext, accountingDocumentId, accountingDocumentVersion, property, newValueExpr, tokensExpr);



		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
		if(accountingDocument.getVersion() != accountingDocumentVersion){
			String message = "The target version("+accountingDocument.getVersion()+") is not equals to version("+accountingDocumentVersion+") provided";
			throwExceptionWithMessage(message);
		}
		synchronized(accountingDocument){
			//will be good when the accountingDocument loaded from this JVM process cache.
			//also good when there is a ram based DAO implementation
			//make changes to AccountingDocument.
			
			accountingDocument.changeProperty(property, newValueExpr);
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().done());
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
			//return saveAccountingDocument(userContext, accountingDocument, tokens().done());
		}

	}

	public AccountingDocument updateAccountingDocumentProperty(RetailscmUserContext userContext,String accountingDocumentId, int accountingDocumentVersion, String property, String newValueExpr,String [] tokensExpr) throws Exception
	{
		checkParamsForUpdatingAccountingDocument(userContext, accountingDocumentId, accountingDocumentVersion, property, newValueExpr, tokensExpr);

		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
		if(accountingDocument.getVersion() != accountingDocumentVersion){
			String message = "The target version("+accountingDocument.getVersion()+") is not equals to version("+accountingDocumentVersion+") provided";
			throwExceptionWithMessage(message);
		}
		synchronized(accountingDocument){
			//will be good when the accountingDocument loaded from this JVM process cache.
			//also good when there is a ram based DAO implementation
			//make changes to AccountingDocument.

			accountingDocument.changeProperty(property, newValueExpr);
			
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().done());
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
			//return saveAccountingDocument(userContext, accountingDocument, tokens().done());
		}

	}
	protected Map<String,Object> emptyOptions(){
		return tokens().done();
	}

	protected AccountingDocumentTokens tokens(){
		return AccountingDocumentTokens.start();
	}
	protected Map<String,Object> parseTokens(String [] tokensExpr){
		return tokens().initWithArray(tokensExpr);
	}
	protected Map<String,Object> allTokens(){
		return AccountingDocumentTokens.all();
	}
	protected Map<String,Object> analyzeTokens(){
		return tokens().allTokens().analyzeAllLists().done();
	}
	protected Map<String,Object> summaryTokens(){
		return tokens().allTokens().done();
	}
	protected Map<String,Object> viewTokens(){
		return tokens().allTokens()
		.sortOriginalVoucherListWith(OriginalVoucher.ID_PROPERTY,sortDesc())
		.sortAccountingDocumentLineListWith(AccountingDocumentLine.ID_PROPERTY,sortDesc())
		.done();

	}
	protected Map<String,Object> mergedAllTokens(String []tokens){
		return AccountingDocumentTokens.mergeAll(tokens).done();
	}
	
	protected void checkParamsForTransferingAnotherAccountingPeriod(RetailscmUserContext userContext, String accountingDocumentId, String anotherAccountingPeriodId) throws Exception
 	{

 		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);
 		checkerOf(userContext).checkIdOfAccountingPeriod(anotherAccountingPeriodId);//check for optional reference

 		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);

 	}
 	public AccountingDocument transferToAnotherAccountingPeriod(RetailscmUserContext userContext, String accountingDocumentId, String anotherAccountingPeriodId) throws Exception
 	{
 		checkParamsForTransferingAnotherAccountingPeriod(userContext, accountingDocumentId,anotherAccountingPeriodId);
 
		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
		synchronized(accountingDocument){
			//will be good when the accountingDocument loaded from this JVM process cache.
			//also good when there is a ram based DAO implementation
			AccountingPeriod accountingPeriod = loadAccountingPeriod(userContext, anotherAccountingPeriodId, emptyOptions());
			accountingDocument.updateAccountingPeriod(accountingPeriod);
			
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, emptyOptions());

			return present(userContext,accountingDocument, allTokens());

		}

 	}

	


	public CandidateAccountingPeriod requestCandidateAccountingPeriod(RetailscmUserContext userContext, String ownerClass, String id, String filterKey, int pageNo) throws Exception {

		CandidateAccountingPeriod result = new CandidateAccountingPeriod();
		result.setOwnerClass(ownerClass);
		result.setOwnerId(id);
		result.setFilterKey(filterKey==null?"":filterKey.trim());
		result.setPageNo(pageNo);
		result.setValueFieldName("id");
		result.setDisplayFieldName("name");

		pageNo = Math.max(1, pageNo);
		int pageSize = 20;
		//requestCandidateProductForSkuAsOwner
		SmartList<AccountingPeriod> candidateList = accountingPeriodDaoOf(userContext).requestCandidateAccountingPeriodForAccountingDocument(userContext,ownerClass, id, filterKey, pageNo, pageSize);
		result.setCandidates(candidateList);
		int totalCount = candidateList.getTotalCount();
		result.setTotalPage(Math.max(1, (totalCount + pageSize -1)/pageSize ));
		return result;
	}

 	protected void checkParamsForTransferingAnotherDocumentType(RetailscmUserContext userContext, String accountingDocumentId, String anotherDocumentTypeId) throws Exception
 	{

 		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);
 		checkerOf(userContext).checkIdOfAccountingDocumentType(anotherDocumentTypeId);//check for optional reference

 		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);

 	}
 	public AccountingDocument transferToAnotherDocumentType(RetailscmUserContext userContext, String accountingDocumentId, String anotherDocumentTypeId) throws Exception
 	{
 		checkParamsForTransferingAnotherDocumentType(userContext, accountingDocumentId,anotherDocumentTypeId);
 
		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
		synchronized(accountingDocument){
			//will be good when the accountingDocument loaded from this JVM process cache.
			//also good when there is a ram based DAO implementation
			AccountingDocumentType documentType = loadAccountingDocumentType(userContext, anotherDocumentTypeId, emptyOptions());
			accountingDocument.updateDocumentType(documentType);
			
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, emptyOptions());

			return present(userContext,accountingDocument, allTokens());

		}

 	}

	


	public CandidateAccountingDocumentType requestCandidateDocumentType(RetailscmUserContext userContext, String ownerClass, String id, String filterKey, int pageNo) throws Exception {

		CandidateAccountingDocumentType result = new CandidateAccountingDocumentType();
		result.setOwnerClass(ownerClass);
		result.setOwnerId(id);
		result.setFilterKey(filterKey==null?"":filterKey.trim());
		result.setPageNo(pageNo);
		result.setValueFieldName("id");
		result.setDisplayFieldName("name");

		pageNo = Math.max(1, pageNo);
		int pageSize = 20;
		//requestCandidateProductForSkuAsOwner
		SmartList<AccountingDocumentType> candidateList = accountingDocumentTypeDaoOf(userContext).requestCandidateAccountingDocumentTypeForAccountingDocument(userContext,ownerClass, id, filterKey, pageNo, pageSize);
		result.setCandidates(candidateList);
		int totalCount = candidateList.getTotalCount();
		result.setTotalPage(Math.max(1, (totalCount + pageSize -1)/pageSize ));
		return result;
	}

 //--------------------------------------------------------------
	

 	protected AccountingPeriod loadAccountingPeriod(RetailscmUserContext userContext, String newAccountingPeriodId, Map<String,Object> options) throws Exception
 	{
    
 		return accountingPeriodDaoOf(userContext).load(newAccountingPeriodId, options);
 	  
 	}
 	


	

 	protected AccountingDocumentType loadAccountingDocumentType(RetailscmUserContext userContext, String newDocumentTypeId, Map<String,Object> options) throws Exception
 	{
    
 		return accountingDocumentTypeDaoOf(userContext).load(newDocumentTypeId, options);
 	  
 	}
 	


	
	//--------------------------------------------------------------

	public void delete(RetailscmUserContext userContext, String accountingDocumentId, int accountingDocumentVersion) throws Exception {
		//deleteInternal(userContext, accountingDocumentId, accountingDocumentVersion);
	}
	protected void deleteInternal(RetailscmUserContext userContext,
			String accountingDocumentId, int accountingDocumentVersion) throws Exception{

		accountingDocumentDaoOf(userContext).delete(accountingDocumentId, accountingDocumentVersion);
	}

	public AccountingDocument forgetByAll(RetailscmUserContext userContext, String accountingDocumentId, int accountingDocumentVersion) throws Exception {
		return forgetByAllInternal(userContext, accountingDocumentId, accountingDocumentVersion);
	}
	protected AccountingDocument forgetByAllInternal(RetailscmUserContext userContext,
			String accountingDocumentId, int accountingDocumentVersion) throws Exception{

		return accountingDocumentDaoOf(userContext).disconnectFromAll(accountingDocumentId, accountingDocumentVersion);
	}




	public int deleteAll(RetailscmUserContext userContext, String secureCode) throws Exception
	{
		/*
		if(!("dElEtEaLl".equals(secureCode))){
			throw new AccountingDocumentManagerException("Your secure code is not right, please guess again");
		}
		return deleteAllInternal(userContext);
		*/
		return 0;
	}


	protected int deleteAllInternal(RetailscmUserContext userContext) throws Exception{
		return accountingDocumentDaoOf(userContext).deleteAll();
	}





	protected void checkParamsForAddingOriginalVoucher(RetailscmUserContext userContext, String accountingDocumentId, String title, String madeBy, String receivedBy, String voucherType, String voucherImage,String [] tokensExpr) throws Exception{

				checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);


		checkerOf(userContext).checkTitleOfOriginalVoucher(title);

		checkerOf(userContext).checkMadeByOfOriginalVoucher(madeBy);

		checkerOf(userContext).checkReceivedByOfOriginalVoucher(receivedBy);

		checkerOf(userContext).checkVoucherTypeOfOriginalVoucher(voucherType);

		checkerOf(userContext).checkVoucherImageOfOriginalVoucher(voucherImage);


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);



	}
	public  AccountingDocument addOriginalVoucher(RetailscmUserContext userContext, String accountingDocumentId, String title, String madeBy, String receivedBy, String voucherType, String voucherImage, String [] tokensExpr) throws Exception
	{
		checkParamsForAddingOriginalVoucher(userContext,accountingDocumentId,title, madeBy, receivedBy, voucherType, voucherImage,tokensExpr);

		OriginalVoucher originalVoucher = createOriginalVoucher(userContext,title, madeBy, receivedBy, voucherType, voucherImage);

		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, emptyOptions());
		synchronized(accountingDocument){
			//Will be good when the accountingDocument loaded from this JVM process cache.
			//Also good when there is a RAM based DAO implementation
			accountingDocument.addOriginalVoucher( originalVoucher );
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withOriginalVoucherList().done());
			
			originalVoucherManagerOf(userContext).onNewInstanceCreated(userContext, originalVoucher);
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}
	}
	protected void checkParamsForUpdatingOriginalVoucherProperties(RetailscmUserContext userContext, String accountingDocumentId,String id,String title,String madeBy,String receivedBy,String voucherType,String voucherImage,String [] tokensExpr) throws Exception {

		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);
		checkerOf(userContext).checkIdOfOriginalVoucher(id);

		checkerOf(userContext).checkTitleOfOriginalVoucher( title);
		checkerOf(userContext).checkMadeByOfOriginalVoucher( madeBy);
		checkerOf(userContext).checkReceivedByOfOriginalVoucher( receivedBy);
		checkerOf(userContext).checkVoucherTypeOfOriginalVoucher( voucherType);
		checkerOf(userContext).checkVoucherImageOfOriginalVoucher( voucherImage);


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);


	}
	public  AccountingDocument updateOriginalVoucherProperties(RetailscmUserContext userContext, String accountingDocumentId, String id,String title,String madeBy,String receivedBy,String voucherType,String voucherImage, String [] tokensExpr) throws Exception
	{
		checkParamsForUpdatingOriginalVoucherProperties(userContext,accountingDocumentId,id,title,madeBy,receivedBy,voucherType,voucherImage,tokensExpr);

		Map<String, Object> options = tokens()
				.allTokens()
				//.withOriginalVoucherListList()
				.searchOriginalVoucherListWith(OriginalVoucher.ID_PROPERTY, tokens().is(), id).done();

		AccountingDocument accountingDocumentToUpdate = loadAccountingDocument(userContext, accountingDocumentId, options);

		if(accountingDocumentToUpdate.getOriginalVoucherList().isEmpty()){
			throw new AccountingDocumentManagerException("OriginalVoucher is NOT FOUND with id: '"+id+"'");
		}

		OriginalVoucher item = accountingDocumentToUpdate.getOriginalVoucherList().first();
		beforeUpdateOriginalVoucherProperties(userContext,item, accountingDocumentId,id,title,madeBy,receivedBy,voucherType,voucherImage,tokensExpr);
		item.updateTitle( title );
		item.updateMadeBy( madeBy );
		item.updateReceivedBy( receivedBy );
		item.updateVoucherType( voucherType );
		item.updateVoucherImage( voucherImage );


		//checkParamsForAddingOriginalVoucher(userContext,accountingDocumentId,name, code, used,tokensExpr);
		AccountingDocument accountingDocument = saveAccountingDocument(userContext, accountingDocumentToUpdate, tokens().withOriginalVoucherList().done());
		synchronized(accountingDocument){
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}
	}

	protected  void beforeUpdateOriginalVoucherProperties(RetailscmUserContext userContext, OriginalVoucher item, String accountingDocumentId, String id,String title,String madeBy,String receivedBy,String voucherType,String voucherImage, String [] tokensExpr)
						throws Exception {
			// by default, nothing to do
	}

	protected OriginalVoucher createOriginalVoucher(RetailscmUserContext userContext, String title, String madeBy, String receivedBy, String voucherType, String voucherImage) throws Exception{

		OriginalVoucher originalVoucher = new OriginalVoucher();
		
		
		originalVoucher.setTitle(title);		
		originalVoucher.setMadeBy(madeBy);		
		originalVoucher.setReceivedBy(receivedBy);		
		originalVoucher.setVoucherType(voucherType);		
		originalVoucher.setVoucherImage(voucherImage);
	
		
		return originalVoucher;


	}

	protected OriginalVoucher createIndexedOriginalVoucher(String id, int version){

		OriginalVoucher originalVoucher = new OriginalVoucher();
		originalVoucher.setId(id);
		originalVoucher.setVersion(version);
		return originalVoucher;

	}

	protected void checkParamsForRemovingOriginalVoucherList(RetailscmUserContext userContext, String accountingDocumentId,
			String originalVoucherIds[],String [] tokensExpr) throws Exception {

		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);
		for(String originalVoucherIdItem: originalVoucherIds){
			checkerOf(userContext).checkIdOfOriginalVoucher(originalVoucherIdItem);
		}


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);

	}
	public  AccountingDocument removeOriginalVoucherList(RetailscmUserContext userContext, String accountingDocumentId,
			String originalVoucherIds[],String [] tokensExpr) throws Exception{

			checkParamsForRemovingOriginalVoucherList(userContext, accountingDocumentId,  originalVoucherIds, tokensExpr);


			AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
			synchronized(accountingDocument){
				//Will be good when the accountingDocument loaded from this JVM process cache.
				//Also good when there is a RAM based DAO implementation
				accountingDocumentDaoOf(userContext).planToRemoveOriginalVoucherList(accountingDocument, originalVoucherIds, allTokens());
				accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withOriginalVoucherList().done());
				deleteRelationListInGraph(userContext, accountingDocument.getOriginalVoucherList());
				return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
			}
	}

	protected void checkParamsForRemovingOriginalVoucher(RetailscmUserContext userContext, String accountingDocumentId,
		String originalVoucherId, int originalVoucherVersion,String [] tokensExpr) throws Exception{
		
		checkerOf(userContext).checkIdOfAccountingDocument( accountingDocumentId);
		checkerOf(userContext).checkIdOfOriginalVoucher(originalVoucherId);
		checkerOf(userContext).checkVersionOfOriginalVoucher(originalVoucherVersion);

		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);


	}
	public  AccountingDocument removeOriginalVoucher(RetailscmUserContext userContext, String accountingDocumentId,
		String originalVoucherId, int originalVoucherVersion,String [] tokensExpr) throws Exception{

		checkParamsForRemovingOriginalVoucher(userContext,accountingDocumentId, originalVoucherId, originalVoucherVersion,tokensExpr);

		OriginalVoucher originalVoucher = createIndexedOriginalVoucher(originalVoucherId, originalVoucherVersion);
		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
		synchronized(accountingDocument){
			//Will be good when the accountingDocument loaded from this JVM process cache.
			//Also good when there is a RAM based DAO implementation
			accountingDocument.removeOriginalVoucher( originalVoucher );
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withOriginalVoucherList().done());
			deleteRelationInGraph(userContext, originalVoucher);
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}


	}
	protected void checkParamsForCopyingOriginalVoucher(RetailscmUserContext userContext, String accountingDocumentId,
		String originalVoucherId, int originalVoucherVersion,String [] tokensExpr) throws Exception{
		
		checkerOf(userContext).checkIdOfAccountingDocument( accountingDocumentId);
		checkerOf(userContext).checkIdOfOriginalVoucher(originalVoucherId);
		checkerOf(userContext).checkVersionOfOriginalVoucher(originalVoucherVersion);

		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);


	}
	public  AccountingDocument copyOriginalVoucherFrom(RetailscmUserContext userContext, String accountingDocumentId,
		String originalVoucherId, int originalVoucherVersion,String [] tokensExpr) throws Exception{

		checkParamsForCopyingOriginalVoucher(userContext,accountingDocumentId, originalVoucherId, originalVoucherVersion,tokensExpr);

		OriginalVoucher originalVoucher = createIndexedOriginalVoucher(originalVoucherId, originalVoucherVersion);
		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
		synchronized(accountingDocument){
			//Will be good when the accountingDocument loaded from this JVM process cache.
			//Also good when there is a RAM based DAO implementation

			

			accountingDocument.copyOriginalVoucherFrom( originalVoucher );
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withOriginalVoucherList().done());
			
			originalVoucherManagerOf(userContext).onNewInstanceCreated(userContext, (OriginalVoucher)accountingDocument.getFlexiableObjects().get(BaseEntity.COPIED_CHILD));
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}

	}

	protected void checkParamsForUpdatingOriginalVoucher(RetailscmUserContext userContext, String accountingDocumentId, String originalVoucherId, int originalVoucherVersion, String property, String newValueExpr,String [] tokensExpr) throws Exception{
		


		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);
		checkerOf(userContext).checkIdOfOriginalVoucher(originalVoucherId);
		checkerOf(userContext).checkVersionOfOriginalVoucher(originalVoucherVersion);


		if(OriginalVoucher.TITLE_PROPERTY.equals(property)){
			checkerOf(userContext).checkTitleOfOriginalVoucher(parseString(newValueExpr));
		}
		
		if(OriginalVoucher.MADE_BY_PROPERTY.equals(property)){
			checkerOf(userContext).checkMadeByOfOriginalVoucher(parseString(newValueExpr));
		}
		
		if(OriginalVoucher.RECEIVED_BY_PROPERTY.equals(property)){
			checkerOf(userContext).checkReceivedByOfOriginalVoucher(parseString(newValueExpr));
		}
		
		if(OriginalVoucher.VOUCHER_TYPE_PROPERTY.equals(property)){
			checkerOf(userContext).checkVoucherTypeOfOriginalVoucher(parseString(newValueExpr));
		}
		
		if(OriginalVoucher.VOUCHER_IMAGE_PROPERTY.equals(property)){
			checkerOf(userContext).checkVoucherImageOfOriginalVoucher(parseString(newValueExpr));
		}
		


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);


	}

	public  AccountingDocument updateOriginalVoucher(RetailscmUserContext userContext, String accountingDocumentId, String originalVoucherId, int originalVoucherVersion, String property, String newValueExpr,String [] tokensExpr)
			throws Exception{

		checkParamsForUpdatingOriginalVoucher(userContext, accountingDocumentId, originalVoucherId, originalVoucherVersion, property, newValueExpr,  tokensExpr);

		Map<String,Object> loadTokens = this.tokens().withOriginalVoucherList().searchOriginalVoucherListWith(OriginalVoucher.ID_PROPERTY, tokens().equals(), originalVoucherId).done();



		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, loadTokens);

		synchronized(accountingDocument){
			//Will be good when the accountingDocument loaded from this JVM process cache.
			//Also good when there is a RAM based DAO implementation
			//accountingDocument.removeOriginalVoucher( originalVoucher );
			//make changes to AcceleraterAccount.
			OriginalVoucher originalVoucherIdVersionKey = createIndexedOriginalVoucher(originalVoucherId, originalVoucherVersion);

			OriginalVoucher originalVoucher = accountingDocument.findTheOriginalVoucher(originalVoucherIdVersionKey);
			if(originalVoucher == null){
				throw new AccountingDocumentManagerException(originalVoucherId+" is NOT FOUND" );
			}

			beforeUpdateOriginalVoucher(userContext, originalVoucher, accountingDocumentId, originalVoucherId, originalVoucherVersion, property, newValueExpr,  tokensExpr);
			originalVoucher.changeProperty(property, newValueExpr);
			
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withOriginalVoucherList().done());
			originalVoucherManagerOf(userContext).onUpdated(userContext, originalVoucher, this, "updateOriginalVoucher");
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}

	}

	/** if you has something need to do before update data from DB, override this */
	protected void beforeUpdateOriginalVoucher(RetailscmUserContext userContext, OriginalVoucher existed, String accountingDocumentId, String originalVoucherId, int originalVoucherVersion, String property, String newValueExpr,String [] tokensExpr)
  			throws Exception{
  }
	/*

	*/




	protected void checkParamsForAddingAccountingDocumentLine(RetailscmUserContext userContext, String accountingDocumentId, String name, String code, String direct, BigDecimal amount, String accountingSubjectId,String [] tokensExpr) throws Exception{

				checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);


		checkerOf(userContext).checkNameOfAccountingDocumentLine(name);

		checkerOf(userContext).checkCodeOfAccountingDocumentLine(code);

		checkerOf(userContext).checkDirectOfAccountingDocumentLine(direct);

		checkerOf(userContext).checkAmountOfAccountingDocumentLine(amount);

		checkerOf(userContext).checkAccountingSubjectIdOfAccountingDocumentLine(accountingSubjectId);


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);



	}
	public  AccountingDocument addAccountingDocumentLine(RetailscmUserContext userContext, String accountingDocumentId, String name, String code, String direct, BigDecimal amount, String accountingSubjectId, String [] tokensExpr) throws Exception
	{
		checkParamsForAddingAccountingDocumentLine(userContext,accountingDocumentId,name, code, direct, amount, accountingSubjectId,tokensExpr);

		AccountingDocumentLine accountingDocumentLine = createAccountingDocumentLine(userContext,name, code, direct, amount, accountingSubjectId);

		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, emptyOptions());
		synchronized(accountingDocument){
			//Will be good when the accountingDocument loaded from this JVM process cache.
			//Also good when there is a RAM based DAO implementation
			accountingDocument.addAccountingDocumentLine( accountingDocumentLine );
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withAccountingDocumentLineList().done());
			
			accountingDocumentLineManagerOf(userContext).onNewInstanceCreated(userContext, accountingDocumentLine);
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}
	}
	protected void checkParamsForUpdatingAccountingDocumentLineProperties(RetailscmUserContext userContext, String accountingDocumentId,String id,String name,String code,String direct,BigDecimal amount,String [] tokensExpr) throws Exception {

		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);
		checkerOf(userContext).checkIdOfAccountingDocumentLine(id);

		checkerOf(userContext).checkNameOfAccountingDocumentLine( name);
		checkerOf(userContext).checkCodeOfAccountingDocumentLine( code);
		checkerOf(userContext).checkDirectOfAccountingDocumentLine( direct);
		checkerOf(userContext).checkAmountOfAccountingDocumentLine( amount);


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);


	}
	public  AccountingDocument updateAccountingDocumentLineProperties(RetailscmUserContext userContext, String accountingDocumentId, String id,String name,String code,String direct,BigDecimal amount, String [] tokensExpr) throws Exception
	{
		checkParamsForUpdatingAccountingDocumentLineProperties(userContext,accountingDocumentId,id,name,code,direct,amount,tokensExpr);

		Map<String, Object> options = tokens()
				.allTokens()
				//.withAccountingDocumentLineListList()
				.searchAccountingDocumentLineListWith(AccountingDocumentLine.ID_PROPERTY, tokens().is(), id).done();

		AccountingDocument accountingDocumentToUpdate = loadAccountingDocument(userContext, accountingDocumentId, options);

		if(accountingDocumentToUpdate.getAccountingDocumentLineList().isEmpty()){
			throw new AccountingDocumentManagerException("AccountingDocumentLine is NOT FOUND with id: '"+id+"'");
		}

		AccountingDocumentLine item = accountingDocumentToUpdate.getAccountingDocumentLineList().first();
		beforeUpdateAccountingDocumentLineProperties(userContext,item, accountingDocumentId,id,name,code,direct,amount,tokensExpr);
		item.updateName( name );
		item.updateCode( code );
		item.updateDirect( direct );
		item.updateAmount( amount );


		//checkParamsForAddingAccountingDocumentLine(userContext,accountingDocumentId,name, code, used,tokensExpr);
		AccountingDocument accountingDocument = saveAccountingDocument(userContext, accountingDocumentToUpdate, tokens().withAccountingDocumentLineList().done());
		synchronized(accountingDocument){
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}
	}

	protected  void beforeUpdateAccountingDocumentLineProperties(RetailscmUserContext userContext, AccountingDocumentLine item, String accountingDocumentId, String id,String name,String code,String direct,BigDecimal amount, String [] tokensExpr)
						throws Exception {
			// by default, nothing to do
	}

	protected AccountingDocumentLine createAccountingDocumentLine(RetailscmUserContext userContext, String name, String code, String direct, BigDecimal amount, String accountingSubjectId) throws Exception{

		AccountingDocumentLine accountingDocumentLine = new AccountingDocumentLine();
		
		
		accountingDocumentLine.setName(name);		
		accountingDocumentLine.setCode(code);		
		accountingDocumentLine.setDirect(direct);		
		accountingDocumentLine.setAmount(amount);		
		AccountingSubject  accountingSubject = new AccountingSubject();
		accountingSubject.setId(accountingSubjectId);		
		accountingDocumentLine.setAccountingSubject(accountingSubject);
	
		
		return accountingDocumentLine;


	}

	protected AccountingDocumentLine createIndexedAccountingDocumentLine(String id, int version){

		AccountingDocumentLine accountingDocumentLine = new AccountingDocumentLine();
		accountingDocumentLine.setId(id);
		accountingDocumentLine.setVersion(version);
		return accountingDocumentLine;

	}

	protected void checkParamsForRemovingAccountingDocumentLineList(RetailscmUserContext userContext, String accountingDocumentId,
			String accountingDocumentLineIds[],String [] tokensExpr) throws Exception {

		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);
		for(String accountingDocumentLineIdItem: accountingDocumentLineIds){
			checkerOf(userContext).checkIdOfAccountingDocumentLine(accountingDocumentLineIdItem);
		}


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);

	}
	public  AccountingDocument removeAccountingDocumentLineList(RetailscmUserContext userContext, String accountingDocumentId,
			String accountingDocumentLineIds[],String [] tokensExpr) throws Exception{

			checkParamsForRemovingAccountingDocumentLineList(userContext, accountingDocumentId,  accountingDocumentLineIds, tokensExpr);


			AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
			synchronized(accountingDocument){
				//Will be good when the accountingDocument loaded from this JVM process cache.
				//Also good when there is a RAM based DAO implementation
				accountingDocumentDaoOf(userContext).planToRemoveAccountingDocumentLineList(accountingDocument, accountingDocumentLineIds, allTokens());
				accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withAccountingDocumentLineList().done());
				deleteRelationListInGraph(userContext, accountingDocument.getAccountingDocumentLineList());
				return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
			}
	}

	protected void checkParamsForRemovingAccountingDocumentLine(RetailscmUserContext userContext, String accountingDocumentId,
		String accountingDocumentLineId, int accountingDocumentLineVersion,String [] tokensExpr) throws Exception{
		
		checkerOf(userContext).checkIdOfAccountingDocument( accountingDocumentId);
		checkerOf(userContext).checkIdOfAccountingDocumentLine(accountingDocumentLineId);
		checkerOf(userContext).checkVersionOfAccountingDocumentLine(accountingDocumentLineVersion);

		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);


	}
	public  AccountingDocument removeAccountingDocumentLine(RetailscmUserContext userContext, String accountingDocumentId,
		String accountingDocumentLineId, int accountingDocumentLineVersion,String [] tokensExpr) throws Exception{

		checkParamsForRemovingAccountingDocumentLine(userContext,accountingDocumentId, accountingDocumentLineId, accountingDocumentLineVersion,tokensExpr);

		AccountingDocumentLine accountingDocumentLine = createIndexedAccountingDocumentLine(accountingDocumentLineId, accountingDocumentLineVersion);
		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
		synchronized(accountingDocument){
			//Will be good when the accountingDocument loaded from this JVM process cache.
			//Also good when there is a RAM based DAO implementation
			accountingDocument.removeAccountingDocumentLine( accountingDocumentLine );
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withAccountingDocumentLineList().done());
			deleteRelationInGraph(userContext, accountingDocumentLine);
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}


	}
	protected void checkParamsForCopyingAccountingDocumentLine(RetailscmUserContext userContext, String accountingDocumentId,
		String accountingDocumentLineId, int accountingDocumentLineVersion,String [] tokensExpr) throws Exception{
		
		checkerOf(userContext).checkIdOfAccountingDocument( accountingDocumentId);
		checkerOf(userContext).checkIdOfAccountingDocumentLine(accountingDocumentLineId);
		checkerOf(userContext).checkVersionOfAccountingDocumentLine(accountingDocumentLineVersion);

		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);


	}
	public  AccountingDocument copyAccountingDocumentLineFrom(RetailscmUserContext userContext, String accountingDocumentId,
		String accountingDocumentLineId, int accountingDocumentLineVersion,String [] tokensExpr) throws Exception{

		checkParamsForCopyingAccountingDocumentLine(userContext,accountingDocumentId, accountingDocumentLineId, accountingDocumentLineVersion,tokensExpr);

		AccountingDocumentLine accountingDocumentLine = createIndexedAccountingDocumentLine(accountingDocumentLineId, accountingDocumentLineVersion);
		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, allTokens());
		synchronized(accountingDocument){
			//Will be good when the accountingDocument loaded from this JVM process cache.
			//Also good when there is a RAM based DAO implementation

			

			accountingDocument.copyAccountingDocumentLineFrom( accountingDocumentLine );
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withAccountingDocumentLineList().done());
			
			accountingDocumentLineManagerOf(userContext).onNewInstanceCreated(userContext, (AccountingDocumentLine)accountingDocument.getFlexiableObjects().get(BaseEntity.COPIED_CHILD));
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}

	}

	protected void checkParamsForUpdatingAccountingDocumentLine(RetailscmUserContext userContext, String accountingDocumentId, String accountingDocumentLineId, int accountingDocumentLineVersion, String property, String newValueExpr,String [] tokensExpr) throws Exception{
		


		checkerOf(userContext).checkIdOfAccountingDocument(accountingDocumentId);
		checkerOf(userContext).checkIdOfAccountingDocumentLine(accountingDocumentLineId);
		checkerOf(userContext).checkVersionOfAccountingDocumentLine(accountingDocumentLineVersion);


		if(AccountingDocumentLine.NAME_PROPERTY.equals(property)){
			checkerOf(userContext).checkNameOfAccountingDocumentLine(parseString(newValueExpr));
		}
		
		if(AccountingDocumentLine.CODE_PROPERTY.equals(property)){
			checkerOf(userContext).checkCodeOfAccountingDocumentLine(parseString(newValueExpr));
		}
		
		if(AccountingDocumentLine.DIRECT_PROPERTY.equals(property)){
			checkerOf(userContext).checkDirectOfAccountingDocumentLine(parseString(newValueExpr));
		}
		
		if(AccountingDocumentLine.AMOUNT_PROPERTY.equals(property)){
			checkerOf(userContext).checkAmountOfAccountingDocumentLine(parseBigDecimal(newValueExpr));
		}
		


		checkerOf(userContext).throwExceptionIfHasErrors(AccountingDocumentManagerException.class);


	}

	public  AccountingDocument updateAccountingDocumentLine(RetailscmUserContext userContext, String accountingDocumentId, String accountingDocumentLineId, int accountingDocumentLineVersion, String property, String newValueExpr,String [] tokensExpr)
			throws Exception{

		checkParamsForUpdatingAccountingDocumentLine(userContext, accountingDocumentId, accountingDocumentLineId, accountingDocumentLineVersion, property, newValueExpr,  tokensExpr);

		Map<String,Object> loadTokens = this.tokens().withAccountingDocumentLineList().searchAccountingDocumentLineListWith(AccountingDocumentLine.ID_PROPERTY, tokens().equals(), accountingDocumentLineId).done();



		AccountingDocument accountingDocument = loadAccountingDocument(userContext, accountingDocumentId, loadTokens);

		synchronized(accountingDocument){
			//Will be good when the accountingDocument loaded from this JVM process cache.
			//Also good when there is a RAM based DAO implementation
			//accountingDocument.removeAccountingDocumentLine( accountingDocumentLine );
			//make changes to AcceleraterAccount.
			AccountingDocumentLine accountingDocumentLineIdVersionKey = createIndexedAccountingDocumentLine(accountingDocumentLineId, accountingDocumentLineVersion);

			AccountingDocumentLine accountingDocumentLine = accountingDocument.findTheAccountingDocumentLine(accountingDocumentLineIdVersionKey);
			if(accountingDocumentLine == null){
				throw new AccountingDocumentManagerException(accountingDocumentLineId+" is NOT FOUND" );
			}

			beforeUpdateAccountingDocumentLine(userContext, accountingDocumentLine, accountingDocumentId, accountingDocumentLineId, accountingDocumentLineVersion, property, newValueExpr,  tokensExpr);
			accountingDocumentLine.changeProperty(property, newValueExpr);
			
			accountingDocument = saveAccountingDocument(userContext, accountingDocument, tokens().withAccountingDocumentLineList().done());
			accountingDocumentLineManagerOf(userContext).onUpdated(userContext, accountingDocumentLine, this, "updateAccountingDocumentLine");
			return present(userContext,accountingDocument, mergedAllTokens(tokensExpr));
		}

	}

	/** if you has something need to do before update data from DB, override this */
	protected void beforeUpdateAccountingDocumentLine(RetailscmUserContext userContext, AccountingDocumentLine existed, String accountingDocumentId, String accountingDocumentLineId, int accountingDocumentLineVersion, String property, String newValueExpr,String [] tokensExpr)
  			throws Exception{
  }
	/*

	*/




	public void onNewInstanceCreated(RetailscmUserContext userContext, AccountingDocument newCreated) throws Exception{
		ensureRelationInGraph(userContext, newCreated);
		sendCreationEvent(userContext, newCreated);

    
	}

  
  

  public void sendAllItems(RetailscmUserContext ctx) throws Exception{
    accountingDocumentDaoOf(ctx).loadAllAsStream().forEach(
          event -> sendInitEvent(ctx, event)
    );
  }



	// -----------------------------------//  登录部分处理 \\-----------------------------------
	@Override
  protected BusinessHandler getLoginProcessBizHandler(RetailscmUserContextImpl userContext) {
    return this;
  }

	public void onAuthenticationFailed(RetailscmUserContext userContext, LoginContext loginContext,
			LoginResult loginResult, IdentificationHandler idHandler, BusinessHandler bizHandler)
			throws Exception {
		// by default, failed is failed, nothing can do
	}
	// when user authenticated success, but no sec_user related, this maybe a new user login from 3-rd party service.
	public void onAuthenticateNewUserLogged(RetailscmUserContext userContext, LoginContext loginContext,
			LoginResult loginResult, IdentificationHandler idHandler, BusinessHandler bizHandler)
			throws Exception {
		// Generally speaking, when authenticated user logined, we will create a new account for him/her.
		// you need do it like :
		// First, you should create new data such as:
		//   AccountingDocument newAccountingDocument = this.createAccountingDocument(userContext, ...
		// Next, create a sec-user in your business way:
		//   SecUser secUser = secUserManagerOf(userContext).createSecUser(userContext, login, mobile ...
		// And set it into loginContext:
		//   loginContext.getLoginTarget().setSecUser(secUser);
		// Next, create an user-app to connect secUser and newAccountingDocument
		//   UserApp uerApp = userAppManagerOf(userContext).createUserApp(userContext, secUser.getId(), ...
		// Also, set it into loginContext:
		//   loginContext.getLoginTarget().setUserApp(userApp);
		// and in most case, this should be considered as "login success"
		//   loginResult.setSuccess(true);
		//
		// Since many of detailed info were depending business requirement, So,
		throw new Exception("请重载函数onAuthenticateNewUserLogged()以处理新用户登录");
	}
	protected SmartList<UserApp> getRelatedUserAppList(RetailscmUserContext userContext, SecUser secUser) {
    MultipleAccessKey key = new MultipleAccessKey();
    key.put(UserApp.SEC_USER_PROPERTY, secUser.getId());
    key.put(UserApp.APP_TYPE_PROPERTY, AccountingDocument.INTERNAL_TYPE);
    SmartList<UserApp> userApps = userContext.getDAOGroup().getUserAppDAO().findUserAppWithKey(key, EO);
    return userApps;
  }
	// -----------------------------------\\  登录部分处理 //-----------------------------------



	// -----------------------------------// list-of-view 处理 \\-----------------------------------
    protected void enhanceForListOfView(RetailscmUserContext userContext,SmartList<AccountingDocument> list) throws Exception {
    	if (list == null || list.isEmpty()){
    		return;
    	}
		List<AccountingPeriod> accountingPeriodList = RetailscmBaseUtils.collectReferencedObjectWithType(userContext, list, AccountingPeriod.class);
		userContext.getDAOGroup().enhanceList(accountingPeriodList, AccountingPeriod.class);
		List<AccountingDocumentType> documentTypeList = RetailscmBaseUtils.collectReferencedObjectWithType(userContext, list, AccountingDocumentType.class);
		userContext.getDAOGroup().enhanceList(documentTypeList, AccountingDocumentType.class);


    }
	
	public Object listByAccountingPeriod(RetailscmUserContext userContext,String accountingPeriodId) throws Exception {
		return listPageByAccountingPeriod(userContext, accountingPeriodId, 0, 20);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object listPageByAccountingPeriod(RetailscmUserContext userContext,String accountingPeriodId, int start, int count) throws Exception {
		SmartList<AccountingDocument> list = accountingDocumentDaoOf(userContext).findAccountingDocumentByAccountingPeriod(accountingPeriodId, start, count, new HashMap<>());
		enhanceForListOfView(userContext, list);
		RetailscmCommonListOfViewPage page = new RetailscmCommonListOfViewPage();
		page.setClassOfList(AccountingDocument.class);
		page.setContainerObject(AccountingPeriod.withId(accountingPeriodId));
		page.setRequestBeanName(this.getBeanName());
		page.setDataList((SmartList)list);
		page.setPageTitle("会计凭证列表");
		page.setRequestName("listByAccountingPeriod");
		page.setRequestOffset(start);
		page.setRequestLimit(count);
		page.setDisplayMode("auto");
		page.setLinkToUrl(TextUtil.encodeUrl(String.format("%s/listByAccountingPeriod/%s/",  getBeanName(), accountingPeriodId)));

		page.assemblerContent(userContext, "listByAccountingPeriod");
		return page.doRender(userContext);
	}
  
	public Object listByDocumentType(RetailscmUserContext userContext,String documentTypeId) throws Exception {
		return listPageByDocumentType(userContext, documentTypeId, 0, 20);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object listPageByDocumentType(RetailscmUserContext userContext,String documentTypeId, int start, int count) throws Exception {
		SmartList<AccountingDocument> list = accountingDocumentDaoOf(userContext).findAccountingDocumentByDocumentType(documentTypeId, start, count, new HashMap<>());
		enhanceForListOfView(userContext, list);
		RetailscmCommonListOfViewPage page = new RetailscmCommonListOfViewPage();
		page.setClassOfList(AccountingDocument.class);
		page.setContainerObject(AccountingDocumentType.withId(documentTypeId));
		page.setRequestBeanName(this.getBeanName());
		page.setDataList((SmartList)list);
		page.setPageTitle("会计凭证列表");
		page.setRequestName("listByDocumentType");
		page.setRequestOffset(start);
		page.setRequestLimit(count);
		page.setDisplayMode("auto");
		page.setLinkToUrl(TextUtil.encodeUrl(String.format("%s/listByDocumentType/%s/",  getBeanName(), documentTypeId)));

		page.assemblerContent(userContext, "listByDocumentType");
		return page.doRender(userContext);
	}
  
  // -----------------------------------\\ list-of-view 处理 //-----------------------------------v
  
 	/**
	 * miniprogram调用返回固定的detail class
	 *
	 * @return
	 * @throws Exception
	 */
 	public Object wxappview(RetailscmUserContext userContext, String accountingDocumentId) throws Exception{
    SerializeScope vscope = SerializeScope.EXCLUDE().nothing();
		AccountingDocument merchantObj = (AccountingDocument) this.view(userContext, accountingDocumentId);
    String merchantObjId = accountingDocumentId;
    String linkToUrl =	"accountingDocumentManager/wxappview/" + merchantObjId + "/";
    String pageTitle = "会计凭证"+"详情";
		Map result = new HashMap();
		List propList = new ArrayList();
		List sections = new ArrayList();
 
		propList.add(
				MapUtil.put("id", "1-id")
				    .put("fieldName", "id")
				    .put("label", "ID")
				    .put("type", "text")
				    .put("linkToUrl", "")
				    .put("displayMode", "{}")
				    .into_map()
		);
		result.put("id", merchantObj.getId());

		propList.add(
				MapUtil.put("id", "2-name")
				    .put("fieldName", "name")
				    .put("label", "名称")
				    .put("type", "text")
				    .put("linkToUrl", "")
				    .put("displayMode", "{}")
				    .into_map()
		);
		result.put("name", merchantObj.getName());

		propList.add(
				MapUtil.put("id", "3-accountingDocumentDate")
				    .put("fieldName", "accountingDocumentDate")
				    .put("label", "会计凭证日期")
				    .put("type", "date")
				    .put("linkToUrl", "")
				    .put("displayMode", "{}")
				    .into_map()
		);
		result.put("accountingDocumentDate", merchantObj.getAccountingDocumentDate());

		propList.add(
				MapUtil.put("id", "4-accountingPeriod")
				    .put("fieldName", "accountingPeriod")
				    .put("label", "会计期间")
				    .put("type", "auto")
				    .put("linkToUrl", "accountingPeriodManager/wxappview/:id/")
				    .put("displayMode", "{\"brief\":\"start_date\",\"imageUrl\":\"\",\"name\":\"auto\",\"title\":\"name\",\"imageList\":\"\"}")
				    .into_map()
		);
		result.put("accountingPeriod", merchantObj.getAccountingPeriod());

		propList.add(
				MapUtil.put("id", "5-documentType")
				    .put("fieldName", "documentType")
				    .put("label", "文档类型")
				    .put("type", "auto")
				    .put("linkToUrl", "accountingDocumentTypeManager/wxappview/:id/")
				    .put("displayMode", "{\"brief\":\"description\",\"imageUrl\":\"\",\"name\":\"auto\",\"title\":\"name\",\"imageList\":\"\"}")
				    .into_map()
		);
		result.put("documentType", merchantObj.getDocumentType());

		//处理 sectionList

		//处理Section：originalVoucherListSection
		Map originalVoucherListSection = ListofUtils.buildSection(
		    "originalVoucherListSection",
		    "原始凭证列表",
		    null,
		    "",
		    "__no_group",
		    "originalVoucherManager/listByBelongsTo/"+merchantObjId+"/",
		    "auto"
		);
		sections.add(originalVoucherListSection);

		result.put("originalVoucherListSection", ListofUtils.toShortList(merchantObj.getOriginalVoucherList(), "originalVoucher"));

		//处理Section：accountingDocumentLineListSection
		Map accountingDocumentLineListSection = ListofUtils.buildSection(
		    "accountingDocumentLineListSection",
		    "会计文件行表",
		    null,
		    "",
		    "__no_group",
		    "accountingDocumentLineManager/listByBelongsTo/"+merchantObjId+"/",
		    "auto"
		);
		sections.add(accountingDocumentLineListSection);

		result.put("accountingDocumentLineListSection", ListofUtils.toShortList(merchantObj.getAccountingDocumentLineList(), "accountingDocumentLine"));

		result.put("propList", propList);
		result.put("sectionList", sections);
		result.put("pageTitle", pageTitle);
		result.put("linkToUrl", linkToUrl);

		vscope.field("propList", SerializeScope.EXCLUDE())
				.field("sectionList", SerializeScope.EXCLUDE())
				.field("pageTitle", SerializeScope.EXCLUDE())
				.field("linkToUrl", SerializeScope.EXCLUDE());
		userContext.forceResponseXClassHeader("com.terapico.appview.DetailPage");
		return BaseViewPage.serialize(result, vscope);
	}

  










}




