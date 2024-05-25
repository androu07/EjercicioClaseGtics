package com.example.lab9.Controller;

import com.example.lab9.Entity.Personajes;
import com.example.lab9.Repository.PersonajesRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class WSController {

    final
    PersonajesRepository personajesRepository;

    public WSController(PersonajesRepository personajesRepository) {
        this.personajesRepository = personajesRepository;
    }

    @GetMapping("/ws/personaje/list")
    public List<Personajes> listaPersonajes(){
        return personajesRepository.findAll();
    }

    @GetMapping("/ws/personaje/{id}")
    public ResponseEntity<HashMap<String, Object>> obtenerPersonajePorId(@PathVariable("id") String idStr){

        HashMap<String, Object> responseJson = new HashMap<>();

        try{
           int id = Integer.parseInt(idStr);
           Optional<Personajes> optPersonajes = personajesRepository.findById(id);
           if(optPersonajes.isPresent()){
               responseJson.put("personaje", optPersonajes.get());
               return ResponseEntity.ok(responseJson);
           }
           else{
               LocalDateTime fechaHoraActual = LocalDateTime.now();
               responseJson.put("date", fechaHoraActual);
               responseJson.put("error", "ID Personaje NO encontrado.");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseJson);
           }
        }
        catch(NumberFormatException e){
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            responseJson.put("date", fechaHoraActual);
            responseJson.put("error", "ID Personaje NO encontrado.");
            return ResponseEntity.badRequest().body(responseJson);
        }

    }

    @PostMapping("/ws/personaje/save")
    public ResponseEntity<HashMap<String, Object>> guardarPersonaje(
            @RequestBody Personajes personajes,
            @RequestParam(value = "fetchid", required = false) boolean fetchId) {

        HashMap<String, Object> responseMap = new HashMap<>();

        personajesRepository.save(personajes);
        if(fetchId){
            responseMap.put("ID", personajes.getId());
        }
        responseMap.put("personaje creado", personajes);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);

    }

    @PutMapping("/ws/personaje/save")
    public ResponseEntity<HashMap<String, Object>> actualizarPersonaje(@RequestBody Personajes personajes) {

        HashMap<String, Object> responseMap = new HashMap<>();

        Optional<Personajes> optPersonajes = personajesRepository.findById(personajes.getId());
        if(optPersonajes.isPresent()){
            personajesRepository.save(personajes);
            responseMap.put("personaje actualizado", personajes);
            return ResponseEntity.ok(responseMap);
        }
        else{
            responseMap.put("error", "El personaje para actualizar no existe.");
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            responseMap.put("date", fechaHoraActual);
            return ResponseEntity.badRequest().body(responseMap);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String, String>> gestionExcepcion(HttpServletRequest request) {
        HashMap<String, String> responseMap = new HashMap<>();
        if(request.getMethod().equals("POST") || request.getMethod().equals("PUT")){
            responseMap.put("error", "Error en validación de Datos.");
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            String fechaHoraActualString = fechaHoraActual.toString();
            responseMap.put("date", fechaHoraActualString);
        }
        return ResponseEntity.badRequest().body(responseMap);

    }

    @DeleteMapping("/ws/personaje/delete/{id}")
    public ResponseEntity<HashMap<String, Object>> eliminarPersonaje(@PathVariable("id") String idStr){
        HashMap<String, Object> responseMap = new HashMap<>();

        try {
            int id = Integer.parseInt(idStr);
            if(personajesRepository.existsById(id)){
                personajesRepository.deleteById(id);
                return ResponseEntity.ok(responseMap);
            }
            else {
                responseMap.put("error", "ID Personaje NO encontrado.");
                LocalDateTime fechaHoraActual = LocalDateTime.now();
                responseMap.put("date", fechaHoraActual);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
            }
        }
        catch(NumberFormatException ex){
            responseMap.put("error", "ID Personaje NO encontrado.");
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            responseMap.put("date", fechaHoraActual);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }
    }

}
