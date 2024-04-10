Refactor this method to reduce its cognitive complexity


private BalanceHistoryCache(final ParticipantKey key) {
   ParticipantInfo partInfo = ParticipantInfo.getInstance(key.getClient(),key.getPlan(),key.getParticipant());
   int pptAcctAge = partInfo.getParticipantAccountAgeInMonths();
   startDate = Calendar.getInstance();
   endDate = Calendar.getInstance(); 
   if(pptAcctAge >= 24) {
      if(LOGGER.isDebugEnabled()) {
         LOGGER.debug("Participant's {0} account age is >= 24 months, making a call to SOL service with 2yr duration...",key.getParticipant());
      }
      startDate.add(Calendar.YEAR, -2);
   } else if(pptAcctAge >= 12  ) {
      if(LOGGER.isDebugEnabled()) {
         LOGGER.debug("Participant's {0} account age is >=12 months, making a call to SOL service with 1yr duration...",key.getParticipant());
      }
      startDate.add(Calendar.YEAR, -1);
   } else if(pptAcctAge >= 6 ) {
      if(LOGGER.isDebugEnabled()){
         LOGGER.debug("Participant's {0} account age is >=6 months,  making a call to SOL service with 6 months duration...",key.getParticipant());
      }
      startDate.add(Calendar.MONTH, -(6));
   } else if (pptAcctAge >= 3) {
      if(LOGGER.isDebugEnabled()) {
         LOGGER.debug("Participant's {0} account age is >=3 months,  making a call to SOL service with 6 months duration...",key.getParticipant());
      }
      startDate.add(Calendar.MONTH, -(3));
   } else {
      startDate = null;
      endDate = null;
   }

       if (startDate == null && endDate == null) {
           //Participant's account is lessThan three months old.. Return empty Map.
           balanceHistory = Collections.emptyMap();
       } else {
           try {
               SolServices solService = SolServices.getInstance(key.getClient(), key.getPlan(), key.getParticipant());
               balanceHistory = solService.getBalanceHistoryData(startDate, endDate);
           } catch (BusinessException solException) {
               if (LOGGER.isErrorEnabled()) {
                   LOGGER.error("Exception occured for Balance History Sol Service....", solException);
               }
               throw solException;
           }
       }
}
