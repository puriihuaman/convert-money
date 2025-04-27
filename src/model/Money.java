package model;

public class Money {
    private String baseCode;
    private String targetCode;
    private double conversionRate;
    private double conversionResult;
    
    public Money() {
    }
    
    public Money(String baseCode, String targetCode, double conversionRate, double conversionResult)
    {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.conversionRate = conversionRate;
        this.conversionResult = conversionResult;
    }
    
    public String getBaseCode() {
        return baseCode;
    }
    
    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }
    
    public String getTargetCode() {
        return targetCode;
    }
    
    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }
    
    public double getConversionRate() {
        return conversionRate;
    }
    
    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }
    
    public double getConversionResult() {
        return conversionResult;
    }
    
    public void setConversionResult(double conversionResult) {
        this.conversionResult = conversionResult;
    }
}
