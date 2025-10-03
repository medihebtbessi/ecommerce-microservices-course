package com.iheb.ecommerce.notification;

import com.iheb.ecommerce.kafka.order.OrderConfirmation;
import com.iheb.ecommerce.kafka.payment.PaymentConfirmation;
import lombok.*;
import org.apache.kafka.shaded.com.google.protobuf.DescriptorProtos;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {
    @Id
    private String id;
    private NotificationType notificationType;
    private LocalDateTime notificationDate;
    private OrderConfirmation orderConfirmation;
    private PaymentConfirmation paymentConfirmation;
}
