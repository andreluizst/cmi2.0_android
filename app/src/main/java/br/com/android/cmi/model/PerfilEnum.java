package br.com.android.cmi.model;


import java.io.Serializable;

/**
 *
 * @author Bruno
 */
public enum PerfilEnum implements Serializable, Selectable {
    
    ADMINISTRADOR("Administrador","administrador"),
    GERENTE("Gerente","gerente"),
    MEDICO("Médico","medico"),
    SECRETARIA("Secretária","secretaria"),
    PACIENTE("Paciente", "paciente");
    
    private String nome;
    private String valor;
    
    private PerfilEnum(String nome, String valor)
    {
        this.nome = nome;
        this.valor = valor;
    }

    /**
     * @return the nome
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * @return the valor
     */
    @Override
    public String getValor() {
        return valor;
    }
    
    public static PerfilEnum getByValor(String valor) {
        for (PerfilEnum perfil : values()) {
            if (perfil.getValor().equals(valor)) {
                return perfil;
            }
        }
        return null;
    }    
}
