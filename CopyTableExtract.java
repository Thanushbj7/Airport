Here are the key points from the Data Management topic in Salesforce Trailhead:

### **1. Importance of Data Management in Salesforce**
- **Data Quality**: Ensures that data is accurate, complete, and reliable. High-quality data is critical for effective decision-making, reporting, and customer relationship management.
- **Data Governance**: Establishes policies and procedures to manage data consistently, ensuring data security, compliance, and quality across the organization.

### **2. Data Import Tools**
- **Data Import Wizard**: A tool designed for non-technical users to import data into Salesforce. It supports importing data into standard objects like Accounts, Contacts, Leads, and Custom Objects. It is easy to use with a step-by-step interface and is best for importing up to 50,000 records at a time.
- **Data Loader**: A more advanced tool that allows for importing, updating, exporting, and deleting large volumes of data (up to 5 million records) in Salesforce. It supports all standard and custom objects and is ideal for more complex data management tasks.

### **3. Data Export Tools**
- **Data Export Service**: A Salesforce feature that allows users to export data manually or schedule automatic exports (weekly or monthly). It provides a complete backup of all data in Salesforce.
- **Data Loader**: In addition to importing data, Data Loader can be used to export Salesforce data into various file formats (such as CSV) for analysis or backup purposes.

### **4. Maintaining Data Quality**
- **Validation Rules**: Used to enforce specific data entry requirements, ensuring that data entered into Salesforce meets defined criteria. This helps prevent bad data from being saved in the system.
- **Duplicate Management**: Salesforce provides Duplicate Rules and Matching Rules to prevent and manage duplicate records. These rules help maintain data integrity by identifying potential duplicates before they are created or imported.

### **5. Data Backup and Restore**
- **Backup Strategies**: Regularly backing up data is essential for disaster recovery and data loss prevention. Salesforce provides several options for data backup, including manual exports, scheduled exports, and third-party tools.
- **Data Recovery Services**: Salesforce offers a data recovery service, but it is a paid, last-resort option that can be costly and time-consuming. Organizations are encouraged to maintain their own regular backup practices.

### **6. Mass Data Operations**
- **Mass Update**: Use tools like Data Loader or third-party apps to perform bulk updates to records. This is useful when a large number of records need to be modified based on specific criteria.
- **Mass Delete**: Allows users to delete large volumes of data efficiently. Salesforce provides a Mass Delete Wizard for common objects like Accounts, Leads, and Contacts, but Data Loader can also be used for mass deletion tasks.

### **7. Data Archiving and Deletion**
- **Data Archiving**: Salesforce recommends archiving old or inactive data to improve performance and manage storage limits. Data that is no longer needed for day-to-day operations but must be retained for compliance or historical purposes can be archived.
- **Recycle Bin**: Deleted records in Salesforce are moved to the Recycle Bin, where they can be restored or permanently deleted. The Recycle Bin retains data for 15 days before it is permanently removed.

### **8. Managing Storage Limits**
- **Data Storage vs. File Storage**: Salesforce provides separate storage limits for data (records) and files (attachments, documents). Monitoring and managing storage usage is crucial to avoid exceeding limits and incurring additional costs.
- **Optimize Storage Usage**: Regularly review and delete unnecessary records or attachments, compress files, and use third-party storage options to manage storage efficiently.

### **9. Salesforce Data Relationships**
- **Lookup Relationships**: Establish a loose relationship between two objects. They are optional and do not affect the parent-child relationship.
- **Master-Detail Relationships**: Create a strong relationship where the detail object is closely linked to the master object, inheriting sharing and security settings.
- **Junction Objects**: Custom objects used to create many-to-many relationships between two objects, enabling more complex data modeling.

### **10. Best Practices for Data Management**
- **Regular Data Cleansing**: Routinely review and clean data to remove duplicates, incorrect entries, and outdated information to maintain high-quality data.
- **Data Governance**: Implement a robust data governance framework, including clear guidelines and processes for data entry, management, and security.
- **User Training**: Educate users on proper data entry practices, the importance of data quality, and how to use Salesforce tools effectively.

By following these key points and best practices, organizations can optimize their data management in Salesforce, ensuring accurate, high-quality data that supports effective decision-making and customer relationship management.











Here are the key points on optimizing customer data with standard and custom objects in Salesforce:

### **1. Understanding Objects in Salesforce**
- **Objects**: Objects in Salesforce are database tables that store specific types of data. There are two types of objects: **Standard Objects** and **Custom Objects**.
- **Standard Objects**: Predefined objects provided by Salesforce, such as Accounts, Contacts, Opportunities, Leads, and Cases. These objects are designed to support core CRM functionalities.
- **Custom Objects**: User-defined objects created to capture specific data unique to an organization’s business needs. Custom objects provide flexibility to extend Salesforce’s functionality beyond what is offered by standard objects.

### **2. Utilizing Standard Objects for Customer Data**
- **Accounts and Contacts**: Accounts represent companies or individuals you're doing business with, while Contacts represent individuals associated with those accounts.
- **Leads and Opportunities**: Leads are potential customers or sales prospects, and Opportunities represent potential revenue-generating sales deals that you are working on.
- **Cases**: Cases are used to track customer issues and support requests, helping to provide excellent customer service.

### **3. When to Use Custom Objects**
- **Specific Business Processes**: Custom objects should be used when you have specific business processes that cannot be effectively managed using standard objects. For example, if you need to track inventory, you might create a custom object called "Inventory".
- **Additional Data Requirements**: If your organization needs to track additional data that does not fit well into standard objects, custom objects provide a way to capture and manage that data effectively.

### **4. Optimizing Data with Custom Fields**
- **Custom Fields**: Add custom fields to standard or custom objects to capture additional data points specific to your business needs. Custom fields can be of different types such as text, date, number, checkbox, formula, and picklist.
- **Formula Fields**: Use formula fields to perform calculations based on other fields in the object. This helps in deriving meaningful insights without manually updating data.

### **5. Relationships Between Objects**
- **Lookup Relationships**: Create a loose relationship between two objects. For example, a custom object "Projects" might have a lookup relationship to the "Accounts" standard object.
- **Master-Detail Relationships**: Create a strong relationship where the detail object inherits the sharing and security settings of the master object. For example, a custom object "Order Items" might have a master-detail relationship with a standard object "Orders".
- **Many-to-Many Relationships**: Achieved using a junction object, which is a custom object with two master-detail relationships to other objects, enabling many-to-many relationships.

### **6. Leveraging Schema Builder**
- **Schema Builder**: A visual tool in Salesforce that helps you design and understand your data model. It allows you to create and modify objects, fields, and relationships in a drag-and-drop interface.

### **7. Data Quality and Integrity**
- **Validation Rules**: Set up validation rules on objects to ensure data quality by restricting the type of data that can be entered into a field.
- **Required Fields**: Ensure critical data is always captured by making fields required during record creation or updating.
- **Duplicate Management**: Use duplicate rules and matching rules to prevent duplicate records and maintain a clean dataset.

### **8. Automating Data Management**
- **Process Builder and Flows**: Use Process Builder and Salesforce Flows to automate data updates, notifications, and record creation based on specific criteria.
- **Workflow Rules**: Automate standard internal procedures and processes to save time and ensure data consistency.

### **9. Reporting and Analytics**
- **Custom Reports**: Create custom reports to analyze data from both standard and custom objects, providing insights into customer behaviors, trends, and business performance.
- **Dashboards**: Build dashboards to visualize key metrics and KPIs using data from standard and custom objects.

### **10. Best Practices for Managing Customer Data**
- **Use Standard Objects Where Possible**: Leverage standard objects as much as possible for common CRM functionalities to align with Salesforce best practices and reduce customization efforts.
- **Keep the Data Model Simple**: Avoid overcomplicating your data model with too many custom objects and fields, which can impact performance and data integrity.
- **Regularly Review and Clean Data**: Periodically audit your data for accuracy and completeness. Use tools like data loader for mass updates and cleanup.
- **Documentation and Training**: Document the purpose and use of each object and field, and ensure end-users are trained on how to properly enter and manage data.

By optimizing customer data with the right mix of standard and custom objects, organizations can better manage their data, streamline processes, and derive valuable insights to drive business growth.







1. Multi-Tenant ArchitectureDefinition: Salesforce uses a multi-tenant architecture, meaning multiple organizations (tenants) share the same physical infrastructure (servers, databases, and software).Benefits: Reduces costs, simplifies upgrades and maintenance, and allows for continuous improvements and innovations for all tenants simultaneously without impacting individual customizations.2. Metadata-Driven Development ModelDefinition: Salesforce is built on a metadata-driven architecture. Metadata is data about data, such as the structure of objects, fields, page layouts, and configurations.Benefits: Allows administrators and developers to easily customize applications without affecting the core software code. Changes are made declaratively through point-and-click tools, which makes development faster and more flexible.3. API-First ApproachDefinition: Salesforce has an API-first architecture, meaning every function within the platform can be accessed programmatically via APIs.Benefits: Facilitates integration with external systems and supports a wide range of use cases, including mobile applications, third-party software, and internal tools.4. Trust and Security ModelDefinition: Security is a core component of Salesforce's architecture. The platform provides robust security features, including data encryption, two-factor authentication, IP whitelisting, and role-based access controls.Benefits: Ensures data security and privacy, compliance with regulatory standards, and builds trust with customers.5. Salesforce Data ArchitectureData Storage: Salesforce uses a combination of relational databases to store structured data and file storage for unstructured data, such as documents and attachments.Data Modeling: Consists of standard and custom objects, which represent database tables. Fields in these objects represent table columns, and records represent rows.6. Salesforce AppExchange and EcosystemAppExchange: Salesforce's marketplace for third-party applications that extend the platform's functionality. These apps are built on the same platform, ensuring seamless integration.Ecosystem: Includes a robust community of partners, developers, and administrators who contribute to the platform's innovation and knowledge-sharing.7. Salesforce Lightning PlatformDefinition: Lightning is Salesforce's modern user interface and development framework for building custom applications.Components: Lightning App Builder, Lightning Components, and Lightning Web Components (LWC) for creating responsive, dynamic web applications.Benefits: Provides a modern, responsive user experience and accelerates app development through reusable components.8. Upgrades and Continuous DeliveryRegular Upgrades: Salesforce releases three major upgrades annually, automatically delivering new features and enhancements to all customers.No Downtime: Upgrades are designed to occur without any downtime or disruption to the customers' customizations or business operations.9. Scalability and PerformanceScalability: Salesforce’s architecture is designed to scale easily with the growth of customers' businesses, handling large volumes of data and users efficiently.Performance Optimization: Salesforce uses various techniques to ensure optimal performance, such as data caching, efficient query processing, and asynchronous processing.10. Platform ServicesServices: Includes various built-in services such as Einstein for AI-powered analytics, Flow for process automation, and Heroku for building and deploying apps.Integration Services: Salesforce provides multiple integration options like REST, SOAP APIs, and MuleSoft for seamless connectivity with external systems






3. Salesforce Admin Tools and FeaturesSetup Menu: The main hub for all administrative tasks, where you can configure settings, manage users, and customize the platform.Data Loader and Import Wizard: Tools for importing, exporting, and updating large volumes of data.Validation Rules: Ensure data integrity by enforcing business rules.Flow Builder and Process Builder: Tools for automating business processes without code.Sandbox Environments: Separate environments for development, testing, and training purposes to ensure changes do not impact the live environment.Sharing Settings and Roles: Configuring who can see what data across the organization.





3. Salesforce Admin Tools and FeaturesSetup Menu: The main hub for all administrative tasks, where you can configure settings, manage users, and customize the platform.Data Loader and Import Wizard: Tools for importing, exporting, and updating large volumes of data.Validation Rules: Ensure data integrity by enforcing business rules.Flow Builder and Process Builder: Tools for automating business processes without code.Sandbox Environments: Separate environments for development, testing, and training purposes to ensure changes do not impact the live environment.Sharing Settings and Roles: Configuring who can see what data across the organization.








@isTest
private class TestOpportunityUpdate {

    @isTest
    static void testUpdateOpportunityAgreementSignaturesPending() {
        List<Opportunity> oppList = new List<Opportunity>();
        // Create Opportunity records with StageName 'Agreement Signatures Pending'
        // Insert Opportunity records into database
        
        // Call the method under test
        Test.startTest();
        // Execute your method here
        Test.stopTest();
        
        // Assert the values of opptoupdate
        // Assert other outcomes as needed
    }
    
    @isTest
    static void testUpdateOpportunityPlanDeliveryPending() {
        List<Opportunity> oppList = new List<Opportunity>();
        // Create Opportunity records with StageName 'Plan Delivery Pending'
        // Insert Opportunity records into database
        
        // Call the method under test
        Test.startTest();
        // Execute your method here
        Test.stopTest();
        
        // Assert the values of opptoupdate
        // Assert other outcomes as needed
    }
    
    @isTest
    static void testOpportunityCannotBeSubmittedForReview() {
        List<Opportunity> oppList = new List<Opportunity>();
        // Create Opportunity records with StageName other than 'Agreement Signatures Pending' or 'Plan Delivery Pending'
        // Insert Opportunity records into database
        
        // Call the method under test
        Test.startTest();
        // Execute your method here
        Test.stopTest();
        
        // Assert the values of MaptoReturn
        // Assert other outcomes as needed
    }
}











Opportunity opptoupdate=new Opportunity();
                for(Opportunity tempopp:oppList) { 
                    
                    if(tempopp.StageName=='Agreement Signatures Pending') {
                        
                        opptoupdate.Id=tempopp.Id;
                        opptoupdate.StageName=tempopp.StageName;                
                        opptoupdate.Client_Sign_Date__c=tempopp.Client_Sign_Date__c;
                    }
                    else if(tempopp.StageName=='Plan Delivery Pending') {
                        
                        opptoupdate.Id=tempopp.Id;
                        opptoupdate.StageName=tempopp.StageName;  
                    }
                    else { 
                        MaptoReturn.put('CanNotChange','Cannot be submitted for review at this stage.');
                    }
                }





@IsTest
public class OpptSendForApprovalCntrlrTest {
    
    @testSetup 
    static void setup() {
        RecordType rTypeopportunity = TestUtils.getRecordType('Opportunity', 'BD Financial Agreement');
        RecordType rTypeopportunitychild = TestUtils.getRecordType('Opportunity', 'BD Financial Agreement Amendment');    
        RecordType rTypeAccount = TestUtils.getRecordType('Account', 'Other Firms');
        
        Account client = new Account(Name = 'Test Account', RecordTypeId = rTypeAccount.Id);
        insert client;
        
        User testuser = [select Id from User Where Id =: UserInfo.getUserId()];  
        Plan__c plan = TestUtils.createPlan('IPS', null, false);
        plan.RR_Email_Permission_Level__c = 'Opt In';
        
        insert plan;

        // Create Opportunities
        List<Opportunity> opportunities = new List<Opportunity>{
            new Opportunity(Name = 'Test OpportunityTest123', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'New', Term__c = 'One-Time', Fee_Schedule_Type__c = 'Hourly', One_Time_Total_Fee__c = 30.00, 
                            Fee_Schedule__c = 'Split', payment_type__c = 'ACH', CloseDate = System.today()),                                             
            new Opportunity(Name = 'Test OpportunityTest1234', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'New', I_agree_I_am_licensed__c = 'Yes', Term__c = 'Annual', Fee_Schedule__c = 'Monthly', 
                            payment_type__c = 'ACH', CloseDate = System.today(), Intial_Flat_Fee__c = 20.00),
            new Opportunity(Name = 'Test OpportunityTest12345', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'New', Term__c = 'One-Time', Fee_Schedule__c = 'Split', payment_type__c = 'ACH', CloseDate = System.today()), 
            new Opportunity(Name = 'Test OpportunityTest12346', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Plan Delivery Pending', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'New', I_agree_I_am_licensed__c = 'Yes', Term__c = 'Annual', Fee_Schedule__c = 'Monthly', 
                            payment_type__c = 'ACH', CloseDate = System.today(), Intial_Flat_Fee__c = 20.00),
            new Opportunity(Name = 'Test OpportunityTest12347', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Closed Won', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'Closed', Term__c = 'One-Time', One_Time_Total_Fee__c = 40.00, Fee_Schedule__c = 'Split', payment_type__c = 'ACH', CloseDate = System.today())
        };
        insert opportunities;
        
        // Create Attachments
        List<Attachment> attachments = new List<Attachment>{
            new Attachment(Name = 'Agreement', Body = Blob.valueOf('Unit Test Attachment Body'), ParentId = opportunities[1].Id),
            new Attachment(Name = 'Agreement', Body = Blob.valueOf('Unit Test Attachment Body'), ParentId = opportunities[3].Id)
        };
        insert attachments;
        
        // Create Content Document and Link
        ContentVersion contentVersion = new ContentVersion(
            Title = 'FinancialAgreement',
            PathOnClient = 'Penguins.jpg',
            VersionData = Blob.valueOf('Test Content'),
            IsMajorVersion = true
        );
        insert contentVersion;
        
        List<ContentDocument> documents = [SELECT Id, Title, LatestPublishedVersionId FROM ContentDocument];
        ContentDocumentLink cdl = new ContentDocumentLink(
            LinkedEntityId = opportunities[0].Id,
            ContentDocumentId = documents[0].Id,
            ShareType = 'V'
        );
        insert cdl;

        // Create Financial Plan
        Financial_Plan__c testplan = new Financial_Plan__c(
            BD_Financial_Agreement__c = opportunities[3].Id,
            TRP_Plan_Approval_Status__c = 'Not Started'
        );
        insert testplan;
    }

    @isTest
    public static void opportunity123() {
        Test.startTest(); 
        List<Opportunity> fecttempdata1 = [select Id from Opportunity where Name = 'Test OpportunityTest123'];
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata1[0].Id, 'Theme4d');                   
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Success'), 'Opportunity should be successfully updated.');
        System.assertEquals('Opportunity sent for TRP Approval and stage is changed to TRP Agreement Approval Pending.', resultMap.get('Success'));
    }
    
    @isTest
    public static void opportunity1234() {
        Test.startTest(); 
        List<Opportunity> fecttempdata2 = [select Id from Opportunity where Name = 'Test OpportunityTest1234']; 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata2[0].Id, '');                
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Success'), 'Opportunity should be successfully updated.');
        System.assertEquals('Opportunity sent for TRP Approval and stage is changed to TRP Agreement Approval Pending.', resultMap.get('Success'));
    }
    
    @isTest
    public static void opportunity12345() {
        Test.startTest(); 
        List<Opportunity> fecttempdata3 = [select Id from Opportunity where Name = 'Test OpportunityTest12345']; 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata3[0].Id, '');                
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Attachment'), 'There should be an error for missing agreement attachment.');
        System.assertEquals('There should be at least one Attachment having "Agreement" keyword in the name before Submitting for Approval.', resultMap.get('Attachment'));
    }
	
    @isTest
    public static void opportunity12346() {
        Test.startTest(); 
        List<Opportunity> fecttempdata4 = [select Id from Opportunity where Name = 'Test OpportunityTest12346']; 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata4[0].Id, '');           
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Success'), 'Financial Plan should be successfully sent for TRP Approval.');
        System.assertEquals('Financial Plan is sent for TRP Approval', resultMap.get('Success'));
    }
    
    @isTest
    public static void opportunity12347() {
        Test.startTest(); 
        List<Opportunity> fecttempdata = [select Id from Opportunity where Name = 'Test OpportunityTest12347']; 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata[0].Id, '');           
        Test.stopTest();
        
        System.assert(resultMap.containsKey('CanNotChange'), 'Opportunity should not be submitted for review at this stage.');
        System.assertEquals('Cannot be submitted for review at this stage.', resultMap.get('CanNotChange'));
    }
    
    @isTest
    public static void opportunityError() {
        Test.startTest(); 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(null, null);           
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Exception'), 'There should be an exception for null opportunity ID.');
        System.assertNotEquals('', resultMap.get('Exception'), 'Exception message should not be empty.');
    }
		}




















@IsTest
public class OpptSendForApprovalCntrlrTest {
    
    @testSetup 
    static void setup() {
        RecordType rTypeopportunity=TestUtils.getRecordType('Opportunity', 'BD Financial Agreement');
        RecordType rTypeopportunitychild=TestUtils.getRecordType('Opportunity', 'BD Financial Agreement Amendment');    
        RecordType rTypeAccount=TestUtils.getRecordType('Account', 'Other Firms');
        
        Account client = new Account(Name='Test Account', RecordTypeId = rTypeAccount.Id );
        
        insert client;
        
        User testuser = [select Id from User Where Id =: UserInfo.getUserId()];  
        Plan__c plan = TestUtils.createPlan('IPS', null, false);
        plan.RR_Email_Permission_Level__c = 'Opt In';
        
        //Create Opportunity
        
        Opportunity pOpportunity = new Opportunity(Name = 'Test OpportunityTest123', AccountId = client.Id, Plan__c = plan.Id, 
                                                   OwnerId = testuser.Id,StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.ID, 
                                                   Opportunity_Status__c = 'New', Term__c = 'One-Time',Fee_Schedule_Type__c = 'Hourly',One_Time_Total_Fee__c=30.00, Fee_Schedule__c = 'Split', payment_type__c='ACH', CloseDate = System.today());                                             
        
        Opportunity pOpportunity1 = new Opportunity(Name = 'Test OpportunityTest1234', AccountId = client.Id, Plan__c = plan.Id, 
                                                    OwnerId = testuser.Id,StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.ID, 
                                                    Opportunity_Status__c = 'New', I_agree_I_am_licensed__c='Yes',Term__c = 'Annual', Fee_Schedule__c = 'Monthly', payment_type__c='ACH', CloseDate = System.today(),Intial_Flat_Fee__c=20.00);                                             
        
        Opportunity pOpportunity2 = new Opportunity(Name = 'Test OpportunityTest12345', AccountId = client.Id, Plan__c = plan.Id, 
                                                    OwnerId = testuser.Id,StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.ID, 
                                                    Opportunity_Status__c = 'New', Term__c = 'One-Time', Fee_Schedule__c = 'Split', payment_type__c='ACH', CloseDate = System.today());                                             
        
        Opportunity pOpportunity3 = new Opportunity(Name = 'Test OpportunityTest12346', AccountId = client.Id, Plan__c = plan.Id, 
                                                    OwnerId = testuser.Id,StageName = 'Plan Delivery Pending', RecordTypeId = rTypeopportunity.ID, 
                                                    Opportunity_Status__c = 'New', I_agree_I_am_licensed__c='Yes',Term__c = 'Annual', Fee_Schedule__c = 'Monthly', payment_type__c='ACH', CloseDate = System.today(),Intial_Flat_Fee__c=20.00);                                               
        
        Opportunity pOpportunity4 = new Opportunity(Name = 'Test OpportunityTest12347', AccountId = client.Id, Plan__c = plan.Id, 
                                                   OwnerId = testuser.Id,StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.ID, 
                                                   Opportunity_Status__c = 'New', Term__c = 'One-Time', One_Time_Total_Fee__c=40.00, Fee_Schedule__c = 'Split', payment_type__c='ACH', CloseDate = System.today());                                             
        
        List<Opportunity> tempopport=new List<Opportunity>();
        tempopport.add(pOpportunity) ;
        tempopport.add(pOpportunity1) ;
        tempopport.add(pOpportunity2) ;
        tempopport.add(pOpportunity3) ;
        tempopport.add(pOpportunity4) ;
        insert tempopport;
        
        Attachment attach=new Attachment();     
        attach.Name='Agreement';
        Blob bodyBlob=Blob.valueOf('Unit Test Attachment Body');
        attach.body=bodyBlob;
        attach.parentId=tempopport[1].Id;             
        insert attach; 
        
        Attachment attach2=new Attachment();     
        attach2.Name='Agreement';
        Blob bodyBlob2=Blob.valueOf('Unit Test Attachment Body');
        attach2.body=bodyBlob2;
        attach2.parentId=tempopport[3].Id;             
        insert attach2;
                
        // Content Document creation
        
        ContentVersion contentVersion = new ContentVersion(
            Title = 'FinancialAgreement',
            PathOnClient = 'Penguins.jpg',
            VersionData = Blob.valueOf('Test Content'),
            IsMajorVersion = true
        );
        insert contentVersion;
        
        List<ContentDocument> documents = [SELECT Id, Title, LatestPublishedVersionId FROM ContentDocument];
        
        //create ContentDocumentLink  record 
        ContentDocumentLink cdl = New ContentDocumentLink();
        cdl.LinkedEntityId = tempopport[0].id;
        cdl.ContentDocumentId = documents[0].Id;
        cdl.shareType = 'V';
        insert cdl;
        
        System.debug('0. CDL ' + cdl);
        
        Financial_Plan__c testplan=new Financial_Plan__c();
        testplan.BD_Financial_Agreement__c=tempopport[3].Id;
        testplan.TRP_Plan_Approval_Status__c='Not Started';
        insert testplan;
        
    }

    @isTest
    public static void opportunity123() {
        Test.startTest(); 
        List<Opportunity> fecttempdata1=[select Id from Opportunity where Name=:'Test OpportunityTest123'];
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata1[0].Id,'Theme4d');                   
        Test.stopTest();
    }
    
    @isTest
    public static void opportunity1234() {
        Test.startTest(); 
        List<Opportunity> fecttempdata2=[select Id from Opportunity where Name=:'Test OpportunityTest1234']; 
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata2[0].Id,'');                
        Test.stopTest();
    }
    
    @isTest
    public static void opportunity12345() {
        Test.startTest(); 
        List<Opportunity> fecttempdata3=[select Id from Opportunity where Name=:'Test OpportunityTest12345']; 
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata3[0].Id,'');                
        Test.stopTest();
    }
	
    @isTest
	public static void opportunity123456() {
        Test.startTest(); 
        List<Opportunity> fecttempdata4=[select Id from Opportunity where Name=:'Test OpportunityTest12346']; 
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata4[0].Id,'');           
        Test.stopTest();
    }
    
    @isTest
	public static void opportunity12347() {
        Test.startTest(); 
        List<Opportunity> fecttempdata = [select Id from Opportunity where Name=:'Test OpportunityTest12347']; 
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata[0].Id,'');           
        Test.stopTest();
    }
    
    @isTest
	public static void opportunityError() {
        Test.startTest(); 
        OpptSendForApprovalCntrlr.FetchOpportunity(null,null);           
        Test.stopTest();
    }
}















@AuraEnabled
    public static Map<string,string> FetchOpportunity(string Id,string optionaltheme) {
        
        Map<String,String> MaptoReturn = new Map<String,String>(); 
        try{
            String theme;
            if(optionaltheme!='') {  
                theme=optionaltheme;
            }
            else {
                theme = UserInfo.getUiThemeDisplayed();  
            }

            integer countagreement=0;
            integer countotherdoc=0;
            List<Opportunity> oppList =[SELECT Id, IsDeleted, AccountId, RecordTypeId, Name, Description, StageName, Amount, OwnerId, 
                                        Opportunity_Status__c,  Plan_Name__c, Plan__c,  Opportunity_Type__c, Plan_Number__c, Action_Plan_Delivery_Method__c, Client_Profiler_Link__c, Count__c, 
                                        ParentOpportunity__c, Plan_Accepted__c, Plan_Delivered__c, Amendment_Parent_Opportunity__c,DP_Doc_RecordId__c,Client_Sign_Date__c,
                                        Fee_Schedule_Type__c,I_agree_I_am_licensed__c,payment_type__c,Term__c,One_Time_Total_Fee__c,Fee_Per_Hour__c,Number_of_Hours__c,Split_Amt_Pd_After_Delivery__c,Split_Amt_Pd_Upon_Sig__c,
                                        Intial_Flat_Fee__c,CreatedById ,Fee_Schedule__c FROM Opportunity where Id=:id]; 
            System.debug('Opportunityfetched-----'+oppList);
            
            Map<String,Opportunity> oppMap = new Map<String,Opportunity>();
            Map<String, List<Attachment>> oppAttchMap = new Map<String, List<Attachment>>();

            for(Opportunity tempopp:oppList) {
                
                MaptoReturn.put('Opportunity',tempopp.Name);
                
                User tempuser=[Select Name from User where Id=:tempopp.CreatedById];
                
                MaptoReturn.put('CreatedBy',tempuser.Name);  
            }                             
            
            String agreementDocName=Label.I_APP_FP_AGREEMENT_DOC_NAME;
            System.debug('Agreementfetched-----'+agreementDocName);
            if (String.IsBlank(agreementDocName))
                agreementDocName = '%agreement%';
            else
                agreementDocName = '%'+agreementDocName+'%';

            Boolean flag=false;
            ID BDRecordId=Schema.SObjectType.Opportunity.RecordTypeInfosByName.get('BD Financial Agreement').RecordTypeId;
            ID BDRecordIdchild=Schema.SObjectType.Opportunity.RecordTypeInfosByName.get('BD Financial Agreement Amendment').RecordTypeId;
            
            for(Opportunity OppIter : OppList) {
                if(OppIter.Amendment_Parent_Opportunity__c==null) {
                    if(OppIter.recordtypeid==BDRecordId) {
                        oppMap.put(String.valueOf(OppIter.Id), OppIter);
                    }
                }
                
                else {
                    if(OppIter.recordtypeid==BDRecordIdchild) {
                        oppMap.put(String.valueOf(OppIter.Id), OppIter);
                    }  
                }
            }
            
            List<Attachment> attachList =[select id,name,parentid from Attachment where name LIKE :agreementDocName and parentid IN: oppMap.KeySet()];
            for(Attachment attach : attachList) {   
                List<Attachment> attachLists ;
                if(oppAttchMap.containsKey(attach.parentid)){
                    attachLists = oppAttchMap.get(attach.parentid);
                    attachLists.add(attach);
                    
                }
                else{
                    attachLists = new List<Attachment>();
                    attachLists.add(attach);
                }
                oppAttchMap.put(attach.parentid,attachLists);
            }

            List<ContentDocumentLink> CDL= [SELECT ContentDocumentId FROM ContentDocumentLink WHERE LinkedEntityId =: Id]; 
            List<Id> tempId= new List<Id>();
            for(ContentDocumentLink tempcontentid:CDL) {
                tempId.add(tempcontentid.ContentDocumentId) ; 
            }
            
            List<ContentDocument> CD=  [Select Id, Title, parentid from ContentDocument Where Id IN :tempId]; 
            for(ContentDocument tempcontentdoc:CD) {
                string title=tempcontentdoc.Title; 
                if(title.containsIgnoreCase('agreement')) {
                    
                    countagreement++;
                    countotherdoc++;
                }
                else {
                    countotherdoc++;
                }
            }

            for(Opportunity opp : oppMap.Values()) {
                
                system.debug('oppAttchMap.get(opp.Id) ### '+oppAttchMap.get(opp.Id));
                system.debug('opp.DP_Doc_RecordId__c ### '+opp.DP_Doc_RecordId__c);
                
                //  if(oppAttchMap.get(opp.Id) == null || (oppAttchMap.get(opp.Id) != null && String.IsBlank(opp.DP_Doc_RecordId__c)))
                if(oppAttchMap.get(opp.Id) == null && String.IsBlank(opp.DP_Doc_RecordId__c) && countagreement==0)
                    // opp.adderror('There should be at least one Attachment having "Agreement" keyword in the name before entering the client sign date.'); 
                    MaptoReturn.put('Attachment','There should be at least one Attachment having "Agreement" keyword in the name before Submitting for Approval.'); 
                
                String errMsg = IsValidAfterClientSignDt(opp);  
                
                if(!String.isBlank(errMsg)) {
                    system.debug('all validations--'+ errMsg);
                    //opp.adderror(errMsg);
                    MaptoReturn.put('OtherValidation',errMsg);
                }
                
                MaptoReturn.put('Theme',theme); 
            }
            if(MaptoReturn.containsKey('Attachment') || MaptoReturn.containsKey('OtherValidation')) {
                System.debug('Last Opportunity details errors--'+MaptoReturn);
            }
            else {
                Opportunity opptoupdate=new Opportunity();
                for(Opportunity tempopp:oppList) { 
                    
                    if(tempopp.StageName=='Agreement Signatures Pending') {
                        
                        opptoupdate.Id=tempopp.Id;
                        opptoupdate.StageName=tempopp.StageName;                
                        opptoupdate.Client_Sign_Date__c=tempopp.Client_Sign_Date__c;
                    }
                    else if(tempopp.StageName=='Plan Delivery Pending') {
                        
                        opptoupdate.Id=tempopp.Id;
                        opptoupdate.StageName=tempopp.StageName;  
                    }
                    else { 
                        MaptoReturn.put('CanNotChange','Cannot be submitted for review at this stage.');
                    }
                }
                
                if(opptoupdate.Id!=null &&  opptoupdate.StageName=='Agreement Signatures Pending') {
                    
                    if(opptoupdate.Client_Sign_Date__c==null) {
                        opptoupdate.StageName='TRP Agreement Approval Pending'; 
                        opptoupdate.Client_Sign_Date__c=Date.today();
                        update opptoupdate;
                        MaptoReturn.put('Success','Opportunity sent for  TRP Approval and stage is changed to TRP Agreement Approval Pending.');
                    }
                    else {
                        opptoupdate.StageName='TRP Agreement Approval Pending';
                        update opptoupdate;  
                    }
                    
                    MaptoReturn.put('Success','Opportunity sent for  TRP Approval and stage is changed to TRP Agreement Approval Pending.');
                }
                
                else if(opptoupdate.Id!=null &&  opptoupdate.StageName=='Plan Delivery Pending') {
                    List<Financial_Plan__C> tempplan= [select Id,TRP_Plan_Approval_Status__c from Financial_Plan__c where BD_Financial_Agreement__c=:opptoupdate.Id  order by CreatedDate desc ] ; 
                    if(tempplan.size()>0) {
                        Financial_Plan__C tempfinacialplan=tempplan[0];
                        if(tempfinacialplan.TRP_Plan_Approval_Status__c=='Not Started') {
                            Financial_Plan__C plantoupdate=new Financial_Plan__C();
                            plantoupdate.Id=tempfinacialplan.Id;
                            plantoupdate.TRP_Plan_Approval_Status__c='Approval Pending';
                            update plantoupdate;
                            MaptoReturn.put('Success','Financial Plan is sent for TRP  Approval');
                        }
                        else
                            MaptoReturn.put('OtherValidation','No Action Performed');  
                    }
                    else {
                        MaptoReturn.put('OtherValidation','No Action Performed');
                    }
                    
                }
                
                else {
                    
                }

            }  
            return MaptoReturn;
        }
        catch(Exception ex) {
            MaptoReturn.put('Exception',ex.getMessage()); 
            return MaptoReturn;
        }
        
    }
