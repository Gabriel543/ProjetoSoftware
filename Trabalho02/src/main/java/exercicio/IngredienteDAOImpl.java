package exercicio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;

public class IngredienteDAOImpl implements IngredienteDAO{

    public long inclui(Ingrediente umIngrediente)
    {	EntityManager em = null;
        EntityTransaction tx = null;

        try
        {	// transiente - objeto novo: ainda não persistente
            // persistente - apos ser persistido
            // destacado - objeto persistente não vinculado a um entity manager

            em = FabricaDeEntityManager.criarSessao();
            tx = em.getTransaction();
            tx.begin();

            em.persist(umIngrediente);

            tx.commit();
            return umIngrediente.getId();
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
        }finally
        {   em.close();
        }

    }


    public void altera(Ingrediente umIngrediente) throws IngredienteNaoEncontradoException {
        EntityManager em = null;
        EntityTransaction tx = null;
        Ingrediente ingrediente = null;
        try
        {
            em = FabricaDeEntityManager.criarSessao();
            tx = em.getTransaction();
            tx.begin();

            ingrediente = em.find(Ingrediente.class, umIngrediente.getId(), LockModeType.PESSIMISTIC_WRITE);

            if(ingrediente == null)
            {	tx.rollback();
                throw new IngredienteNaoEncontradoException("Ingrediente não encontrado");
            }
            em.merge(umIngrediente);
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


    public void exclui(long id) throws IngredienteNaoEncontradoException {
        EntityManager em = null;
        EntityTransaction tx = null;

        try
        {
            em = FabricaDeEntityManager.criarSessao();
            tx = em.getTransaction();
            tx.begin();

            Ingrediente ingrediente = em.find(Ingrediente.class, id, LockModeType.PESSIMISTIC_WRITE);

            if(ingrediente == null)
            {	tx.rollback();
                throw new IngredienteNaoEncontradoException("Ingrediente não encontrado");
            }

            // COMO PARA REMOVER UM INGREDIENTE NA JPA É PRECISO PRIMEIRAMENTE
            // RECUPERÁ-LO, QUANDO O  INGREDIENTE É  RECUPERADO SEU NÚMERO  DE
            // VERSÃO  JÁ  ATUALIZADO  VEM  JUNTO,  O  QUE  FAZ  COM QUE O
            // CONTROLE DE VERSÃO NÃO FUNCIONE SE A REMOÇÃO ACONTECER APÓS
            // UMA ATUALIZAÇÃO, OU SE OCORREREM DUAS REMOÇÕES EM  PARALELO
            // DO MESMO INGREDIENTE.

            // LOGO, A  EXCEÇÃO  OptimisticLockException NUNCA  ACONTECERÁ
            // NO CASO DE REMOÇÕES.

            em.remove(ingrediente);

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


    public Ingrediente recuperaUmIngrediente(long numero) throws IngredienteNaoEncontradoException {
        EntityManager em = null;

        try
        {	em = FabricaDeEntityManager.criarSessao();

            Ingrediente umIngrediente = em.find(Ingrediente.class, numero);

            // Características no método find():
            // 1. É genérico: não requer um cast.
            // 2. Retorna null caso a linha não seja encontrada no banco.

            if(umIngrediente == null)
            {	throw new IngredienteNaoEncontradoException("Ingrediente não encontrado");
            }
            return umIngrediente;
        }
        finally
        {   em.close();
        }
    }


    public List<Ingrediente> recuperaIngredientes() {
        EntityManager em = null;

        try
        {	em = FabricaDeEntityManager.criarSessao();

            List<Ingrediente> ingredientes = em
                    .createQuery("select p from Ingrediente p order by p.id")
                    .getResultList();

            // Retorna um List vazio caso a tabela correspondente esteja vazia.

            return ingredientes;
        }
        finally
        {   em.close();
        }
    }
}
