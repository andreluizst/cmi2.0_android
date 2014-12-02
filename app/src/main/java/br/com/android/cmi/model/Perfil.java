package br.com.android.cmi.model;

import java.io.Serializable;

public class Perfil implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 9L;
	
	
	private Integer codPerfil;
    private String descricao;
    
    public Perfil(){
        
    }
    
    public Perfil(Integer codPerfil, String descricao){
        this.codPerfil = codPerfil;
        this.descricao = descricao;
    }

    /**
     * @return the codPerfil
     */
    public Integer getCodPerfil() {
        return codPerfil;
    }

    /**
     * @param codPerfil the codPerfil to set
     */
    public void setCodPerfil(Integer codPerfil) {
        this.codPerfil = codPerfil;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codPerfil == null) ? 0 : codPerfil.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Perfil other = (Perfil) obj;
        if (codPerfil == null) {
            if (other.codPerfil != null) {
                return false;
            }
        } else if (!codPerfil.equals(other.codPerfil)) {
            return false;
        }
        return true;
    }

}