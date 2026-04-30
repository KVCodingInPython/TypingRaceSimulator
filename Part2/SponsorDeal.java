public final class SponsorDeal {
    private enum Condition {
        NONE,
        NO_BURNOUTS,
        HIGH_WPM,
        HIGH_ACCURACY
    }

    private final String sponsorName;
    private final String conditionDescription;
    private final int bonusCoins;
    private final Condition condition;
    private final int threshold;

    private SponsorDeal(String sponsorName, String conditionDescription, int bonusCoins, Condition condition, int threshold) {
        this.sponsorName = sponsorName;
        this.conditionDescription = conditionDescription;
        this.bonusCoins = bonusCoins;
        this.condition = condition;
        this.threshold = threshold;
    }

    public static SponsorDeal none() {
        return new SponsorDeal("No Sponsor", "No active deal", 0, Condition.NONE, 0);
    }

    public static SponsorDeal keyCorp() {
        return new SponsorDeal("KeyCorp", "+50 coins if you finish without a single burnout", 50, Condition.NO_BURNOUTS, 0);
    }

    public static SponsorDeal speedForge() {
        return new SponsorDeal("SpeedForge", "+35 coins if you finish at 55 WPM or higher", 35, Condition.HIGH_WPM, 55);
    }

    public static SponsorDeal steadyHands() {
        return new SponsorDeal("SteadyHands", "+40 coins if you finish with 90% accuracy or better", 40, Condition.HIGH_ACCURACY, 90);
    }

    public static SponsorDeal[] choices() {
        return new SponsorDeal[] {
            none(),
            keyCorp(),
            speedForge(),
            steadyHands()
        };
    }

    public int calculateBonus(Typist typist, int finalWpm, double finalAccuracyPercent) {
        if (typist == null || condition == Condition.NONE) {
            return 0;
        }

        boolean satisfied;
        switch (condition) {
            case NO_BURNOUTS:
                satisfied = typist.getBurnoutEventCount() == 0;
                break;
            case HIGH_WPM:
                satisfied = finalWpm >= threshold;
                break;
            case HIGH_ACCURACY:
                satisfied = finalAccuracyPercent >= threshold;
                break;
            default:
                satisfied = false;
                break;
        }

        return satisfied ? bonusCoins : 0;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public int getBonusCoins() {
        return bonusCoins;
    }

    public boolean isActive() {
        return condition != Condition.NONE;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SponsorDeal)) {
            return false;
        }
        SponsorDeal that = (SponsorDeal) other;
        return bonusCoins == that.bonusCoins
            && threshold == that.threshold
            && sponsorName.equals(that.sponsorName)
            && conditionDescription.equals(that.conditionDescription)
            && condition == that.condition;
    }

    @Override
    public int hashCode() {
        int result = sponsorName.hashCode();
        result = 31 * result + conditionDescription.hashCode();
        result = 31 * result + bonusCoins;
        result = 31 * result + condition.hashCode();
        result = 31 * result + threshold;
        return result;
    }

    @Override
    public String toString() {
        if (!isActive()) {
            return sponsorName;
        }
        return sponsorName + " - " + conditionDescription;
    }
}
