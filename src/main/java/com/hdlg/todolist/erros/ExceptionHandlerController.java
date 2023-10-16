package com.hdlg.todolist.erros;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



// A anotação @ControllerAdvice é usada para criar um controlador global de exceções. Isso significa que este controlador trata exceções em toda a aplicação.
@ControllerAdvice
public class ExceptionHandlerController {

    // Este método trata exceções do tipo HttpMessageNotReadableException.
//     anotação @ExceptionHandler é usada para marcar um método como um manipulador de exceções.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        // Cria uma resposta HTTP com status 400 (Bad Request).
        // O corpo da resposta contém a mensagem da exceção mais específica.
        return ResponseEntity.status(400).body(e.getMostSpecificCause().getMessage());
    }


         //  Pode-se criar mais classes de excepcpoes se for necessario para ser tratadas em toda aplicacao vao passar daqui
         //   Se for encontrada a excpcao do tipo vai ser tratada. No exemplo acima temos o tipo de excpction  HttpMessageNotReadableException que ee lancada,
         //  Quando o na entidade task no atributo title ultrapassa o length definido.
}

