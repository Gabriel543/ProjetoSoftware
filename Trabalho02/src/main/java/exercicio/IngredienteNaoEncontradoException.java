package exercicio;

public class IngredienteNaoEncontradoException extends Exception {

    private final static long serialVersionUID = 1;

    private int codigo;

    public IngredienteNaoEncontradoException(String msg)
    {	super(msg);
    }

    public IngredienteNaoEncontradoException(int codigo, String msg)
    {	super(msg);
        this.codigo = codigo;
    }

    public int getCodigoDeErro()
    {	return codigo;
    }
}
