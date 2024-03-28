@Immutable
@DefaultAnnotation(NonNull.class)
public class BalanceHistoryCache implements java.io.Serializable {
   private static final long serialVersionUID = 1L;
   private static final Log LOGGER = LogFactory.getLog(BalanceHistoryCache.class);
    private static final Factory FACTORY = new Factory();
   private Calendar startDate;
   private Calendar endDate;
    private Map<Integer, BalanceHistoryData> balanceHistory;

   @Value("${ppt.bridgeapp.enabled}")
   private boolean bridgeappEnabledKey;


   @ThreadSafe
   @DefaultAnnotation(NonNull.class)
    @CacheConfiguration("pcf.cache.transition.participant.config")
   private static final class Factory extends AbstractObjectCacheElementFactory<ParticipantKey, BalanceHistoryCache> {
      private Factory() {
         init(CacheConfigUtils.getCacheConfig(CbcServices.CB_PARTICIPANT_CACHE_CONFIG));
      }

      @Override
      public BalanceHistoryCache createInstance(final ParticipantKey key) {
         return new BalanceHistoryCache(key);
      }
   }

    public static BalanceHistoryCache getInstance(final ParticipantKey key){
        if (key == null) {
            throw new IllegalArgumentException("Invalid Participant Key");
        }

        return FACTORY.getInstance(key);
    }
   
   /**
    * Private constructor
    * @param key
    */
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
            PlanInfo pi = PlanInfo.getInstance(key.getClient(), key.getPlan());
            PlanControl pc = pi.getPlanControl(ControlType.WEB);
            boolean isPlatformUpgradeEnabled = StringUtils.equalsIgnoreCase("B", pc.getPlatformUpgradeFlag());
            if (bridgeappEnabledKey && isPlatformUpgradeEnabled) {
               ParticipantHistBridgeClientImpl participantHistBridgeClient = ParticipantHistBridgeClientImpl.getInstance(key.getClient(), key.getPlan(), key.getParticipant());
               ParticipantDataResponse participantResponse = participantHistBridgeClient.getBalanceHistoryData(startDate, endDate);
            } else {
               balanceHistory = solService.getBalanceHistoryData(startDate, endDate);
            }
         } catch (BusinessException solException) {
            if (LOGGER.isErrorEnabled()) {
               LOGGER.error("Exception occured for Balance History Sol Service....", solException);
            }
            throw solException;
         } catch (SolException e) {
            e.printStackTrace();
         }
      }
   }

   public void setBridgeappEnabledKey(@Value("${ppt.bridgeapp.enabled}") boolean bridgeappEnabledKey) {
      this.bridgeappEnabledKey = bridgeappEnabledKey;
   }

   /**
    * Returns the BalanceHistoryData sorted Map
    * @return Map<Integer,BalanceHistoryData>
    */
   public Map<Integer, BalanceHistoryData> getBalanceHistoryData() {
      return balanceHistory;
   }
   
   public Calendar getStartDate() {
      return this.startDate;
   }
   
   public Calendar getEndDate() {
      return this.endDate;
   }
}
 
