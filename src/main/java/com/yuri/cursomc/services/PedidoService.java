package com.yuri.cursomc.services;

import java.util.Date;
import java.util.Optional;

import com.yuri.cursomc.domain.ItemPedido;
import com.yuri.cursomc.domain.PagamentoComBoleto;
import com.yuri.cursomc.domain.Pedido;
import com.yuri.cursomc.domain.enums.EstadoPagamento;
import com.yuri.cursomc.repositories.ItemPedidoRepository;
import com.yuri.cursomc.repositories.PagamentoRepository;
import com.yuri.cursomc.repositories.PedidoRepository;
import com.yuri.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {
  
  @Autowired
  private PedidoRepository repo;

  @Autowired
  private BoletoService boletoService;

  @Autowired
  private PagamentoRepository pagamentoRepository;

  @Autowired
  private ProdutoService produtoService;

  @Autowired
  private ItemPedidoRepository itemPedidoRepository;

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private EmailService emailService;

  public Pedido find(Integer id) {

    Optional<Pedido> obj = repo.findById(id);

    return obj.orElseThrow(
        () -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
  }
  
  @Transactional
  public Pedido insert(Pedido obj) {

    obj.setId(null);
    obj.setInstante(new Date());
    obj.setCliente(clienteService.find(obj.getCliente().getId()));
    obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
    obj.getPagamento().setPedido(obj);

    if (obj.getPagamento() instanceof PagamentoComBoleto) {
      PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
      boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
    }

    obj = repo.save(obj);
    
    pagamentoRepository.save(obj.getPagamento());

    for (ItemPedido ip : obj.getItens()) {
      ip.setDesconto(0.0);
      ip.setProduto(produtoService.find(ip.getProduto().getId()));
      ip.setPreco(ip.getProduto().getPreco());
      ip.setPedido(obj);
    }

    itemPedidoRepository.saveAll(obj.getItens());

    emailService.sendOrderConfirmationHtmlEmail(obj);

    return obj;

  }

}
