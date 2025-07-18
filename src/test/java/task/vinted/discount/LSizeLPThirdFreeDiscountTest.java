package task.vinted.discount;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.vinted.model.Shipment;
import task.vinted.service.PriceService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LSizeLPThirdFreeDiscountTest {

  @Mock
  private PriceService priceService;

  @Mock
  private DiscountContext discountContext;

  @InjectMocks
  private LSizeLPThirdFreeDiscount discountRule;

  @Test
  void shouldApplyDiscountForThirdLSizeLP() {

    LocalDate date = LocalDate.of(2015, 2, 1);
    YearMonth yearMonth = YearMonth.from(date);
    Shipment shipment = new Shipment(date, "L", "LP", new BigDecimal("6.90"), BigDecimal.ZERO);
    when(discountContext.getLSizeLPCountForMonth(yearMonth)).thenReturn(2);
    when(priceService.getLSizeLPProviderPrice("LP", "L")).thenReturn(new BigDecimal("6.90"));
    when(discountContext.getRemainingMonthlyDiscount(date)).thenReturn(new BigDecimal("10.00"));
    when(discountContext.addDiscount(date, new BigDecimal("6.90"))).thenReturn(new BigDecimal("6.90"));

    discountRule.applyDiscount(shipment);

    assertEquals(new BigDecimal("0.00"), shipment.getShipmentPrice(), "Shipment price should be 0.00 after full discount");
    assertEquals(new BigDecimal("6.90"), shipment.getDiscountAmount(), "Discount amount should be 6.90");
    verify(discountContext).incrementLSizeLPCount(yearMonth);
    verify(discountContext).getRemainingMonthlyDiscount(date);
    verify(discountContext).addDiscount(date, new BigDecimal("6.90"));
  }

  @Test
  void shouldNotApplyDiscountForNonLSizeOrNonLP() {

    LocalDate date = LocalDate.of(2015, 2, 1);
    Shipment shipment1 = new Shipment(date, "S", "LP", new BigDecimal("1.50"), BigDecimal.ZERO);
    Shipment shipment2 = new Shipment(date, "L", "MR", new BigDecimal("4.00"), BigDecimal.ZERO);

    discountRule.applyDiscount(shipment1);
    discountRule.applyDiscount(shipment2);

    assertEquals(new BigDecimal("1.50"), shipment1.getShipmentPrice(), "Shipment price should remain unchanged for S size");
    assertEquals(BigDecimal.ZERO, shipment1.getDiscountAmount(), "Discount amount should be 0 for S size");
    assertEquals(new BigDecimal("4.00"), shipment2.getShipmentPrice(), "Shipment price should remain unchanged for MR provider");
    assertEquals(BigDecimal.ZERO, shipment2.getDiscountAmount(), "Discount amount should be 0 for MR provider");
    verifyNoInteractions(priceService, discountContext);
  }

  @Test
  void shouldNotApplyDiscountWhenLimitExhausted() {

    LocalDate date = LocalDate.of(2015, 2, 1);
    YearMonth yearMonth = YearMonth.from(date);
    Shipment shipment = new Shipment(date, "L", "LP", new BigDecimal("6.90"), BigDecimal.ZERO);
    when(discountContext.getLSizeLPCountForMonth(yearMonth)).thenReturn(2);
    when(priceService.getLSizeLPProviderPrice("LP", "L")).thenReturn(new BigDecimal("6.90"));
    when(discountContext.getRemainingMonthlyDiscount(date)).thenReturn(BigDecimal.ZERO);

    discountRule.applyDiscount(shipment);

    assertEquals(new BigDecimal("6.90"), shipment.getShipmentPrice(), "Shipment price should remain unchanged");
    assertEquals(BigDecimal.ZERO, shipment.getDiscountAmount(), "Discount amount should be 0");
    verify(discountContext).incrementLSizeLPCount(yearMonth);
    verify(discountContext).getRemainingMonthlyDiscount(date);
    verifyNoMoreInteractions(discountContext);
    verify(priceService).getLSizeLPProviderPrice("LP", "L");
  }
}