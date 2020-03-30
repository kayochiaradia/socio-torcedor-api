package br.com.socio.torcedor.repository;

import br.com.socio.torcedor.model.SocioTorcedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocioTorcedorRepository extends JpaRepository<SocioTorcedor, Integer> {

    Optional<SocioTorcedor> findByEmailIgnoreCase(String email);

}
