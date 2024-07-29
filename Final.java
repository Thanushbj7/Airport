.slds-spinner_container {
    display: flex;
    align-items: center;
}

.slds-spinner_container span {
    margin-left: 10px;
}



import { LightningElement, track } from 'lwc';

export default class ClientSearch extends LightningElement {
    @track ssn = '';
    @track guestLast4SSN = '';
    @track showProcessing = false;
    @track showMessage = false;
    @track message = '';

    handleInputChange(event) {
        const field = event.target.dataset.id;
        if (field === 'ssn') {
            this.ssn = event.target.value;
        } else if (field === 'guestLast4SSN') {
            this.guestLast4SSN = event.target.value;
        }
    }

    handleSearch() {
        this.showProcessing = true;
        // Call Apex method to search accounts
        // Example: searchAccounts({ ssn: this.ssn, guestLast4SSN: this.guestLast4SSN })
        setTimeout(() => {
            this.showProcessing = false;
        }, 2000);
    }

    handleClear() {
        this.ssn = '';
        this.guestLast4SSN = '';
        this.showProcessing = true;
        // Call Apex method to reset search
        // Example: resetSearch()
        setTimeout(() => {
            this.showProcessing = false;
        }, 2000);
    }
}


<template>
    <lightning-card title="Client" icon-name="custom:custom63">
        <div slot="header">Client/Entity Search</div>
        <div class="slds-m-around_medium">
            <lightning-input label="SSN/TIN" value={ssn} onchange={handleInputChange} data-id="ssn" placeholder="i.e. 000000000 or 000-00-0000"></lightning-input>
            <p class="slds-text-align_right"><b>OR</b></p>
            <lightning-input label="Guest ID Last 4 SSN" value={guestLast4SSN} onchange={handleInputChange} data-id="guestLast4SSN" placeholder="i.e. 00000"></lightning-input>
        </div>
        <lightning-button variant="brand" label="Search" onclick={handleSearch}></lightning-button>
        <lightning-button label="Clear All" onclick={handleClear}></lightning-button>
        
        <template if:true={showProcessing}>
            <div class="slds-spinner_container">
                <div role="status" class="slds-spinner slds-spinner_medium">
                    <span class="slds-assistive-text">Loading</span>
                    <div class="slds-spinner__dot-a"></div>
                    <div class="slds-spinner__dot-b"></div>
                </div>
                <span>Processing...</span>
            </div>
        </template>
        
        <template if:true={showMessage}>
            <div class="slds-text-color_error slds-m-around_medium">
                <strong>{message}</strong>
            </div>
        </template>
    </lightning-card>
</template>








<template>
    <lightning-card title="Client" icon-name="custom:custom63">
        <div slot="header">Client/Entity Search</div>
        <lightning-button variant="brand" label="Search" onclick={handleSearch}></lightning-button>
        <lightning-button label="Clear All" onclick={handleClear}></lightning-button>
        
        <template if:true={showProcessing}>
            <div class="slds-spinner_container">
                <div role="status" class="slds-spinner slds-spinner_medium">
                    <span class="slds-assistive-text">Loading</span>
                    <div class="slds-spinner__dot-a"></div>
                    <div class="slds-spinner__dot-b"></div>
                </div>
                <span>Processing...</span>
            </div>
        </template>
        
        <template if:true={showMessage}>
            <div class="slds-text-color_error slds-m-around_medium">
                <strong>{message}</strong>
            </div>
        </template>
    </lightning-card>
</template>



	import { LightningElement, track } from 'lwc';

export default class ClientSearch extends LightningElement {
    @track showProcessing = false;
    @track showMessage = false;
    @track message = '';

    handleSearch() {
        this.showProcessing = true;
        // Call Apex method to search accounts
        // Example: searchAccounts()
        setTimeout(() => {
            this.showProcessing = false;
        }, 2000);
    }

    handleClear() {
        this.showProcessing = true;
        // Call Apex method to reset search
        // Example: resetSearch()
        setTimeout(() => {
            this.showProcessing = false;
        }, 2000);
    }
}

.slds-spinner_container {
    display: flex;
    align-items: center;
}

.slds-spinner_container span {
    margin-left: 10px;
}









<apex:sectionHeader title="Client" subtitle="Client/Entity Search"/>
<apex:form id="frm">
    <br/>
    <apex:actionFunction action="{!setConsoleFlag}" name="setConsoleFlag" rerender="dummySection2Rerender"/>
    <apex:pageBlock id="page" >       
        <apex:pageBlockButtons id="pbb" style="text-align:left;padding-left:120px;" location="top">
            <apex:actionStatus startText=" Processing the stuff " stopText=" Done " id="button1"  >
                 <apex:facet name="start" >Processing...
                           <img src="/apexpages/devmode/img/saveStatus.gif" />
                 </apex:facet>
                 <apex:facet name="stop">   
                    <apex:commandButton value="Search" action="{!searchAccounts}" status="button1" rerender="page" id="CS_SEARCH_BUTTON"/>
                 </apex:facet>
            </apex:actionStatus>
            <apex:actionStatus startText=" Processing the stuff " stopText=" Done " id="button3"  >
                 <apex:facet name="start" >Processing...
                    <img src="/apexpages/devmode/img/saveStatus.gif" />
                 </apex:facet>               
                 <apex:facet name="stop">   
                    <apex:commandButton value="Clear All" action="{!resetSearch}" status="button3" rerender="page"/>
                 </apex:facet>
            </apex:actionStatus>
        </apex:pageBlockButtons>
        <apex:pageMessages id="pageMessages"/>
        <apex:pageBlockSection columns="1" rendered="{!IF(NOT(ISNULL($CurrentPage.parameters.reason)) && ($CurrentPage.parameters.reason=='NOT-AUTHENTICATED'), true, false)}" >
            <apex:pageblocksectionitem >
                <div style="font-family:Arial,Helvetica,sans-serif;font-weight:700;font-color:#FF0000;font-size:120%;color:#FF0000;text-align:center;">
                    The caller did not provide SSN and may NOT be an existing customer.  Proceed with that knowledge and try to identify the caller using all tools available at your disposal.
                </div>
            </apex:pageblocksectionitem>
        </apex:pageBlockSection>
        <apex:pageBlockSection columns="1" rendered="{!IF(NOT(ISNULL($CurrentPage.parameters.reason)) && ($CurrentPage.parameters.reason=='NOT-AUTHENTICATED-HYBRID-RIA'), true, false)}" >
            <apex:pageblocksectionitem >
                <div style="font-family:Arial,Helvetica,sans-serif;font-weight:700;font-color:#FF0000;font-size:120%;color:#FF0000;text-align:center;">
                    The Caller is an External Registered Investment Advisor's Client, Please contact Voya Financial Advisors Resource center.
                </div>
            </apex:pageblocksectionitem>
        </apex:pageBlockSection>
        <!-- 
        <table width="100%">
            <tr colspan="2" width="100%">
                <td style="font-size:95%">&nbsp;?&nbsp;<apex:outputLink value="{!URLFOR($Action.Account.List, $ObjectType.Account)}" id="back">Back to Client List View </apex:outputLink></td>
            </tr>
        </table>
         -->
        <apex:pageBlockSection columns="3" title="Client Search Criteria" collapsible="false">
            <apex:pageblocksectionitem labelStyle="width:8%;" dataStyle="width:21%" helpText="i.e. 000000000 or 000-00-0000">
                <apex:outputLabel value="SSN/TIN" for="clientSSN"/>
                <apex:outputPanel layout="block" >
                    <apex:inputText value="{!ssn}" id="clientSSN" />
                    <p style="text-align:right"><b>OR</b></p>
                </apex:outputPanel>                                             
            </apex:pageblocksectionitem>
           <apex:pageblocksectionitem labelStyle="width:8%;" dataStyle="width:21%" helpText="i.e. 00000">
                <apex:outputLabel value="Guest ID Last 4 SSN" for="guestLast4SSN"/>
                <apex:outputPanel layout="block" >
                    <apex:inputText value="{!guestLast4SSN}" id="guestLast4SSN" />
                </apex:outputPanel>                                             
            </apex:pageblocksectionitem>
           
              <apex:pageblocksectionitem labelStyle="width:8%;" dataStyle="width:21%">
                <apex:outputLabel value=" " for="_x2"/>
                <apex:outputPanel layout="block" />
            </apex:pageblocksectionitem>
            
            <apex:pageblocksectionitem labelStyle="width:8%;" dataStyle="width:21%" helpText="Client's First Name. Enter % sign for a wildcard search of the Salesforce data only.">
                <apex:outputLabel value="First Name" for="firstName"/>
                <apex:outputPanel layout="block" >
                    <apex:inputText value="{!FirstName}" id="firstName" />
                    <p style="text-align:right"><b>OR</b></p>
                </apex:outputPanel>                                                     
            </apex:pageblocksectionitem>
            <apex:pageblocksectionitem labelStyle="width:8%;" dataStyle="width:21%" helpText="Client's Last Name. Enter % sign for a wildcard search of the Salesforce data only.">
                <apex:outputLabel value="Last Name/Entity Name" for="lastName"/>
                <apex:outputPanel layout="block" >
                    <apex:inputText value="{!LastName}" id="lastName" />
                </apex:outputPanel>                                                     
            </apex:pageblocksectionitem>
            <apex:pageblocksectionitem labelStyle="width:8%;" dataStyle="width:21%" helpText="State Abbreviation (i.e. CT for Connecticut)">
                    <apex:outputLabel value="State" for="state"/>
                    <apex:outputPanel layout="block" >
                        <apex:selectList style="vertical-align:top;" id="state" multiselect="false" value="{!state}" size="1">
                                <apex:selectOptions value="{!states}"/>                 
                              </apex:selectList>
                          </apex:outputPanel>
            </apex:pageblocksectionitem>  
            
            <apex:pageblocksectionitem labelStyle="width:8%;" dataStyle="width:21%"  helpText="Enter full email address to search for client in Salesforce or KPD">
                <apex:outputLabel value="Email" for="Email"/>
                <apex:outputPanel layout="block" >
                    <apex:inputText value="{!email}" id="email" />
                  <!--  <p style="text-align:right"><b>OR</b></p>-->
                </apex:outputPanel>  
                </apex:pageblocksectionitem>
                
             <apex:pageblocksectionitem labelStyle="width:8%;" dataStyle="width:21%" helptext="Will include results from Known Party Database (KPD) when checked" >
                <apex:outputLabel value="KPD" for="KPD"/>
                <apex:outputPanel layout="block" >
                    <apex:inputCheckbox value="{!kpdFlag}" id="kpdflag" />
                   
                </apex:outputPanel>                                                     
            </apex:pageblocksectionitem>
            
           </apex:pageBlockSection>
        <apex:pageBlockSection columns="3" collapsible="false" showHeader="false" rendered="{!IF(totalPage > 1, true, false)}" id="paginationBlock">
            <apex:pageblocksectionitem labelStyle="width:1%;" dataStyle="width:21%">
                        <apex:outputLabel value="" for="_x1" />
                        <apex:outputPanel layout="block" style="padding-left:80px;">
                    <apex:outputText value="Previous" style="font-size:95%;text-decoration:underline;color:#909090;" rendered="{!IF(pagiCount == 1, true, false)}" />
                    <apex:commandLink action="{!shiftPaginationIndex}" value="Previous" style="font-size:95%" rendered="{!IF(pagiCount != 1, true, false)}" reRender="sResults, paginationBlock, buttonsSections">
                        <apex:param name="Previous" value="Previous" assignTo="{!PagiIndex}"/>
                    </apex:commandLink>
                </apex:outputPanel>
                  </apex:pageblocksectionitem>
                  <apex:pageblocksectionitem labelStyle="width:1%;" dataStyle="width:21%">
                        <apex:outputLabel value="" for="_x1" />
                        <apex:outputPanel layout="block" style="padding-left:80px;">
                    <apex:repeat value="{!pagiList}" var="string" >
                        <apex:commandLink action="{!testPagination}" value="{!string}" rendered="{!string != pagiCount}" style="font-size:95%" reRender="sResults, paginationBlock">
                            <apex:param name="{!string}" value="{!string}" assignTo="{!pagiCount}"/>
                        </apex:commandLink> 
                        <apex:commandLink action="{!testPagination}" value="{!string}" rendered="{!string == pagiCount}" style="font-size:95%;text-decoration:none;" reRender="sResults, paginationBlock, buttonsSections">
                            <apex:param name="{!string}" value="{!string}" assignTo="{!pagiCount}"/>
                        </apex:commandLink> 
                        &nbsp;&nbsp;
                    </apex:repeat>
                </apex:outputPanel>
                  </apex:pageblocksectionitem>
            
            <apex:pageblocksectionitem labelStyle="width:1%;" dataStyle="width:21%">
                        <apex:outputLabel value="" for="_x1" />
                        <apex:outputPanel layout="block" style="padding-left:80px;">
                    <apex:commandLink action="{!shiftPaginationIndex}" value="Next" style="font-size:95%" rendered="{!IF(pagiCount != totalPage, true, false)}" reRender="sResults, paginationBlock, buttonsSections">
                        <apex:param name="Next" value="Next" assignTo="{!PagiIndex}"/>
                    </apex:commandLink>
                    <apex:outputText value="Next" style="font-size:95%;text-decoration:underline;color:#909090;" rendered="{!IF(pagiCount == totalPage, true, false)}" />
                </apex:outputPanel>
                  </apex:pageblocksectionitem>
            
        </apex:pageBlockSection>
        <apex:pageBlockSection title="Search Results" collapsible="true" columns="1" rendered="{!displaySrchResultsBlock}" id="sResults">
            <apex:pageBlockTable value="{!SearchList}" var="search" title="Search Results" rendered="{!displaySrchResultsBlock}">
                <apex:column headerValue="Last Four SSN">
                <!-- Updated to remove TIN if it contains case 24374 AND 24375 -->
                    <apex:outputText value="{!IF(CONTAINS(LOWER(search.ssn),'tin'),RIGHT(SUBSTITUTE(search.ssn,'TIN',''),4),RIGHT(search.ssn,4))}"/>
                </apex:column>              
                <apex:column headerValue="Client Name" rendered="{!islightningaccessuser != true}">
                    <apex:outputLink value="/{!search.accountId}" rendered="{!search.accountType == 'SFDC' && isInConsole == false}" target="_blank">
                        <apex:outputText value="{!search.accountName}"/>
                    </apex:outputLink>
                    <apex:outputLink rendered="{!search.accountType == 'SFDC' && isInConsole == true}" onclick="sforce.console.openPrimaryTab(null, '/{!search.accountId}', true); return false;">
                        <apex:outputText value="{!search.accountName}"/>
                    </apex:outputLink>
                    <apex:outputText value="{!search.accountName}" rendered="{!search.accountType != 'SFDC'}"/>                    
                </apex:column>
                <apex:column headerValue="Client Name" rendered="{!islightningaccessuser == true}">
                    <apex:outputLink value="/{!search.accountId}" rendered="{!search.accountType == 'SFDC'}">
                        <apex:outputText value="{!search.accountName}"/>
                    </apex:outputLink>
                    <apex:outputText value="{!search.accountName}" rendered="{!search.accountType != 'SFDC'}"/>
                </apex:column>  
                <!-- Added column to show record type it contains case 24374 AND 24375 -->
                <apex:column headerValue="Type">
                    <apex:outputText value="{!search.clientType}"/>
                </apex:column>
                <apex:column headerValue="Address">
                    <apex:outputText value="{!search.address}"/>
                </apex:column>
                <apex:column headerValue="Consent Status">
                    <apex:outputText value="{!search.iiaConsent}"/>
                </apex:column>
                <apex:column headerValue="RR Access Level">
                    <apex:outputText value="{!search.rrAccessLevel}"/>
                </apex:column>
                <apex:column headerValue="Online Planning Indicator">
                    <apex:outputText value="{!search.onlinePlanningInd}"/>
                </apex:column>
                <apex:column headerValue="Source" rendered="{!hasAdminNRelatedProfile}">
                    <apex:outputText value="{!search.accountType}"/>
                </apex:column>
                <!-- Not in Service Console -->                   
                <apex:column headerValue="Action" rendered="{!isInConsole == false}">
                    <apex:actionStatus startText=" Processing the stuff " stopText=" Done " id="detailView"  >
                        <apex:facet name="start" >Processing...
                            <img src="/apexpages/devmode/img/saveStatus.gif" />
                         </apex:facet>
                         <apex:facet name="stop">
                            <apex:commandLink action="{!goToDetailedView}" status="detailView" rerender="detailView" rendered="{!(search.ssn != null && search.ssn != '' && islightningaccessuser != true)}" >Go To Detailed View
                                <apex:param name="clientType" value="{!IF(CONTAINS(search.clientType, 'Entity'), 'Entity', 'Client')}" assignTo="{!clientType}"/>
                                 <apex:param name="cliId" value="{!search.ssn}" assignTo="{!ssn}"/>
                                 <apex:param name="phno" value="{!$CurrentPage.parameters.DNIS}" assignTo="{!dnis}"/>
                                 <apex:param name="source" value="{!$CurrentPage.parameters.Source}" assignTo="{!source}"/>
                                 <apex:param name="vruApp" value="{!$CurrentPage.parameters.vruApp}" assignTo="{!ctiVRUApp}"/>
                                 <apex:param name="ctiEDU" value="{!$CurrentPage.parameters.ctiEDU}" assignTo="{!ctiEDU}"/>
                                 <apex:param name="AuthenticatedFlag" value="{!$CurrentPage.parameters.AuthenticatedFlag}" assignTo="{!securityParameter}"/>
                                 <apex:param name="type" value="{!$CurrentPage.parameters.type}" assignTo="{!caseOrigin}"/>
                                 <apex:param name="caseId" value="{!$CurrentPage.parameters.caseId}" assignTo="{!caseId}"/>
                            </apex:commandLink>
                        </apex:facet>
                    </apex:actionStatus>
                </apex:column>
                
                <!-- In Service Console -->
                <apex:column headerValue="Action" rendered="{!isInConsole == true}">
                       <apex:commandLink onClick="goToDetailPage4Console({!hasClientSummAccess}, '{!search.ssn}', '{!$CurrentPage.parameters.DNIS}', '{!$CurrentPage.parameters.Source}', '{!$CurrentPage.parameters.vruApp}', '{!$CurrentPage.parameters.ctiEDU}', '{!$CurrentPage.parameters.caseId}', '{!$CurrentPage.parameters.AuthenticatedFlag}', '{!$CurrentPage.parameters.type}', '{!IF(CONTAINS(search.clientType, 'Entity'), 'Entity', 'Client')}'); return false;" rendered="{!(search.ssn != null && search.ssn != '' && islightningaccessuser != true)}" >Go To Detailed View </apex:commandLink>
                       <script>
                        if({!navigate2ConsoleDetailAutomatically} == true)
                             goToDetailPage4Console({!hasClientSummAccess}, '{!search.ssn}', '{!$CurrentPage.parameters.DNIS}', '{!$CurrentPage.parameters.Source}', '{!$CurrentPage.parameters.vruApp}', '{!$CurrentPage.parameters.ctiEDU}', '{!$CurrentPage.parameters.caseId}', '{!$CurrentPage.parameters.AuthenticatedFlag}', '{!$CurrentPage.parameters.type}', '{!IF(CONTAINS(search.clientType, 'Entity'), 'Entity', 'Client')}');
                       </script>
                </apex:column>
                          
            </apex:pageBlockTable>
        </apex:pageBlockSection>
        <apex:pageBlockSection columns="1" rendered="{!IF(NOT(ISNULL($CurrentPage.parameters.reason)) && (($CurrentPage.parameters.reason=='CLIENT-NOT-FOUND') || ($CurrentPage.parameters.reason=='NOT-AUTHENTICATED') && (hasCSAnRelatedProfile || hasISTnRelatedProfile || hasAdminNRelatedProfile)), true, false)}" id="buttonsSections">
            <br/><br/>
            <!-- <apex:commandLink rendered="{!isInConsole == false}" action="{!sendToCreateClientOppAndInteractionPage}" value="Create a New Client with an Opportunity and Interaction"/>
            <apex:commandLink rendered="{!isInConsole == false}" action="{!recordServiceCall}" value="Record Call" /> -->
            
            <!-- Added param to show client creation page based on entity/clien case 24374 AND 24375 -->
            <apex:commandLink rendered="{!isInConsole == true}" action="{!createNewClient4mConsole}" value="Create a New Client" oncomplete="sforce.console.openPrimaryTab(null, '{!newClientCreationPageURL}', true, 'New Client'); return false;">
            <apex:param name="client" value="Client" assignTo="{!accType}"/>
            </apex:commandLink>
            <apex:commandLink rendered="{!isInConsole == true}" action="{!createNewClient4mConsole}" value="Create a New Entity" oncomplete="sforce.console.openPrimaryTab(null, '{!newClientCreationPageURL}', true, 'New Entity'); return false;">
            <apex:param name="entity" value="Entity" assignTo="{!accType}"/>
            </apex:commandLink>
        </apex:pageBlockSection>
        
         <apex:pageBlockSection id="dummySection2Rerender"/>        
    </apex:pageBlock>
</apex:form>
</apex:page>



















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
