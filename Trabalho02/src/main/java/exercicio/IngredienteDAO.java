package exercicio;

import java.util.List;

public interface IngredienteDAO {

    long inclui(Ingrediente umIngrediente);

    void altera(Ingrediente umIngrediente)
            throws IngredienteNaoEncontradoException;

    void exclui(long id)
            throws IngredienteNaoEncontradoException;

    Ingrediente recuperaUmIngrediente(long numero)
            throws IngredienteNaoEncontradoException;

    List<Ingrediente> recuperaIngredientes();
}
