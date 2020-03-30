package br.com.socio.torcedor.controller;

import br.com.socio.torcedor.exception.SocioTorcedorJaCadastradoException;
import br.com.socio.torcedor.model.ErrorInfo;
import br.com.socio.torcedor.model.SocioTorcedor;
import br.com.socio.torcedor.service.CampanhaService;
import br.com.socio.torcedor.service.SocioTorcedorService;
import br.com.socio.torcedor.vo.CampanhaVO;
import br.com.socio.torcedor.vo.SocioTorcedorVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/socios")
@Api(value = "Sócio Torcedor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
        tags = {"Endpoints do Sócio Torcedor"})
public class SocioTorcedorController {

    private static final Logger LOGGER = LogManager.getLogger(SocioTorcedorController.class);

    private final SocioTorcedorService socioTorcedorService;

    private final CampanhaService campanhaService;

    @Autowired
    public SocioTorcedorController(SocioTorcedorService socioTorcedorService, CampanhaService campanhaService) {
        this.socioTorcedorService = socioTorcedorService;
        this.campanhaService = campanhaService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @HystrixCommand(fallbackMethod = "retornaStatusCreated")
    @ApiOperation(value = "Cria um novo sócio torcedor com base nos parametros passados",
            notes = "Cria um novo sócio torcedor e retorna a lista de campanhas associadas ao time do torcedor" +
                    ",caso o serviço de campanhas esteja indisponível o serviço cria o sócio torcedor e retorna o status " +
                    "201 criado",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<CampanhaVO>> cadastrarSocioTorcedor(@Valid @RequestBody SocioTorcedorVO socioTorcedorVO) {

        try {

            final List<CampanhaVO> campanhasByTimeCoracao =
                    campanhaService.getCampanhasByTimeCoracao(socioTorcedorVO.getTimeCoracao());

            final SocioTorcedor socioTorcedor =
                    socioTorcedorService.cadastrarSocioTorcedor(socioTorcedorVO);
            LOGGER.info("Sócio Torcedor : {} cadastrado com sucesso", socioTorcedor);

            return new ResponseEntity<>(campanhasByTimeCoracao,
                    HttpStatus.CREATED);

        } catch (ConstraintViolationException ex) {

            LOGGER.info("Sócio Torcedor com e-mail: {} já cadastrado", socioTorcedorVO.getEmail());
            throw new SocioTorcedorJaCadastradoException();
        }
    }

    private ResponseEntity<List<CampanhaVO>> retornaStatusCreated(SocioTorcedorVO socioTorcedorVO) {

        try {

            final SocioTorcedor socioTorcedor = socioTorcedorService.cadastrarSocioTorcedor(socioTorcedorVO);

            LOGGER.info("Sócio torcedor : {} cadastrado com sucesso", socioTorcedor);
            LOGGER.info("Nao foi possível conectar-se com o servidor de campanhas", socioTorcedor);

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (DuplicateKeyException ex) {

            LOGGER.error("Sócio torcedor com e-amail: {} já cadastrado", socioTorcedorVO.getEmail());
            throw new SocioTorcedorJaCadastradoException();
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SocioTorcedorJaCadastradoException.class)
    @ResponseBody ErrorInfo
    handleSocioTorcedorJaCadastradoException( SocioTorcedorJaCadastradoException ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() ,ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ErrorInfo handleInternalServerError(Exception ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() , ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    ErrorInfo
    handleHttpMessageNotReadableException( HttpMessageNotReadableException ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() ,ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody ErrorInfo
    handleValidationException( MethodArgumentNotValidException ex) {
        return new ErrorInfo(ServletUriComponentsBuilder.fromCurrentRequest().path("").toUriString() ,ex);
    }
}
