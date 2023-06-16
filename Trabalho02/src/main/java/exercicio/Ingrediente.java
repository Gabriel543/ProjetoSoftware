package exercicio;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import javax.persistence.*;

@Entity // É uma entidade relacional( é uma tabela)
@DynamicInsert
@DynamicUpdate
@Table(name="ingrediente")
public class Ingrediente {
    private Long id;
    private String nome;

    private int versao;

    public Ingrediente(){}

    public Ingrediente(String nome) {
        this.nome = nome;
    }

    @Id // Chave primaria
    @GeneratedValue(strategy= GenerationType.IDENTITY) // Valor gerado - auto incremente
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="nome")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Version // Versionamento - Lock otimista
    @Column(name = "versao")
    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }
}
