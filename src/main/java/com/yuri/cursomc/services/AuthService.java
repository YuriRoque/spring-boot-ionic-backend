package com.yuri.cursomc.services;

import java.util.Random;

import com.yuri.cursomc.domain.Cliente;
import com.yuri.cursomc.repositories.ClienteRepository;
import com.yuri.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private ClienteRepository clienteRepository;
  
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private EmailService emailService;

  private Random rand = new Random();

  public void sendNewPassword(String email) {

    Cliente cliente = clienteRepository.findByEmail(email);

    if (cliente == null) {
      throw new ObjectNotFoundException("E-mail não encontrado");
    }

    String newPass = newPassword();
    
    cliente.setSenha(bCryptPasswordEncoder.encode(newPass));
    clienteRepository.save(cliente);

    emailService.sendNewPasswordEmail(cliente, newPass);
      


  }

  private String newPassword() {

    char[] vet = new char[10];

    for (int i = 0; i < vet.length; i++) {
      vet[i] = randomChar();
    }

    return new String(vet);

  }

  // De acordo com o unicode "(rand.nextInt(10) + 48)" de cada número e letra, gera a senha, usando a classe Random do Java para ir colocando letras e numeros na senha de forma aleatória
  private char randomChar() {

    int opt = rand.nextInt(3);

    /* De acordo com a tabela unicode o número 0 começa no codigo 48, como o rand gera de 0 a 9, ele vai gerar os números a partir daí
    A mesma lógica se aplica às letras minusculas e maiuculas */
    if (opt == 0) { // gera numero
      return (char) (rand.nextInt(10) + 48);
    } else if (opt == 1) { // gera letra maiuscula
      return (char) (rand.nextInt(26) + 65);
    } else { // gera letra minuscula
      return (char) (rand.nextInt(26) + 97);
    }

  }

}
