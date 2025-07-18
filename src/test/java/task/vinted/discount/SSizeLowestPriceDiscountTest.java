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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SSizeLowestPriceDiscountTest {

  @Mock
  private PriceService priceService;

  @Mock
  private DiscountContext discountContext;

  @InjectMocks
  private SSizeLowestPriceDiscount discountRule;

  @Test
  void shouldApplyDiscountForSSize() {

    LocalDate date = LocalDate.of(2015, 2, 1);
    Shipment shipment = new Shipment(date, "S", "MR", new BigDecimal("2.00"), BigDecimal.ZERO);
    when(priceService.getLowestPrice("S")).thenReturn(new BigDecimal("1.50"));
    when(discountContext.getRemainingMonthlyDiscount(date)).thenReturn(new BigDecimal("10.00"));
    when(discountContext.addDiscount(eq(date), any(BigDecimal.class))).thenReturn(new BigDecimal("0.50"));

    discountRule.applyDiscount(shipment);

    assertEquals(new BigDecimal("1.50"), shipment.getShipmentPrice(), "Shipment price should be reduced to 1.50");
    assertEquals(new BigDecimal("0.50"), shipment.getDiscountAmount(), "Discount amount should be 0.50");
    verify(discountContext).addDiscount(date, new BigDecimal("0.50"));
  }

  @Test
  void shouldNotApplyDiscountForNonSSize() {

    LocalDate date = LocalDate.of(2015, 2, 1);
    Shipment shipment = new Shipment(date, "M", "MR", new BigDecimal("3.00"), BigDecimal.ZERO);

    discountRule.applyDiscount(shipment);

    assertEquals(new BigDecimal("3.00"), shipment.getShipmentPrice(), "Shipment price should remain unchanged");
    assertEquals(BigDecimal.ZERO, shipment.getDiscountAmount(), "Discount amount should be 0");
    verifyNoInteractions(priceService, discountContext);
  }

  @Test
  void shouldNotApplyDiscountWhenLimitExhausted() {

    LocalDate date = LocalDate.of(2015, 2, 1);
    Shipment shipment = new Shipment(date, "S", "MR", new BigDecimal("2.00"), BigDecimal.ZERO);
    when(priceService.getLowestPrice("S")).thenReturn(new BigDecimal("1.50"));
    when(discountContext.getRemainingMonthlyDiscount(date)).thenReturn(BigDecimal.ZERO);

    discountRule.applyDiscount(shipment);

    assertEquals(new BigDecimal("2.00"), shipment.getShipmentPrice(), "Shipment price should remain unchanged");
    assertEquals(BigDecimal.ZERO, shipment.getDiscountAmount(), "Discount amount should be 0");
    verify(discountContext).getRemainingMonthlyDiscount(date);
    verifyNoMoreInteractions(discountContext);
    verify(priceService).getLowestPrice("S");
  }
}
