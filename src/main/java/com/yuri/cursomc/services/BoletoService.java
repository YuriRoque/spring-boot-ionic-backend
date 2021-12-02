package com.yuri.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import com.yuri.cursomc.domain.PagamentoComBoleto;

import org.springframework.stereotype.Service;

@Service
public class BoletoService {
  
  public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date isntanteDoPedido) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(isntanteDoPedido);
    cal.add(Calendar.DAY_OF_MONTH, 7);
    pagto.setDataVencimento(cal.getTime());
  }

}
