package exercicio;


import java.util.List;

import corejava.Console;

public class Principal
{	public static void main (String[] args) 
	{	
		String nome;
		double lanceMinimo;
		String dataCadastro;
		Ingrediente umIngrediente;

		IngredienteDAO ingredienteDAO = FabricaDeDAOs.getDAO(IngredienteDAO.class);

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voc� deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um ingrediente");
			System.out.println("2. Alterar um ingrediente");
			System.out.println("3. Remover um ingrediente");
			System.out.println("4. Listar todos os ingrediente");
			System.out.println("5. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um n�mero entre 1 e 5:");
					
			switch (opcao)
			{	case 1:
				{
					nome = Console.readLine('\n' + 
						"Informe o nome do ingrediente: ");
						
					umIngrediente = new Ingrediente(nome);
					
					long numero = ingredienteDAO.inclui(umIngrediente);
					
					System.out.println('\n' + "Ingrediente n�mero " +
					    numero + " inclu�do com sucesso!");	

					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o n�mero do ingrediente que voc� deseja alterar: ");
										
					try
					{	umIngrediente = ingredienteDAO.recuperaUmIngrediente(resposta);
					}
					catch(IngredienteNaoEncontradoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}

					System.out.println('\n' + 
						"N�mero = " + umIngrediente.getId() +
						"    Nome = " + umIngrediente.getNome());
						//"    Lance M�nimo = " + umProduto.getLanceMinimo() +
				        //"    Vers�o = " + umProduto.getVersao());
												
					System.out.println('\n' + "O que voc� deseja alterar?");
					System.out.println('\n' + "1. Nome");
					//System.out.println("2. Lance M�nimo");

					int opcaoAlteracao = Console.readInt('\n' + 
											"Digite um n�mero de 1 a 2:");
					
					switch (opcaoAlteracao)
					{	case 1:
							String novoNome = Console.
										readLine("Digite o novo nome: ");
							
							umIngrediente.setNome(novoNome);

							try
							{	ingredienteDAO.altera(umIngrediente);

								System.out.println('\n' + 
									"Altera��o de nome efetuada com sucesso!");
							}
							catch(IngredienteNaoEncontradoException e)
							{	System.out.println('\n' + e.getMessage());
							}
							catch(EstadoDeObjetoObsoletoException e)
							{	System.out.println('\n' + "A opera��o n�o foi " +
							        "efetuada: os dados que voc� " +
							    	"tentou salvar foram modificados " +
							    	"por outro usu�rio.");
							}
								
							break;
					
						/*case 2:
							double novoLanceMinimo = Console.
									readDouble("Digite o novo lance m�nimo: ");
							
							umProduto.setLanceMinimo(novoLanceMinimo);

							try
							{	produtoDAO.altera(umProduto);

								System.out.println('\n' + 
									"Altera��o de lance m�nimo efetuada " +
									"com sucesso!");						
							}
							catch(ProdutoNaoEncontradoException e)
							{	System.out.println('\n' + e.getMessage());
							}
							catch(EstadoDeObjetoObsoletoException e)
							{	System.out.println('\n' + "A opera��o n�o foi " +
							        "efetuada: os dados que voc� " +
							    	"tentou salvar foram modificados " +
							    	"por outro usu�rio.");
							}
								
							break; */

						default:
							System.out.println('\n' + "Op��o inv�lida!");
					}

					break;
				}

				case 3:
				{	int resposta = Console.readInt('\n' + 
						"Digite o n�mero do ingrediente que voc� deseja remover: ");
									
					try
					{	umIngrediente = ingredienteDAO.recuperaUmIngrediente(resposta);
					}
					catch(IngredienteNaoEncontradoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"N�mero = " + umIngrediente.getId() +
						"    Nome = " + umIngrediente.getNome() +
					    "    Vers�o = " + umIngrediente.getVersao());
														
					String resp = Console.readLine('\n' + 
						"Confirma a remo��o do ingrediente?");

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
							"Ingrediente n�o removido.");
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
							//"  Lance m�nimo = " + produto.getLanceMinimo() +
							//"  Data Cadastro = " + produto.getDataCadastroMasc() +
							"  Vers�o = " + ingrediente.getVersao());
					}
					
					break;
				}

				case 5:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Op��o inv�lida!");
			}
		}		
	}
}
