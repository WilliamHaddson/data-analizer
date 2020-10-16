package com.south.analyzer.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Cliente
 * 
 * @author William
 *
 */
@Getter
@Setter
public class Cliente {

	/**
	 * ID padrão de Clientes
	 */
	@Getter
	private static final Long ID = 002L;
	
	
	/**
	 * CNPJ do cliente
	 */
	private String cnpj;
	
	/**
	 * Nome do cliente
	 */
	private String nome;
	
	/**
	 * Área de trabalho do cliente
	 */
	private String areaTrabalho;

}
