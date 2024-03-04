// ... (existing code)

<lightning-tabset>
    <lightning-tab label="Available Targeted Messages">
        <!-- Existing content for Available Targeted Messages tab -->
    </lightning-tab>
    <lightning-tab key="detailTab" label="Detail" if:true={isDetailTabVisible}>
        <!-- Content for the Detail tab -->
    </lightning-tab>
</lightning-tabset>





handleOfferNameClick(event) {
    // Prevent the default behavior of the click event (opening the URL)
    event.preventDefault();

    // Set a property to indicate that the detail tab should be visible
    this.isDetailTabVisible = true;

    // Dynamically create a new tab
    this.createNewTab('Detail', 'Detail Tab Label');
}

createNewTab(tabName, tabLabel) {
    const tabset = this.template.querySelector('lightning-tabset');

    if (tabset) {
        const newTab = document.createElement('lightning-tab');
        newTab.label = tabLabel;
        newTab.key = tabName;

        // You can add additional content or components to the new tab as needed

        // Append the new tab to the existing tabset
        tabset.appendChild(newTab);
    }
}

// ... (existing code)






// NewTabHandler.js
import { LightningElement, wire } from 'lwc';
import { subscribe, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';

export default class NewTabHandler extends LightningElement {
    @wire(MessageContext)
    messageContext;

    subscription;

    connectedCallback() {
        // Subscribe to the message channel
        this.subscription = subscribe(
            this.messageContext,
            EXAMPLE_MESSAGE_CHANNEL,
            (message) => this.handleMessage(message)
        );
    }

    handleMessage(message) {
        if (message.createNewTab) {
            this.createNewTab(message.tabName, message.tabLabel);
        }
    }

    createNewTab(tabName, tabLabel) {
        // Logic to dynamically create a new tab in your component
        // You can use a similar approach as mentioned in the previous response
    }

    disconnectedCallback() {
        // Unsubscribe from the message channel when the component is destroyed
        unsubscribe(this.subscription);
    }
}






// Import the message service and your message channel
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';

// ...

handleOfferNameClick(event) {
    // Prevent the default behavior of the click event (opening the URL)
    event.preventDefault();

    // Set a property to indicate that the detail tab should be visible
    this.isDetailTabVisible = true;

    // Publish a message to indicate that a new tab should be created
    const messagePayload = {
        createNewTab: true,
        tabName: 'Detail',
        tabLabel: 'Detail Tab Label'
    };

    publish(this.messageContext, EXAMPLE_MESSAGE_CHANNEL, messagePayload);
}






<lightning-tabset>
    <lightning-tab label="Available Targeted Messages">
        <!-- Existing content for Available Targeted Messages tab -->
    </lightning-tab>
    <lightning-tab key="detailTab" label="Detail" if:true={isDetailTabVisible}>
        <!-- Content for the Detail tab -->
    </lightning-tab>
</lightning-tabset>





// Add this property
@track isDetailTabVisible = false;

// ...

handleOfferNameClick(event) {
    // Prevent the default behavior of the click event (opening the URL)
    event.preventDefault();

    // Set a property to indicate that the detail tab should be visible
    this.isDetailTabVisible = true;

    // Add logic to dynamically create a new tab
    this.createNewTab('Detail', 'Detail Tab Label');
}

createNewTab(tabName, tabLabel) {
    const tabset = this.template.querySelector('lightning-tabset');

    if (tabset) {
        const newTab = document.createElement('lightning-tab');
        newTab.label = tabLabel;
        newTab.key = tabName;

        // You can add additional content or components to the new tab as needed

        // Insert the new tab next to the "Available Targeted Messages" tab
        const availableTab = tabset.querySelector('[label="Available Targeted Messages"]');
        if (availableTab) {
            const tabIndex = Array.from(tabset.children).indexOf(availableTab);
            tabset.insertBefore(newTab, tabset.children[tabIndex + 1]);
        }
    }
}






{ label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra', type: 'url', typeAttributes: { label: { fieldName: 'offerPlanName' }, onclick: 'handleOfferNameClick' } },

handleOfferNameClick(event) {
    // Prevent the default behavior of the click event (opening the URL)
    event.preventDefault();

    // Set a property to indicate that the detail tab should be visible
    this.isDetailTabVisible = true;
}

<lightning-tabset>
    <lightning-tab label="Available Targeted Messages">
        <!-- Existing content for Available Targeted Messages tab -->
    </lightning-tab>
    <lightning-tab key="detailTab" label="Detail" if:true={isDetailTabVisible}>
        <!-- Content for the Detail tab -->
    </lightning-tab>
</lightning-tabset>





<template>
    <div class="slds-card" style="height:400px; width:610px">
            <lightning-card title="Targeted Messages" icon-name="standard:case">

                    <h2 slot="title">
                    <lightning-icon icon-name="action:new_note"   size="small" title=" Targeted Message "></lightning-icon>
                    <b>Targeted Message</b>
            </h2>
                    

                    <div class="slds-p-horizontal_xx-small">
                            <lightning-tabset>
                                    <lightning-tab label="Available Targeted Messages">
                                            <div class="demo-only demo-only--sizing slds-grid slds-wrap"> 
                                                    
                                            </div>
                                            <div style="height: 200px;"> <div class="slds-m-top_small slds-m-horizontal_x-small">
                                                <c-clickable-mess-nam
                                                     key-field="Id"
                                                     data={data}
                                                     columns={columns}>
                                                </c-clickable-mess-nam>
                                            </div> 
                                            </div>
                                    </lightning-tab>
                                    <lightning-tab label="Detail">

                                    </lightning-tab>


                                    
                            </lightning-tabset>
                    </div>
            </lightning-card>
    </div>

    
</template>







                                                         import { LightningElement, api, track, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import fetchWrapperCases from '@salesforce/apex/caseHistoryLWC.fetchWrapperCases';
import initClientOffers from '@salesforce/apex/TargetedMessage.initClientOffers';
const columns = [
    //{ label: 'PlanId', fieldName: 'linkName', type:'url' ,typeAttributes: { label: {fieldName: 'caseNumber'}, target:'_blank'}},
    //{ label: 'Date', fieldName: 'createdDate' },
    { label: 'PlanId', fieldName: 'offerPlanId' },
    { label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra', type: 'url', typeAttributes: { label: { fieldName: 'offerPlanName' } } },
    { label: 'Active Mailer', fieldName: 'activeMailer', columnKey: 'accM' },
    { label: 'Message Name', fieldName: 'offerName', type: 'url', typeAttributes: { label: { fieldName: 'offerName' } } },



];

export default class CTargetedMessage extends LightningElement {
    isvisible=false;
    @track data = [];
    @track columns = columns;
    wiredRecords;
    handleclick(){
        this.isvisible=true;
    }
    connectedCallback() {
        // Parse the URL and get the 'passedValue' parameter				 
        const urlParams = new URLSearchParams(window.location.search);
        this.clientSSN = urlParams.get('clientSSN');
        console.log('ssn should appear here' + this.clientSSN);
        console.log('inside connected callback');
    }






    @wire(initClientOffers, { clientSSN: '$clientSSN' }) wiredCases(value) {
        //console.log("Columns stringified 1st  --" , this.columns);
        this.wiredRecords = value;
        const { data, error } = value;
        if (data) {
            this.data = data;

        }

    }
}
