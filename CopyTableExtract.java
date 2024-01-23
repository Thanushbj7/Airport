import { LightningElement, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import getObject from '@salesforce/apex/CaseRelatedListApex.getObject';

const columns = [
    { label: 'Case Number', fieldName: 'CaseNumber' },
    { label: 'Date', fieldName: 'CreatedDate' },
    { label: 'Plan Id', fieldName: 'PlanID_Text__c' },
    { label: 'Inquiry', fieldName: 'Call_Type__c' },
    { label: 'Transactions', fieldName: 'Call_Type__c' },
    { label: 'Account Maintenance', fieldName: 'Call_Type__c' },
    { label: 'Forms', fieldName: 'Call_Type__c'},
    { label: 'Others', fieldName: 'Call_Type__c'},
];

export default class CaseHistoryLWC extends LightningElement {
    @wire(MessageContext) messageContext;
    @wire(getObject) wiredCases;

    get data() {
        if (this.wiredCases.data) {
            return this.wiredCases.data.map(row => ({
                ...row,
                CaseNumber: row.Case__r.CaseNumber,
                CreatedDate: row.Case__r.CreatedDate
            }));
        }
        return [];
    }

    get columns() {
        return columns;
    }

    connectedCallback() {
        // ... existing code
    }

    sendMessage() {
        // ... existing code
    }

    isInquiry(callActivity) {
        return callActivity === 'Inquiry';
    }
  }
