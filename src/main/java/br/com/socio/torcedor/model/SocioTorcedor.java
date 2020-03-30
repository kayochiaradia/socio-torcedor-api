package br.com.socio.torcedor.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SOCIO_TORCEDOR")
public class SocioTorcedor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Size(min = 5, max = 100, message = "Nome tem capacidade de 5 a 100 caracteres.")
    @Column(name = "NOME_COMPLETO")
    private String nomeCompleto;

    @Size(min = 5, max = 150, message = "E-mail tem capacidade de 5 a 150 caracteres.")
    @Email
    @Column(name = "EMAIL")
    private String email;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "DATA_NASCIMENTO")
    private LocalDate dataNascimento;

    @Size(min = 5, max = 100, message = "Time do coração tem a capacidade de 5 a 100 caracteres.")
    @Column(name = "TIME_CORACAO")
    private String timeCoracao;

    public SocioTorcedor(String nomeCompleto, String email, LocalDate dataNascimento, String timeCoracao) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.timeCoracao = timeCoracao;
    }

}
