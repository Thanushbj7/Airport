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
        }  else {
            try {
				PlanInfo pi = PlanInfo.getInstance(key.getClient(), key.getPlan());
				PlanControl pc = pi.getPlanControl(ControlType.WEB);
				boolean isPlatformUpgradeEnabled = StringUtils.equalsIgnoreCase("B", pc.getPlatformUpgradeFlag());
				boolean bridgeappEnabledKey = StringUtils.equals("true",System.getProperty("ppt.bridgeapp.enabled"));
 
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("isPlatformUpgradeEnabled"+isPlatformUpgradeEnabled);
					LOGGER.debug("bridgeappEnabledKey"+bridgeappEnabledKey);
				}
				if (bridgeappEnabledKey && isPlatformUpgradeEnabled) {
					ServiceManager serviceManager = ServiceManagerFactory.getInstance();
					ParticipantHistoryBridgeDAO participantHistBridgeClient = serviceManager.getService(ServiceID.valueOf("participant.history.bridge.dao"));
					balanceHistory = participantHistBridgeClient.getBalanceHistoryData(key.getClient(), key.getPlan(), key.getParticipant(),startDate,endDate);
				}else {
 
					SolServices solService = SolServices.getInstance(key.getClient(), key.getPlan(), key.getParticipant());
					balanceHistory = solService.getBalanceHistoryData(startDate, endDate);
				}
            } catch (BusinessException solException) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Exception occured for Balance History Sol Service....", solException);
                }
                throw solException;
            }
        }
	}







private BalanceHistoryCache(final ParticipantKey key) {
    ParticipantInfo partInfo = ParticipantInfo.getInstance(key.getClient(), key.getPlan(), key.getParticipant());
    int pptAcctAge = partInfo.getParticipantAccountAgeInMonths();
    startDate = Calendar.getInstance();
    endDate = Calendar.getInstance();

    if (pptAcctAge >= 24) {
        logDebug(key, "Participant's account age is >= 24 months, making a call to SOL service with 2yr duration...", 2);
        startDate.add(Calendar.YEAR, -2);
    } else if (pptAcctAge >= 12) {
        logDebug(key, "Participant's account age is >=12 months, making a call to SOL service with 1yr duration...", 1);
        startDate.add(Calendar.YEAR, -1);
    } else if (pptAcctAge >= 6) {
        logDebug(key, "Participant's account age is >=6 months, making a call to SOL service with 6 months duration...", 6);
        startDate.add(Calendar.MONTH, -6);
    } else if (pptAcctAge >= 3) {
        logDebug(key, "Participant's account age is >=3 months, making a call to SOL service with 3 months duration...", 3);
        startDate.add(Calendar.MONTH, -3);
    } else {
        startDate = null;
        endDate = null;
    }

    if (startDate == null && endDate == null) {
        balanceHistory = Collections.emptyMap();
    } else {
        try {
            SolServices solService = SolServices.getInstance(key.getClient(), key.getPlan(), key.getParticipant());
            balanceHistory = solService.getBalanceHistoryData(startDate, endDate);
        } catch (BusinessException solException) {
            logError("Exception occurred for Balance History Sol Service....", solException);
            throw solException;
        }
    }
}

private void logDebug(ParticipantKey key, String message, int duration) {
    if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Participant's {} account age is >= {} months, {}", key.getParticipant(), duration, message);
    }
}

private void logError(String message, BusinessException exception) {
    if (LOGGER.isErrorEnabled()) {
        LOGGER.error(message, exception);
    }
}










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
