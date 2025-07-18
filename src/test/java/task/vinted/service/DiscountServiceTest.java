package task.vinted.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.vinted.discount.DiscountRule;
import task.vinted.model.Shipment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

  @Mock
  private PriceService priceService;

  @Mock
  private DiscountRule sSizeDiscountRule;

  @Mock
  private DiscountRule lSizeLPDiscountRule;

  @InjectMocks
  private DiscountService discountService;

  @Test
  void shouldApplyPriceAndDiscountForSSize() {

    LocalDate date = LocalDate.of(2015, 2, 1);
    Shipment shipment = new Shipment(date, "S", "MR", BigDecimal.ZERO, BigDecimal.ZERO);
    when(priceService.getPriceByProviderAndSize("MR", "S")).thenReturn(new BigDecimal("2.00"));
    discountService = new DiscountService(List.of(sSizeDiscountRule, lSizeLPDiscountRule), priceService);

    discountService.applyDiscounts(shipment);

    assertEquals(new BigDecimal("2.00"), shipment.getShipmentPrice(), "Shipment price should be set to 2.00");
    verify(sSizeDiscountRule).applyDiscount(shipment);
    verify(lSizeLPDiscountRule).applyDiscount(shipment);
    verify(priceService).getPriceByProviderAndSize("MR", "S");
  }

  @Test
  void shouldNotApplyDiscountForNonApplicableSize() {

    LocalDate date = LocalDate.of(2015, 2, 1);
    Shipment shipment = new Shipment(date, "M", "MR", BigDecimal.ZERO, BigDecimal.ZERO);
    when(priceService.getPriceByProviderAndSize("MR", "M")).thenReturn(new BigDecimal("3.00"));
    discountService = new DiscountService(List.of(sSizeDiscountRule, lSizeLPDiscountRule), priceService);

    discountService.applyDiscounts(shipment);

    assertEquals(new BigDecimal("3.00"), shipment.getShipmentPrice(), "Shipment price should be set to 3.00");
    assertEquals(BigDecimal.ZERO, shipment.getDiscountAmount(), "Discount amount should be 0");
    verify(sSizeDiscountRule).applyDiscount(shipment);
    verify(lSizeLPDiscountRule).applyDiscount(shipment);
    verify(priceService).getPriceByProviderAndSize("MR", "M");
  }

  @Test
  void shouldNotApplyDiscountWhenLimitExhausted() {

    LocalDate date = LocalDate.of(2015, 2, 1);
    Shipment shipment = new Shipment(date, "S", "MR", BigDecimal.ZERO, BigDecimal.ZERO);
    when(priceService.getPriceByProviderAndSize("MR", "S")).thenReturn(new BigDecimal("2.00"));
    discountService = new DiscountService(List.of(sSizeDiscountRule, lSizeLPDiscountRule), priceService);

    discountService.applyDiscounts(shipment);

    assertEquals(new BigDecimal("2.00"), shipment.getShipmentPrice(), "Shipment price should be set to 2.00");
    assertEquals(BigDecimal.ZERO, shipment.getDiscountAmount(), "Discount amount should be 0");
    verify(sSizeDiscountRule).applyDiscount(shipment);
    verify(lSizeLPDiscountRule).applyDiscount(shipment);
    verify(priceService).getPriceByProviderAndSize("MR", "S");
  }
}
