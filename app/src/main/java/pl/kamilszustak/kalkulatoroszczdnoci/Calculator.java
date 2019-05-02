package pl.kamilszustak.kalkulatoroszczdnoci;

public class Calculator {

    private double actualBalance;
    private double profit;

    public  Calculator(double bankBalance, int periodMonths, double monthlyAmount, double yearPercent, int capitalization, boolean capitalizationAtEndOfPeriod, double tax) {
        actualBalance = bankBalance;
        double monthPercent;
        int months = 1;
        double taxMulpiplier = 1 - (tax/100);
        int profitIncrementation = 0;
        double tempProfit = 0;

        if(capitalizationAtEndOfPeriod) {
            profitIncrementation = periodMonths;
            monthPercent = ((yearPercent/12)/100)*periodMonths;
        }
        else {
            monthPercent = yearPercent / (capitalization*100);
            switch (capitalization) {
                case 1:
                    profitIncrementation = 12;
                    break;
                case 4:
                    profitIncrementation = 3;
                    break;
                case 12:
                    profitIncrementation = 1;
                    break;
            }
        }

        while(months <= periodMonths) {
            actualBalance = actualBalance + monthlyAmount;
            if((months%profitIncrementation == 0) && (actualBalance>0)) {
                // Oblicza oprocentowanie, odlicza podatek Belki i dodaje do sumy początkowej
                tempProfit = (actualBalance * monthPercent) * taxMulpiplier;
                profit += tempProfit;
                actualBalance += tempProfit;
            }
            months++;
        }
        actualBalance = ((double)Math.round(actualBalance*100))/100;
    }

    public double getActualBalance() {
        return actualBalance;
    }

    public double getProfit() {
        return profit;
    }
}