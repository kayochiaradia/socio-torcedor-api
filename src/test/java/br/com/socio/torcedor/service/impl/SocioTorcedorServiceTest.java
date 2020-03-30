package br.com.socio.torcedor.service.impl;

import br.com.socio.torcedor.base.IntegrationBaseTest;
import br.com.socio.torcedor.model.SocioTorcedor;
import br.com.socio.torcedor.service.SocioTorcedorService;
import br.com.socio.torcedor.vo.SocioTorcedorVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class SocioTorcedorServiceTest extends IntegrationBaseTest {

    @Autowired
    private SocioTorcedorService socioTorcedorService;

    @Test
    public void socioTorcedorDeveSerCadastradoComDadosCorretos(){
        SocioTorcedor socioTorcedor = socioTorcedorService.cadastrarSocioTorcedor(new SocioTorcedorVO("João das neves", "joaodasneves251@teste.com", LocalDate.of(2020, 5, 1), "Corinthians"));

        assertThat(socioTorcedor)
                .as("O socio torcedor deve ser cadastrado com sucesso")
                .isNotNull();
    }
}
