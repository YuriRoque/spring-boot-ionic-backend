package com.yuri.cursomc.services;

import com.yuri.cursomc.domain.Pedido;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
  
  void sendOrderConfirmationEmail(Pedido obj);

  void sendEmail(SimpleMailMessage msg);

}
