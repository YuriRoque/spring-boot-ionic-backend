package com.yuri.cursomc.services;

import java.util.Optional;

import com.yuri.cursomc.domain.Pedido;
import com.yuri.cursomc.repositories.PedidoRepository;
import com.yuri.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
  
  @Autowired
  private PedidoRepository repo;

  public Pedido buscar(Integer id) {

    Optional<Pedido> obj = repo.findById(id);

    return obj.orElseThrow(
        () -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName())
    );
  }

}
