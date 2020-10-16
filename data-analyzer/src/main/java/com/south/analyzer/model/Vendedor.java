package com.south.analyzer.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vendedor {
	
	@Getter
	private static final Long ID = 001L;
	
	private String cpf;
	private String nome;
	private Double salario;
	private List<Venda> vendas;

}
