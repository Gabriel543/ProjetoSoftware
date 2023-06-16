package exercicio;


import java.util.List;

import corejava.Console;

public class Principal
{	public static void main (String[] args) 
	{	
		String nome;
		String descricao;
		double lanceMinimo;
		String dataCadastro;
		Ingrediente umIngrediente;

		IngredienteDAO ingredienteDAO = FabricaDeDAOs.getDAO(IngredienteDAO.class);

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que você deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um ingrediente");
			System.out.println("2. Alterar um ingrediente");
			System.out.println("3. Remover um ingrediente");
			System.out.println("4. Listar todos os ingrediente");
			System.out.println("5. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um número entre 1 e 5:");
					
			switch (opcao)
			{	case 1:
				{
					nome = Console.readLine('\n' + 
						"Informe o nome do ingrediente: ");
					descricao = Console.readLine('\n' +
							"Informe o descrição do ingrediente: ");
						
					umIngrediente = new Ingrediente(nome,descricao);
					
					long numero = ingredienteDAO.inclui(umIngrediente);
					
					System.out.println('\n' + "Ingrediente número " +
					    numero + " incluído com sucesso!");	

					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o número do ingrediente que você deseja alterar: ");
										
					try
					{	umIngrediente = ingredienteDAO.recuperaUmIngrediente(resposta);
					}
					catch(IngredienteNaoEncontradoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}

					System.out.println('\n' + 
						"Número = " + umIngrediente.getId() +
						"    Nome = " + umIngrediente.getNome());
						//"    Lance Mínimo = " + umProduto.getLanceMinimo() +
				        //"    Versão = " + umProduto.getVersao());
												
					System.out.println('\n' + "O que você deseja alterar?");
					System.out.println('\n' + "1. Nome");
					System.out.println('\n' + "2. Descrição");

					int opcaoAlteracao = Console.readInt('\n' + 
											"Digite um número de 1 a 2:");
					
					switch (opcaoAlteracao)
					{	case 1:
							String novoNome = Console.
										readLine("Digite o novo nome: ");
							
							umIngrediente.setNome(novoNome);

							try
							{	ingredienteDAO.altera(umIngrediente);

								System.out.println('\n' + 
									"Alteração de nome efetuada com sucesso!");
							}
							catch(IngredienteNaoEncontradoException e)
							{	System.out.println('\n' + e.getMessage());
							}
							catch(EstadoDeObjetoObsoletoException e)
							{	System.out.println('\n' + "A operação não foi " +
							        "efetuada: os dados que você " +
							    	"tentou salvar foram modificados " +
							    	"por outro usuário.");
							}
								
							break;
					
						case 2:
							String novaDescricao = Console.
									readLine("Digite uma nova Descricao: ");
							
							umIngrediente.setDescricao(novaDescricao);

							try
							{
								ingredienteDAO.altera(umIngrediente);

								System.out.println('\n' + 
									"Alteração da descrição alterada com Sucesso!");
							}catch (IngredienteNaoEncontradoException e)
							{	System.out.println('\n' + e.getMessage());
							}
							catch(EstadoDeObjetoObsoletoException e)
							{	System.out.println('\n' + "A operação não foi " +
							        "efetuada: os dados que você " +
							    	"tentou salvar foram modificados " +
							    	"por outro usuário.");
							}
								
							break;

						default:
							System.out.println('\n' + "Opção inválida!");
					}

					break;
				}

				case 3:
				{	int resposta = Console.readInt('\n' + 
						"Digite o número do ingrediente que você deseja remover: ");
									
					try
					{	umIngrediente = ingredienteDAO.recuperaUmIngrediente(resposta);
					}
					catch(IngredienteNaoEncontradoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"Número = " + umIngrediente.getId() +
						"    Nome = " + umIngrediente.getNome() +
						"	Descricao = " + umIngrediente.getDescricao() +
					    "    Versão = " + umIngrediente.getVersao());
														
					String resp = Console.readLine('\n' + 
						"Confirma a remoção do ingrediente?");

					if(resp.equals("s"))
					{	try
						{	ingredienteDAO.exclui (umIngrediente.getId());
							System.out.println('\n' + 
								"Ingrediente removido com sucesso!");
						}
						catch(IngredienteNaoEncontradoException e)
						{	System.out.println('\n' + e.getMessage());
						}
					}
					else
					{	System.out.println('\n' + 
							"Ingrediente não removido.");
					}
					
					break;
				}

				case 4:
				{	
					List<Ingrediente> ingredientes = ingredienteDAO.recuperaIngredientes();

					for (Ingrediente ingrediente : ingredientes)
					{	
						System.out.println('\n' + 
							"Id = " + ingrediente.getId() +
							"  Nome = " + ingrediente.getNome() +
							"  Descricao = " + ingrediente.getDescricao() +
							//"  Data Cadastro = " + produto.getDataCadastroMasc() +
							"  Versão = " + ingrediente.getVersao());
					}
					
					break;
				}

				case 5:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Opção inválida!");
			}
		}		
	}
}
