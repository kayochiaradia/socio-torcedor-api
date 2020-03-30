package br.com.socio.torcedor.repository;

import br.com.socio.torcedor.base.IntegrationBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SocioTorcedorRepositoryTest extends IntegrationBaseTest {

    @Autowired
    private SocioTorcedorRepository socioTorcedorRepository;

    @Test
    public void deveTrazerApenasUmRegistroDeSocioTorcedor() {

        assertThat(socioTorcedorRepository.findByEmailIgnoreCase("joao@teste.com"))
                .as("Deve Trazer somente um registro")
                .isNotEmpty();
    }

}
