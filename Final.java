async saveOpportunity() {
            try {
        
                if (!this.responseError && !this.responseReasonError && !this.commentError && !this.messageError) {
                    const res1 = await getClientLastName({ recordId: this.recordId });
                    console.log('res1', res1);
                    const result = await createOpportunity({ clientLastName: res1, campaignName: this.message, ownerId: this.ownerName, response: this.responseValue, responseReason: this.responseReasonValue, comment: this.messageValue });
                    console.log('result', result);
                    //alert('result')
                    const toast = await this.showToast(result);
                    const re = await this.reset(); 
                    /*      console.log('this.clientLastName', this.clientLastName);
                        console.log('this.message', this.message);
                        console.log('this.ownerName', this.ownerName);
                        console.log('this.responseValue', this.responseValue);
                        console.log('this.responseReasonValue', this.responseReasonValue);
                        console.log('this.messageValue', this.messageValue);*/
                    // createOpportunity({ clientLastName: this.clientLastName, campaignName: this.message, ownerId: this.ownerName, response: this.responseValue, responseReason: this.responseReasonValue, comment: this.messageValue })

                    //.then((result) => {

                    //  this.opp = result;
                    ///  this.error = undefined;
                    // console.log('end of saveOpp', this.opp);
                    // }).catch((error) => {
                    //  this.error = error;
                    //  this.opp = undefined;
                    // });
                }
            } catch (error) {
                console.error(error);
            }
        }
        reset() {
            this.refs.textArea.value = '';
        }

        showToast(comm) {
            const event = new ShowToastEvent({
                title: 'The below Opportunity has been  Created',
                message: comm,
                variant: 'success',
                mode: 'dismissable'
            });
            this.dispatchEvent(event);
        }
