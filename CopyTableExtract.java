import {​​​​​​​ LightningElement, api, track, wire }​​​​​​​ from 'lwc';

import createCaseActions from '@salesforce/apex/lightningPopController.createCaseActions';

import createNewCase from '@salesforce/apex/lightningPopController.createNewCase';

import {​​​​​​​ ShowToastEvent }​​​​​​​ from "lightning/platformShowToastEvent";

import getPlans from '@salesforce/apex/lightningPopController.initializeAndLoadPlanData';

import getCurrentCase from '@salesforce/apex/lightningPopController.getCurrentCase';

import updateCase from '@salesforce/apex/lightningPopController.updateCase';

import getRelatedCaseActions from '@salesforce/apex/lightningPopController.getRelatedCaseActions';

import getCaseId from '@salesforce/apex/lightningPopController.getCaseId';

import profileid from '@salesforce/label/c.TestForTM';

import CALL_TYPE from '@salesforce/schema/Case_Actions__c.Call_Type__c';

import CALL_ACTIVITY from '@salesforce/schema/Case_Actions__c.Call_Activity__c';

import CASE_ACTION_OBJECT from '@salesforce/schema/Case_Actions__c';

import {​​​​​​​ getObjectInfo, getPicklistValues }​​​​​​​ from 'lightning/uiObjectInfoApi';

import {​​​​​​​ getObjectInfos }​​​​​​​ from "lightning/uiObjectInfoApi";

import {​​​​​​​ RefreshEvent }​​​​​​​ from 'lightning/refresh';

import PAAGTOCASEACTION_MESSAGE_CHANNELL from '@salesforce/messageChannel/paagToCaseAction__c';

import {​​​​​​​ MessageContext, subscribe }​​​​​​​ from 'lightning/messageService';

import CALLER_TYPE from '@salesforce/schema/Case.Caller_Type__c';

import CASE_ORIGIN from '@salesforce/schema/Case.Origin';

import CASE_OBJECT from '@salesforce/schema/Case';

import {​​​​​​​ refreshApex }​​​​​​​ from "@salesforce/apex";

import LightningConfirm from 'lightning/confirm';

const columns = [

    {​​​​​​​ label: 'Plan Id', fieldName: 'PlanID_Text__c' }​​​​​​​,

    {​​​​​​​ label: 'Call Activity', fieldName: 'Call_Activity__c' }​​​​​​​,

    {​​​​​​​ label: 'Call Type', fieldName: 'Call_Type__c' }​​​​​​​,

];
 
export default class CCCaseAction extends LightningElement {​​​​​​​

    //filter=profileid;

    //  profileid;

    @track columns = columns;

    @track data = [];
 
    //caseActionList = [];

    @track receivedMessage = '';

    planId;

    callActivity;

    callType;

    @track subscription = null;

    @track callTypeError = '';

    @track callActivityError = '';

    @track planIdError = '';

    @track commentError = '';

    callActivity = '';

    @track callerTpeError = '';

    @track caseOriginError = '';

    caseIds;

    @track callerTypeOptions;

    callerType = 'Participant';

    @track callActivities;

    @track planIds ;

    @track callTypes;

    caseNum;

    clientSSN = '';

    ctiVRUApp;

    callTypesData;;

    @track caseOriginOptions;

    caseOrigin = 'Phone';    

    Id;

    caseId;

    @wire(MessageContext)

    messageContext;

    @api recordId;

    @track checkSubmit=false;

    @track CaseactionList;
 
    @wire(getObjectInfo, {​​​​​​​ objectApiName: CASE_ACTION_OBJECT }​​​​​​​)

    objectInfo;

    @wire(getPicklistValues, {​​​​​​​

        // objectApiName: OPPORTUNITY_OBJECT,

        recordTypeId: '$objectInfo.data.defaultRecordTypeId',
 
        fieldApiName: CALL_TYPE

            //RESPONSE

    }​​​​​​​)

    callTypeInfo({​​​​​​​ data, error }​​​​​​​) {​​​​​​​

        if (data) this.callTypesData = data;

        // console.log('this is call type', JSON.stringify(data));

        console.log('recordTypeId', this.recordTypeId);

        // console.log('this.callTypeOptions', JSON.stringify(this.callTypeData));

    }​​​​​​​

    if (error) {​​​​​​​

        console.log('error', error);

    }​​​​​​​

    @wire(getPicklistValues, {​​​​​​​

        // objectApiName: OPPORTUNITY_OBJECT,

        recordTypeId: '$objectInfo.data.defaultRecordTypeId',
 
        fieldApiName: CALL_ACTIVITY

            //RESPONSE

    }​​​​​​​)

    callActivityInfo({​​​​​​​ data, error }​​​​​​​) {​​​​​​​

        if (data) this.callActivitiesOptions = data.values;

        // console.log('this is call Activity', JSON.stringify(data));

        console.log('recordTypeId', this.recordTypeId);

        // console.log('this.callTypeOptions', JSON.stringify(this.callTypeData));

    }​​​​​​​

    if (error) {​​​​​​​

        console.log('error', error);

    }​​​​​​​

    handlePlanIdsChange(event) {​​​​​​​

        this.planId = event.target.value;

        let index = event.target.dataset.id;

        let fieldName = event.target.name;

        let value = event.target.value;

            for(let i = 0; i < this.CaseactionList.length; i++) {​​​​​​​

                if(this.CaseactionList[i].index === parseInt(index)) {​​​​​​​

                    this.CaseactionList[i][fieldName] = value;

                }​​​​​​​

                else{​​​​​​​

                    this.CaseactionList[i].ShowCallTypeDropDown=false;

                }​​​​​​​  

            }​​​​​​​

            this.planIdError='';

        console.log('In Call Type Change'+JSON.stringify(this.CaseactionList))

    }​​​​​​​

    handlecallActivitiesChange(event) {​​​​​​​        

        let key = this.callTypesData.controllerValues[event.target.value];

        console.log('Value of key is', this.key);

        this.callTypesOptions = this.callTypesData.values.filter(opt => opt.validFor.includes(key));

        // console.log('this is handleChange', JSON.stringify(this.responseReasonOptions));

        this.callActivity = event.target.value;

        console.log('In Call Type Change'+(this.callTypesOptions))

        //  alert(this.fields);

        // console.log('Hey', acc);

        // this.callActivity = event.target.value;

        let index = event.target.dataset.id;

        let fieldName = event.target.name;

        let value = event.target.value;

            for(let i = 0; i < this.CaseactionList.length; i++) {​​​​​​​

                if(this.CaseactionList[i].index === parseInt(index)) {​​​​​​​

                    this.CaseactionList[i][fieldName] = value;

                    // callType mark to empty when changing call activity

                    this.CaseactionList[i].calltype='';

                    this.CaseactionList[i].ShowCallTypeDropDown=true;

                }​​​​​​​else{​​​​​​​

                    this.CaseactionList[i].ShowCallTypeDropDown=false;

                }​​​​​​​

            }​​​​​​​

             this.callActivityError = ''

            console.log('In Call Type Change'+JSON.stringify(this.CaseactionList))

    }​​​​​​​

    handlecallTypesChange(event) {​​​​​​​

        this.callType = event.target.value;

        let index = event.target.dataset.id;

        let fieldName = event.target.name;

        let value = event.target.value;

            for(let i = 0; i < this.CaseactionList.length; i++) {​​​​​​​

                if(this.CaseactionList[i].index === parseInt(index)) {​​​​​​​

                    this.CaseactionList[i][fieldName] = value;

                    //this.CaseactionList[i].ShowCallTypeDropDown=false;

                }​​​​​​​

            }​​​​​​​

            this.callTypeError='';

            console.log('In Call Type Change'+JSON.stringify(this.CaseactionList))

    }​​​​​​​

    @wire(getObjectInfo, {​​​​​​​ objectApiName: CASE_OBJECT }​​​​​​​)

    objectInfo1;

    @wire(getPicklistValues, {​​​​​​​

        // objectApiName: OPPORTUNITY_OBJECT,

        recordTypeId: '$objectInfo1.data.defaultRecordTypeId',

        //recordTypeId: '$Campaignrecordtypeid',

        fieldApiName: CASE_ORIGIN

            //RESPONSE

    }​​​​​​​)

    caseOriginInfo({​​​​​​​ data, error }​​​​​​​) {​​​​​​​

        if (data) this.caseOriginOptions = data.values;

        //  console.log('this is response Reason', JSON.stringify(data));

        // console.log('this.responseReasonOptions', JSON.stringify(this.responseReasonData));

    }​​​​​​​
 
 
    @wire(getPicklistValues, {​​​​​​​

        // objectApiName: OPPORTUNITY_OBJECT,

        recordTypeId: '$objectInfo1.data.defaultRecordTypeId',

        //recordTypeId: '$Campaignrecordtypeid',

        fieldApiName: CALLER_TYPE

            //RESPONSE

    }​​​​​​​)

    callerTypeInfo({​​​​​​​ data, error }​​​​​​​) {​​​​​​​

        if (data) this.callerTypeOptions = data.values;

        // console.log('this is response', JSON.stringify(data));

    }​​​​​​​

    handleCallerTypeChange(event) {​​​​​​​

        this.callerType = event.target.value;

        this.checkSubmit=false;

        console.log('this.callerType', this.callerType);

        //event.target.value;

    }​​​​​​​

    handleCaseOriginChange(event) {​​​​​​​

        this.caseOrigin = event.target.value;

        this.checkSubmit=false;

        console.log('this.caseOrigin', this.caseOrigin);

        //event.target.value;

    }​​​​​​​
 
    async onCreateCaseAction() {​​​​​​​
 
        try {​​​​​​​

            //Validate Plan Ids

            if (!this.planId) {​​​​​​​

                this.planIdError = 'Plan Id is required.';

            }​​​​​​​ else {​​​​​​​

                this.planIdError = '';

            }​​​​​​​
 
            //Validate Plan Ids

            if (!this.callActivity) {​​​​​​​

                this.callActivityError = 'Call Activity is required.';

            }​​​​​​​ else {​​​​​​​

                this.callActivityError = '';

            }​​​​​​​
 
            //Validate Plan Ids

            if (!this.callType) {​​​​​​​

                this.callTypeError = 'Call Type is required.';

            }​​​​​​​ else {​​​​​​​

                this.callTypeError = '';

            }​​​​​​​
 
            //if (!this.callTypeError && !this.callActivityError && !this.planIdError) {​​​​​​​
 
                console.log('onCreateCaseAction recordid ',this.recordId )

                const csId = await getCaseId({​​​​​​​ clientId: this.recordId }​​​​​​​);

                console.log('onCreateCaseAction csId', csId);

                if(csId  === null){​​​​​​​
 
                    const NewcaseId = await createNewCase({​​​​​​​ clientId: this.recordId, planId: this.planId, callActivity: this.callActivity, callType: this.callType }​​​​​​​)

                    console.log('onCreateCase csId', NewcaseId);                    

                    const res = await getCurrentCase({​​​​​​​ Id: NewcaseId }​​​​​​​);

                    console.log('nCreateCas res', res);

                    if(res.length==1){​​​​​​​          

                        this.caseNum = res[0].CaseNumber;

                    }​​​​​​​                                  

                    const caseActionId = await createCaseActions({​​​​​​​ clientId: this.recordId,caseId: NewcaseId, planId: this.planId, callActivity: this.callActivity, callType: this.callType }​​​​​​​)

                    const caseActionIdStr = JSON.stringify(caseActionId)

                    let caseActionIdSet = [];

                    caseActionIdSet.push(caseActionId);

                    const caseActionToBeDisplayed = await getRelatedCaseActions({​​​​​​​ caseId: NewcaseId, caseActionId: caseActionIdSet }​​​​​​​)

                    let arrFinal = caseActionToBeDisplayed;                

                    console.log("onCreateCaseAction array: ", arrFinal);

                    arrFinal = arrFinal.map(row => {​​​​​​​

                    return {​​​​​​​ PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c }​​​​​​​;
 
                    }​​​​​​​);

                    this.data = arrFinal;

                    console.log("onCreateCaseAction final", arrFinal);

                }​​​​​​​  

                else{​​​​​​​

                    for(let i = 0; i < this.CaseactionList.length; i++) {​​​​​​​

                        if(this.CaseactionList[i].planId !='' && this.CaseactionList[i].planId != null) {​​​​​​​

                            this.planId=this.CaseactionList[i].planId;

                            // callType mark to empty when changing call activity

                            this.callActivity=this.CaseactionList[i].callActivity;

                            this.callActivity=this.CaseactionList[i].callActivity;

                            this.callType =this.CaseactionList[i].callType;                            

                console.log('onCreateCaseAction this.planId', this.planId);

                console.log('onCreateCaseAction this.callActivity', this.callActivity);

                console.log('onCreateCaseAction this.callType', this.callType);                

                const caseActionId = await createCaseActions({​​​​​​​ clientId: this.recordId,caseId: csId, planId: this.planId, callActivity: this.callActivity, callType: this.callType }​​​​​​​)

                console.log('onCreateCaseAction save : ' + caseActionId);

                const caseActionIdStr = JSON.stringify(caseActionId)

                console.log('onCreateCaseActionstr : ' + caseActionIdStr);
 
                let caseActionIdSet = [];

                caseActionIdSet.push(caseActionId);
 
                const caseActionToBeDisplayed = await getRelatedCaseActions({​​​​​​​ caseId: csId, caseActionId: caseActionIdSet }​​​​​​​)

                console.log("onCreateCaseAction get ------: ", caseActionToBeDisplayed);
 
              //  this.caseActionList.push(caseActionToBeDisplayed);

              //  console.log("onCreateCaseAction list : ", this.caseActionList);
 
                let arrFinal = caseActionToBeDisplayed;

                console.log("onCreateCaseAction array: ", arrFinal);

                arrFinal = arrFinal.map(row => {​​​​​​​
 
                    return {​​​​​​​ PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c }​​​​​​​;
 
                }​​​​​​​);

                this.data = arrFinal;

                console.log("onCreateCaseAction final", arrFinal);

                    }​​​​​​​

                }​​​​​​​

              }​​​​​​​

              this.initloadData();

                this.callType = '';

                this.callActivity = '';

                this.planId = '';

            //}​​​​​​​

        }​​​​​​​ catch (error) {​​​​​​​

            console.error(error);

        }​​​​​​​
 
    }​​​​​​​
 
    handleCommentsChange(event) {​​​​​​​

        this.comment = event.target.value;

        this.checkSubmit=false;

    }​​​​​​​
 
    async onCaseSave() {​​​​​​​

        try {​​​​​​​
 
            //Validate Comments

            if (!this.comment) {​​​​​​​

                this.commentError = 'Comment is required.';

            }​​​​​​​ else {​​​​​​​

                this.commentError = '';

            }​​​​​​​

            if (!this.callerType) {​​​​​​​

                this.callerTpeError = 'callerType is required.';

            }​​​​​​​ else {​​​​​​​

                this.callerTpeError = '';

            }​​​​​​​

            if (!this.caseOrigin) {​​​​​​​

                this.caseOriginError = 'caseOrigin is required.';

            }​​​​​​​ else {​​​​​​​

                this.caseOriginError = '';

            }​​​​​​​

            if (!this.commentError && !this.callerTpeError && !this.caseOriginError) {​​​​​​​

                this.checkSubmit=true;
 
                const csId = await getCaseId({​​​​​​​ clientId: this.recordId }​​​​​​​);

                this.CseNumber =csId;

                console.log('Oncase save', csId);

                if(csId  === null){​​​​​​​

                    const NewcaseId = await createNewCase({​​​​​​​ clientId: this.recordId, planId: this.planId, callActivity: this.callActivity, callType: this.callType }​​​​​​​)

                    const rescase = await getCurrentCase({​​​​​​​ Id: NewcaseId }​​​​​​​);

                    if(rescase.length==1){​​​​​​​

                        this.caseNum = rescase[0].CaseNumber;

                        this.CseNumber = rescase[0].Id;                    

                        const res = await updateCase({​​​​​​​ Id: this.CseNumber, Comment: this.comment, callerType: this.callerType, origin: this.caseOrigin }​​​​​​​);

                        const toast = await this.showToast(res);                    

                        this.dispatchEvent(new CustomEvent('recordChange'));

                    }​​​​​​​
 
                   //}​​​​​​​

                }​​​​​​​

                else{​​​​​​​                      

                    const res = await updateCase({​​​​​​​ Id: this.CseNumber, Comment: this.comment, callerType: this.callerType, origin: this.caseOrigin }​​​​​​​);

                    console.log('inside onCaseSave this.callerType', this.callerType);

                    console.log('inside onCaseSave this.caseOrigin', this.caseOrigin);

                    console.log('inside onCaseSave tres', res);

                    //  this.callerType = 'Participant';

                    // this.caseOrigin = 'Phone';

                    // this.comment = '';

                    const toast = await this.showToast(res);

                    // const re = await this.reset();

                    this.dispatchEvent(new CustomEvent('recordChange'));

                }​​​​​​​  

            }​​​​​​​

        }​​​​​​​ catch (e) {​​​​​​​

            console.log("error ", e);

            this.showToast(e);

        }​​​​​​​

    }​​​​​​​

    reset() {​​​​​​​
 
        this.refs.textArea.value = '';

    }​​​​​​​
 
    showToast(comm) {​​​​​​​

        const event = new ShowToastEvent({​​​​​​​

            title: 'The below comment has been added to the Case',

            message: comm,

            variant: 'success',

            mode: 'dismissable'

        }​​​​​​​);

        this.dispatchEvent(event);

    }​​​​​​​

    async handleSubscribe2() {​​​​​​​

        console.log('handleSubscribe2 started');

       /*if (this.subscription) {​​​​​​​

            console.log('Inside IF');

            return;

        }​​​​​​​*/

            if (this.subscription) {​​​​​​​

                return;

            }​​​​​​​

            this.subscription = subscribe(

                this.messageContext,

                PAAGTOCASEACTION_MESSAGE_CHANNELL,

                (message) => this.displayMessage(message)

            );  

        const csId = await getCaseId({​​​​​​​ clientId: this.recordId }​​​​​​​);

        console.log(' case id', csId);

         //this.caseIds = csId;        

         let caseActionIdSet = [];

      //  caseActionIdSet.push(message.caseAction);

         const caseActionToBeDisplayed = await getRelatedCaseActions({​​​​​​​ caseId: csId, caseActionId: caseActionIdSet }​​​​​​​)

         console.log("handle caseaction get ------: ", caseActionToBeDisplayed);

         let arrFinal = caseActionToBeDisplayed;

         console.log("handle CaseAction array: ", arrFinal);

        if(arrFinal != null ){​​​​​​​

         arrFinal = arrFinal.map(row => {​​​​​​​

             return {​​​​​​​ PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c }​​​​​​​;

         }​​​​​​​);

         this.data = arrFinal;

         console.log("handle CaseAction final", arrFinal);

       }​​​​​​​

    }​​​​​​​

    /*unsubscribe() {​​​​​​​

        unsubscribe(this.subscription);

        this.subscription = null;

    }​​​​​​​*/

    async displayMessage(message) {​​​​​​​

        // this.receivedMessage = message ? JSON.stringify(message, null, '\t') : 'no message payload';

        const csId = await getCaseId({​​​​​​​ clientId: this.recordId }​​​​​​​);

     console.log('res1 after updating the case', csId);

      //this.caseIds = csId;        

      let caseActionIdSet = [];

     caseActionIdSet.push(message.caseAction);
 
      const caseActionToBeDisplayed = await getRelatedCaseActions({​​​​​​​ caseId: csId, caseActionId: caseActionIdSet }​​​​​​​)

      console.log("onload caseaction get ------: ", caseActionToBeDisplayed);

      let arrFinal = caseActionToBeDisplayed;

      console.log("onload CaseAction array: ", arrFinal);

     if(arrFinal != null ){​​​​​​​

      arrFinal = arrFinal.map(row => {​​​​​​​
 
          return {​​​​​​​ PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c }​​​​​​​;
 
      }​​​​​​​);

      this.data = arrFinal;

      console.log("onload CaseAction final", arrFinal);

    }​​​​​​​

    }​​​​​​​
 
    async connectedCallback() {​​​​​​​  

        //calling the casaction from controller

        const csId = await getCaseId({​​​​​​​ clientId: this.recordId }​​​​​​​);              

         let caseActionIdSet = [];      

         const caseActionToBeDisplayed = await getRelatedCaseActions({​​​​​​​ caseId: csId, caseActionId: caseActionIdSet }​​​​​​​)

         console.log("Connected caseaction get ------: ", caseActionToBeDisplayed);      

         let arrFinal = caseActionToBeDisplayed;        

         console.log("connected CaseAction array: ", arrFinal);

        if(arrFinal != null ){​​​​​​​

         arrFinal = arrFinal.map(row => {​​​​​​​  

             return {​​​​​​​ PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c }​​​​​​​;

         }​​​​​​​);

         this.data = arrFinal;

         console.log("connected CaseAction final", arrFinal);

       }​​​​​​​
 
        //End

       this.handleSubscribe2();

        // Parse the URL and get the 'passedValue' parameter

        console.log('checking profileid  ', profileid);

        var attached = '[' + profileid + ']';

        console.log('Attached profileid ', attached);

        console.log("Columns stringified 1st  OOOOO--in connectedcallback csalwc", this.recordId);

        try {​​​​​​​

            const urlParams = new URLSearchParams(window.location.search);

            this.passedValue = urlParams.get('passedValue') || 'No value passed';
 
            this.clientSSN = urlParams.get('clientSSN');

            this.ctiVRUApp = urlParams.get('ctiVRUApp');

            this.Id = urlParams.get('Id');
 
            const res1 = await getCaseId({​​​​​​​ clientId: this.recordId }​​​​​​​);

            console.log('res1', res1);

            const res = await getCurrentCase({​​​​​​​ Id: res1 }​​​​​​​);

            console.log('res', res);

            if(res.length==1){​​​​​​​          

            this.caseNum = res[0].CaseNumber;

            }​​​​​​​

            // this.callerType = res[0].Caller_Type__c;

            //this.caseOrigin =   res[0].Origin;
 
 

            const result = await getPlans({​​​​​​​ clientId: this.recordId }​​​​​​​);

            console.log('plan result now', result);

            if (result.length ==2) {​​​​​​​

                console.log('Array size --', result.length);

                this.planId = result[0].planId;

            }​​​​​​​

            console.log('planid in caseaction',this.planId)


           this.planIds = result.map(plan => {​​​​​​​

                return {​​​​​​​

                    label: plan.planId,

                    value: plan.planId //JSON.stringify(plan)

                }​​​​​​​;

            }​​​​​​​);

            this.planIds.reverse()

            console.log('planIds in caseaction',this.planIds)

        }​​​​​​​ catch (e) {​​​​​​​

            console.log("error  ", e);
 
        }​​​​​​​

       // this call initial load on case action

        this.initloadData()

    }​​​​​​​
 
    //Code for Add more rows logic -->

    initloadData() {​​​​​​​        

        let CaseactionList = [];

        this.createRow(CaseactionList);

        this.CaseactionList = CaseactionList;

    }​​​​​​​
 
    // method to add new row

    createRow(CaseactionList) {​​​​​​​        

        let CActionObject = {​​​​​​​}​​​​​​​;

        if(CaseactionList.length > 0) {​​​​​​​

            CActionObject.index = CaseactionList[CaseactionList.length - 1].index + 1;

        }​​​​​​​ else {​​​​​​​

            CActionObject.index = 1;

        }​​​​​​​

        if(this.planId !='' && this.planId != '' && this.planId != undefined)

            CActionObject.planId = 'None';

        else

        CActionObject.planId = 'None';

        CActionObject.callActivity = '';

        CActionObject.callType = '';

        CActionObject.ShowCallTypeDropDown = true;

        CaseactionList.push(CActionObject);

    }​​​​​​​

    /**

     * Adds a new row

     */

    addNewRow() {​​​​​​​

        this.planIdError='';

        this.callActivityError='';

        this.callTypeError='';

        for(let i = 0; i < this.CaseactionList.length; i++) {​​​​​​​

            if((this.CaseactionList[i].planId == null || this.CaseactionList[i].planId == ''))

                this.planIdError= 'Plan Id is required';

            if((this.CaseactionList[i].callActivity == null || this.CaseactionList[i].callActivity == ''))

                this.callActivityError = 'Call Activity is required'

            if((this.CaseactionList[i].callType == null || this.CaseactionList[i].callType == ''))

                this.callTypeError = 'Call Type is required.';
 
        }​​​​​​​

        if(this.planIdError == '' && this.callActivityError =='' && this.callTypeError == ''){​​​​​​​

            this.createRow(this.CaseactionList);  

        }​​​​​​​  
 
        console.log('In Call Type Change'+JSON.stringify(this.CaseactionList))

    }​​​​​​​

    /**

     * Removes the selected row

     */

    removeRow(event) {​​​​​​​

        let toBeDeletedRowIndex = event.target.name;

        let CaseactionList = [];

        if(this.CaseactionList.length != 1){​​​​​​​

            for(let i = 0; i < this.CaseactionList.length; i++) {​​​​​​​

                let tempRecord = Object.assign({​​​​​​​}​​​​​​​, this.CaseactionList[i]); //cloning object

                if(tempRecord.index !== toBeDeletedRowIndex) {​​​​​​​

                    CaseactionList.push(tempRecord);

                }​​​​​​​

            }​​​​​​​

            for(let i = 0; i < CaseactionList.length; i++) {​​​​​​​

                CaseactionList[i].index = i + 1;

            }​​​​​​​

            this.CaseactionList = CaseactionList;

        }​​​​​​​

    }​​​​​​​

    /**

     * Removes all rows

     */

    removeAllRows() {​​​​​​​

        let CaseactionList = [];

        this.createRow(CaseactionList);

        this.CaseactionList = CaseactionList;

    }​​​​​​​

    createCaseActionsv1() {​​​​​​​

        console.log('FINAL Case Action List',JSON.stringify(this.CaseactionList))

    }​​​​​​​

 
 
}​​​​​​​
 








<template>
          
    <!--    <lightning-card title="Current Case/Call Tracking" icon-name="standard:case" >-->
        <lightning-card icon-name="standard:case" >
                <h1 slot="title" class="no-wrap">Current Case : {​​​​​​caseNum}​​​​​​</h1>
        <!--        <div  class="slds-grid slds-border_top "> -->
                        
                    
                        <div class="slds-p-horizontal_xxx-small">
                                
                    <!--                <div>   <h2 slot="title" class="" style="background-color:#F0F0F0;">
                                                <font size="3"> <strong class="customm-class">{​​​​​​caseNum}​​​​​​</strong>
                                                </font> </h2>
                                </div>-->
                                <lightning-card  >
                                        <div>   <h2 slot="title" class="" style="background-color:#F0F0F0;">
                                                <font size="3"> <strong class="customm-class">Case Information </strong>
                                                </font> </h2>
                                        </div>
                                        <div class="demo-only demo-only--sizing slds-grid slds-wrap slds-m-top_xx-small">
                                                <div class="slds-size_1-of-2">
                                                        <div class="slds-m-horizontal_x-small " >
                                                            <lightning-combobox required="true" onchange={​​​​​​handleCallerTypeChange}​​​​​​  name="CallerType" label="Caller Type" value={​​​​​​callerType}​​​​​​ options={​​​​​​callerTypeOptions}​​​​​​ ></lightning-combobox>    
                                                            <div class="error">{​​​​​​callerTpeError}​​​​​​</div>
                                                                <!--<lightning-input label="Caller Type" value={​​​​​​callerType}​​​​​​> </lightning-input>-->
                                                        </div>
                                                </div>
                                                <div class="slds-size_1-of-2">
                                                        <div class="slds-m-horizontal_x-small">
                                                            <lightning-combobox required="true" onchange={​​​​​​handleCaseOriginChange}​​​​​​  name="CaseOrigin" label="Case Origin" value={​​​​​​caseOrigin}​​​​​​  options={​​​​​​caseOriginOptions}​​​​​​ ></lightning-combobox>    
                                                            <div class="error">{​​​​​​caseOriginError}​​​​​​</div>
                                                            <!--<lightning-input label="Case Origin" value={​​​​​​caseOrigin}​​​​​​> </lightning-input>-->
                                                        </div>
                                                </div>
                                        </div>
                                        <div class="slds-m-horizontal_x-small">
                                                <lightning-textarea lwc:ref="textArea" required="true" name="input1" label="Comments" onchange={​​​​​​handleCommentsChange}​​​​​​  value={​​​​​​comment}​​​​​​ ></lightning-textarea>
                                                <div class="error">{​​​​​​commentError}​​​​​​</div>
                                        </div>
                                        <div class="slds-m-top_small slds-m-horizontal_xxxx-small " >
                                                <lightning-button  variant="brand" label="Save" title="Save" onclick={​​​​​​onCaseSave}​​​​​​ disabled={​​​​​​checkSubmit}​​​​​​ class="slds-m-horizontal_x-small"></lightning-button>
                                        </div>
                                </lightning-card>
                                <lightning-card >
                                        <div>   <h2 slot="title" class="slds-m-top_medium" style="background-color:#F0F0F0;">
                                                <font size="3"> <strong class="customm-class">Action Information </strong>
                                                </font> </h2>
                                        </div>
                                        <!-- Table -->
                                         <div class="slds-align_absolute-center" style="color:red;">
                                            {​​​​​​planIdError}​​​​​​<br/>
                                            {​​​​​​callActivityError}​​​​​​<br/>
                                            {​​​​​​callTypeError}​​​​​​
                                         </div>
        <table class="slds-table slds-table_bordered slds-no-row-hover slds-table_cell-buffer" role="grid">
            <thead>
            <tr>
                <th scope="col" height="22">Plan Id</th>
                <th scope="col" height="22">Call Activity</th>
                <th scope="col" height="22">Call Type</th>
                <th scope="col" height="22" style="width: 3rem"></th>
            </tr>
            </thead>
            <tbody>
            <template for:each={​​​​​​CaseactionList}​​​​​​ for:item="rec" >
                <tr key={​​​​​​rec}​​​​​​ class="slds-hint-parent">                    
                    <td>
                        <lightning-combobox name="planId" variant="label-hidden" label="Plan Ids" data-id={​​​​​​rec.index}​​​​​​ value={​​​​​​rec.planId}​​​​​​ options={​​​​​​planIds}​​​​​​ 
                                                                                                        onchange={​​​​​​handlePlanIdsChange}​​​​​​ required="true"></lightning-combobox>                        
                    </td>
                    <td>
                        <lightning-combobox name="callActivity" variant="label-hidden" label="Call Activities" data-id={​​​​​​rec.index}​​​​​​ value={​​​​​​rec.callActivity}​​​​​​ options={​​​​​​callActivitiesOptions}​​​​​​
                                                                                                        onchange={​​​​​​handlecallActivitiesChange}​​​​​​ required="true"></lightning-combobox>
                        
                    </td>
                    <td>
                        
                        <template if:true={​​​​​​rec.ShowCallTypeDropDown}​​​​​​>
                        <lightning-combobox name="callType" variant="label-hidden" label="Call Types" data-id={​​​​​​rec.index}​​​​​​ data-inputable="true" value={​​​​​​rec.callType}​​​​​​ options={​​​​​​callTypesOptions}​​​​​​
                                                                                                        onchange={​​​​​​handlecallTypesChange}​​​​​​ required="true"></lightning-combobox>
                                                                                                        
                        </template>
                        <template if:false={​​​​​​rec.ShowCallTypeDropDown}​​​​​​>
                            <lightning-input type="text" variant="label-hidden" label="" data-id={​​​​​​rec.index}​​​​​​ name="callType" value={​​​​​​rec.callType}​​​​​​></lightning-input>
                        </template>
                                                                                                    </td>
                    <td>
                        <div class="slds-p-left_small slds-p-vertical_small">
                        <lightning-icon icon-name="action:new" alternative-text="Add Row" access-key={​​​​​​rec.index}​​​​​​ id={​​​​​​rec.index}​​​​​​ name={​​​​​​rec.index}​​​​​​ title="Add Row" onclick={​​​​​​addNewRow}​​​​​​ size="xx-small" ></lightning-icon>
                        <lightning-icon icon-name="action:delete" alternative-text="Delete Row"  access-key={​​​​​​rec.index}​​​​​​ id={​​​​​​rec.index}​​​​​​ name={​​​​​​rec.index}​​​​​​ title="Delete Row" onclick={​​​​​​removeRow}​​​​​​ size="xx-small"></lightning-icon>
                        <!--<lightning-button-icon icon-name="utility:delete" alternative-text="Remove" title="Remove" name={​​​​​​rec.index}​​​​​​ onclick={​​​​​​removeRow}​​​​​​></lightning-button-icon>-->
                    </div>
                    </td>
                </tr>
            </template>
            </tbody>
        </table>
        
                                        <div class="demo-only demo-only--sizing slds-grid slds-wrap slds-m-top_xx-small"> 
                                                <!--<div class="slds-size_1-of-3">
                                                        <div class="slds-m-horizontal_x-small">
                                                                <lightning-combobox name="planId" label="Plan Ids" value={​​​​​​planId}​​​​​​ options={​​​​​​planIds}​​​​​​ 
                                                                                                        onchange={​​​​​​handlePlanIdsChange}​​​​​​ required="true"></lightning-combobox>
                                                                <div class="error">{​​​​​​planIdError}​​​​​​</div>
                                                        </div>
                                                </div>
                                                <div class="slds-size_1-of-3">
                                                        <div class="slds-m-horizontal_x-small">
                                                                <lightning-combobox name="callActivity" label="Call Activities" value={​​​​​​callActivity}​​​​​​ options={​​​​​​callActivitiesOptions}​​​​​​
                                                                                                        onchange={​​​​​​handlecallActivitiesChange}​​​​​​ required="true"></lightning-combobox>
                                                                <div class="error">{​​​​​​callActivityError}​​​​​​</div>
                                                        </div>
                                                </div>
                                                <div class="slds-size_1-of-3">
                                                        <div class="slds-m-horizontal_x-small">
                                                                <lightning-combobox name="callType" label="Call Types" value={​​​​​​callType}​​​​​​ options={​​​​​​callTypesOptions}​​​​​​
                                                                                                        onchange={​​​​​​handlecallTypesChange}​​​​​​ required="true"></lightning-combobox>
                                                                <div class="error">{​​​​​​callTypeError}​​​​​​</div>
                                                        </div>
                                                </div>-->
                                                <div class="slds-m-top_small slds-m-horizontal_xxxx-small">
                                                        <lightning-button variant="brand" label="Create Case Action" title="Create Case Action" onclick={​​​​​​onCreateCaseAction}​​​​​​ class="slds-m-horizontal_x-small"></lightning-button>
                                                </div>
                                        </div>
                                        <div style="height: 200px;"> <div class="slds-m-top_small slds-m-horizontal_x-small">
                                                <lightning-datatable
                                                                                         key-field="Id"
                                                                                         data={​​​​​​data}​​​​​​
                                                                                         columns={​​​​​​columns}​​​​​​
                                                                                         hide-checkbox-column="true">
                                                </lightning-datatable>
                                                </div>  </div>
                                </lightning-card>
                        </div>
        <!--        </div>      --> 
        </lightning-card>
</template>
 
