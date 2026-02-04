package com.lambda;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * P = Monto del préstamo
 * i = Tasa de interés mensual
 * n = Plazo del crédito en meses
 * <p>
 * Cuota mensual = (P * i) / (1 - (1 + i) ^ (-n))
 */

public class LambdaBank implements RequestHandler<BankRequest, BankResponse> {

    @Override
    public BankResponse handleRequest(BankRequest bankRequest, Context context) {
        MathContext mathContext = MathContext.DECIMAL128;
        BigDecimal amount = bankRequest.getAmount().setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthyRate = bankRequest.getRate()
                                            .setScale(2, RoundingMode.HALF_UP)
                                            .divide(BigDecimal.valueOf(100), mathContext);
        BigDecimal monthyRateWithAccount = bankRequest.getRate()
                                                    .subtract(BigDecimal.valueOf(0.2), mathContext)
                                                    .setScale(2, RoundingMode.HALF_UP)
                                                    .divide(BigDecimal.valueOf(100), mathContext);

        Integer term = bankRequest.getTerm();

        BigDecimal monthlyPayment = this.calculateRate(amount, monthyRate, term, mathContext);
        BigDecimal monthlyPaymentWithAccount = this.calculateRate(amount, monthyRateWithAccount, term, mathContext);

        BankResponse bankResponse = new BankResponse();
        bankResponse.setQuota(monthlyPayment);
        bankResponse.setRate(monthyRate);
        bankResponse.setTerm(term);
        //with Account
        bankResponse.setQuotaWithAccount(monthlyPaymentWithAccount);
        bankResponse.setRateWithAccount(monthyRateWithAccount);
        bankResponse.setTermWithAccount(term);

        return bankResponse;
    }

    /**
     * P = Monto del préstamo
     * i = Tasa de interés mensual
     * n = Plazo del crédito en meses
     * <p>
     * Cuota mensual = (P * i) / (1 - (1 + i) ^ (-n))
     */

    public BigDecimal calculateRate(BigDecimal amount, BigDecimal rate, Integer term, MathContext mathContext){

        //calculate (1 + i)
        BigDecimal onePlusRate = rate.add(BigDecimal.ONE, mathContext);
        //(1 + i) ^ n
        BigDecimal onePlusRateToN = onePlusRate.pow(term, mathContext);
        //(1 + i) ^ -n pasarlo a negativo se utiliza el reciproco 1/x
        BigDecimal onePlusRateToNnegative = BigDecimal.ONE.divide(onePlusRateToN, mathContext);
        //Calcular Cuota mensual = (P * i) / (1 - (1 + i) ^ (-n))
        BigDecimal amountMultiplyRate = amount.multiply(rate, mathContext);
        //(1 - (1 + i) ^ (-n)
        BigDecimal oneSubstrac = BigDecimal.ONE.subtract(onePlusRateToNnegative, mathContext);
        BigDecimal finalRate = amountMultiplyRate.divide(oneSubstrac, mathContext);


        //final to 2 decimal
        finalRate = finalRate.setScale(2, RoundingMode.HALF_UP);

        return finalRate;
    }
}