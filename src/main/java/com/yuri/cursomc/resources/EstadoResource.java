package com.yuri.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import com.yuri.cursomc.domain.Cidade;
import com.yuri.cursomc.domain.Estado;
import com.yuri.cursomc.dto.CidadeDTO;
import com.yuri.cursomc.dto.EstadoDTO;
import com.yuri.cursomc.services.CidadeService;
import com.yuri.cursomc.services.EstadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {
  
  @Autowired
  private EstadoService estadoService;

  @Autowired
  private CidadeService cidadeService;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<EstadoDTO>> findAll() {

    List<Estado> list = estadoService.findAll();
    List<EstadoDTO> listDto = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());

    return ResponseEntity.ok().body(listDto);

  }

  @RequestMapping(value = "/{estadoId}/cidades")
  public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {

    List<Cidade> list = cidadeService.findByEstado(estadoId);
    List<CidadeDTO> listDto = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());

    return ResponseEntity.ok().body(listDto);

  }

}
