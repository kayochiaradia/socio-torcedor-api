package br.com.socio.torcedor.controller;

import br.com.socio.torcedor.base.IntegrationBaseTest;
import br.com.socio.torcedor.exception.SocioTorcedorJaCadastradoException;
import br.com.socio.torcedor.model.SocioTorcedor;
import br.com.socio.torcedor.repository.SocioTorcedorRepository;
import br.com.socio.torcedor.service.CampanhaService;
import br.com.socio.torcedor.vo.CampanhaVO;
import br.com.socio.torcedor.vo.SocioTorcedorVO;
import feign.RetryableException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

public class SocioTorcedorControllerTest extends IntegrationBaseTest {

    @Autowired
    private SocioTorcedorController socioTorcedorController;

    @Autowired
    private SocioTorcedorRepository socioTorcedorRepository;

    @MockBean
    private CampanhaService campanhaService;

    @Before
    public void setUp() throws Exception {
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);

        List<CampanhaVO> campanhas = new ArrayList<>();
        campanhas.add(new CampanhaVO("Campanha 2",  "Corinthians",
                LocalDate.of(2017,10,05),LocalDate.of(2017,10,9)));
        campanhas.add(new CampanhaVO("Campanha 4",  "RedBull",
                LocalDate.of(2017,10,10),LocalDate.of(2017,10,20)));

        given(this.campanhaService.getCampanhasByTimeCoracao("Corinthians"))
                .willReturn(campanhas);
    }

    @Test
    public void quandoSocioTorcedorCadastradoDeveRetornarListaDeCampanhas() throws Exception {
        final ResponseEntity<List<CampanhaVO>> responseEntity =
                socioTorcedorController.cadastrarSocioTorcedor(new SocioTorcedorVO("Kayo Chiaradia", "kayo.chiaradia@gmail.com",
                        LocalDate.of(1995, 9 , 30), "Corinthians"));

        assertThat(responseEntity).as("O Sócio torcedor deve ser criado com sucesso").isNotNull();
        assertThat(responseEntity.getStatusCode()).as("O Status code deve ser created").isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).as("Deve retornar a lista de campanhas").hasSize(2);
    }

    @Test
    public void quandoServicoDeCampanhasEstiverForaHystrixDeveChamarCallbackECadastrarSocioERetornarCreated() throws Exception {

        given(this.campanhaService.getCampanhasByTimeCoracao("Corinthians"))
                .willThrow(RetryableException.class);

        final ResponseEntity<List<CampanhaVO>> responseEntity =
                socioTorcedorController.cadastrarSocioTorcedor(new SocioTorcedorVO("Kayo Chiaradia", "kayo.chiaradia@hotmail.com",
                        LocalDate.of(1995, 9 , 30), "Corinthians"));

        assertThat(responseEntity).as("O Sócio torcedor deve ser criado com sucesso").isNotNull();
        assertThat(responseEntity.getStatusCode()).as("O Status code deve ser created").isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void naoDeveCadastrarDoisUsuariosComMesmoEmail() throws Exception{

        assertThatExceptionOfType(SocioTorcedorJaCadastradoException.class)
                .isThrownBy(() ->  socioTorcedorController.cadastrarSocioTorcedor(new SocioTorcedorVO("Kayo Chiaradia", "kayo.chiaradia@hotmail.com",
                        LocalDate.of(1995, 9 , 30), "Corinthians")))
                .withMessageContaining("Usuário já cadastrado");

    }
}