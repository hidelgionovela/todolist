package com.hdlg.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hdlg.todolist.user.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

         @Autowired
         private UserRepository userRepository;

         @Override
         protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                           FilterChain filterChain) throws ServletException, IOException {

                  var servletPath = request.getServletPath();

                  if (servletPath.startsWith("/tasks")) {

                           // pegar a autenticacao (usuario e senha)
                           var authorization = request.getHeader("Authorization");
                           System.out.println("Authorization");
                           System.out.println("1. " + authorization);

                           var authEncoded = authorization.substring("Basic".length()).trim();
                           System.out.println("2. " + authEncoded);

                           byte[] authDecode = Base64.getDecoder().decode(authEncoded);
                           System.out.println("3. " + authDecode);

                           var authString = new String(authDecode);
                           System.out.println("4. " + authString);

                           // ["username", "password"] -> split
                           String[] credentials = authString.split(":");
                           String username = credentials[0];
                           String passwordd = credentials[1];

                           System.out.println(username);
                           System.out.println(passwordd);

                           // Validar usuario

                           var user = this.userRepository.findByUsername(username);
                           System.out.println("user: " + user);
                           if (user == null) {
                                    response.sendError(401);
                           } else {
                                    // validar senha
                                    var result = BCrypt.verifyer().verify(passwordd.toCharArray(), user.getPassword());
                                    System.out.println("result: " + result.verified);

                                    if (result.verified) {
                                             // setando o idUser ou seja pegando o id do usuario que esta a tentando cadastrar a tarefa
                                             request.setAttribute("idUser", user.getId());
                                             filterChain.doFilter(request, response);
                                    } else {
                                             response.sendError(401);
                                    }

                           }
                  } else {
                           filterChain.doFilter(request, response);
                  }

         }

}
