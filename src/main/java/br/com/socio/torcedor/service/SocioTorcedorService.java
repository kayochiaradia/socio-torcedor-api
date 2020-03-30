package br.com.socio.torcedor.service;


import br.com.socio.torcedor.model.SocioTorcedor;
import br.com.socio.torcedor.vo.SocioTorcedorVO;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDate;

public interface SocioTorcedorService {
    SocioTorcedor cadastrarSocioTorcedor(SocioTorcedorVO socioTorcedorVO)
            throws DuplicateKeyException;
}
