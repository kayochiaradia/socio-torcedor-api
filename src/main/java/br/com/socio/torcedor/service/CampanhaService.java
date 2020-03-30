package br.com.socio.torcedor.service;


import br.com.socio.torcedor.vo.CampanhaVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "campanhaService", url = "http://localhost:8080")
public interface CampanhaService {

    @RequestMapping("/v1/campanhas/time-coracao/{timeCoracao}")
    List<CampanhaVO> getCampanhasByTimeCoracao(@PathVariable("timeCoracao") String timeCoracao);

}
