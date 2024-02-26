import { LightningElement, wire, track, api } from 'lwc';
import { subscribe, MessageContext, unsubscribe } from 'lightning/messageService';
import { ShowToastEvent } from 'lightning/platformShowToastEvent';
import { getRecord, getFieldValue } from 'lightning/uiRecordApi';
import { NavigationMixin } from 'lightning/navigation';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import getKnowledgeArticles from '@salesforce/apex/lightningPopController.getKnowledgeArticles';
import chChannel from '@salesforce/messageChannel/chChannel__c';



export default class subscriberLWC extends NavigationMixin(LightningElement) {
    @track receivedValue = '';
    @track subscription = null;
    @wire(MessageContext)
    messageContext;
    selectedKARecordId;
    CueInData;
    divHeight;
    displayKADetailModal = false;
    clientStatus;
    isLoading = false;
    @track currentPage = 1;
    pageSize = 10; // Number of items per page
    callTypeInquiry = '';
    subscription = null;
    @wire(MessageContext) messageContext;

    connectedCallback() {
        this.handleSubscribe();
    }
    disconnectedCallback() {
        this.handleUnsubscribe();
    }
    handleSubscribe() {
        if (!this.subscription) {
            subscription = subscribe(this.messageContext, chChannel,
                (parameter) => {
                    this.callTypeInquiry =;
                }
            );
        }
    }
    handleUnsubscribe() {
        unsubscribe(this.subscription);
        this.subscription = null;
    }


    get items() {
        if (this.CueInData) {
            const startIndex = (this.currentPage - 1) * this.pageSize;
            const endIndex = this.currentPage * this.pageSize;
            return this.CueInData.slice(startIndex, endIndex);
        }
        return [];
    }

    // Getter to calculate the total number of pages
    get isPrevDisabled() {
        return this.currentPage <= 1 || !this.CueInData || this.CueInData.length === 0;
    }

    get isNextDisabled() {
        return !this.CueInData || this.currentPage >= this.totalPages || this.CueInData.length === 0;
    }

    get totalPages() {
        return this.CueInData ? Math.ceil(this.CueInData.length / this.pageSize) : 0;
    }
    connectedCallback() {
        this.isLoading = true;
        console.log('Connect')
        this.divHeight = "height: 20 rem;";
        getKnowledgeArticles({
                receivedValue: this.receivedValue,
                clientStatus: this.clientStatus
            })
            .then(result => {
                // Handle the result from the Apex method
                console.log('Apex Method Result:', result);
                this.CueInData = result
                this.currentPage = 1
                this.isLoading = false;
                // Add your logic to handle the result as needed
            })
            .catch(error => {
                console.error('Error calling Apex method:', error);
                this.showToast('Error', 'Error calling Apex method', 'error');
            });
        this.subscribeToMessageChannel();
    }

    subscribeToMessageChannel() {
        console.log('Here')
        try {
            this.subscription = subscribe(
                this.messageContext,
                EXAMPLE_MESSAGE_CHANNEL,
                (message) => this.handleMessage(message)
            );
        } catch (error) {
            console.log('Error ' + error)
        }
    }

    handleMessage(message) {
        console.log('Message ' + message)
        this.isLoading = true;
        this.receivedValue = message.message;
        this.clientStatus = message.status;
        getKnowledgeArticles({
                receivedValue: this.receivedValue,
                clientStatus: this.clientStatus
            })
            .then(result => {
                // Handle the result from the Apex method
                console.log('Apex Method Result:', result);
                this.CueInData = result
                this.isLoading = false;
                // Add your logic to handle the result as needed
            })
            .catch(error => {
                console.error('Error calling Apex method:', error);
                this.isLoading = false;
                // this.showToast('Error', 'Error calling Apex method', 'error');
            });
    }
    navigateToKARecordView(event) {
        console.log('Navigate')
        this.selectedKARecordId = event.target.name;
        const eventaura = new CustomEvent('recordidpassed', {
            detail: { recordId: this.selectedKARecordId }
        });
        this.dispatchEvent(eventaura);


        // Navigate to the external URL

        console.log('Line 58 ' + this.selectedKARecordId)
        try {
            this.selectedKARecordId = event.target.name;
            this[NavigationMixin.Navigate]({
                type: "standard__recordPage",
                attributes: {
                    recordId: this.selectedKARecordId,
                    objectApiName: "Knowledge_kav",
                    actionName: "view",
                },
            });
        } catch (error) {
            console.log('Error ' + error)
        }
    }
    openKADetailModal(event) {
        console.log('Event target ' + event.target.name)
        const DELAY_PopoverOpen = 250;
        this.selectedKARecordId = event.target.name;
        // Debouncing this method: Do not actually invoke the Apex call as long as this function is
        // being called within a delay of DELAY. This is to avoid a very large number of Apex method calls.
        window.clearTimeout(this.delayTimeout);
        // eslint-disable-next-line @lwc/lwc/no-async-operation
        this.delayTimeout = setTimeout(() => {
            this.displayKADetailModal = true;
        }, DELAY_PopoverOpen);
        console.log('KA Model ' + this.displayKADetailModal)
    }

    closeKADetailModal(event) {
        const DELAY_PopoverClose = 300;
        const kadetailLoaded = event.detail;
        // Debouncing this method: Do not actually invoke the Apex call as long as this function is
        // being called within a delay of DELAY. This is to avoid a very large number of Apex method calls.
        window.clearTimeout(this.delayTimeout);
        // eslint-disable-next-line @lwc/lwc/no-async-operation
        this.delayTimeout = setTimeout(() => {
            if (kadetailLoaded === true) {
                this.displayKADetailModal = true;
            } else if (kadetailLoaded === false) {
                this.displayKADetailModal = false;
            } else {
                this.displayKADetailModal = false;
            }
        }, DELAY_PopoverClose);
    }

    handleKnowArticleRowAction() {

    }
    handleCopyChatAnswer() {

    }

    handleNext() {
        if (this.currentPage < this.totalPages) {
            this.currentPage += 1;
        }
    }

    // Method to navigate to the previous page
    handlePrevious() {
        if (this.currentPage > 1) {
            this.currentPage -= 1;
        }
    }


}
