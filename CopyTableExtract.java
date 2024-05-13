@isTest
private class TestGetPaagAlerts {
    
    @isTest
    static void testGetPaagAlerts() {
        // Test data setup
        String planId = 'TestPlanId';
        String clientId = 'TestClientId';
        
        // Create test data for PAAG_Alerts__c
        List<PAAG_Alerts__c> paagAlerts = new List<PAAG_Alerts__c>();
        // Add test PAAG_Alerts__c records to paagAlerts list
        
        // Insert test PAAG_Alerts__c records
        insert paagAlerts;
        
        // Call the method
        List<PaagAlertWrapper> result = YourClassName.getPaagAlerts(planId, clientId);
        
        // Assert that the result is not null
        System.assertNotEquals(null, result, 'Result should not be null.');
        
        // Assert other conditions based on your logic and test data
        // For example, check if the returned list contains expected PaagAlertWrapper objects.
    }
}







@AuraEnabled(cacheable=true)
    public static List<PaagAlertWrapper> getPaagAlerts(String planId, String clientId) {
        //system.debug('Client Id '+ clientId);
        PaagAlertWrapper[] alerts = new PaagAlertWrapper[0];
        
        String paagAlertQuery = 'select Message__c,Display_Start_Date__c,Display_End_Date__c,PAAG_Alert_Call_Type__c from PAAG_Alerts__c where (Global__c = true or Plan__r.Name = \'' + planId + '\'';
        if(clientId != null && clientId != '') {
            paagAlertQuery = paagAlertQuery + ' or (Client_Id__c =\'' + clientId + '\')';
        } 
        paagAlertQuery = paagAlertQuery + ') order by Display_Start_Date__c desc';
        
        //system.debug('paagAlertQuery :::: ' + paagAlertQuery);
        
        List<PAAG_Alerts__c> paagAlertList = (List<PAAG_Alerts__c>)Database.query(paagAlertQuery);
        
        //system.debug('paagAlertList ::: ' + paagAlertList);
        
         for(PAAG_Alerts__c paagAlert : paagAlertList){
             boolean beforeDate = paagAlert.Display_Start_Date__c != null && Date.today() < paagAlert.Display_Start_Date__c;
             boolean afterDate = paagAlert.Display_End_Date__c !=null && Date.today() > paagAlert.Display_End_Date__c;
             //system.debug('===================================Before Date ' + beforeDate);
             if((paagAlert.Display_Start_Date__c != null && Date.today() < paagAlert.Display_Start_Date__c) || (paagAlert.Display_End_Date__c !=null && Date.today() > paagAlert.Display_End_Date__c))
                continue;                
             
             PaagAlertWrapper alert = new PaagAlertWrapper();
             alert.message = paagAlert.Message__c;
             alert.PAAGAlertCallType=paagAlert.PAAG_Alert_Call_Type__c;
             alerts.add(alert);
         }
         
        return alerts;
    }






@isTest
private class TestCreateCaseActionForPaag {
    
    @isTest
    static void testCreateCaseActionForPaag() {
        // Test data setup
        Case newCase = new Case();
        newCase.Subject = 'Test Case';
        // Add any other required fields
        insert newCase;
        
        // Call the method
        Id caseActionId = YourClassName.createCaseActionForPaag('TestPlanId', 'TestCallType', newCase.Id);
        
        // Retrieve the inserted Case Action record
        Case_Actions__c insertedCaseAction = [SELECT Id FROM Case_Actions__c WHERE Id = :caseActionId];
        
        // Assert that the Case Action record was created successfully
        System.assertNotEquals(null, insertedCaseAction, 'Case Action record should have been created.');
    }
}





import { LightningElement } from 'lwc';
import createCaseActionForPaag from '@salesforce/apex/YourApexClass.createCaseActionForPaag';

describe('createCaseActionForPaag Test', () => {
    it('should create Case Action for PAAG', () => {
        // Setup
        const planId = 'TestPlanId';
        const callType = 'TestCallType';
        const caseId = 'TestCaseId';
        const expectedId = 'TestId123';

        // Mocking Apex method
        jest.mock(
            '@salesforce/apex/YourApexClass.createCaseActionForPaag',
            () => ({
                default: jest.fn()
            }),
            { virtual: true }
        );

        // Calling the mocked Apex method
        createCaseActionForPaag.mockResolvedValue({ Id: expectedId });

        // Execution
        return createCaseActionForPaag({ planId, callType, caseId })
            .then(result => {
                // Assertion
                expect(result).toBe(expectedId);
                expect(createCaseActionForPaag.mock.calls[0][0]).toEqual({
                    planId,
                    callType,
                    caseId
                });
            });
    });
});






public static Id createCaseActionForPaag(String planId,  String callType, Id caseId){
        List<Case_Actions__c> caListToInsert= new List<Case_Actions__c>();
        
        List<Case_Actions__c> caseActions = [select id from Case_Actions__c where  PlanID_Text__c=:planId and case__c = :caseId and Call_Type__c = :callType];
        if(caseActions.size()==0)
        {
        Case_Actions__c c =new Case_Actions__c();
        c.Case__c=caseId;
        c.PlanID_Text__c = planId ;
        c.Call_Activity__c = 'Inquiry' ;
        c.Call_Type__c = callType ;
        caListToInsert.add(c);
        }
            insert caListToInsert;
        //system.debug('Returned CaseAction Id for PAAG' + caListToInsert[0].Id);
      return caListToInsert[0].Id ;
    }
