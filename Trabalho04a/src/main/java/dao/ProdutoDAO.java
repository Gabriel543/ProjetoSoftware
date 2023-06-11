package dao;

import java.util.List;

import excecao.ObjetoNaoEncontradoException;
import modelo.Produto;

public interface ProdutoDAO {
	long inclui(Produto umProduto);

	void altera(Produto umProduto) throws ObjetoNaoEncontradoException;

	void exclui(long id) throws ObjetoNaoEncontradoException;

	Produto recuperaUmProduto(long numero) throws ObjetoNaoEncontradoException;

	List<Produto> recuperaProdutos();
}