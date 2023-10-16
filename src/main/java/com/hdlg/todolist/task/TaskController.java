package com.hdlg.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdlg.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

         @Autowired
         private TaskRepository taskRepository;

         @PostMapping
         public ResponseEntity create (@RequestBody TaskModel taskModel, HttpServletRequest request){

                  System.out.println("Chegou no controller, id=: " + request.getAttribute("idUser"));

                  // setando o id do usuario logado
                  taskModel.setIdUser((UUID) request.getAttribute("idUser"));
                  // verificando se a data de inicio ou fim de actividades nao ee maior ou menor que a actual
                  var currentDate = LocalDateTime.now();
                  if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
                           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio / fim deve ser maior do que a data actual");
                  }

                  if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
                           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor do que a data fim");
                  }

                  // salvando os dados
                  var task = this.taskRepository.save(taskModel);
                  // devolvendo a resposta de sucesso
                  return ResponseEntity.status(HttpStatus.OK).body(task);                 

         }

         @GetMapping
         public List<TaskModel> list( HttpServletRequest request){
                  var idUser = request.getAttribute("idUser");
                  var tasks = this.taskRepository.findByIdUser(idUser);
                  return tasks;

         }

         // @PutMapping("/{id}")
         // public TaskModel update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request){

         //          var idUser = request.getAttribute("idUser");
         //          System.out.println("userId: "+idUser);
                  
         //          var createAt = taskRepository.findById(id).get().getCreateAt();
         //          System.out.println("CreateAt: "+createAt);
                  
         //          taskModel.setIdUser((UUID)idUser); 
         //          taskModel.setCreateAt((LocalDateTime) createAt);
         //          taskModel.setId(id);
         //          return this.taskRepository.save(taskModel);
         // }

         // Forma mais simples de fazer mudanca parcial sem perder os dados que nao vao ser setados, no anterios tinhamos perda de dados por isso tinhamos muitos sets
         @PutMapping("/{id}")
         public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request){
                  
                  var task = taskRepository.findById(id).orElse(null);
                  if (task == null) {
                           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa nao encontrada");
                  }

                  var idUser = request.getAttribute("idUser");
                  if (!task.getIdUser().equals(idUser)) {
                           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario nao tem permissao para alterar a tarefa");
                  }

                  Utils.copyNonNullProperties(taskModel, task);
                  
                  var taskUpdate = this.taskRepository.save(task);

                  return ResponseEntity.status(HttpStatus.OK).body(taskUpdate);  
         }
         
}
