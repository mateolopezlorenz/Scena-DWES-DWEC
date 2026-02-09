package daw2026.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import daw2026.Model.Local;
import daw2026.Model.User;
import daw2026.Repository.LocalRepository;
import daw2026.Repository.UserRepository;

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

    public Optional<Local> createLocal(User user, Local local) {
        if (userRepository.findByName(user.getName()).isEmpty()) {
            return Optional.empty();
        }
        if (localRepository.findByName(local.getName()).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(localRepository.save(local));
    }

    public Optional<Local> updateLocal(User user, Local local) {
        if (userRepository.findByName(user.getName()).isEmpty()) {
            return Optional.empty();
        }
        if (localRepository.findById(local.getId()).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(localRepository.save(local));
    }

    public boolean deleteLocal(Long id) {
        if (localRepository.findById(id).isPresent()) {
            localRepository.deleteById(id);
            return true;
        }
        return false;
    }
}