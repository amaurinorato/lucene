package br.com.fiap.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;

import br.com.fiap.model.Resultado;

@Stateful
public class ResultadoDAO {

	public static Map<String, List<Resultado>> resutados = new HashMap<String, List<Resultado>>();
	
	
}
