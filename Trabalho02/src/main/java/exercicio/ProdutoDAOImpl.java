package exercicio;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;

public class ProdutoDAOImpl implements ProdutoDAO
{	
	public long inclui(Produto umProduto) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	// transiente - objeto novo: ainda não persistente
			// persistente - apos ser persistido 
			// destacado - objeto persistente não vinculado a um entity manager
		
			em = FabricaDeEntityManager.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			em.persist(umProduto);
			
			tx.commit();
			return umProduto.getId();
		} 
		catch(RuntimeException e)
		{   
			if (tx != null)
		    {   try
		    	{	tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   em.close();
		}
	}

	public void altera(Produto umProduto) throws ProdutoNaoEncontradoException
	{	EntityManager em = null;
		EntityTransaction tx = null;
		Produto produto = null;
		try
		{	
			em = FabricaDeEntityManager.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			produto = em.find(Produto.class, umProduto.getId(), LockModeType.PESSIMISTIC_WRITE);
			
			if(produto == null)
			{	tx.rollback();
				throw new ProdutoNaoEncontradoException("Produto não encontrado");
			}
			em.merge(umProduto);
			tx.commit();
		} 
		catch(OptimisticLockException e)  // sub-classe de RuntimeException
		{   
			if (tx != null)
		    {   tx.rollback();
		    }
			throw new EstadoDeObjetoObsoletoException();
		}
		catch(RuntimeException e)
		{   
			if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   em.close();
		}
	}

	public void exclui(long numero) throws ProdutoNaoEncontradoException 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			em = FabricaDeEntityManager.criarSessao();
			tx = em.getTransaction();
			tx.begin();

			Produto produto = em.find(Produto.class, numero, LockModeType.PESSIMISTIC_WRITE);
			
			if(produto == null)
			{	tx.rollback();
				throw new ProdutoNaoEncontradoException("Produto não encontrado");
			}

			// COMO PARA REMOVER UM PRODUTO NA JPA É PRECISO PRIMEIRAMENTE
			// RECUPERÁ-LO, QUANDO O  PRODUTO É  RECUPERADO SEU NÚMERO  DE
			// VERSÃO  JÁ  ATUALIZADO  VEM  JUNTO,  O  QUE  FAZ  COM QUE O 
			// CONTROLE DE VERSÃO NÃO FUNCIONE SE A REMOÇÃO ACONTECER APÓS 
			// UMA ATUALIZAÇÃO, OU SE OCORREREM DUAS REMOÇÕES EM  PARALELO 
			// DO MESMO PRODUTO.
			
			// LOGO, A  EXCEÇÃO  OptimisticLockException NUNCA  ACONTECERÁ
			// NO CASO DE REMOÇÕES.

			em.remove(produto);

			tx.commit();
		} 
		catch(RuntimeException e)
		{   
			if (tx != null)
		    {   try
		        {	tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   em.close();
		}
	}

	public Produto recuperaUmProduto(long numero) throws ProdutoNaoEncontradoException
	{	EntityManager em = null;
		
		try
		{	em = FabricaDeEntityManager.criarSessao();

			Produto umProduto = em.find(Produto.class, numero);
			
			// Características no método find():
			// 1. É genérico: não requer um cast.
			// 2. Retorna null caso a linha não seja encontrada no banco.

			if(umProduto == null)
			{	throw new ProdutoNaoEncontradoException("Produto não encontrado");
			}
			return umProduto;
		} 
		finally
		{   em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutos()
	{	EntityManager em = null;
		
		try
		{	em = FabricaDeEntityManager.criarSessao();

			List<Produto> clientes = em
				.createQuery("select p from Produto p order by p.id")
				.getResultList();

			// Retorna um List vazio caso a tabela correspondente esteja vazia.
			
			return clientes;
		} 
		finally
		{   em.close();
		}
	}
}