<template>
    <lightning-card title="Client/Entity Search">
        <!-- Your component content here -->
    </lightning-card>
</template>





	import { LightningElement, wire } from 'lwc';
import { NavigationMixin } from 'lightning/navigation';
import { loadScript } from 'lightning/platformResourceLoader';
import jquery from '@salesforce/resourceUrl/jquery';
import { getRecord } from 'lightning/uiRecordApi';
import initializeOfferPop from '@salesforce/apex/I_SearchController.initializeOfferPop';
import { ShowToastEvent } from 'lightning/platformShowToastEvent';

export default class ClientSearch extends NavigationMixin(LightningElement) {
    @wire(initializeOfferPop)
    offerPopObject;

    renderedCallback() {
        if (this._isRendered) return;
        this._isRendered = true;

        Promise.all([
            loadScript(this, jquery),
            loadScript(this, '/support/console/40.0/integration.js'),
            loadScript(this, '../../soap/ajax/32.0/connection.js')
        ])
        .then(() => {
            this.initializeConsole();
        })
        .catch(error => {
            this.dispatchEvent(
                new ShowToastEvent({
                    title: 'Error loading resources',
                    message: error.message,
                    variant: 'error',
                }),
            );
        });
    }

    initializeConsole() {
        if (sforce.console.isInConsole()) {
            setConsoleFlag();
            try {
                sforce.console.getEnclosingPrimaryTabId((primarytab) => {
                    if (primarytab && primarytab.id) {
                        sforce.console.setTabTitle('Client Search', primarytab.id);
                    }
                });
            } catch (err) {
                console.error(err);
            }
        }

        document.addEventListener('keypress', this.handleKeyPress.bind(this));
    }

    handleKeyPress(event) {
        if (event.keyCode === 13) {
            const searchButton = this.template.querySelector('[data-id="CS_SEARCH_BUTTON"]');
            if (searchButton) {
                searchButton.click();
            }
            event.preventDefault();
        }
    }

    goToDetailPage4Console(hasClientSummAccess, ssn, dnis, source, vruApp, eduId, caseId, AuthenticatedFlag, caseorigin, clientType) {
        let url;
        if (hasClientSummAccess) {
            url = `/apex/RRPage?fromSearch=1&clientID=${ssn}&DNIS=${dnis}&Source=${source}&vruApp=${vruApp}&ctiEDU=${eduId}&AuthenticatedFlag=${AuthenticatedFlag}&type=${caseorigin}&caseId=${caseId}&opID=${this.offerPopObject.data.Id}&clientType=${clientType}`;
        } else {
            url = `/apex/CTIPage?fromSearch=1&clientID=${ssn}&DNIS=${dnis}&Source=${source}&vruApp=${vruApp}&ctiEDU=${eduId}&AuthenticatedFlag=${AuthenticatedFlag}&type=${caseorigin}&caseId=${caseId}&opID=${this.offerPopObject.data.Id}&clientType=${clientType}`;
        }

        if (sforce.console.isInConsole()) {
            url += '&open_in_console=on';
            try {
                sforce.console.getEnclosingPrimaryTabId((primarytab) => {
                    if (primarytab && primarytab.id) {
                        sforce.console.openPrimaryTab(primarytab.id, url, true);
                    } else {
                        sforce.console.openPrimaryTab(null, url, true);
                    }
                });
            } catch (err) {
                console.error(err);
            }
        } else {
            url += '&open_in_console=off';
            window.open(url, '_self');
        }

        return false;
    }
																					  }
