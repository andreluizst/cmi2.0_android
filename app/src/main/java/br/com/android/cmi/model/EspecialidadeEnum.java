package br.com.android.cmi.model;

import java.io.Serializable;

/**
 *
 * @author Bruno
 */
public enum EspecialidadeEnum implements Serializable, Selectable {
    
    CARDIOLOGISTA("Cardiologista","cardiologista"),
    ENDOCRINOLOGISTA("Endocrinologista","endocrinologista"),
    CLINICO_GERAL("Clínico Geral","clinico_geral"),
    OFTAMOLOGISTA("Oftamologista","oftamologista"),
    OTORRINOLARINGOLOGISTA("Otorrinolaringologista","otorrinolaringologista"),
    DERMATOLOGISTA("Dermalogista","dermatolgista"),
    GASTROENTEROLOGISTA("Gastroenterologista","Gastroenterologista"),
    NEUROLOGISTA("Neurologista","neurologista");
    
    private String nome;
    private String valor;
    
    private EspecialidadeEnum(String nome,String valor)
    {
        this.nome = nome;
        this.valor = valor;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }
    
    public static EspecialidadeEnum getByValor(String valor) {
        for (EspecialidadeEnum especialidade : values()) {
            if (especialidade.getValor().equals(valor)) {
                return especialidade;
            }
        }
        return null;
    }    
    
}
