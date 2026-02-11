package daw2026.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import daw2026.Model.Local;
import daw2026.Model.User;
import daw2026.Repository.UserRepository;
import daw2026.Service.LocalService;
import daw2026.exception.LocalAlreadyExistsException;
import daw2026.exception.ResourceNotFoundException;
import daw2026.exception.UnauthorizedException;

@RestController
@RequestMapping("/api/locals")
public class LocalController {

    @Autowired
    private LocalService localService;

    @Autowired
    private UserRepository userRepository;

    // Obtener todos los locales
    @GetMapping("/all")
    public ResponseEntity<List<Local>> getAllLocals() {
        List<Local> locals = localService.findAll();
        return ResponseEntity.ok(locals);
    }

    // Obtener local por nombre
    @GetMapping("/searchLocal")
    public ResponseEntity<Local> getLocalByName(@RequestParam String name) {
        Optional<Local> local = localService.findByName(name);
        if (local.isPresent()) {
            return ResponseEntity.ok(local.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un nuevo local
    @PostMapping("/createLocal")
    public ResponseEntity<?> createLocal(@RequestBody Local local,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Local createdLocal = localService.createLocal(user.getId(), local);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLocal);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (LocalAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el local");
        }
    }

    // Actualizar un local
    @PutMapping("/updateLocal/{id}")
    public ResponseEntity<?> updateLocal(@PathVariable Long id,
                                         @RequestBody Local local,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            local.setId(id);
            Local updatedLocal = localService.updateLocal(user.getId(), local);
            return ResponseEntity.ok(updatedLocal);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (LocalAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el local");
        }
    }

    // Eliminar un local
    @DeleteMapping("/deleteLocal/{id}")
    public ResponseEntity<?> deleteLocal(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            localService.deleteLocal(user.getId(), id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el local");
        }
    }
}
