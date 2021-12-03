package com.yuri.cursomc.services;

import java.util.Date;

import com.yuri.cursomc.domain.Pedido;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

public abstract class AbstractEmailService implements EmailService {
  
  @Value("${default.sender}")
  private String sender;

  @Override
  public void sendOrderConfirmationEmail(Pedido obj) {

    SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);

    sendEmail(sm);

  }

  protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {

    SimpleMailMessage sm = new SimpleMailMessage();

    sm.setTo(obj.getCliente().getEmail());
    sm.setFrom(sender);
    sm.setSubject("Pedido confirmado! Código: " + obj.getId());
    sm.setSentDate(new Date(System.currentTimeMillis()));
    sm.setText(obj.toString());

    return sm;
  }

}
