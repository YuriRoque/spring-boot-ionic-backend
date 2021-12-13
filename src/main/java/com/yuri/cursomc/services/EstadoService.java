package com.yuri.cursomc.services;

import java.util.List;

import com.yuri.cursomc.domain.Estado;
import com.yuri.cursomc.repositories.EstadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {
  
  @Autowired
  private EstadoRepository estadoRepository;

  public List<Estado> findAll() {
    return estadoRepository.findAllByOrderByNome();
  }

}
