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
