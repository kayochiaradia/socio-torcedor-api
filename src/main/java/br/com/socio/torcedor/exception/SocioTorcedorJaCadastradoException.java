package br.com.socio.torcedor.exception;

public class SocioTorcedorJaCadastradoException extends RuntimeException {

    public SocioTorcedorJaCadastradoException() {
        super("Usuário já cadastrado");
    }
}
