package com.south.analyzer.controller;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.south.analyzer.service.ArquivoRegisterService;

/**
 * Controlador do caminho %HOMEPATH%/data/in
 * 
 * @author William
 *
 */
@Component
public class FileController {

	@Autowired
	private ArquivoRegisterService arquivoService;

	/**
	 * Monitora o caminho %HOMEPATH%/data/in
	 */
	public void monitorarPasta() {
		try (WatchService servico = FileSystems.getDefault().newWatchService()){
			
			Map<WatchKey, Path> mapa = new HashMap<>();
			Path caminho = Paths.get(System.getenv("HOMEPATH") + "/data/in");
			mapa.put(caminho.register(servico, 
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY),
					caminho);
			
			WatchKey visualizador;
			
			do {
				
				visualizador = servico.take();
				for (WatchEvent<?> event : visualizador.pollEvents()) {
					Path caminhoEvent = (Path) event.context();
					
					arquivoService.lerArquivo(caminhoEvent.toString());
					
				}
			} while (visualizador.reset());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicia uma thread que fará o monitoramento do caminho, paralelo ao funcionamento interno do spring boot
	 */
	@PostConstruct
	public void monitorar() {
		new Thread() {
			@Override
			public void run() {
				monitorarPasta();
			}
		}.start();
	}
	
}
