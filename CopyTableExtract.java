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
