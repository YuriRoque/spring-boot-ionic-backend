package com.yuri.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.yuri.cursomc.domain.Cliente;
import com.yuri.cursomc.domain.enums.TipoCliente;
import com.yuri.cursomc.dto.ClienteNewDTO;
import com.yuri.cursomc.repositories.ClienteRepository;
import com.yuri.cursomc.resources.exceptions.FieldMessage;
import com.yuri.cursomc.services.validation.utils.BR;

import org.springframework.beans.factory.annotation.Autowired;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

  @Autowired
  private ClienteRepository repo;

  @Override
  public void initialize(ClienteInsert ann) {
  }

  @Override
  public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {

    List<FieldMessage> list = new ArrayList<>();

    if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
      list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
    }

    if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
      list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
    }

    Cliente aux = repo.findByEmail(objDto.getEmail());
    if (aux != null) {
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