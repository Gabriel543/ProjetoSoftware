package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import excecao.InfraestruturaException;

public class JPAUtil {

	// ThreadLocal variavel local a um thread expecífico
	// Um transactionController por Thread
	private static final ThreadLocal<Integer> threadTransactionController = new ThreadLocal<>();
	private static EntityManagerFactory emf;
	private static final ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();
	private static final ThreadLocal<EntityTransaction> threadTransaction = new ThreadLocal<EntityTransaction>();

	static {
		try {
			emf = Persistence.createEntityManagerFactory("exercicio");
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println(">>>>>>>>>> Mensagem de erro: " + e.getMessage());
			throw e;
		}
	}

	public static void beginTransaction() { // System.out.println("Vai criar transacao");

		EntityTransaction tx = threadTransaction.get();

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Entrou em beginTransaction()");
		if(threadTransactionController.get() == null) {
			threadTransactionController.set(0); // Inicia o threadTransactionController

			if (threadTransactionController.get() == 0) { //checa se a transacao pode ser geita
				try {
					if (tx == null) {
						tx = getEntityManager().getTransaction();
						tx.begin();
						threadTransaction.set(tx);
						// System.out.println("Criou transacao");
					} else { // System.out.println("Nao criou transacao");
					}
				} catch (RuntimeException ex) {
					throw new InfraestruturaException(ex);
				}
			}
			threadTransactionController.set(threadTransactionController.get() + 1); //adiciona no threadTransactionController
		}
	}

	public static EntityManager getEntityManager() { // System.out.println("Abriu ou recuperou sessão");

		EntityManager s = threadEntityManager.get();
		// Abre uma nova Sessão, se a thread ainda não possui uma.
		try {
			if (s == null) {
				s = emf.createEntityManager();
				threadEntityManager.set(s);
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Criou o entity manager");
			}
		} catch (RuntimeException ex) {
			throw new InfraestruturaException(ex);
		}
		return s;
	}

	public static void commitTransaction() {
		EntityTransaction tx = threadTransaction.get();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Entrou em commitTransaction");
		if(threadTransactionController.get() < 1  || threadTransactionController.get() == null){//checa se beginTransaction foi chamado
			throw new RuntimeException("commitTransaction chamado impropriamente");
		}
		if(threadTransactionController.get() == 1) {
			try {
				if (tx != null && tx.isActive()) {
					tx.commit();
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Comitou transacao");
				}
				threadTransaction.set(null); //fecha transação na thread
				threadTransactionController.set(0);//reseta o contador
			} catch (RuntimeException ex) {
				try {
					rollbackTransaction();
				} catch (RuntimeException e) {
				}

				throw new InfraestruturaException(ex);
			}
		}else{
			threadTransactionController.set(threadTransactionController.get() - 1); //decrementa o contador
		}

	}

	public static void rollbackTransaction() {
		System.out.println("Vai efetuar rollback de transacao");

		EntityTransaction tx = threadTransaction.get();
		try {
			threadTransaction.set(null); //fecha transação na thread
			threadTransactionController.set(0); //reseta contador
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		} catch (RuntimeException ex) {
			throw new InfraestruturaException(ex);
		} finally {
			closeEntityManager();
		}
	}

	public static void closeEntityManager() { // System.out.println("Vai fechar sessão");

		try {
			EntityManager s = threadEntityManager.get();
			threadEntityManager.set(null);
			if (s != null && s.isOpen()) {
				s.close();
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Fechou o entity manager");
			}

			EntityTransaction tx = threadTransaction.get();
			if (tx != null && tx.isActive()) {
				rollbackTransaction();
				throw new RuntimeException("EntityManager sendo fechado " + "com transação ativa.");
			}
		} catch (RuntimeException ex) {
			throw new InfraestruturaException(ex);
		}
	}
}
