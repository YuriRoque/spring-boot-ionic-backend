package com.yuri.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.yuri.cursomc.domain.Cliente;
import com.yuri.cursomc.dto.ClienteDTO;
import com.yuri.cursomc.repositories.ClienteRepository;
import com.yuri.cursomc.resources.exceptions.FieldMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private ClienteRepository repo;

  @Override
  public void initialize(ClienteUpdate ann) {
  }

  @Override
  public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {

    @SuppressWarnings("unchecked")
    Map<String, String> map = (Map<String, String>) request
        .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    Integer uriId = Integer.parseInt(map.get("id"));

    List<FieldMessage> list = new ArrayList<>();

    Cliente aux = repo.findByEmail(objDto.getEmail());
    if (aux != null && !aux.getId().equals(uriId)) {
      list.add(new FieldMessage("email", "E-mail já existente"));
    }

    for (FieldMessage e : list) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(e.getMessage())
          .addPropertyNode(e.getFieldName()).addConstraintViolation();
    }
    return list.isEmpty();
  }

}