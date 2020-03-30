package br.com.socio.torcedor.service.impl;


import br.com.socio.torcedor.model.SocioTorcedor;
import br.com.socio.torcedor.repository.SocioTorcedorRepository;
import br.com.socio.torcedor.service.SocioTorcedorService;
import br.com.socio.torcedor.vo.SocioTorcedorVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Validated
public class SocioTorcedorServiceImpl implements SocioTorcedorService {

    private static final Logger LOGGER = LogManager.getLogger(SocioTorcedorService.class);

    private final SocioTorcedorRepository socioTorcedorRepository;

    public SocioTorcedorServiceImpl(SocioTorcedorRepository socioTorcedorRepository) {
        this.socioTorcedorRepository = socioTorcedorRepository;
    }

    public SocioTorcedor cadastrarSocioTorcedor(SocioTorcedorVO socioTorcedorVO)
            throws DuplicateKeyException {

        final SocioTorcedor socioTorcedor = new SocioTorcedor(socioTorcedorVO.getNomeCompleto(),
                socioTorcedorVO.getEmail(), socioTorcedorVO.getDataNascimento(), socioTorcedorVO.getTimeCoracao());

        Optional<SocioTorcedor> socioTorcedorOptional = socioTorcedorRepository.findByEmailIgnoreCase(socioTorcedor.getEmail());

        if(socioTorcedorOptional.isPresent()) {
            if(socioTorcedorOptional.get().getEmail().equals(socioTorcedor.getEmail())) {
                throw new DuplicateKeyException("Email já cadastrado");
            }
        }

        LOGGER.info("Cadastrando Sócio Torcedor : {}", socioTorcedor);

        return socioTorcedorRepository.save(socioTorcedor);

    }
}
