package com.south.analyzer.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Venda {

	@Getter
	private static final Long ID = 003L;
	
	private Long vendaId;
	private String nomeVendedor;
	
	private List<Item> itens;

}
