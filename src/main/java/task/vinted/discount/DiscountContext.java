package task.vinted.discount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class DiscountContext {

  Map<YearMonth, BigDecimal> monthlyDiscount = new HashMap<>();
  Map<YearMonth, Integer> monthlyLPLCount = new HashMap<>();
  private BigDecimal monthlyDiscountLimit;

  public DiscountContext(BigDecimal monthlyDiscountLimit) {
    this.monthlyDiscountLimit = monthlyDiscountLimit;
  }

  public BigDecimal getRemainingMonthlyDiscount(LocalDate date) {
    YearMonth yearMonth = YearMonth.from(date);
    return monthlyDiscountLimit.subtract(monthlyDiscount.getOrDefault(yearMonth, BigDecimal.ZERO));
  }

  public BigDecimal addDiscount(LocalDate date, BigDecimal discount) {
    YearMonth yearMonth = YearMonth.from(date);
    BigDecimal totalDiscount = monthlyDiscount.getOrDefault(yearMonth, BigDecimal.ZERO);
    BigDecimal remaining = monthlyDiscountLimit.subtract(totalDiscount);

    if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
      return BigDecimal.ZERO;
    }

    BigDecimal toApply = discount.min(remaining);
    monthlyDiscount.put(yearMonth, totalDiscount.add(toApply));
    return toApply;
  }

  public int getLSizeLPCountForMonth(YearMonth yearMonth) {
    return monthlyLPLCount.getOrDefault(yearMonth, 0);
  }

  public void incrementLSizeLPCount(YearMonth yearMonth) {
    monthlyLPLCount.put(yearMonth, getLSizeLPCountForMonth(yearMonth) + 1);
  }
}
