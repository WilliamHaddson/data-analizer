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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.south.analyzer.service.ArquivoRegisterService;

/**
 * Controlador do caminho %HOMEPATH%/data/in
 * 
 * @author William
 *
 */
@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private ArquivoRegisterService arquivoService;
	
	private static boolean rodar;
	public static boolean parado = false;

	/**
	 * Realiza o get na URL {URl}/file, e redireciona para a view html
	 * 
	 * @return
	 */
	@GetMapping
	public ModelAndView fileMonitor() {
		ModelAndView mv = new ModelAndView("FileMonitor");
		
		return mv;
	}
	
	/**
	 * Inicia o monitoramento do caminho %HOMEPATH%/data/in
	 * 
	 * @return
	 */
	@PostMapping("/start")
	public ModelAndView monitorarPasta() {
		rodar = true;
		parado = false;
		ModelAndView mv = new ModelAndView("FileMonitor");
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
					String nomeArquivo = caminhoEvent.toString();
					String tipoArquivo = nomeArquivo.substring(nomeArquivo.length()-4);
					
					boolean isDat = tipoArquivo.equals(".dat") ? true : false;
					
					if(isDat) {
						if(!parado) {
							arquivoService.lerArquivo(nomeArquivo);
						}
					}
					
				}
			} while (visualizador.reset() && rodar);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mv;
	}
	
	/**
	 * Para o monitoramento do caminho %HOMEPATH%/data/in
	 * 
	 * @return
	 */
	@PostMapping("/stop")
	public ModelAndView pararMonitoramento() {
		ModelAndView mv = new ModelAndView("FileMonitor");
		rodar = false;
		parado = true;
		
		return mv;
	}
	
}
