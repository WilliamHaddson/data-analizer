package com.south.analyzer.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.south.analyzer.model.Cliente;
import com.south.analyzer.model.Item;
import com.south.analyzer.model.Venda;
import com.south.analyzer.model.Vendedor;

/**
 * Lê o arquivo, cria o relatório e o salva no caminho %HOMEPATH%/data/out
 * 
 *  @author William
 *
 */
@Component
public class ArquivoRegisterService {

	private List<Vendedor> vendedores;
	private List<Cliente> clientes;
	private List<Venda> vendas;

	/**
	 * Lê um arquivo, no caminho %HOMEPATH%/data/in
	 * Chama os métodos: 
	 * 		private void construirEntidades(String dadosVenda)
	 * 		private String montarRelatorio(List<Vendedor> vendedores, List<Cliente> clientes, List<Venda> vendas)
	 * 		private void criarArquivo(String relatorio, String filename)
	 * 
	 * @param filename
	 */
	public void lerArquivo(String filename) {
		vendedores = new ArrayList<>();
		clientes = new ArrayList<>();
		vendas = new ArrayList<>();
		
		if(filename.contains(".dat")) {	
			try {
				File arquivo = new File(System.getenv("HOMEPATH") + "/data/in/" + filename);
				
				FileInputStream arquivoEntrada = new FileInputStream(arquivo);
				DataInputStream entrada = new DataInputStream(arquivoEntrada);
				String dadosVenda = new String(entrada.readAllBytes(), "UTF-8");
				
				construirEntidades(dadosVenda);
				String relatorio = montarRelatorio(vendedores, clientes, vendas);
				criarArquivo(relatorio, filename);
				
				entrada.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Constrói as entidades, com base nos dados do arquivo capturado
	 * Chama o método:
	 * 		private List<Item> contruirItem(String strItens)
	 * 
	 * @param dadosVenda
	 */
	public void construirEntidades(String dadosVenda) {
		vendedores = new ArrayList<>();
		clientes = new ArrayList<>();
		vendas = new ArrayList<>();
		String[] strings = dadosVenda.split("\n");
		
		for(int i = 0; i < strings.length; i++) {
			String[] propriedades = strings[i].split("ç");
			if(propriedades[0].equals("001")) {
				Vendedor vendedor = new Vendedor();
				vendedor.setCpf(propriedades[1]);
				vendedor.setNome(propriedades[2]);
				vendedor.setSalario(Double.valueOf(propriedades[3]));
				
				vendedores.add(vendedor);
			} else if(propriedades[0].equals("002")) {
				Cliente cliente = new Cliente();
				cliente.setCnpj(propriedades[1]);
				cliente.setNome(propriedades[2]);
				cliente.setAreaTrabalho(propriedades[3]);
				
				clientes.add(cliente);
			} else if(propriedades[0].equals("003")) {
				Venda venda = new Venda();
				venda.setVendaId(Long.valueOf(propriedades[1]));
				venda.setNomeVendedor(propriedades[3]);
				
				venda.setItens(contruirItens(propriedades[2]));
				
				vendas.add(venda);
			}
		}
		
		vendedores = atribuirVendas(vendedores, vendas);
		
	}

	/**
	 * Retorna os itens da venda, com base na string que contém os dados do arquivo
	 * 
	 * @param strItens
	 * @return
	 */
	public List<Item> contruirItens(String strItens) {
		List<Item> itens = new ArrayList<>();
		strItens = strItens.replace("[", "");
		strItens = strItens.replace("]", "");
		String[] itensString = strItens.split(",");
		
		for(int i = 0; i < itensString.length; i++) {
			String[] propriedadesItem = itensString[i].split("-");
			Item item = new Item();
			item.setId(Long.valueOf(propriedadesItem[0]));
			item.setQuantidade(Integer.valueOf(propriedadesItem[1]));
			item.setPreco(Double.valueOf(propriedadesItem[2]));
			
			itens.add(item);
		}
		
		return itens;
	}
	
	/**
	 * Retorna uma lista de vendedores, com suas vendas atribuídas
	 * 
	 * @param vendedores
	 * @param vendas
	 * @return
	 */
	public List<Vendedor> atribuirVendas(List<Vendedor> vendedores, List<Venda> vendas) {
		List<Venda> vendasDoVendedor = new ArrayList<>();
		
		for(Vendedor vendedor: vendedores) {
			for(Venda venda : vendas) {
				if(venda.getNomeVendedor().equals(vendedor.getNome())) {
					vendasDoVendedor.add(venda);
				}
			}
			
			vendedor.setVendas(vendasDoVendedor);
		}
		
		return vendedores;
	}
	
	/**
	 * Retorna o relatório da venda, em uma string
	 * Chama os métodos:
	 * 		private String vendaMaisCara(List<Venda> vendas)
	 * 		private String piorVendedor(List<Vendedor> vendedores)
	 * 
	 * @param vendedores
	 * @param clientes
	 * @param vendas
	 * @return
	 */
	public String montarRelatorio(List<Vendedor> vendedores, List<Cliente> clientes, List<Venda> vendas) {
		StringBuilder relatorio = new StringBuilder();
		relatorio.append("Qtde Clientes => ");
		relatorio.append(clientes.size());
		relatorio.append("\n");
		relatorio.append("Qtde Vendedores => ");
		relatorio.append(vendedores.size());
		relatorio.append("\n");
		relatorio.append("ID venda mais cara => ");
		relatorio.append(vendaMaisCara(vendas));
		relatorio.append("\n");
		relatorio.append("Pior Vendedor => ");
		relatorio.append(piorVendedor(vendedores));
		relatorio.append("\n");
		
		return relatorio.toString();
	}
	
	/**
	 * Retorna a venda mais cara
	 * 
	 * @param vendas
	 * @return
	 */
	public String vendaMaisCara(List<Venda> vendas) {
		Long idVendaMaisCara = 0L;
		double preco = 0;
		
		for(Venda venda : vendas){
			double precoItens = 0;
			
			for(Item item : venda.getItens()) {
				precoItens += item.getPreco();
			}
			
			if(precoItens > preco) {
				preco = precoItens;
				idVendaMaisCara = venda.getVendaId();
			}
		}
		
		return String.valueOf(idVendaMaisCara);
	}
	
	/**
	 * Retorna o pior vendedor
	 * 
	 * @param vendedores
	 * @return
	 */
	public String piorVendedor(List<Vendedor> vendedores) {
		Vendedor pior = new Vendedor();
		pior.setVendas(new ArrayList<>());
		boolean first = true;
		
		for(Vendedor vendedor : vendedores) {
			if(first || vendedor.getVendas().size() < pior.getVendas().size()) {
				pior = vendedor;
				first = false;
			}
		}
		
		StringBuilder piorVendedor = new StringBuilder();
		piorVendedor.append("001");
		piorVendedor.append("ç");
		piorVendedor.append(pior.getCpf());
		piorVendedor.append("ç");
		piorVendedor.append(pior.getNome());
		piorVendedor.append("ç");
		piorVendedor.append(pior.getSalario());
		piorVendedor.append(" com ");
		piorVendedor.append(pior.getVendas().size());
		piorVendedor.append(" vendas");
		
		return piorVendedor.toString();
	}

	/**
	 * Cria o arquivo {filename}.done.dat, no caminho %HOMEPATH%/data/out
	 * 
	 * @param relatorio
	 * @param filename
	 */
	public void criarArquivo(String relatorio, String filename) {
		filename = filename.replace(".dat", ".done.dat");
		
		try {
			File arquivo = new File(System.getenv("HOMEPATH") + "/data/out/" + filename);
			FileOutputStream arquivoSaida;
			arquivoSaida = new FileOutputStream(arquivo);
			DataOutputStream outStream = new DataOutputStream(arquivoSaida);
			
			outStream.write(relatorio.getBytes());
			outStream.close();
			arquivoSaida.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
