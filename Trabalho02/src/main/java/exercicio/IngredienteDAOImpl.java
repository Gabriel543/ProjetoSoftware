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
        {	// transiente - objeto novo: ainda n�o persistente
            // persistente - apos ser persistido
            // destacado - objeto persistente n�o vinculado a um entity manager

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
                throw new IngredienteNaoEncontradoException("Ingrediente n�o encontrado");
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
                throw new IngredienteNaoEncontradoException("Ingrediente n�o encontrado");
            }

            // COMO PARA REMOVER UM INGREDIENTE NA JPA � PRECISO PRIMEIRAMENTE
            // RECUPER�-LO, QUANDO O  INGREDIENTE �  RECUPERADO SEU N�MERO  DE
            // VERS�O  J�  ATUALIZADO  VEM  JUNTO,  O  QUE  FAZ  COM QUE O
            // CONTROLE DE VERS�O N�O FUNCIONE SE A REMO��O ACONTECER AP�S
            // UMA ATUALIZA��O, OU SE OCORREREM DUAS REMO��ES EM  PARALELO
            // DO MESMO INGREDIENTE.

            // LOGO, A  EXCE��O  OptimisticLockException NUNCA  ACONTECER�
            // NO CASO DE REMO��ES.

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

            // Caracter�sticas no m�todo find():
            // 1. � gen�rico: n�o requer um cast.
            // 2. Retorna null caso a linha n�o seja encontrada no banco.

            if(umIngrediente == null)
            {	throw new IngredienteNaoEncontradoException("Ingrediente n�o encontrado");
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
