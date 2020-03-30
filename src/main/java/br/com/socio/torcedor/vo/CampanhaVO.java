package br.com.socio.torcedor.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "CampanhaResource", description = "Representa os dados da campanha que devem ser recebidos pela API Rest de campanha")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampanhaVO extends RepresentationModel<CampanhaVO> {

    @ApiModelProperty(value = "Nome da campanha", dataType = "string", required = true)
    private String nome;

    @ApiModelProperty(value = "Id do time do coração", dataType = "string", required = true)
    private String timeCoracaoId;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "Data de inicio de vigência", dataType = "date", required = true)
    private LocalDate inicioVigencia;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(value = "Data de fim de vigência", dataType = "date", required = true)
    private LocalDate fimVigencia;

    @JsonIgnore
    private String chave;

    public CampanhaVO(String nome, String timeCoracaoId, LocalDate inicioVigencia, LocalDate fimVigencia) {
        this.nome = nome;
        this.timeCoracaoId = timeCoracaoId;
        this.inicioVigencia = inicioVigencia;
        this.fimVigencia = fimVigencia;
    }
}
