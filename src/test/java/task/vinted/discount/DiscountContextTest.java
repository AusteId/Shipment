package task.vinted.discount;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class DiscountContextTest {

  private static final BigDecimal MONTHLY_LIMIT = BigDecimal.TEN;

  @Test
  void shouldReturnRemainingDiscount() {

    DiscountContext context = new DiscountContext(MONTHLY_LIMIT);
    LocalDate date = LocalDate.of(2015, 2, 1);
    BigDecimal remaining = context.getRemainingMonthlyDiscount(date);
    assertEquals(MONTHLY_LIMIT, remaining, "Remaining discount should be 10 when no discounts applied");
    context.addDiscount(date, new BigDecimal("3.00"));
    remaining = context.getRemainingMonthlyDiscount(date);

    assertEquals(new BigDecimal("7.00"), remaining, "Remaining discount should be 7 after 3 discount applied");
  }

  @Test
  void shouldAddDiscountWithinLimit() {

    DiscountContext context = new DiscountContext(MONTHLY_LIMIT);
    LocalDate date = LocalDate.of(2015, 2, 1);
    BigDecimal discountToAdd = new BigDecimal("4.00");
    BigDecimal appliedDiscount = context.addDiscount(date, discountToAdd);

    assertEquals(discountToAdd, appliedDiscount, "Applied discount should match requested discount");
    assertEquals(new BigDecimal("6.00"), context.getRemainingMonthlyDiscount(date),
            "Remaining discount should be 6 after 4 discount applied");
  }

  @Test
  void shouldTrackLSizeLPCount() {

    DiscountContext context = new DiscountContext(MONTHLY_LIMIT);
    YearMonth yearMonth = YearMonth.of(2015, 2);
    context.incrementLSizeLPCount(yearMonth);
    context.incrementLSizeLPCount(yearMonth);

    assertEquals(2, context.getLSizeLPCountForMonth(yearMonth),
            "L size LP count should be 2 after two increments");

    YearMonth otherMonth = YearMonth.of(2015, 3);

    assertEquals(0, context.getLSizeLPCountForMonth(otherMonth),
            "L size LP count should be 0 for different month");
  }
}
