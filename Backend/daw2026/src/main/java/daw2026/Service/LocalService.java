package daw2026.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import daw2026.Model.Local;
import daw2026.Model.User;
import daw2026.Repository.LocalRepository;
import daw2026.Repository.UserRepository;
import daw2026.exception.LocalAlreadyExistsException;
import daw2026.exception.ResourceNotFoundException;
import daw2026.exception.UnauthorizedException;

@Service
public class LocalService {

    private final LocalRepository localRepository;
    private final UserRepository userRepository;

    public LocalService(LocalRepository localRepository, UserRepository userRepository) {
        this.localRepository = localRepository;
        this.userRepository = userRepository;
    }

    public List<Local> findAll() {
        return localRepository.findAll();
    }

    public Optional<Local> findByName(String name) {
        return localRepository.findByName(name);
    }

    
    public Local createLocal(Long userId, Local local) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + userId + " no encontrado."));
        
        if (localRepository.findByName(local.getName()).isPresent()) {
            throw new LocalAlreadyExistsException("El local '" + local.getName() + "' ya existe.");
        }
        local.setUser(user);
        return localRepository.save(local);
    }

    public Local updateLocal(Long userId, Local local) {
        Local existingLocal = localRepository.findById(local.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Local con ID " + local.getId() + " no encontrado."));
        if (!existingLocal.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("No tienes permiso para editar este local.");
        }
        if (!existingLocal.getName().equals(local.getName())) {
            if (localRepository.findByName(local.getName()).isPresent()) {
                throw new LocalAlreadyExistsException("El local '" + local.getName() + "' ya existe.");
            }
        }
        
        return localRepository.save(local);
    }

    public void deleteLocal(Long userId, Long id) {
        Local local = localRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Local con ID " + id + " no encontrado."));
        if (!local.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("No tienes permiso para eliminar este local.");
        }
        
        localRepository.deleteById(id);
    }
}