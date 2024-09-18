trigger UpdateCaseActionCount on CaseAction__c (after insert, after update, after delete, after undelete) {

    // Set to hold all Case IDs related to CaseActions
    Set<Id> caseIds = new Set<Id>();

    // Collect all Case IDs from the CaseAction records in Trigger.new/old (depending on context)
    if (Trigger.isInsert || Trigger.isUpdate || Trigger.isUndelete) {
        for (CaseAction__c caseAction : Trigger.new) {
            caseIds.add(caseAction.Case__c);
        }
    }

    if (Trigger.isDelete) {
        for (CaseAction__c caseAction : Trigger.old) {
            caseIds.add(caseAction.Case__c);
        }
    }

    // Query Case records to update the 'Count Using Trigger' field
    List<Case> casesToUpdate = new List<Case>();

    // Query the number of related CaseAction records for each Case
    Map<Id, Integer> caseActionCountMap = new Map<Id, Integer>();
    List<AggregateResult> caseActionCounts = [
        SELECT Case__c, COUNT(Id) count
        FROM CaseAction__c
        WHERE Case__c IN :caseIds
        GROUP BY Case__c
    ];

    // Store the Case Id and related CaseAction count in a map
    for (AggregateResult ar : caseActionCounts) {
        caseActionCountMap.put((Id) ar.get('Case__c'), (Integer) ar.get('count'));
    }

    // For each Case, update the 'Count Using Trigger' field
    for (Id caseId : caseIds) {
        Case caseRecord = new Case(
            Id = caseId,
            Count_Using_Trigger__c = caseActionCountMap.containsKey(caseId) ? caseActionCountMap.get(caseId) : 0
        );
        casesToUpdate.add(caseRecord);
    }

    // Update all the Case records with the new count
    if (!casesToUpdate.isEmpty()) {
        update casesToUpdate;
    }
}






trigger PopulateOwnerOnCaseAction on Case (after insert, after update) {

    // Create a Set to store Case IDs from the Case records in Trigger.new
    Set<Id> caseIds = new Set<Id>();

    // Collect all Case IDs from the trigger
    for (Case caseRecord : Trigger.new) {
        caseIds.add(caseRecord.Id);
    }

    // Query the Owner (User or Queue) for these Case records
    Map<Id, String> caseOwnerMap = new Map<Id, String>();

    // Query the Case owners from User or Queue (Group)
    List<Case> casesWithOwners = [SELECT Id, OwnerId, Owner.Name FROM Case WHERE Id IN :caseIds];
    for (Case caseRecord : casesWithOwners) {
        caseOwnerMap.put(caseRecord.Id, caseRecord.Owner.Name);
    }

    // Query related CaseAction records to the Cases
    List<CaseAction__c> caseActionList = [SELECT Id, Case__c FROM CaseAction__c WHERE Case__c IN :caseIds];

    // Update CaseAction records with the Owner name from the related Case
    for (CaseAction__c caseAction : caseActionList) {
        if (caseOwnerMap.containsKey(caseAction.Case__c)) {
            caseAction.Owner__c = caseOwnerMap.get(caseAction.Case__c);
        }
    }

    // Perform DML operation to update CaseAction records
    if (!caseActionList.isEmpty()) {
        update caseActionList;
    }
}








trigger PopulateActNotesOnOpportunityCreation on Opportunity (before insert) {

    // Get the record type Id for the 'Rollover' record type
    RecordType rolloverRecordType = [SELECT Id FROM RecordType WHERE SObjectType = 'Opportunity' AND Name = 'Rollover' LIMIT 1];
    
    // Iterate through the list of new Opportunities being created
    for (Opportunity opp : Trigger.new) {
        
        // Check if the record type is 'Rollover'
        if (opp.RecordTypeId == rolloverRecordType.Id) {
            
            // Populate the 'ACT Notes' field with the specified text
            opp.ACT_Notes__c = 'Test Notes By Thanush';
        }
    }
}





trigger PreventCaseDeletion on Case (before delete) {

    // List to store case IDs from the trigger context
    Set<Id> caseIds = new Set<Id>();

    // Gather all Case IDs from the records being deleted
    for (Case caseRecord : Trigger.old) {
        caseIds.add(caseRecord.Id);
    }

    // Query for any Case Action records that are related to the cases being deleted
    List<Case_Action__c> relatedCaseActions = [
        SELECT Id FROM Case_Action__c WHERE Case__c IN :caseIds
    ];

    // If there are any related Case Action records, prevent deletion
    if (relatedCaseActions.size() > 0) {
        for (Case caseRecord : Trigger.old) {
            caseRecord.addError('You cannot delete this Case because it has related Case Action records.');
        }
    }
}






trigger PreventPlanIdChange on Case_Action__c (before update) {
    // Get the Profile ID for System Administrator
    Id sysAdminProfileId = [SELECT Id FROM Profile WHERE Name = 'System Administrator' LIMIT 1].Id;

    // Iterate through the Case Action records being updated
    for (Case_Action__c caseAction : Trigger.new) {
        // Get the old version of the record to compare field values
        Case_Action__c oldCaseAction = Trigger.oldMap.get(caseAction.Id);

        // Check if the Plan Id is being changed and if the user's profile is not System Administrator
        if (caseAction.Plan_Id__c != oldCaseAction.Plan_Id__c && 
            UserInfo.getProfileId() != sysAdminProfileId) {
            // Prevent the update and show an error message
            caseAction.addError('You do not have permission to change the Plan Id.');
        }
    }
}







@isTest
static void ProtfolioTestOne(){
    Test.startTest();
    
    // Query for Account record
    List<Account> RAcct = [SELECT Id, Name, SSN__c, is_Entity__c, FirstName, LastName, Middle_Name__pc FROM Account LIMIT 1];

    // Check if the Account list is empty
    if (RAcct.isEmpty()) {
        System.assert(false, 'No Account records found.'); // Stop the test if no accounts found
    } else {
        System.debug('Account retrieved: ' + RAcct[0]); // Debug statement to confirm account retrieval
    }

    // Query for OFAC_Log__c record
    OFAC_Log__c prRecord1 = [SELECT Id, Name, WorkItem_Id__c, SSN__c, Format_Type__c, CIP_Status__c,
                             First_Name__c, Last_Name__c, Full_Name__c, Middle_Name__c, Channel__c, Process__c, 
                             Override_Reason_Notes__c, Document_ID__c FROM OFAC_Log__c WHERE Type__c = 'CIP' LIMIT 1];

    // Check if OFAC_Log__c record is null
    if (prRecord1 == null) {
        System.assert(false, 'No OFAC_Log__c records found.'); // Stop the test if no OFAC_Log__c record found
    } else {
        System.debug('OFAC_Log__c retrieved: ' + prRecord1); // Debug statement to confirm OFAC_Log__c retrieval
        prRecord1.Format_Type__c = 'I';
        update prRecord1; // Update record if not null
    }

    // Query for LexisNexisWorkitemID__c record
    LexisNexisWorkitemID__c prRecord = [SELECT Id, Name, Workitem_ID__c, Document_ID__c FROM LexisNexisWorkitemID__c LIMIT 1];

    // Check if LexisNexisWorkitemID__c record is null
    if (prRecord == null) {
        System.assert(false, 'No LexisNexisWorkitemID__c records found.'); // Stop the test if no LexisNexisWorkitemID__c record found
    } else {
        System.debug('LexisNexisWorkitemID__c retrieved: ' + prRecord); // Debug statement to confirm LexisNexisWorkitemID__c retrieval
        prRecord.WorkItem_Id__c = '';
        update prRecord; // Update record if not null
    }

    // Call future method to simulate future call
    LexisNexisCIPReporttoDocupace.futureCallUpload();

    // Instantiate LexisNexisCIPReporttoDocupace class
    LexisNexisCIPReporttoDocupace pr = new LexisNexisCIPReporttoDocupace();

    // Check if the Account record is null before processing
    if (!RAcct.isEmpty()) {
        String workitemXML = pr.processWorkItemRequest(RAcct[0]).xmlResponse; // Process work item if account exists
        System.debug('workitemXML response: ' + workitemXML); // Debug statement to confirm XML response

        String jsonResponse = EBlotterDocupaceDataServiceHelper.createDocumentRecord('PR_DOC_REC', '', '', UserInfo.getSessionId(), '');
        System.debug('JSON response for Document Record: ' + jsonResponse); // Debug statement to confirm JSON response

        String jsonResponse1 = EBlotterDocupaceDataServiceHelper.createWorkitemRecord('PR_DP_DOCS_WI', '', workitemXML, '', '999988812', UserInfo.getSessionId(), '');
        System.debug('JSON response for Workitem Record: ' + jsonResponse1); // Debug statement to confirm JSON response

        pr.uploadDoucmentstoDocupace(prRecord, null); // Upload documents if records exist
    } else {
        System.assert(false, 'No valid Account records to process.'); // Stop the test if no valid accounts
    }

    Test.stopTest();
}






@isTest
static void ProtfolioTestOne(){
    Test.startTest();

    // Query the Account record
    List<Account> RAcct = [SELECT Id, Name, SSN__c, is_Entity__c, FirstName, LastName, Middle_Name__pc FROM Account LIMIT 1];

    if (RAcct.isEmpty()) {
        // Handle the case where no Account records were found
        System.assert(false, 'No Account records found.');
    } else {
        // Query the OFAC_Log__c record
        OFAC_Log__c prRecord1 = [SELECT Id, Name, WorkItem_Id__c, SSN__c, Format_Type__c, CIP_Status__c,
                                 First_Name__c, Last_Name__c, Full_Name__c, Middle_Name__c, Channel__c, Process__c,
                                 Override_Reason_Notes__c, Document_ID__c FROM OFAC_Log__c WHERE Type__c='CIP' LIMIT 1];

        if (prRecord1 == null) {
            // Handle the case where no OFAC_Log__c records were found
            System.assert(false, 'No OFAC_Log__c records found.');
        } else {
            prRecord1.Format_Type__c = 'I';
            update prRecord1;
        }

        // Query the LexisNexisWorkitemID__c record
        LexisNexisWorkitemID__c prRecord = [SELECT Id, Name, Workitem_ID__c, Document_ID__c FROM LexisNexisWorkitemID__c LIMIT 1];

        if (prRecord == null) {
            // Handle the case where no LexisNexisWorkitemID__c records were found
            System.assert(false, 'No LexisNexisWorkitemID__c records found.');
        } else {
            prRecord.WorkItem_Id__c = '';
            update prRecord;
        }

        // Call future method
        LexisNexisCIPReporttoDocupace.futureCallUpload();
        LexisNexisCIPReporttoDocupace pr = new LexisNexisCIPReporttoDocupace();

        // Call method only if RAcct contains records
        String workitemXML = pr.processWorkItemRequest(RAcct[0]).xmlResponse;

        String jsonResponse = EBlotterDocupaceDataServiceHelper.createDocumentRecord('PR_DOC_REC', '', '', UserInfo.getSessionId(), '');
        String jsonResponse1 = EBlotterDocupaceDataServiceHelper.createWorkitemRecord('PR_DP_DOCS_WI', '', workitemXML, '', '999988812', UserInfo.getSessionId(), '');

        pr.uploadDoucmentstoDocupace(prRecord, null);
    }

    Test.stopTest();
}@isTest
    static void ProtfolioTestOne(){
        Test.startTest();
        
        
         list<Account> RAcct = [SELECT Id, Name, SSN__c, is_Entity__c,FirstName, LastName, Middle_Name__pc FROM Account limit 1];
       

        OFAC_Log__c prRecord1 =[SELECT Id, Name, WorkItem_Id__c, SSN__c, Format_Type__c,CIP_Status__c,
                                First_Name__c, Last_Name__c, Full_Name__c, Middle_Name__c,Channel__c,Process__c,Override_Reason_Notes__c,
                                Document_ID__c FROM OFAC_Log__c where Type__c='CIP' limit 1];
        prRecord1.Format_Type__c='I';
        update prRecord1;
        
        LexisNexisWorkitemID__c prRecord =[select id, name,Workitem_ID__c,Document_ID__c from LexisNexisWorkitemID__c Limit 1 ];
        List<LexisNexisWorkitemID__c> portList = LexisNexisWorkitemID__c.getAll().Values();
         prRecord.WorkItem_Id__c = '';
        update prRecord;
        
        
       
	 LexisNexisCIPReporttoDocupace.futureCallUpload();
        LexisNexisCIPReporttoDocupace pr = new LexisNexisCIPReporttoDocupace();
        
        String workitemXML = pr.processWorkItemRequest(RAcct[0]).xmlResponse;
        
         String jsonResponse = EBlotterDocupaceDataServiceHelper.createDocumentRecord('PR_DOC_REC', '', '', UserInfo.getSessionId(), '');
        String jsonResponse1 = EBlotterDocupaceDataServiceHelper.createWorkitemRecord('PR_DP_DOCS_WI','',workitemXML,'','999988812',UserInfo.getSessionId(),'');
                
        pr.uploadDoucmentstoDocupace(prRecord,null);
        
        
        Test.stopTest();
    }
System.NullPointerException: Attempt to de-reference a null object




@isTest(SeeAllData=false)
private class testClassRegistrationAccessRemoval {

    private static testMethod void testRegistrationAccessRemoval_lastNdays() {
        // create test data
        // Get Sys Admin profile record
        Profile userProfileSysAdmin = TestUtils.getProfile('System Administrator');

        // Get BD IFA Rep profile record
        Profile userProfileBDIFA = TestUtils.getProfile('BD Ret Rep');

        // Get User Role
        UserRole userRole = TestUtils.getUserRole('Sales Director - IST');

        // Create test user record
        // Create a test BD IFA field rep user 
        User bdIfaRep = TestUtils.createUser('100200300', 'testuser2@sw.com', 'testUser2@voya.com', userProfileBDIFA.Id, userRole.Id, false, false);
        bdIfaRep.CommunityNickname = 'test22';

        // Create a sys admin test user record
        User sysAdmin = TestUtils.createUser('020202023', 'testuser1@sw.com', 'testUser1@voya.com', userProfileSysAdmin.Id, userRole.Id, false, false);
        sysAdmin.CommunityNickname = 'test2';

        List<User> users = new List<User>{bdIfaRep, sysAdmin};
        insert users;

        // Get Client record type id
        RecordType clientRecordType = TestUtils.getAccountRecordType('Client', true);

        Test.startTest();

        System.runAs(sysAdmin) {
            // Create rep number and rep number user records
            // Create Rep Number
            Rep_Number__c repNumber = TestUtilsSBR.createRepNum('testRepN1', '123');

            // Create Rep Number User records
            Rep_Number_User__c repNumberUser = TestUtilsSBR.createRepNumUser(repNumber.Id, bdIfaRep.Id, bdIfaRep.Rep_ID__c);

            // Create a Client record
            Account client = TestUtils.createPersonAccount(clientRecordType.Id, true);

            // Add sys admin user in Account team
            AccountTeamMember atm1 = new AccountTeamMember(AccountId = client.Id, UserId = sysAdmin.Id, TeamMemberRole = 'Sales Rep');
            insert atm1;

            // Create Financial Account records for this client and create Financial Account share records
            // Create Financial Account test data
            Financial_Account__c fAccount = new Financial_Account__c(Name = 'test FA', Status__c = 'Active', Client__c = client.Id,
                                                                      Batch_Id__c = '123', Hybrid_RIA__c = true);
            insert fAccount;

            // Create Financial Account team members
            Financial_Account_Team__c fat = new Financial_Account_Team__c(Name = 'testFAT2', Financial_Account__c = fAccount.Id,
                                                                          FATeam_External_Id__c = '124', Batch_Id__c = '124',
                                                                          User__c = bdIfaRep.Id, Role__c = 'Primary');
            insert fat;

            // Insert financial account share records                                                                                                                                
            Financial_Account__Share fShare1 = new Financial_Account__Share(
                ParentId = fAccount.Id, UserOrGroupId = bdIfaRep.Id, AccessLevel = 'Edit',
                RowCause = Schema.Financial_Account__Share.RowCause.Data_Load__c);
            insert fShare1;

            // Create registration record
            Registration__c registration = TestUtilsSBR.createRegistration(repNumber.Id, client, 'Traditional IRA');
            insert registration;

            // Create registration client members record
            Registration_Client_Members__c rcm = TestUtilsSBR.createRegClientMember(
                registration.Id, client, 1, 'Primary Account Holder', false);
            insert rcm;

            // Call the scheduler for RegistrationAccessRemoval batch job
            Datetime futureDate = System.now().addMinutes(2); // Schedule to run 2 minutes from now
            String cronExp = futureDate.format('s m H d M \'?\' yyyy'); // Format the datetime to a valid cron expression

            String jobId = System.schedule('testRegistrationAccessRemoval', cronExp, new RegistrationAccessRemoval(false, 0));

            // Test access removal batch job
            RegistrationAccessRemoval regAccessRemoval = new RegistrationAccessRemoval(true, 1);
            ID regAccessRemovalBatchID = Database.executeBatch(regAccessRemoval, RegistrationAccessRemoval.BATCH_SIZE_DEFAULT);
            RegistrationAccessRemoval reg1 = new RegistrationAccessRemoval();

        }
        Test.stopTest();
    }
}







/**
 * This class provides test code coverage for RegistrationAccessRemoval batch process
 *
 * @author: Deshraj Kumawat
 * @date: 05/23/2013
 * @version: 1.0
 */
@isTest(SeeAllData=false)
private class testClassRegistrationAccessRemoval {
    
    public static String CRON_EXP = '0 0 0 3 9 ? 2022';
    
    /**
     * This test method is to test registration access removal batch 
     * process for Registration Client members
     */
    private static testMethod void testRegistrationAccessRemoval_lastNdays() {
        //create test data
        //Get Sys Admin profile record
        Profile userProfileSysAdmin = TestUtils.getProfile('System Administrator');
        
        //Get BD IFA Rep profile record
        Profile userProfileBDIFA = TestUtils.getProfile('BD Ret Rep');
        
        //Get User Role
        UserRole userRole = TestUtils.getUserRole('Sales Director - IST');
        
        //Create test user record
        //Create a test BD IFA field rep user 
        User bdIfaRep = TestUtils.createUser('100200300', 'testuser2@sw.com', 'testUser2@voya.com', userProfileBDIFA.Id, userRole.Id, false, false);
        bdIfaRep.CommunityNickname = 'test22';
        
        //Create a sys admin test user record
        User sysAdmin = TestUtils.createUser('020202023', 'testuser1@sw.com', 'testUser1@voya.com', userProfileSysAdmin.Id, userRole.Id, false, false);
        sysAdmin.CommunityNickname = 'test2';
        
        List<User> users = new List<User>{bdIfaRep, sysAdmin};
        insert users;
        
        //Get Client record type id
        RecordType clientRecordType = TestUtils.getAccountRecordType('Client', true);
        
        Test.startTest();
            
            system.runAs(sysAdmin) {
                //Create rep number and rep number user records
                //Create Rep Number
                Rep_Number__c repNumber = TestUtilsSBR.createRepNum('testRepN1', '123');
                
                //Create Rep Number User records
                Rep_Number_User__c repNumberUser = TestUtilsSBR.createRepNumUser(repNumber.Id, bdIfaRep.Id, bdIfaRep.Rep_ID__c);
        
                //Create a Client record
                Account client = TestUtils.createPersonAccount(clientRecordType.Id, true);
                
                //Add sys admin user in Account team
                AccountTeamMember atm1 = new AccountTeamMember(AccountId = client.Id, UserId = sysAdmin.Id, TeamMemberRole = 'Sales Rep');
                insert atm1;
                
                //Create Financial Account records for this client and create Financial Account share records
                //Create Financial Account test data
                Financial_Account__c fAccount = new Financial_Account__c(Name = 'test FA', Status__c = 'Active', Client__c = client.Id,
                                                                    Batch_Id__c = '123',Hybrid_RIA__c=true);
                insert fAccount;
                
                //Create Financial Account team members
                Financial_Account_Team__c fat = new Financial_Account_Team__c(Name='testFAT2', Financial_Account__c = fAccount.Id,
                                                                              FATeam_External_Id__c ='124',Batch_Id__c = '124',
                                                                              User__c = bdIfaRep.Id, Role__c = 'Primary');
                insert fat;
                
                //Insert financial account share records                                                                                                                                
                Financial_Account__Share fShare1 = new Financial_Account__Share(
                                                                ParentId = fAccount.Id, UserOrGroupId = bdIfaRep.Id, AccessLevel = 'Edit', 
                                                                RowCause = Schema.Financial_Account__Share.RowCause.Data_Load__c);
                insert fShare1;
                
                
                //Create registration record
                Registration__c registration = TestUtilsSBR.createRegistration(repNumber.Id, client, 'Traditional IRA');
                insert registration;
                
                //Create registration client members record
                Registration_Client_Members__c rcm = TestUtilsSBR.createRegClientMember(
                                                        registration.Id, client, 1, 'Primary Account Holder', false);
                insert rcm;
                
                //call the schedular for RegistrationAccessRemoval batch job
                String jobId = system.schedule('testRegistrationAccessRemoval', CRON_EXP, new RegistrationAccessRemoval(false, 0));
                
                //Test access removal batch job
                RegistrationAccessRemoval regAccessRemoval = new RegistrationAccessRemoval(true, 1);
                ID regAccessRemovalBatchID = Database.executeBatch(regAccessRemoval,RegistrationAccessRemoval.BATCH_SIZE_DEFAULT);
           RegistrationAccessRemoval reg1=new RegistrationAccessRemoval();
            
            }
        Test.stopTest();
    }
}
System.AsyncException: Based on configured schedule, the given trigger 'SCHEDULED_APEX_JOB_TYPE.000000000000000' will never fire.






public static testMethod void validateFinancialPlanStatusUpdateBatch1() {
    RecordType[] rtList1 = [SELECT Id, IsPersonType, Name, SobjectType FROM RecordType WHERE SobjectType = 'Financial_Plan__c' AND Name = 'Moneyguide'];
    RecordType[] rtList2 = [SELECT Id, IsPersonType, Name, SobjectType FROM RecordType WHERE SobjectType = 'Financial_Plan__c' AND Name = 'Manual Upload'];
    RecordType[] rtAccList = [SELECT Id, IsPersonType, Name, SobjectType FROM RecordType WHERE SobjectType = 'Account' AND Name = 'client' AND IsPersonType = true];

    Account accObj = new Account();
    accObj.ssn__c = '123456789';
    accObj.FirstName = 'TestFN';
    accObj.LastName = 'TestLn';
    accObj.recordtypeid = rtAccList[0].id;
    insert accObj;

    Plan__c planObj = new Plan__c(Name = 'abccc', Market_ist__c = 'External');
    insert planObj;

    Opportunity oppObj = new Opportunity(Name = 'abc', Plan__c = planObj.id, CloseDate = System.TODAY(), at_Risk__c = 2500.0, StageName = 'Retained', LeadSource = 'PFD', accountid = accObj.id);
    insert oppObj;

    Financial_Plan__c fpObj1 = new Financial_Plan__c();
    fpObj1.Status__c = 'Active';
    fpObj1.MGP_Plan_Updated__c = System.today().addMonths(-37);
    fpObj1.RecordTypeId = rtList1[0].id;
    fpObj1.BD_Financial_Agreement__c = oppObj.id;

    Financial_Plan__c fpObj2 = new Financial_Plan__c();
    fpObj2.Status__c = 'Active';
    fpObj2.MGP_Plan_Updated__c = System.today().addMonths(-37);
    fpObj2.RecordTypeId = rtList2[0].id;
    fpObj2.BD_Financial_Agreement__c = oppObj.id;

    insert new List<Financial_Plan__c>{fpObj1, fpObj2};

    FinancialPlanStatusUpdateBatch batchable = new FinancialPlanStatusUpdateBatch('');
    Database.executeBatch(batchable, 200);

    // Set up a schedule in the future
    FinancialPlanStatusUpdateBatch batchable1 = new FinancialPlanStatusUpdateBatch('');
    Datetime futureDate = System.now().addMinutes(2); // Schedule to run 2 minutes from now

    String sch = futureDate.format('s m H d M \'?\' yyyy'); // Format the datetime to a valid cron expression

    String jobID = System.schedule('FinancialPlanStatusUpdateBatchJob', sch, batchable1);
    
    // Validate that the scheduled job was created
    Test.stopTest();
}












public static testMethod void validateFinancialPlanStatusUpdateBatch1()
    {
        RecordType[] rtList1 =[Select Id, IsPersonType, Name, SobjectType from RecordType where SobjectType ='Financial_Plan__c' and Name ='Moneyguide'];
        RecordType[] rtList2 =[Select Id, IsPersonType, Name, SobjectType from RecordType where SobjectType ='Financial_Plan__c' and Name ='Manual Upload'];
        RecordType[] rtAccList =[Select Id, IsPersonType, Name, SobjectType from RecordType where  SobjectType ='Account' and Name ='client' and IsPersonType = true];
        
        Account accObj = new Account();
		accObj.ssn__c='123456789';
		accObj.FirstName= 'TestFN';
		accObj.LastName='TestLn';
		accObj.recordtypeid=rtAccList[0].id;
		insert accObj;
        
        Plan__c planObj = new Plan__c(Name='abccc',Market_ist__c='External');
        insert planObj;
        
        Opportunity oppObj = new Opportunity(Name='abc' , Plan__c=planObj.id ,CloseDate=System.TODAY(), at_Risk__c=2500.0 ,StageName='Retained' , LeadSource='PFD', accountid=accObj.id );
        insert oppObj;
        
        Financial_Plan__c fpObj1 = new Financial_Plan__c();
        fpObj1.Status__c = 'Active';
        fpObj1.MGP_Plan_Updated__c = System.today().addMonths(-37);
        fpObj1.RecordTypeId=rtList1[0].id;
        fpObj1.BD_Financial_Agreement__c = oppObj.id;
        
        Financial_Plan__c fpObj2 = new Financial_Plan__c();
        fpObj2.Status__c = 'Active';
        fpObj2.MGP_Plan_Updated__c = System.today().addMonths(-37);
        fpObj2.RecordTypeId=rtList2[0].id;
        fpObj2.BD_Financial_Agreement__c = oppObj.id;
        
        insert new List<Financial_Plan__c>{fpObj1, fpObj2};
        
        FinancialPlanStatusUpdateBatch batchable = new FinancialPlanStatusUpdateBatch('');
        Database.executeBatch(batchable, 200);
        
        FinancialPlanStatusUpdateBatch batchable1 = new FinancialPlanStatusUpdateBatch('');
        String sch = '0 0 0 3 9 ? 2022';
        
		String jobID = system.schedule('FinancialPlanStatusUpdateBatchJob', sch, batchable1);
        
        
    }

System.AsyncException: Based on configured schedule, the given trigger 'SCHEDULED_APEX_JOB_TYPE.000000000000000' will never fire.

System.LimitException: Too many SOQL queries: 101
  static testMethod void RRRecordInteractionControllerTest3() {
        
        //Get profile record        
        Profile p = [Select id, name from Profile where name ='System Administrator'];
        
        UserRole r=TestUtils.getUserRole('Executive Management');
        //Creating User
        
        
        
        User testUser1 = new User(LastName ='test', FirstName ='test',emailencodingkey='UTF-8',UserRoleId=r.id,EmployeeNumber='123456',
                                  profileid = p.Id, Email = 'testtest@voya.com', CompanyName='testComp', Alias='test',
                                  languagelocalekey='en_US',timezonesidkey='America/Los_Angeles',localesidkey='en_US',
                                  username='testdataatvoyaSW@testvoya.com',CommunityNickname='tesdt',Street='Test',city='test');
        
        
        
        
        RecordType[] rt =[Select Id, IsPersonType, Name, SobjectType from RecordType 
                          where  SobjectType ='Account' 
                          and Name ='client' 
                          and IsPersonType = true];
        
        
        Account acc = new Account();
        acc.ssn__c='123456789';
        acc.FirstName= 'TestAccount';
        acc.LastName='TestLast';
        acc.recordtypeid=rt[0].id;
        insert acc;
        
        Account acc12 = new Account();
        acc12.ssn__c='123456777';
        acc12.FirstName= 'TestAccount';
        acc12.LastName='TestLast';
        acc12.recordtypeid=rt[0].id;
        insert acc12;
        
        campaign campaign = new Campaign();
        campaign.StartDate = Date.Today(); //Match on Execution Date
        campaign.Type = 'BMZ Campaign';
        campaign.Status = 'In Progress';
        campaign.IsActive = true;
        campaign.Name = 'Test Campaign';
        campaign.External_ID__c = '123456789';
        insert campaign;
        
        String OppRecTypeId= [select Id from RecordType where (Name='Financial Planning') and (SobjectType='Opportunity')].Id;
        Opportunity opprec = new Opportunity(RecordTypeID=OppRecTypeId,Name = 'Newopp',AccountId = acc.Id,StageName = 'Member',CloseDate = Date.newInstance(2015 , 05 ,20));
        Insert opprec;
        
        Offer_Pop__c o= new Offer_Pop__c();
        o.Client__c=acc12.id;
        o.OfferPop_Transaction_ID__c='123456';
        o.User__c= testUser1.id;
        o.Top_Offer__c =campaign.id;
        o.Source__c='CTI';
        o.Opportunity__c=opprec.id;
        o.Offers_Available__c='test';
        o.Lead_Source__c='test';
        o.Client__c =acc.id;
        o.Client_ID__c ='123456';
        o.CTI_DNIS_Number__c= 'DNIS';
        o.Action__c='test';
        insert o;
        
        Plan__c plan = new Plan__c();
        plan.name='666182';
        Plan.Native_PLan_ID__c='666182';
        plan.PAAG_Configuration__c = 'TEST';
        insert plan;
        
        PAAG__c paag = new PAAG__c();
        paag.plan__c = plan.id;
        paag.Market_Filter_Value__c='';
        insert paag;
        
        String taskid= RRControllerHelper.recordTypeMap.get('Client Interaction-Task');
        
        Test.startTest();
        
        ApexPages.currentPage().getParameters().put('requestFrom','clientHeader');
        ApexPages.currentPage().getParameters().put('oppRecType','Planning');
        ApexPages.currentPage().getParameters().put('profileName','RR Rep');
        ApexPages.currentPage().getParameters().put('offerPopId',o.id);
        ApexPages.currentPage().getParameters().put('campaignLeadSource',campaign.id);
        ApexPages.currentPage().getParameters().put('accountId',acc12.id);
        ApexPages.currentPage().getParameters().put('whatId',acc12.id);
        ApexPages.currentPage().getParameters().put('planId',plan.id);
        ApexPages.currentPage().getParameters().put('recTypeAndStage','Appointment');
        ApexPages.currentPage().getParameters().put('recordTypeId',taskid);
        
        RRRecordInteractionController rrcI = new RRRecordInteractionController();
        rrcI.isFutureActivityOnly=true;
        
        rrcI.isActivityCreated=true;
        //rrcI.isInteractionCreate=true;
        //rrcI.isFollowUpTaskCreate=true;
        rrcI.personEmail='xyz@voya.com';
        rrcI.saveAndReloadFutureActivityRecords();
        
        ApexPages.currentPage().getParameters().put('requestFrom','oppSumm');
        ApexPages.currentPage().getParameters().put('profileName','Home Office Representative_IST');
        ApexPages.currentPage().getParameters().put('oppRecType','Planning');
        ApexPages.currentPage().getParameters().put('offerPopId',o.id);
        ApexPages.currentPage().getParameters().put('campaignLeadSource',campaign.id);
        ApexPages.currentPage().getParameters().put('accountId',acc.id);
        ApexPages.currentPage().getParameters().put('whatId',acc.id);
        ApexPages.currentPage().getParameters().put('planId',plan.id);
        ApexPages.currentPage().getParameters().put('recTypeAndStage','Appointment');
        ApexPages.currentPage().getParameters().put('recordTypeId',taskid);
        
        RRRecordInteractionController rrcI2 = new RRRecordInteractionController();
        rrcI2.isFutureActivityOnly=false;
        rrcI2.personEmail='xyz@voya.com';
        rrcI2.emailTemplate='official';
        rrcI2.selectedEmailTemplate='official';
        rrcI2.meetingEvent = new Event(RecordTypeId = RRControllerHelper.recordTypeMap.get('Appointment-Event'), OpportunityRecordTypeAndStage__c = 'Client Interaction');
        rrcI2.saveAndReloadFutureActivityRecords();
        //rrcI2.isEmailSent = true;
        rrcI2.sendEmail();
        
        ApexPages.currentPage().getParameters().put('requestFrom','oppSumm');
        ApexPages.currentPage().getParameters().put('profileName','RR Rep');
        ApexPages.currentPage().getParameters().put('oppRecType','Planning');
        ApexPages.currentPage().getParameters().put('offerPopId',o.id);
        ApexPages.currentPage().getParameters().put('campaignLeadSource',campaign.id);
        ApexPages.currentPage().getParameters().put('accountId',acc.id);
        ApexPages.currentPage().getParameters().put('whatId',acc.id);
        ApexPages.currentPage().getParameters().put('planId',plan.id);
        ApexPages.currentPage().getParameters().put('recTypeAndStage','Appointment');
        ApexPages.currentPage().getParameters().put('recordTypeId',taskid);
        
        RRRecordInteractionController rrcI8 = new RRRecordInteractionController();
        rrcI8.initializePage(false);
        rrcI8.isFutureActivityOnly=false;
                
        rrcI8.saveAndReloadFutureActivityRecords();
        
        
        ApexPages.currentPage().getParameters().put('requestFrom','ras');
        ApexPages.currentPage().getParameters().put('profileName','RR Ret Rep');
        ApexPages.currentPage().getParameters().put('oppRecType','Planning');
        ApexPages.currentPage().getParameters().put('offerPopId',o.id);
        ApexPages.currentPage().getParameters().put('campaignLeadSource',campaign.id);
        ApexPages.currentPage().getParameters().put('accountId',acc12.id);
        ApexPages.currentPage().getParameters().put('whatId',acc12.id);
        ApexPages.currentPage().getParameters().put('planId',plan.id);
        ApexPages.currentPage().getParameters().put('recTypeAndStage','Appointment');
        ApexPages.currentPage().getParameters().put('recordTypeId',taskid);
        ApexPages.currentPage().getParameters().put('atRisk','abc');
        ApexPages.currentPage().getParameters().put('source','xyz');
        RRRecordInteractionController rrcI1 = new RRRecordInteractionController();
        rrcI1.initializePage(false);
        rrcI1.isFutureActivityOnly=false;
        rrcI1.saveAndReloadFutureActivityRecords();
        
        ApexPages.currentPage().getParameters().put('requestFrom','oppSumm');
        ApexPages.currentPage().getParameters().put('profileName','RR Ret Rep');
        ApexPages.currentPage().getParameters().put('oppRecType','Planning');
        ApexPages.currentPage().getParameters().put('offerPopId',o.id);
        ApexPages.currentPage().getParameters().put('campaignLeadSource',campaign.id);
        ApexPages.currentPage().getParameters().put('accountId',acc12.id);
        ApexPages.currentPage().getParameters().put('whatId',acc12.id);
        ApexPages.currentPage().getParameters().put('planId',plan.id);
        ApexPages.currentPage().getParameters().put('recTypeAndStage','Appointment');
        ApexPages.currentPage().getParameters().put('recordTypeId',taskid);
        ApexPages.currentPage().getParameters().put('atRisk','abc');
        ApexPages.currentPage().getParameters().put('source','xyz');
        RRRecordInteractionController rrcI4 = new RRRecordInteractionController();
        rrcI4.initializePage(false);
        rrcI4.isFutureActivityOnly=false;
        rrcI4.saveAndReloadFutureActivityRecords();
        
        ApexPages.currentPage().getParameters().put('requestFrom','button');
        ApexPages.currentPage().getParameters().put('profileName','RR Ret Rep');
        ApexPages.currentPage().getParameters().put('oppRecType','Planning');
        ApexPages.currentPage().getParameters().put('offerPopId',o.id);
        ApexPages.currentPage().getParameters().put('campaignLeadSource',campaign.id);
        ApexPages.currentPage().getParameters().put('accountId',acc12.id);
        ApexPages.currentPage().getParameters().put('whatId',acc12.id);
        ApexPages.currentPage().getParameters().put('planId',Plan.id);
        ApexPages.currentPage().getParameters().put('recTypeAndStage','Appointment');
        ApexPages.currentPage().getParameters().put('recordTypeId',taskid);
        ApexPages.currentPage().getParameters().put('atRisk','abc');
        ApexPages.currentPage().getParameters().put('source','xyz');
        
        RRRecordInteractionController rrcI5 = new RRRecordInteractionController();
        
        rrcI5.meetingPhoneSelected='1234567890';
        rrcI5.isFocusEventTab=true;
        rrcI5.initializePage(false);
        rrcI5.isFutureActivityOnly=false;
        rrcI5.saveActivityRecords();
        rrcI5.saveAndReloadFutureActivityRecords();        
        
        rrcI5.selectedEmailTemplate='test';
        rrcI5.isError = false;
        rrcI5.isEventCreate = true;
        rrcI5.saveAndCreateEMail();
        rrcI5.selectedEmailTemplate = 'TEMPLATE_ALL_PURPOSE_PHONE';
        rrcI5.loadTemplate();
        
        Datetime startDate = system.now();
        Datetime endDate = system.now();
        rrcI5.selectedEmailTemplate = 'Specific to Snapshot (In-Person Appointment)';
        rrcI5.sendMailWithAttachment(startDate, endDate, 'CT', 'HelloWorld', 'abc@mail.com', 'TestPerson', 'TestPerson', 'abc2@mail.com', 'ccAddress@mail.com');
        
        Event evt = new Event();
        evt.StartDateTime = startDate;
        evt.EndDateTime = endDate;
        insert evt;
        rrcI5.personEmail='st@gmail.com';
        rrcI5.emailTemplate='abc';
        rrcI5.schEvent = evt;
        try{
        rrcI5.sendEmail();
        }catch(Exception e)
        {
            system.debug(e.getLineNumber()+'--Exception e--'+e.getMessage());
        }
        rrcI5.cancelSendEmail(); 
        rrcI5.closeSendEmailPopup();  
        rrcI5.closeMCEmailMessage();
        
        
        Task interactionTask = new Task();
        interactionTask.Subject = 'Test Task';
        interactionTask.Status = 'Not Started';
        interactionTask.Activity_Type__c = 'Email';
        insert interactionTask;
        
        rrcI5.Taskcreation();
        rrcI5.SendMCEmail();
       
        
        
         
        
        Test.stopTest();            
    } 
